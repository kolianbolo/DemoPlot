package ru.bolobanov.demoplotter;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.BasicStroke;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.sharedpreferences.Pref;

import java.util.HashMap;
import java.util.Random;

import ru.bolobanov.demoplotter.number_sequence.AbstractSequenceOfNumbers;
import ru.bolobanov.demoplotter.number_sequence.GayssianSequence;
import ru.bolobanov.demoplotter.number_sequence.LinearSequence;
import ru.bolobanov.demoplotter.number_sequence.SequenceOfNumbers;
import ru.bolobanov.demoplotter.number_sequence.SquareSequence;

/**
 * Created by Bolobanov Nikolay
 */

@EActivity(R.layout.a_main)
public class MainActivity extends ActionBarActivity {
    @ViewById
    public Button startButton;
    @ViewById
    public Button stopButton;
    @ViewById
    public Button nonameButton;
    @ViewById
    public Button choiceButton;
    @ViewById
    public LinearLayout chartLinear;

    @Pref
    PreferencesService_ aPreferences;

    Random rndGenerator = new Random();

    private XYMultipleSeriesDataset mDataset = new XYMultipleSeriesDataset();
    private XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();
    private GraphicalView mChartView;
    private XYSeries mCurrentSeries;
    private XYSeriesRenderer mCurrentRenderer;
    private XYSeries topEnvelopSeries;
    private XYSeriesRenderer topEnvelopRenderer;
    private XYSeries bottomEnvelopSeries;
    private XYSeriesRenderer bottomEnvelopRenderer;
    Thread pointThread;


    Handler sourceHandler;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mChartView == null) {
            chartInit();
        }
        applyPreference();
    }

    @AfterViews
    void initialiseActionBar() {
        getSupportActionBar().setDisplayShowHomeEnabled(false);
    }

    private void init() {
        sourceHandler = new Handler() {
            public void handleMessage(android.os.Message message) {
                if (message.obj != null) {
                    HashMap<String, Double> hashPoint = (HashMap<String, Double>) message.obj;
                    if (hashPoint.containsKey("X")) {
                        addPoint(hashPoint.get("X"), hashPoint.get("Y"));
                    }
                } else {
                    stop();
                }
            }
        };
    }

    private void applyPreference() {
        boolean freeMove = aPreferences.free_move().get();
        mRenderer.setZoomEnabled(freeMove, freeMove);
        mRenderer.setPanEnabled(freeMove, freeMove);
    }

    public void chartInit() {
        mChartView = ChartFactory.getLineChartView(this, mDataset, mRenderer);
        mRenderer.setShowLegend(false);
        mRenderer.setPointSize(Constants.POINT_SIZE);
        mRenderer.setZoomRate(Constants.DEFAULT_ZOOM_RATE);
        mRenderer.setXAxisMax(Constants.DEFAULT_X_MAX);
        mRenderer.setXAxisMin(Constants.DEFAULT_X_MIN);
        mRenderer.setYAxisMin(Constants.DEFAULT_Y_MIN);
        mRenderer.setYAxisMax(Constants.DEFAULT_Y_MAX);
        mRenderer.setYLabelsPadding(12);
        mRenderer.setYLabelsVerticalPadding(-4);
        mRenderer.setShowGridX(true);
        mRenderer.setYLabels(14);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        Log.d("", "chartLinear = " + chartLinear + "    startButton=" + startButton);
        if (chartLinear != null) {
            chartLinear.addView(mChartView, params);
        }
        if (mCurrentSeries == null) {
            mCurrentSeries = new XYSeries("mainSeries");
        }
        mDataset.addSeries(mCurrentSeries);

        if (topEnvelopSeries == null) {
            topEnvelopSeries = new XYSeries("topEnvelop");
            mDataset.addSeries(topEnvelopSeries);
        }

        if (bottomEnvelopSeries == null) {
            bottomEnvelopSeries = new XYSeries("bottomEnvelop");
            mDataset.addSeries(bottomEnvelopSeries);
        }

        // create a new renderer for the new series
        mRenderer.setXTitle("секунды");
        mRenderer.setYTitle("км/ч");
        if (mCurrentRenderer == null) {
            mCurrentRenderer = new XYSeriesRenderer();
            mRenderer.addSeriesRenderer(mCurrentRenderer);
            // set some renderer properties
            mCurrentRenderer.setPointStyle(PointStyle.CIRCLE);
            mCurrentRenderer.setFillPoints(true);
            mCurrentRenderer.setColor(Color.GREEN);
            mCurrentRenderer.setLineWidth(2);
        }

        if (topEnvelopRenderer == null) {
            topEnvelopRenderer = new XYSeriesRenderer();
            mRenderer.addSeriesRenderer(topEnvelopRenderer);
            // set some renderer properties
            topEnvelopRenderer.setColor(Color.GREEN);
            topEnvelopRenderer.setStroke(BasicStroke.DASHED);
            topEnvelopRenderer.setLineWidth(2);
        }

        if (bottomEnvelopRenderer == null) {
            bottomEnvelopRenderer = new XYSeriesRenderer();
            mRenderer.addSeriesRenderer(bottomEnvelopRenderer);
            // set some renderer properties
            bottomEnvelopRenderer.setColor(Color.GREEN);
            bottomEnvelopRenderer.setStroke(BasicStroke.DASHED);
            bottomEnvelopRenderer.setLineWidth(2);
        }

        mChartView.repaint();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add("настройки");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d("MENU", "Cliced MenuItem is " + item.getTitle());
        if ("настройки".equals(item.getTitle())) {
            startActivity(new Intent(this, PreferencesActivity_.class));
        }
        return super.onOptionsItemSelected(item);
    }


    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.startButton:
                start();
                startButton.setEnabled(false);
                stopButton.setEnabled(true);
                break;
            case R.id.stopButton:
                if (pointThread != null) {
                    pointThread.interrupt();
                }
                stop();
                break;
        }
    }

    public void stop() {
        startButton.setEnabled(true);
        stopButton.setEnabled(false);
    }

    public void start() {
        mCurrentSeries.clear();
        final String aSource = aPreferences.listOfSources().get();
        SequenceOfNumbers aSequence;
        if ("1".equals(aSource)) {
            aSequence = (new AbstractSequenceOfNumbers.Builder()).setStep(0.1).build(LinearSequence.class);
        } else if ("2".equals(aSource)) {
            aSequence = (new AbstractSequenceOfNumbers.Builder()).setStep(0.1).build(SquareSequence.class);
        } else {
            aSequence = (new AbstractSequenceOfNumbers.Builder()).setStep(0.1).build(GayssianSequence.class);
        }
        pointThread = new DataSourceThread(sourceHandler, aSequence);
        pointThread.start();
    }

    private void addPoint(double pX, double pY) {
        double noise = 0;

        if (aPreferences.noise().get()) {
            noise = rndGenerator.nextGaussian();
        }
        double x = pX;
        double y = pY + 0.1 * noise;
        mCurrentSeries.add(x, y);
        mChartView.repaint();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("currentSeries", mCurrentSeries);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedState) {
        super.onRestoreInstanceState(savedState);
        mCurrentSeries = (XYSeries) savedState.getSerializable("currentSeries");
    }
}