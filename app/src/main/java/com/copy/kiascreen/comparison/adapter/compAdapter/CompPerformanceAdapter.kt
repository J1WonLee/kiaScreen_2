package com.copy.kiascreen.comparison.adapter.compAdapter

import android.annotation.SuppressLint
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
class CompPerformanceAdapter : RecyclerView.Adapter<CompPerformanceAdapter.PerformanceHolder>(){
    var data = CompListItems.compItemList.toMutableList()
    // 기준인 첫번째 아이템과 2~3 번째 아이템의 다른 필드들을 일시적으로 담는 List
    private var differentFields  : List<KProperty1<PerformanceItem, *>>? = null
    // 일시적으로 담은 differentField를 Map 형식으로 저장
    private var differentFieldsMap : kotlin.collections.HashMap< Int ,List<KProperty1<PerformanceItem, *>>> = kotlin.collections.HashMap<Int, List<KProperty1<PerformanceItem, *>>>()
    private var isUpdatedFlag = false

    @SuppressLint("ResourceAsColor")
    private val diffAction = {
        fieldName : String, binding :  ItemCompDetail1Binding ->
            when(fieldName) {
                "width" -> binding.itemCompWidthLinear.setBackgroundColor(R.color.teal_200)
                "height"-> binding.itemCompHeightLinear.setBackgroundColor(R.color.teal_200)
                "engine" -> binding.itemCompCfcLinear.setBackgroundColor(R.color.teal_200)
                "fuel" -> binding.itemCompCityFcLinear.setBackgroundColor(R.color.teal_200)
                "gear" -> binding.itemCompHfcLinear.setBackgroundColor(R.color.teal_200)
                "maxOutput" -> binding.itemCompFegLinear.setBackgroundColor(R.color.teal_200)
                "maxTorque" -> binding.itemCompMaxTorqueLinear.setBackgroundColor(R.color.teal_200)
                "cc"        -> binding.itemCompCcLinear.setBackgroundColor(R.color.teal_200)
                "dm"        -> binding.itemCompDmLinear.setBackgroundColor(R.color.teal_200)
                "fuelTank"  -> binding.itemCompTankLinear.setBackgroundColor(R.color.teal_200)
                "batteryType" -> binding.itemCompBatteryLinear.setBackgroundColor(R.color.teal_200)
                "maxBatteryCapacity" -> binding.itemCompBcLinear.setBackgroundColor(R.color.teal_200)
                "driveDistance" -> binding.itemCompDriveDistanceLinear.setBackgroundColor(R.color.teal_200)
                "rapidCharge" -> binding.itemCompCtRapidLinear.setBackgroundColor(R.color.teal_200)
                "normalCharge" -> binding.itemCompCtNormalLinear.setBackgroundColor(R.color.teal_200)
                "overallLength" -> binding.itemCompOlLinear.setBackgroundColor(R.color.teal_200)
                "wheelB" -> binding.itemCompWbLinear.setBackgroundColor(R.color.teal_200)
                "frontT" -> binding.itemCompFtLinear.setBackgroundColor(R.color.teal_200)
                "backT" -> binding.itemCompBtLinear.setBackgroundColor(R.color.teal_200)
                "tire" -> binding.itemCompTireLinear.setBackgroundColor(R.color.teal_200)
                "weight" -> binding.itemCompCwLinear.setBackgroundColor(R.color.teal_200)
                else -> {}
            }
    }

    init {
         showDiffItems(data[0].perform)
    }

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

    fun deletePerFormData(position: Int) {
        this.data.removeAt(position)
        isUpdatedFlag = true
        showDiffItems(data[0].perform)
        Log.d("compTest", "CompPerformanceAdapter deletePerFormData data size == ${data.size} and position == ${position}")
        this.notifyDataSetChanged()
    }

    inner class PerformanceHolder(val binding : ItemCompDetail1Binding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item : PerformanceItem, position: Int) {
            Log.d("compTest", "CompPerformanceAdapter PerformanceHolder bind called")
            initData(binding, item)
        }

        fun resetBgInWhite() {
            Log.d("compTest", "=== CompPerformanceAdapter resetBgInWhite called position ")
            resetBgColor(binding)
        }

        fun setDiffItemBackground(position: Int) {
            Log.d("compTest", "=== CompPerformanceAdapter setDiffItemBackground bind called ===")
            val diffItems = differentFieldsMap[position]

            diffItems?.forEach {
                diffAction(it.name, binding)
            }
        }

        fun setFirstItemBackground() {
            Log.d("compTest", "=== CompPerformanceAdapter setFirstItemBackground bind called ===")
            try {
                var diffItems = differentFieldsMap[1]

                diffItems?.forEach {
                    diffAction(it.name, binding)
                }

                diffItems = differentFieldsMap[2]
                diffItems?.forEach {
                    diffAction(it.name, binding)
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
        holder.bind(data[position].perform, position)
        holder.resetBgInWhite()

        if (position == 0) {
            holder.setFirstItemBackground()
        }

        if (position > 0) {
            holder.setDiffItemBackground(position)
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
            btContentTv.text = item.batteryType
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

    private fun resetBgColor(binding: ItemCompDetail1Binding) {
        if (isUpdatedFlag) {
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
    private fun showDiffItems(item : PerformanceItem) {
        Log.d("compTest", "CompPerformanceAdapter == showDiffItems ==")
        try {
            for (index in 1 until data.size) {
                differentFields = PerformanceItem::class.memberProperties.filter {
                    !Objects.equals(it.get(item), it.get(data[index].perform))
                }

                differentFields!!.forEach {
//                    Log.d("compTest", "compPerformanceAdapter == index = $index comp test diffentFields == ${it.name}")
                }

                differentFields?.let { differentFieldsMap.put(index, it)  }
            }
        }
        catch (e : Exception) {
            Log.d("compTest", " compPerformanceAdapter showDiffItems Exception  showDiffItems == ${e.message}")
        }
    }
}