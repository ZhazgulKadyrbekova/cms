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

@CrossOrigin
@RestController
@RequestMapping("/webhook")
public class WebhookController {
    @Autowired private BishClientService clientService;

    @PostMapping
    public ResponseMessage addClientToDB(HttpEntity<String> httpEntity) {
        String body = httpEntity.getBody();
        JSONObject object = new JSONObject(body);
        JSONObject data = (JSONObject) object.get("data");
        BishClient client = clientService.parseJson(data, new BishClient());
        clientService.create(client);
        return new ResponseMessage("Client data has been saved in database.");
    }
}
