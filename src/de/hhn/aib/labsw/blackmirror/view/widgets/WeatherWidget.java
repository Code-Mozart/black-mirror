package de.hhn.aib.labsw.blackmirror.view.widgets;

import de.hhn.aib.labsw.blackmirror.model.WeatherSet;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.LinkedList;
import java.util.concurrent.*;

public class WeatherWidget extends AbstractWidget {
    private static final String ADDRESS = "https://api.open-meteo.com/v1/forecast?latitude=%s&longitude=%s&hourly=temperature_2m,pressure_msl,precipitation,weathercode&current_weather=true&timezone=Europe/Berlin";
    private static final DateTimeFormatter DATEFORMAT = new DateTimeFormatterBuilder().appendPattern("yyyy-MM-dd'T'HH:mm").parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0).toFormatter();

    //Linked List with the weather sets
    LinkedList<WeatherSet> sets = null;

    //Scheduler runs the data update automatically every 15 Minutes
    ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

    //Location for which the weather data should be downloaded
    private String LAT = "49.066";
    private String LON = "9.220";

    private JPanel backgroundPanel;
    private WeatherLabel header_08h;
    private WeatherLabel temperature_08h;
    private WeatherLabel wc_08h;
    private WeatherLabel header_12h;
    private WeatherLabel temperature_12h;
    private WeatherLabel wc_12h;
    private WeatherPanel weather_08h;
    private WeatherPanel weather_12h;
    private WeatherPanel weather_16h;
    private WeatherLabel header_16h;
    private WeatherLabel temperature_16h;
    private WeatherLabel wc_16h;
    private WeatherPanel weather_20h;
    private WeatherLabel header_20h;
    private WeatherLabel temperature_20h;
    private WeatherLabel wc_20h;
    private WeatherPanel weather_c;
    private WeatherLabel header_c;
    private WeatherLabel temperature_c;
    private WeatherLabel wc_c;

    public WeatherWidget() {
        setSize(400,400);
        initGUI();
        executor.scheduleAtFixedRate(new WeatherWidget.WeatherReceiver(), 0, 15, TimeUnit.MINUTES);
    }

    private void initGUI(){
        backgroundPanel = new JPanel();
        backgroundPanel.setLayout(new GridBagLayout());
        backgroundPanel.setBackground(Color.BLACK);
        add(backgroundPanel);
        GridBagConstraints gbc = new GridBagConstraints();

        //Init current weather
        weather_c = new WeatherPanel();
        header_c = new WeatherLabel("aktuell");
        temperature_c = new WeatherLabel("12°C");
        wc_c = new WeatherLabel("leicht bewölkt");
        weather_c.add(header_c);
        weather_c.add(temperature_c);
        weather_c.add(wc_c);
        gbc.gridwidth = 4;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.ipadx = 10;
        gbc.ipady = 10;
        gbc.weightx = 0.0;
        backgroundPanel.add(weather_c,gbc);

        //Init 8h weather
        weather_08h = new WeatherPanel();
        header_08h = new WeatherLabel("08:00");
        temperature_08h = new WeatherLabel("12°C");
        wc_08h = new WeatherLabel("leicht bewölkt");
        weather_08h.add(header_08h);
        weather_08h.add(temperature_08h);
        weather_08h.add(wc_08h);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.ipadx = 10;
        gbc.ipady = 10;
        backgroundPanel.add(weather_08h,gbc);

        //Init 12h weather
        weather_12h = new WeatherPanel();
        header_12h = new WeatherLabel("12:00");
        temperature_12h = new WeatherLabel("12°C");
        wc_12h = new WeatherLabel("leicht bewölkt");
        weather_12h.add(header_12h);
        weather_12h.add(temperature_12h);
        weather_12h.add(wc_12h);
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.ipadx = 10;
        gbc.ipady = 10;
        backgroundPanel.add(weather_12h,gbc);

        //Init 16h weather
        weather_16h = new WeatherPanel();
        header_16h = new WeatherLabel("16:00");
        temperature_16h = new WeatherLabel("12°C");
        wc_16h = new WeatherLabel("leicht bewölkt");
        weather_16h.add(header_16h);
        weather_16h.add(temperature_16h);
        weather_16h.add(wc_16h);
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 3;
        gbc.ipadx = 10;
        gbc.ipady = 10;
        backgroundPanel.add(weather_16h,gbc);

        //Init 20h weather
        weather_20h = new WeatherPanel();
        header_20h = new WeatherLabel("20:00");
        temperature_20h = new WeatherLabel("12°C");
        wc_20h = new WeatherLabel("leicht bewölkt");
        weather_20h.add(header_20h);
        weather_20h.add(temperature_20h);
        weather_20h.add(wc_20h);
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 3;
        gbc.ipadx = 10;
        gbc.ipady = 10;
        backgroundPanel.add(weather_20h,gbc);
    }

    /**
     * set the location for which the weather information should be downloaded
     */
    public void setGPSLocation(String lat, String lon) {
        this.LAT = lat;
        this.LON = lon;
    }

    /**
     * converts the given JSON Object to a List of weather sets
     *
     * @param data the weather data received as JSON
     */
    private void updateData(JSONObject data) {
        JSONArray times = data.getJSONObject("hourly").getJSONArray("time");
        JSONArray weathercodes = data.getJSONObject("hourly").getJSONArray("weathercode");
        JSONArray temperatures = data.getJSONObject("hourly").getJSONArray("temperature_2m");
        JSONArray precipitations = data.getJSONObject("hourly").getJSONArray("precipitation");

        LinkedList<WeatherSet> weatherSets = new LinkedList<>();
        for (int i = 0; i < times.length(); i++) {
            WeatherSet temp = new WeatherSet();
            temp.setTemperature(temperatures.getDouble(i));
            temp.setPrecipitation(precipitations.getDouble(i));
            temp.setWeathercode(weathercodes.getInt(i));
            LocalDateTime dt = LocalDateTime.parse(times.getString(i), DATEFORMAT);
            temp.setTimestamp(dt);
            weatherSets.add(temp);
        }
        sets = weatherSets;

        //GUI stuff must run on UI thread...
        SwingUtilities.invokeLater(this::updateGUI);
    }

    private void updateGUI() {
        int cTime = LocalDateTime.now().getHour();
        WeatherSet temp = sets.get(cTime);
        temperature_c.setText("%02.2f °C".formatted(temp.getTemperature()));
        wc_c.setText(temp.getCodeAsString());
        temp = sets.get(8);
        temperature_08h.setText("%02.2f °C".formatted(temp.getTemperature()));
        wc_08h.setText(temp.getCodeAsString());
        temp = sets.get(12);
        temperature_12h.setText("%02.2f °C".formatted(temp.getTemperature()));
        wc_12h.setText(temp.getCodeAsString());
        temp = sets.get(16);
        temperature_16h.setText("%02.2f °C".formatted(temp.getTemperature()));
        wc_16h.setText(temp.getCodeAsString());
        temp = sets.get(20);
        temperature_20h.setText("%02.2f °C".formatted(temp.getTemperature()));
        wc_20h.setText(temp.getCodeAsString());
    }

    class WeatherReceiver extends Thread {
        /**
         * connect to the server and download the json file.
         */
        @Override
        public void run() {
            try {
                HttpClient client = HttpClient.newHttpClient();
                HttpRequest req = HttpRequest.newBuilder(URI.create(ADDRESS.formatted(LAT, LON))).header("accept", "application/json").build();
                CompletableFuture<HttpResponse<String>> futureResult = client.sendAsync(req, HttpResponse.BodyHandlers.ofString());
                updateData(new JSONObject(futureResult.get().body()));
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
    }

    static class WeatherPanel extends JPanel{
        public WeatherPanel(){
            this.setBackground(Color.BLACK);
            this.setLayout(new GridLayout(3,1));
        }
    }

    static class WeatherLabel extends JLabel{
        public WeatherLabel(){
            this.setForeground(Color.WHITE);
        }

        public WeatherLabel(String text){
            super(text);
            this.setForeground(Color.WHITE);
        }
    }
}
