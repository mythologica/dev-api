package com.devtools.worker.tools;

import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.*;

import java.util.Iterator;
import java.util.Map;

public class ConfigReader {
    private static ConfigReader INSTANCE = new ConfigReader();
    private final static String APP_CONFIG = "custom-app-config";
    private final static String APP_YML_FILE = "classpath:/application.yml"; //"classpath:/custom-config.yaml"
    private final static String LOCAL_CONFIG_NAME_KEY= "local.config.name";
    private final static String LOCAL_CONFIG_PATH_KEY= "local.config.file";
    private String localFullFileName = null;
    private ConfigurableEnvironment appEnv = null;
    private ConfigurableEnvironment localEnv = null;

    private ConfigReader() {
        this.init();
    }

    private Resource getResource(String fullFileName ) {
        Resource resource = null;
        try {
            resource = new DefaultResourceLoader().getResource(fullFileName);
        } catch (Exception ex) {
            // null
        }
        return resource;
    }

    private ConfigurableEnvironment getEnv(String config, String fullFileName) {
        ConfigurableEnvironment env = null;

        Resource resource = getResource(fullFileName);

        if (resource == null || !resource.exists()) {
            // throw new IllegalArgumentException("Resource " + fullFileName + " does not exist");
            System.out.println("Resource " + fullFileName + " does not exist");
            return null;
        }


        ConfigurableApplicationContext ctx = null;
        try {
            PropertySource<?> propertySource = new YamlPropertySourceLoader().load(config, resource).get(0);
            ctx = new GenericXmlApplicationContext();
            env = ctx.getEnvironment(); // ctx.getEnvironment();
            env.getPropertySources().addLast(propertySource);

            // system 정보 
            Map systemInfo = env.getSystemEnvironment();
            Iterator it = systemInfo.keySet().iterator();

            while (it.hasNext()) {
                String key = (String)it.next();
                String value = systemInfo.get(key).toString();
                System.out.println("key:"+key+",value:"+value);
            }

        } catch (Exception ex) {
            System.out.println(new IllegalStateException("Failed to load yaml configuration from " + fullFileName, ex).getLocalizedMessage());
            return null;
        } finally {
            if (ctx != null) {
                ctx.close();
            }
        }
        return env;
    }

    private void init() {
        if (this.appEnv == null) {
            this.appEnv = getEnv(APP_CONFIG, APP_YML_FILE);

            String configName = this.appEnv.getProperty(LOCAL_CONFIG_NAME_KEY , "");
            this.localFullFileName = this.appEnv.getProperty(LOCAL_CONFIG_PATH_KEY);

            this.localEnv = getEnv(configName, this.localFullFileName);
        }
    }

    public static boolean hasConfig() {
        if( INSTANCE.appEnv == null ) {
            INSTANCE.init();
        }
        return INSTANCE.appEnv != null && INSTANCE.localEnv != null;
    }

    public static String getPropertyFromApp(String key , String defaultValue) {
        if( INSTANCE.appEnv == null ) {
            INSTANCE.init();
        }
        if (INSTANCE.appEnv != null && INSTANCE.appEnv.getProperty(key) != null) {
            return INSTANCE.appEnv.getProperty(key , defaultValue);
        }
        return defaultValue;
    }

    public static boolean hasLocalConfig() {
        if( INSTANCE.appEnv == null ) {
            INSTANCE.init();
        }
        boolean isExistFileName = false;
        try {
            isExistFileName = INSTANCE.localFullFileName != null && new DefaultResourceLoader().getResource(INSTANCE.localFullFileName).exists();
        } catch (Exception ex) {
            //file not exists
        }
        return isExistFileName;
    }

    public static String getPropertyFromLocal(String key , String defaultValue) {
        if( INSTANCE.appEnv == null ) {
            INSTANCE.init();
        }
        if (INSTANCE.localEnv != null && INSTANCE.localEnv.getProperty(key) != null) {
            return INSTANCE.localEnv.getProperty(key, defaultValue);
        }
        return defaultValue;
    }
}
