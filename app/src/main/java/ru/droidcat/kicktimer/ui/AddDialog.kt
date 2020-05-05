package ru.droidcat.kicktimer.ui

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.core.widget.doOnTextChanged
import androidx.databinding.DataBindingUtil
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.add_dialog.*
import ru.droidcat.kicktimer.R
import ru.droidcat.kicktimer.databinding.AddDialogBinding

class AddDialog(
        val label: String,
        val hintText: String,
        val buttonText: String,
        val errorText: String,
        val addDialogCallback: AddDialogCallback
): BottomSheetDialogFragment() {

    override fun getTheme(): Int = R.style.BottomSheetDialogTheme

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog = BottomSheetDialog(requireContext(), theme)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: AddDialogBinding = DataBindingUtil.inflate(inflater, R.layout.add_dialog, container, false)
        val view = binding.root
        binding.label = label
        binding.hintText = hintText
        binding.buttonText = buttonText
        binding.executePendingBindings()
        
        val inputText = view.findViewById<TextInputEditText>(R.id.add_dialog_textinputedittext)
        val inputLayout = view.findViewById<TextInputLayout>(R.id.add_dialog_textinputlayout)
        val confirmButton = view.findViewById<MaterialButton>(R.id.add_dialog_confirm)
        inputText.doAfterTextChanged {
            inputLayout.error = null
        }
        confirmButton.setOnClickListener {
            val text = inputLayout.editText?.text.toString()
            if(text.isEmpty()) {
                inputLayout.error = errorText
            }
            else {
                addDialogCallback.onResult(text)
                dismiss()
            }
        }
        
        return view
    }
}

class AddDialogCallback(val callback: (text: String?) -> Unit) {
    fun onResult(text: String?) = callback(text)
}