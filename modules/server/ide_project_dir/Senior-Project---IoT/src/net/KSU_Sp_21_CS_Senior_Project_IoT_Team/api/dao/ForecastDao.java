package net.KSU_Sp_21_CS_Senior_Project_IoT_Team.api.dao;

import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import net.KSU_Sp_21_CS_Senior_Project_IoT_Team.api.models.Forecast;
import net.KSU_Sp_21_CS_Senior_Project_IoT_Team.api.models.Location;
import net.KSU_Sp_21_CS_Senior_Project_IoT_Team.api.models.Schedule;
import net.KSU_Sp_21_CS_Senior_Project_IoT_Team.api.models.WeatherData;
import net.KSU_Sp_21_CS_Senior_Project_IoT_Team.api.util.Utils;

import java.io.IOException;
import java.net.Authenticator;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.Duration;
import java.util.List;

public class ForecastDao implements Dao {
    enum Query {
        GET_CUR_FORECAST(
                "select * from iot_db.Weather_Forecast WF join iot_db.Location L on L.Location_ID = WF.Location_ID where L.City = ? and L.State = ? and L.Country = ?;"
        ),
        DEL_FORECAST(
                "delete from iot_db.Weather_Forecast where Forecast_ID = ?;"
        ),
        ADD_FORECAST(
                "insert into iot_db.Weather_Forecast (Date, Data_From_API, Location_ID) values (?, ?, ?);"
        )
        ;
        public final String sql;

        Query(String sql) {
            this.sql = sql;
        }
    }

    private static final long FORECAST_CACHE_LIFETIME = 7200000;

    public Forecast getForecastByLocation(Location location) {
        final Connection connection;

        //if ((connection = Dao.getDBConnection()) == null) return null;
        try {
            Forecast forecast;
            /*
            final PreparedStatement statement = connection.prepareStatement(
                    Query.GET_CUR_FORECAST.sql
            );

            statement.setString(1, location.city);
            statement.setString(2, location.province);
            statement.setString(3, location.country);

            final JsonArray json = Utils.rsToJSON(statement.executeQuery());

            if (json.size() == 0) {
                // we don't have a forecast cached
                String query = queryWeatherAPI(location);
                if (query == null) return null;
                // create new
                forecast = new Forecast(
                        0, System.currentTimeMillis(), query, location.LocationID
                );

                PreparedStatement addForecast = connection.prepareStatement(Query.ADD_FORECAST.sql);
                addForecast.setLong(1, forecast.date);
                addForecast.setString(2, forecast.dataJSON);
                addForecast.setInt(3, forecast.locationID);
                addForecast.execute();


            }
*/
            //forecast = GSON.fromJson(json, Forecast.class);
            //if (forecast.date < System.currentTimeMillis() + FORECAST_CACHE_LIFETIME) {
                String query = queryWeatherAPI(location);
                if (query == null) return null;
                // remove old
            /*
                PreparedStatement remForecast = connection.prepareStatement(Query.DEL_FORECAST.sql);
                remForecast.setInt(1, forecast.forecastID);
                remForecast.execute();

             */
                // create new
                forecast = new Forecast(
                        0, System.currentTimeMillis(), query, location.LocationID
                );
                /*
                PreparedStatement addForecast = connection.prepareStatement(Query.ADD_FORECAST.sql);
                addForecast.setLong(1, forecast.date);
                addForecast.setString(2, forecast.dataJSON);
                addForecast.setInt(3, forecast.locationID);
                addForecast.execute();

                 */
            //}

            return forecast;
        } catch (/*SQL*/Exception sqlException) {
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
        try {
            return HttpClient.newBuilder()
                    .version(HttpClient.Version.HTTP_1_1)
                    .build()
                    .send(request, HttpResponse.BodyHandlers.ofString())
                    .body();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Integer extractTempFromForecast(Forecast forecast) {
        WeatherData data = GSON.fromJson(forecast.dataJSON, WeatherData.class);
        if (data != null && data.main != null) {
            return kelvinToFahrenheit(data.main.temp).intValue();
        }
        return null;
    }

    private static Double kelvinToFahrenheit(double kelvin) {
        return (kelvin - 273.15) * 1.8 + 32;
    }

    @Override
    public void close() throws IOException {
        // TODO
    }
}
