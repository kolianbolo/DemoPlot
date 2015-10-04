package ru.bolobanov.demoplotter.number_sequence;

import ru.bolobanov.demoplotter.number_sequence.exception.OutOfNumbersException;

/**
 * Created by Bolobanov Nikolay
 */

public interface SequenceOfNumbers {

    public void next() throws OutOfNumbersException;

    public double getX();

    public double getY();

}
