package neobis.cms.Service.Bishkek;

import neobis.cms.Dto.TeacherDTO;
import neobis.cms.Dto.WorkerDTO;
import neobis.cms.Entity.Bishkek.BishCourses;
import neobis.cms.Entity.Bishkek.BishTeachers;
import neobis.cms.Entity.Bishkek.User;
import neobis.cms.Exception.ResourceNotFoundException;
import neobis.cms.Repo.Bishkek.BishTeacherRepo;
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
public class BishTeacherServiceImpl implements BishTeacherService {
    private final BishTeacherRepo teacherRepo;
    private final UserService userService;
    private final BishCoursesService coursesService;

    public BishTeacherServiceImpl(BishTeacherRepo teacherRepo, UserService userService, @Lazy BishCoursesService coursesService) {
        this.teacherRepo = teacherRepo;
        this.userService = userService;
        this.coursesService = coursesService;
    }

    private List<WorkerDTO> toWorkers(List<BishTeachers> teachers, List<User> users) {
        List<WorkerDTO> workers = new ArrayList<>();

        for (BishTeachers teacher : teachers)
            workers.add(new WorkerDTO(teacher.getName(), teacher.getSurname(), teacher.getEmail(), teacher.getPhoneNo(), teacher.getPosition(),
                    teacher.getCourseName()));

        for (User user : users)
            workers.add(new WorkerDTO(user.getName(), user.getSurname(), user.getEmail(), user.getPhoneNo(), user.getPosition(), null));

        return workers;
    }

    private Set<WorkerDTO> toWorkers(Set<BishTeachers> teachers, Set<User> users) {
        Set<WorkerDTO> workers = new HashSet<>();

        for (BishTeachers teacher : teachers)
            workers.add(new WorkerDTO(teacher.getName(), teacher.getSurname(), teacher.getEmail(), teacher.getPhoneNo(), teacher.getPosition(),
                    teacher.getCourseName()));

        for (User user : users)
            workers.add(new WorkerDTO(user.getName(), user.getSurname(), user.getEmail(), user.getPhoneNo(), user.getPosition(), null));

        return workers;
    }

    @Override
    public Set<WorkerDTO> getWithPredicate(String field, String position, List<Long> courseID) {
        Set<BishTeachers> teachers = new HashSet<>();
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
                BishTeachers teacher = coursesService.findCourseById(course).getTeacher();
                if (teacher.getPosition().equalsIgnoreCase(position))
                    teachers.add(teacher);
            }
        } else if (courseID != null) {
            for (long course : courseID)
                teachers.add(coursesService.findCourseById(course).getTeacher());
        } else if (position != null) {
            teachers.addAll(teacherRepo.findAllByPositionContainingIgnoringCase(position));
            users.addAll(userService.getAllByPositionAndCity(position, "bishkek"));
        }
        return toWorkers(teachers, users);

    }

    @Override
    public Page<WorkerDTO> getAllWorkers(Pageable pageable) {
        List<BishTeachers> teachers = teacherRepo.findAll();
        List<User> users = userService.getUsersByCity("bishkek");
        List<WorkerDTO> workers = toWorkers(teachers, users);

        final int start = (int)pageable.getOffset();
        final int end = Math.min((start + pageable.getPageSize()), workers.size());
        return new PageImpl<>(workers.subList(start, end), pageable, workers.size());
    }

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
    public List<BishTeachers> getTeachersByName(String name) {
        List<BishTeachers> teachers = teacherRepo.findAllBySurnameContainingIgnoringCase(name);
        teachers.addAll(teacherRepo.findAllByNameContainingIgnoringCase(name));
        return teachers;
    }

    private BishTeachers teacherToDTO(BishTeachers teacher, TeacherDTO teacherDTO) {
        teacher.setName(teacherDTO.getName());
        teacher.setSurname(teacherDTO.getSurname());
        teacher.setEmail(teacherDTO.getEmail());
        teacher.setPhoneNo(teacherDTO.getPhoneNo());
        teacher.setPosition(teacherDTO.getPosition());
        teacher.setStartDate(teacherDTO.getStartDate());
        teacher.setEndDate(teacherDTO.getEndDate());
        if (teacherDTO.getCourse() != 0) {
            BishCourses course = coursesService.setTeacher(teacherDTO.getCourse(), teacher.getID());
            teacher.setCourseName(course.getName());
        }
        return teacher;
    }

    @Override
    public BishTeachers addTeacher(TeacherDTO teacherDTO) {
        BishTeachers teacher = teacherToDTO(new BishTeachers(), teacherDTO);
        return teacherRepo.save(teacher);
    }

    @Override
    public BishTeachers updateTeacherInfo(long id, TeacherDTO teacherDTO) {
        BishTeachers teacher = teacherToDTO(teacherRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher with id " + id + " has not found")), teacherDTO);
        return teacherRepo.save(teacher);
    }

    @Override
    public String deleteTeacherById(long id) {
        BishTeachers teacher = teacherRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher id " + id + " has not found"));
        teacherRepo.delete(teacher);
        return "Teacher with id " + id + " has successfully deleted";
    }

    @Override
    public Set<Object> simpleSearch(String nameOrPhone) {
        Set<Object> teachers = new HashSet<>();
        for (String item : nameOrPhone.split(" ")) {
            teachers.addAll(teacherRepo.findAllByNameContainingIgnoringCase(item));
            teachers.addAll(userService.getAllByName(item));

            teachers.addAll(teacherRepo.findAllBySurnameContainingIgnoringCase(item));
            teachers.addAll(userService.getAllBySurname(item));
        }
        teachers.addAll(teacherRepo.findAllByPhoneNoContaining(nameOrPhone));
        teachers.addAll(userService.getAllByPhoneNo(nameOrPhone));
        teachers.addAll(teacherRepo.findAllByEmailContainingIgnoringCase(nameOrPhone));
        teachers.addAll(userService.getAllByEmail(nameOrPhone));

        return teachers;
    }

    @Override
    public Page<WorkerDTO> advancedSearch(Pageable pageable, List<Long> courseList) {
        List<WorkerDTO> workers = new ArrayList<>();
        if (courseList != null) {
            for (long courseID : courseList) {
                BishTeachers teacher = coursesService.findCourseById(courseID).getTeacher();
                workers.add(new WorkerDTO(teacher.getName(), teacher.getSurname(), teacher.getEmail(), teacher.getPhoneNo(), teacher.getPosition(), teacher.getCourseName()));
            }
            final int start = (int)pageable.getOffset();
            final int end = Math.min((start + pageable.getPageSize()), workers.size());
            return new PageImpl<>(workers.subList(start, end), pageable, workers.size());
        } else {
            return this.getAllWorkers(pageable);
        }

    }
}
