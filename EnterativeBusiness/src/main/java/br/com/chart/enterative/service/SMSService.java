package br.com.chart.enterative.service;

import com.nexmo.client.NexmoClient;
import com.nexmo.client.sms.MessageStatus;
import com.nexmo.client.sms.SmsSubmissionResponse;
import com.nexmo.client.sms.messages.TextMessage;
import java.util.concurrent.atomic.AtomicBoolean;
import org.springframework.stereotype.Service;

/**
 *
 * @author William Leite
 */
@Service
public class SMSService {

    public boolean send(final String apiKey, final String apiSecret, final String from, final String to, final String body) {
        NexmoClient client = NexmoClient.builder().apiKey(apiKey).apiSecret(apiSecret).build();
        TextMessage message = new TextMessage(from, to, body);
        
        StringBuilder result = new StringBuilder(0);
        AtomicBoolean done = new AtomicBoolean(true);
        
        try {
            SmsSubmissionResponse response = client.getSmsClient().submitMessage(message);
            response.getMessages().stream().forEach(m -> {
                if (m.getStatus() != MessageStatus.OK) {
                    done.set(false);
                }
                result.append(m.toString()).append("\n");
            });
        } catch (Exception e) {
            done.set(false);
            e.printStackTrace();
        }
        
        System.out.println("SMS: " + result.toString());
        
        return done.get();
    }
}
