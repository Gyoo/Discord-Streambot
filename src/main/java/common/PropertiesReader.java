package common;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesReader {
    private static PropertiesReader ourInstance = new PropertiesReader();

    private Properties prop;

    public static PropertiesReader getInstance() {
        return ourInstance;
    }

    private PropertiesReader(){
        InputStream inputStream = null;
        try {
            prop = new Properties();
            String propFileName = "config.properties";

            inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);

            if (inputStream != null) {
                prop.load(inputStream);
            } else {
                throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
            }
        } catch (Exception e) {
            Logger.writeToErr(e, "");
        } finally {
            try {
                assert inputStream != null;
                inputStream.close();
            } catch (IOException e) {
                Logger.writeToErr(e, "");
            }
        }
    }

    public Properties getProp() {
        return prop;
    }

    public void setProp(Properties prop) {
        this.prop = prop;
    }
}
