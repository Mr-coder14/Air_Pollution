package Classes;

import java.util.Random;

public class City {
    private String name;
    private String district;
    private int aqi;
    private String aqiStatus;
    private int pm25;
    private int pm10;
    private int no2;
    private int o3;
    private String lastUpdated;
    private String aqiColor;

    public City(String name, String district) {
        this.name = name;
        this.district = district;
        generateRandomPollutionData();
        setAqiStatus();
        generateLastUpdatedTime();
    }

    // Constructor for backward compatibility
    public City(String name) {
        this.name = name;
        this.district = getDistrictFromName(name);
        generateRandomPollutionData();
        setAqiStatus();
        generateLastUpdatedTime();
    }

    private void generateRandomPollutionData() {
        Random random = new Random();

        // Generate realistic pollution values
        this.pm25 = 25 + random.nextInt(100); // 25-125 μg/m³
        this.pm10 = 40 + random.nextInt(150); // 40-190 μg/m³
        this.no2 = 20 + random.nextInt(80);   // 20-100 μg/m³
        this.o3 = 30 + random.nextInt(120);   // 30-150 μg/m³

        // Calculate AQI based on PM2.5 (simplified calculation)
        this.aqi = calculateAQI(this.pm25);
    }

    private int calculateAQI(int pm25) {
        // Simplified AQI calculation based on PM2.5
        if (pm25 <= 30) return 20 + (pm25 * 30 / 30);
        else if (pm25 <= 60) return 51 + ((pm25 - 31) * 49 / 29);
        else if (pm25 <= 90) return 101 + ((pm25 - 61) * 49 / 29);
        else if (pm25 <= 120) return 151 + ((pm25 - 91) * 49 / 29);
        else if (pm25 <= 250) return 201 + ((pm25 - 121) * 99 / 129);
        else return 301 + ((pm25 - 251) * 199 / 249);
    }

    private void setAqiStatus() {
        if (aqi <= 50) {
            aqiStatus = "Good";
            aqiColor = "#4CAF50";
        } else if (aqi <= 100) {
            aqiStatus = "Moderate";
            aqiColor = "#FFEB3B";
        } else if (aqi <= 150) {
            aqiStatus = "Unhealthy for Sensitive";
            aqiColor = "#FF9800";
        } else if (aqi <= 200) {
            aqiStatus = "Unhealthy";
            aqiColor = "#F44336";
        } else if (aqi <= 300) {
            aqiStatus = "Very Unhealthy";
            aqiColor = "#9C27B0";
        } else {
            aqiStatus = "Hazardous";
            aqiColor = "#660000";
        }
    }

    private void generateLastUpdatedTime() {
        Random random = new Random();
        int minutes = random.nextInt(30) + 1; // 1-30 minutes ago
        if (minutes == 1) {
            lastUpdated = "Updated 1 min ago";
        } else if (minutes < 60) {
            lastUpdated = "Updated " + minutes + " mins ago";
        } else {
            lastUpdated = "Updated 1 hour ago";
        }
    }

    private String getDistrictFromName(String cityName) {
        // Map common Delhi locations to their districts
        switch (cityName.toLowerCase()) {
            case "anand vihar":
            case "patparganj":
            case "shahdara":
            case "mayur vihar":
                return "East Delhi";
            case "rk puram":
            case "lodhi road":
            case "safdarjung":
            case "aiims":
                return "South Delhi";
            case "punjabi bagh":
            case "wazirpur":
            case "ashok vihar":
                return "North West Delhi";
            case "mundka":
            case "nangloi":
            case "rohini":
                return "Outer Delhi";
            case "mandir marg":
            case "connaught place":
            case "ito":
                return "Central Delhi";
            case "ihbas dilshad garden":
            case "dilshad garden":
                return "North East Delhi";
            case "dwarka":
            case "najafgarh":
            case "bawana":
                return "South West Delhi";
            case "jahangirpuri":
            case "burari":
            case "narela":
                return "North Delhi";
            case "shadipur":
            case "patel nagar":
            case "karol bagh":
                return "Central Delhi";
            case "okhla":
            case "nehru place":
            case "lajpat nagar":
                return "South East Delhi";
            default:
                return "Delhi";
        }
    }

    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDistrict() { return district; }
    public void setDistrict(String district) { this.district = district; }

    public int getAqi() { return aqi; }
    public void setAqi(int aqi) { this.aqi = aqi; }

    public String getAqiStatus() { return aqiStatus; }
    public void setAqiStatus(String aqiStatus) { this.aqiStatus = aqiStatus; }

    public int getPm25() { return pm25; }
    public void setPm25(int pm25) { this.pm25 = pm25; }

    public int getPm10() { return pm10; }
    public void setPm10(int pm10) { this.pm10 = pm10; }

    public int getNo2() { return no2; }
    public void setNo2(int no2) { this.no2 = no2; }

    public int getO3() { return o3; }
    public void setO3(int o3) { this.o3 = o3; }

    public String getLastUpdated() { return lastUpdated; }
    public void setLastUpdated(String lastUpdated) { this.lastUpdated = lastUpdated; }

    public String getAqiColor() { return aqiColor; }
    public void setAqiColor(String aqiColor) { this.aqiColor = aqiColor; }
}