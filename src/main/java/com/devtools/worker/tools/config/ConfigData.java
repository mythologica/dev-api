package com.devtools.worker.tools.config;

//import com.dodam.builder.util.ConfigReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ConfigData {
    @Value("${spring.config.activate.on-profile}")
    private String onProfile;

    public void reload() {
        // ConfigReader.setOnProfile(this.onProfile);
    }
}
