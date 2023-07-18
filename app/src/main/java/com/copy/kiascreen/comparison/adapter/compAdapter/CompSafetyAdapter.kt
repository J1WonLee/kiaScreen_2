package com.copy.kiascreen.comparison.adapter.compAdapter

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.copy.kiascreen.comparison.vo.CompItem
import com.copy.kiascreen.comparison.vo.CompListItems
import com.copy.kiascreen.comparison.vo.EconItem
import com.copy.kiascreen.comparison.vo.SafetyItem
import com.copy.kiascreen.databinding.ItemCompDetail2Binding
import com.copy.kiascreen.databinding.ItemCompDetail3Binding
import java.util.*
import kotlin.reflect.KProperty1
import kotlin.reflect.full.memberProperties

class CompSafetyAdapter : RecyclerView.Adapter<CompSafetyAdapter.SafetyHolder>(){
    private var data = CompListItems.compItemList.toMutableList()
    private var differentFields  : List<KProperty1<SafetyItem, *>>? = null
    private var differentFieldsMap : kotlin.collections.HashMap< Int ,List<KProperty1<SafetyItem, *>>> = kotlin.collections.HashMap<Int, List<KProperty1<SafetyItem, *>>>()
    private var isUpdatedFlag = false

    private val diffAction = {
            fieldName : String, binding : ItemCompDetail3Binding ->
        when(fieldName) {
            "totalGrade" -> binding.itemCompTotalGradeConstraint.setBackgroundColor(Color.GRAY)
            "year"-> binding.itemCompYearLinear.setBackgroundColor(Color.GRAY)
            "score" -> binding.itemCompGradeLinear.setBackgroundColor(Color.GRAY)
            "crashSafety" -> binding.itemCompCrashSafetyLinear.setBackgroundColor(Color.GRAY)
            "pedSafety" -> binding.itemCompPedSafetyLinear.setBackgroundColor(Color.GRAY)
            "preventSafety" -> binding.itemCompPreventSafetyLinear.setBackgroundColor(Color.GRAY)
            else -> {}
        }
    }

    init {
        showDiffItems(data[0].safetyItem)
    }

    fun addSafetyData(newData : CompItem) {
        Log.d("compTest", "CompSafetyAdapter addData called")
        this.data.add(newData)
        showDiffItems(data[0].safetyItem)
        Log.d("compTest", "CompSafetyAdapter after add data size == ${data.size}")
        this.notifyItemInserted(data.size - 1)
    }

    fun updatedSafetyData(updatedData : CompItem, position: Int) {
        Log.d("compTest", "CompSafetyAdapter updatedPerFormData called")
        this.data[position] = updatedData
        showDiffItems(data[0].safetyItem)
        Log.d("compTest", "CompSafetyAdapter after update data size == ${data.size} and position == ${position}")
        this.notifyItemChanged(position)
    }

    fun deleteSafetyData(position: Int) {
        Log.d("compTest", "CompSafetyAdapter after deleteSafetyData === data size == ${data.size} and position == ${position}")
        this.data.removeAt(position)
        isUpdatedFlag = true
        this.notifyDataSetChanged()
    }

    fun setSafetyDataAfterReset() {
        Log.d("compTest", "CompSafetyAdapter setSafetyDataAfterReset called")
        data = CompListItems.compItemList.toMutableList()
        Log.d("compTest", "CompSafetyAdapter setSafetyDataAfterReset data size = ${data.size}")
        showDiffItems(data[0].safetyItem)
        notifyDataSetChanged()
    }

    inner class SafetyHolder(val binding:ItemCompDetail3Binding) : RecyclerView.ViewHolder(binding.root){
        fun bind(item : SafetyItem) {
            Log.d("compTest", "CompSafetyAdapter onBindViewHolder position = $position")
            initData(binding, item)
        }

        fun resetBgInWhite() {
            resetBgColor(binding)
        }

        fun setDiffItemBackground(position: Int) {
            Log.d("compTest", "=== CompSafetyAdapter setDiffItemBackground bind called ===")
            val diffItems = differentFieldsMap[position]

            diffItems?.forEach {
                diffAction(it.name, binding)
            }
        }

        fun setFirstItemBackground() {
            Log.d("compTest", "=== CompSafetyAdapter setFirstItemBackground bind called ===")
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
                Log.d("compTest", "=== CompSafetyAdapter setFirstItemBackground exception error : ${e.message} ===")
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SafetyHolder {
        val binding = ItemCompDetail3Binding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SafetyHolder(binding)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: SafetyHolder, position: Int) {
        Log.d("compTest", "CompSafetyAdapter onBindViewHolder position = $position, data size = ${data.size}")
        holder.bind(data[position].safetyItem)

        holder.resetBgInWhite()

        if (position == 0) {
            holder.setFirstItemBackground()
        }

        if (position > 0) {
            holder.setDiffItemBackground(position)
        }
    }

    private fun initData(binding: ItemCompDetail3Binding, item : SafetyItem) {
        binding.totalGradeContentTv.text = item.totalGrade
        binding.testYearContentTv.text = item.year
        binding.gradeContentTv.text = item.score
        binding.crashSafetyContentTv.text = item.crashSafety
        binding.pedSafetyContentTv.text = item.pedSafety
        binding.preventSafetyContentTv.text = item.preventSafety
    }

    private fun resetBgColor(binding: ItemCompDetail3Binding) {
        if (isUpdatedFlag) {
            binding.apply {
                itemCompTotalGradeConstraint.setBackgroundColor(Color.WHITE)
                itemCompYearLinear.setBackgroundColor(Color.WHITE)
                itemCompGradeLinear.setBackgroundColor(Color.WHITE)
                itemCompCrashSafetyLinear.setBackgroundColor(Color.WHITE)
                itemCompPedSafetyLinear.setBackgroundColor(Color.WHITE)
                itemCompPreventSafetyLinear.setBackgroundColor(Color.WHITE)
            }
        }
    }

    private fun showDiffItems(item : SafetyItem) {
        Log.d("compTest", "CompSafetyAdapter == showDiffItems ==")
        try {
            for (index in 1 until data.size) {
                differentFields = SafetyItem::class.memberProperties.filter {

                    !Objects.equals(it.get(item), it.get(data[index].safetyItem))
                }

                differentFields!!.forEach {
                    Log.d("compTest", "CompSafetyAdapter == index = $index comp test diffentFields == ${it.name} ")
                }

                differentFields?.let { differentFieldsMap.put(index, it)  }
            }
        }
        catch (e : Exception) {
            Log.d("compTest", " CompSafetyAdapter showDiffItems Exception  showDiffItems == ${e.message}")
        }
    }
}