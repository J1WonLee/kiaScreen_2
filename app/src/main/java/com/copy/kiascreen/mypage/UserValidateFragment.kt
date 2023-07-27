package com.copy.kiascreen.mypage

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.DialogFragment
import com.copy.kiascreen.databinding.FragmentUserValidationBinding
import com.copy.kiascreen.roomVo.User

// 개인 정보 수정 페이지 들어가기전에 유저 비밀번호 확인
class UserValidateFragment : DialogFragment() {
    private lateinit var binding : FragmentUserValidationBinding
    private var user : User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        user = arguments?.getParcelable<User>("user")
    }

    override fun onStart() {
        super.onStart()
        setDialog()
    }

    private fun setDialog() {
        dialog?.let {
            it.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            it.window?.setLayout(
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
            )
        }

        var params = dialog?.window?.attributes

        dialog?.window?.attributes = params!!
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentUserValidationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        validateClick()
        setKeyDownListener()
    }

    private fun validateClick() {
        binding.validateBtn.setOnClickListener {
            chkPwd()
        }
    }

    private fun chkPwd() {
        val pwd = binding.validtePwdInputEditText.text.toString().trim()
        if (pwd == user?.pwd) {
            // activity 로 이동
            Toast.makeText(requireContext(), "validate!!", Toast.LENGTH_SHORT).show()
            Intent(requireActivity(), UserInfoActivity::class.java).apply {
                putExtra("user", user)
                startActivity(this)
                dismiss()
            }
        }
        else {
            Toast.makeText(requireContext(), "회원 정보가 일치하지 않습니다", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setKeyDownListener() {
        binding.validtePwdInputEditText.setOnKeyListener { v, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                chkPwd()
                true
            }
            false
        }
    }

    companion object {
        fun newInstance(user : User) : UserValidateFragment {
            val args = Bundle().apply {
                this.putParcelable("user", user)
            }

            return UserValidateFragment().apply {
                arguments = args
            }
        }
    }
}