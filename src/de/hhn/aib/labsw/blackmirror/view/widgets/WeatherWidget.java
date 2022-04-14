package de.hhn.aib.labsw.blackmirror.view.widgets;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import de.hhn.aib.labsw.blackmirror.model.WeatherSet;

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
import java.util.ArrayList;
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
    ArrayList<WeatherSet> weatherSets = null;

    //Scheduler runs the data update automatically every 15 Minutes
    ScheduledExecutorService updateExecutor = Executors.newSingleThreadScheduledExecutor();

    //Location for which the weather data should be downloaded
    private double LAT = 49.066;
    private double LON = 9.220;

    private final WeatherPanel[] weatherPanels = new WeatherPanel[5];

    public WeatherWidget() {
        initGUI();
        setSize(600,400);
        updateExecutor.scheduleAtFixedRate(new WeatherWidget.WeatherReceiver(), 0, 15, TimeUnit.MINUTES);
    }

    /**
     * initializes the GUI
     */
    private void initGUI(){
        //GUI Items
        JPanel backgroundPanel = new JPanel();
        backgroundPanel.setLayout(new GridBagLayout());
        backgroundPanel.setBackground(Color.BLACK);
        add(backgroundPanel);

        for(int i = 0; i<weatherPanels.length;i++){
            weatherPanels[i] = new WeatherPanel();
            WeatherWidgetConstraints wwc = new WeatherWidgetConstraints();
            if(i == 0){
                wwc.gridy = 0;
                wwc.gridx = 0;
                wwc.gridwidth = weatherPanels.length-1;
                wwc.weightx = 0.0;
                backgroundPanel.add(weatherPanels[i],wwc);
            }
            else{
                wwc.gridy = 1;
                wwc.gridx = i-1;
                backgroundPanel.add(weatherPanels[i],wwc);
            }
        }
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

        ArrayList<WeatherSet> weatherSets = new ArrayList<>();
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
        this.weatherSets = weatherSets;

        //GUI stuff must run on UI thread...
        SwingUtilities.invokeLater(this::updateGUI);
    }

    /**
     * updates the gui to display the new data
     */
    private void updateGUI() {
        for(int i = 0;i<weatherPanels.length;i++){
            switch (i) {
                case 0 -> weatherPanels[i].setData(resources.getString("header_current"), weatherSets.get(LocalDateTime.now().getHour()));
                case 1 -> weatherPanels[i].setData(resources.getString("header_01"), weatherSets.get(8));
                case 2 -> weatherPanels[i].setData(resources.getString("header_02"), weatherSets.get(12));
                case 3 -> weatherPanels[i].setData(resources.getString("header_03"), weatherSets.get(16));
                case 4 -> weatherPanels[i].setData(resources.getString("header_04"), weatherSets.get(20));
                default -> weatherPanels[i].setData("no header", weatherSets.get(32));
            }
        }
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
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            } catch (JsonMappingException e) {
                e.printStackTrace();
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * creates a single panel that holds the information for one time
     */
    static class WeatherPanel extends JPanel{
        WeatherLabel header;
        WeatherLabel icon;
        WeatherLabel temperature;
        WeatherLabel pressure;
        WeatherLabel wind;
        WeatherLabel humidity;

        public WeatherPanel(){
            this.setBackground(Color.BLACK);
            this.setLayout(new GridBagLayout());
            GridBagConstraints ic = new InnerConstraints();
            header = new WeatherLabel();
            icon = new WeatherLabel();
            temperature = new WeatherLabel();
            wind = new WeatherLabel();
            pressure = new WeatherLabel();
            humidity = new WeatherLabel();
            this.add(header,ic);
            ic.gridy++;
            this.add(icon,ic);
            ic.gridy++;
            this.add(temperature,ic);
            ic.gridy++;
            this.add(pressure,ic);
            ic.gridy++;
            this.add(humidity,ic);
            ic.gridy++;
            this.add(wind,ic);
        }

        public void setData(String headerText, WeatherSet set){
            header.setText(headerText);
            icon.setIcon(set.getCodeAsIcon());
            temperature.setText("%01.1f °C".formatted(set.getTemperature()));
            wind.setText("%01.1f Km/h".formatted(set.getWindspeed()));
            pressure.setText("%04.1f hPa".formatted(set.getPressure()));
            humidity.setText("%d %%".formatted(set.getHumidity()));
        }
    }

    /**
     * special label that already has a white text color.
     */
    static class WeatherLabel extends JLabel{
        public WeatherLabel(){
            this.setForeground(Color.WHITE);
        }

        public WeatherLabel(String text){
            super(text);
            this.setForeground(Color.WHITE);
        }
    }

    /**
     * special constraint to be used inside the weather containers
     */
    static class InnerConstraints extends GridBagConstraints{
        public InnerConstraints(){
            super();
            fill = GridBagConstraints.BOTH;
            anchor = GridBagConstraints.CENTER;
            weightx = 1;
            weighty = 1;
            gridy = 0;
            gridx = 0;
        }
    }

    /**
     * special constraints that is used to place the weather containers
     */
    static class WeatherWidgetConstraints extends GridBagConstraints{
        public WeatherWidgetConstraints(){
            super();
            anchor = GridBagConstraints.CENTER;
            insets = new Insets(0,25,50,25);
        }
    }
}
