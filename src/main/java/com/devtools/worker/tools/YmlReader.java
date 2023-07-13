package com.devtools.worker.tools;

import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.*;

import java.util.ArrayList;
import java.util.Map;
import java.util.LinkedHashMap;

public class YmlReader {
    private final static String LOCAL_CONFIG= "custom-local-config";
    private final static String LOCAL_YML_FILE = "file:/data/workspace/was/config/local.yml";

    private final static String APP_CONFIG= "custom-app-config";
    private final static String APP_YML_FILE = "classpath:/application.yml"; //"classpath:/custom-config.yaml"

    private static ConfigurableEnvironment appEnv = null;
    private static ConfigurableEnvironment localEnv = null;

    private static ConfigurableEnvironment getEnv(String config, String fullFileName) {
        ConfigurableEnvironment env = null;
        Resource resource = new DefaultResourceLoader().getResource(fullFileName);

        if (!resource.exists()) {
            throw new IllegalArgumentException("Resource " + fullFileName + " does not exist");
        }

        ConfigurableApplicationContext ctx = null;
        try {
            PropertySource<?> propertySource = new YamlPropertySourceLoader().load(config, resource).get(0);
            ctx = new GenericXmlApplicationContext();
            env = ctx.getEnvironment(); // ctx.getEnvironment();
            env.getPropertySources().addLast(propertySource);
        }
        catch (Exception ex) {
            throw new IllegalStateException(
                    "Failed to load yaml configuration from " + fullFileName, ex);
        } finally {
            if( ctx != null ) {
                ctx.close();
            }
        }
        return env;
    }

    public static String getPropertyFromApp(String key) {
        if( appEnv == null ) {
            appEnv = getEnv(APP_CONFIG, APP_YML_FILE);
        }
        if( appEnv != null && appEnv.getProperty(key) != null) {
            return appEnv.getProperty(key);
        }
        return "";
    }

    public static boolean isLocal() {
        try {
            return new FileSystemResource(LOCAL_YML_FILE).exists();
        }catch(Exception ex) {
            return false;
        }
    }

    public static String getPropertyFromLocal(String key) {
        if( localEnv == null ) {
            localEnv = getEnv(LOCAL_CONFIG, LOCAL_YML_FILE);
        }
        if( localEnv != null && localEnv.getProperty(key) != null) {
            return localEnv.getProperty(key);
        }
        return "";
    }

}
