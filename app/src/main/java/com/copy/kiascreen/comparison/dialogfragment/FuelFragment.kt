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
import com.copy.kiascreen.databinding.DialogFuelGuideBinding
import com.copy.kiascreen.util.fragment.BaseDialogFragmentNoVM

class FuelFragment : BaseDialogFragmentNoVM<DialogFuelGuideBinding>() {
    private lateinit var layout1 : LayoutDialogContent
    private lateinit var layout2 : LayoutDialogContent
    private lateinit var layout3 : LayoutDialogContent
    private lateinit var layout4 : LayoutDialogContent
    private lateinit var layout5 : LayoutDialogContent
    private lateinit var layout6 : LayoutDialogContent
    private lateinit var layout7 : LayoutDialogContent
    private lateinit var layout8 : LayoutDialogContent

    override fun setWindowParams() {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.setLayout(
            ConstraintLayout.LayoutParams.WRAP_CONTENT,
            ConstraintLayout.LayoutParams.WRAP_CONTENT
        )
    }

    override fun inflateViewBinding(inflater: LayoutInflater, container: ViewGroup?) = DialogFuelGuideBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        setText()
    }

    private fun initView() {
        layout1 = binding.fuelContent1
        layout2 = binding.fuelContent2
        layout3 = binding.fuelContent3
        layout4 = binding.fuelContent4
        layout5 = binding.fuelContent5
        layout6 = binding.fuelContent6
        layout7 = binding.fuelContent7
        layout8 = binding.fuelContent8
    }

    private fun setText() {
        layout1.setLayoutText(R.string.text_fuel_guide_1)
        layout2.setLayoutText(R.string.text_fuel_guide_2)
        layout3.setLayoutText(R.string.text_fuel_guide_5)
        layout4.setLayoutText(R.string.text_fuel_guide_6)
        layout5.setLayoutText(R.string.text_fuel_guide_7)
        layout6.setLayoutText(R.string.text_fuel_guide_8)
        layout7.setLayoutText(R.string.text_fuel_guide_9)
        layout8.setLayoutText(R.string.text_fuel_guide_10)
    }
}