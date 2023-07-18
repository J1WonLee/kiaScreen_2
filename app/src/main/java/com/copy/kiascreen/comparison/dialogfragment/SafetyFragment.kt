package com.copy.kiascreen.comparison.dialogfragment

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import com.copy.kiascreen.R
import com.copy.kiascreen.custom.layout.LayoutDialogContent
import com.copy.kiascreen.databinding.DialogSafetyGuideBinding
import com.copy.kiascreen.util.fragment.BaseDialogFragmentNoVM

class SafetyFragment : BaseDialogFragmentNoVM<DialogSafetyGuideBinding>() {
    private lateinit var layout1 : LayoutDialogContent
    private lateinit var layout2 : LayoutDialogContent
    private lateinit var layout3 : LayoutDialogContent
    private lateinit var layout4 : LayoutDialogContent

    override fun setWindowParams() {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.setLayout(
            ConstraintLayout.LayoutParams.WRAP_CONTENT,
            ConstraintLayout.LayoutParams.WRAP_CONTENT
        )
    }

    override fun inflateViewBinding(inflater: LayoutInflater, container: ViewGroup?) = DialogSafetyGuideBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        setText()
    }

    private fun initView() {
        layout1 = binding.safetyContent1
        layout2 = binding.safetyContent2
        layout3 = binding.safetyContent3
        layout4 = binding.safetyContent4
    }

    private fun setText() {
        layout1.setLayoutText(R.string.text_safety_content_1)
        layout2.setLayoutText(R.string.text_safety_content_2)
        layout3.setLayoutText(R.string.text_safety_content_3)
        layout4.setLayoutText(R.string.text_safety_content_4)
    }


}