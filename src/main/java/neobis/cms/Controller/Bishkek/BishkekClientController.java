package neobis.cms.Controller.Bishkek;

import neobis.cms.Dto.ClientDTO;
import neobis.cms.Entity.Bishkek.BishClient;
import neobis.cms.Service.Bishkek.BishClientService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.*;

@CrossOrigin
@RestController
@RequestMapping("/bishkek/client")
public class BishkekClientController {

    private final Logger log = LogManager.getLogger();

    private final BishClientService clientService;

    public BishkekClientController(BishClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping
    public List<BishClient> getAll() {
        clientService.addClientsToDB();
        return clientService.getAllClientsFromDB();
    }

    @GetMapping("/{id}")
    public BishClient getById(@PathVariable Long id) {
        clientService.addClientsToDB();
        return clientService.getClientById(id);
    }

    @GetMapping("/status/{status}")
    public List<BishClient> getAllByStatus(@PathVariable String status) {
        clientService.addClientsToDB();
        return clientService.getAllByStatus(status);
    }

    @PostMapping
    public BishClient addClient(@RequestBody ClientDTO clientDTO) {
        log.info("In Bishkek created new client {}", clientDTO.toString());
        return clientService.create(clientDTO);
    }

    @PutMapping("/{client_id}/status/{status_id}")
    public BishClient changeStatus(Principal principal, @PathVariable("client_id") Long id, @PathVariable("status_id") Long status_id) {
        return clientService.changeStatus(id, status_id, principal.getName());
    }

    @PutMapping("/{id}")
    public BishClient updateClient(Principal principal, @PathVariable Long id, @RequestBody ClientDTO clientDTO) {
        log.info("In Bishkek updated existing client {}", clientDTO.toString());
        return clientService.updateClient(id, clientDTO, principal.getName());
    }

}
