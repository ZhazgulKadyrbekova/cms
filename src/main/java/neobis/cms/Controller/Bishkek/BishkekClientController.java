package neobis.cms.Controller.Bishkek;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import neobis.cms.Dto.ClientDTO;
import neobis.cms.Dto.ResponseMessage;
import neobis.cms.Entity.Bishkek.BishClient;
import neobis.cms.Service.Bishkek.BishClientService;
import neobis.cms.Service.ExcelService;
import neobis.cms.Util.ExcelUtilHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", dataType = "int", paramType = "query",
                    value = "Results page you want to retrieve (0...N)", defaultValue = "0"),
            @ApiImplicitParam(name = "size", dataType = "int", paramType = "query",
                    value = "Number of records per page.", defaultValue = "20")
    })
    public Page<BishClient> getAll(Pageable pageable
//                                    @RequestParam(defaultValue = "0") Integer pageNo,
//                                    @RequestParam(defaultValue = "20") Integer pageSize
    ) {
        // clientService.addClientsToDB();
        return clientService.getAllClientsFromDB(pageable);
    }

    @GetMapping("/search")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", dataType = "int", paramType = "query",
                    value = "Results page you want to retrieve (0...N)", defaultValue = "0"),
            @ApiImplicitParam(name = "size", dataType = "int", paramType = "query",
                    value = "Number of records per page.", defaultValue = "20")
    })
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
    public ResponseEntity<Resource> getFile() {
        String filename = "Clients.xlsx";
        InputStreamResource file = new InputStreamResource(excelService.loadBishClients());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(file);
    }

    @GetMapping("/{id}")
    public BishClient getById(@PathVariable Long id) {
//        clientService.addClientsToDB();
        return clientService.getClientById(id);
    }

    @PostMapping("/import")
    public ResponseEntity<ResponseMessage> importFile(@RequestParam("file") MultipartFile file) {
        String message = "";
        if (ExcelUtilHelper.hasExcelFormat(file)) {
            try {
                excelService.saveBishClients(file);

                message = "Successfully uploaded file " + file.getOriginalFilename();
                return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
            } catch (Exception e) {
	            e.printStackTrace();
                message = "Could not upload file " + file.getOriginalFilename() + "! " + e.getMessage();
                return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
            }
        }

        message = "Please, upload an excel file in xlsx format!";
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(message));
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
