package neobis.cms.Service.Bishkek;

import neobis.cms.Dto.TeacherDTO;
import neobis.cms.Dto.WorkerDTO;
import neobis.cms.Entity.Bishkek.BishCourses;
import neobis.cms.Entity.Bishkek.BishPosition;
import neobis.cms.Entity.Bishkek.BishTeachers;
import neobis.cms.Entity.Bishkek.User;
import neobis.cms.Exception.ResourceNotFoundException;
import neobis.cms.Repo.Bishkek.BishPositionRepo;
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
    private final BishPositionRepo positionRepo;

    public BishTeacherServiceImpl(BishTeacherRepo teacherRepo, UserService userService, @Lazy BishCoursesService
            coursesService, BishPositionRepo positionRepo) {
        this.teacherRepo = teacherRepo;
        this.userService = userService;
        this.coursesService = coursesService;
        this.positionRepo = positionRepo;
    }

    private List<WorkerDTO> toWorkers(List<BishTeachers> teachers, List<User> users) {
        List<WorkerDTO> workers = new ArrayList<>();

        for (BishTeachers teacher : teachers) {
            WorkerDTO worker = new WorkerDTO();
            worker.setWorkerID(teacher.getID());
            worker.setName(teacher.getName());
            worker.setSurname(teacher.getSurname());
            worker.setPatronymic(teacher.getPatronymic());
            worker.setEmail(teacher.getEmail());
            worker.setPhoneNo(teacher.getPhoneNo());
            worker.setTable("Teacher");
            worker.setPatent(teacher.getPatent());
            worker.setStartDate(teacher.getStartDate());
            worker.setEndDate(teacher.getEndDate());
            BishPosition position = teacher.getPosition();
            if (position != null)
                worker.setPosition(position.getName());
            BishCourses course = teacher.getCourse();
            if (course != null)
                worker.setCourseName(course.getName());
            workers.add(worker);
        }

        for (User user : users) {
            WorkerDTO worker = new WorkerDTO();
            worker.setWorkerID(user.getID());
            worker.setName(user.getName());
            worker.setSurname(user.getSurname());
            worker.setPatronymic(user.getPatronymic());
            worker.setEmail(user.getEmail());
            worker.setPhoneNo(user.getPhoneNo());
            worker.setTable("User");
            BishPosition position = user.getPosition();
            if (position != null)
                worker.setPosition(position.getName());
            workers.add(worker);
        }
        return workers;
    }

    private Set<WorkerDTO> toWorkers(Set<BishTeachers> teachers, Set<User> users) {
        Set<WorkerDTO> workers = new HashSet<>();

        for (BishTeachers teacher : teachers) {
            WorkerDTO worker = new WorkerDTO();
            worker.setWorkerID(teacher.getID());
            worker.setName(teacher.getName());
            worker.setSurname(teacher.getSurname());
            worker.setPatronymic(teacher.getPatronymic());
            worker.setEmail(teacher.getEmail());
            worker.setPhoneNo(teacher.getPhoneNo());
            worker.setTable("Teacher");
            worker.setPatent(teacher.getPatent());
            worker.setStartDate(teacher.getStartDate());
            worker.setEndDate(teacher.getEndDate());
            BishPosition position = teacher.getPosition();
            if (position != null)
                worker.setPosition(position.getName());
            BishCourses course = teacher.getCourse();
            if (course != null)
                worker.setCourseName(course.getName());
            workers.add(worker);
        }

        for (User user : users) {
            WorkerDTO worker = new WorkerDTO();
            worker.setWorkerID(user.getID());
            worker.setName(user.getName());
            worker.setSurname(user.getSurname());
            worker.setPatronymic(user.getPatronymic());
            worker.setEmail(user.getEmail());
            worker.setPhoneNo(user.getPhoneNo());
            worker.setTable("User");
            BishPosition position = user.getPosition();
            if (position != null)
                worker.setPosition(position.getName());
            workers.add(worker);
        }

        return workers;
    }

    @Override
    public Set<WorkerDTO> getWithPredicate(String field, List<Long> positionID, List<Long> courseID) {
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

        if (positionID != null && courseID != null) {
            for (long course : courseID) {
                BishTeachers teacher = coursesService.findCourseById(course).getTeacher();
                for (long position : positionID) {
                    if (teacher.getPosition().getID() == position)
                        teachers.add(teacher);
                }
            }
        } else if (courseID != null) {
            for (long course : courseID)
                teachers.add(coursesService.findCourseById(course).getTeacher());
        } else if (positionID != null) {
            for (long position : positionID) {
                BishPosition bishPosition = positionRepo.findById(position).orElseThrow(() ->
                        new ResourceNotFoundException("Position with ID " + position + " has not found"));
                teachers.addAll(teacherRepo.findAllByPosition(bishPosition));
                users.addAll(userService.getAllByPositionAndCity(bishPosition, "bishkek"));
            }
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

    private BishTeachers DTOToTeacher(BishTeachers teacher, TeacherDTO teacherDTO) {
        teacher.setName(teacherDTO.getName());
        teacher.setSurname(teacherDTO.getSurname());
        teacher.setPatronymic(teacherDTO.getPatronymic());
        teacher.setEmail(teacherDTO.getEmail());
        teacher.setPhoneNo(teacherDTO.getPhoneNo());
        teacher.setStartDate(teacherDTO.getStartDate());
        teacher.setEndDate(teacherDTO.getEndDate());
        teacher.setDescription(teacherDTO.getDescription());
        if (teacherDTO.getPosition() != 0) {
            BishPosition position = positionRepo.findById(teacherDTO.getPosition()).orElseThrow(() ->
                            new ResourceNotFoundException("Position with ID " + teacherDTO.getPosition() + " has not found"));
            teacher.setPosition(position);
        }
        if (teacherDTO.getCourse() != 0) {
            BishCourses course = coursesService.findCourseById(teacherDTO.getCourse());
            teacher.setCourse(course);
        }
        return teacher;
    }

    @Override
    public BishTeachers addTeacher(TeacherDTO teacherDTO) {
        BishTeachers teacher = DTOToTeacher(new BishTeachers(), teacherDTO);
        return teacherRepo.save(teacher);
    }

    @Override
    public BishTeachers updateTeacherInfo(long id, TeacherDTO teacherDTO) {
        BishTeachers teacher = DTOToTeacher(teacherRepo.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Teacher with id " + id + " has not found")), teacherDTO);
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
}
