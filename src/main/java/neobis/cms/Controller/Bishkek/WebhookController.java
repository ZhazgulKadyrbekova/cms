package neobis.cms.Controller.Bishkek;

import neobis.cms.Dto.ResponseMessage;
import neobis.cms.Entity.Bishkek.BishClient;
import neobis.cms.Service.Bishkek.BishClientService;
import org.json.JSONObject;
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
            String result = URLDecoder.decode(body, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            // not going to happen - value came from JDK's own StandardCharsets
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
//        URLDecoder.decode( body, "UTF-8" );
        System.out.println(body);
        JSONObject object = new JSONObject(body);
        JSONObject data = (JSONObject) object.get("data");
        BishClient client = clientService.parseJson(data, new BishClient());
        clientService.create(client);
        return new ResponseMessage("Client data has been saved in database.");
    }
}
