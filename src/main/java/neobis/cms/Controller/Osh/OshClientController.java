package neobis.cms.Controller.Osh;

import lombok.extern.log4j.Log4j2;
import neobis.cms.Dto.ClientDTO;
import neobis.cms.Entity.Bishkek.BishClient;
import neobis.cms.Entity.Osh.OshClient;
import neobis.cms.Service.Bishkek.BishClientService;
import neobis.cms.Service.Osh.OshClientService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/osh/client")
public class OshClientController {

    private final Logger log = LogManager.getLogger();

    @Autowired
    private OshClientService clientService;

//    @GetMapping(consumes = "application/json", produces = "application/json")
//    public String getClients(@RequestParam(name = "domain", defaultValue = "neolabs.dev", required = false) String domain,
//                               @RequestParam(name = "api_key", defaultValue = "e539509b630b27e47ac594d0dbba4e69", required = false) String api_key,
//                               @RequestParam(name = "status", required = false) Integer status,
//                               @RequestParam(name = "date_from", required = false) Timestamp date_from,
//                               @RequestParam(name = "date_to", required = false) Timestamp date_to,
//                               @RequestParam(name = "client_phone", required = false) String phone,
//                               @RequestParam(name = "client_email", required = false) String email,
//                               @RequestParam(name = "start", required = false) Integer start,
//                               @RequestParam(name = "count", required = false) Integer count) {
//        clientService.addClientsToDB();
//    }

    @GetMapping("/getAll")
    public List<OshClient> getAll() {
        return clientService.getAllClientsFromDB();
    }

    @GetMapping("/getByStatus/{status}")
    public List<OshClient> getAllByStatus(@PathVariable String status) {
        clientService.addClientsToDB();
        return clientService.getAllByStatus(status);
    }

    @PostMapping("/create")
    public OshClient addClient(@RequestBody ClientDTO clientDTO) {
        log.info("In Bishkek created new client {}", clientDTO.toString());
        return clientService.create(clientDTO);
    }

}
