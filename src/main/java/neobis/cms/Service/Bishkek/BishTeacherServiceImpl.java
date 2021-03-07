package neobis.cms.Service.Bishkek;

import neobis.cms.Dto.TeacherDTO;
import neobis.cms.Entity.Bishkek.BishTeachers;
import neobis.cms.Exception.ResourceNotFoundException;
import neobis.cms.Repo.Bishkek.BishTeacherRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BishTeacherServiceImpl implements BishTeacherService {
    @Autowired
    private BishTeacherRepo teacherRepo;

    @Override
    public List<BishTeachers> getAllTeachers() {
        return teacherRepo.findAll();
    }

    @Override
    public BishTeachers getTeacherById(long id) {
        return teacherRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher id " + id + " was not found"));
    }

    @Override
    public BishTeachers getTeacherByName(String name) {
        BishTeachers teacher = teacherRepo.findByNameContainingIgnoringCase(name);
        if (teacher == null)
            throw new ResourceNotFoundException("Teacher with name " + name + " was not found");
        return teacher;
    }

    @Override
    public BishTeachers addTeacher(TeacherDTO teacherDTO) {
        BishTeachers teacher = new BishTeachers();
        teacher.setName(teacherDTO.getName());
        return teacherRepo.save(teacher);
    }

    @Override
    public BishTeachers updateTeacherInfo(long id, TeacherDTO teacherDTO) {
        BishTeachers teacher = teacherRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher id " + id + " was not found"));
        teacher.setName(teacherDTO.getName());
        return teacherRepo.save(teacher);
    }

    @Override
    public String deleteTeacherById(long id) {
        BishTeachers teacher = teacherRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher id " + id + " was not found"));
        teacherRepo.delete(teacher);
        return "Teacher id " + id + " has successfully deleted";
    }
}
