package com.copy.kiascreen.registry.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.activity.OnBackPressedCallback
import com.copy.kiascreen.R
import com.copy.kiascreen.databinding.FragmentServiceTermBinding
import com.copy.kiascreen.registry.RegisterAgreeActivity
import com.copy.kiascreen.registry.RegisterTerm
import com.copy.kiascreen.util.OnBackPressedListener
import com.copy.kiascreen.util.fragment.BaseFragment

class FragmentInfoServiceTerm : BaseFragment<FragmentServiceTermBinding>(), OnBackPressedListener {
    private var registerInterface : RegisterTerm? = null

    override fun inflateViewBinding(inflater: LayoutInflater, container: ViewGroup?) = FragmentServiceTermBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setClickListener()
    }

    private fun setClickListener() {
        binding.agreeBtn.setOnClickListener {
            // activity에 동의버튼을 클릭시켜준다.
            registerInterface?.let {
                closeFragment()
                it.onAgreeClick(1)
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        try {
            registerInterface = context as RegisterTerm

        } catch (e : Exception) {
            Log.d("FragmentInfoServiceTerm", "onAttach class Type Exception ${e.message}")
        }
    }

    override fun onDetach() {
        super.onDetach()
    }

    override fun onBackPressed() {
        Log.d("FragmentInfoServiceTerm", "onBackPressed called")
        closeFragment()
    }

    private fun closeFragment() {
        activity?.let {
            it.findViewById<FrameLayout>(R.id.term_frame)?.isClickable = false
            it.supportFragmentManager?.beginTransaction()?.remove(this)?.commit()
        }
    }


}