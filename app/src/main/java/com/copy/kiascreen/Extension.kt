package com.copy.kiascreen

import android.app.Activity
import android.content.Context
import android.content.res.Resources.getSystem
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import com.copy.kiascreen.custom.MenuToggleImageView
import com.copy.kiascreen.custom.MenuWrapperLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

fun MenuToggleImageView.toggleMenu(child : MenuWrapperLayout) : Boolean {
    return this.toggleWrapper(child)
}

fun MenuToggleImageView.closeMenu(child : MenuWrapperLayout) {
    this.hideMenuWrapper(child)
}

fun ConstraintLayout.toggleVisibility() {
    if (this.visibility == View.VISIBLE) {
        this.visibility = View.GONE
    } else {
        this.visibility = View.VISIBLE
    }
}

fun LinearLayout.toggleVisibility() {
    if (this.visibility == View.VISIBLE) {
        this.visibility = View.GONE
    }
    else {
        this.visibility = View.VISIBLE
    }
}


fun RecyclerView.smoothSnapToPosition(position : Int, snapMode : Int = LinearSmoothScroller.SNAP_TO_START) {
    val scrollDuration = 800f
    val smoothScroller = object : LinearSmoothScroller(this.context) {

        override fun getVerticalSnapPreference(): Int = snapMode
        override fun getHorizontalSnapPreference(): Int = snapMode
        override fun calculateSpeedPerPixel(displayMetrics: DisplayMetrics?): Float {
            return scrollDuration / computeHorizontalScrollRange()
        }
    }

    smoothScroller.targetPosition = position
    layoutManager?.startSmoothScroll(smoothScroller)
}

fun Spinner.setSpinnerAdapter(spinnerAdapter : ArrayAdapter<String>) {
    with(this) {
        this.adapter = spinnerAdapter
        this.setSelection(spinnerAdapter.count)
        this.dropDownVerticalOffset = 45f.dpToPix()
    }
}

fun Array<String>.setStringArray(context: Context ) : ArrayAdapter<String> {
    val spinnerAdapter = object : ArrayAdapter<String>(context, R.layout.item_sns_spinner) {
        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

            val view =  super.getView(position, convertView, parent)

            if (position == count) {
                (view.findViewById<View>(R.id.tvItemSpinner) as TextView).text = ""
                (view.findViewById<View>(R.id.tvItemSpinner) as TextView).hint = getItem(count)
            }

            return view
        }

        override fun getCount(): Int {
            return this@setStringArray.size - 1
        }
    }

    spinnerAdapter.addAll(this.toMutableList())
    spinnerAdapter.add(" ")
    return spinnerAdapter
}


fun Float.dpToPix() : Int = (this * getSystem().displayMetrics.density).toInt()

fun View.showAnim(context : Context) {
    Log.d("animTest", "showANim")
    var animaton = AnimationUtils.loadAnimation(context, R.anim.anim_stick_header_show)
    this.startAnimation(animaton)
}

fun View.hideAnim(context : Context) {
    this.startAnimation(AnimationUtils.loadAnimation(context,  R.anim.anim_stick_header_hide))
}


fun LifecycleOwner.repeatOnStarted (func : suspend CoroutineScope.() -> Unit) {
    lifecycleScope.launch {
        lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED, func)
    }
}

fun Activity.getScreenHeight() : Int {
    var displayMetrics = DisplayMetrics()
    this.windowManager.defaultDisplay.getMetrics(displayMetrics)
    var height = displayMetrics.heightPixels
    Log.i("ScreenSizeTest", "metrics height = ${height}")
    return height
}

fun View.reSizeHeight(h : Int) {
    val lp = layoutParams
    lp?.let {
        it.height = h
        layoutParams = it
    }
}


