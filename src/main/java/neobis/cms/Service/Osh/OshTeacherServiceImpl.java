package neobis.cms.Service.Osh;

import neobis.cms.Dto.TeacherDTO;
import neobis.cms.Dto.WorkerDTO;
import neobis.cms.Entity.Bishkek.User;
import neobis.cms.Entity.Osh.OshTeachers;
import neobis.cms.Exception.ResourceNotFoundException;
import neobis.cms.Repo.Osh.OshTeacherRepo;
import neobis.cms.Service.Bishkek.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OshTeacherServiceImpl implements OshTeacherService {
    @Autowired
    private OshTeacherRepo teacherRepo;
    @Autowired
    private UserService userService;
    @Autowired
    private OshCoursesService coursesService;

    @Override
    public List<OshTeachers> getAllTeachers() {
        return teacherRepo.findAll();
    }

    @Override
    public List<OshTeachers> getAllByName(String name) {
        return teacherRepo.findAllByNameContainingIgnoringCase(name);
    }

    @Override
    public Page<WorkerDTO> getWithPredicate(Pageable pageable, String position, Long courseID) {
        List<WorkerDTO> workers = new ArrayList<>();
        List<OshTeachers> teachers = new ArrayList<>();
        List<User> users = new ArrayList<>();
        if (position != null && courseID != null) {
            OshTeachers teacher = coursesService.findCourseById(courseID).getTeacher();
            if (teacher.getPosition().equals(position))
                teachers.add(teacher);
        } else if (courseID != null) {
            teachers.add(coursesService.findCourseById(courseID).getTeacher());
        } else if (position != null) {
            teachers = teacherRepo.findAllByPositionContaining(position);
            users = userService.getAllByPositionAndCity(position, "osh");
        }

        for (OshTeachers teacher : teachers) {
            workers.add(new WorkerDTO(teacher.getName(), teacher.getSurname(), teacher.getEmail(), teacher.getPhoneNo(), teacher.getPosition(), teacher.getCourseName()));
        }
        for (User user : users) {
            workers.add(new WorkerDTO(user.getName(), user.getSurname(), user.getEmail(), user.getPhoneNo(), user.getPosition(), null));
        }
        final int start = (int)pageable.getOffset();
        final int end = Math.min((start + pageable.getPageSize()), workers.size());
        return new PageImpl<>(workers.subList(start, end), pageable, workers.size());
    }

    @Override
    public Page<WorkerDTO> getAll(Pageable pageable) {
        List<OshTeachers> teachers = teacherRepo.findAll();
        List<User> users = userService.getUsersByCity("osh");
        List<WorkerDTO> workers = new ArrayList<>();
        for (OshTeachers teacher : teachers) {
            workers.add(new WorkerDTO(teacher.getName(), teacher.getSurname(), teacher.getEmail(), teacher.getPhoneNo(), teacher.getPosition(), teacher.getCourseName()));
        }
        for (User user : users) {
            workers.add(new WorkerDTO(user.getName(), user.getSurname(), user.getEmail(), user.getPhoneNo(), user.getPosition(), null));
        }
        final int start = (int)pageable.getOffset();
        final int end = Math.min((start + pageable.getPageSize()), workers.size());
        return new PageImpl<>(workers.subList(start, end), pageable, workers.size());
    }

    @Override
    public OshTeachers getTeacherById(long id) {
        return teacherRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher id " + id + " was not found"));
    }

    @Override
    public OshTeachers getTeacherByName(String name) {
        OshTeachers teacher = teacherRepo.findByNameContainingIgnoringCase(name);
        if (teacher == null)
            throw new ResourceNotFoundException("Teacher with name " + name + " was not found");
        return teacher;
    }

    @Override
    public OshTeachers addTeacher(TeacherDTO teacherDTO) {
        OshTeachers teacher = new OshTeachers();
        teacher.setName(teacherDTO.getName());
        teacher.setSurname(teacherDTO.getSurname());
        teacher.setEmail(teacherDTO.getEmail());
        teacher.setPhoneNo(teacherDTO.getPhoneNo());
        teacher.setPosition(teacherDTO.getPosition());
        teacher.setCourseName(teacherDTO.getCourseName());
        teacher.setStartDate(teacherDTO.getStartDate());
        teacher.setEndDate(teacherDTO.getEndDate());
        return teacherRepo.save(teacher);
    }

    @Override
    public OshTeachers updateTeacherInfo(long id, TeacherDTO teacherDTO) {
        OshTeachers teacher = teacherRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher id " + id + " was not found"));
        teacher.setName(teacherDTO.getName());
        teacher.setSurname(teacherDTO.getSurname());
        teacher.setEmail(teacherDTO.getEmail());
        teacher.setPhoneNo(teacherDTO.getPhoneNo());
        teacher.setPosition(teacherDTO.getPosition());
        teacher.setCourseName(teacherDTO.getCourseName());
        teacher.setStartDate(teacherDTO.getStartDate());
        teacher.setEndDate(teacherDTO.getEndDate());
        return teacherRepo.save(teacher);
    }

    @Override
    public String deleteTeacherById(long id) {
        OshTeachers teacher = teacherRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher id " + id + " was not found"));
        teacherRepo.delete(teacher);
        return "Teacher id " + id + " has successfully deleted";
    }
}
