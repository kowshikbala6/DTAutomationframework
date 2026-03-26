package utils;

import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {

    private static Properties prop = new Properties();

    static {

        try {

            InputStream is = ConfigReader.class
                    .getClassLoader()
                    .getResourceAsStream("config/config.properties");

            if (is == null) {
                throw new RuntimeException("config.properties NOT found in classpath");
            }

            prop.load(is);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static String get(String key) {
        return prop.getProperty(key);
    }

}