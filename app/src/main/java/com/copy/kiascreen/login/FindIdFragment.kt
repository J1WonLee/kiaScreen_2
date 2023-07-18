package com.copy.kiascreen.login

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.copy.kiascreen.databinding.FragmentFindIdragmentBinding
import com.copy.kiascreen.repeatOnStarted
import com.copy.kiascreen.room.RoomUtil
import com.copy.kiascreen.room.successOrNull
import com.copy.kiascreen.roomVo.User
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FindIdFragment : DialogFragment() {
    private lateinit var binding : FragmentFindIdragmentBinding
    private val viewModel : LoginViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {}
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentFindIdragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnFindBtnClick()
        repeatOnStarted {
            viewModel.findIdSharedFlow.collect {
                handleFindIdResult(it)
            }
        }
        setOnKeyListener()
    }

    private fun setOnFindBtnClick() {
        binding.idFindBtn.setOnClickListener {
            if (binding.mailInputEditText.text!!.isNotEmpty()) {
                viewModel.findId(binding.mailInputEditText.text.toString())
            }
        }
    }

    private fun setOnKeyListener() {
        binding.mailInputEditText.setOnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN) {
                if (binding.mailInputEditText.text!!.isNotEmpty()) {
                    viewModel.findId(binding.mailInputEditText.text.toString())
                }
                true
            }
            false
        }
    }

    private fun handleFindIdResult(result : RoomUtil<*>) {
        when(result) {
            is RoomUtil.Loading -> Log.d("loginTest", "onLoading!!")
            is RoomUtil.Success -> {
                if (result.successOrNull() != null) {
                    val user = (result.data as User)
                    binding.showIdTv.visibility = View.VISIBLE
                    binding.showIdTv.text = user.id
                }
                else {
                    binding.showIdTv.visibility = View.VISIBLE
                    binding.showIdTv.text = "일치하는 아이디가 없습니다"
                }
            }
            is RoomUtil.Error -> {

            }
        }
    }

    companion object {

        fun newInstance(param1: String, param2: String) = FindIdFragment()
    }
}