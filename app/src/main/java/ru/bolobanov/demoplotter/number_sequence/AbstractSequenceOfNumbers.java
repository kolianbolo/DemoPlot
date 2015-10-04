package ru.bolobanov.demoplotter.number_sequence;

import ru.bolobanov.demoplotter.number_sequence.exception.OutOfNumbersException;

/**
 * Created by Bolobanov Nikolay
 */
public abstract class AbstractSequenceOfNumbers implements SequenceOfNumbers {

    boolean isFirst = true;
    protected double mStartNumber;
    protected double mStopNumber;
    protected double mStep;
    protected double mShiftX;
    protected double mX;
    protected double mY;

    public static class Builder {
        protected double mStartNumber = 0;
        protected double mStopNumber = 20;
        protected double mStep = 0.1;
        protected double mShiftX = 10;

        public Builder setStart(double pStartNumber) {
            mStartNumber = pStartNumber;
            return this;
        }

        public Builder setStop(double pStopNumber) {
            mStopNumber = pStopNumber;
            return this;
        }

        public Builder setStep(double pStep) {
            mStep = pStep;
            return this;
        }


        public Builder setShiftX(double pShiftX) {
            mShiftX = pShiftX;
            return this;
        }


        public AbstractSequenceOfNumbers build(Class pClass) {
            if (pClass.equals(LinearSequence.class)) {
                return new LinearSequence(this);
            } else if (pClass.equals(SquareSequence.class)) {
                return new SquareSequence(this);
            } else if (pClass.equals(GayssianSequence.class)) {
                return new GayssianSequence(this);
            }
            return null;
        }
    }

    protected AbstractSequenceOfNumbers(Builder builder) {
        mStartNumber = builder.mStartNumber;
        mStopNumber = builder.mStopNumber;
        mStep = builder.mStep;
        mShiftX = builder.mShiftX;

    }

    public void next() throws OutOfNumbersException {
        if (isFirst) {
            mX = mStartNumber;
            mY = function(mStartNumber);
            isFirst = false;
        } else {
            if (isOverflow()) {
                throw new OutOfNumbersException();
            } else {
                mX = mX + mStep;
                mY = function(mX);
            }
        }
    }

    abstract protected double function(double pX);

    protected boolean isOverflow() {
        if ((mX + mStep) > mStopNumber) {
            return true;
        }
        return false;
    }

    public double getX() {
        return mX;
    }

    public double getY() {
        return mY;
    }
}
