package com.copy.kiascreen.comparison

import android.content.Intent
import android.graphics.drawable.Animatable
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.vectordrawable.graphics.drawable.Animatable2Compat
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import androidx.viewpager2.widget.ViewPager2
import com.copy.kiascreen.*
import com.copy.kiascreen.comparison.adapter.*
import com.copy.kiascreen.comparison.adapter.compAdapter.*
import com.copy.kiascreen.comparison.extension.*
import com.copy.kiascreen.comparison.snapplistener.BounceEdgeEffectFactory

import com.copy.kiascreen.menu.FragmentMainMenu
import com.copy.kiascreen.menu.FragmentSearch
import com.copy.kiascreen.comparison.vo.CompListItems
import com.copy.kiascreen.comparison.vo.SelectedOption
import com.copy.kiascreen.custom.LinePagerIndicatorDecoration
import com.copy.kiascreen.custom.StickyHeaderRvDecoration
import com.copy.kiascreen.custom.StickyScrollView
import com.copy.kiascreen.databinding.ActivityBuildCmpBinding
import com.copy.kiascreen.menu.FragmentActivityBridge
import com.copy.kiascreen.roomVo.BrandItems
import com.copy.kiascreen.roomVo.User
import com.copy.kiascreen.util.AlertUtil
import com.copy.kiascreen.util.OnBackPressedListener
import com.copy.kiascreen.util.activity.BaseActivity
import com.copy.kiascreen.util.activity.TransitionMode
import com.copy.kiascreen.util.textlink.TextLink
import com.google.android.material.appbar.MaterialToolbar
import com.google.firebase.inappmessaging.FirebaseInAppMessaging
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BuildCompActivity : BaseActivity<ActivityBuildCmpBinding, CompViewModel>(TransitionMode.HORIZON), SearchRecyclerAddInterface, HeaderInterface,
    FragmentActivityBridge, ScrollListener.ScrollListenerBridge {
    override val viewModel: CompViewModel by viewModels()

    private lateinit var scrollView : StickyScrollView
    private lateinit var toolbar : MaterialToolbar
    private lateinit var comparsionRecycler : RecyclerView
    private lateinit var adapter : ComparisonAdapter
    private lateinit var stickyHeaderRecyclerView: RecyclerView

    private lateinit var performViewPager2: ViewPager2
    private lateinit var econViewPager2: ViewPager2
    private lateinit var safetyViewPager2: ViewPager2
    private lateinit var extraPriceViewPager2: ViewPager2

    /*
    // 7/20 하단 목록을 vp2 -> rv로 변경. scrollListener도 rv에 맞게 변경함.
    // 7/20 하단 목록을 rv로 -> vp2 변경
    private lateinit var performRv: RecyclerView
    private lateinit var econRv: RecyclerView
    private lateinit var safetyRv: RecyclerView
    private lateinit var extraPriceRv: RecyclerView

     */

    private val scrollListener = ScrollListener(this)

    private val headerAdapter : HeaderRecyclerAdapter by lazy {
        HeaderRecyclerAdapter(this)
    }

    private val performAdapter : CompPerformanceAdapter by lazy {
        CompPerformanceAdapter(this)
    }

    private val econAdapter : CompEconAdapter by lazy {
        CompEconAdapter(this)
    }

    private val safetyAdapter : CompSafetyAdapter by lazy {
        CompSafetyAdapter(this)
    }

    private val extraPriceAdapter : CompExtraPriceAdapter by lazy {
        CompExtraPriceAdapter(this)
    }

    private val kncapTv : TextView by lazy {
        binding.layoutKcnap.kncapLinkTv
    }

    private var isResetClicked = false

    override fun inflateLayout(layoutInflater: LayoutInflater) = ActivityBuildCmpBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initToolbar()
        initCompRecycler()
        setSpinnerAdapter()
    }

    override fun getDataFromIntent() {
        Log.d("loginTest", "getUserDataFromIntent")

        user = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("user", User::class.java)
        }
        else {
            intent.getParcelableExtra<User>("user")
        }
        Log.d("loginTest", "${user?.id}")
    }

    override fun initView() {
        toolbar = binding.toolbar
        scrollView = binding.mainScrollView
        comparsionRecycler = binding.compRecycler
        stickyHeaderRecyclerView = binding.compStickyRecycler

        scrollView.run {
            header = stickyHeaderRecyclerView
            stickListener = { _ ->
                Log.d("StickyHeader", "stickListener")
            }
            freeListener = { _ ->
                Log.d("StickyHeader", "freeListener")
            }
        }
        performViewPager2 = binding.compDetailVp1
        econViewPager2 = binding.compDetailVp2
        safetyViewPager2 = binding.compDetailVp3
        extraPriceViewPager2 = binding.compDetailVp4

        TextLink(kncapTv).apply {
            setLinkify("교통안전공단 바로가기")
        }
    }

    private fun initToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setDisplayShowTitleEnabled(false)
            it.setHomeAsUpIndicator(R.drawable.baseline_arrow_back_24)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> { Toast.makeText(this, "home", Toast.LENGTH_SHORT).show() }
            R.id.main_menu_extend_menu -> {
                initAnimate(R.id.main_menu_extend_menu)
            }
            R.id.main_menu_search_engine -> {  initSearchDialog() }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    private fun initAnimate(id : Int) {
        try {
            val item = toolbar.menu.findItem(id)
            item?.let {
                var animDrawable = AnimatedVectorDrawableCompat.create(this, R.drawable.avd_drawer_open)
                animDrawable?.registerAnimationCallback(object : Animatable2Compat.AnimationCallback() {
                    override fun onAnimationEnd(drawable: Drawable?) {
                        inflateFragment()
                    }
                })
                item.icon = animDrawable
                val animatable = item.icon
                if (animatable is Animatable) {
                    animatable.start()
                }
            }
        } catch (e : Exception) {
            Log.d("toolbarTest", "initANimate error msg = ${e.message}")
        }
    }

    // 메뉴 프래그먼트 실행
    private fun inflateFragment() {
        binding.mainBottomSheet.visibility = View.GONE
        val fragmentManager = supportFragmentManager
        binding.menuFragmentHolder.isClickable = true
        fragmentManager.beginTransaction().replace(R.id.menu_fragment_holder, FragmentMainMenu.newInstance(user)).commit()
    }

    // 검색 창을 dialogFragment형태로 보여준다.
    private fun initSearchDialog() {
        FragmentSearch().show(supportFragmentManager, null)
    }

    // 차량 선택 리사이클러 설정
    private fun initCompRecycler() {
        adapter = ComparisonAdapter(this, BrandItems.spinnerList)
        comparsionRecycler.addItemDecoration(LinePagerIndicatorDecoration())
        comparsionRecycler.adapter = adapter
        adapter.setAddInterface(this)

        comparsionRecycler.apply {
            edgeEffectFactory = BounceEdgeEffectFactory()
        }

        setResetBtnClick()
        PagerSnapHelper().attachToRecyclerView(comparsionRecycler)
    }

    // 모델 비교 버튼 클릭 이벤트
    // 모델 비교 버튼 클릭 시 vp들을 설정 + stickyHeaderRv를 설정
    private fun setBottomNaviClickListener() {
        binding.modelCompareBtn.setOnClickListener {
            binding.modelCompDetailWrapper.visibility = View.VISIBLE

            var selectedLists = adapter.getSelectedCar()        // 선택한 항목 갯수만큼 아이템을 추가해 줘야 한다.
            Log.d("headerAdapterTest", "selectedList size onmain = ${selectedLists.size}")
            setStickyRecycler(selectedLists)

            for (i in 0 until selectedLists.size -1) {      // 이미 하나가 기본적으로 들어가 있기 때문에 횟수 조절함
                CompListItems.addCompItem()
            }

            // 리셋 버튼의 클릭 유무에 따라서 vp2 데이터 처리 방법이 상이함
            if (!isResetClicked) {
                initPerformanceVp2()
            } else {
                setDataAfterReset()
            }

            setScrollListener2()
            binding.mainBottomSheet.visibility = View.GONE
            setCompVpClose()
            setSwitchListener()
        }
    }

    // 하단 pager2 어뎁터 설정, 초기화 버튼 클릭된적 없음
    private fun initPerformanceVp2() {
        performViewPager2.adapter = performAdapter
        econViewPager2.adapter = econAdapter
        safetyViewPager2.adapter = safetyAdapter
        extraPriceViewPager2.adapter = extraPriceAdapter

    }

    // 초기화 버튼 클릭 시 pager2들의 데이터들을 초기화 시켜준다.
    private fun setDataAfterReset() {
        performAdapter.setPerfromDataAfterReset()
        econAdapter.setEconDataAfterReset()
        safetyAdapter.setSafetyDataAfterReset()
        extraPriceAdapter.setExtraPriceDataAfterReset()
    }

    // comparisonAdapter에서 아이템 추가 시, 이전 spinner들 다 선택했는지 stage로 보내줌.
    override fun onAddBtnClick(stage : Int) {
        // alert
        var msg = ""
        when (stage) {
            0 -> {
                msg = "브랜드를 선택해 주세요"
            }
            1 -> {
                msg = "차량을 선택해 주세요"
            }
            2 -> {
                msg = "엔진을 선택해 주세요"
            }
            3 -> {
                msg = "트림을 선택해 주세요"
            }
            else -> {
                adapter.addItem(comparsionRecycler)
                return
            }
        }
        // spinner 선택 안한거 있으면 alert으로 선택하라고 알려 준다
        if (msg.isNotEmpty()) {
            AlertUtil.makeAlertDialog(this, msg)
        }
    }

    override fun onDelBtnClick(position: Int) {
        adapter.onDelBtnClicked(position, comparsionRecycler)

        /*
//        adapter.count--
        var mPosition = position
        adapter.let {
            Log.d("adapterTest", "ondelbnclick!! adapter cnt = ${adapter.mitems.size} position = $position")
            it.notifyItemRemoved(position)
            it.notifyItemRangeRemoved(position, adapter.mitems.size - position)
        }

        if (position == adapter.mitems.size){
            mPosition--
        }

        comparsionRecycler.findViewHolderForAdapterPosition(mPosition)?.let {
            Log.d("adapterTest", "findViewHolderPosition and count = ${adapter.mitems.size} mPosition = $mPosition")
            it.itemView.findViewById<ConstraintLayout>(R.id.add_wrapper_constraint).visibility = View.VISIBLE
            comparsionRecycler.smoothSnapToPosition(mPosition)
        }

        adapter.holderMap[mPosition]?.let {
            Log.d("adapterTest", "holderMap not null")
            it.itemView.findViewById<ConstraintLayout>(R.id.add_wrapper_constraint).visibility = View.VISIBLE
        }

         */
    }

    // 비교 차량 이미지 설정
    override fun setRecCarImg() {
        comparsionRecycler.setRecImg(adapter)

        /*
        for(i in  1 until adapter.mitems.size) {          // adapter.count -> adapter.mitems.size
            comparsionRecycler.findViewHolderForAdapterPosition(i)?.let {
                // 해당 holder의 trim이 선택되어져 있다면, 추천 비교 차량은 보여주지 않는다.
                Log.d("adapterTest", "setRecCarImg, position = $i , adapter count = ${adapter.mitems.size}")
                /*
                var spinner = it.itemView.findViewById<Spinner>(R.id.trim_spinner)
                if (spinner.selectedItemPosition == spinner.adapter.count) {
                    // 현재 트림까지 선택이 안된 상태
                    // 추천 비교 차량 보여준다 (구매 신청 버튼, 예상 비용 뷰를 가려준다)
                    it.itemView.findViewById<AppCompatButton>(R.id.build_btn).visibility = View.GONE
                    it.itemView.findViewById<AppCompatButton>(R.id.purchase_consult_btn).visibility = View.GONE
                    it.itemView.findViewById<ConstraintLayout>(R.id.price_wrapper).visibility = View.GONE

                    it.itemView.findViewById<ConstraintLayout>(R.id.rec_car_wrapper_constraint).visibility = View.VISIBLE
                    it.itemView.findViewById<ViewPager2>(R.id.rec_car_img_vp).adapter?.notifyDataSetChanged()
                }

                 */

                setCompRecImg(it)
            }

            if (i==2 && comparsionRecycler.findViewHolderForAdapterPosition(i) == null) {
                var holder = adapter.holderMap[2]
                holder?.let {
                    Log.d("holderTest", "index == 2 and lastItem holder is not null")
                    /*
                    var spinner = it.itemView.findViewById<Spinner>(R.id.trim_spinner)
                    if (spinner.selectedItemPosition == spinner.adapter.count) {
                        // 현재 트림까지 선택이 안된 상태
                        // 추천 비교 차량 보여준다 (구매 신청 버튼, 예상 비용 뷰를 가려준다)
                        it.itemView.findViewById<AppCompatButton>(R.id.build_btn).visibility = View.GONE
                        it.itemView.findViewById<AppCompatButton>(R.id.purchase_consult_btn).visibility = View.GONE
                        it.itemView.findViewById<ConstraintLayout>(R.id.price_wrapper).visibility = View.GONE

                        it.itemView.findViewById<ConstraintLayout>(R.id.rec_car_wrapper_constraint).visibility = View.VISIBLE
                        it.itemView.findViewById<ViewPager2>(R.id.rec_car_img_vp).adapter?.notifyDataSetChanged()
                    }

                     */

                    setCompRecImg(it)
                }
            }
        }

         */
    }

    // 모델 비교 버튼 활성화 + 클릭 이벤트 설정
    override fun setCompareBtnEnable() {
        binding.modelCompareBtn.isEnabled = true
        binding.modelCompareBtn.setBackgroundColor(resources.getColor(R.color.black))
        setBottomNaviClickListener()
    }

    // 모델 변경 및 삭제 추가 시 , 하단의 비교 상세보기 페이저에 반영해준다
    override fun setCompPager2Item(adapterPosition : Int) {
        // 모델 비교 버튼을 클릭 했을 때에만, 하단 페이저에 갱신 내역을 반영해줘야 함
        if (binding.mainBottomSheet.visibility == View.GONE) {
            Log.d("compTest", "== setCompPager2Item ==  adapter.getSelectedCar().size : ${adapter.getSelectedCar().size}")
            Log.d("updateTest", "===setCompPager2Item test ==== adapter.getSelectedCar().size :  ${adapter.getSelectedCar().size}")
            if (adapter.getSelectedCar().size >=2) {
                Log.d("compTest", "== setCompPager2Item ==  adapterPosition : $adapterPosition")
                if (performAdapter.data.size > adapter.getSelectedCar().size) {
                    // 목록이 제거된 경우.
                    Log.d("compTest", "== item removed==")
                    Log.d("updateTest", "== item removed==")
                    performAdapter.deletePerFormData(adapterPosition)
                    econAdapter.deletePerFormData(adapterPosition)
                    safetyAdapter.deleteSafetyData(adapterPosition)
                    extraPriceAdapter.deleteExtraPriceData(adapterPosition)
                }
                else if (performAdapter.data.size < adapter.getSelectedCar().size) {
                    // 목록이 추가된 경우
                    Log.d("compTest", "== item add==")
                    Log.d("updateTest", "== item add==")
                    var newData = CompListItems.getComeItem()

                    performAdapter.addPerFormData(newData)
                    econAdapter.addEconData(newData)
                    safetyAdapter.addSafetyData(newData)
                    extraPriceAdapter.addExtraPriceData(newData)

                }
                else {
                    // 목록이 수정된 경우
                    Log.d("compTest", "== item updated==")
                    Log.d("updateTest", "== item updated==")
                    var updatedData = CompListItems.getUpdatedItem()

                    performAdapter.updatedPerFormData(updatedData, adapterPosition)
                    econAdapter.updatedEconData(updatedData, adapterPosition)
                    safetyAdapter.updatedSafetyData(updatedData, adapterPosition)
                    extraPriceAdapter.updatedExtraPriceData(updatedData, adapterPosition)
                }
            }
        }
    }

    // sticky recycler에 아이템 추가 , 갱신
    override fun addHeaderRecycler(item: SelectedOption, position: Int) {
        if (binding.mainBottomSheet.visibility == View.GONE) {
            if (headerAdapter.getItemSize() != adapter.selectedCarList.size) {
                // 트림 선택 완료시 데이터 크기다 다르다 -> 더하거나 뺌.
                headerAdapter.addSelectedItem(item)
            }
            else {
                // 트림 선택 완료시 데이터 크기가 같다 -> 수정만 함
                headerAdapter.updateSelectedItem(item, position )
            }
        }
    }

    // sticky recycler에 아이템 제거
    override fun delHeaderRecycler(position: Int) {
        if (binding.mainBottomSheet.visibility == View.GONE) {
            headerAdapter.deleteSelectedItem(position)
        }
    }

    // 모델 선택 초기화 버튼
    private fun setResetBtnClick(){
        binding.resetImg.setOnClickListener {
            /*
            comparsionRecycler.adapter?.let {
                var size = it.itemCount

                Log.d("adapterTest", "reset clicked !! ${adapter.mitems.size},  <<<<<mitems size ${resetItem.size} << resetItem size")
                it.notifyItemRangeRemoved(0, size)
                it.notifyItemRangeChanged(0, size)

                adapter.resetItem(listOf(Brand("기아", BrandItems.kiaCars)))

                comparsionRecycler.findViewHolderForAdapterPosition(0)?.let { holder ->
                    Log.d("adapterTest", "findViewHolderPosition")
                    holder.itemView.findViewById<ConstraintLayout>(R.id.add_wrapper_constraint).visibility = View.VISIBLE
                }

                adapter.holderMap[0]?.let { holder ->
                    Log.d("adapterTest", "holderMap not null")
                    holder.itemView.findViewById<ConstraintLayout>(R.id.add_wrapper_constraint).visibility = View.VISIBLE
                }
            }

             */

            adapter.reset(comparsionRecycler)

            binding.modelCompDetailWrapper.visibility = View.GONE
            stickyHeaderRecyclerView.apply {
                adapter = null
            }
            // reset 버튼 클릭시, flag를 조정하여, 아이템이 없을 시에는 스크롤 내려도 sticky rv를 안보이게 한다.
            scrollView.isModelCompareClicked = false

            binding.mainBottomSheet.visibility = View.VISIBLE
            binding.modelCompareBtn.apply {
                visibility = View.VISIBLE
                isEnabled = false
                setBackgroundColor(resources.getColor(R.color.btn_gray))
            }

            CompListItems.resetCompItemList()       // Object Class 아이템 초기화
            isResetClicked = true

        }
    }

    // stickyHeader 리사이클러 설정, 선택한 항목들에 대한 정보들을 stickyHeader의 item으로 사용 함.
    private fun setStickyRecycler(itemList : MutableList<SelectedOption>) {
        Log.d("headerAdapterTest", "setStickyRecycler, itemSize = ${itemList.size}")
        headerAdapter.setSelectedItem(itemList)
        headerAdapter.setInterface(this)
        scrollView.isModelCompareClicked = true

        // 처음 만들어짐
        if (!isResetClicked) {
            val linearLayoutManagerWrapper = LinearLayoutManagerWrapper(this, LinearLayoutManager.HORIZONTAL, false)
            stickyHeaderRecyclerView.apply {
                layoutManager = linearLayoutManagerWrapper
                adapter = headerAdapter
                addItemDecoration(StickyHeaderRvDecoration())
            }

            if (stickyHeaderRecyclerView.onFlingListener == null) {
                PagerSnapHelper().attachToRecyclerView(stickyHeaderRecyclerView)
            }
        }
        // 리셋한번 돌림
        else {
            val linearLayoutManagerWrapper = LinearLayoutManagerWrapper(this, LinearLayoutManager.HORIZONTAL, false)
            stickyHeaderRecyclerView.apply {
                layoutManager = linearLayoutManagerWrapper
                adapter = headerAdapter
            }

        }



    }
    private fun setScrollListener2() {
        comparsionRecycler.addOnScrollListener(scrollListener.oneListener)
        stickyHeaderRecyclerView.addOnScrollListener(scrollListener.stickyHeaderListener)

        /*
        performRv.addOnScrollListener(scrollListener.performListener)
        econRv.addOnScrollListener(scrollListener.performListener)
        safetyRv.addOnScrollListener(scrollListener.performListener)
        extraPriceRv.addOnScrollListener(scrollListener.performListener)

         */

        performViewPager2.registerOnPageChangeCallback(scrollListener.pagerCallBack)
        econViewPager2.registerOnPageChangeCallback(scrollListener.pgaerCallBack2)
        safetyViewPager2.registerOnPageChangeCallback(scrollListener.pgaerCallBack2)
        extraPriceViewPager2.registerOnPageChangeCallback(scrollListener.pgaerCallBack2)


        comparsionRecycler.scrollToPosition(0)
    }

    // sticky header에서 아이템 추가 클릭 시, compRecyclerView로 이동시켜준다
    override fun scrollToPosition() {
        scrollView.smoothScrollTo(comparsionRecycler.top ,0)
    }

    // 바텀 spinner 설정
    private fun setSpinnerAdapter() {
        val snsString = resources.getStringArray(R.array.sns_spinner_array)
        val snsSpinnerAdapter = snsString.setStringArray(this)

        val familyString = resources.getStringArray(R.array.sitemap_spinner_array)
        val familySpinnerAdapter = familyString.setStringArray(this)

        binding.compBottomLayout.snsSpinner.setSpinnerAdapter(snsSpinnerAdapter)
        binding.compBottomLayout.familySiteSpinner.setSpinnerAdapter(familySpinnerAdapter)
    }

    private fun setCompVpClose() {
        Log.d("vpToggleTest", "setCompVpClose called")

        binding.compTitleExpandImg.toggleVp(performViewPager2)
        binding.compTitle2ExpandImg.toggleVp(econViewPager2)
        binding.compTitle3ExpandImg.toggleVp(safetyViewPager2, binding.layoutKcnap.kncapConstraintWrapper)
        binding.compTitle4ExpandImg.toggleVp(extraPriceViewPager2)

        /*
        binding.compTitleExpandImg.toggleRv(performRv)
        binding.compTitle2ExpandImg.toggleRv(econRv)
        binding.compTitle3ExpandImg.toggleRv(safetyRv)
        binding.compTitle4ExpandImg.toggleRv(extraPriceRv)

         */
    }

    // 서로 다른 항목 switch
    private fun setSwitchListener() {
        binding.modelCompareSwitch.toggleDiffFields(performAdapter, econAdapter, safetyAdapter)
    }

    override fun onResume() {
        super.onResume()
        supportFragmentManager.findFragmentById(R.id.menu_fragment_holder)
            ?.let { supportFragmentManager.beginTransaction().remove(it).commit() }
        binding.menuFragmentHolder.isClickable = false
        Log.d("LoginTest", "bq onResume Called!")

        FirebaseInAppMessaging.getInstance().triggerEvent("main_activity_ready")
    }

    override fun onBackPressed() {
        val fragments = supportFragmentManager.fragments
        if (fragments.size > 0) {
            for (fragment in fragments) {
                if (fragment is OnBackPressedListener) {
                    (fragment as OnBackPressedListener).onBackPressed()
                    return
                }
            }
        }
        super.onBackPressed()
    }

    override fun setLogout(flag: Boolean) {
        if (flag) {
            user = null
        }
    }

    override fun showBottomNavi() {
        binding.mainBottomSheet.visibility = View.VISIBLE
    }

    // recyclerView postiion 이동
    override fun moveViewPosition(targetPosition: Int) {
        stickyHeaderRecyclerView.scrollToPosition(targetPosition)

        // vp2 포지션 이동
        performViewPager2.setCurrentItem(targetPosition, true)
        econViewPager2.setCurrentItem(targetPosition, true)
        safetyViewPager2.setCurrentItem(targetPosition, true)
        extraPriceViewPager2.setCurrentItem(targetPosition, true)

        /*
        performRv.scrollToPosition(targetPosition)
        safetyRv.scrollToPosition(targetPosition)
        econRv.scrollToPosition(targetPosition)
        extraPriceRv.scrollToPosition(targetPosition)
        */
    }

    override fun movePagerPosition(targetPosition: Int) {
        comparsionRecycler.scrollToPosition(targetPosition)
        stickyHeaderRecyclerView.smoothScrollToPosition(targetPosition)
//        performRv.scrollToPosition(targetPosition)
//        safetyRv.scrollToPosition(targetPosition)
//        econRv.scrollToPosition(targetPosition)
//        extraPriceRv.scrollToPosition(targetPosition)


        safetyViewPager2.currentItem = targetPosition
        econViewPager2.currentItem = targetPosition
        extraPriceViewPager2.currentItem = targetPosition


    }

    override fun movePerformPagerPosition(targetPosition: Int) {
        performViewPager2.setCurrentItem(targetPosition)
    }
}

/*
    // 리사이클러뷰 스크롤 같이 움직이게 리스너 장착시켜 준다.
    private fun setScrollListener() {
        comparsionRecycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    var lastFirstVisiblePosition = ((recyclerView.layoutManager) as LinearLayoutManager).findFirstCompletelyVisibleItemPosition()
                    var lastVisiblePosition = ((recyclerView.layoutManager) as LinearLayoutManager).findLastVisibleItemPosition()
                    Log.d("scrollTest", "Idle firstPosition = $lastFirstVisiblePosition")
                    Log.d("scrollTest", "Idle lastVisiblePosition = $lastVisiblePosition")

                    if (lastFirstVisiblePosition != RecyclerView.NO_POSITION) {
                        // 다른 recyclerView 포지션 이동
                        stickyHeaderRecyclerView.scrollToPosition(lastFirstVisiblePosition)

                        // vp2 포지션 이동
                        performViewPager2.setCurrentItem(lastFirstVisiblePosition, true)
                        econViewPager2.setCurrentItem(lastFirstVisiblePosition, true)
                        safetyViewPager2.setCurrentItem(lastFirstVisiblePosition, true)
                        extraPriceViewPager2.setCurrentItem(lastFirstVisiblePosition, true)
                    }
                    else if (lastVisiblePosition != RecyclerView.NO_POSITION) {
                        // 다른 recyclerView 포지션 이동
                        stickyHeaderRecyclerView.scrollToPosition(lastVisiblePosition)

                        // vp2 포지션 이동
                        performViewPager2.setCurrentItem(lastVisiblePosition, true)
                        econViewPager2.setCurrentItem(lastVisiblePosition, true)
                        safetyViewPager2.setCurrentItem(lastVisiblePosition, true)
                        extraPriceViewPager2.setCurrentItem(lastVisiblePosition, true)
                    }
                }
            }
        })

        stickyHeaderRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            var lastScrollPosition = 0
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                Log.d("scrollTest", "stickyHeaderRecyclerView im on scrolled!!!!")
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    var lastFirstVisiblePosition = ((recyclerView.layoutManager) as LinearLayoutManager).findFirstCompletelyVisibleItemPosition()
                    var lastVisiblePosition = ((recyclerView.layoutManager) as LinearLayoutManager).findLastVisibleItemPosition()

                    Log.d("scrollTest", "Idle firstPosition = $lastFirstVisiblePosition")
                    Log.d("scrollTest", "Idle lastVisiblePosition = $lastVisiblePosition")

                    if (lastFirstVisiblePosition != RecyclerView.NO_POSITION) {
                        // 다른 recyclerView 포지션 이동
                        lastScrollPosition = lastFirstVisiblePosition
                        comparsionRecycler.scrollToPosition(lastFirstVisiblePosition)

                        // vp2 포지션 이동
                        performViewPager2.setCurrentItem(lastFirstVisiblePosition, true)
                        econViewPager2.setCurrentItem(lastFirstVisiblePosition, true)
                        safetyViewPager2.setCurrentItem(lastFirstVisiblePosition, true)
                        extraPriceViewPager2.setCurrentItem(lastFirstVisiblePosition, true)
                    }
                    else if (lastVisiblePosition != RecyclerView.NO_POSITION) {
                        // 다른 recyclerView 포지션 이동
                        lastScrollPosition = lastVisiblePosition
                        comparsionRecycler.scrollToPosition(lastVisiblePosition)

                        // vp2 포지션 이동
                        performViewPager2.setCurrentItem(lastVisiblePosition, true)
                        econViewPager2.setCurrentItem(lastVisiblePosition, true)
                        safetyViewPager2.setCurrentItem(lastVisiblePosition, true)
                        extraPriceViewPager2.setCurrentItem(lastVisiblePosition, true)
                    }
                }

            }
        })

        performViewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            var curPage = 0
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                curPage = position
            }

            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
                if (state == ViewPager2.SCROLL_STATE_IDLE) {
                    comparsionRecycler.scrollToPosition(curPage)
                    stickyHeaderRecyclerView.smoothScrollToPosition(curPage)

                    safetyViewPager2.setCurrentItem(curPage)
                    econViewPager2.setCurrentItem(curPage)
                    extraPriceViewPager2.setCurrentItem(curPage)
                }
            }
        })

        econViewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            var curPage = 0
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                curPage = position
            }

            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
                if (state == ViewPager2.SCROLL_STATE_IDLE) {
                    performViewPager2.setCurrentItem(curPage)
                }
            }
        })

        safetyViewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            var curPage = 0
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                curPage = position
            }

            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
                if (state == ViewPager2.SCROLL_STATE_IDLE) {
                    performViewPager2.setCurrentItem(curPage)
                }
            }
        })

        extraPriceViewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            var curPage = 0
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                curPage = position
            }

            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
                if (state == ViewPager2.SCROLL_STATE_IDLE) {
                    performViewPager2.setCurrentItem(curPage)
                }
            }
        })

        comparsionRecycler.scrollToPosition(0)
    }

    private fun addItem() {
        adapter.addSpinnerItem(BrandItems.brandItems2)
//        adapter.count++
//        comparsionRecycler.adapter?.notifyItemInserted(adapter.count-1)
//        comparsionRecycler.smoothSnapToPosition(adapter.count-1)

//        adapter.count++
        comparsionRecycler.adapter?.notifyItemInserted(adapter.mitems.size-1)
        comparsionRecycler.smoothSnapToPosition(adapter.mitems.size-1)
    }

    private fun setCompRecImg(holder : RecyclerView.ViewHolder) {
        var spinner = holder.itemView.findViewById<Spinner>(R.id.trim_spinner)
        if (spinner.selectedItemPosition == spinner.adapter.count) {
            // 현재 트림까지 선택이 안된 상태
            // 추천 비교 차량 보여준다 (구매 신청 버튼, 예상 비용 뷰를 가려준다)
            holder.itemView.findViewById<AppCompatButton>(R.id.build_btn).visibility = View.GONE
            holder.itemView.findViewById<AppCompatButton>(R.id.purchase_consult_btn).visibility = View.GONE
            holder.itemView.findViewById<ConstraintLayout>(R.id.price_wrapper).visibility = View.GONE
            holder.itemView.findViewById<ConstraintLayout>(R.id.rec_car_wrapper_constraint).visibility = View.VISIBLE

            holder.itemView.findViewById<ViewPager2>(R.id.rec_car_img_vp).adapter?.let{
                (it as ComparRecImgAdapter).setLinks(BrandItems.baseImgLink)
                it.notifyDataSetChanged()
            }
        }
    }
 */