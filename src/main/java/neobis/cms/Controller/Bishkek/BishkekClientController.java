package neobis.cms.Controller.Bishkek;

import io.swagger.annotations.ApiParam;
import neobis.cms.Dto.ClientDTO;
import neobis.cms.Dto.ResponseMessage;
import neobis.cms.Entity.Bishkek.BishClient;
import neobis.cms.Service.Bishkek.BishClientService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

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

    @GetMapping("/search")
    public List<BishClient> getWithPredicate(
//                                            @ApiParam(value="yyyy-MM-dd-HH:mm") @RequestParam(required = false) String dateAfter,
//                                            @ApiParam(value="yyyy-MM-dd-HH:mm") @RequestParam(required = false) String dateBefore,
                                             @RequestParam(required = false) String nameOrPhone,
                                             @RequestParam(required = false) List<Long> status_id,
                                             @RequestParam(required = false) List<Long> course_id,
                                             @RequestParam(required = false) List<Long> occupation_id
//                                             @RequestParam(required = false) String utm
    ) {
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH:mm");
//        LocalDateTime date1 = null, date2 = null;
//        if (dateAfter != null)
//        	date1 = LocalDateTime.parse(dateAfter, formatter);
//        if (dateBefore != null)
//            date2 = LocalDateTime.parse(dateBefore, formatter);
        if (nameOrPhone != null)
            return clientService.search(nameOrPhone);
        return clientService.getWithPredicate(status_id, course_id, occupation_id);
    }

    @GetMapping("/{id}")
    public BishClient getById(@PathVariable Long id) {
//        clientService.addClientsToDB();
        return clientService.getClientById(id);
    }

    @GetMapping("/status/{status_id}")
    public List<BishClient> getAllByStatus(@PathVariable Long status_id) {
//        clientService.addClientsToDB();
        return clientService.getAllByStatus(status_id);
    }

//    @GetMapping("/name/{name}")
//    public List<BishClient> getAllByName(@PathVariable String name) {
////        clientService.addClientsToDB();
//        return clientService.getAllByName(name);
//    }

    @PostMapping
    public BishClient addClient(@RequestBody ClientDTO clientDTO, Principal principal) {
        log.info("In Bishkek created new client {}", clientDTO.toString());
        return clientService.create(clientDTO, principal.getName());
    }

    @PutMapping("/{client_id}/status/{status_id}")
    public BishClient changeStatus(Principal principal, @PathVariable("client_id") Long id, @PathVariable("status_id") Long status_id) {
        return clientService.changeStatus(id, status_id, principal.getName());
    }

    @PutMapping("/{client_id}/city")
    public ResponseMessage changeCity(@PathVariable Long client_id, Principal principal) {
        clientService.changeCity(client_id, principal.getName());
        return new ResponseMessage("Client with id " + client_id + " has been successfully moved to another city");
    }

    @PutMapping("/{id}")
    public BishClient updateClient(Principal principal, @PathVariable Long id, @RequestBody ClientDTO clientDTO) {
        log.info("In Bishkek updated existing client {}", clientDTO.toString());
        return clientService.updateClient(id, clientDTO, principal.getName());
    }

}
