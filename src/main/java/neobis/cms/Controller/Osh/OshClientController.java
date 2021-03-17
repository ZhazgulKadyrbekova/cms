package neobis.cms.Controller.Osh;

import neobis.cms.Dto.ClientDTO;
import neobis.cms.Dto.ResponseMessage;
import neobis.cms.Entity.Osh.OshClient;
import neobis.cms.Service.Osh.OshClientService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    public Page<OshClient> getAll(Pageable pageable) {
        return clientService.getAllClientsFromDB(pageable);
    }

    @GetMapping("/search")
    public Page<OshClient> getWithPredicate(Pageable pageable,
                                            @RequestParam(required = false) String nameOrPhone,
                                            @RequestParam(required = false) List<Long> status_id,
                                            @RequestParam(required = false) List<Long> course_id,
                                            @RequestParam(required = false) List<Long> occupation_id) {
        if (nameOrPhone != null)
            return clientService.search(pageable, nameOrPhone);
        return clientService.getWithPredicate(pageable, status_id, course_id, occupation_id);
    }

    @GetMapping("/{id}")
    public OshClient getById(@PathVariable Long id) {
        return clientService.getClientByID(id);
    }

    @GetMapping("/status/{status_id}")
    public List<OshClient> getAllByStatus(@PathVariable Long status_id) {
        return clientService.getAllByStatus(status_id);
    }

//    @GetMapping("/name/{name}")
//    public List<OshClient> getAllByName(@PathVariable String name) {
//        return clientService.getAllByName(name);
//    }

    @PostMapping
    public OshClient addClient(@RequestBody ClientDTO clientDTO, Principal principal) {
        log.info("In Osh created new client {}", clientDTO.toString());
        return clientService.create(clientDTO, principal.getName());
    }

    @PutMapping("/{client_id}/status/{status_id}")
    public OshClient changeStatus(Principal principal, @PathVariable("client_id") Long id, @PathVariable("status_id") Long status_id) {
        return clientService.changeStatus(id, status_id, principal.getName());
    }

    @PutMapping("/{client_id}/city")
    public ResponseMessage changeCity(@PathVariable Long client_id, Principal principal) {
        clientService.changeCity(client_id, principal.getName());
        return new ResponseMessage("Client with id " + client_id + " has been successfully moved to another city");
    }

    @PutMapping("/{id}")
    public OshClient updateClient(Principal principal, @PathVariable Long id, @RequestBody ClientDTO clientDTO) {
        return clientService.updateClient(id, clientDTO, principal.getName());
    }

}
