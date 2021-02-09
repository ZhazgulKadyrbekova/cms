package neobis.cms.Controller.Bishkek;

import neobis.cms.Entity.Bishkek.Student;
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

    @GetMapping
    public List<Student> findAll() {
        return studentService.findAll();
    }

    @PostMapping
    public Student add(@RequestBody Student student) {
        return studentService.save(student);
    }
}
