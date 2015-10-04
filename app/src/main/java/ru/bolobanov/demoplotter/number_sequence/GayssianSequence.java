package ru.bolobanov.demoplotter.number_sequence;

/**
 * Created by Bolobanov Nikolay on 24.09.15.
 */
public class GayssianSequence extends AbstractSequenceOfNumbers {

    private double mAmplitude = 9;
    private double mSigma = 0.07;

    protected GayssianSequence(Builder builder) {
        super(builder);
    }

    @Override
    protected double function(double pX) {
        return mAmplitude * Math.pow(Math.E, -1 * mSigma * ((pX - mShiftX) * (pX - mShiftX)));
    }
}
