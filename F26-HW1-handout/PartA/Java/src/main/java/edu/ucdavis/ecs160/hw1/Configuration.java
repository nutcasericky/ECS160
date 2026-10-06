package edu.ucdavis.ecs160.hw1;

public class Configuration {

    private String appName;
    private String logLevel;
    private int maxConnections;
    private boolean debugMode;

    private static final Configuration instance = new Configuration();

    private Configuration(){
        appName = "ECS160-HW1";
        logLevel = "INFO";
        maxConnections = 32;
        debugMode = true;
    }
    
    public static Configuration getInstance(){
        return instance;
    }
}
