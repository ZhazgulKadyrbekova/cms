package neobis.cms.Controller.Bishkek;

import neobis.cms.Dto.PaymentDTO;
import neobis.cms.Dto.StudentDTO;
import neobis.cms.Dto.StudentShowDTO;
import neobis.cms.Service.Bishkek.BishPaymentService;
import neobis.cms.Service.Bishkek.BishStudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/bishkek/student")
public class BishkekStudentController {

    @Autowired
    private BishStudentService studentService;

    @Autowired
    private BishPaymentService paymentService;

    @GetMapping("/getAll")
    public List<StudentShowDTO> getAll() {
        return studentService.getAll();
    }

    @PostMapping("/add")
    public StudentShowDTO create(@RequestBody StudentDTO studentDTO) {
        return studentService.create(studentDTO);
    }

    @PostMapping("/addPayment")
    public StudentShowDTO addPayment(@RequestBody PaymentDTO paymentDTO) {
        StudentShowDTO student = studentService.getStudentByClientID(paymentDTO.getClient());
        paymentService.create(paymentDTO);
        student.setPayments(paymentService.getAllByClient(paymentDTO.getClient()));
        return student;
    }

}
