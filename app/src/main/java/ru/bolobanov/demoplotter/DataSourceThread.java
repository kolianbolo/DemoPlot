package ru.bolobanov.demoplotter;

import android.os.Handler;
import android.os.Message;

import java.util.HashMap;

import ru.bolobanov.demoplotter.number_sequence.SequenceOfNumbers;
import ru.bolobanov.demoplotter.number_sequence.exception.OutOfNumbersException;

/**
 * Created by Bolobanov Nikolay
 */
public class DataSourceThread extends Thread {

    boolean isDestroy = false;
    Handler mDataHandler;
    SequenceOfNumbers mSequenceOfNumber;

    public DataSourceThread(Handler dataHandler, SequenceOfNumbers pSequenceOfNumber) {
        this.mDataHandler = dataHandler;
        mSequenceOfNumber = pSequenceOfNumber;
    }

    public void run() {
        HashMap<String, Double> messageHash;
        try {
            while (true) {
                if (isDestroy) {
                    break;
                }
                Thread.sleep(100);
                messageHash = new HashMap<String, Double>();
                mSequenceOfNumber.next();
                messageHash.put("X", mSequenceOfNumber.getX());
                messageHash.put("Y", mSequenceOfNumber.getY());
                Message msg = new Message();
                msg.obj = messageHash;
                mDataHandler.sendMessage(msg);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (OutOfNumbersException e) {
            //ну и прекрасно - значит закончили, отправляем пустое сообщение
            Message msg = new Message();
            mDataHandler.sendMessage(msg);
        }
    }

    public void onDestroy() {
        isDestroy = true;
    }
}
