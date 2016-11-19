package kzt.com.simplemeditationtimer.utils;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by k-seito on 2016/09/18.
 */
public class AnimationUtils {

    public static void startMeditation(final View start, final View stop, final View pause) {
        List<Animator> animatorList = new ArrayList<>();

        ObjectAnimator startFadeAnimator = ObjectAnimator.ofFloat(start, "alpha", 1f, 0f);
        startFadeAnimator.setDuration(100);
        animatorList.add(startFadeAnimator);

        ObjectAnimator moveRightAnimator = ObjectAnimator.ofFloat(stop, "translationX", 0f, stop.getWidth());
        moveRightAnimator.setDuration(100);
        animatorList.add(moveRightAnimator);

        ObjectAnimator stopFadeAnimator = ObjectAnimator.ofFloat(stop, "alpha", 0f, 1f);
        stopFadeAnimator.setDuration(100);
        animatorList.add(stopFadeAnimator);

        ObjectAnimator moveLeftAnimation = ObjectAnimator.ofFloat(pause, "translationX", 0, -pause.getWidth());
        moveLeftAnimation.setDuration(100);
        animatorList.add(moveLeftAnimation);

        ObjectAnimator pauseFadeAnimator = ObjectAnimator.ofFloat(pause, "alpha", 0f, 1f);
        pauseFadeAnimator.setDuration(100);
        animatorList.add(pauseFadeAnimator);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(animatorList);
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                stop.setVisibility(View.VISIBLE);
                pause.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                start.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animatorSet.start();
    }

    public static void stopMeditation(final View start, final View stop, final View pause) {
        List<Animator> animatorList = new ArrayList<>();

        ObjectAnimator startFadeAnimator = ObjectAnimator.ofFloat(start, "alpha", 0f, 1f);
        startFadeAnimator.setDuration(100);
        animatorList.add(startFadeAnimator);

        ObjectAnimator moveRightAnimator = ObjectAnimator.ofFloat(stop, "translationX", stop.getWidth(), 0);
        moveRightAnimator.setDuration(100);
        animatorList.add(moveRightAnimator);

        ObjectAnimator stopFadeAnimator = ObjectAnimator.ofFloat(stop, "alpha", 1f, 0f);
        stopFadeAnimator.setDuration(100);
        animatorList.add(stopFadeAnimator);

        ObjectAnimator moveLeftAnimation = ObjectAnimator.ofFloat(pause, "translationX", -pause.getWidth(), 0);
        moveLeftAnimation.setDuration(100);
        animatorList.add(moveLeftAnimation);

        ObjectAnimator pauseFadeAnimator = ObjectAnimator.ofFloat(pause, "alpha", 1f, 0f);
        pauseFadeAnimator.setDuration(100);
        animatorList.add(pauseFadeAnimator);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(animatorList);
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                start.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                stop.setVisibility(View.INVISIBLE);
                pause.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animatorSet.start();
    }

    public static AnimatorSet flashing(final View view) {
        List<Animator> animatorList = new ArrayList<>();

        ObjectAnimator hideView = ObjectAnimator.ofFloat(view, "alpha", 0f, 0f);
        hideView.setDuration(500);
        animatorList.add(hideView);

        final ObjectAnimator showView = ObjectAnimator.ofFloat(view, "alpha", 1f, 1f);
        showView.setDuration(500);
        animatorList.add(showView);

        final AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playSequentially(animatorList);
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                animatorSet.start();
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                showView.start();
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animatorSet.start();
        return animatorSet;
    }
}
