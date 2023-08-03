package com.copy.kiascreen.comparison.extension

import android.util.Log
import android.view.View
import android.widget.Spinner
import android.widget.Switch
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.SwitchCompat
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import androidx.viewpager2.widget.ViewPager2
import com.copy.kiascreen.R
import com.copy.kiascreen.comparison.adapter.ComparRecImgAdapter
import com.copy.kiascreen.comparison.adapter.ComparisonAdapter
import com.copy.kiascreen.comparison.adapter.compAdapter.CompEconAdapter
import com.copy.kiascreen.comparison.adapter.compAdapter.CompExtraPriceAdapter
import com.copy.kiascreen.comparison.adapter.compAdapter.CompPerformanceAdapter
import com.copy.kiascreen.comparison.adapter.compAdapter.CompSafetyAdapter
import com.copy.kiascreen.comparison.vo.Brand
import com.copy.kiascreen.roomVo.BrandItems
import com.copy.kiascreen.smoothSnapToPosition

// 아이템 제거시, 해당 목록 아이템리스트에서 삭제.
// holder Map에 이동할 위치에 있는 holder가 있을 경우 (포커스를 받지 못해서 view update가 불가능한 경우) 해당 holder에 접근해서 visibility 관리
fun ComparisonAdapter.onDelBtnClicked(position : Int, compRecycler : RecyclerView) {
    var mPosition = position

    this.apply {
        Log.d("adapterTest", "ondelbnclick!! adapter cnt = ${this.mitems.size} position = $position")
        this.notifyItemRemoved(position)
        this.notifyItemRangeRemoved(position, this.mitems.size - position)
    }

    // 제익 마지막 항목을 삭제 했을 경우에는, 이전 항목으로 scroll 시켜 줘야 함.
    if (position == this.mitems.size) {
        --mPosition
    }

    // 만약 해당 위치에 있는 홀더가 detached된게 아니라면, 추가 버튼을 보이게 하고 해당 위치로 스크롤 시켜준다. 주로 마지막 항목을 지웟을 경우임.
    compRecycler.findViewHolderForAdapterPosition(mPosition)?.let {
        Log.d("adapterTest", "findViewHolderPosition and count = ${this.mitems.size} mPosition = $mPosition")
        it.itemView.findViewById<ConstraintLayout>(R.id.add_wrapper_constraint).visibility = View.VISIBLE
        compRecycler.smoothSnapToPosition(mPosition)
    }

    // 아이템 추가 버튼을 보여준다. (viewHolder가 detached됬을 경우를 위해서, holderMap에서 꺼내 쓴다.) 해당 경우는 주로 3개의 항목 중 2번째 항목을 삭제 했을 경우임.
    this.holderMap[mPosition]?.let {
        Log.d("adapterTest", "holderMap not null")
        it.itemView.findViewById<ConstraintLayout>(R.id.add_wrapper_constraint).visibility = View.VISIBLE
    }
}

// 아이템 추가.
// 실제로는 retrofit2를 통해서 아이템을 가져와야 함으로, 매개변수로 해당 아이템 하나를 받아와야 한다.
fun ComparisonAdapter.addItem(compRecycler : RecyclerView) {
    this.addSpinnerItem(BrandItems.brandItems2)     // 실제로 데이터 받아올 때는 해당 데이터를 넘겨줘야 함
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

// compRv 의 이미지 pager 설정
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

// compRv 아이템 초기화, selected item list 초기화
fun ComparisonAdapter.reset(compRecycler: RecyclerView) {
    var size = this.itemCount

    this.notifyItemRangeRemoved(0, size)
    this.notifyItemRangeChanged(0, size)
    this.resetItem(listOf(Brand("기아", BrandItems.kiaCars)))
    this.resetSelectedItem()

    compRecycler.findViewHolderForAdapterPosition(0)?.let { holder ->
        Log.d("adapterTest", "findViewHolderPosition")
        holder.itemView.findViewById<ConstraintLayout>(R.id.add_wrapper_constraint).visibility = View.VISIBLE
    }

    this.holderMap[0]?.let { holder ->
        Log.d("adapterTest", "holderMap not null")
        holder.itemView.findViewById<ConstraintLayout>(R.id.add_wrapper_constraint).visibility = View.VISIBLE
    }
}

// vp2 title img ( close and open toggle 이미지 클릭 리스너)
fun AppCompatImageView.toggleVp(pager: ViewPager2) {
    this.setOnClickListener {
        if (pager.visibility != View.GONE) {
            pager.visibility = View.GONE
            this.setImageDrawable(resources.getDrawable(R.drawable.baseline_add_24))
        }
        else {
            pager.visibility = View.VISIBLE
            this.setImageDrawable(resources.getDrawable(R.drawable.straight_line))
        }
    }
}

// vp2 title img ( close and open toggle 이미지 클릭 리스너) , vp말고도 constraintWrapper 같이 붙어있는경우 (kncap 때문에 만듬)
fun AppCompatImageView.toggleVp(pager: ViewPager2, wrapper : ConstraintLayout) {
    this.setOnClickListener {
        if (pager.visibility != View.GONE) {
            wrapper.visibility = View.GONE
            pager.visibility = View.GONE
            this.setImageDrawable(resources.getDrawable(R.drawable.baseline_add_24))
        }
        else {
            wrapper.visibility = View.VISIBLE
            pager.visibility = View.VISIBLE
            this.setImageDrawable(resources.getDrawable(R.drawable.straight_line))
        }
    }
}

fun AppCompatImageView.toggleRv(pager: RecyclerView) {
    this.setOnClickListener {
        if (pager.visibility != View.GONE) {
            pager.visibility = View.GONE
            this.setImageDrawable(resources.getDrawable(R.drawable.baseline_add_24))
        }
        else {
            pager.visibility = View.VISIBLE
            this.setImageDrawable(resources.getDrawable(R.drawable.straight_line))
        }
    }
}

// 서로 다른 항목 switch toggle
fun SwitchCompat.toggleDiffFields(performanceAdapter: CompPerformanceAdapter, econAdapter: CompEconAdapter, safetyAdapter: CompSafetyAdapter) {
    this.setOnCheckedChangeListener { _, isChecked ->
        performanceAdapter.toggleDiffItems(isChecked)
        econAdapter.toggleDiffItems(isChecked)
        safetyAdapter.toggleDiffItems(isChecked)
    }
}

// vp vectorDrawable Animation
fun AppCompatImageView.toggleVpAvd(pager: ViewPager2) {
    this.setOnClickListener {
        var isOpen = if (pager.visibility != View.GONE) {
            pager.visibility = View.GONE
            true
        }
        else {
            pager.visibility = View.VISIBLE
            false
        }

        try {
            // 열린상태 -> 닫아줘야함
            var drawable = if (isOpen) {
                AnimatedVectorDrawableCompat.create(this.context, R.drawable.avd_vp_drawer_open)
            }
            // 닫힌상태 -> 열어줘야 함
            else {
                AnimatedVectorDrawableCompat.create(this.context, R.drawable.avd_vp_drawer_close)
            }

            this.setImageDrawable(drawable)
            drawable?.start()

        } catch (e : Exception) {
            if (pager.visibility != View.GONE) {
                this.setImageDrawable(resources.getDrawable(R.drawable.baseline_add_24))
            }
            else {
                this.setImageDrawable(resources.getDrawable(R.drawable.straight_line))
            }
        }
    }
}

// vp2 title img ( close and open toggle 이미지 클릭 리스너) , vp말고도 constraintWrapper 같이 붙어있는경우 (kncap 때문에 만듬)
fun AppCompatImageView.toggleVpAvd(pager: ViewPager2, wrapper : ConstraintLayout) {
    this.setOnClickListener {
        var isOpen = if (pager.visibility != View.GONE) {
            pager.visibility = View.GONE
            wrapper.visibility = View.GONE
            true
        }
        else {
            pager.visibility = View.VISIBLE
            wrapper.visibility = View.VISIBLE
            false
        }

        try {
            // 열린상태 -> 닫아줘야함
            var drawable = if (isOpen) {
                AnimatedVectorDrawableCompat.create(this.context, R.drawable.avd_vp_drawer_open)
            }
            // 닫힌상태 -> 열어줘야 함
            else {
                AnimatedVectorDrawableCompat.create(this.context, R.drawable.avd_vp_drawer_close)
            }

            this.setImageDrawable(drawable)
            drawable?.start()

        } catch (e : Exception) {
            if (pager.visibility != View.GONE) {
                this.setImageDrawable(resources.getDrawable(R.drawable.baseline_add_24))
            }
            else {
                this.setImageDrawable(resources.getDrawable(R.drawable.straight_line))
            }
        }
    }
}

