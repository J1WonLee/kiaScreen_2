package com.copy.kiascreen.menu

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Animatable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.vectordrawable.graphics.drawable.Animatable2Compat
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import com.copy.kiascreen.*
import com.copy.kiascreen.application.KiaSampleApplication
import com.copy.kiascreen.custom.MenuToggleImageView
import com.copy.kiascreen.custom.MenuWrapperLayout
import com.copy.kiascreen.custom.layout.LayoutMenuChild
import com.copy.kiascreen.custom.layout.LayoutMenuChild5
import com.copy.kiascreen.databinding.FragmentOverlayBinding
import com.copy.kiascreen.login.LoginActivity
import com.copy.kiascreen.mypage.MyPageActivity
import com.copy.kiascreen.registry.RegisterAgreeActivity
import com.copy.kiascreen.roomVo.User
import com.copy.kiascreen.setting.SettingActivity
import com.copy.kiascreen.util.OnBackPressedListener
import com.google.android.material.appbar.MaterialToolbar

class FragmentMainMenu : Fragment(), OnBackPressedListener {
    private lateinit var binding : FragmentOverlayBinding
    private lateinit var menu1 : TextView
    private lateinit var menu1Item : MenuWrapperLayout
    private lateinit var toolbar: MaterialToolbar
    private lateinit var purchaseToggle : MenuToggleImageView
    private lateinit var trialToggle : MenuToggleImageView
    private lateinit var rentToggle : MenuToggleImageView
    private lateinit var trialPlaceToggle : MenuToggleImageView
    private lateinit var eventToggle : MenuToggleImageView
    private lateinit var csToggle : MenuToggleImageView
    private lateinit var maintanceToggle : MenuToggleImageView
    private lateinit var centerToggle : MenuToggleImageView
    private lateinit var purGuideToggle : MenuToggleImageView
    private lateinit var discoverToggle : MenuToggleImageView
    private lateinit var newKiaToggle : MenuToggleImageView
    private lateinit var sustainToggle : MenuToggleImageView

    // 최하위 메뉴 customView
    private lateinit var rentChildMenu : LayoutMenuChild
    private lateinit var newKiaChildMenu : LayoutMenuChild
    private lateinit var maintanceChildMenu : LayoutMenuChild
    private lateinit var sustainChildMenu : LayoutMenuChild

    private lateinit var csChildMenu : LayoutMenuChild5
    private lateinit var purchaseGuideChildMenu : LayoutMenuChild5
    private lateinit var trialPlaceChildMenu : LayoutMenuChild5

    private var mOpenMenu : MenuToggleImageView? = null
    private var mOpenWrapper : MenuWrapperLayout? = null
    private var loginUser : User? = null
    private var fragmentBridge : FragmentActivityBridge? = null
    private var onBackPressedCallback : OnBackPressedCallback? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginUser = arguments?.getParcelable<User?>("user")

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentOverlayBinding.inflate(inflater, container, false)
        addGlobalListener()
        return binding.root
    }

    private fun addGlobalListener() {
        binding.bottomBackgroundView.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                binding.bottomBackgroundView.viewTreeObserver.removeOnGlobalLayoutListener(this)
                var screenHeight = requireActivity().getScreenHeight()
                Log.d("overlayFragmentTest", "===screenSize = ${screenHeight}")
                var bottomGridBottom = binding.bottomGrid.bottom
                Log.d("overlayFragmentTest", "===== bottom = ${bottomGridBottom}")

                var viewHeight = kotlin.math.abs(screenHeight - bottomGridBottom)

                binding.bottomBackgroundView.reSizeHeight(viewHeight)
            }

        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initView()
        initToolbar()
        setChildContent()
        setToggleListener()
        childMenuToggleListener()
        moveLoginOrRegister()
        setLoginRegisterTxt()

//        closeIcon.setOnClickListener {
//            Toast.makeText(context, "closeIcon click", Toast.LENGTH_SHORT).show()
//            activity?.findViewById<FrameLayout>(R.id.menu_fragment_holder)?.isClickable = false
//            activity?.supportFragmentManager?.beginTransaction()?.remove(this)?.commit()
//        }
    }

    private fun initView() {
        toolbar = binding.overlayToolbar
        menu1 = binding.purchase
        menu1Item = binding.purchaseWrapper
        purchaseToggle = binding.purchaseToggle
        trialToggle = binding.trialToggle
        rentToggle = binding.rentToggle
        trialPlaceToggle = binding.trialPlaceToggle
        eventToggle = binding.eventToggle
        csToggle = binding.csToggle
        maintanceToggle = binding.maintanceToggle
        purGuideToggle = binding.guideToggle
        centerToggle = binding.centerToggle
        discoverToggle = binding.discoverToggle
        newKiaToggle = binding.newKiaToggle
        sustainToggle = binding.sustainToggle

        rentChildMenu = binding.rentCarItemWrapper
        newKiaChildMenu = binding.newKiaItemWrapper
        csChildMenu = binding.centerItemWrapper
        purchaseGuideChildMenu = binding.guideItemWrapper
        maintanceChildMenu = binding.maintanceItemWrapper
        sustainChildMenu = binding.sustainItemWrapper
        trialPlaceChildMenu = binding.trialPlaceItemWrapper
    }

    private fun setChildContent() {
        rentChildMenu.setMenuItemViewText(resources.getStringArray(R.array.rent_array))
        csChildMenu.setMenuItemViewText(resources.getStringArray(R.array.cs_array))
        newKiaChildMenu.setMenuItemViewText(resources.getStringArray(R.array.new_kia_array))
        purchaseGuideChildMenu.setMenuItemViewText(resources.getStringArray(R.array.cs_purchase_guide_array))
        maintanceChildMenu.setMenuItemViewText(resources.getStringArray(R.array.maintance_array))
        sustainChildMenu.setMenuItemViewText(resources.getStringArray(R.array.sustain_array))
        trialPlaceChildMenu.setMenuItemViewText(resources.getStringArray(R.array.kia_experience_array))
    }

    private fun setToggleListener() {
        val purchaseListener = View.OnClickListener {
            //            purchaseToggle.toggleEvent(binding.purchaseWrapper)
            closeOpenMenu(purchaseToggle)
            // 열림
            if (purchaseToggle.toggleWrapper(binding.purchaseWrapper)) {

                setOpenMenu(purchaseToggle, binding.purchaseWrapper)
            }
            else {
//                rentToggle.hideMenu(binding.rentCarItemWrapper)
                rentToggle.hideCustomMenu(rentChildMenu)
            }
        }

        binding.purchase.setOnClickListener(purchaseListener)
        purchaseToggle.setOnClickListener(purchaseListener)

        val trialListener = View.OnClickListener {
            closeOpenMenu(trialToggle)

            if (trialToggle.toggleWrapper(binding.trialWrapper)) {
                setOpenMenu(trialToggle, binding.trialWrapper)
            }
            else {
                trialPlaceToggle.hideMenu(binding.trialPlaceItemWrapper)
            }
        }

        trialToggle.setOnClickListener(trialListener)
        binding.trial.setOnClickListener(trialListener)

        val eventListener = View.OnClickListener {
            closeOpenMenu(eventToggle)

            if (eventToggle.toggleWrapper(binding.eventWrapper)) {
                setOpenMenu(eventToggle, binding.eventWrapper)
            }
        }

        eventToggle.setOnClickListener(eventListener)
        binding.event.setOnClickListener(eventListener)

        val csListener = View.OnClickListener {
            csToggle.setOnClickListener {
                closeOpenMenu(csToggle)

                if (csToggle.toggleWrapper(binding.csWrapper)) {
                    setOpenMenu(csToggle, binding.csWrapper)
                }
                else {
                    // 고객 지원 메뉴 중, 유지관리 고개센터 구매가이드 3개 닫아줘야 함
                    maintanceToggle.hideMenu(binding.maintanceItemWrapper)
                    purGuideToggle.hideMenu(binding.guideItemWrapper)
                    centerToggle.hideMenu(binding.centerItemWrapper)
                }
            }
        }

        csToggle.setOnClickListener(csListener)
        binding.cs.setOnClickListener(csListener)

        val discoverListener = View.OnClickListener {
            closeOpenMenu(discoverToggle)

            if (discoverToggle.toggleWrapper(binding.discoverWrapper)) {
                setOpenMenu(discoverToggle, binding.discoverWrapper)
            }
            else {
                newKiaToggle.hideCustomMenu(newKiaChildMenu)
                sustainToggle.hideMenu(binding.sustainItemWrapper)
            }
        }

        discoverToggle.setOnClickListener(discoverListener)
        binding.discoverKia.setOnClickListener(discoverListener)
    }

    private fun childMenuToggleListener() {
        rentToggle.setOnClickListener {
//            rentToggle.toggleEvent(binding.rentCarItemWrapper)
            rentToggle.toggleCustomEvent(rentChildMenu)
        }

        trialPlaceToggle.setOnClickListener {
            trialPlaceToggle.toggleEvent(binding.trialPlaceItemWrapper)
        }

        maintanceToggle.setOnClickListener {
            maintanceToggle.toggleEvent(binding.maintanceItemWrapper)
            purGuideToggle.hideMenu(binding.guideItemWrapper)
            centerToggle.hideMenu(binding.centerItemWrapper)
        }

        purGuideToggle.setOnClickListener {
            purGuideToggle.toggleEvent(binding.guideItemWrapper)
            maintanceToggle.hideMenu(binding.maintanceItemWrapper)
            centerToggle.hideMenu(binding.centerItemWrapper)
        }

        centerToggle.setOnClickListener {
            centerToggle.toggleEvent(binding.centerItemWrapper)
            purGuideToggle.hideMenu(binding.guideItemWrapper)
            maintanceToggle.hideMenu(binding.maintanceItemWrapper)
        }

        newKiaToggle.setOnClickListener {
            newKiaToggle.toggleCustomEvent(newKiaChildMenu)
            sustainToggle.hideMenu(binding.sustainItemWrapper)
        }

        sustainToggle.setOnClickListener {
            sustainToggle.toggleEvent(binding.sustainItemWrapper)
            newKiaToggle.hideCustomMenu(newKiaChildMenu)
        }

    }

    private fun setOpenMenu(toggleImageView: MenuToggleImageView, wrapperLinearLayout: MenuWrapperLayout) {
        mOpenMenu = toggleImageView
        mOpenWrapper = wrapperLinearLayout
    }

    private fun closeOpenMenu(clickToggle : MenuToggleImageView) {
        mOpenMenu?.let {
            if (it != clickToggle) {
                it.toggleMenu(mOpenWrapper!!)
                closeChildMenu()
            } else {
                mOpenMenu = null
            }
        }
    }

    private fun closeChildMenu() {
        Log.d("menuTest", "closeChildMenu called")
        mOpenMenu?.let {
            when (it) {
                purchaseToggle -> {
                    Log.d("menuTest", "closeChildMenu called - purchaseToggle")
                    rentToggle.hideCustomMenu(rentChildMenu)
                }
                trialToggle -> {
                    Log.d("menuTest", "closeChildMenu called - trialToggle")
                    trialPlaceToggle.hideMenu(binding.trialPlaceItemWrapper)
                }
                csToggle -> {
                    Log.d("menuTest", "closeChildMenu called - csToggle")
                    maintanceToggle.hideMenu(binding.maintanceItemWrapper)
                    purGuideToggle.hideMenu(binding.guideItemWrapper)
                    centerToggle.hideMenu(binding.centerItemWrapper)
                }
                discoverToggle -> {
                    Log.d("menuTest", "closeChildMenu called - discoverToggle")
                    newKiaToggle.hideCustomMenu(newKiaChildMenu)
                    sustainToggle.hideMenu(binding.sustainItemWrapper)
                }
                else -> { return }
            }
        }
    }


    private fun initToolbar() {
        toolbar.inflateMenu(R.menu.overlay_menu)
        toolbar.setNavigationIcon(R.drawable.baseline_search_24)

        toolbar.setNavigationOnClickListener {
//            activity?.supportFragmentManager?.beginTransaction()?.add(R.id.menu_fragment_holder, FragmentSearch())?.commit()
//            binding.overlayScrollConstraint.setBackgroundColor(resources.getColor(R.color.transparent))

            FragmentSearch().show(activity?.supportFragmentManager!! , null)
        }

        toolbar.setOnMenuItemClickListener {
            when(it.itemId) {
                R.id.overlay_exit -> {
                    initAnimate(R.id.overlay_exit)
//                    activity?.findViewById<FrameLayout>(R.id.menu_fragment_holder)?.isClickable = false
//                    activity?.supportFragmentManager?.beginTransaction()?.remove(this)?.commit()
//                    fragmentBridge?.showBottomNavi()
                    true
                }else -> false
            }
        }
    }

    private fun initAnimate(itemId : Int) {
        val item = toolbar.menu.findItem(itemId)

        val animDrawable = AnimatedVectorDrawableCompat.create(requireContext(), R.drawable.avd_drawer_close)
        animDrawable?.registerAnimationCallback(object : Animatable2Compat.AnimationCallback() {
            override fun onAnimationEnd(drawable: Drawable?) {
                activity?.findViewById<FrameLayout>(R.id.menu_fragment_holder)?.isClickable = false
                activity?.supportFragmentManager?.beginTransaction()?.remove(this@FragmentMainMenu)?.commit()
                fragmentBridge?.showBottomNavi()
            }
        })
        item.icon = animDrawable
        val animatable = item.icon
        if (animatable is Animatable) {
            animatable.start()
        }
    }

    private fun moveLoginOrRegister() {
        // 로그인 한 상태면 로그아웃 / 마이페이지임
        binding.login.setOnClickListener {
            if (loginUser == null) {
                Intent(requireActivity(), LoginActivity::class.java).apply {
                    requireActivity().startActivity(this)
//                    requireActivity().overridePendingTransition(R.anim.activity_fade_in, R.anim.activity_fade_out)
                }
            } else {
                KiaSampleApplication.prefs.loginFlag = false
                KiaSampleApplication.prefs.autoLoginFlag = false
                loginUser = null
                binding.login.text = "로그인"
                binding.register.text= "회원가입"
                fragmentBridge?.setLogout(true)
            }
        }

        binding.register.setOnClickListener {
            // 회원 가입으로 이동한다
            if (loginUser == null) {
                Intent(requireActivity(), RegisterAgreeActivity::class.java).apply {
                    requireActivity().startActivity(this)
                }
            }
            else {
                Intent(requireActivity(), MyPageActivity::class.java).apply {
                    putExtra("user", loginUser)
                    requireActivity().startActivity(this)
                }
            }
        }

        binding.setting.setOnClickListener {
            Intent(requireActivity(), SettingActivity::class.java).apply {
                loginUser?.let {
                    putExtra("user" , loginUser)
                }
                requireActivity().startActivity(this)
            }
        }
    }

    // 로그인 한 상태면 -> 로그인 -> 로그아웃 / 회원가입 -> 마이페이지
    private fun setLoginRegisterTxt() {
        loginUser?.let {
            // 로그인 한 상태
            binding.login.text = "로그아웃"
            binding.register.text = "마이페이지"
        }
    }

    private fun startIntent(intent : Intent) {
        startActivity(intent)
        requireActivity().overridePendingTransition(R.anim.activity_fade_in, R.anim.activity_fade_out)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            fragmentBridge = context as FragmentActivityBridge
            // activity에서 onbackpressed super.()해줘야함 안해주면 동작x
            /*
            onBackPressedCallback = object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    activity?.findViewById<FrameLayout>(R.id.menu_fragment_holder)?.isClickable = false
                    activity?.supportFragmentManager?.beginTransaction()?.remove(this@FragmentMainMenu)?.commit()
                    fragmentBridge?.showBottomNavi()
                }
            }
            requireActivity().onBackPressedDispatcher.addCallback(this, onBackPressedCallback!!)

             */
        }catch (e : java.lang.Exception) {
            Log.d("FragmentMainMenu", "onAttach class Type Exception ${e.message}")
        }
    }

    // sharedPreference에 현재 로그인 값을 넣어준다 -> 메인에서 onResume에서 확인 후, 로그인 여부 처리할 수 있게 함
    override fun onPause() {
        super.onPause()
        Log.d("LoginTest", "bq onPause Called!")
    }

    override fun onDetach() {
        super.onDetach()
        onBackPressedCallback?.remove()
    }

    companion object {
        fun newInstance(userData : User?) : FragmentMainMenu {
            val args = Bundle().apply {
                putParcelable("user", userData)
            }

            return FragmentMainMenu().apply {
                arguments = args
            }
        }
    }

    override fun onBackPressed() {
        activity?.findViewById<FrameLayout>(R.id.menu_fragment_holder)?.isClickable = false
        activity?.supportFragmentManager?.beginTransaction()?.remove(this@FragmentMainMenu)?.commit()
        fragmentBridge?.showBottomNavi()
    }


}