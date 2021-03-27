package neobis.cms.Service.Bishkek;

import neobis.cms.Dto.TeacherDTO;
import neobis.cms.Dto.WorkerDTO;
import neobis.cms.Entity.Bishkek.BishTeachers;
import neobis.cms.Entity.Bishkek.User;
import neobis.cms.Exception.ResourceNotFoundException;
import neobis.cms.Repo.Bishkek.BishTeacherRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BishTeacherServiceImpl implements BishTeacherService {
    @Autowired
    private BishTeacherRepo teacherRepo;
    @Autowired
    private UserService userService;
    @Autowired
    private BishCoursesService coursesService;

    @Override
    public Page<WorkerDTO> getWithPredicate(Pageable pageable, String position, Long courseID) {
        List<WorkerDTO> workers = new ArrayList<>();
        List<BishTeachers> teachers = new ArrayList<>();
        List<User> users = new ArrayList<>();
        if (position != null && courseID != null) {
            BishTeachers teacher = coursesService.findCourseById(courseID).getTeacher();
            if (teacher.getPosition().equalsIgnoreCase(position))
                teachers.add(teacher);
        } else if (courseID != null) {
            teachers.add(coursesService.findCourseById(courseID).getTeacher());
        } else if (position != null) {
            teachers = teacherRepo.findAllByPositionContainingIgnoringCase(position);
            users = userService.getAllByPositionAndCity(position, "bishkek");
        }

        for (BishTeachers teacher : teachers) {
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
    public Page<WorkerDTO> getAllTeachers(Pageable pageable) {
        List<BishTeachers> teachers = teacherRepo.findAll();
        List<User> users = userService.getUsersByCity("bishkek");
        List<WorkerDTO> workers = new ArrayList<>();
        for (BishTeachers teacher : teachers) {
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
    public BishTeachers getTeacherById(long id) {
        return teacherRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher id " + id + " was not found"));
    }

    @Override
    public List<BishTeachers> getTeachersByName(String name) {
        List<BishTeachers> teachers = teacherRepo.findAllBySurnameContainingIgnoringCase(name);
        teachers.addAll(teacherRepo.findAllByNameContainingIgnoringCase(name));
        return teachers;
    }

    @Override
    public BishTeachers addTeacher(TeacherDTO teacherDTO) {
        BishTeachers teacher = new BishTeachers();
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
    public BishTeachers updateTeacherInfo(long id, TeacherDTO teacherDTO) {
        BishTeachers teacher = teacherRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher with id " + id + " has not found"));
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
        BishTeachers teacher = teacherRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher id " + id + " has not found"));
        teacherRepo.delete(teacher);
        return "Teacher with id " + id + " has successfully deleted";
    }
}
