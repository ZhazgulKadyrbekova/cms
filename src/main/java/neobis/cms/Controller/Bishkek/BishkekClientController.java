package neobis.cms.Controller.Bishkek;

import neobis.cms.Dto.ClientDTO;
import neobis.cms.Entity.Bishkek.BishClient;
import neobis.cms.Service.Bishkek.BishClientService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@CrossOrigin
@RestController
@RequestMapping("/bishkek/client")
public class BishkekClientController {

    private final Logger log = LogManager.getLogger();

    @Autowired
    private BishClientService clientService;

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
        log.info("In Bishkek created new client {}", clientDTO.toString());
        return clientService.create(clientDTO);
    }

}
