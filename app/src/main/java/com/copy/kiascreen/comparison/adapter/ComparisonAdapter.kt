package com.copy.kiascreen.comparison.adapter

import android.content.Context
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.copy.kiascreen.roomVo.BrandItems
import com.copy.kiascreen.roomVo.BrandItems.carImgLink
import com.copy.kiascreen.R
import com.copy.kiascreen.comparison.vo.*
import com.copy.kiascreen.databinding.ItemComparisonBinding
import com.copy.kiascreen.setSpinnerAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlin.math.abs

class ComparisonAdapter(val context: Context, item : MutableList<MutableList<Brand>>) : RecyclerView.Adapter<ComparisonAdapter.ComparisonHolder>() {
    private val brandItems = context.resources.getStringArray(R.array.brand_array)
    private var beforeSize = 0
    var mInterface : SearchRecyclerAddInterface? = null
//    var count = 1
    var holderMap : HashMap<Int, RecyclerView.ViewHolder> = HashMap()
    var mitems = item

    // 선택한 옵션 목록
    var selectedCarList = mutableListOf<SelectedOption>()

    private var basePrice = 0   // 첫번째 (기준이 되는) 아이템 트림 선택까지 했을 경우 발생하는 총 비용을 담는 변수
    private var isLastItemDeleted = false       // 마지막 아이템이 삭제 된 경우에는, 마지막에 선택한 아이템에 대한 정보들을 이전 항목으로 이전을 시켜줘야함 , true로 설정되는 경우는 -> 아이템이 3개인 경우에서 두번째 항목 삭제 했을 때
    private var lastSelectedItem  = LastSelectedItem()
    private var delClicked = false

    fun addSpinnerItem(addItems : MutableList<Brand>) {
        mitems.add(addItems)
    }

    fun resetItem(items : List<Brand>) {
        mitems.clear()
        mitems.add(items.toMutableList())
    }

    inner class ComparisonHolder(val binding : ItemComparisonBinding) : RecyclerView.ViewHolder(binding.root), RecImgInterface {
        fun bind(position : Int) {
            Log.d("spinnerTest", "position == ${position}")
            binding.carSpinner.isEnabled = false
            binding.engineSpinner.isEnabled = false
            binding.trimSpinner.isEnabled = false

            if (position == 0 || position == 1) {
                binding.addWrapperConstraint.visibility = View.VISIBLE
                binding.priceWrapper.visibility = View.GONE
            }

            if (position == 2) {
                binding.addWrapperConstraint.visibility = View.GONE
            }

            setBrandSpinnerAdapter(binding, position)
            setCarImgPager(binding, position)

            binding.recCarImgVp.adapter?.let {
                (binding.recCarImgVp.adapter as ComparRecImgAdapter).mInterface = this@ComparisonHolder
            }
        }

        // 아이템 추가 전에 이전 아이템을 다 선택했는지 파악한 후, 안했을 경우 해당 항목을 선택하라고 보여준다.
        fun onAddBtnClick() {
            binding.addCarImg.setOnClickListener {
                var stage = if(binding.brandSpinner.selectedItemPosition == binding.brandSpinner.adapter.count && adapterPosition >= 1) {
                    0
                } else if (binding.carSpinner.selectedItemPosition == binding.carSpinner.adapter.count) {
                    1
                } else if (binding.engineSpinner.selectedItemPosition == binding.engineSpinner.adapter.count) {
                    2
                } else if (binding.trimSpinner.selectedItemPosition == binding.trimSpinner.adapter.count){
                    3
                } else {
                    binding.addWrapperConstraint.visibility = View.GONE
                    4
                }
                Log.d("adapterTest", "carSpinnerSelected Item = ${binding.carSpinner.selectedItemPosition} , totalCount = ${binding.carSpinner.adapter.count}")
                mInterface?.onAddBtnClick(stage)

            }
        }

        // 아이템 삭제
        fun onDelBtnClick(position: Int) {
            binding.comparItemDelImg.setOnClickListener {
                Log.d("adapterTest", "====onDelBtnCLink, position = $position")
                beforeSize = mitems.size        // 아이템을 3개까지 선택 한 후 두번째 항목 삭제할 경우 갱신 않되는 문제를 처리 하기 위한 변수
                mitems.removeAt(position)
                mInterface?.onDelBtnClick(position) // 아이템 삭제 시, stickyHeader , vp2에 해당 위치의 아이템이 삭제됬음을 알려줘야 한다

                // 선택된 목록을 지워준다.
                if (selectedCarList.size > position){
                    Log.d("updateTest", "===onDelBtnClick test ==== position   ${position}}")
                    selectedCarList.removeAt(position)
                    mInterface?.setCompPager2Item(position)
                    mInterface?.delHeaderRecycler(position)
                }

                // 지우기 전의 크기가 3이고, 2번째 아이템을 지운 경우에는, 3번째 아이템의 클릭이 초기화 되기 때문
                if (beforeSize == 3 && position == 1) {
                    Log.d("adapterTest", "==== onDelBtnCLick, beforeSize = $beforeSize, position = $position")
                    isLastItemDeleted = true
                }

                delClicked = true
            }
        }

        fun onLastItemDeleted() {
            if (isLastItemDeleted) {        // 아이템을 3개 고른 상태에서, 두번째 아이템을 삭제한 경우 동작하는 함수
                // spinner 다시 재선택 해준다
                isLastItemDeleted = false
                setSpinnerItem(binding)
            }
        }

        override fun onImgClick(basePrice : Int, totalPrice : Int) {
            Log.d("imgAdapterTest", "===click event===")
            setSpinnerSelection(binding)        // 추천 차량 이미지 클릭 시 해당에 맞는 목록 활성화 해준다
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComparisonHolder {
        val binding = ItemComparisonBinding.inflate(LayoutInflater.from(context), parent, false)
        return ComparisonHolder(binding)
    }

    override fun getItemCount(): Int = mitems.size

    override fun onBindViewHolder(holder: ComparisonHolder, position: Int) {
        Log.d("adapterTest", "===onBindHolder, position = $position")
        Log.d("adapterTest", "===onBindHolder, adapterPosition = ${holder.adapterPosition}")
//        holder.bind(holder.adapterPosition)
        holder.bind(position)
        holder.onAddBtnClick()
        holder.onDelBtnClick(position)
        holder.onLastItemDeleted()
    }

    /*
        아이템 크기가, 화면 하나에 거의 꽉찰정도여서 화면 밖으로 스크롤 됬을경우(포커스를 받지 못할 경우 viewholder에서 갱신 작업이 이루어지지 않는 문제가 있음
        해당 아이템이 화면 밖으로 나갔을 경우(포커스 받지 못할 경우) 임시적으로 저장, 반대로 포커스를 받은 경우에는 삭제
     */
    override fun onViewAttachedToWindow(holder: ComparisonHolder) {
        Log.d("holderTest", "attach view, remove holderMap ${holder.adapterPosition}")
        holderMap.remove(holder.adapterPosition)
        super.onViewAttachedToWindow(holder)
    }

    override fun onViewDetachedFromWindow(holder: ComparisonHolder) {
        Log.d("holderTest", "detached from view, add holderMap ${holder.adapterPosition}")
        holderMap[holder.adapterPosition] = holder
        super.onViewDetachedFromWindow(holder)
    }

    // spinner 아이템 목록 설정 , 브랜드 엔진 컬러 트림 순으로 세팅된다.
    private fun setBrandSpinnerAdapter(binding : ItemComparisonBinding, adapterPosition : Int) {
        Log.d("spinnerTest", "setBrandSpinnerAdapter adapterPosition == ${adapterPosition}")
        val spinnerAdapter = if (adapterPosition == 0) {
            binding.brandSpinner.setBackgroundColor(context.resources.getColor(R.color.transparent))
            binding.brandSpinner.isEnabled = false
            binding.carSpinner.isEnabled = true
            getSpinnerAdapter(brandItems.toMutableList(), "기아")
        }
        else {
            // 아이템 2개 고르고 리셋했을 경우에, 보여야 하는 뷰가 안보이는 경우가 존재해서 추가 함
            binding.brandSpinner.setBackgroundDrawable(context.resources.getDrawable(R.drawable.spinner_background))
            binding.brandSpinner.isEnabled = true
            binding.carSpinner.isEnabled = false
            getSpinnerAdapter(brandItems.toMutableList(), "제조사 선택")
        }

        var positionItem = mitems[adapterPosition]


        binding.brandSpinner.setSpinnerAdapter(spinnerAdapter)

        binding.brandSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                var childItems : List<Car> = positionItem[0].cars
                when(position) {
                    0 -> {
                        // kia
                        childItems = positionItem[0].cars
                        binding.carSpinner.isEnabled = true
                    }

                    1 -> {
                        // 쉐보레
                        childItems = positionItem[1].cars
                        binding.carSpinner.isEnabled = true
                    }
                    2 -> {
                        // 현대
                        childItems = positionItem[2].cars
                        binding.carSpinner.isEnabled = true
                    }
                    else -> {

                    }

                }
                setCarSpinnerAdapter(childItems, binding, adapterPosition)
                saveLastSelectedSpinnerItem(adapterPosition, position, 0)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) { }

        }
    }

    private fun setCarSpinnerAdapter(carItems : List<Car>, binding : ItemComparisonBinding, adapterPosition: Int) {

        var carList = mutableListOf<String>()
        for(item in carItems) {
            carList.add(item.carName)
        }

        val spinnerAdapter = getSpinnerAdapter(carList, "차량 선택")

        binding.carSpinner.setSpinnerAdapter(spinnerAdapter)

        binding.carSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                var childItems : List<Engine> = carItems[0].engines
                var carPrice = 0
                when(position) {
                    0 -> {
                        childItems = carItems[0].engines
                        binding.engineSpinner.isEnabled = true
                        carPrice += carItems[0].carPrice
                    }

                    1 -> {
                        childItems = carItems[1].engines
                        binding.engineSpinner.isEnabled = true
                        carPrice += carItems[1].carPrice
                    }

                    2 -> {
                        childItems = carItems[2].engines
                        binding.engineSpinner.isEnabled = true
                        carPrice += carItems[2].carPrice
                    }
                    3 -> {
                        // 브랜드 선택을 다시 한 경우.
                        if (binding.brandSpinner.selectedItemPosition == binding.brandSpinner.adapter.count && adapterPosition >= 1) {
                            binding.carSpinner.isEnabled = false
                        }
                    }
                }

                setEngineSpinnerAdapter(childItems, binding, carPrice, adapterPosition)
                binding.purchaseConsultBtn.blockClick(binding.buildBtn)
                saveLastSelectedSpinnerItem(adapterPosition, position, 1)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                binding.engineSpinner.isEnabled = false
            }

        }
    }

    private fun setEngineSpinnerAdapter(engineItems : List<Engine>, binding : ItemComparisonBinding, carPrice : Int, adapterPosition: Int) {
        Log.d("spinnerTest", "=========== engineSpinner price = $carPrice")
        var engineList = mutableListOf<String>()
        for(item in engineItems) {
            engineList.add(item.engineName)
        }

        val spinnerAdapter = getSpinnerAdapter(engineList, "엔진 선택")

        /*
        // 확장 함수로 대체
        binding.engineSpinner.adapter = spinnerAdapter
        binding.engineSpinner.setSelection(spinnerAdapter.count)
        binding.engineSpinner.dropDownVerticalOffset = dpToPix(45f).toInt()
         */

        binding.engineSpinner.setSpinnerAdapter(spinnerAdapter)

        binding.engineSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                var childItems : List<Trim> = engineItems[0].trims
                var enginePrice = 0
                when(position) {
                    0 -> {
                        childItems = engineItems[0].trims
                        binding.trimSpinner.isEnabled = true
                        enginePrice = engineItems[0].enginePrice + carPrice
                    }

                    1 -> {
                        childItems = engineItems[1].trims
                        binding.trimSpinner.isEnabled = true
                        enginePrice =engineItems[1].enginePrice + carPrice
                    }

                    2 -> {
                        childItems = engineItems[2].trims
                        binding.trimSpinner.isEnabled = true
                        enginePrice =engineItems[2].enginePrice + carPrice
                    }
                    3 -> {
                        if (binding.carSpinner.selectedItemPosition == binding.carSpinner.adapter.count) {
                            binding.engineSpinner.isEnabled = false
                        }
                    }
                }
                showRecCarImg(binding, adapterPosition)
                setTrimSpinnerAdapter(childItems, binding, enginePrice, adapterPosition)

                binding.purchaseConsultBtn.blockClick(binding.buildBtn)
                saveLastSelectedSpinnerItem(adapterPosition, position, 2)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) { }

        }
    }

    private fun setTrimSpinnerAdapter(trimItems : List<Trim>, binding : ItemComparisonBinding, enginePrice : Int, adapterPosition: Int) {
        var trimList = mutableListOf<String>()
        for(item in trimItems) {
            trimList.add(item.trimName)
        }

        val spinnerAdapter = getSpinnerAdapter(trimList, "트림 선택")

        binding.trimSpinner.adapter = spinnerAdapter
        binding.trimSpinner.setSelection(spinnerAdapter.count)
        binding.trimSpinner.dropDownVerticalOffset = dpToPix(45f).toInt()

        binding.trimSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                var trimPrice = 0
                when(position) {
                    0 -> {
                        trimPrice = enginePrice + trimItems[0].trimPrice
                        calculatePrice(binding, trimPrice, adapterPosition)
                    }

                    1 -> {
                        trimPrice = enginePrice + trimItems[1].trimPrice
                        calculatePrice(binding, trimPrice, adapterPosition)
                    }

                    2 -> {
                        trimPrice = enginePrice + trimItems[2].trimPrice
                        calculatePrice(binding, trimPrice, adapterPosition)
                    }
                    3 -> {
                        if (binding.engineSpinner.selectedItemPosition == binding.engineSpinner.adapter.count) {
                            binding.trimSpinner.isEnabled = false
                        }
                    }
                }
                saveLastSelectedSpinnerItem(adapterPosition, position, 3)
                setCarImg(binding,adapterPosition)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) { }
        }
    }

    private fun getSpinnerAdapter(stringList : MutableList<String>, hint : String) : ArrayAdapter<String> {
        val spinnerAdapter = object : ArrayAdapter<String>(context, R.layout.item_spinner) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

                val view =  super.getView(position, convertView, parent)

                if (position == count) {
                    (view.findViewById<View>(R.id.tvItemSpinner) as TextView).text = ""
                    (view.findViewById<View>(R.id.tvItemSpinner) as TextView).hint = getItem(count)
                }

                return view
            }

            override fun getCount(): Int {
                return super.getCount() - 1
            }
        }

        spinnerAdapter.addAll(stringList.toMutableList())
        spinnerAdapter.add(hint)
        return spinnerAdapter
    }

    // 트림까지 선택 시, 가격 계산 및, 선택된 옵션을 저장, 바텀 시트 버튼 활성화
    private fun calculatePrice(binding : ItemComparisonBinding, trimPrice : Int, adapterPosition: Int) {
        // price wrpper visibility -> visible + 버튼 활성화
        if (adapterPosition >= 1) {
            // 아이템을 2개 이상 선택 했음, 메인뷰 바텀시트 버튼 활성화 시켜줘야 함.
            binding.recCarWrapperConstraint.visibility = View.GONE
            binding.buildBtn.visibility = View.VISIBLE
            binding.purchaseConsultBtn.visibility = View.INVISIBLE
            binding.buildBtn.visibility = View.INVISIBLE
            setComparePrice(binding, trimPrice)
            mInterface?.setCompareBtnEnable()
        }
        else if (adapterPosition == 0) {
            binding.purchaseConsultBtn.visibility = View.VISIBLE
            binding.buildBtn.visibility = View.VISIBLE
        }

        var afterPrice = (trimPrice + (trimPrice * 0.1).toInt())

        binding.priceWrapper.visibility = View.VISIBLE
        binding.basicPriceTv.text = trimPrice.toString()
        binding.afterPriceTv.text = afterPrice.toString()
        binding.purchaseConsultBtn.enableClick(binding.buildBtn)


        // 첫번째 항목(기준)을 다 선택하면, 추천 차량 이미지들을 나머지 항목들에게 보여줘야 한다
        if (adapterPosition == 0) {
            mInterface?.setRecCarImg()
            basePrice = trimPrice + (trimPrice * 0.1).toInt()
        }

        Log.d("updateTest", "===calculatePrice test ==== ")
        setSelectedItemList(binding, adapterPosition, afterPrice)
    }

    // 차량 이미지 설정
    private fun setCarImg(binding: ItemComparisonBinding, adapterPosition: Int) {
        if (adapterPosition == 0) {
            // 엔진 트림 선택 되었을 때 이미지 보여줌
            if (binding.carSpinner.selectedItemPosition == binding.carSpinner.adapter.count || binding.engineSpinner.selectedItemPosition == binding.engineSpinner.adapter.count
                || binding.trimSpinner.selectedItemPosition == binding.trimSpinner.adapter.count) {
                binding.blankCarImg.changeToBlankImg()
            }
            else {
                binding.blankCarImg.changeToCarImg(carImgLink[0])
            }
        }
        else {
            // 브랜드 엔지 트림 선택 되었을 때 이미지 보여줌
            if ( binding.brandSpinner.selectedItemPosition == binding.brandSpinner.adapter.count || binding.carSpinner.selectedItemPosition == binding.carSpinner.adapter.count
                || binding.engineSpinner.selectedItemPosition == binding.engineSpinner.adapter.count || binding.trimSpinner.selectedItemPosition == binding.trimSpinner.adapter.count) {
                binding.blankCarImg.changeToBlankImg()
            }
            else {
                binding.blankCarImg.changeToCarImg(carImgLink[1])
            }
        }
    }

    // 엔진까지 선택됬을 때, 추천 차량 이미지들을 보여준다.
    // 추천 차량 이미지를 보여주는 경우는 2가지. 1. 첫번째 아이템을 다 골랐을 때 -> 나머지 아이템들에게 추천 차량 이미지를 보여준다 / 2. 첫번째 아이템 이외의 아이템의 엔진을 선택했을 경우 -> 해당 선택한 엔진에 따른 추천 차량 이미지를 보여준다
    private fun showRecCarImg(binding : ItemComparisonBinding, adapterPosition: Int) {
        // comparRecImg의 item 다시 설정해 준 뒤, 보여줌

        if (adapterPosition >= 1 && binding.engineSpinner.selectedItemPosition != binding.engineSpinner.adapter.count) {
            // 가격 및 구매 상담, 견적 버튼을 가리고, 추천 차량 이미지 vp , tab은 보여준다
            binding.recCarWrapperConstraint.visibility = View.VISIBLE
            binding.priceWrapper.visibility = View.GONE
            binding.purchaseConsultBtn.visibility = View.GONE
            binding.buildBtn.visibility = View.GONE

            binding.recCarImgVp.adapter?.let {
                (it as ComparRecImgAdapter).setLinks(BrandItems.carImgLink)
                it.notifyDataSetChanged()
            }
        }
    }

    // 트림 선택 까지 다 했을 경우, 첫번째 아이템과 가격을 비교하여 표시해 준다.
    private fun setComparePrice(binding: ItemComparisonBinding, comPrice : Int) {
        if (basePrice < comPrice) {
            // 글자색 붉은색
            binding.afterPriceCompareTv?.let {
                it.visibility = View.VISIBLE
                it.text = abs(basePrice - comPrice).toString()
                it.setTextColor(context.resources.getColor(R.color.red))
            }

            binding.afterPriceCompareImg.apply {
                this.visibility = View.VISIBLE
                this.setImageResource(R.drawable.baseline_arrow_drop_up_24)
            }
        }
        else {
            // 글자색 파란색
            binding.afterPriceCompareTv?.let {
                it.visibility = View.VISIBLE
                it.text = abs(basePrice - comPrice).toString()
                it.setTextColor(context.resources.getColor(R.color.blue))
            }

            binding.afterPriceCompareImg.apply {
                this.visibility = View.VISIBLE
                this.setImageResource(R.drawable.baseline_arrow_drop_down_24)
            }
        }
    }

    // 추천 차량 pager을 설정해준다
    private fun setCarImgPager(binding : ItemComparisonBinding, position: Int) {
        if (position >= 1) {
            binding.buildBtn.visibility = View.GONE
            binding.purchaseConsultBtn.visibility = View.GONE
            binding.priceWrapper.visibility = View.GONE
            binding.comparItemDelImg.visibility = View.VISIBLE

            // ComparImgAdapter
            binding.recCarWrapperConstraint.visibility = View.VISIBLE
            binding.recCarImgVp.adapter = ComparRecImgAdapter(context)

            TabLayoutMediator(binding.recCarImgTab, binding.recCarImgVp) {_,_ -> }.attach()

            // 수평 스크롤을 중첩시킬 수 없어서 indicator를 클릭해서 이동하는 식으로 구현함
            binding.recCarImgTab.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    var des = tab?.position
                    binding.recCarImgVp.setCurrentItem(des!!, true)
                }
                override fun onTabUnselected(tab: TabLayout.Tab?) {}

                override fun onTabReselected(tab: TabLayout.Tab?) {}
            })
        }
        // 첫번째 항목은 추천차량이 존재해선 안됨
        else {
            binding.comparItemDelImg.visibility = View.GONE
        }
    }

    //마지막 아이템일 경우에는 임시로 저장을 해줘야 함(이전 아이템 삭제 시 마지막에 고른 아이템들에 대한 정보를 표기해 줘야 함)
    private fun saveLastSelectedSpinnerItem(adapterPosition: Int, selectedPosition : Int, stage : Int) {
        if (adapterPosition == 2) {
            when(stage) {
                0 -> lastSelectedItem.brand = selectedPosition
                1 -> lastSelectedItem.car = selectedPosition
                2 -> lastSelectedItem.engine = selectedPosition
                3 -> lastSelectedItem.trim = selectedPosition
            }
        }
    }

    // 이전에 선택한 항목들에 따라서 spinner 선택해줌., delay 안주면 선택이 안됨(이전 아이템 선택 안됬을 경우에 다음 아이템이 클릭 되는걸 막아놔서 그럼)
    private fun setSpinnerItem(binding: ItemComparisonBinding) {
        binding.brandSpinner.setSelection(lastSelectedItem.brand)

        binding.carSpinner.postDelayed({
            binding.carSpinner.setSelection(lastSelectedItem.car)
        }, 200)

        binding.engineSpinner.postDelayed({
            binding.engineSpinner.setSelection(lastSelectedItem.engine)
        }, 300)

        binding.trimSpinner.postDelayed({
            binding.trimSpinner.setSelection(lastSelectedItem.trim)
        },400)
    }

    // 추천 차량 클릭 시 해당 정보에 맞는 항목들을 클릭 시켜 준다
    private fun setSpinnerItem(binding: ItemComparisonBinding, selectedItem : LastSelectedItem) {
        binding.brandSpinner.setSelection(lastSelectedItem.brand)

        binding.carSpinner.postDelayed({
            binding.carSpinner.setSelection(lastSelectedItem.car)
        }, 200)

        binding.engineSpinner.postDelayed({
            binding.engineSpinner.setSelection(lastSelectedItem.engine)
        }, 300)

        binding.trimSpinner.postDelayed({
            binding.trimSpinner.setSelection(lastSelectedItem.trim)
        },400)
    }

    private fun setSpinnerSelection(binding: ItemComparisonBinding) {
        // 추천 차량 이미지 클릭 시 해당에 맞는 목록 활성화 해준다
        setSpinnerItem(binding, LastSelectedItem(1,2,3,1))
    }

    // 트림 선택 완료 시, 리스트에 추가해줌. 단, 이미 존재한 경우에는 교체 해줌
    private fun setSelectedItemList(binding: ItemComparisonBinding, adapterPosition: Int, totalPrice : Int){
        Log.d("adaterTest", "===setSelectedItemList test ==== adapterPosition   ${adapterPosition}}")
        Log.d("updateTest", "===setSelectedItemList test ==== adapterPosition   ${adapterPosition}}")
        var brand : Brand = if (adapterPosition == 0) {
            mitems[adapterPosition].first()
        }
        else {
            mitems[adapterPosition][binding.brandSpinner.selectedItemPosition]
        }

        var car : Car = brand.cars[binding.carSpinner.selectedItemPosition]
        var engine : Engine = car.engines[binding.engineSpinner.selectedItemPosition]
        var trim : Trim = engine.trims[binding.trimSpinner.selectedItemPosition]

        var selectedCar = SelectedOption(brand.brandName, car.carName, engine.engineName, trim.trimName, totalPrice)

        if (adapterPosition >= selectedCarList.size) {
            selectedCarList.add(selectedCar)
            Log.d("adaterTest", "===setBrandList test ==== add   ${selectedCarList.size}}")
        }
        else if(selectedCarList.isNotEmpty() && selectedCarList[adapterPosition] != null) {
            selectedCarList[adapterPosition] = selectedCar
            Log.d("adaterTest", "===setBrandList test ==== set   ${selectedCarList.size}}")
        }

        // headerRecycler 아이템 갱신 및 비교 내역 아이템 갱신
//        mInterface?.addHeaderRecycler(selectedCar, adapterPosition)
//        mInterface?.setCompPager2Item(adapterPosition)
        // headerRecycler 아이템 갱신 및 비교 내역 아이템 갱신
        // 두번 째 아이템 삭제 시, 마지막 아이템이 두번째 위치로 오게 되면서, update 해주는 함수가 remove 함수 후에 호출되고 있음.
        // 막기 위해서, 아이템 삭제 시 flag를 true로 두고, 한번 해당 함수가 호출되면 delflag 해제, 다음에 갱신 및 추가시 수정 먹도로 함
        if (beforeSize != 3 || adapterPosition != 1) {
            mInterface?.addHeaderRecycler(selectedCar, adapterPosition)
            mInterface?.setCompPager2Item(adapterPosition)
        }
        else if (!delClicked) {
            mInterface?.addHeaderRecycler(selectedCar, adapterPosition)
            mInterface?.setCompPager2Item(adapterPosition)
        }

        delClicked = false
    }

    fun getSelectedCar() : MutableList<SelectedOption> {
        Log.d("adapterTest", "selectedCarListSize = ${selectedCarList.size}")
        return selectedCarList
    }

    // 확장 함수로 대체 됨
    private fun dpToPix(dp : Float) : Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            context.resources.displayMetrics
        )
    }

    fun setAddInterface(addInterface : SearchRecyclerAddInterface) {
        this.mInterface = addInterface
    }

}


