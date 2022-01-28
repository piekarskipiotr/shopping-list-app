package com.apps.bacon.shoppinglistapp.ui.grocery

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.apps.bacon.shoppinglistapp.R
import com.apps.bacon.shoppinglistapp.databinding.DialogInsertGroceryBinding
import com.apps.bacon.shoppinglistapp.utils.AmountValidation
import com.apps.bacon.shoppinglistapp.utils.Button
import com.apps.bacon.shoppinglistapp.utils.NameValidation
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint
import java.lang.Exception
import javax.inject.Inject
import javax.inject.Named

@AndroidEntryPoint
class GroceryDialog : BottomSheetDialogFragment() {
    @Inject
    @Named("name_error")
    lateinit var notValidNameErrorMessage: String
    @Inject
    @Named("amount_error")
    lateinit var notValidAmountErrorMessage: String
    private lateinit var binding: DialogInsertGroceryBinding
    private lateinit var listener: GroceryDialogListener
    private var isNameValid = false
    private var isAmountValid = false

    fun newInstance(): GroceryDialog {
        return GroceryDialog()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.setOnShowListener { dialog ->
            val bottomSheetDialog = dialog as BottomSheetDialog
            bottomSheetDialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
            initValidation()

            binding.addNewGroceryButton.setOnClickListener {
                val groceryName = binding.groceryNameTextInput.text.toString().trim()
                val amount = binding.amountTextInput.text.toString().toInt()
                listener.onInsertButtonClick(groceryName, amount)

                Toast.makeText(context, getString(R.string.grocery_added), Toast.LENGTH_SHORT).show()
                bottomSheetDialog.dismiss()
            }

            // set button state on view created or recreated
            setAddButtonState(
                NameValidation.validate(binding.groceryNameTextInput.text) && NameValidation.validate(binding.amountTextInput.text)
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogInsertGroceryBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = context as GroceryDialogListener
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun initValidation() {
        binding.groceryNameTextInput.onTextChanged {
            when(NameValidation.validate(it)) {
                true -> {
                    isNameValid = true
                    setNameErrorMessage(null)
                }
                false -> {
                    isNameValid = false
                    setNameErrorMessage(notValidNameErrorMessage)
                }
            }

            setAddButtonState(isNameValid && isAmountValid)
        }

        binding.amountTextInput.onTextChanged {
            when(AmountValidation.validate(it)) {
                true -> {
                    isAmountValid = true
                    setAmountErrorMessage(null)
                }
                false -> {
                    isAmountValid = false
                    setAmountErrorMessage(notValidAmountErrorMessage)
                }
            }

            setAddButtonState(isAmountValid && isAmountValid)
        }
    }

    private fun setNameErrorMessage(string: String?) {
        binding.groceryNameTextInputLayout.error = string
    }

    private fun setAmountErrorMessage(string: String?) {
        binding.amountTextInputLayout.error = string
    }

    private fun setAddButtonState(isEnabled: Boolean) {
        if(isEnabled) {
            binding.addNewGroceryButton.apply {
                isClickable = true
                alpha = Button.State.Enable.alpha
            }
        } else {
            binding.addNewGroceryButton.apply {
                isClickable = false
                alpha = Button.State.Disable.alpha
            }
        }
    }

    private fun TextInputEditText.onTextChanged(onTextChanged: (CharSequence?) -> Unit) {
        this.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                onTextChanged.invoke(p0)
            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })
    }

    interface GroceryDialogListener {
        fun onInsertButtonClick(groceryName: String, amount: Int)
    }
}