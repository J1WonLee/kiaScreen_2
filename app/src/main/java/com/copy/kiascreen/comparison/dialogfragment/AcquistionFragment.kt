package com.copy.kiascreen.comparison.dialogfragment

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import com.copy.kiascreen.databinding.DialogAcquisitionGuideBinding
import com.copy.kiascreen.util.fragment.BaseDialogFragmentNoVM

class AcquistionFragment : BaseDialogFragmentNoVM<DialogAcquisitionGuideBinding>() {
    override fun setWindowParams() {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.setLayout(
            ConstraintLayout.LayoutParams.WRAP_CONTENT,
            ConstraintLayout.LayoutParams.WRAP_CONTENT
        )
    }

    override fun inflateViewBinding(inflater: LayoutInflater, container: ViewGroup?) = DialogAcquisitionGuideBinding.inflate(inflater, container, false)
}