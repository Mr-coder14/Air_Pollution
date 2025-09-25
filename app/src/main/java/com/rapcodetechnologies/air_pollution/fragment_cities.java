package com.rapcodetechnologies.air_pollution;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import Adaptors.CityAdapter;
import Classes.City;

public class fragment_cities extends Fragment {

    private RecyclerView recyclerView;
    private CityAdapter adapter;
    private EditText searchEditText;
    private ImageView clearSearch;
    private LinearLayout emptyStateLayout;
    private TextView goodStationsCount, moderateStationsCount, unhealthyStationsCount;
    private List<City> cityList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cities, container, false);

        initializeViews(view);
        setupRecyclerView();
        setupSearchFunctionality();
        populateCityData();
        updateStatistics();

        return view;
    }

    private void initializeViews(View view) {
        recyclerView = view.findViewById(R.id.recyclerCities);
        searchEditText = view.findViewById(R.id.searchEditText);
        clearSearch = view.findViewById(R.id.clearSearch);
        emptyStateLayout = view.findViewById(R.id.emptyStateLayout);
        goodStationsCount = view.findViewById(R.id.goodStationsCount);
        moderateStationsCount = view.findViewById(R.id.moderateStationsCount);
        unhealthyStationsCount = view.findViewById(R.id.unhealthyStationsCount);
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
    }

    private void setupSearchFunctionality() {
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String query = s.toString().trim();

                // Show/hide clear button
                if (query.isEmpty()) {
                    clearSearch.setVisibility(View.GONE);
                } else {
                    clearSearch.setVisibility(View.VISIBLE);
                }

                // Filter the list
                if (adapter != null) {
                    adapter.filter(query);

                    // Show/hide empty state
                    if (adapter.getItemCount() == 0 && !query.isEmpty()) {
                        emptyStateLayout.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                    } else {
                        emptyStateLayout.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        clearSearch.setOnClickListener(v -> {
            searchEditText.setText("");
            searchEditText.clearFocus();
        });
    }

    private void populateCityData() {
        cityList = new ArrayList<>();

        // Add comprehensive list of Delhi monitoring stations
        cityList.add(new City("Anand Vihar", "East Delhi"));
        cityList.add(new City("RK Puram", "South Delhi"));
        cityList.add(new City("Punjabi Bagh", "North West Delhi"));
        cityList.add(new City("Mundka", "Outer Delhi"));
        cityList.add(new City("Mandir Marg", "Central Delhi"));
        cityList.add(new City("IHBAS Dilshad Garden", "North East Delhi"));
        cityList.add(new City("Dwarka", "South West Delhi"));
        cityList.add(new City("Jahangirpuri", "North Delhi"));
        cityList.add(new City("Patparganj", "East Delhi"));
        cityList.add(new City("Shadipur", "Central Delhi"));
        cityList.add(new City("Wazirpur", "North West Delhi"));
        cityList.add(new City("Okhla", "South East Delhi"));

        // Additional monitoring stations
        cityList.add(new City("Lodhi Road", "South Delhi"));
        cityList.add(new City("Mayur Vihar", "East Delhi"));
        cityList.add(new City("Rohini", "Outer Delhi"));
        cityList.add(new City("Shahdara", "East Delhi"));
        cityList.add(new City("Ashok Vihar", "North West Delhi"));
        cityList.add(new City("Lajpat Nagar", "South East Delhi"));
        cityList.add(new City("Karol Bagh", "Central Delhi"));
        cityList.add(new City("Connaught Place", "Central Delhi"));
        cityList.add(new City("Nehru Place", "South East Delhi"));
        cityList.add(new City("Patel Nagar", "Central Delhi"));
        cityList.add(new City("ITO", "Central Delhi"));
        cityList.add(new City("Safdarjung", "South Delhi"));
        cityList.add(new City("AIIMS", "South Delhi"));
        cityList.add(new City("Burari", "North Delhi"));
        cityList.add(new City("Narela", "North Delhi"));
        cityList.add(new City("Najafgarh", "South West Delhi"));
        cityList.add(new City("Bawana", "South West Delhi"));
        cityList.add(new City("Nangloi", "Outer Delhi"));

        // More locations for comprehensive coverage
        cityList.add(new City("Janakpuri", "West Delhi"));
        cityList.add(new City("Rajouri Garden", "West Delhi"));
        cityList.add(new City("Tilak Nagar", "West Delhi"));
        cityList.add(new City("Vikaspuri", "West Delhi"));
        cityList.add(new City("Uttam Nagar", "West Delhi"));
        cityList.add(new City("Paschim Vihar", "West Delhi"));
        cityList.add(new City("Pitampura", "North West Delhi"));
        cityList.add(new City("Shalimar Bagh", "North West Delhi"));
        cityList.add(new City("Model Town", "North West Delhi"));
        cityList.add(new City("Civil Lines", "North Delhi"));
        cityList.add(new City("Kashmere Gate", "North Delhi"));
        cityList.add(new City("Red Fort", "Central Delhi"));
        cityList.add(new City("India Gate", "Central Delhi"));
        cityList.add(new City("Lodi Gardens", "South Delhi"));
        cityList.add(new City("Vasant Kunj", "South West Delhi"));
        cityList.add(new City("Mahipalpur", "South West Delhi"));
        cityList.add(new City("Hauz Khas", "South Delhi"));
        cityList.add(new City("Green Park", "South Delhi"));
        cityList.add(new City("Greater Kailash", "South Delhi"));
        cityList.add(new City("Kalkaji", "South East Delhi"));
        cityList.add(new City("Govindpuri", "South East Delhi"));
        cityList.add(new City("Sarita Vihar", "South East Delhi"));

        adapter = new CityAdapter(cityList);
        adapter.setOnCityClickListener(this::onCityClick);
        recyclerView.setAdapter(adapter);
    }

    private void onCityClick(City city) {
        // Handle city click - could navigate to detail view
        Toast.makeText(getContext(),
                "Selected: " + city.getName() + "\nAQI: " + city.getAqi() + " (" + city.getAqiStatus() + ")",
                Toast.LENGTH_SHORT).show();

        // You can add navigation logic here
        // For example: findNavController().navigate(R.id.action_to_city_detail, bundle);
    }

    private void updateStatistics() {
        if (cityList == null || cityList.isEmpty()) return;

        int goodCount = 0, moderateCount = 0, unhealthyCount = 0;

        for (City city : cityList) {
            String status = city.getAqiStatus();
            switch (status) {
                case "Good":
                    goodCount++;
                    break;
                case "Moderate":
                    moderateCount++;
                    break;
                case "Unhealthy for Sensitive":
                case "Unhealthy":
                case "Very Unhealthy":
                case "Hazardous":
                    unhealthyCount++;
                    break;
            }
        }

        // Update UI
        if (goodStationsCount != null) goodStationsCount.setText(String.valueOf(goodCount));
        if (moderateStationsCount != null) moderateStationsCount.setText(String.valueOf(moderateCount));
        if (unhealthyStationsCount != null) unhealthyStationsCount.setText(String.valueOf(unhealthyCount));
    }

    // Method to refresh data (can be called from parent activity/fragment)
    public void refreshData() {
        if (cityList != null) {
            // Regenerate pollution data for all cities
            for (City city : cityList) {
                // You could add a refresh method to City class
                // city.refreshPollutionData();
            }

            if (adapter != null) {
                adapter.notifyDataSetChanged();
            }

            updateStatistics();
        }
    }

    // Method to get current filter query
    public String getCurrentSearchQuery() {
        return searchEditText != null ? searchEditText.getText().toString() : "";
    }

    // Method to set search query programmatically
    public void setSearchQuery(String query) {
        if (searchEditText != null) {
            searchEditText.setText(query);
        }
    }
}