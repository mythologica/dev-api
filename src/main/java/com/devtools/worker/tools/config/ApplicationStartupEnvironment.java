package com.devtools.worker.tools.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class ApplicationStartupEnvironment implements ApplicationListener<ApplicationStartedEvent> {

    private final Environment env;

    @Autowired
    public ApplicationStartupEnvironment(Environment env) {
        this.env = env;
    }

    @Override
    public void onApplicationEvent(ApplicationStartedEvent event) {
        String username = env.getProperty("site.author-name");
        String password = env.getProperty("site.author-email");
        System.out.println("author-name = " + username);
        System.out.println("author-email = " + password);
    }
}
