package Adaptors;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.rapcodetechnologies.air_pollution.R;

import java.util.ArrayList;
import java.util.List;

import Classes.City;

public class CityAdapter extends RecyclerView.Adapter<CityAdapter.CityViewHolder> {

    private List<City> originalCityList;
    private List<City> filteredCityList;
    private OnCityClickListener onCityClickListener;

    public interface OnCityClickListener {
        void onCityClick(City city);
    }

    public CityAdapter(List<City> cityList) {
        this.originalCityList = cityList;
        this.filteredCityList = new ArrayList<>(cityList);
    }

    public void setOnCityClickListener(OnCityClickListener listener) {
        this.onCityClickListener = listener;
    }

    @NonNull
    @Override
    public CityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_city, parent, false);
        return new CityViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CityViewHolder holder, int position) {
        City city = filteredCityList.get(position);
        holder.bind(city);
    }

    @Override
    public int getItemCount() {
        return filteredCityList.size();
    }

    public void filter(String query) {
        filteredCityList.clear();

        if (query.isEmpty()) {
            filteredCityList.addAll(originalCityList);
        } else {
            String lowerCaseQuery = query.toLowerCase().trim();
            for (City city : originalCityList) {
                if (city.getName().toLowerCase().contains(lowerCaseQuery) ||
                        city.getDistrict().toLowerCase().contains(lowerCaseQuery)) {
                    filteredCityList.add(city);
                }
            }
        }

        notifyDataSetChanged();
    }

    public List<City> getFilteredList() {
        return filteredCityList;
    }

    class CityViewHolder extends RecyclerView.ViewHolder {
        private TextView cityName, cityDistrict, aqiValue, aqiStatus, pm25Value, pm10Value, lastUpdated;
        private CardView aqiCard;

        public CityViewHolder(@NonNull View itemView) {
            super(itemView);
            cityName = itemView.findViewById(R.id.cityName);
            cityDistrict = itemView.findViewById(R.id.cityDistrict);
            aqiValue = itemView.findViewById(R.id.aqiValue);
            aqiStatus = itemView.findViewById(R.id.aqiStatus);
            pm25Value = itemView.findViewById(R.id.pm25Value);
            pm10Value = itemView.findViewById(R.id.pm10Value);
            lastUpdated = itemView.findViewById(R.id.lastUpdated);
            aqiCard = itemView.findViewById(R.id.aqiCard);

            itemView.setOnClickListener(v -> {
                if (onCityClickListener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        onCityClickListener.onCityClick(filteredCityList.get(position));
                    }
                }
            });
        }

        public void bind(City city) {
            cityName.setText(city.getName());
            cityDistrict.setText(city.getDistrict());
            aqiValue.setText(String.valueOf(city.getAqi()));
            aqiStatus.setText(city.getAqiStatus());
            pm25Value.setText(String.valueOf(city.getPm25()));
            pm10Value.setText(String.valueOf(city.getPm10()));
            lastUpdated.setText(city.getLastUpdated());

            // Set AQI card background color
            try {
                int color = Color.parseColor(city.getAqiColor());
                aqiCard.setCardBackgroundColor(color);
            } catch (IllegalArgumentException e) {
                // Fallback to default color if parsing fails
                aqiCard.setCardBackgroundColor(ContextCompat.getColor(itemView.getContext(), R.color.aqi_good));
            }

            // Set status text color and background
            int statusColor = getAQIStatusColor(city.getAqiStatus());
            aqiStatus.setTextColor(ContextCompat.getColor(itemView.getContext(), statusColor));
            aqiStatus.setBackgroundResource(getStatusBackground(city.getAqiStatus()));
        }

        private int getAQIStatusColor(String status) {
            switch (status) {
                case "Good":
                    return R.color.aqi_good;
                case "Moderate":
                    return R.color.text_primary;
                case "Unhealthy for Sensitive":
                    return R.color.aqi_unhealthy_sensitive;
                case "Unhealthy":
                    return R.color.aqi_unhealthy;
                case "Very Unhealthy":
                    return R.color.aqi_very_unhealthy;
                case "Hazardous":
                    return R.color.aqi_hazardous;
                default:
                    return R.color.aqi_good;
            }
        }

        private int getStatusBackground(String status) {
            switch (status) {
                case "Good":
                    return R.drawable.status_badge_good;
                case "Moderate":
                    return R.drawable.status_badge_moderate;
                case "Unhealthy for Sensitive":
                case "Unhealthy":
                    return R.drawable.status_badge_unhealthy;
                case "Very Unhealthy":
                case "Hazardous":
                    return R.drawable.status_badge_hazardous;
                default:
                    return R.drawable.status_badge_good;
            }
        }
    }
}