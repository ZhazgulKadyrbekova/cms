package neobis.cms.Controller.Bishkek;

import neobis.cms.Dto.ResponseMessage;
import neobis.cms.Entity.Bishkek.BishClient;
import neobis.cms.Service.Bishkek.BishClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

@CrossOrigin
@RestController
@RequestMapping("/webhook")
public class WebhookController {
    @Autowired private BishClientService clientService;

    @PostMapping
    public ResponseMessage addClientToDB(HttpEntity<String> httpEntity) {
        String body = httpEntity.getBody();
        try {
            body = URLDecoder.decode(body, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
        BishClient client = clientService.parseClient(body);
        clientService.create(client);
        return new ResponseMessage("Client data has been saved in database.");
    }
}
