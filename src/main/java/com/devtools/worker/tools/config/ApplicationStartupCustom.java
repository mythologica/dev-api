package com.devtools.worker.tools.config;


import com.dodam.builder.util.ConfigReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class ApplicationStartupCustom implements ApplicationListener<ApplicationStartedEvent> {

    @org.springframework.beans.factory.annotation.Value("${spring.config.activate.on-profile}")
    private String onProfile;

    @Value("${site.author-name}")
    private String authorName;

    @Override
    public void onApplicationEvent(ApplicationStartedEvent event) {
        System.out.println("onProfile = " + onProfile);
        System.out.println("password = " + authorName);
        ConfigReader.setOnProfile(onProfile);
    }
}
