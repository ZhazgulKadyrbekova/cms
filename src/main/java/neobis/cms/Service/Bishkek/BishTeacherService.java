package neobis.cms.Service.Bishkek;

import neobis.cms.Dto.TeacherDTO;
import neobis.cms.Dto.WorkerDTO;
import neobis.cms.Entity.Bishkek.BishTeachers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;

public interface BishTeacherService {
    Set<WorkerDTO> getWithPredicate(String field, String position, List<Long> courseID);
    Page<WorkerDTO> getAllWorkers(Pageable pageable);
    List<BishTeachers> getAllTeachers();
    BishTeachers getTeacherById(long id);
    List<BishTeachers> getTeachersByName(String name);
    BishTeachers addTeacher(TeacherDTO teacherDTO);
    BishTeachers updateTeacherInfo(long id, TeacherDTO teacherDTO);
    String deleteTeacherById(long id);
    Set<Object> simpleSearch(String nameOrPhone);
    Page<WorkerDTO> advancedSearch(Pageable pageable, List<Long> course);
}
