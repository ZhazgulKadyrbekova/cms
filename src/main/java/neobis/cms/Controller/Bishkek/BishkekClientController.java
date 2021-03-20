package neobis.cms.Controller.Bishkek;

import neobis.cms.Dto.ClientDTO;
import neobis.cms.Dto.ResponseMessage;
import neobis.cms.Entity.Bishkek.BishClient;
import neobis.cms.Service.Bishkek.BishClientService;
import neobis.cms.Service.ExcelService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.Principal;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/bishkek/client")
public class BishkekClientController {

    private final Logger log = LogManager.getLogger();

    private final BishClientService clientService;
    private final ExcelService excelService;
    public BishkekClientController(BishClientService clientService, ExcelService excelService) {
        this.clientService = clientService;
        this.excelService = excelService;
    }

    @GetMapping
    public Page<BishClient> getAll(Pageable pageable
//                                    @RequestParam(defaultValue = "0") Integer pageNo,
//                                    @RequestParam(defaultValue = "20") Integer pageSize
    ) {
        clientService.addClientsToDB();
        return clientService.getAllClientsFromDB(pageable);
    }

    @GetMapping("/search")
    public Page<BishClient> getWithPredicate(Pageable pageable,
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
            return clientService.search(pageable, nameOrPhone);
        return clientService.getWithPredicate(pageable, status_id, course_id, occupation_id);
    }

    @GetMapping(value = "/export")
//            produces = MediaType.APPLICATION_OCTET_STREAM)
    public byte[] getFile() throws IOException {
        Workbook workbook = excelService.getWorkbook();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            workbook.write(bos);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            bos.close();
        }
        //        File file = excelService.writeIntoFile();
//        return ResponseEntity.ok(file, MediaType.APPLICATION_OCTET_STREAM)
//                .header("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"" ) //optional
//                .build();
        return bos.toByteArray();
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
