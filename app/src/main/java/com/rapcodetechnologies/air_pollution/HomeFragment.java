package com.rapcodetechnologies.air_pollution;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.GridLabelRenderer;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Random;

public class HomeFragment extends Fragment {

    private GraphView graph;
    private TextView aqiValue, aqiStatus;
    private TextView todayNo2Value, todayNo2Status, todayO3Value, todayO3Status;
    private TextView tomorrowNo2Value, tomorrowNo2Status, tomorrowO3Value, tomorrowO3Status;
    private TextView dayAfterNo2Value, dayAfterNo2Status, dayAfterO3Value, dayAfterO3Status;

    private Random random = new Random();

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        initializeViews(view);
        setupGraph();
        setupForecastData();
        updateCurrentAQI();

        return view;
    }

    private void initializeViews(View view) {
        graph = view.findViewById(R.id.graph);
        aqiValue = view.findViewById(R.id.aqi_value);
        aqiStatus = view.findViewById(R.id.aqi_status);

        // Today's data
        todayNo2Value = view.findViewById(R.id.today_no2_value);
        todayNo2Status = view.findViewById(R.id.today_no2_status);
        todayO3Value = view.findViewById(R.id.today_o3_value);
        todayO3Status = view.findViewById(R.id.today_o3_status);

        // Tomorrow's data
        tomorrowNo2Value = view.findViewById(R.id.tomorrow_no2_value);
        tomorrowNo2Status = view.findViewById(R.id.tomorrow_no2_status);
        tomorrowO3Value = view.findViewById(R.id.tomorrow_o3_value);
        tomorrowO3Status = view.findViewById(R.id.tomorrow_o3_status);

        // Day after tomorrow's data
        dayAfterNo2Value = view.findViewById(R.id.day_after_no2_value);
        dayAfterNo2Status = view.findViewById(R.id.day_after_no2_status);
        dayAfterO3Value = view.findViewById(R.id.day_after_o3_value);
        dayAfterO3Status = view.findViewById(R.id.day_after_o3_status);
    }

    private void setupGraph() {
        // Generate realistic air quality data for 24 hours
        DataPoint[] no2Data = generatePollutantData(24, 20, 60); // NO2 data
        DataPoint[] o3Data = generatePollutantData(24, 30, 80);  // O3 data

        // Create line series for NO2
        LineGraphSeries<DataPoint> no2Series = new LineGraphSeries<>(no2Data);
        no2Series.setColor(ContextCompat.getColor(requireContext(), R.color.no2_color));
        no2Series.setThickness(6);
        no2Series.setDrawDataPoints(true);
        no2Series.setDataPointsRadius(8);
        no2Series.setTitle("NO₂");

        // Add gradient background for NO2
        no2Series.setDrawBackground(true);
        no2Series.setBackgroundColor(Color.parseColor("#33FF5722"));

        // Create line series for O3
        LineGraphSeries<DataPoint> o3Series = new LineGraphSeries<>(o3Data);
        o3Series.setColor(ContextCompat.getColor(requireContext(), R.color.o3_color));
        o3Series.setThickness(6);
        o3Series.setDrawDataPoints(true);
        o3Series.setDataPointsRadius(8);
        o3Series.setTitle("O₃");

        // Add gradient background for O3
        o3Series.setDrawBackground(true);
        o3Series.setBackgroundColor(Color.parseColor("#332196F3"));

        // Add series to graph
        graph.addSeries(no2Series);
        graph.addSeries(o3Series);

        // Configure graph appearance
        configureGraph();
    }

    private void configureGraph() {
        // Configure viewport
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(23);
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMinY(0);
        graph.getViewport().setMaxY(100);

        // Enable scrolling and zooming
        graph.getViewport().setScrollable(true);
        graph.getViewport().setScrollableY(true);
        graph.getViewport().setScalable(true);
        graph.getViewport().setScalableY(true);

        // Configure grid and labels
        GridLabelRenderer gridLabel = graph.getGridLabelRenderer();
        gridLabel.setHorizontalAxisTitle("Hour of Day");
        gridLabel.setVerticalAxisTitle("Concentration (μg/m³)");
        gridLabel.setHorizontalAxisTitleTextSize(32);
        gridLabel.setVerticalAxisTitleTextSize(32);
        gridLabel.setTextSize(28);
        gridLabel.setGridColor(ContextCompat.getColor(requireContext(), R.color.graph_grid));
        gridLabel.setHorizontalLabelsColor(ContextCompat.getColor(requireContext(), R.color.graph_label));
        gridLabel.setVerticalLabelsColor(ContextCompat.getColor(requireContext(), R.color.graph_label));
        gridLabel.setNumHorizontalLabels(6);
        gridLabel.setNumVerticalLabels(6);

        // Customize grid appearance
        gridLabel.setGridStyle(GridLabelRenderer.GridStyle.BOTH);
        gridLabel.setHighlightZeroLines(false);

        // Add padding
        gridLabel.setPadding(32);

        // Enable legend
        graph.getLegendRenderer().setVisible(true);
        graph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);
        graph.getLegendRenderer().setBackgroundColor(Color.TRANSPARENT);
        graph.getLegendRenderer().setTextSize(32);
    }

    private DataPoint[] generatePollutantData(int hours, int baseValue, int maxValue) {
        DataPoint[] dataPoints = new DataPoint[hours];

        for (int i = 0; i < hours; i++) {
            // Create realistic daily pollution pattern
            double timeMultiplier = Math.sin(Math.PI * i / 12.0) * 0.3 + 0.7; // Daily cycle
            double randomVariation = 0.8 + (random.nextDouble() * 0.4); // ±20% random variation
            double value = baseValue + (maxValue - baseValue) * timeMultiplier * randomVariation;

            // Add some noise for realism
            value += (random.nextGaussian() * 5);

            // Ensure value is within reasonable bounds
            value = Math.max(10, Math.min(maxValue + 20, value));

            dataPoints[i] = new DataPoint(i, value);
        }

        return dataPoints;
    }

    private void setupForecastData() {
        // Generate forecast data for next 3 days
        ForecastData today = generateForecastData();
        ForecastData tomorrow = generateForecastData();
        ForecastData dayAfter = generateForecastData();

        // Update UI with forecast data
        updateForecastUI(today, tomorrow, dayAfter);
    }

    private ForecastData generateForecastData() {
        // Generate realistic pollution values
        int no2Value = 20 + random.nextInt(40); // 20-60 μg/m³
        int o3Value = 30 + random.nextInt(50);  // 30-80 μg/m³

        return new ForecastData(no2Value, o3Value);
    }

    private void updateForecastUI(ForecastData today, ForecastData tomorrow, ForecastData dayAfter) {
        // Update today's data
        todayNo2Value.setText(String.valueOf(today.no2));
        todayNo2Status.setText(getAQIStatus(today.no2, "NO2"));
        todayNo2Status.setTextColor(ContextCompat.getColor(requireContext(), getAQIColor(today.no2, "NO2")));

        todayO3Value.setText(String.valueOf(today.o3));
        todayO3Status.setText(getAQIStatus(today.o3, "O3"));
        todayO3Status.setTextColor(ContextCompat.getColor(requireContext(), getAQIColor(today.o3, "O3")));

        // Update tomorrow's data
        tomorrowNo2Value.setText(String.valueOf(tomorrow.no2));
        tomorrowNo2Status.setText(getAQIStatus(tomorrow.no2, "NO2"));
        tomorrowNo2Status.setTextColor(ContextCompat.getColor(requireContext(), getAQIColor(tomorrow.no2, "NO2")));

        tomorrowO3Value.setText(String.valueOf(tomorrow.o3));
        tomorrowO3Status.setText(getAQIStatus(tomorrow.o3, "O3"));
        tomorrowO3Status.setTextColor(ContextCompat.getColor(requireContext(), getAQIColor(tomorrow.o3, "O3")));

        // Update day after tomorrow's data
        dayAfterNo2Value.setText(String.valueOf(dayAfter.no2));
        dayAfterNo2Status.setText(getAQIStatus(dayAfter.no2, "NO2"));
        dayAfterNo2Status.setTextColor(ContextCompat.getColor(requireContext(), getAQIColor(dayAfter.no2, "NO2")));

        dayAfterO3Value.setText(String.valueOf(dayAfter.o3));
        dayAfterO3Status.setText(getAQIStatus(dayAfter.o3, "O3"));
        dayAfterO3Status.setTextColor(ContextCompat.getColor(requireContext(), getAQIColor(dayAfter.o3, "O3")));
    }

    private void updateCurrentAQI() {
        // Calculate overall AQI based on current pollutant levels
        int currentAQI = calculateOverallAQI();
        aqiValue.setText(String.valueOf(currentAQI));

        String status = getOverallAQIStatus(currentAQI);
        int color = getOverallAQIColor(currentAQI);

        aqiStatus.setText(status);
        aqiValue.setTextColor(ContextCompat.getColor(requireContext(), color));
        aqiStatus.setTextColor(ContextCompat.getColor(requireContext(), color));
    }

    private int calculateOverallAQI() {
        // Simplified AQI calculation
        return 35 + random.nextInt(30); // Returns AQI between 35-65 (Good to Moderate range)
    }

    private String getAQIStatus(int value, String pollutant) {
        if (pollutant.equals("NO2")) {
            if (value <= 40) return "Good";
            else if (value <= 80) return "Moderate";
            else if (value <= 180) return "Unhealthy";
            else return "Very Unhealthy";
        } else { // O3
            if (value <= 50) return "Good";
            else if (value <= 100) return "Moderate";
            else if (value <= 168) return "Unhealthy";
            else return "Very Unhealthy";
        }
    }

    private int getAQIColor(int value, String pollutant) {
        String status = getAQIStatus(value, pollutant);
        switch (status) {
            case "Good":
                return R.color.aqi_good;
            case "Moderate":
                return R.color.aqi_moderate;
            case "Unhealthy":
                return R.color.aqi_unhealthy;
            case "Very Unhealthy":
                return R.color.aqi_very_unhealthy;
            default:
                return R.color.aqi_good;
        }
    }

    private String getOverallAQIStatus(int aqi) {
        if (aqi <= 50) return "Good";
        else if (aqi <= 100) return "Moderate";
        else if (aqi <= 150) return "Unhealthy for Sensitive";
        else if (aqi <= 200) return "Unhealthy";
        else if (aqi <= 300) return "Very Unhealthy";
        else return "Hazardous";
    }

    private int getOverallAQIColor(int aqi) {
        if (aqi <= 50) return R.color.aqi_good;
        else if (aqi <= 100) return R.color.aqi_moderate;
        else if (aqi <= 150) return R.color.aqi_unhealthy_sensitive;
        else if (aqi <= 200) return R.color.aqi_unhealthy;
        else if (aqi <= 300) return R.color.aqi_very_unhealthy;
        else return R.color.aqi_hazardous;
    }

    // Helper class for forecast data
    private static class ForecastData {
        int no2;
        int o3;

        ForecastData(int no2, int o3) {
            this.no2 = no2;
            this.o3 = o3;
        }
    }
}