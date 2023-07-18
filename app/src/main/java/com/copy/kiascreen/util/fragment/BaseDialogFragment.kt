package com.copy.kiascreen.util.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModel
import androidx.viewbinding.ViewBinding

abstract class BaseDialogFragment <B : ViewBinding, VB : ViewModel?> : DialogFragment() {
    private var _binding : B? = null
    abstract val viewModel : VB

    val binding
        get() =  _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = inflateViewBinding(inflater, container)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        setWindowParams()
    }

    abstract fun setWindowParams()

    abstract fun inflateViewBinding(inflater: LayoutInflater, container: ViewGroup?) : B

    override fun onDestroyView() {
        super.onDestroyView()
    }
}