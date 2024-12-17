package app;

import app.config.ApplicationConfig;

public class Main {
    public static void main(String[] args) {
        ApplicationConfig.startServer(7074);
//        Populate.runIfEmpty(); // only for test purposes
    }
}
