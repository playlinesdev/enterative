package br.com.chart;

import br.com.chart.enterative.enums.ACTIVATION_PROCESS;
import br.com.chart.enterative.vo.ActiveResourceVO;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.context.event.SimpleApplicationEventMulticaster;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

/**
 * Classe principal do sistema
 *
 * @author William Leite
 *
 */
@SpringBootApplication
@EnableScheduling
@EnableAsync
@EnableJpaRepositories("br.com.chart.enterative.repository")
@EntityScan(value = "br.com.chart.enterative.entity")
@ComponentScan("br.com.chart.enterative")
public class BootstrapApplication implements SchedulingConfigurer {

    public static void main(String[] args) {
        BootstrapApplication.activeResources = new ConcurrentHashMap<>();
        
        SpringApplication.run(BootstrapApplication.class, args);
    }

    @Bean(name = "applicationEventMulticaster")
    public ApplicationEventMulticaster simpleApplicationEventMulticaster() {
        SimpleApplicationEventMulticaster eventMulticaster = new SimpleApplicationEventMulticaster();
        eventMulticaster.setTaskExecutor(taskExecutor());
        return eventMulticaster;
    }

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.setScheduler(taskExecutor());
    }

    @Bean
    public Executor taskExecutor() {
        return Executors.newScheduledThreadPool(50);
    }

    // Active Resource
    public static ConcurrentHashMap<ACTIVATION_PROCESS, ActiveResourceVO> activeResources;
    
    public static boolean isActive(ACTIVATION_PROCESS process) {
        ActiveResourceVO resource = BootstrapApplication.activeResources.getOrDefault(process, null);
        return Objects.nonNull(resource) && resource.isActive(process);
    }
}