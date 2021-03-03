package neobis.cms.Controller.Osh;

import neobis.cms.Dto.ClientDTO;
import neobis.cms.Entity.Osh.OshClient;
import neobis.cms.Service.Osh.OshClientService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/osh/client")
public class OshClientController {

    private final Logger log = LogManager.getLogger();

    @Autowired
    private OshClientService clientService;

    @GetMapping
    public List<OshClient> getAll() {
        return clientService.getAllClientsFromDB();
    }

    @GetMapping("/{id}")
    public OshClient getById(@PathVariable Long id) {
        return clientService.getClientByID(id);
    }

    @GetMapping("/status/{status}")
    public List<OshClient> getAllByStatus(@PathVariable String status) {
        return clientService.getAllByStatus(status);
    }

    @PostMapping
    public OshClient addClient(@RequestBody ClientDTO clientDTO) {
        log.info("In Osh created new client {}", clientDTO.toString());
        return clientService.create(clientDTO);
    }

    @PutMapping("/{client_id}/status/{status_id}")
    public OshClient changeStatus(Principal principal, @PathVariable("client_id") Long id, @PathVariable("status_id") Long status_id) {
        return clientService.changeStatus(id, status_id, principal.getName());
    }

    @PutMapping("/{id}")
    public OshClient updateClient(Principal principal, @PathVariable Long id, @RequestBody ClientDTO clientDTO) {
        return clientService.updateClient(id, clientDTO, principal.getName());
    }

}
