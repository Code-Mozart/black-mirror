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

public class WeatherWidget extends AbstractWidget{
    private static final String ADDRESS = "https://api.open-meteo.com/v1/forecast?latitude=%s&longitude=%s&hourly=temperature_2m,pressure_msl,precipitation,weathercode&current_weather=true&timezone=Europe/Berlin";
    private static final DateTimeFormatter DATEFORMAT = new DateTimeFormatterBuilder().appendPattern("yyyy-MM-dd'T'HH:mm").parseDefaulting(ChronoField.SECOND_OF_MINUTE,0).toFormatter();

    //Linked List with the weather sets
    LinkedList<WeatherSet> sets = null;

    //Scheduler runs the data update automatically every 15 Minutes
    ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

    //Location for which the weather data should be downloaded
    private String LAT = "49.066";
    private String LON = "9.220";

    //GUI elements
    private JLabel temp1,temp2,temp3,temp4,temp5;
    private JLabel wc1,wc2,wc3,wc4,wc5;
    private JPanel p1,p2,p3,p4,p5;
    private JPanel backgroundPanel;

    public WeatherWidget(){
        this.setSize(400, 400);
        executor.scheduleAtFixedRate(new WeatherReceiver(),0,15, TimeUnit.MINUTES);
        initGUI();
    }

    private void initGUI(){
        //Create Background Panel
        backgroundPanel = new JPanel();
        backgroundPanel.setBackground(Color.BLACK);
        backgroundPanel.setLayout(new GridBagLayout());
        this.add(backgroundPanel);

        //Panel for current weather, top in the middle
        GridBagConstraints gbc = new GridBagConstraints();
        GridLayout gl = new GridLayout(2,1);
        p1 = new JPanel();
        p1.setLayout(gl);
        gbc.gridwidth=4;
        gbc.gridx=0;
        gbc.gridy=0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.BOTH;
        backgroundPanel.add(p1,gbc);
        //Panel for 8:00 weather, bottom 1
        p2 = new JPanel();
        gl = new GridLayout(2,1);
        p2.setLayout(gl);
        gbc = new GridBagConstraints();
        gbc.gridx=0;
        gbc.gridy=1;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.BOTH;
        backgroundPanel.add(p2,gbc);
        //Panel for 12:00 weather, bottom 2
        p3 = new JPanel();
        gl = new GridLayout(2,1);
        p3.setLayout(gl);
        gbc = new GridBagConstraints();
        gbc.gridx=1;
        gbc.gridy=1;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.BOTH;
        backgroundPanel.add(p3,gbc);
        //Panel for 16:00 weather, bottom 3
        p4 = new JPanel();
        gl = new GridLayout(2,1);
        p4.setLayout(gl);
        gbc = new GridBagConstraints();
        gbc.gridx=2;
        gbc.gridy=1;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.BOTH;
        backgroundPanel.add(p4,gbc);
        //Panel for 20:00 weather, bottom 4
        p5 = new JPanel();
        gl = new GridLayout(2,1);
        p5.setLayout(gl);
        gbc = new GridBagConstraints();
        gbc.gridx=3;
        gbc.gridy=1;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.BOTH;
        backgroundPanel.add(p5,gbc);

        //add temperature labels;
        temp1 = new JLabel("50°C");
        p1.add(temp1);
        temp2 = new JLabel("50°C");
        p2.add(temp2);
        temp3 = new JLabel("50°C");
        p3.add(temp3);
        temp4 = new JLabel("50°C");
        p4.add(temp4);
        temp5 = new JLabel("50°C");
        p5.add(temp5);
    }

    /**
     * set the location for which the weather information should be downloaded
     */
    public void setLocation(String lat, String lon){
        this.LAT = lat;
        this.LON = lon;
    }

    /**
     * converts the given JSON Object to a List of weather sets
     * @param data the weather data received as JSON
     */
    private void updateData(JSONObject data){
        JSONArray times = data.getJSONObject("hourly").getJSONArray("time");
        JSONArray weathercodes = data.getJSONObject("hourly").getJSONArray("weathercode");
        JSONArray temperatures = data.getJSONObject("hourly").getJSONArray("temperature_2m");
        JSONArray precipitations = data.getJSONObject("hourly").getJSONArray("precipitation");

        LinkedList<WeatherSet> weatherSets = new LinkedList<>();
        for (int i = 0; i<times.length();i++){
            WeatherSet temp = new WeatherSet();
            temp.setTemperature(temperatures.getDouble(i));
            temp.setPrecipitation(precipitations.getDouble(i));
            temp.setWeathercode(weathercodes.getInt(i));
            LocalDateTime dt = LocalDateTime.parse(times.getString(i),DATEFORMAT);
            temp.setTimestamp(dt);
            weatherSets.add(temp);
        }
        sets = weatherSets;
        updateGUI();
    }

    private void updateGUI(){

    }

    class WeatherReceiver extends Thread {
        /**
         * connect to the server and download the json file.
         */
        @Override
        public void run() {
            try {
                HttpClient client = HttpClient.newHttpClient();
                HttpRequest req = HttpRequest.newBuilder(URI.create(ADDRESS.formatted(LAT,LON))).header("accept","application/json").build();
                CompletableFuture<HttpResponse<String>> futureResult = client.sendAsync(req, HttpResponse.BodyHandlers.ofString());
                updateData(new JSONObject(futureResult.get().body()));
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
    }
}
