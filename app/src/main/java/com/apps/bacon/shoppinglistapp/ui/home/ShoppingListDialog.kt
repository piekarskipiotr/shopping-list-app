package com.apps.bacon.shoppinglistapp.ui.home

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.apps.bacon.shoppinglistapp.R
import com.apps.bacon.shoppinglistapp.databinding.DialogInsertShoppingListBinding
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
class ShoppingListDialog : BottomSheetDialogFragment() {
    @Inject
    @Named("name_error")
    lateinit var notValidNameErrorMessage: String
    private lateinit var binding: DialogInsertShoppingListBinding
    private lateinit var listener: ShoppingListDialogListener
    private var isNameValid = false

    fun newInstance(): ShoppingListDialog {
        return ShoppingListDialog()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.setOnShowListener { dialog ->
            val bottomSheetDialog = dialog as BottomSheetDialog
            bottomSheetDialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
            initValidation()

            binding.addNewShoppingListButton.setOnClickListener {
                val shoppingListName = binding.nameTextInput.text.toString().trim()
                listener.onInsertButtonClick(shoppingListName)

                Toast.makeText(context, getString(R.string.shopping_list_added), Toast.LENGTH_SHORT).show()
                bottomSheetDialog.dismiss()
            }

            //set button state on view created or recreated
            setAddButtonState(NameValidation.validate(binding.nameTextInput.text))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogInsertShoppingListBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = context as ShoppingListDialogListener
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun initValidation() {
        binding.nameTextInput.onTextChanged {
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

            setAddButtonState(isNameValid)
        }
    }

    private fun setAddButtonState(isEnabled: Boolean) {
        if(isEnabled) {
            binding.addNewShoppingListButton.apply {
                isClickable = true
                alpha = Button.State.Enable.alpha
            }
        } else {
            binding.addNewShoppingListButton.apply {
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

    private fun setNameErrorMessage(string: String?) {
        binding.nameTextInputLayout.error = string
    }

    interface ShoppingListDialogListener {
        fun onInsertButtonClick(shoppingListName: String)
    }
}