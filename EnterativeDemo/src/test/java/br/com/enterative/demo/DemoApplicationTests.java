package br.com.enterative.demo;

import br.com.enterative.demo.service.Configuration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@EnableConfigurationProperties(Configuration.class)
public class DemoApplicationTests {

	@Test
	public void contextLoads() {
	}

}
