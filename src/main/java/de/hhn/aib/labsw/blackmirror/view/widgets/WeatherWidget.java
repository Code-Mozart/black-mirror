package de.hhn.aib.labsw.blackmirror.view.widgets;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import de.hhn.aib.labsw.blackmirror.model.ApiDataModels.Location;
import de.hhn.aib.labsw.blackmirror.model.WeatherSet;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * This widget displays the weather information for a given location.
 * The location can be updated with the setGPSLocation method
 */
public class WeatherWidget extends AbstractWidget {
    ResourceBundle resources = ResourceBundle.getBundle("lang/WeatherWidget", Locale.getDefault());

    //Linked List with the weather sets
    List<WeatherSet> weatherSets = null;

    private final WeatherPanel[] weatherPanels = new WeatherPanel[5];

    //Static icons
    private static final ImageIcon clearIcon = new ImageIcon(WeatherSet.class.getResource("/icons/clear.png"));
    private static final ImageIcon msunnyIcon = new ImageIcon(WeatherSet.class.getResource("/icons/mostlysunny.png"));
    private static final ImageIcon mcloudyIcon = new ImageIcon(WeatherSet.class.getResource("/icons/mostlycloudy.png"));
    private static final ImageIcon cloudyIcon = new ImageIcon(WeatherSet.class.getResource("/icons/cloudy.png"));
    private static final ImageIcon fogIcon = new ImageIcon(WeatherSet.class.getResource("/icons/fog.png"));
    private static final ImageIcon drizzleIcon = new ImageIcon(WeatherSet.class.getResource("/icons/chancerain.png"));
    private static final ImageIcon rainIcon = new ImageIcon(WeatherSet.class.getResource("/icons/rain.png"));
    private static final ImageIcon hailIcon = new ImageIcon(WeatherSet.class.getResource("/icons/flurries.png"));
    private static final ImageIcon snowIcon = new ImageIcon(WeatherSet.class.getResource("/icons/chancesnow.png"));
    private static final ImageIcon sleetIcon = new ImageIcon(WeatherSet.class.getResource("/icons/chancesleet.png"));
    private static final ImageIcon stormIcon = new ImageIcon(WeatherSet.class.getResource("/icons/tstorms.png"));

    private static final HashMap<Integer, Icon> iconMap = new HashMap<>();
    static{
        iconMap.put(0,clearIcon);
        iconMap.put(1,msunnyIcon);
        iconMap.put(2,mcloudyIcon);
        iconMap.put(3,cloudyIcon);
        iconMap.put(45,fogIcon);
        iconMap.put(48,fogIcon);
        iconMap.put(51,drizzleIcon);
        iconMap.put(53,drizzleIcon);
        iconMap.put(55,drizzleIcon);
        iconMap.put(56,drizzleIcon);
        iconMap.put(57,drizzleIcon);
        iconMap.put(61,drizzleIcon);
        iconMap.put(63,rainIcon);
        iconMap.put(65,rainIcon);
        iconMap.put(66,drizzleIcon);
        iconMap.put(67,rainIcon);
        iconMap.put(71,snowIcon);
        iconMap.put(73,snowIcon);
        iconMap.put(75,snowIcon);
        iconMap.put(77,hailIcon);
        iconMap.put(80,drizzleIcon);
        iconMap.put(81,rainIcon);
        iconMap.put(82,rainIcon);
        iconMap.put(85,snowIcon);
        iconMap.put(86,snowIcon);
        iconMap.put(95,stormIcon);
        iconMap.put(96,stormIcon);
        iconMap.put(99,stormIcon);
    }

    /**
     * Convert a given weather code to the relevant icon
     * @param code code to be converted
     * @return weather code as icon
     */
    public static Icon convertCodeToIcon(int code){
        return iconMap.get(code);
    }

    public WeatherWidget() {
        initGUI();
        setSize(600,400);
        updateExecutor.scheduleAtFixedRate(new WeatherWidget.WeatherReceiver(), 0, 15, TimeUnit.MINUTES);
        this.subscribe("location", this);
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
     * updates the gui to display the new data
     */
    public void updateGUI(List<WeatherSet> weatherSets) {
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
            icon.setIcon(convertCodeToIcon(set.getWeathercode()));
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
