package com.copy.kiascreen.comparison.snapplistener

import android.graphics.Canvas
import android.util.Log
import android.widget.EdgeEffect
import androidx.dynamicanimation.animation.SpringAnimation
import androidx.dynamicanimation.animation.SpringForce
import androidx.recyclerview.widget.RecyclerView


private const val OVERSCROLL_TRANSLATION_MAGNITUDE = 0.5f
private const val FLING_TRANSLATION_MAGNITUDE = 0.5f


class BounceEdgeEffectFactory : RecyclerView.EdgeEffectFactory() {

    override fun createEdgeEffect(view: RecyclerView, direction: Int): EdgeEffect {

        return object : EdgeEffect(view.context) {
            var anim : SpringAnimation? = null

            override fun onPull(deltaDistance: Float) {
                super.onPull(deltaDistance)
                Log.d("pullTest", "view transX = ${view.translationX}")
                handlePull(deltaDistance)
            }

            override fun onPull(deltaDistance: Float, displacement: Float) {
                super.onPull(deltaDistance, displacement)
                Log.d("pullTest", "view transX = ${view.translationX}")
                handlePull(deltaDistance)
            }

            override fun onAbsorb(velocity: Int) {
                super.onAbsorb(velocity)

                val sign = if (direction == DIRECTION_RIGHT) -1 else 1
                val translationVelocity = sign * velocity * FLING_TRANSLATION_MAGNITUDE
                anim?.cancel()
                anim = createAnim().setStartVelocity(translationVelocity)?.also { it.start() }
            }

            override fun onRelease() {
                super.onRelease()

                if (view.translationX != 0f) {
                    anim = createAnim()?.also { it.start() }
                }
            }

            private fun handlePull(deltaDistance : Float) {
                val sign = if (direction == DIRECTION_RIGHT) -1 else 1

                val translationXDelta = sign * view.height * deltaDistance * OVERSCROLL_TRANSLATION_MAGNITUDE

                view.translationX += translationXDelta
                anim?.cancel()
            }

            override fun draw(canvas: Canvas?): Boolean {
                // 기존의 오버스크롤 이펙트를 없앤다.
                return false
            }

            override fun isFinished(): Boolean {
                return anim?.isRunning?.not() ?: true
            }

            private fun createAnim() = SpringAnimation(view, SpringAnimation.TRANSLATION_X)
                .setSpring(
                    SpringForce()
                    .setFinalPosition(0f)
                    .setDampingRatio(SpringForce.DAMPING_RATIO_MEDIUM_BOUNCY)
                    .setStiffness(SpringForce.STIFFNESS_LOW)
                )
        }
    }
}