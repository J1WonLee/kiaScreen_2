package com.copy.kiascreen.registry.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.DialogFragment
import com.copy.kiascreen.databinding.FragmentKiaServiceTermBinding
import com.copy.kiascreen.databinding.FragmentServiceTermBinding
import com.copy.kiascreen.util.fragment.BaseDialogFragment

class FragmentInfoTerm : DialogFragment() {
    private lateinit var binding : FragmentKiaServiceTermBinding

    override fun onStart() {
        super.onStart()
        setWindowParams()
        this.isCancelable = false
    }

    private fun setWindowParams() {
        Log.d("DialogTest", "setWindowParams caled")
        dialog?.let {
            it.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            it.window?.setLayout(
                ConstraintLayout.LayoutParams.MATCH_PARENT,
                1700
            )
        }

        var params = dialog?.window?.attributes

        dialog?.window?.attributes = params!!
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentKiaServiceTermBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.agreeBtn.setOnClickListener {
            this.dismiss()
        }
    }
}