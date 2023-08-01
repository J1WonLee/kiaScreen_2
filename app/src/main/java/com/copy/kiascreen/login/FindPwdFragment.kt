package com.copy.kiascreen.login

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context.CLIPBOARD_SERVICE
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels

import com.copy.kiascreen.databinding.FragmentFindPwdfragmentBinding
import com.copy.kiascreen.repeatOnStarted
import com.copy.kiascreen.room.RoomUtil
import com.copy.kiascreen.room.successOrNull
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FindPwdFragment : DialogFragment() {
    private lateinit var binding : FragmentFindPwdfragmentBinding
    private val viewModel : LoginViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentFindPwdfragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        setDialog()
    }

    private fun setDialog() {
        dialog?.let {
            it.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            it.window?.setLayout(
                ConstraintLayout.LayoutParams.MATCH_PARENT,
                ConstraintLayout.LayoutParams.MATCH_PARENT
            )
        }

        var params = dialog?.window?.attributes

        dialog?.window?.attributes = params!!
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setClickListener()
        setPwdEnableCopy()
        repeatOnStarted {
            viewModel.resetPwdSharedFlow.collect {
                handleResetPwdResult(it)
            }
        }
        setOnKeyListener()
    }

    private fun setClickListener() {
        binding.idFindBtn.setOnClickListener {
            if (binding.mailInputEditText.text!!.isNotEmpty() && binding.idInputText.text!!.isNotEmpty()) {
                Log.d("pwdTest", "findPwd!! started")
                viewModel.findPwd( binding.idInputText.text.toString().trim() , binding.mailInputEditText.text.toString().trim())
            }
        }

        binding.dialogCloseImg.setOnClickListener {
            dismiss()
        }

    }

    private fun handleResetPwdResult(result : RoomUtil<*>) {
        when(result) {
            is RoomUtil.Loading -> Log.d("loginTest", "onLoading!!")
            is RoomUtil.Success -> {
                if (result.successOrNull() != null) {
                    val newPwd = (result.data as String)
                    Log.d("loginTest", "onLoading!! newPwd = $newPwd")
                    binding.showIdTv.visibility = View.VISIBLE
                    binding.showIdTv.text = newPwd
                }
                else {
                    binding.showIdTv.visibility = View.VISIBLE
                    binding.showIdTv.text = "아이디와 비밀번호를 다시확인해 주세요"
                }
            }
            is RoomUtil.Error -> {
                Log.d("loginTest", "error!!" )
            }
        }
    }

    private fun setPwdEnableCopy() {
        binding.showIdTv.setOnLongClickListener {
            if (binding.showIdTv.visibility == View.VISIBLE) {
                var text = binding.showIdTv.text.toString()
                val clipboardManager = requireActivity().getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
                val clipData = ClipData.newPlainText("message", text)

                clipboardManager.setPrimaryClip(clipData)
                Toast.makeText(requireContext(), "클립보드에 복사되었습니다", Toast.LENGTH_SHORT).show()
            }

            false
        }
    }

    private fun setOnKeyListener() {
        binding.mailInputEditText.setOnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN) {
                if (binding.mailInputEditText.text!!.isNotEmpty() && binding.idInputText.text!!.isNotEmpty()) {
                    Log.d("pwdTest", "findPwd!! started")
                    viewModel.findPwd(
                        binding.idInputText.text.toString().trim(),
                        binding.mailInputEditText.text.toString().trim()
                    )
                }
                true
            }
            false
        }
    }



}