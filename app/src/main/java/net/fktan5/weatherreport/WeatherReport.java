package net.fktan5.weatherreport;

import java.util.List;

/**
 * Created on 15/03/31.
 */
public class WeatherReport {
    private String base;
    private String cod;
    private String dt;
    private String id;
    private String name;
    private Main main;
    private List<Weather> weather;

    public WeatherReport(){}

    public List<Weather> getWeather() {
        return weather;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getDt() {
        return dt;
    }

    public String getCod() {
        return cod;
    }

    public String getBase() {
        return base;
    }

    public Main getMain(){ return this.main; }

    public List<Weather> getBody(){ return this.weather; }

    public class Main {
        private float temp;
        private float humidity;
        private float pressure;
        private float temp_min;
        private float temp_max;

        public float getTemp() {
            return temp;
        }

        public float getHumidity() {
            return humidity;
        }

        public float getPressure() {
            return pressure;
        }

        public float getTemp_min() {
            return temp_min;
        }

        public float getTemp_max() {
            return temp_max;
        }
    }

    public class Weather{
        private long id;
        private String main;
        private String description;
        private String icon;
        public long getId() {
            return id;
        }

        public String getMain() {
            return main;
        }

        public String getDescription() {
            return description;
        }

        public String getIcon() {
            return icon;
        }

    }
}
