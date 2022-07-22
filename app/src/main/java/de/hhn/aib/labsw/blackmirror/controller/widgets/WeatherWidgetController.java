package de.hhn.aib.labsw.blackmirror.controller.widgets;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import de.hhn.aib.labsw.blackmirror.controller.API.TopicListener;
import de.hhn.aib.labsw.blackmirror.model.ApiDataModels.Location;
import de.hhn.aib.labsw.blackmirror.model.WeatherSet;
import de.hhn.aib.labsw.blackmirror.view.widgets.AbstractWidget;
import de.hhn.aib.labsw.blackmirror.view.widgets.WeatherWidget;

import javax.swing.*;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.*;

/**
 * @author Luis Gutzeit
 * @version 2022-05-11
 */
public class WeatherWidgetController extends AbstractWidgetController {
    private static final String ADDRESS = "https://api.open-meteo.com/v1/forecast?latitude=%02.4f&longitude=%02.4f&hourly=temperature_2m,relativehumidity_2m,apparent_temperature,pressure_msl,precipitation,weathercode,windspeed_10m&timezone=Europe/Berlin";
    private static final DateTimeFormatter DATEFORMAT = new DateTimeFormatterBuilder().appendPattern("yyyy-MM-dd'T'HH:mm").parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0).toFormatter();

    private final WeatherWidget widget;
    ScheduledFuture<?> scheduledUpdate;

    //Location for which the weather data should be downloaded
    private double LAT = 49.066;
    private double LON = 9.220;

    public WeatherWidgetController() {
        this.widget = new WeatherWidget();
        scheduledUpdate = Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(new WeatherReceiver(),0,15,TimeUnit.MINUTES);
        subscribe("location");
    }

    @Override
    public AbstractWidget getWidget() {
        return widget;
    }

    /**
     * converts the given JSON Object to a List of weather sets
     *
     * @param data the weather data received as JSON
     */
    private void updateData(JsonNode data) {
        ArrayNode times = (ArrayNode) data.get("hourly").get("time");
        ArrayNode weathercodes = (ArrayNode) data.get("hourly").get("weathercode");
        ArrayNode temperatures = (ArrayNode) data.get("hourly").get("temperature_2m");
        ArrayNode precipitations = (ArrayNode) data.get("hourly").get("precipitation");
        ArrayNode windspeeds = (ArrayNode) data.get("hourly").get("windspeed_10m");
        ArrayNode humidities = (ArrayNode) data.get("hourly").get("relativehumidity_2m");
        ArrayNode pressures = (ArrayNode) data.get("hourly").get("pressure_msl");

        List<WeatherSet> weatherSets = new ArrayList<>();
        for (int i = 0; i < times.size(); i++) {
            WeatherSet temp = new WeatherSet();
            temp.setTemperature(temperatures.get(i).asDouble());
            temp.setPrecipitation(precipitations.get(i).asDouble());
            temp.setWeathercode(weathercodes.get(i).asInt());
            temp.setWindspeed(windspeeds.get(i).asDouble());
            temp.setHumidity(humidities.get(i).asInt());
            temp.setPressure(pressures.get(i).asDouble());
            temp.setTimestamp(LocalDateTime.parse(times.get(i).textValue(), DATEFORMAT));
            weatherSets.add(temp);
        }

        //GUI stuff must run on UI thread...
        SwingUtilities.invokeLater(()-> widget.updateGUI(weatherSets));
    }

    /**
     * set the location for which the weather information should be downloaded
     * @throws IllegalArgumentException when coordinates invalid
     */
    public void setGPSLocation(double lat, double lon) {
        if(lat > 90 || lat < -90 || lon > 180 || lon < -180){
            throw new IllegalArgumentException("invalid coordinates!");
        }
        else{
            LAT = lat;
            LON = lon;
        }
    }

    @Override
    public void close() throws Exception {
        super.close();
        scheduledUpdate.cancel(true);
        widget.dispose();
    }

    /**
     * weather receiver that downloads new information automatically when called
     */
    class WeatherReceiver extends Thread {
        /**
         * connect to the server and download the json file.
         */
        @Override
        public void run() {
            try {
                HttpClient client = HttpClient.newHttpClient();
                HttpRequest req = HttpRequest.newBuilder(URI.create(String.format(Locale.US,ADDRESS,LAT, LON))).header("accept", "application/json").build();
                CompletableFuture<HttpResponse<String>> futureResult = client.sendAsync(req, HttpResponse.BodyHandlers.ofString());
                ObjectMapper mapper = new ObjectMapper();
                updateData(mapper.readTree(futureResult.get().body()));
            } catch (InterruptedException | ExecutionException | IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void dataReceived(String topic, JsonNode object) {
        super.dataReceived(topic, object);

        if(topic.equals("location")) {
            try {
                Location loc = TopicListener.nodeToObject(object, Location.class);
                setGPSLocation(loc.lat(), loc.lon());
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            WeatherReceiver receiver = new WeatherReceiver();
            receiver.start();
        }
    }
}
