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
import androidx.constraintlayout.widget.ConstraintLayout
import com.copy.kiascreen.R
import com.copy.kiascreen.custom.layout.LayoutDialogContent
import com.copy.kiascreen.databinding.DialogInsuranceGuideBinding
import com.copy.kiascreen.util.fragment.BaseDialogFragment
import com.copy.kiascreen.util.fragment.BaseDialogFragmentNoVM

class InsuranceFragment : BaseDialogFragmentNoVM<DialogInsuranceGuideBinding> () {
    private lateinit var layout1 : LayoutDialogContent
    private lateinit var layout2 : LayoutDialogContent
    private lateinit var layout3 : LayoutDialogContent
    private lateinit var layout4 : LayoutDialogContent
    private lateinit var layout5 : LayoutDialogContent
    private lateinit var layout6 : LayoutDialogContent
    private lateinit var layout7 : LayoutDialogContent
    private lateinit var layout8 : LayoutDialogContent


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

        /*
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.setLayout(
            ConstraintLayout.LayoutParams.WRAP_CONTENT,
            ConstraintLayout.LayoutParams.WRAP_CONTENT
        )

         */
    }

    override fun inflateViewBinding(inflater: LayoutInflater, container: ViewGroup?) = DialogInsuranceGuideBinding.inflate(inflater, container ,false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        layout1 = binding.insuranceContent1
        layout2 = binding.insuranceContent2
        layout3 = binding.insuranceContent3
        layout4 = binding.insuranceContent4
        layout5 = binding.insuranceContent5
        layout6 = binding.insuranceContent6
        layout7 = binding.insuranceContent7
        layout8 = binding.insuranceContent8
        layout1.setLayoutText(R.string.text_insurance_guide_1)
        layout2.setLayoutText(R.string.text_insurance_guide_2)
        layout3.setLayoutText(R.string.text_insurance_guide_3)
        layout4.setLayoutText(R.string.text_insurance_guide_4)
        layout5.setLayoutText(R.string.text_insurance_guide_5)
        layout6.setLayoutText(R.string.text_insurance_guide_6)
        layout7.setLayoutText(R.string.text_insurance_guide_7)
        layout8.setLayoutText(R.string.text_insurance_guide_8)

        closeDialog()
    }

    private fun closeDialog() {
        binding.dialogCloseImg.setOnClickListener {
            this.dismiss()
        }
    }
}