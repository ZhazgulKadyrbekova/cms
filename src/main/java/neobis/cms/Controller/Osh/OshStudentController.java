package neobis.cms.Controller.Osh;

import neobis.cms.Entity.Osh.Student;
import neobis.cms.Service.Osh.OshStudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/osh/student")
public class OshStudentController {
    @Autowired
    private OshStudentService studentService;

    @GetMapping
    public List<Student> findAll() {
        return studentService.findAll();
    }

    @PostMapping
    public Student add(@RequestBody Student student) {
        return studentService.save(student);
    }
}
