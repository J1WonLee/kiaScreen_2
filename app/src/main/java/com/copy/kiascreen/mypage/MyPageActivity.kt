package com.copy.kiascreen.mypage

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.activity.viewModels
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.copy.kiascreen.comparison.BuildCompActivity
import com.copy.kiascreen.R
import com.copy.kiascreen.databinding.ActivityMyPageBinding
import com.copy.kiascreen.mypage.adapter.MyPageAdapter
import com.copy.kiascreen.mypage.samplevo.MenuItems
import com.copy.kiascreen.roomVo.User
import com.copy.kiascreen.util.activity.BaseActivity
import com.copy.kiascreen.util.activity.TransitionMode
import com.google.android.material.appbar.MaterialToolbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyPageActivity : BaseActivity<ActivityMyPageBinding, MyPageViewModel>(TransitionMode.HORIZON), MyPageAdapterInterface {
    override val viewModel: MyPageViewModel by viewModels()

    private lateinit var userName : TextView
    private lateinit var inquire : TextView
    private lateinit var userInfo : TextView
    private lateinit var payInfo : TextView
    private lateinit var notice : TextView
    private lateinit var toolbar : MaterialToolbar
    private lateinit var recyclerView : RecyclerView

    private var mypageAdapter : MyPageAdapter? = null

    override fun inflateLayout(layoutInflater: LayoutInflater) = ActivityMyPageBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initView()
        setUserName()
        initToolbar()
        setRecyclerView()
        setClickListener()
    }

    override fun initView() {
        userName = binding.mypageMemberNameTv
        inquire = binding.mypageInquireTv
        toolbar = binding.mypageToolbar
        userInfo = binding.memberInfo
    }

    private fun initToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setDisplayShowTitleEnabled(false)
            it.setHomeAsUpIndicator(R.drawable.baseline_arrow_back_24)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.gohome_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> { finish() }
            R.id.mypage_go_home -> {
                Intent(this, BuildCompActivity::class.java).apply {
                    this.putExtra("user", user!!)
                    startActivity(this)
                }
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun setUserName() {
        userName.text = user?.name?.trim()

        var params = ConstraintLayout.LayoutParams(
            ConstraintLayout.LayoutParams.WRAP_CONTENT,ConstraintLayout.LayoutParams.WRAP_CONTENT
        )

        userName.layoutParams = params
    }

    // 각 항목들 클릭 이벤트
    private fun setClickListener() {
        userInfo.setOnClickListener {
            // 비밀번호 확인 fragment Dialog -> userDetailInfoActivity
            supportFragmentManager?.let {
                UserValidateFragment.newInstance(user!!).show(
                    supportFragmentManager, null
                )
            }
        }

        // 회원 정보 수정시 비밀번호 입력하는 프래그먼트
        binding.userInfoEditImg.setOnClickListener {
            supportFragmentManager?.let {
                UserValidateFragment.newInstance(user!!).show(
                    supportFragmentManager, null
                )
            }
        }

    }

    override fun getDataFromIntent() {
        user = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("user", User::class.java)
        }
        else {
            intent.getParcelableExtra<User>("user")
        }
        Log.d("myPageTest", "user id = ${user?.id}")
    }

    private fun setRecyclerView() {
        mypageAdapter = MyPageAdapter(MenuItems.menuItems)
        recyclerView = binding.quickMenuRecycler
        recyclerView.adapter = mypageAdapter

        mypageAdapter?.setInterface(this)
    }

    override fun onItemClick(position: Int) {
        when(position) {
            0 -> {
                // 해당 메뉴로 이동
            }
            1 -> {
                // 모델 비교로 이동
                Intent(this, BuildCompActivity::class.java).apply {
                    putExtra("user", user)
                    startActivity(this)
                }
            }
            2 -> {
                // 해당 메뉴로 이동
            }
        }
    }

    override fun onBackPressed() {
        if (isTaskRoot) {
            // main으로 돌려보낸다
            Intent(this, BuildCompActivity::class.java).apply {
                this.putExtra("user", user!!)
                startActivity(this)
                finish()
                overridePendingTransition(R.anim.activity_animation_none, R.anim.activity_horizon_exit)
            }
        }
        else {
            super.onBackPressed()
        }

    }
}