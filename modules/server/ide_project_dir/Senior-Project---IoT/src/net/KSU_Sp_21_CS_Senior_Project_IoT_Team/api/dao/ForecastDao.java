package net.KSU_Sp_21_CS_Senior_Project_IoT_Team.api.dao;

import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import net.KSU_Sp_21_CS_Senior_Project_IoT_Team.api.models.Forecast;
import net.KSU_Sp_21_CS_Senior_Project_IoT_Team.api.models.Location;
import net.KSU_Sp_21_CS_Senior_Project_IoT_Team.api.models.Schedule;
import net.KSU_Sp_21_CS_Senior_Project_IoT_Team.api.util.Utils;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class ForecastDao implements Dao {
    enum Query {
        ;
        public final String sql;

        Query(String sql) {
            this.sql = sql;
        }
    }

    private static final long FORECAST_CACHE_LIFETIME = 43200000;

    public Forecast getForecastByLocation(Location location) {
        final Connection connection;
        if ((connection = Dao.getDBConnection()) == null) return null;
        try {
            final PreparedStatement statement = connection.prepareStatement(
                    Query.GET_CUR_FORECAST.sql
            );

            statement.setString(1, location.city);
            statement.setString(2, location.province);
            statement.setString(3, location.country);

            final JsonArray json = Utils.rsToJSON(statement.executeQuery());
            if (json.size() == 0) {
                // we don't have a forecast cached
            }

            Forecast forecast = GSON.fromJson(json, Forecast.class);
            if (forecast.date < System.currentTimeMillis() + FORECAST_CACHE_LIFETIME) {
                forecast = new Forecast(
                        0, System.currentTimeMillis(), queryWeatherAPI(location), location.LocationID
                );
                // insert into database
            }

            return forecast;
        } catch (SQLException sqlException) {
            sqlException.printStackTrace(); // TODO: proper logging
        }
        return null;
    }

    private static String queryWeatherAPI(Location location) {
        // get new forecast
        ExternalAPIServiceConfig weatherAPIConfig = Dao.getWeatherAPIConfig();
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(
                        String.format(
                                "%s?q=%s,%s,%s&appid=%s",
                                weatherAPIConfig.address,
                                location.city, location.province, location.country,
                                weatherAPIConfig.key
                        )
                ))
                .build();
    }

    @Override
    public void close() throws IOException {
        // TODO
    }
}
