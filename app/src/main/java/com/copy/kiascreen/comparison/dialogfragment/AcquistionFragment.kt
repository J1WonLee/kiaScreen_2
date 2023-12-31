package com.copy.kiascreen.comparison.dialogfragment

import android.content.Context
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.constraintlayout.widget.ConstraintLayout
import com.copy.kiascreen.databinding.DialogAcquisitionGuideBinding
import com.copy.kiascreen.util.fragment.BaseDialogFragmentNoVM

// 취득세 dialog
class AcquistionFragment : BaseDialogFragmentNoVM<DialogAcquisitionGuideBinding>() {
    override fun setWindowParams() {
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
    override fun inflateViewBinding(inflater: LayoutInflater, container: ViewGroup?) = DialogAcquisitionGuideBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        closeDialog()
    }

    private fun closeDialog() {
        binding.dialogCloseImg.setOnClickListener {
            this.dismiss()
        }
    }
}