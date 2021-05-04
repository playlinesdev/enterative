package br.com.chart.enterative.mock.server;

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
 * @author William Leite
 *
 */
@SpringBootApplication
@EnableScheduling
@EnableAsync
@EnableJpaRepositories("br.com.chart.enterative.mock.server.dao.repository")
@EntityScan(value = "br.com.chart.enterative.mock.server.entity")
@ComponentScan("br.com.chart.enterative.mock.server")
public class EnterativeMockServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(EnterativeMockServerApplication.class, args);
    }

    @Bean(name = "applicationEventMulticaster")
    public ApplicationEventMulticaster simpleApplicationEventMulticaster() {
        SimpleApplicationEventMulticaster eventMulticaster = new SimpleApplicationEventMulticaster();
        eventMulticaster.setTaskExecutor(new SimpleAsyncTaskExecutor());
        return eventMulticaster;
    }
}
