package neobis.cms.Controller.Osh;

import neobis.cms.Entity.Osh.OshStudent;
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
    public List<OshStudent> findAll() {
        return studentService.findAll();
    }

    @PostMapping
    public OshStudent add(@RequestBody OshStudent oshStudent) {
        return studentService.save(oshStudent);
    }
}
