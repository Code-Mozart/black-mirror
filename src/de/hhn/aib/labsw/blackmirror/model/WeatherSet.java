package de.hhn.aib.labsw.blackmirror.model;
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
    private double precipitation;

    // HashMap to convert weather codes to string
    private static final HashMap<Integer,String> codeMap = new HashMap<>();
    static{
        codeMap.put(0,"clear");
        codeMap.put(1,"mainly clear");
        codeMap.put(2,"partly clear");
        codeMap.put(3,"overcast");
        codeMap.put(45,"fog");
        codeMap.put(48,"depositing rime fog");
        codeMap.put(51,"light drizzle");
        codeMap.put(53,"moderate drizzle");
        codeMap.put(55,"dense drizzle");
        codeMap.put(56,"light freezing drizzle");
        codeMap.put(57,"dense freezing drizzle");
        codeMap.put(61,"light rain");
        codeMap.put(63,"moderate rain");
        codeMap.put(65,"heavy rain");
        codeMap.put(66,"light freezing rain");
        codeMap.put(67,"heavy freezing rain");
        codeMap.put(71,"slight snow fall");
        codeMap.put(73,"moderate snow fall");
        codeMap.put(75,"heavy snow fall");
        codeMap.put(77,"snow grains");
        codeMap.put(80,"slight rain showers");
        codeMap.put(81,"moderate rain showers");
        codeMap.put(82,"violent rain showers");
        codeMap.put(85,"slight snow showers");
        codeMap.put(86,"heavy snow showers");
        codeMap.put(95,"thunderstorm");
        codeMap.put(96,"thunderstorm with slight hail");
        codeMap.put(99,"thunderstorm with heavy hail");
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

    /**
     * get the timestamp of the weather set
     * @return the timestamp as LocalDateTime
     */
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    /**
     * get the weather code of the weather set
     * @return the weather code
     */
    public int getWeathercode() {
        return weathercode;
    }

    /**
     * get the temperature of the weather set
     * @return the temperature
     */
    public double getTemperature() {
        return temperature;
    }

    /**
     * get the precipitation of the weather set
     * @return the precipitation
     */
    public double getPrecipitation() {
        return precipitation;
    }

    /**
     * set the timestamp of the weather set
     * @param timestamp the timestamp
     */
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * set the weathercode of the weather set
     * @param weathercode the weathercode
     */
    public void setWeathercode(int weathercode) {
        this.weathercode = weathercode;
    }

    /**
     * set the Temperature of the weather set
     * @param temperature the temperature
     */
    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    /**
     * set the precipitation of the weather set
     * @param precipitation the precipitation
     */
    public void setPrecipitation(double precipitation) {
        this.precipitation = precipitation;
    }

    /**
     * convert the weather set to a string
     * @return string of the weather set
     */
    public String toString(){
        return "%s: %s, %f °C, Niederschlag %s mm/h".formatted(timestamp.toString(), getCodeAsString(),temperature,precipitation);
    }
}
