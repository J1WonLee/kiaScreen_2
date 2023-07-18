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
import com.copy.kiascreen.comparison.dialogfragment.InsuranceFragment
import com.copy.kiascreen.comparison.vo.CompItem
import com.copy.kiascreen.comparison.vo.CompListItems
import com.copy.kiascreen.comparison.vo.EconItem
import com.copy.kiascreen.comparison.vo.PerformanceItem
import com.copy.kiascreen.databinding.ItemCompDetail1Binding
import com.copy.kiascreen.databinding.ItemCompDetail2Binding
import java.util.*
import kotlin.reflect.KProperty1
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.valueParameters

// 경제성 어뎁터
class CompEconAdapter(val context : Context) : RecyclerView.Adapter<CompEconAdapter.EconHolder>(){
    private var data = CompListItems.compItemList.toMutableList()
    private var differentFields  : List<KProperty1<EconItem, *>>? = null
    private var yearString = context.resources.getStringArray(R.array.driving_period_array)
    private var distanceString = context.resources.getStringArray(R.array.driving_distance_array)

    private var differentFieldsMap : kotlin.collections.HashMap< Int ,List<KProperty1<EconItem, *>>> = kotlin.collections.HashMap<Int, List<KProperty1<EconItem, *>>>()
    private var isUpdatedFlag = false

    private val diffAction = {
            fieldName : String, binding :  ItemCompDetail2Binding ->
        when(fieldName) {
            "cfe" -> binding.itemCompCfcLinear.setBackgroundColor(Color.GRAY)
            "cityFe"-> binding.itemCompCityFcLinear.setBackgroundColor(Color.GRAY)
            "hfe" -> binding.itemCompHfcLinear.setBackgroundColor(Color.GRAY)
            "feGrade" -> binding.itemCompFegLinear.setBackgroundColor(Color.GRAY)
            "insuranceGrade" -> binding.itemCompInsuranceLinear.setBackgroundColor(Color.GRAY)
            "co2" -> binding.itemCompC02Linear.setBackgroundColor(Color.GRAY)
            "maintance" -> binding.itemCompMaintacneLinear.setBackgroundColor(Color.GRAY)
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

    fun deletePerFormData(position: Int) {
        Log.d("compTest", "CompEconAdapter after deletePerFormData === data size == ${data.size} and position == ${position}")
//        this.notifyItemRangeChanged(position, 1)
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

    inner class EconHolder(val binding : ItemCompDetail2Binding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item : EconItem) {
            initData(binding, item)
            initSpinner(binding)
        }

        fun resetBgInWhite() {
            Log.d("compTest", "=== CompEconAdapter resetBgInWhite called position ")
            resetBgColor(binding)
        }

        fun setDiffItemBackground(position: Int) {
            Log.d("compTest", "=== CompEconAdapter setDiffItemBackground bind called position = $position===")
            val diffItems = differentFieldsMap[position]

            Log.d("compTest", "=== CompEconAdapter setDiffItemBackground diffItems.size = ${diffItems?.size}===")
            diffItems?.forEach {
                diffAction(it.name, binding)
            }

        }

        fun setFirstItemBackground() {
            Log.d("compTest", "=== CompEconAdapter setFirstItemBackground bind called ===")
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
                Log.d("compTest", "=== CompEconAdapter setFirstItemBackground exception error : ${e.message} ===")
            }
        }

        fun setInsuranceGuideClickListener() {
            binding.insuranceImg.setOnClickListener {
                val activity = context as BuildCompActivity
                InsuranceFragment().show(activity.supportFragmentManager, null)
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

        if (position == 0) {
            holder.setFirstItemBackground()
        }

        if (position > 0) {
            holder.setDiffItemBackground(position)
        }

        holder.setInsuranceGuideClickListener()
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

    private fun resetBgColor(binding: ItemCompDetail2Binding) {
        if (isUpdatedFlag) {
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

    private fun showDiffItems(item : EconItem) {
        Log.d("compTest", "CompEconAdapter == showDiffItems ==")
        try {
            for (index in 1 until data.size) {
                differentFields = EconItem::class.memberProperties.filter {
                    Log.d("compTest", "CompEconAdapter index = $index first item = ${item.insuranceGrade} second item ${data[index].econ.insuranceGrade}")
                    !Objects.equals(it.get(item), it.get(data[index].econ))
                }
//
//                differentFields!!.forEach {
//                   Log.d("compTest", "CompEconAdapter == index = $index comp test diffentFields == ${it.name} ")
//                }

                differentFields?.let { differentFieldsMap.put(index, it)  }
            }
        }
        catch (e : Exception) {
            Log.d("compTest", " CompEconAdapter showDiffItems Exception  showDiffItems == ${e.message}")
        }
    }

}