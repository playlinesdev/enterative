package br.com.enterative.demo.service;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

import static org.junit.Assert.*;
@Component
public class FluentServiceTest {

    @Autowired
    private Configuration config;

    @Autowired
    private FluentService fluentService;

    @Test
    public void send() {

        String endpoint = config.getUrlActivation();
        String content = "";//JSON

        UUID uuid = UUID.randomUUID();
        String token = uuid.toString();

        String login = "HOMOLOGACAO";

        String result = fluentService.send(endpoint, content, token, login);

    }
}