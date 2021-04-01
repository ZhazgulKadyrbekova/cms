package neobis.cms.Service.Osh;

import neobis.cms.Dto.TeacherDTO;
import neobis.cms.Dto.WorkerDTO;
import neobis.cms.Entity.Bishkek.User;
import neobis.cms.Entity.Osh.OshCourses;
import neobis.cms.Entity.Osh.OshTeachers;
import neobis.cms.Exception.ResourceNotFoundException;
import neobis.cms.Repo.Osh.OshTeacherRepo;
import neobis.cms.Service.Bishkek.UserService;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class OshTeacherServiceImpl implements OshTeacherService {
    private final OshTeacherRepo teacherRepo;
    private final UserService userService;
    private final OshCoursesService coursesService;

    public OshTeacherServiceImpl(OshTeacherRepo teacherRepo, UserService userService, @Lazy OshCoursesService coursesService) {
        this.teacherRepo = teacherRepo;
        this.userService = userService;
        this.coursesService = coursesService;
    }

    private List<WorkerDTO> toWorkers(List<OshTeachers> teachers, List<User> users) {
        List<WorkerDTO> workers = new ArrayList<>();

        for (OshTeachers teacher : teachers)
            workers.add(new WorkerDTO(teacher.getName(), teacher.getSurname(), teacher.getEmail(), teacher.getPhoneNo(), teacher.getPosition(),
                    teacher.getCourseName()));

        for (User user : users)
            workers.add(new WorkerDTO(user.getName(), user.getSurname(), user.getEmail(), user.getPhoneNo(), user.getPosition(), null));

        return workers;
    }

    private Set<WorkerDTO> toWorkers(Set<OshTeachers> teachers, Set<User> users) {
        Set<WorkerDTO> workers = new HashSet<>();

        for (OshTeachers teacher : teachers)
            workers.add(new WorkerDTO(teacher.getName(), teacher.getSurname(), teacher.getEmail(), teacher.getPhoneNo(), teacher.getPosition(),
                    teacher.getCourseName()));

        for (User user : users)
            workers.add(new WorkerDTO(user.getName(), user.getSurname(), user.getEmail(), user.getPhoneNo(), user.getPosition(), null));

        return workers;
    }

    @Override
    public Set<WorkerDTO> getWithPredicate(String field, String position, List<Long> courseID) {
        Set<OshTeachers> teachers = new HashSet<>();
        Set<User> users = new HashSet<>();

        if (field != null) {
            for (String item : field.split(" ")) {
                teachers.addAll(teacherRepo.findAllByNameContainingIgnoringCase(item));
                users.addAll(userService.getAllByName(item));

                teachers.addAll(teacherRepo.findAllBySurnameContainingIgnoringCase(item));
                users.addAll(userService.getAllBySurname(item));
            }
            teachers.addAll(teacherRepo.findAllByPhoneNoContaining(field));
            users.addAll(userService.getAllByPhoneNo(field));
            teachers.addAll(teacherRepo.findAllByEmailContainingIgnoringCase(field));
            users.addAll(userService.getAllByEmail(field));
        }

        if (position != null && courseID != null) {
            for (long course : courseID) {
                OshTeachers teacher = coursesService.findCourseById(course).getTeacher();
                if (teacher.getPosition().equalsIgnoreCase(position))
                    teachers.add(teacher);
            }
        } else if (courseID != null) {
            for (long course : courseID) {
                teachers.add(coursesService.findCourseById(course).getTeacher());
            }
        } else if (position != null) {
            teachers.addAll(teacherRepo.findAllByPositionContainingIgnoringCase(position));
            users.addAll(userService.getAllByPositionAndCity(position, "osh"));
        }
        return toWorkers(teachers, users);
    }

    @Override
    public Page<WorkerDTO> getAllWorkers(Pageable pageable) {
        List<OshTeachers> teachers = teacherRepo.findAll();
        List<User> users = userService.getUsersByCity("osh");
        List<WorkerDTO> workers = toWorkers(teachers, users);

        final int start = (int)pageable.getOffset();
        final int end = Math.min((start + pageable.getPageSize()), workers.size());
        return new PageImpl<>(workers.subList(start, end), pageable, workers.size());
    }

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
    public List<OshTeachers> getAllByName(String name) {
        return teacherRepo.findAllByNameContainingIgnoringCase(name);
    }

    @Override
    public OshTeachers getTeacherByName(String name) {
        OshTeachers teacher = teacherRepo.findByNameContainingIgnoringCase(name);
        if (teacher == null)
            throw new ResourceNotFoundException("Teacher with name " + name + " was not found");
        return teacher;
    }

    private OshTeachers teacherToDTO(OshTeachers teacher, TeacherDTO teacherDTO) {
        teacher.setName(teacherDTO.getName());
        teacher.setSurname(teacherDTO.getSurname());
        teacher.setEmail(teacherDTO.getEmail());
        teacher.setPhoneNo(teacherDTO.getPhoneNo());
        teacher.setPosition(teacherDTO.getPosition());
        teacher.setStartDate(teacherDTO.getStartDate());
        teacher.setEndDate(teacherDTO.getEndDate());
        if (teacherDTO.getCourse() != 0) {
            OshCourses course = coursesService.setTeacher(teacherDTO.getCourse(), teacher.getID());
            teacher.setCourseName(course.getName());
        }
        return teacher;
    }

    @Override
    public OshTeachers addTeacher(TeacherDTO teacherDTO) {
        OshTeachers teacher = teacherToDTO(new OshTeachers(), teacherDTO);
        return teacherRepo.save(teacher);
    }

    @Override
    public OshTeachers updateTeacherInfo(long id, TeacherDTO teacherDTO) {
        OshTeachers teacher = teacherToDTO(teacherRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher id " + id + " was not found")), teacherDTO);
        return teacherRepo.save(teacher);
    }

    @Override
    public String deleteTeacherById(long id) {
        OshTeachers teacher = teacherRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher id " + id + " was not found"));
        teacherRepo.delete(teacher);
        return "Teacher with id " + id + " has successfully deleted";
    }

    @Override
    public Set<Object> simpleSearch(String nameOrPhone) {
        Set<Object> workers = new HashSet<>();
        for (String item : nameOrPhone.split(" ")) {
            workers.addAll(teacherRepo.findAllByNameContainingIgnoringCase(item));
            workers.addAll(userService.getAllByName(item));

            workers.addAll(teacherRepo.findAllBySurnameContainingIgnoringCase(item));
            workers.addAll(userService.getAllBySurname(item));
        }
        workers.addAll(teacherRepo.findAllByPhoneNoContaining(nameOrPhone));
        workers.addAll(userService.getAllByPhoneNo(nameOrPhone));
        workers.addAll(teacherRepo.findAllByEmailContainingIgnoringCase(nameOrPhone));
        workers.addAll(userService.getAllByEmail(nameOrPhone));

        return workers;
    }
}
