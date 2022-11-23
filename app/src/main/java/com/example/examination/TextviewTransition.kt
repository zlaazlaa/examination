//package com.example.examination
//
//import android.animation.Animator
//import android.animation.AnimatorSet
//import android.animation.ObjectAnimator
//import android.transition.Transition
//import android.transition.TransitionValues
//import android.util.Property
//import android.util.TypedValue
//import android.view.ViewGroup
//import android.widget.TextView
//
//
//internal class TextviewTransition(isEnter: Boolean) : Transition() {
//    private var mIsEnter = true
//
//    init {
//        mIsEnter = isEnter
//    }
//
//    override fun captureStartValues(transitionValues: TransitionValues?) {}
//    override fun captureEndValues(transitionValues: TransitionValues?) {}
//    override fun createAnimator(
//        sceneRoot: ViewGroup?,
//        startValues: TransitionValues?,
//        endValues: TransitionValues?
//    ): Animator? {
//        return if (startValues == null || endValues == null) {
//            null
//        } else createEnterAnimator(startValues, endValues)
//    }
//
//    private fun createEnterAnimator(
//        startValues: TransitionValues,
//        endValues: TransitionValues
//    ): Animator {
//        // 文字大小过度动画
//        val textSizeAnimator: ObjectAnimator = ObjectAnimator.ofFloat(
//            startValues.view as TextView,
//            object : Property<TextView?, Float?>(Float::class.java, "textSize") {
//                operator fun set(`object`: TextView, value: Float?) {
//                    `object`.setTextSize(TypedValue.COMPLEX_UNIT_PX, value!!)
//                }
//
//                operator fun get(`object`: TextView): Float {
//                    return `object`.textSize
//                }
//            },
//            if (mIsEnter) getResources().getDimensionPixelOffset(R.dimen.text_size_start) else getResources().getDimensionPixelOffset(
//                R.dimen.text_size_end
//            ),
//            if (mIsEnter) getResources().getDimensionPixelOffset(R.dimen.text_size_end) else getResources().getDimensionPixelOffset(
//                R.dimen.text_size_start
//            )
//        )
//
//        val animatorSet = AnimatorSet()
//        animatorSet.playTogether(textSizeAnimator, textColorAnimator)
//        return animatorSet
//    }
//}
