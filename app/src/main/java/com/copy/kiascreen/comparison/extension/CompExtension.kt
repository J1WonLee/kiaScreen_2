package com.copy.kiascreen.comparison.extension

import android.util.Log
import android.view.View
import android.widget.Spinner
import androidx.appcompat.widget.AppCompatButton
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.copy.kiascreen.R
import com.copy.kiascreen.comparison.adapter.ComparRecImgAdapter
import com.copy.kiascreen.comparison.adapter.ComparisonAdapter
import com.copy.kiascreen.comparison.vo.Brand
import com.copy.kiascreen.roomVo.BrandItems
import com.copy.kiascreen.smoothSnapToPosition

// 아이템 제거시, 해당 목록 아이템리스트에서 지우고, holder Map에 이동할 위치에 있는 holder가 있을 경우 (포커스를 받지 못해서 view update가 불가능한 경우) 해당 holder에 접근해서 visibility 관리
fun ComparisonAdapter.onDelBtnClicked(position : Int, compRecycler : RecyclerView) {
    var mPosition = position

    this.apply {
        Log.d("adapterTest", "ondelbnclick!! adapter cnt = ${this.mitems.size} position = $position")
        this.notifyItemRemoved(position)
        this.notifyItemRangeRemoved(position, this.mitems.size - position)
    }

    if (position == this.mitems.size) {
        --mPosition
    }

    compRecycler.findViewHolderForAdapterPosition(mPosition)?.let {
        Log.d("adapterTest", "findViewHolderPosition and count = ${this.mitems.size} mPosition = $mPosition")
        it.itemView.findViewById<ConstraintLayout>(R.id.add_wrapper_constraint).visibility = View.VISIBLE
        compRecycler.smoothSnapToPosition(mPosition)
    }

    this.holderMap[mPosition]?.let {
        Log.d("adapterTest", "holderMap not null")
        it.itemView.findViewById<ConstraintLayout>(R.id.add_wrapper_constraint).visibility = View.VISIBLE
    }
}

// 아이템 추가.
fun ComparisonAdapter.addItem(compRecycler : RecyclerView) {
    this.addSpinnerItem(BrandItems.brandItems2)

    this.notifyItemInserted(this.mitems.size - 1)

    compRecycler.smoothSnapToPosition(this.mitems.size - 1)

}

// 추천 이미지 vp로 설정
fun RecyclerView.setRecImg(compAdapter : ComparisonAdapter) {
    var cnt = compAdapter.mitems.size
    for (i in 1 until cnt) {
        // 첫번째 아이템은 추천 목록을 보여주지 않아야 한다.
        Log.d("adapterTest", "setRecCarImg, position = $i , adapter count = ${cnt}")

        this.findViewHolderForLayoutPosition(i)?.let {
            it.setCompPagerRecImg()
        }

        // 마지막 아이템에 추천항목 목록이 안그려질때 -> viewHolder가 포커스를 받지 못할때.
        if (i == 2 && this.findViewHolderForAdapterPosition(i) == null) {
            var holder = compAdapter.holderMap[2]

            holder?.let {
                Log.d("holderTest", "index == 2 and lastItem holder is not null")

                var spinner = it.itemView.findViewById<Spinner>(R.id.trim_spinner)
                if (spinner.selectedItemPosition == spinner.adapter.count) {
                    // 현재 트림까지 선택이 안된 상태
                    // 추천 비교 차량 보여준다 (구매 신청 버튼, 예상 비용 뷰를 가려준다)
                    it.itemView.findViewById<AppCompatButton>(R.id.build_btn).visibility = View.GONE
                    it.itemView.findViewById<AppCompatButton>(R.id.purchase_consult_btn).visibility = View.GONE
                    it.itemView.findViewById<ConstraintLayout>(R.id.price_wrapper).visibility = View.GONE

                    it.itemView.findViewById<ConstraintLayout>(R.id.rec_car_wrapper_constraint).visibility = View.VISIBLE
                    it.itemView.findViewById<ViewPager2>(R.id.rec_car_img_vp).adapter?.notifyDataSetChanged()
                }

                it.setCompPagerRecImg()
            }

        }
    }
}

fun RecyclerView.ViewHolder.setCompPagerRecImg() {
    var spinner = this.itemView.findViewById<Spinner>(R.id.trim_spinner)

    if (spinner.selectedItemPosition == spinner.adapter.count) {
        // 현재 트림까지 선택이 안된 상태
        //  비교 차량 보여준다 (구매 신청 버튼, 예상 비용 뷰를 가려준다)
        this.itemView.findViewById<AppCompatButton>(R.id.build_btn).visibility = View.GONE
        this.itemView.findViewById<AppCompatButton>(R.id.purchase_consult_btn).visibility = View.GONE
        this.itemView.findViewById<ConstraintLayout>(R.id.price_wrapper).visibility = View.GONE
        this.itemView.findViewById<ConstraintLayout>(R.id.rec_car_wrapper_constraint).visibility = View.VISIBLE

        this.itemView.findViewById<ViewPager2>(R.id.rec_car_img_vp).adapter?.let{
            (it as ComparRecImgAdapter).setLinks(BrandItems.baseImgLink)
            it.notifyDataSetChanged()
        }
    }
}

fun ComparisonAdapter.reset(compRecycler: RecyclerView) {
    var size = this.itemCount

    this.notifyItemRangeRemoved(0, size)
    this.notifyItemRangeChanged(0, size)

    this.resetItem(listOf(Brand("기아", BrandItems.kiaCars)))

    compRecycler.findViewHolderForAdapterPosition(0)?.let { holder ->
        Log.d("adapterTest", "findViewHolderPosition")
        holder.itemView.findViewById<ConstraintLayout>(R.id.add_wrapper_constraint).visibility = View.VISIBLE
    }

    this.holderMap[0]?.let { holder ->
        Log.d("adapterTest", "holderMap not null")
        holder.itemView.findViewById<ConstraintLayout>(R.id.add_wrapper_constraint).visibility = View.VISIBLE
    }


}

