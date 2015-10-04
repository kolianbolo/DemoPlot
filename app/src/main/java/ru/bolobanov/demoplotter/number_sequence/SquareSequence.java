package ru.bolobanov.demoplotter.number_sequence;

/**
 * Created by Bolobanov Nikolay on 17.09.15.
 */
public class SquareSequence extends AbstractSequenceOfNumbers {

    private double mA = 0.1;
    private double mB = 0;
    private double mC = 0;

    protected SquareSequence(Builder builder) {
        super(builder);
    }

    @Override
    protected double function(double pX) {
        return mA * (pX - mShiftX) * (pX - mShiftX) + mB * (pX - mShiftX) + mC;
    }
}
