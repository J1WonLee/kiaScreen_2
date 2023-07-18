package com.copy.kiascreen.menu

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatEditText
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.copy.kiascreen.R
import com.copy.kiascreen.databinding.FragmentSearchBinding
import com.copy.kiascreen.menu.adapter.SearchAdapter
import com.copy.kiascreen.menu.vm.SearchViewModel
import com.copy.kiascreen.menu.vo.MenuSearchItem
import com.copy.kiascreen.toggleVisibility
import com.copy.kiascreen.util.fragment.BaseDialogFragment
import com.google.android.material.tabs.TabLayout
import java.util.*

class FragmentSearch : BaseDialogFragment<FragmentSearchBinding, SearchViewModel>() {
    override val viewModel: SearchViewModel by viewModels()

    private lateinit var tabLayout : TabLayout
    private lateinit var recyclerView: RecyclerView
    private lateinit var editTExt : AppCompatEditText
    private lateinit var recWrapper : ConstraintLayout
    private lateinit var searchResultWrapper : LinearLayout
    private lateinit var searchRecyclerView: RecyclerView

    private var selectedTab = 0
    var searchAdapter : SearchAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
    }

    override fun setWindowParams() {
        Log.d("DialogTest", "setWindowParams caled")
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.setLayout(
            ConstraintLayout.LayoutParams.MATCH_PARENT,
            ConstraintLayout.LayoutParams.WRAP_CONTENT
        )

        dialog?.window?.setGravity(Gravity.TOP)

        var params = dialog?.window?.attributes
        params?.y = 0

        dialog?.window?.attributes = params!!
    }

    override fun inflateViewBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentSearchBinding {
        return FragmentSearchBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initView()
        setAdapter()
        setTabListener()
        setClickListener()
        setKeyDownListener()
        abc()
    }

    private fun initView() {
        tabLayout = binding.searchTabLayout
        recyclerView = binding.searchRecycler
        editTExt = binding.searchEditText
        recWrapper = binding.searchToggleWrapperConstraint
        searchResultWrapper = binding.searchResultLinear
        searchRecyclerView = binding.serachResultRecycler
    }

    private fun setAdapter() {
        var data = arrayListOf<MenuSearchItem>(
            MenuSearchItem.RecItem("123"),
            MenuSearchItem.RecItem("234"),
            MenuSearchItem.RecItem("345"),
        )
        searchAdapter = SearchAdapter(data)
        recyclerView.adapter = searchAdapter

        var resultData = arrayListOf<MenuSearchItem>(
            MenuSearchItem.SearchItem("니로", true),
            MenuSearchItem.SearchItem("니로 EV", true),
            MenuSearchItem.SearchItem("니로 플러스", false)
        )
        var searchResultAdapter = SearchAdapter(resultData)
        searchRecyclerView.adapter = searchResultAdapter
    }


    private fun setTabListener() {
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when(tab?.position) {
                    0 -> {
                        Log.d("tabTest", "clicked = 0")
                        if (selectedTab != 0){
                            var data = arrayListOf<MenuSearchItem>(
                                MenuSearchItem.RecItem("123"),
                                MenuSearchItem.RecItem("234"),
                                MenuSearchItem.RecItem("345"),
                            )

                            searchAdapter?.let {
                                it.data = data
                                it.notifyDataSetChanged()
                            }
                        }

                    }
                    1 -> {
                        if (selectedTab != 1){
                            var data = arrayListOf<MenuSearchItem>(
                                MenuSearchItem.PopItem("999"),
                                MenuSearchItem.PopItem("888"),
                                MenuSearchItem.PopItem("777"),
                            )

                            searchAdapter?.let {
                                it.data = data
                                it.notifyDataSetChanged()
                            }
                        }
                    }
                }
                selectedTab = tab?.position!!
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        })
    }

    private fun setClickListener() {
        binding.closeText.setOnClickListener {
            binding.searchToggleWrapperConstraint.toggleVisibility()
        }

        binding.searchCloseImg.setOnClickListener {
            dialog?.dismiss()
        }

        editTExt.setOnClickListener {
            // 아무것도 입력되어 있지 않을 때면 밑에 추천 검색어 토글로 해야함, 입력을 한 경우에는, 자동완성 결과를 보여줘야 한다
            if (editTExt.text!!.isEmpty()) {
                if (recWrapper.visibility == View.VISIBLE) {
                    recWrapper.visibility = View.GONE
                    it.background = resources.getDrawable(R.drawable.search_edit_text)
                }
                else {
                    recWrapper.visibility = View.VISIBLE
                }
            }
        }

        binding.searchResultCloseTv.setOnClickListener {
            searchResultWrapper.toggleVisibility()
        }
    }

    // 엔터키 리스너
    private fun setKeyDownListener() {
        editTExt.setOnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN) {
                if (editTExt.text!!.isNotEmpty()) {
                    // vm에서 인터넷 작업 수행
                    Log.d("searchTest", "setKeyDownListener!!")
                    true
                }
            }
            false
        }
    }

    private fun abc() {
        editTExt.addTextChangedListener(object : TextWatcher {
            private var timer = Timer()
            private val DELAY = 1000L
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                Log.d("searchTest", "TextChangedListener!! count = $count , before = $before s = $s, start = $start")
                // 입력한 문자 s를 vm에 보내서 실시간 검색 돌려야 함
                if (editTExt.text!!.isNotEmpty())  {
                    Log.d("searchTest", "TextChangedListener!! not empty")
                    searchResultWrapper.visibility = View.VISIBLE
                    recWrapper.visibility = View.GONE
                }
            }

            override fun afterTextChanged(s: Editable?) {
                timer.cancel()

                // vm 작업 중단

                //
                timer = Timer()
                timer.schedule(object : TimerTask() {
                    override fun run() {
                        // vm 작업 시작
                        if (editTExt.text!!.isNotEmpty()) {

                        }
                        else {
                            // 추천 검색어 목록을 보여준다
                        }
                    }

                }, DELAY)
            }

        })
    }

    companion object {
        @JvmStatic
        fun newInstance() = FragmentSearch()
    }
}