package ru.bolobanov.demoplotter.number_sequence;

/**
 * Created by Bolobanov Nikolay
 */
public class LinearSequence extends AbstractSequenceOfNumbers {

    private double mA = 0.3;
    private double mB = 2;

    protected LinearSequence(Builder builder) {
        super(builder);
    }

    @Override
    protected double function(double pX) {
        return mA * pX + mB;
    }
}
