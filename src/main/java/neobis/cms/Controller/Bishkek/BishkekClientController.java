package neobis.cms.Controller.Bishkek;

import neobis.cms.Dto.ClientDTO;
import neobis.cms.Entity.Bishkek.BishClient;
import neobis.cms.Service.Bishkek.BishClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@CrossOrigin
@RestController
@RequestMapping("/bishkek/client")
public class BishkekClientController {

    @Autowired
    private BishClientService clientService;

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
    public List<BishClient> getAll() {
        return clientService.getAllClientsFromDB();
    }

    @GetMapping("/getByStatus/{status}")
    public List<BishClient> getAllByStatus(@PathVariable String status) {
        clientService.addClientsToDB();
        return clientService.getAllByStatus(status);
    }

    @PostMapping("/create")
    public BishClient addClient(@RequestBody ClientDTO clientDTO) {
        return clientService.create(clientDTO);
    }

}
