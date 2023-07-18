package com.copy.kiascreen.custom.layout

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.core.view.setPadding
import com.copy.kiascreen.R
import com.copy.kiascreen.databinding.LayoutCompDialogContentBinding
import com.copy.kiascreen.databinding.LayoutPurchaseChildBinding

class LayoutDialogContent @JvmOverloads constructor(context: Context, attr : AttributeSet? = null, def : Int = 0): LinearLayout(context, attr, def) {
    private val binding : LayoutCompDialogContentBinding by lazy {
        LayoutCompDialogContentBinding.bind(
            LayoutInflater.from(context).inflate(R.layout.layout_comp_dialog_content, this, false)
        )
    }

    init {
        addView(binding.root)
    }

    fun setLayoutText(content : Int) {
        binding.contentTv.setText(content)
    }


}