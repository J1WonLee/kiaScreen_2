package com.copy.kiascreen.custom.layout

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.setPadding
import com.copy.kiascreen.R
import com.copy.kiascreen.databinding.LayoutPurchaseChildBinding

class LayoutMenuChild  @JvmOverloads constructor(context: Context, attr : AttributeSet? = null, def : Int = 0): LinearLayout (context, attr, def) {

    private val binding : LayoutPurchaseChildBinding by lazy {
        LayoutPurchaseChildBinding.bind(
            LayoutInflater.from(context).inflate(R.layout.layout_purchase_child, this, false)
        )
    }

    init {
        addView(binding.root)
        setBackgroundColor(resources.getColor(R.color.menu_wrapper))
        setPadding(resources.getDimension(R.dimen.menu_item_wrapper_padding).toInt())
    }

    fun setMenuItemViewText(array: Array<String>) {
        for (i in 0 until binding.childItemWrapper.childCount) {
            try {
                var item = binding.childItemWrapper.getChildAt(i) as TextView
                var mapper = array[i].split(",")
                item.text = mapper[0]
                setClickListener(item, mapper[1])
            }
            catch(e : Exception) {
                binding.childItemWrapper.getChildAt(i).visibility = View.GONE
                continue
            }
        }
    }

    private fun setClickListener(view : TextView, name : String) {
        view.setOnClickListener {
            Log.d("menuTest", "targetName = $name")
        }
    }


}