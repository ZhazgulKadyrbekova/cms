package neobis.cms.Service.Osh;

import neobis.cms.Dto.TeacherDTO;
import neobis.cms.Dto.WorkerDTO;
import neobis.cms.Entity.Osh.OshTeachers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;

public interface OshTeacherService {
    Set<WorkerDTO> getWithPredicate(String field, List<Long> positionID, List<Long> courseID);
    Page<WorkerDTO> getAllWorkers(Pageable pageable);
    List<OshTeachers> getAllTeachers();
    List<OshTeachers> getAllByName(String name);
    OshTeachers getTeacherById(long id);
    OshTeachers getTeacherByName(String name);
    OshTeachers addTeacher(TeacherDTO teacherDTO);
    OshTeachers updateTeacherInfo(long id, TeacherDTO teacherDTO);
    String deleteTeacherById(long id);
    Set<Object> simpleSearch(String nameOrPhone);
}
