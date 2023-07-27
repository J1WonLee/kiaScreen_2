package com.copy.kiascreen.comparison.adapter.compAdapter

import android.content.Context
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.RecyclerView
import com.copy.kiascreen.R
import com.copy.kiascreen.comparison.BuildCompActivity
import com.copy.kiascreen.comparison.dialogfragment.AcquistionFragment
import com.copy.kiascreen.comparison.dialogfragment.FundFragment
import com.copy.kiascreen.comparison.vo.CompItem
import com.copy.kiascreen.comparison.vo.CompListItems
import com.copy.kiascreen.databinding.ItemCompDetail4Binding
import com.copy.kiascreen.util.StringTypeUtil
import okhttp3.internal.notify

class CompExtraPriceAdapter(val context : Context) : RecyclerView.Adapter<CompExtraPriceAdapter.ExtraPriceHolder>(){
    private var data = CompListItems.compItemList.toMutableList()
    private var extraString = context.resources.getStringArray(R.array.tax_register_cond_array)
    private var localString = context.resources.getStringArray(R.array.fond_register_cond_array)
    private var gangwonString = context.resources.getStringArray(R.array.gang_won_array)
    private var kyonggiString = context.resources.getStringArray(R.array.kyonggi_array)
    private val stringType = StringTypeUtil()

    private var clickCount = 0

    fun addExtraPriceData(newData : CompItem) {
        Log.d("compTest", "CompExtraPriceAdapter addData called")
        this.data.add(newData)
        Log.d("compTest", "CompExtraPriceAdapter after add data size == ${data.size}")
        this.notifyItemInserted(data.size - 1)
    }

    fun updatedExtraPriceData(updatedData : CompItem, position: Int) {
        Log.d("compTest", "CompExtraPriceAdapter updatedPerFormData called")
        this.data[position] = updatedData
        Log.d("compTest", "CompExtraPriceAdapter after update data size == ${data.size} and position == ${position}")
        this.notifyItemChanged(position)
    }

    fun deleteExtraPriceData(position: Int) {
        Log.d("compTest", "CompExtraPriceAdapter after deleteExtraPriceData === data size == ${data.size} and position == ${position}")

        this.data.removeAt(position)
        this.notifyDataSetChanged()
    }

    fun setExtraPriceDataAfterReset() {
        Log.d("compTest", "CompExtraPriceAdapter setExtraPriceDataAfterReset called")
        data = CompListItems.compItemList.toMutableList()
        Log.d("compTest", "CompExtraPriceAdapter setExtraPriceDataAfterReset data size = ${data.size}")
        notifyDataSetChanged()
    }

    inner class ExtraPriceHolder(val binding : ItemCompDetail4Binding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(){
            initData(binding)
        }

        fun setClickListener() {
            binding.acquisitionTaxImg.setOnClickListener {
                try {
                    val activity = context as BuildCompActivity
                    activity?.let {
                        AcquistionFragment().show(it.supportFragmentManager, null)
                    }
                } catch (e : Exception) {
                    Log.d("DialogTest", "ExtraPriceAdapter acquisitionDialog Exception = ${e.message}")
                }

            }

            binding.fondImg.setOnClickListener {
                try {
                    val activity = context as BuildCompActivity
                    activity?.let {
                        FundFragment().show(it.supportFragmentManager, null)
                    }
                } catch (e : Exception) {
                    Log.d("DialogTest", "ExtraPriceAdapter fondDialog Exception = ${e.message}")
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExtraPriceHolder {
        val binding = ItemCompDetail4Binding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ExtraPriceHolder(binding)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: ExtraPriceHolder, position: Int) {
        holder.bind()
        holder.setClickListener()
    }

    private fun initData(binding: ItemCompDetail4Binding) {
        binding.compTotalContentTv.text = stringType.getMoneyFormat("1600000") +" 원"
        binding.acquisitionTaxContentTv.text = stringType.getMoneyFormat("223500") +" 원"
        binding.fondContentTv.text = stringType.getMoneyFormat("0") +" 원"

        initSpinner(binding)
    }

    private fun initSpinner(binding : ItemCompDetail4Binding) {
        /*
        val extraStringArray = extraString.toMutableList().setStringArray(context)
        val localStringArray = localString.toMutableList().setStringArray(context)

        binding.registerCondSpinner.setSpinnerAdapter(extraStringArray)
        binding.registerPlaceSpinner.setSpinnerAdapter(localStringArray)

         */

        binding.registerCondSpinner.adapter = ArrayAdapter(context, R.layout.item_spinner, extraString)
        binding.registerPlaceSpinner.adapter = ArrayAdapter(context, R.layout.item_spinner, localString)

        setAcquSpinnerListener(binding)
        setCondSpinnerListener(binding)

    }

    private fun setCondSpinnerListener(binding : ItemCompDetail4Binding) {
        binding.registerCondSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (position > 0) {
                    binding.compTotalContentTv.text = stringType.getMoneyFormat("0") +" 원"
                    binding.acquisitionTaxContentTv.text = stringType.getMoneyFormat("0") +" 원"
                }
                else {
                    binding.compTotalContentTv.text = stringType.getMoneyFormat("1600000") +" 원"
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun setAcquSpinnerListener(binding : ItemCompDetail4Binding) {
        binding.registerPlaceSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                Log.d("extraPriceSPinnerTest", "onItemSelected position = $position clickCount = $clickCount")

                setChildSpinner(binding, position)

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                Log.d("extraPriceSPinnerTest", "onNothingSelected  clickCount = $clickCount")
            }

        }
    }

    private fun setChildSpinner(binding: ItemCompDetail4Binding, position : Int) {
        if (position == 0) {
            binding.registerPlaceChildSpinner.adapter = ArrayAdapter(context, R.layout.item_spinner, gangwonString)
        }
        else {
            binding.registerPlaceChildSpinner.adapter = ArrayAdapter(context, R.layout.item_spinner, kyonggiString)
        }
        binding.registerPlaceChildSpinner.visibility = View.VISIBLE

        setChildSpinnerListener(binding)
    }

    private fun setChildSpinnerListener(binding : ItemCompDetail4Binding) {
        binding.registerPlaceChildSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

        }
    }
}