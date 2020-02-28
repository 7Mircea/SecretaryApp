package com.example.bazadedateexplo;

class BounceInterpolator implements android.view.animation.Interpolator {
    private double mAmplitudine ;
    private double mFrecventa ;

    BounceInterpolator() {
        mAmplitudine = 0.3;
        mFrecventa = 20;
    }

    BounceInterpolator(double amplitude, double frequency) {
        mAmplitudine = amplitude;
        mFrecventa = frequency;
    }

    public float getInterpolation(float time) {
        return (float) (-1 * Math.pow(Math.E, -time/ mAmplitudine) *
                Math.cos(mFrecventa * time) + 1);
    }
}