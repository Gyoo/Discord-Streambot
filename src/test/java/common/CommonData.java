package common;

public class CommonData {

    public static final String URL = PropertiesReader.getInstance().getProp().getProperty("connection.url");
    public static final String user = PropertiesReader.getInstance().getProp().getProperty("connection.user");
    public static final String password = PropertiesReader.getInstance().getProp().getProperty("connection.password");

}
