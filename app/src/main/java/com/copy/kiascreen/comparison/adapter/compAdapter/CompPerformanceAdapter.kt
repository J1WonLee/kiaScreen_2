package com.copy.kiascreen.comparison.adapter.compAdapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.text.Layout
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.copy.kiascreen.R
import com.copy.kiascreen.comparison.vo.CompItem
import com.copy.kiascreen.comparison.vo.CompListItems
import com.copy.kiascreen.comparison.vo.PerformanceItem
import com.copy.kiascreen.databinding.ItemCompDetail1Binding
import java.util.*
import kotlin.collections.HashMap
import kotlin.reflect.KProperty1
import kotlin.reflect.full.memberProperties

// 성능, 재원 adapter
class CompPerformanceAdapter(private val context : Context) : RecyclerView.Adapter<CompPerformanceAdapter.PerformanceHolder>(){
    var data = CompListItems.compItemList.toMutableList()
    // 기준인 첫번째 아이템과 2~3 번째 아이템의 다른 필드들을 일시적으로 담는 List
    private var differentFields  : List<KProperty1<PerformanceItem, *>>? = null
    // 일시적으로 담은 differentField를 Map 형식으로 저장
    private var differentFieldsMap : kotlin.collections.HashMap< Int ,List<KProperty1<PerformanceItem, *>>> = kotlin.collections.HashMap<Int, List<KProperty1<PerformanceItem, *>>>()
    private var isUpdatedFlag = false       // 삭제, 초기화 후 기존의 값들이 존재하는 경우가 있어서 조절하기 위한 flag
    private var diffFieldsFlag = true      // true -> 기본 값, 다른 필드들 강조, false -> 다른 필드 강조 x

    // 첫번째 아이템과 나머지 아이템들의 필드값이 다른 경우, 해당 layout의 배경을 칠해준다
    private val diffAction = {
        fieldName : String, binding :  ItemCompDetail1Binding ->
        val color = context.resources.getColor(R.color.menu_wrapper)
        when(fieldName) {
            "width" -> binding.itemCompWidthLinear.setBackgroundColor(color)
            "height"-> binding.itemCompHeightLinear.setBackgroundColor(color)
            "engine" -> binding.itemCompCfcLinear.setBackgroundColor(color)
            "fuel" -> binding.itemCompCityFcLinear.setBackgroundColor(color)
            "gear" -> binding.itemCompHfcLinear.setBackgroundColor(color)
            "maxOutput" -> binding.itemCompFegLinear.setBackgroundColor(color)
            "maxTorque" -> binding.itemCompMaxTorqueLinear.setBackgroundColor(color)
            "cc"        -> binding.itemCompCcLinear.setBackgroundColor(color)
            "dm"        -> binding.itemCompDmLinear.setBackgroundColor(color)
            "fuelTank"  -> binding.itemCompTankLinear.setBackgroundColor(color)
            "batteryType" -> binding.itemCompBatteryLinear.setBackgroundColor(color)
            "maxBatteryCapacity" -> binding.itemCompBcLinear.setBackgroundColor(color)
            "driveDistance" -> binding.itemCompDriveDistanceLinear.setBackgroundColor(color)
            "rapidCharge" -> binding.itemCompCtRapidLinear.setBackgroundColor(color)
            "normalCharge" -> binding.itemCompCtNormalLinear.setBackgroundColor(color)
            "overallLength" -> binding.itemCompOlLinear.setBackgroundColor(color)
            "wheelB" -> binding.itemCompWbLinear.setBackgroundColor(color)
            "frontT" -> binding.itemCompFtLinear.setBackgroundColor(color)
            "backT" -> binding.itemCompBtLinear.setBackgroundColor(color)
            "tire" -> binding.itemCompTireLinear.setBackgroundColor(color)
            "weight" -> binding.itemCompCwLinear.setBackgroundColor(color)
            else -> {}
        }
    }

    init {
         showDiffItems(data[0].perform)
    }

    // 아이템 향목 리셋 현재는 기준 아이템 하나를 고정적으로 넣어주고 있기 때문에 해당 고정 아이템으로 설정을 해주고있음
    // 아이ㅏ템을 리셋 시켜 줘야함. 만약 데이터를 받아온다면 adapter를 null로 넣어줘야 할듯 함. (7/20 테스트 해볼 것.)
    fun setPerfromDataAfterReset() {
        Log.d("compTest", "CompPerformanceAdapter setPerfromDataAfterReset called")
        data = CompListItems.compItemList.toMutableList()
        Log.d("compTest", "CompPerformanceAdapter setPerfromDataAfterReset data size = ${data.size}")
        showDiffItems(data[0].perform)
        notifyDataSetChanged()
    }

    fun addPerFormData(newData : CompItem) {
        Log.d("compTest", "CompPerformanceAdapter addData called")
        this.data.add(newData)
        showDiffItems(data[0].perform)
        Log.d("compTest", "CompPerformanceAdapter after add data size == ${data.size}")
        this.notifyItemInserted(data.size - 1)
    }

    fun updatedPerFormData(updatedData : CompItem, position: Int) {
        Log.d("compTest", "CompPerformanceAdapter updatedPerFormData called")
        this.data[position] = updatedData
        showDiffItems(data[0].perform)
        Log.d("compTest", "CompPerformanceAdapter after update data size == ${data.size} and position == ${position}")
        this.notifyItemChanged(position)
    }

    // 아이템 삭제시, 삭제했다는 flag를 조정한다 (아이템 삭제 후 추가 시 및 초기화 선택 후 추가 시, 같은 필드여도 배경색이 남는 경우가 존재하는 경우가 존재했음)
    fun deletePerFormData(position: Int) {
        this.data.removeAt(position)
        isUpdatedFlag = true
        showDiffItems(data[0].perform)
        Log.d("compTest", "CompPerformanceAdapter deletePerFormData data size == ${data.size} and position == ${position}")
        this.notifyDataSetChanged()
    }

    // activity에서 다른 항목 표기 switch의 value값을 가져와서, flag를 조정.
    fun toggleDiffItems(flag : Boolean) {
        Log.d("switchTest", "compPerformanceAdapter toggleDiffItems called flag = ${flag}")
        this.diffFieldsFlag = flag
        this.notifyItemRangeChanged(0, 3)
    }

    inner class PerformanceHolder(val binding : ItemCompDetail1Binding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item : PerformanceItem, position: Int) {
            Log.d("compTest", "CompPerformanceAdapter PerformanceHolder bind called")
            initData(binding, item)
        }

        // 모든 항목의 배경색 초기화 (flag를 통해서 제어하고 있음)
        fun resetBgInWhite() {
            Log.d("compTest", "=== CompPerformanceAdapter resetBgInWhite called position ")
            resetBgColor(binding)
        }

        // 2~3 번째 항목에 대한 다른 항목 배경색 처리
        fun setDiffItemBackground(position: Int) {
            Log.d("compTest", "=== CompPerformanceAdapter setDiffItemBackground bind called ===")
            val diffItems = differentFieldsMap[position]

            diffItems?.forEach {
                diffAction(it.name, binding)
            }
        }

        // diffItems는 첫번째 항목과 나머지 항목을 비교하는 거라서, map에 0번 인덱스로 빼올 수 있는 데이터가 없음.
        // 첫번째 아이템일 경우 try catch문을 통해서 outofindex일 경우 색만 칠해주고 탈출함. -> for문으로 size 만큼 1번 인덱스부터 선회하는 식으로 refactor 해야 함.
        fun setFirstItemBackground() {
            Log.d("compTest", "=== CompPerformanceAdapter setFirstItemBackground bind called ===")
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
                Log.d("compTest", "=== CompPerformanceAdapter setFirstItemBackground exception error : ${e.message} ===")
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PerformanceHolder {
        val binding = ItemCompDetail1Binding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PerformanceHolder(binding)
    }

    override fun getItemCount() : Int {
//        Log.d("comptest", "CompPerformanceAdapter size = ${data.size}")
        return data.size
    }

    override fun onBindViewHolder(holder: PerformanceHolder, position: Int) {
        Log.d("compTest", "========= CompPerformanceAdapter PerformanceHolder onBindViewHolder position = $position ======")
        Log.d("switchTest", "compPerformanceAdapter toggleDiffItems called flag = ${diffFieldsFlag}")
        holder.bind(data[position].perform, position)
        holder.resetBgInWhite()

        /*
        if (position == 0 ) {
            holder.setFirstItemBackground()
        }

        if (position > 0) {
            holder.setDiffItemBackground(position)
        }

         */

        if (diffFieldsFlag) {
            if (position == 0) {
                holder.setFirstItemBackground()
            }

            if (position > 0) {
                holder.setDiffItemBackground(position)
            }
        }
    }


    private fun initData(binding : ItemCompDetail1Binding, item : PerformanceItem) {
        Log.d("compTest", "========= CompPerformanceAdapter initData update flag = $isUpdatedFlag ===========")
        binding.apply {
            engineContentTv.text = item.engine
            fuelContentTv.text = item.fuel
            gearContentTv.text = item.gear
            outputContentTv.text = item.maxOutput
            torqueContentTv.text = item.maxTorque
            ccContentTv.text = item.cc
            dmContentTv.text = item.dm
            tankContentTv.text = item.fuelTank
            batteryContentTv.text = item.batteryType
            bcContentTv.text = item.maxBatteryCapacity
            driveDistanceContentTv.text = item.driveDistance
            driveCtRapidContentTv.text = item.rapidCharge
            driveCtNormalContentTv.text = item.normalCharge
            olContentTv.text = item.overallLength
            widthContentTv.text = item.width
            heightContentTv.text = item.height
            wbContentTv.text = item.wheelB
            ftContentTv.text = item.frontT
            btContentTv.text = item.backT
            tireContentTv.text = item.tire
            cwContentTv.text = item.weight
        }
    }

    // 목록 삭제나, 다른 항목 보기 해제 시, 모든 아이템들의 배경색 초기화 시켜준다
    // 아이템 삭제 후 추가 시, 필드 값이 같아도 색이 칠해져 있는 경우가 발생해서 처리해 줌.
    private fun resetBgColor(binding: ItemCompDetail1Binding) {
        if (isUpdatedFlag || !diffFieldsFlag) {
            binding.apply {
                itemCompWidthLinear.setBackgroundColor(Color.WHITE)
                itemCompHeightLinear.setBackgroundColor(Color.WHITE)
                itemCompCfcLinear.setBackgroundColor(Color.WHITE)
                itemCompCityFcLinear.setBackgroundColor(Color.WHITE)
                itemCompHfcLinear.setBackgroundColor(Color.WHITE)
                itemCompFegLinear.setBackgroundColor(Color.WHITE)
                itemCompMaxTorqueLinear.setBackgroundColor(Color.WHITE)
                itemCompCcLinear.setBackgroundColor(Color.WHITE)
                itemCompDmLinear.setBackgroundColor(Color.WHITE)
                itemCompTankLinear.setBackgroundColor(Color.WHITE)
                itemCompBatteryLinear.setBackgroundColor(Color.WHITE)
                itemCompBcLinear.setBackgroundColor(Color.WHITE)
                itemCompDriveDistanceLinear.setBackgroundColor(Color.WHITE)
                itemCompCtRapidLinear.setBackgroundColor(Color.WHITE)
                itemCompCtNormalLinear.setBackgroundColor(Color.WHITE)
                itemCompOlLinear.setBackgroundColor(Color.WHITE)
                itemCompWbLinear.setBackgroundColor(Color.WHITE)
                itemCompFtLinear.setBackgroundColor(Color.WHITE)
                itemCompBtLinear.setBackgroundColor(Color.WHITE)
                itemCompTireLinear.setBackgroundColor(Color.WHITE)
                itemCompCwLinear.setBackgroundColor(Color.WHITE)
            }
        }
    }



    // 비교 기준은 첫번째 항목
    // 서로 내용이 다른 부분 음영 준다. 아이템은 무조건 최소 2개 이상
    // item항목들 중, field값을 비교해서 다른 필드들을 (index , diffentFieldList)의 맵형태로 저장한다
    private fun showDiffItems(item : PerformanceItem) {
        Log.d("compTest", "CompPerformanceAdapter == showDiffItems ==")
        try {
            for (index in 1 until data.size) {
                differentFields = PerformanceItem::class.memberProperties.filter {
                    !Objects.equals(it.get(item), it.get(data[index].perform))
                }

                differentFields?.let {
                    it.forEach { item -> Log.d("compTest", "showDiffItems = ${item.name}") }
                    differentFieldsMap.put(index, it) }
            }
        }
        catch (e : Exception) {
            Log.d("compTest", " compPerformanceAdapter showDiffItems Exception  showDiffItems == ${e.message}")
        }
    }
}