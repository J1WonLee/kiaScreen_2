package com.copy.kiascreen.comparison.adapter.compAdapter

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.copy.kiascreen.R
import com.copy.kiascreen.comparison.BuildCompActivity
import com.copy.kiascreen.comparison.dialogfragment.InsuranceFragment
import com.copy.kiascreen.comparison.dialogfragment.SafetyFragment
import com.copy.kiascreen.comparison.vo.CompItem
import com.copy.kiascreen.comparison.vo.CompListItems
import com.copy.kiascreen.comparison.vo.EconItem
import com.copy.kiascreen.comparison.vo.SafetyItem
import com.copy.kiascreen.databinding.ItemCompDetail2Binding
import com.copy.kiascreen.databinding.ItemCompDetail3Binding
import java.util.*
import kotlin.reflect.KProperty1
import kotlin.reflect.full.memberProperties

class CompSafetyAdapter (private val context : Context) : RecyclerView.Adapter<CompSafetyAdapter.SafetyHolder>(){
    private var data = CompListItems.compItemList.toMutableList()
    // 기준인 첫번째 아이템과 2~3 번째 아이템의 다른 필드들을 담는 List
    private var differentFields  : List<KProperty1<SafetyItem, *>>? = null
    // 첫번째 아이템과 다른 필드값을 가질 경우 differentField를 Map 형식으로 저장
    private var differentFieldsMap : kotlin.collections.HashMap< Int ,List<KProperty1<SafetyItem, *>>> = kotlin.collections.HashMap<Int, List<KProperty1<SafetyItem, *>>>()
    // 삭제, 초기화 후 기존의 값들이 존재하는 경우가 있어서 조절하기 위한 flag
    private var isUpdatedFlag = false
    // true -> 기본 값, 다른 필드들 강조, false -> 다른 필드 강조 x
    private var diffFieldsFlag = true

    // 첫번째 아이템과 나머지 아이템들의 필드값이 다른 경우, 해당 layout의 배경을 칠해준다
    private val diffAction = {
            fieldName : String, binding : ItemCompDetail3Binding ->
        val color = context.resources.getColor(R.color.menu_wrapper)
        when(fieldName) {
            "totalGrade" -> binding.itemCompTotalGradeConstraint.setBackgroundColor(color)
            "year"-> binding.itemCompYearLinear.setBackgroundColor(color)
            "score" -> binding.itemCompGradeLinear.setBackgroundColor(color)
            "crashSafety" -> binding.itemCompCrashSafetyLinear.setBackgroundColor(color)
            "pedSafety" -> binding.itemCompPedSafetyLinear.setBackgroundColor(color)
            "preventSafety" -> binding.itemCompPreventSafetyLinear.setBackgroundColor(color)
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

    // 아이템 삭제시, 삭제했다는 flag를 조정한다 (아이템 삭제 후 추가 시 및 초기화 선택 후 추가 시, 같은 필드여도 배경색이 남는 경우가 존재하는 경우가 존재했음)
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

    // activity에서 다른 항목 표기 switch의 value값을 가져와서, flag를 조정.
    fun toggleDiffItems(flag : Boolean) {
        this.diffFieldsFlag = flag
        this.notifyItemRangeChanged(0, 3)
    }

    inner class SafetyHolder(val binding:ItemCompDetail3Binding) : RecyclerView.ViewHolder(binding.root){
        fun bind(item : SafetyItem) {
            Log.d("compTest", "CompSafetyAdapter onBindViewHolder position = $position")
            initData(binding, item)
        }

        // 모든 항목의 배경색 초기화 (flag를 통해서 제어하고 있음)
        fun resetBgInWhite() {
            resetBgColor(binding)
        }

        // 2~3 번째 항목에 대한 다른 항목 배경색 처리
        fun setDiffItemBackground(position: Int) {
            Log.d("compTest", "=== CompSafetyAdapter setDiffItemBackground bind called ===")
            val diffItems = differentFieldsMap[position]

            diffItems?.forEach {
                diffAction(it.name, binding)
            }
        }

        // diffItems는 첫번째 항목과 나머지 항목을 비교하는 거라서, map에 0번 인덱스로 빼올 수 있는 데이터가 없음.
        // 첫번째 아이템일 경우 try catch문을 통해서 outofindex일 경우 색만 칠해주고 탈출함. -> for문으로 size 만큼 1번 인덱스부터 선회하는 식으로 refactor 해야 함.
        fun setFirstItemBackground() {
            Log.d("compTest", "=== CompSafetyAdapter setFirstItemBackground bind called ===")
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
                Log.d("compTest", "=== CompSafetyAdapter setFirstItemBackground exception error : ${e.message} ===")
            }
        }

        // 다이얼로그을 띄어준다
        fun setGuideClickListener() {
            binding.totalGradeImg.setOnClickListener {
                try {
                    val activity = context as BuildCompActivity
                    SafetyFragment().show(activity.supportFragmentManager, null)
                } catch (e : Exception) {
                    Log.d("dialogTest", "CompSafetyAdapter safety Exception = ${e.message}")
                }
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

    private fun initData(binding: ItemCompDetail3Binding, item : SafetyItem) {
        binding.totalGradeContentTv.text = item.totalGrade
        binding.testYearContentTv.text = item.year
        binding.gradeContentTv.text = item.score
        binding.crashSafetyContentTv.text = item.crashSafety
        binding.pedSafetyContentTv.text = item.pedSafety
        binding.preventSafetyContentTv.text = item.preventSafety
    }

    // 목록 삭제나, 다른 항목 보기 해제 시, 모든 아이템들의 배경색 초기화 시켜준다
    // 아이템 삭제 후 추가 시, 필드 값이 같아도 색이 칠해져 있는 경우가 발생해서 처리해 줌.
    private fun resetBgColor(binding: ItemCompDetail3Binding) {
        if (isUpdatedFlag || !diffFieldsFlag) {
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

    // item항목들 중, field값을 비교해서 다른 필드들을 (index , diffentFieldList)의 맵형태로 저장한다
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