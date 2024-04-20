package com.example.flowerShop.config;

import com.example.flowerShop.dto.notification.MessageDTO;
import com.example.flowerShop.dto.notification.NotificationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.UUID;

@Component
public class EmailSender {

    @Autowired
    private RabbitSender rabbitSender;

    public void sendEmailToUserAsync(NotificationDTO notificationDTO) {
        try {
            rabbitSender.send(notificationDTO);
        } catch (Exception e) {
            System.out.println("Eroare la trimiterea asincrona a request-ului: " + e.getMessage());
        }
    }

    public void sendEmailToUser(UUID userId, String name, String email) {
        try {
            HttpHeaders headers = new HttpHeaders();
            RestTemplate restTemplate = new RestTemplate();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.setBearerAuth("cata");
            NotificationDTO notificationRequestDto = new NotificationDTO(userId, name, email);
            HttpEntity<NotificationDTO> entity = new HttpEntity<>(notificationRequestDto, headers);
            ResponseEntity<MessageDTO> response = restTemplate.exchange(
                    "http://localhost:8085/send-email",
                    HttpMethod.POST,
                    entity,
                    MessageDTO.class
            );
            if (response.getStatusCode() == HttpStatus.OK) {
                this.sendEmailToUserAsync(notificationRequestDto);
            } else {
                System.out.println("Eroare la trimiterea email");
            }
        } catch (RestClientException e) {
            System.out.println("Eroare la trimiterea request: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Alta eroare " + e.getMessage());
        }
    }
}
