package de.hhn.aib.labsw.blackmirror.model;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.util.HashMap;

/**
 * Weather-Sets contain weather information on a specific date.
 * Version 1.0
 * Luis Gutzeit 24.03.2022
 * last change 25.03.2022
 */
public class WeatherSet {
    private LocalDateTime timestamp;
    private int weathercode;
    private double temperature;
    private double pressure;
    private int humidity;
    private double windspeed;
    private double precipitation;

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

    // HashMap to convert weather codes to string
    private static final HashMap<Integer,String> codeMap = new HashMap<>();
    private static final HashMap<Integer, Icon> iconMap = new HashMap<>();
    static{
        codeMap.put(0,"wc_clear");
        codeMap.put(1,"wc_mainly_clear");
        codeMap.put(2,"wc_partly_clear");
        codeMap.put(3,"wc_overcast");
        codeMap.put(45,"wc_fog");
        codeMap.put(48,"wc_depositing_rime_fog");
        codeMap.put(51,"wc_light_drizzle");
        codeMap.put(53,"wc_moderate_drizzle");
        codeMap.put(55,"wc_dense_drizzle");
        codeMap.put(56,"wc_light_freezing_drizzle");
        codeMap.put(57,"wc_dense_freezing_drizzle");
        codeMap.put(61,"wc_light_rain");
        codeMap.put(63,"wc_moderate_rain");
        codeMap.put(65,"wc_heavy_rain");
        codeMap.put(66,"wc_light_freezing_rain");
        codeMap.put(67,"wc_heavy_freezing_rain");
        codeMap.put(71,"wc_slight_snow_fall");
        codeMap.put(73,"wc_moderate_snow_fall");
        codeMap.put(75,"wc_heavy_snow_fall");
        codeMap.put(77,"wc_snow_grains");
        codeMap.put(80,"wc_slight_rain_showers");
        codeMap.put(81,"wc_moderate_rain_showers");
        codeMap.put(82,"wc_violent_rain_showers");
        codeMap.put(85,"wc_slight_snow_showers");
        codeMap.put(86,"wc_heavy_snow_showers");
        codeMap.put(95,"wc_thunderstorm");
        codeMap.put(96,"wc_thunderstorm_with_slight_hail");
        codeMap.put(99,"wc_thunderstorm_with_heavy_hail");

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

    /**
     * convert the weather code of the weather set to Icon
     * @return weather code as icon
     */
    public Icon getCodeAsIcon(){
        return iconMap.get(weathercode);
    }

    /**
     * Convert a given weather code to the relevant string
     * @param code code to be converted
     * @return weather code as string
     */
    public static String convertCodeToString(int code){
        return codeMap.get(code);
    }

    /**
     * convert the weather code of the weather set to string
     * @return weather code as string
     */
    public String getCodeAsString(){
        return codeMap.get(weathercode);
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public int getWeathercode() {
        return weathercode;
    }

    public double getTemperature() {
        return temperature;
    }

    public double getPrecipitation() {
        return precipitation;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public void setWeathercode(int weathercode) {
        this.weathercode = weathercode;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public void setPrecipitation(double precipitation) {
        this.precipitation = precipitation;
    }

    public double getPressure() {
        return pressure;
    }

    public void setPressure(double pressure) {
        this.pressure = pressure;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public double getWindspeed() {
        return windspeed;
    }

    public void setWindspeed(double windspeed) {
        this.windspeed = windspeed;
    }

    /**
     * convert the weather set to a string
     * @return string of the weather set
     */
    public String toString(){
        return "%s: %s, %f °C, Niederschlag %s mm/h".formatted(timestamp.toString(), getCodeAsString(),temperature,precipitation);
    }
}
