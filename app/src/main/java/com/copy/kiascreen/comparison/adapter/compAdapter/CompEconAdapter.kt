package com.copy.kiascreen.comparison.adapter.compAdapter

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.copy.kiascreen.R
import com.copy.kiascreen.comparison.BuildCompActivity
import com.copy.kiascreen.comparison.dialogfragment.FuelFragment
import com.copy.kiascreen.comparison.dialogfragment.InsuranceFragment
import com.copy.kiascreen.comparison.dialogfragment.InsuranceGradeFragment
import com.copy.kiascreen.comparison.vo.CompItem
import com.copy.kiascreen.comparison.vo.CompListItems
import com.copy.kiascreen.comparison.vo.EconItem
import com.copy.kiascreen.databinding.ItemCompDetail2Binding
import java.util.*
import kotlin.reflect.KProperty1
import kotlin.reflect.full.memberProperties

// 경제성 어뎁터
class CompEconAdapter(val context : Context) : RecyclerView.Adapter<CompEconAdapter.EconHolder>(){
    private var data = CompListItems.compItemList.toMutableList()
    // 기준인 첫번째 아이템과 2~3 번째 아이템의 다른 필드들을 담는 List
    private var differentFields  : List<KProperty1<EconItem, *>>? = null
    // spinner 아이템
    private var yearString = context.resources.getStringArray(R.array.driving_period_array)
    private var distanceString = context.resources.getStringArray(R.array.driving_distance_array)
    // 첫번째 아이템과 다른 필드값을 가질 경우 differentField를 Map 형식으로 저장
    private var differentFieldsMap : kotlin.collections.HashMap< Int ,List<KProperty1<EconItem, *>>> = kotlin.collections.HashMap<Int, List<KProperty1<EconItem, *>>>()
    // 삭제, 초기화 후 기존의 값들이 존재하는 경우가 있어서 조절하기 위한 flag
    private var isUpdatedFlag = false
    // true -> 기본 값, 다른 필드들 강조, false -> 다른 필드 강조 x
    private var diffFieldsFlag = true

    // 첫번째 아이템과 나머지 아이템들의 필드값이 다른 경우, 해당 layout의 배경을 칠해준다
    private val diffAction = {
            fieldName : String, binding :  ItemCompDetail2Binding ->
        val color = context.resources.getColor(R.color.menu_wrapper)
        when(fieldName) {
            "cfe" -> binding.itemCompCfcLinear.setBackgroundColor(color)
            "cityFe"-> binding.itemCompCityFcLinear.setBackgroundColor(color)
            "hfe" -> binding.itemCompHfcLinear.setBackgroundColor(color)
            "feGrade" -> binding.itemCompFegLinear.setBackgroundColor(color)
            "insuranceGrade" -> binding.itemCompInsuranceLinear.setBackgroundColor(color)
            "co2" -> binding.itemCompC02Linear.setBackgroundColor(color)
            "maintance" -> binding.itemCompMaintacneLinear.setBackgroundColor(color)
            else -> {}
        }
    }

    init {
        showDiffItems(data[0].econ)
    }

    fun addEconData(newData : CompItem) {
        Log.d("compTest", "CompEconAdapter addData called")
        this.data.add(newData)
        showDiffItems(data[0].econ)
        Log.d("compTest", "CompEconAdapter after add data size == ${data.size}")
        this.notifyItemInserted(data.size - 1)
    }

    fun updatedEconData(updatedData : CompItem, position: Int) {
        Log.d("compTest", "CompEconAdapter updatedPerFormData called")
        this.data[position] = updatedData
        showDiffItems(data[0].econ)
        Log.d("compTest", "CompEconAdapter after update data size == ${data.size} and position == ${position}")
        this.notifyItemChanged(position)
    }

    // 아이템 삭제시, 삭제했다는 flag를 조정한다 (아이템 삭제 후 추가 시 및 초기화 선택 후 추가 시, 같은 필드여도 배경색이 남는 경우가 존재하는 경우가 존재했음)
    fun deletePerFormData(position: Int) {
        Log.d("compTest", "CompEconAdapter after deletePerFormData === data size == ${data.size} and position == ${position}")
        isUpdatedFlag = true
        this.data.removeAt(position)
        this.notifyDataSetChanged()
    }

    fun setEconDataAfterReset() {
        Log.d("compTest", "CompEconAdapter setEconDataAfterReset called")
        data = CompListItems.compItemList.toMutableList()
        Log.d("compTest", "CompEconAdapter setEconDataAfterReset data size = ${data.size}")
        showDiffItems(data[0].econ)
        notifyDataSetChanged()
    }

    // activity에서 다른 항목 표기 switch의 value값을 가져와서, flag를 조정.
    fun toggleDiffItems(flag : Boolean) {
        this.diffFieldsFlag = flag
        this.notifyItemRangeChanged(0, 3)
    }

    inner class EconHolder(val binding : ItemCompDetail2Binding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item : EconItem) {
            initData(binding, item)
            initSpinner(binding)
        }

        // 모든 항목의 배경색 초기화 (flag를 통해서 제어하고 있음)
        fun resetBgInWhite() {
            Log.d("compTest", "=== CompEconAdapter resetBgInWhite called position ")
            resetBgColor(binding)
        }

        // 2~3 번째 항목에 대한 다른 항목 배경색 처리
        fun setDiffItemBackground(position: Int) {
            Log.d("compTest", "=== CompEconAdapter setDiffItemBackground bind called position = $position===")
            val diffItems = differentFieldsMap[position]

            Log.d("compTest", "=== CompEconAdapter setDiffItemBackground diffItems.size = ${diffItems?.size}===")
            diffItems?.forEach {
                diffAction(it.name, binding)
            }

        }

        // diffItems는 첫번째 항목과 나머지 항목을 비교하는 거라서, map에 0번 인덱스로 빼올 수 있는 데이터가 없음.
        // 첫번째 아이템일 경우 try catch문을 통해서 outofindex일 경우 색만 칠해주고 탈출함. -> for문으로 size 만큼 1번 인덱스부터 선회하는 식으로 refactor 해야 함.
        fun setFirstItemBackground() {
            Log.d("compTest", "=== CompEconAdapter setFirstItemBackground bind called ===")
            try {
                /*
                var diffItems = differentFieldsMap[1]

                diffItems?.forEach {
                    diffAction(it.name, binding)
                }

                diffItems = differentFieldsMap[2]
                diffItems?.forEach {
                    diffAction(it.name, binding)
                }

                 */

                for (index in 1 until data.size) {
                    differentFieldsMap[index]?.forEach {
                        diffAction(it.name, binding)
                    }
                }
            }catch (e : Exception) {
                Log.d("compTest", "=== CompEconAdapter setFirstItemBackground exception error : ${e.message} ===")
            }
        }

        fun setGuideClickListener() {
            binding.insuranceImg.setOnClickListener {
                try {
                    val activity = context as BuildCompActivity
                    InsuranceFragment().show(activity.supportFragmentManager, null)
                } catch (e : Exception) {
                    Log.d("dialogTest", "CompEconAdapter insuranceDialog Exception = ${e.message}")
                }
            }

            binding.fuelCostImg.setOnClickListener {
                try {
                    val activity = context as BuildCompActivity
                    FuelFragment().show(activity.supportFragmentManager, null)
                } catch (e : Exception) {
                    Log.d("dialogTest", "CompEconAdapter Fuel Exception = ${e.message}")
                }
            }

            binding.insuranceGuideImg.setOnClickListener {
                try {
                    val activity = context as BuildCompActivity
                    InsuranceGradeFragment().show(activity.supportFragmentManager, null)
                } catch (e : Exception) {
                    Log.d("dialogTest", "CompEconAdapter Fuel Exception = ${e.message}")
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EconHolder {
        val binding = ItemCompDetail2Binding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EconHolder(binding)
    }

    override fun getItemCount(): Int {
        Log.d("compTest", "CompEconAdapter getItemCount size = ${data.size}")
        return data.size
    }

    override fun onBindViewHolder(holder: EconHolder, position: Int) {
        Log.d("compTest", "CompEconAdapter onBIndeVieHolder position = $position")
        holder.bind(data[position].econ)
        holder.resetBgInWhite()

        if (diffFieldsFlag) {
            if (position == 0) {
                holder.setFirstItemBackground()
            }

            if (position > 0) {
                holder.setDiffItemBackground(position)
            }
        }

        /*
        if (position == 0) {
            holder.setFirstItemBackground()
        }

        if (position > 0) {
            holder.setDiffItemBackground(position)
        }

         */

        holder.setGuideClickListener()
    }

    private fun initData(binding : ItemCompDetail2Binding, item : EconItem) {
        binding.cfcContentTv.text = item.cfe
        binding.cityFcContentTv.text = item.cityFe
        binding.hfcContentTv.text = item.hfe
        binding.fegContentTv.text = item.feGrade
        binding.insuranceContentTv.text = item.insuranceGrade
        binding.co2ContentTv.text = item.co2
        binding.maintacneContentTv.text = item.maintance
    }

    private fun initSpinner(binding : ItemCompDetail2Binding) {
        /*
        val periodArray = yearString.toMutableList().setStringArray(context)
        val distanceArray = distanceString.toMutableList().setStringArray(context)

        binding.usedPeriodSpinner.setSpinnerAdapter(periodArray)
        binding.drivingDistanceSpinner.setSpinnerAdapter(distanceArray)

         */

        binding.usedPeriodSpinner.adapter = ArrayAdapter(context, R.layout.item_spinner, yearString)
        binding.drivingDistanceSpinner.adapter = ArrayAdapter(context, R.layout.item_spinner, distanceString)

        setPeriodSpinnerListener(binding)
        setDistanceSpinnerListener(binding)
    }

    private fun setPeriodSpinnerListener(binding: ItemCompDetail2Binding) {
        binding.usedPeriodSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                binding.carTaxContentTv.text = (13000 * (position + 1)).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
    }

    private fun setDistanceSpinnerListener(binding : ItemCompDetail2Binding) {
        binding.drivingDistanceSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                binding.carInsuranceContentTv.text = (100000 * ((position + 1) * 0.1)).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }
    }

    private fun getSpinnerAdapter(stringList : MutableList<String>) : ArrayAdapter<String> {
        val spinnerAdapter = object : ArrayAdapter<String>(context, R.layout.item_spinner) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

                val view =  super.getView(position, convertView, parent)

                if (position == count) {
                    (view.findViewById<View>(R.id.tvItemSpinner) as TextView).text = ""
                    (view.findViewById<View>(R.id.tvItemSpinner) as TextView).hint = getItem(0)
                }

                return view
            }

            override fun getCount(): Int {
                return super.getCount() - 1
            }
        }
        spinnerAdapter.addAll(stringList.toMutableList())
        return spinnerAdapter
    }
    // 목록 삭제나, 다른 항목 보기 해제 시, 모든 아이템들의 배경색 초기화 시켜준다
    // 아이템 삭제 후 추가 시, 필드 값이 같아도 색이 칠해져 있는 경우가 발생해서 처리해 줌.
    private fun resetBgColor(binding: ItemCompDetail2Binding) {
        if (isUpdatedFlag || !diffFieldsFlag) {
            binding.apply {
                itemCompCfcLinear.setBackgroundColor(Color.WHITE)
                itemCompFegLinear.setBackgroundColor(Color.WHITE)
                itemCompCityFcLinear.setBackgroundColor(Color.WHITE)
                itemCompHfcLinear.setBackgroundColor(Color.WHITE)
                itemCompInsuranceLinear.setBackgroundColor(Color.WHITE)
                itemCompCityFcLinear.setBackgroundColor(Color.WHITE)
                itemCompMaintacneLinear.setBackgroundColor(Color.WHITE)
                itemCompC02Linear.setBackgroundColor(Color.WHITE)
            }
        }
    }

    // item항목들 중, field값을 비교해서 다른 필드들을 (index , diffentFieldList)의 맵형태로 저장한다
    private fun showDiffItems(item : EconItem) {
        Log.d("compTest", "CompEconAdapter == showDiffItems ==")
        try {
            for (index in 1 until data.size) {
                differentFields = EconItem::class.memberProperties.filter {
                    Log.d("compTest", "CompEconAdapter index = $index first item = ${item.insuranceGrade} second item ${data[index].econ.insuranceGrade}")
                    !Objects.equals(it.get(item), it.get(data[index].econ))
                }


                differentFields?.let { differentFieldsMap.put(index, it)  }
            }
        }
        catch (e : Exception) {
            Log.d("compTest", " CompEconAdapter showDiffItems Exception  showDiffItems == ${e.message}")
        }
    }

}