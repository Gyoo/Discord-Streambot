package common;

public class CommonData {

    public static final String URL = PropertiesReader.getInstance().getProp().getProperty("hibernate.connection.url.test");
    public static final String user = PropertiesReader.getInstance().getProp().getProperty("hibernate.connection.username");
    public static final String password = PropertiesReader.getInstance().getProp().getProperty("hibernate.connection.password");

    public static final String userID = "63263941735755776";

}
