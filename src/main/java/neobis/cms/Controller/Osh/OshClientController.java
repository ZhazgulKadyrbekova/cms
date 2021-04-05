package neobis.cms.Controller.Osh;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import neobis.cms.Dto.ClientDTO;
import neobis.cms.Dto.PaymentDTO;
import neobis.cms.Dto.ResponseMessage;
import neobis.cms.Entity.Osh.OshClient;
import neobis.cms.Entity.Osh.OshCourses;
import neobis.cms.Service.ExcelService;
import neobis.cms.Service.Osh.OshClientService;
import neobis.cms.Util.ExcelUtilHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/osh/client")
public class OshClientController {

    private final Logger log = LogManager.getLogger();

    private final OshClientService clientService;
    private final ExcelService excelService;

    public OshClientController(OshClientService clientService, ExcelService excelService) {
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
    public Page<OshClient> getAll(Pageable pageable) {
        return clientService.getAllClientsFromDB(pageable);
    }

    @GetMapping("/filter")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", dataType = "int", paramType = "query",
                    value = "Results page you want to retrieve (0...N)", defaultValue = "0"),
            @ApiImplicitParam(name = "size", dataType = "int", paramType = "query",
                    value = "Number of records per page.", defaultValue = "20"),
            @ApiImplicitParam(name = "field", dataType = "string", paramType = "query",
                    value = "Name, surname, email, phone number by which to search.")})
    public Page<OshClient> filter(Pageable pageable,
                                            @ApiIgnore @RequestParam(required = false) String field,
                                            @RequestParam(required = false) List<Long> status_id,
                                            @RequestParam(required = false) List<Long> course_id,
                                            @RequestParam(required = false) List<Long> occupation_id,
                                            @RequestParam(required = false) List<Long> utm_id ) {
        List<OshClient> clients = new ArrayList<>(clientService.getWithPredicate(field, status_id, course_id, occupation_id, utm_id));
        final int start = (int)pageable.getOffset();
        final int end = Math.min((start + pageable.getPageSize()), clients.size());

        return new PageImpl<>(clients.subList(start, end), pageable, clients.size());
    }

    @GetMapping("/search")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", dataType = "int", paramType = "query",
                    value = "Results page you want to retrieve (0...N)", defaultValue = "0"),
            @ApiImplicitParam(name = "size", dataType = "int", paramType = "query",
                    value = "Number of records per page.", defaultValue = "20"),
            @ApiImplicitParam(name = "field", dataType = "string", paramType = "query", required = true,
                    value = "Name, surname, email, phone number by which to search.")})
    public Page<OshClient> search(Pageable pageable, @ApiIgnore @RequestParam String field) {
        List<OshClient> responses = new ArrayList<>(clientService.search(field));

        final int start = (int)pageable.getOffset();
        final int end = Math.min((start + pageable.getPageSize()), responses.size());

        return new PageImpl<>(responses.subList(start, end), pageable, responses.size());
    }

    @GetMapping(value = "/export")
    public ResponseEntity<Resource> getFile() {
        String filename = "export-clients-osh.xlsx";
        InputStreamResource file = new InputStreamResource(excelService.loadOshClients());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(file);
    }

    @GetMapping("/{id}")
    public OshClient getById(@PathVariable Long id) {
        return clientService.getClientByID(id);
    }

    @PostMapping("/import")
    public ResponseEntity<ResponseMessage> importFile(@RequestParam("file")MultipartFile file) {
        String message;
        if (ExcelUtilHelper.hasExcelFormat(file)) {
            try {
                excelService.saveOshClients(file);

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
    public List<OshClient> getAllByStatus(@PathVariable Long status_id) {
        return clientService.getAllByStatus(status_id);
    }

    @PostMapping
    public OshClient addClient(@RequestBody ClientDTO clientDTO, Principal principal) {
        log.info("In Osh created new client {}", clientDTO.toString());
        return clientService.create(clientDTO, principal.getName());
    }

    @PutMapping("/{client_id}/status/{status_id}")
    public OshClient changeStatus(Principal principal, @PathVariable("client_id") Long id, @PathVariable("status_id") Long status_id) {
        return clientService.changeStatus(id, status_id, principal.getName());
    }

    @GetMapping("/{client_id}/courses")
    public List<OshCourses> getClientCourses(@PathVariable("client_id") Long id) {
        return clientService.getCourses(id);
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

    @DeleteMapping("/{id}")
    public ResponseMessage deleteClient(Principal principal, @PathVariable Long id) {
        log.info("In Osh deleted existing client {}", id);
        return clientService.deleteClient(id, principal.getName());
    }

    @PostMapping("/{client_id}/payment")
    public OshClient addPayment(Principal principal, @PathVariable("client_id") Long id, @RequestBody PaymentDTO paymentDTO) {
        return clientService.addPayment(id, paymentDTO, principal.getName());
    }

    @PutMapping("/{client_id}/payment/{payment_id}")
    public OshClient editPayment(Principal principal, @PathVariable("client_id") Long id, @RequestBody PaymentDTO paymentDTO, @PathVariable("payment_id") Long paymentID) {
        return clientService.editPayment(id, paymentDTO, paymentID, principal.getName());
    }

    @DeleteMapping("/{client_id}/payment/{payment_id}")
    public ResponseMessage deletePayment(Principal principal, @PathVariable("client_id") Long id, @PathVariable("payment_id") Long paymentID) {
        return clientService.deletePayment(id, paymentID, principal.getName());
    }

}
