package neobis.cms.Controller.Bishkek;

import io.swagger.annotations.ApiParam;
import neobis.cms.Dto.ClientDTO;
import neobis.cms.Entity.Bishkek.BishClient;
import neobis.cms.Service.Bishkek.BishClientService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
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

    @GetMapping("/search")
    public List<BishClient> getWithPredicate(@ApiParam(value="yyyy-MM-dd HH:mm") @RequestParam(required = false) String dateAfter,
                                             @ApiParam(value="yyyy-MM-dd HH:mm") @RequestParam(required = false) String dateBefore,
                                             @RequestParam(required = false) Long status_id,
                                             @RequestParam(required = false) Long course_id,
                                             @RequestParam(required = false) String occupation,
                                             @RequestParam(required = false) String utm) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime date1 = null, date2 = null;
        if (dateAfter != null)
        	date1 = LocalDateTime.parse(dateAfter, formatter);
        if (dateBefore != null)
            date2 = LocalDateTime.parse(dateBefore, formatter);
        return clientService.getWithPredicate(date1, date2, status_id, course_id, occupation, utm);
    }

    @GetMapping("/{id}")
    public BishClient getById(@PathVariable Long id) {
        clientService.addClientsToDB();
        return clientService.getClientById(id);
    }

    @GetMapping("/status/{status_id}")
    public List<BishClient> getAllByStatus(@PathVariable Long status_id) {
        clientService.addClientsToDB();
        return clientService.getAllByStatus(status_id);
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
