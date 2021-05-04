package br.com.chart.enterative.ws;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.context.event.SimpleApplicationEventMulticaster;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * Classe principal do sistema
 *
 * @author Cristhiano Roberto
 *
 */
@SpringBootApplication
@EnableScheduling
@EnableAsync
@EnableJpaRepositories("br.com.chart.enterative.repository")
@EntityScan(value = "br.com.chart.enterative.entity")
@ComponentScan("br.com.chart.enterative")
public class EnterativeWsApplication {

    public static void main(String[] args) {
        SpringApplication.run(EnterativeWsApplication.class, args);
    }

    @Bean(name = "applicationEventMulticaster")
    public ApplicationEventMulticaster simpleApplicationEventMulticaster() {
        SimpleApplicationEventMulticaster eventMulticaster = new SimpleApplicationEventMulticaster();
        eventMulticaster.setTaskExecutor(new SimpleAsyncTaskExecutor());
        return eventMulticaster;
    }
}
