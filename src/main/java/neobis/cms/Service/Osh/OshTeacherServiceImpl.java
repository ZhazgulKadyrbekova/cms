package neobis.cms.Service.Osh;

import neobis.cms.Dto.TeacherDTO;
import neobis.cms.Dto.WorkerDTO;
import neobis.cms.Entity.Bishkek.BishPosition;
import neobis.cms.Entity.Bishkek.User;
import neobis.cms.Entity.Osh.OshCourses;
import neobis.cms.Entity.Osh.OshPosition;
import neobis.cms.Entity.Osh.OshTeachers;
import neobis.cms.Exception.IllegalArgumentException;
import neobis.cms.Exception.ResourceNotFoundException;
import neobis.cms.Repo.Bishkek.BishPositionRepo;
import neobis.cms.Repo.Osh.OshPositionRepo;
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
    private final BishPositionRepo bishPositionRepo;
    private final OshPositionRepo oshPositionRepo;

    public OshTeacherServiceImpl(OshTeacherRepo teacherRepo, UserService userService, @Lazy OshCoursesService
            coursesService, BishPositionRepo bishPositionRepo, OshPositionRepo oshPositionRepo) {
        this.teacherRepo = teacherRepo;
        this.userService = userService;
        this.coursesService = coursesService;
        this.bishPositionRepo = bishPositionRepo;
        this.oshPositionRepo = oshPositionRepo;
    }

    private List<WorkerDTO> toWorkers(List<OshTeachers> teachers, List<User> users) {
        List<WorkerDTO> workers = new ArrayList<>();

        for (OshTeachers teacher : teachers) {
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
            OshPosition position = teacher.getPosition();
            if (position != null)
                worker.setPosition(position.getName());
            OshCourses course = coursesService.findCourseByTeacher(teacher);
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

    private Set<WorkerDTO> toWorkers(Set<OshTeachers> teachers, Set<User> users) {
        Set<WorkerDTO> workers = new HashSet<>();

        for (OshTeachers teacher : teachers) {
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
            OshPosition position = teacher.getPosition();
            if (position != null)
                worker.setPosition(position.getName());
            OshCourses course = coursesService.findCourseByTeacher(teacher);
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

        if (positionID != null && courseID != null) {
            for (long course : courseID) {
                OshTeachers teacher = coursesService.findCourseById(course).getTeacher();
                for (long position : positionID) {
                    if (teacher.getPosition().getID() == position)
                        teachers.add(teacher);
                }
            }
        } else if (courseID != null) {
            for (long course : courseID) {
                teachers.add(coursesService.findCourseById(course).getTeacher());
            }
        } else if (positionID != null) {
            for (long position : positionID) {
                OshPosition oshPosition = oshPositionRepo.findById(position).orElseThrow(() ->
                                new ResourceNotFoundException("Должность с идентификатором " + position + " не найдена."));
                teachers.addAll(teacherRepo.findAllByPosition(oshPosition));

                users.addAll(userService.getAllByPositionAndCity(bishPositionRepo
                        .findByNameContainingIgnoringCase(oshPosition.getName()), "osh"));
            }
        }
        return toWorkers(teachers, users);
    }

    @Override
    public Page<WorkerDTO> getAllWorkers(Pageable pageable) {
        List<OshTeachers> teachers = teacherRepo.findAllByOrderByIDAsc();
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
                .orElseThrow(() -> new ResourceNotFoundException("Преподаватель с идентификатором " + id + " не найден."));
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

    private OshTeachers DTOToTeacher(OshTeachers teacher, TeacherDTO teacherDTO) {
        teacher.setName(teacherDTO.getName());
        teacher.setSurname(teacherDTO.getSurname());
        teacher.setPatronymic(teacherDTO.getPatronymic());
        if (!teacherRepo.findAllByEmailIgnoringCase(teacherDTO.getEmail()).isEmpty()) {
            throw new IllegalArgumentException("Преподаватель с электронной почтой " + teacherDTO.getEmail() + " уже имеется.");
        }
        teacher.setEmail(teacherDTO.getEmail());
        teacher.setPhoneNo(teacherDTO.getPhoneNo());
        teacher.setPatent(teacherDTO.getPatent());
        teacher.setStartDate(teacherDTO.getStartDate());
        teacher.setEndDate(teacherDTO.getEndDate());
        teacher.setDescription(teacherDTO.getDescription());
        if (teacherDTO.getPosition() != 0) {
            OshPosition position = oshPositionRepo.findById(teacherDTO.getPosition()).orElseThrow(() ->
                            new ResourceNotFoundException("Должность с идентификатором " + teacherDTO.getPosition() + " не найдена."));
            teacher.setPosition(position);
        }
        return teacher;
    }

    @Override
    public OshTeachers addTeacher(TeacherDTO teacherDTO) {
        OshTeachers teacher = DTOToTeacher(new OshTeachers(), teacherDTO);
        teacher = teacherRepo.save(teacher);
        OshCourses courses = coursesService.findCourseById(teacherDTO.getCourse());
        courses.setTeacher(teacher);
        coursesService.save(courses);
        return teacher;
    }

    @Override
    public OshTeachers updateTeacherInfo(long id, TeacherDTO teacherDTO) {
        OshTeachers teacher = DTOToTeacher(getTeacherById(id), teacherDTO);
        teacher = teacherRepo.save(teacher);
        OshCourses courses = coursesService.findCourseById(teacherDTO.getCourse());
        courses.setTeacher(teacher);
        coursesService.save(courses);
        return teacher;
    }

    @Override
    public String deleteTeacherById(long id) {
        OshTeachers teacher = getTeacherById(id);
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
