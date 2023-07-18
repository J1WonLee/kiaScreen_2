package com.copy.kiasample.coach

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.copy.kiascreen.R
import com.copy.kiascreen.application.KiaSampleApplication
import com.copy.kiascreen.comparison.BuildCompActivity
import com.copy.kiascreen.databinding.CoachPager3Binding
import com.copy.kiascreen.roomVo.User
import com.copy.kiascreen.util.fragment.BaseFragment


class CoachFragment03 : BaseFragment<CoachPager3Binding>() {
    var user : User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        user = arguments?.getParcelable<User?>("user")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.startBtn.setOnClickListener {
            Intent(requireContext(), BuildCompActivity::class.java).apply {
                KiaSampleApplication.prefs.coachFlag = true
                user?.let {
                    putExtra("user", user)
                }
                requireContext().startActivity(this)
                requireActivity().finish()
//                requireActivity().overridePendingTransition(R.anim.activity_fade_in, R.anim.activity_fade_out)
            }
        }
    }

    override fun inflateViewBinding(inflater: LayoutInflater, container: ViewGroup?): CoachPager3Binding {
        return CoachPager3Binding.inflate(inflater, container, false)
    }

    companion object {

        @JvmStatic
        fun newInstance(user : User) : CoachFragment03 {
            val args = Bundle().apply {
                putParcelable("user", user)
            }

            return CoachFragment03().apply {
                arguments = args
            }
        }
    }
}