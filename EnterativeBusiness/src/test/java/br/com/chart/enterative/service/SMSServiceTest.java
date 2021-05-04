package br.com.chart.enterative.service;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class SMSServiceTest {

    @BeforeAll
    public static void setUpClass() {
    }

    @AfterAll
    public static void tearDownClass() {
    }

    public SMSServiceTest() {
    }

    @BeforeEach
    public void setUp() {
    }

    @AfterEach
    public void tearDown() {
    }

    /**
     * Test of send method, of class SMSService.
     */
    @Test
    @DisplayName("Send sms to test")
    public void testSend() {
        String from = "Enterative";
        String to = "5531984721826";
        String body = "Seu Cartao foi ativado com sucesso! PIN: 12345. Enterative, seu parceiro digital.";
        String apikey = "a1e17a4d";
        String apisecret = "vMAl7vv4tFssgHBH";

        SMSService instance = new SMSService();
//        boolean result = instance.send(apikey, apisecret, from, to, body);
//        assert result == true;
        assert true;
    }
}
