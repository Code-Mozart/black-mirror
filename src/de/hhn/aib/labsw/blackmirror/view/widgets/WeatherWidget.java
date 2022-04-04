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
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.concurrent.*;

/**
 * This widget displays the weather information for a given location.
 * The location can be updated with the setGPSLocation method
 */
public class WeatherWidget extends AbstractWidget {
    private static final String ADDRESS = "https://api.open-meteo.com/v1/forecast?latitude=%02.4f&longitude=%02.4f&hourly=temperature_2m,relativehumidity_2m,apparent_temperature,pressure_msl,precipitation,weathercode,windspeed_10m&timezone=Europe/Berlin";
    private static final DateTimeFormatter DATEFORMAT = new DateTimeFormatterBuilder().appendPattern("yyyy-MM-dd'T'HH:mm").parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0).toFormatter();

    ResourceBundle resources = ResourceBundle.getBundle("WeatherWidget", Locale.getDefault());

    //Linked List with the weather sets
    LinkedList<WeatherSet> sets = null;

    //Scheduler runs the data update automatically every 15 Minutes
    ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

    //Location for which the weather data should be downloaded
    private double LAT = 49.066;
    private double LON = 9.220;

    //GUI Items
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
    private WeatherLabel pressure_c;
    private WeatherLabel humidity_c;
    private WeatherLabel windspeed_c;
    private WeatherLabel windspeed_08h;
    private WeatherLabel windspeed_12h;
    private WeatherLabel windspeed_16h;
    private WeatherLabel windspeed_20h;
    private WeatherLabel wc_c;

    //Labels that contain the images
    private JLabel iconPosition_c;
    private JLabel iconPosition_08h;
    private JLabel iconPosition_12h;
    private JLabel iconPosition_16h;
    private JLabel iconPosition_20h;

    public WeatherWidget() {
        initGUI();
        setSize(600,400);
        executor.scheduleAtFixedRate(new WeatherWidget.WeatherReceiver(), 0, 15, TimeUnit.MINUTES);
    }

    /**
     * initializes the GUI
     */
    private void initGUI(){
        backgroundPanel = new JPanel();
        backgroundPanel.setLayout(new GridBagLayout());
        backgroundPanel.setBackground(Color.BLACK);
        add(backgroundPanel);
        WeatherWidgetConstraints wwc = new WeatherWidgetConstraints();
        InnerConstraints ic = new InnerConstraints();

        //Init current weather
        weather_c = new WeatherPanel();
        header_c = new WeatherLabel(resources.getString("header_current"));
        temperature_c = new WeatherLabel();
        humidity_c = new WeatherLabel();
        pressure_c = new WeatherLabel();
        windspeed_c = new WeatherLabel();
        wc_c = new WeatherLabel();
        weather_c.add(header_c,ic);
        ic.gridy++;
        iconPosition_c = new JLabel(new ImageIcon("icons/cloudy.png"));
        weather_c.add(iconPosition_c,ic);
        ic.gridy++;
        weather_c.add(temperature_c,ic);
        ic.gridy++;
        weather_c.add(wc_c,ic);
        ic.gridy++;
        weather_c.add(humidity_c,ic);
        ic.gridy++;
        weather_c.add(pressure_c,ic);
        ic.gridy++;
        weather_c.add(windspeed_c,ic);
        wwc.gridwidth = 4;
        wwc.weightx = 0.0;
        backgroundPanel.add(weather_c,wwc);

        //Init 8h weather
        weather_08h = new WeatherPanel();
        header_08h = new WeatherLabel(resources.getString("header_01"));
        temperature_08h = new WeatherLabel();
        windspeed_08h = new WeatherLabel();
        wc_08h = new WeatherLabel();
        ic.gridy = 0;
        weather_08h.add(header_08h,ic);
        ic.gridy++;
        iconPosition_08h = new JLabel(new ImageIcon("icons/cloudy.png"));
        weather_08h.add(iconPosition_08h,ic);
        ic.gridy++;
        weather_08h.add(temperature_08h,ic);
        ic.gridy++;
        weather_08h.add(wc_08h,ic);
        ic.gridy++;
        weather_08h.add(windspeed_08h,ic);
        wwc = new WeatherWidgetConstraints();
        wwc.gridx = 0;
        wwc.gridy = 3;
        backgroundPanel.add(weather_08h,wwc);

        //Init 12h weather
        weather_12h = new WeatherPanel();
        header_12h = new WeatherLabel(resources.getString("header_02"));
        temperature_12h = new WeatherLabel();
        windspeed_12h = new WeatherLabel();
        wc_12h = new WeatherLabel();
        ic.gridy=0;
        weather_12h.add(header_12h,ic);
        ic.gridy++;
        iconPosition_12h = new JLabel(new ImageIcon("icons/cloudy.png"));
        weather_12h.add(iconPosition_12h,ic);
        ic.gridy++;
        weather_12h.add(temperature_12h,ic);
        ic.gridy++;
        weather_12h.add(wc_12h,ic);
        ic.gridy++;
        weather_12h.add(windspeed_12h,ic);
        wwc = new WeatherWidgetConstraints();
        wwc.gridx = 1;
        wwc.gridy = 3;
        backgroundPanel.add(weather_12h,wwc);

        //Init 16h weather
        weather_16h = new WeatherPanel();
        header_16h = new WeatherLabel(resources.getString("header_03"));
        temperature_16h = new WeatherLabel();
        windspeed_16h = new WeatherLabel();
        wc_16h = new WeatherLabel();
        ic.gridy = 0;
        weather_16h.add(header_16h,ic);
        ic.gridy++;
        iconPosition_16h = new JLabel(new ImageIcon("icons/cloudy.png"));
        weather_16h.add(iconPosition_16h,ic);
        ic.gridy++;
        weather_16h.add(temperature_16h,ic);
        ic.gridy++;
        weather_16h.add(wc_16h,ic);
        ic.gridy++;
        weather_16h.add(windspeed_16h,ic);
        wwc = new WeatherWidgetConstraints();
        wwc.gridx = 2;
        wwc.gridy = 3;
        backgroundPanel.add(weather_16h,wwc);

        //Init 20h weather
        weather_20h = new WeatherPanel();
        header_20h = new WeatherLabel(resources.getString("header_04"));
        temperature_20h = new WeatherLabel();
        windspeed_20h = new WeatherLabel();
        wc_20h = new WeatherLabel();
        ic.gridy = 0;
        weather_20h.add(header_20h,ic);
        ic.gridy++;
        iconPosition_20h = new JLabel(new ImageIcon("icons/cloudy.png"));
        weather_20h.add(iconPosition_20h,ic);
        ic.gridy++;
        weather_20h.add(temperature_20h,ic);
        ic.gridy++;
        weather_20h.add(wc_20h,ic);
        ic.gridy++;
        weather_20h.add(windspeed_20h,ic);
        wwc = new WeatherWidgetConstraints();
        wwc.gridx = 3;
        wwc.gridy = 3;
        backgroundPanel.add(weather_20h,wwc);
    }

    /**
     * set the location for which the weather information should be downloaded
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
        JSONArray windspeeds = data.getJSONObject("hourly").getJSONArray("windspeed_10m");
        JSONArray humidities = data.getJSONObject("hourly").getJSONArray("relativehumidity_2m");
        JSONArray pressures = data.getJSONObject("hourly").getJSONArray("pressure_msl");

        LinkedList<WeatherSet> weatherSets = new LinkedList<>();
        for (int i = 0; i < times.length(); i++) {
            WeatherSet temp = new WeatherSet();
            temp.setTemperature(temperatures.getDouble(i));
            temp.setPrecipitation(precipitations.getDouble(i));
            temp.setWeathercode(weathercodes.getInt(i));
            temp.setWindspeed(windspeeds.getDouble(i));
            temp.setHumidity(humidities.getInt(i));
            temp.setPressure(pressures.getDouble(i));
            LocalDateTime dt = LocalDateTime.parse(times.getString(i), DATEFORMAT);
            temp.setTimestamp(dt);
            weatherSets.add(temp);
        }
        sets = weatherSets;

        //GUI stuff must run on UI thread...
        SwingUtilities.invokeLater(this::updateGUI);
    }

    private void updateGUI() {
        //update current weather
        WeatherSet temp = sets.get(LocalDateTime.now().getHour());
        temperature_c.setText("Temperatur: %01.1f °C".formatted(temp.getTemperature()));
        wc_c.setText(resources.getString(temp.getCodeAsString()));
        pressure_c.setText("Luftdruck: %01.1f hPa".formatted(temp.getPressure()));
        humidity_c.setText("Luftfeuchte: %d %%".formatted(temp.getHumidity()));
        windspeed_c.setText("Windgeschwindigkeit: %01.1f Km/h".formatted(temp.getWindspeed()));
        iconPosition_c.setIcon(temp.getCodeAsIcon());

        //update 08h weather
        temp = sets.get(8);
        temperature_08h.setText("%01.1f °C".formatted(temp.getTemperature()));
        wc_08h.setText(resources.getString(temp.getCodeAsString()));
        iconPosition_08h.setIcon(temp.getCodeAsIcon());
        windspeed_08h.setText("%01.1f Km/h".formatted(temp.getWindspeed()));

        //update 12h weather
        temp = sets.get(12);
        temperature_12h.setText("%01.1f °C".formatted(temp.getTemperature()));
        wc_12h.setText(resources.getString(temp.getCodeAsString()));
        iconPosition_12h.setIcon(temp.getCodeAsIcon());
        windspeed_12h.setText("%01.1f Km/h".formatted(temp.getWindspeed()));

        //update 16h weather
        temp = sets.get(16);
        temperature_16h.setText("%01.1f °C".formatted(temp.getTemperature()));
        wc_16h.setText(resources.getString(temp.getCodeAsString()));
        iconPosition_16h.setIcon(temp.getCodeAsIcon());
        windspeed_16h.setText("%01.1f Km/h".formatted(temp.getWindspeed()));

        //update 20h weather
        temp = sets.get(20);
        temperature_20h.setText("%01.1f °C".formatted(temp.getTemperature()));
        wc_20h.setText(resources.getString(temp.getCodeAsString()));
        iconPosition_20h.setIcon(temp.getCodeAsIcon());
        windspeed_20h.setText("%01.1f Km/h".formatted(temp.getWindspeed()));
    }

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
                updateData(new JSONObject(futureResult.get().body()));
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
    }

    static class WeatherPanel extends JPanel{
        public WeatherPanel(){
            this.setBackground(Color.BLACK);
            this.setLayout(new GridBagLayout());
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

    static class InnerConstraints extends GridBagConstraints{
        public InnerConstraints(){
            super();
            fill = GridBagConstraints.BOTH;
            weightx = 1;
            weighty = 1;
            gridy = 0;
            gridx = 0;
        }
    }

    static class WeatherWidgetConstraints extends GridBagConstraints{
        public WeatherWidgetConstraints(){
            super();
            anchor = GridBagConstraints.CENTER;
            insets = new Insets(0,25,50,25);
        }
    }
}
