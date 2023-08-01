package com.copy.kiascreen.util.textwatcher

import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.widget.EditText
import com.copy.kiascreen.databinding.ActivityMemberRegisterBinding
import java.util.regex.Pattern

// 회원가입, 회원수정, 검색창  textWatcher 제공 class
// id, pwd, mail textWatcher
class TextWatcherHelper (textInterface : TextWatcherInterface) {
    private var mInterface = textInterface

    // 아이디의 시작은 영어만
    private val inputFilterId by lazy {
        InputFilter { source, start, end, dest, dstart, dend ->
            val ps = Pattern.compile("^[a-z]+$")
            if (!ps.matcher(source).matches()) {
                return@InputFilter ""
            }
            else {
                return@InputFilter null
            }
        }
    }

    // 아이디는 영문자 숫자 조합
    private val inputFilterId2 by lazy {
        InputFilter { source, start, end, dest, dstart, dend ->
            val ps = Pattern.compile("^[a-z0-9]*$")
            if (!ps.matcher(source).matches()) {
                return@InputFilter ""
            } else {
                return@InputFilter null
            }
        }
    }

    // 메일 도메인은 영문자 + '.'
    private val mailDomainFilter by lazy {
        InputFilter { source, start, end, dest, dstart, dend ->
            val ps = Pattern.compile("^[a-z.]*$")
            if (!ps.matcher(source).matches()) {
                return@InputFilter ""
            } else {
                return@InputFilter null
            }
        }
    }

    // 메일 아이디 첫글자는 반드시 영어
    private val mailIdFilter by lazy {
        InputFilter { source, start, end, dest, dstart, dend ->
            val ps = Pattern.compile("^[a-z]+$")
            if (!ps.matcher(source).matches()) {
                return@InputFilter ""
            } else {
                return@InputFilter null
            }
        }
    }

    // 첫글자 이후는 영어 + 숫자
    private val mailIdFilter2 by lazy {
        InputFilter { source, start, end, dest, dstart, dend ->
            val ps = Pattern.compile("^[a-z\\d]+$")
            if (!ps.matcher(source).matches()) {
                return@InputFilter ""
            } else {
                return@InputFilter null
            }
        }
    }

    // 비밀번호는 영문자와 특수문자만
    private val pwdFilter by lazy {
        InputFilter { source, start, end, dest, dstart, dend ->
            val ps = Pattern.compile("^[a-zA-Z0-9\\d!@#$%^&]+$")
            if (!ps.matcher(source).matches()) {
                return@InputFilter ""
            } else {
                return@InputFilter null
            }
        }
    }



    // id editText textWatcher
    fun setIdWatcher(target : EditText) {
        target.filters = arrayOf(inputFilterId) // 처음 시작 시, 필터를 설정 해 줘야 함
        target.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                mInterface.chkDupId()

                if (count == 0 && target.text!!.isEmpty()) {
                    target.filters = arrayOf(inputFilterId)
                }
                else if (count >= 1 || target.text!!.isNotEmpty()) {
                    target.filters = arrayOf(inputFilterId2)
                }
            }

            override fun afterTextChanged(s: Editable?) { }

        })
    }

    // pwd editText textWatcher
    fun setPwdWatcher(target: EditText) {
        target.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                mInterface.chkPwd()
            }

            override fun afterTextChanged(s: Editable?) { }

        })
    }

    // mail editText textWatcher
    fun setMailIdWatcher(target : EditText) {
        target.filters = arrayOf(mailIdFilter)
        target.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (count == 0 && target.text!!.isEmpty()) {
                    target.filters = arrayOf(mailIdFilter)
                }
                else if (count >= 1) {
                    target.filters = arrayOf(mailIdFilter2)
                }
            }

            override fun afterTextChanged(s: Editable?) { }

        })
    }

    // mail (domain) Address textWatcher
    fun setMailAddrWatcher(target : EditText) {
        target.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (target.text!!.isNotEmpty()) {
                    target.filters = arrayOf(mailDomainFilter)
                }
            }

            override fun afterTextChanged(s: Editable?) { }
        })
    }

    // 검색 editText textWatcher
    fun setSearchWatcher(target : EditText) {
        target.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                TODO("Not yet implemented")
            }

            override fun afterTextChanged(s: Editable?) {
                TODO("Not yet implemented")
            }

        })
    }
}