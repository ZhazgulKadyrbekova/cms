package neobis.cms.Controller.Bishkek;

import neobis.cms.Entity.Bishkek.BishStudent;
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
    public List<BishStudent> findAll() {
        return studentService.findAll();
    }

    @PostMapping
    public BishStudent add(@RequestBody BishStudent bishStudent) {
        return studentService.save(bishStudent);
    }
}
