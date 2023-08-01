package com.copy.kiascreen.mypage.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import com.bumptech.glide.Glide
import com.copy.kiascreen.databinding.ItemSnapPagerBinding
import com.copy.kiascreen.mypage.samplevo.SnapItem
import com.copy.kiascreen.roomVo.User
import com.copy.kiascreen.util.fragment.BaseFragment

class SnapPagerFragment : BaseFragment<ItemSnapPagerBinding>() {
    private var menuItem : SnapItem? = null
    private lateinit var imgItem : AppCompatImageView
    private lateinit var title : TextView
    private lateinit var content : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        menuItem = arguments?.getParcelable<SnapItem?>("menuItem")
    }

    override fun inflateViewBinding(inflater: LayoutInflater, container: ViewGroup?) = ItemSnapPagerBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // img glide &&
        initView()
        menuItem?.let {
            setContents(it)
        }
    }

    private fun initView() {
        imgItem = binding.snapImg
        title = binding.snapContnetTv1
        content = binding.snapContentTv2
    }

    private fun setContents(item : SnapItem) {
        Glide.with(this).load(item.imgDrawable).into(imgItem)
        title.text = item.title
        content.text = item.content
    }


    companion object {
        fun newInstance(menuIem : SnapItem) : SnapPagerFragment {
            val args = Bundle().apply {
                this.putParcelable("menuItem", menuIem)
            }

            return SnapPagerFragment().apply {
                arguments = args
            }
        }
    }
}