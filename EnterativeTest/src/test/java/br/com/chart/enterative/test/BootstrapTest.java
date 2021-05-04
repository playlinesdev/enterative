package br.com.chart.enterative.test;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 *
 * @author William Leite
 */
@SpringBootApplication
@EnableJpaRepositories("br.com.chart.enterative.repository")
@EntityScan(value = "br.com.chart.enterative.entity")
@ComponentScan("br.com.chart.enterative")
public class BootstrapTest {
}