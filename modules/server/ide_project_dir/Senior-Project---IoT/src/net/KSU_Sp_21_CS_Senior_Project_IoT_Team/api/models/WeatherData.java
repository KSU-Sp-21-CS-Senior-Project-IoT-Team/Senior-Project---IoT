package net.KSU_Sp_21_CS_Senior_Project_IoT_Team.api.models;

public class WeatherData {
    public static final class MainData {
        public final Double temp;
        public final Double feels_like;
        public final Double temp_min;
        public final Double temp_max;
        public final Integer pressure;
        public final Integer humidity;

        public MainData(Double temp, Double feels_like, Double temp_min, Double temp_max, Integer pressure, Integer humidity) {
            this.temp = temp;
            this.feels_like = feels_like;
            this.temp_min = temp_min;
            this.temp_max = temp_max;
            this.pressure = pressure;
            this.humidity = humidity;
        }
    }

    public final MainData main;

    public WeatherData(MainData main) {
        this.main = main;
    }
}
