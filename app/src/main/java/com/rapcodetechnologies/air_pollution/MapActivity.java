package com.rapcodetechnologies.air_pollution;

import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Random;

public class MapActivity extends AppCompatActivity {

    private GraphView graph;
    private Spinner timeRangeSpinner, pollutantSpinner;
    private TextView graphTitle, currentValue, minValue, maxValue, avgValue;
    private final Random random = new Random();

    private String selectedTimeRange = "24 Hours";
    private String selectedPollutant = "Both (NO₂ & O₃)";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        initializeViews();
        setupToolbar();
        setupSpinners();
        updateGraph();
    }

    private void initializeViews() {
        graph = findViewById(R.id.full_graph);
        timeRangeSpinner = findViewById(R.id.time_range_spinner);
        pollutantSpinner = findViewById(R.id.pollutant_spinner);
        graphTitle = findViewById(R.id.graph_title);
        currentValue = findViewById(R.id.current_value);
        minValue = findViewById(R.id.min_value);
        maxValue = findViewById(R.id.max_value);
        avgValue = findViewById(R.id.avg_value);
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Air Quality Analysis");
        }
    }

    private void setupSpinners() {
        // Time Range Spinner
        String[] timeRanges = {"24 Hours", "3 Days", "7 Days", "30 Days"};
        ArrayAdapter<String> timeAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, timeRanges);
        timeRangeSpinner.setAdapter(timeAdapter);
        timeRangeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedTimeRange = timeRanges[position];
                updateGraph();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        // Pollutant Spinner
        String[] pollutants = {"Both (NO₂ & O₃)", "NO₂ Only", "O₃ Only"};
        ArrayAdapter<String> pollutantAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, pollutants);
        pollutantSpinner.setAdapter(pollutantAdapter);
        pollutantSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedPollutant = pollutants[position];
                updateGraph();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void updateGraph() {
        graph.removeAllSeries();

        int dataPoints = getDataPointsForTimeRange(selectedTimeRange);

        if (selectedPollutant.equals("Both (NO₂ & O₃)") || selectedPollutant.equals("NO₂ Only")) {
            DataPoint[] no2Data = generatePollutantData(dataPoints, 20, 60);
            LineGraphSeries<DataPoint> no2Series = new LineGraphSeries<>(no2Data);
            no2Series.setColor(ContextCompat.getColor(this, R.color.no2_color));
            no2Series.setThickness(8);
            no2Series.setDrawDataPoints(true);
            no2Series.setDataPointsRadius(10);
            no2Series.setTitle("NO₂");
            no2Series.setDrawBackground(true);
            no2Series.setBackgroundColor(Color.parseColor("#33FF5722"));
            graph.addSeries(no2Series);
        }

        if (selectedPollutant.equals("Both (NO₂ & O₃)") || selectedPollutant.equals("O₃ Only")) {
            DataPoint[] o3Data = generatePollutantData(dataPoints, 30, 80);
            LineGraphSeries<DataPoint> o3Series = new LineGraphSeries<>(o3Data);
            o3Series.setColor(ContextCompat.getColor(this, R.color.o3_color));
            o3Series.setThickness(8);
            o3Series.setDrawDataPoints(true);
            o3Series.setDataPointsRadius(10);
            o3Series.setTitle("O₃");
            o3Series.setDrawBackground(true);
            o3Series.setBackgroundColor(Color.parseColor("#332196F3"));
            graph.addSeries(o3Series);
        }

        configureGraph(dataPoints);
        updateStatistics();
    }

    private int getDataPointsForTimeRange(String timeRange) {
        switch (timeRange) {
            case "24 Hours":
                return 24;
            case "3 Days":
                return 72;
            case "7 Days":
                return 168;
            case "30 Days":
                return 720;
            default:
                return 24;
        }
    }

    private void configureGraph(int maxX) {
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(maxX - 1);
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMinY(0);
        graph.getViewport().setMaxY(120);

        graph.getViewport().setScrollable(true);
        graph.getViewport().setScrollableY(true);
        graph.getViewport().setScalable(true);
        graph.getViewport().setScalableY(true);

        GridLabelRenderer gridLabel = graph.getGridLabelRenderer();
        gridLabel.setHorizontalAxisTitle(getXAxisLabel());
        gridLabel.setVerticalAxisTitle("Concentration (μg/m³)");
        gridLabel.setHorizontalAxisTitleTextSize(36);
        gridLabel.setVerticalAxisTitleTextSize(36);
        gridLabel.setTextSize(32);
        gridLabel.setGridColor(ContextCompat.getColor(this, R.color.graph_grid));
        gridLabel.setHorizontalLabelsColor(ContextCompat.getColor(this, R.color.graph_label));
        gridLabel.setVerticalLabelsColor(ContextCompat.getColor(this, R.color.graph_label));
        gridLabel.setNumHorizontalLabels(8);
        gridLabel.setNumVerticalLabels(7);
        gridLabel.setGridStyle(GridLabelRenderer.GridStyle.BOTH);
        gridLabel.setHighlightZeroLines(false);
        gridLabel.setPadding(40);

        graph.getLegendRenderer().setVisible(true);
        graph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);
        graph.getLegendRenderer().setBackgroundColor(Color.parseColor("#CC000000"));
        graph.getLegendRenderer().setTextSize(36);
        graph.getLegendRenderer().setMargin(20);
        graph.getLegendRenderer().setPadding(15);
    }

    private String getXAxisLabel() {
        switch (selectedTimeRange) {
            case "24 Hours":
                return "Hour";
            case "3 Days":
                return "Hours (3 Days)";
            case "7 Days":
                return "Hours (7 Days)";
            case "30 Days":
                return "Hours (30 Days)";
            default:
                return "Time";
        }
    }

    private DataPoint[] generatePollutantData(int hours, int baseValue, int maxValue) {
        DataPoint[] dataPoints = new DataPoint[hours];

        for (int i = 0; i < hours; i++) {
            double timeMultiplier = Math.sin(Math.PI * i / 12.0) * 0.3 + 0.7;
            double randomVariation = 0.8 + (random.nextDouble() * 0.4);
            double value = baseValue + (maxValue - baseValue) * timeMultiplier * randomVariation;
            value += (random.nextGaussian() * 5);
            value = Math.max(10, Math.min(maxValue + 20, value));
            dataPoints[i] = new DataPoint(i, value);
        }

        return dataPoints;
    }

    private void updateStatistics() {

        double current = 45.5;
        double min = 22.3;
        double max = 68.7;
        double avg = 42.8;

        currentValue.setText(String.format(Locale.getDefault(), "%.1f μg/m³", current));
        minValue.setText(String.format(Locale.getDefault(), "%.1f", min));
        maxValue.setText(String.format(Locale.getDefault(), "%.1f", max));
        avgValue.setText(String.format(Locale.getDefault(), "%.1f", avg));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}