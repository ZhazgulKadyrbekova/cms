package neobis.cms.Service.Osh;

import neobis.cms.Dto.TeacherDTO;
import neobis.cms.Entity.Bishkek.BishTeachers;
import neobis.cms.Entity.Osh.OshTeachers;
import neobis.cms.Exception.ResourceNotFoundException;
import neobis.cms.Repo.Bishkek.BishTeacherRepo;
import neobis.cms.Repo.Osh.OshTeacherRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OshTeacherServiceImpl implements OshTeacherService {
    @Autowired
    private OshTeacherRepo teacherRepo;

    @Override
    public List<OshTeachers> getAllTeachers() {
        return teacherRepo.findAll();
    }

    @Override
    public OshTeachers getTeacherById(long id) {
        return teacherRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher id " + id + " was not found"));
    }

    @Override
    public OshTeachers getTeacherByName(String name) {
        OshTeachers teacher = teacherRepo.findByNameContainingIgnoringCaseAndDeleted(name, false);
        if (teacher == null)
            throw new ResourceNotFoundException("Teacher with name " + name + " was not found");
        return teacher;
    }

    @Override
    public OshTeachers addTeacher(TeacherDTO teacherDTO) {
        OshTeachers teacher = new OshTeachers();
        teacher.setName(teacherDTO.getName());
        return teacherRepo.save(teacher);
    }

    @Override
    public OshTeachers updateTeacherInfo(long id, TeacherDTO teacherDTO) {
        OshTeachers teacher = teacherRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher id " + id + " was not found"));
        teacher.setName(teacherDTO.getName());
        return teacherRepo.save(teacher);
    }

    @Override
    public String deleteTeacherById(long id) {
        OshTeachers teacher = teacherRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher id " + id + " was not found"));
        teacher.setDeleted(true);
        teacherRepo.save(teacher);
        return "Teacher id " + id + " has successfully deleted";
    }
}
