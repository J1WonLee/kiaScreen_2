package com.copy.kiascreen.comparison.dialogfragment

import android.content.Context
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.copy.kiascreen.R
import com.copy.kiascreen.custom.layout.LayoutDialogContent
import com.copy.kiascreen.databinding.DialogInsuranceGradeGuideBinding
import com.copy.kiascreen.util.fragment.BaseDialogFragmentNoVM

class InsuranceGradeFragment : BaseDialogFragmentNoVM<DialogInsuranceGradeGuideBinding>() {
    private lateinit var layout1 : LayoutDialogContent
    private lateinit var layout2 : LayoutDialogContent

    override fun setWindowParams() {
        Log.d("DialogTest", "setWindowParams caled")

        val size = Point()
        val windowManager = requireContext().getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = windowManager.defaultDisplay

        display.getSize(size)
        val deviceWidth = size.x

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val params: ViewGroup.LayoutParams? = dialog?.window?.attributes
        params?.width = (deviceWidth * 0.9).toInt()
        params?.height = (size.y * 0.9).toInt()

        dialog?.window?.attributes = params as WindowManager.LayoutParams
    }

    override fun inflateViewBinding(inflater: LayoutInflater, container: ViewGroup?) =  DialogInsuranceGradeGuideBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        setText()
        closeDialog()
    }

    private fun initView() {
        layout1 = binding.safetyContent1
        layout2 = binding.safetyContent2

    }

    private fun setText() {
        layout1.setLayoutText(R.string.text_insurance_grade_content_1)
        layout2.setLayoutText(R.string.text_insurance_grade_content_2)

    }

    private fun closeDialog() {
        binding.dialogCloseImg.setOnClickListener {
            this.dismiss()
        }
    }

}