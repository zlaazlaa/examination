//package com.example.examination;
//
//
//import android.animation.AnimatorSet;
//import android.transition.Transition;
//import android.transition.TransitionValues;
//
//class TextviewTransition extends Transition {
//
//    private boolean mIsEnter = true;
//
//    public TextviewTransition(boolean isEnter) {
//        mIsEnter = isEnter;
//    }
//
//    @Override
//    public void captureStartValues(TransitionValues transitionValues) {
//    }
//
//    @Override
//    public void captureEndValues(TransitionValues transitionValues) {
//    }
//
//    @Override
//    public Animator createAnimator(ViewGroup sceneRoot, TransitionValues startValues, TransitionValues endValues) {
//        if (startValues == null || endValues == null) {
//            return null;
//        }
//        return createEnterAnimator(startValues, endValues);
//    }
//
//    private Animator createEnterAnimator(TransitionValues startValues, TransitionValues endValues) {
//        // 文字大小过度动画
//        ObjectAnimator textSizeAnimator = ObjectAnimator.ofFloat((TextView) startValues.view,
//                new Property<TextView, Float>(Float.class, "textSize") {
//                    @Override
//                    public void set(TextView object, Float value) {
//                        object.setTextSize(TypedValue.COMPLEX_UNIT_PX, value);
//                    }
//
//                    @Override
//                    public Float get(TextView object) {
//                        return object.getTextSize();
//                    }
//                },
//                mIsEnter ? getResources().getDimensionPixelOffset(R.dimen.text_size_start) :
//                        getResources().getDimensionPixelOffset(R.dimen.text_size_end),
//                mIsEnter ? getResources().getDimensionPixelOffset(R.dimen.text_size_end) :
//                        getResources().getDimensionPixelOffset(R.dimen.text_size_start)
//
//        );
//        // 文字颜色过度动画
//        ObjectAnimator textColorAnimator = ObjectAnimator.ofArgb((TextView) startValues.view,
//                new Property<TextView, Integer>(Integer.class, "textColor") {
//                    @Override
//                    public void set(TextView object, Integer value) {
//                        object.setTextColor(value);
//                    }
//
//                    @Override
//                    public Integer get(TextView object) {
//                        return object.getCurrentTextColor();
//                    }
//                },
//                mIsEnter ? getResources().getColor(R.color.start_color, null) :
//                        getResources().getColor(R.color.end_color, null),
//                mIsEnter ? getResources().getColor(R.color.end_color, null) :
//                        getResources().getColor(R.color.start_color, null)
//        );
//
//        AnimatorSet animatorSet = new AnimatorSet();
//        animatorSet.playTogether(textSizeAnimator, textColorAnimator);
//        return animatorSet;
//    }
//
//    @Override
//    public void captureStartValues(TransitionValues transitionValues) {
//
//    }
//}
