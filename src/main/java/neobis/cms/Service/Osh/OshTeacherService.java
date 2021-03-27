package neobis.cms.Service.Osh;

import neobis.cms.Dto.TeacherDTO;
import neobis.cms.Dto.WorkerDTO;
import neobis.cms.Entity.Osh.OshTeachers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OshTeacherService {
    Page<WorkerDTO> getWithPredicate(Pageable pageable, String position, Long courseID);
    Page<WorkerDTO> getAll(Pageable pageable);
    List<OshTeachers> getAllTeachers();
    List<OshTeachers> getAllByName(String name);
    OshTeachers getTeacherById(long id);
    OshTeachers getTeacherByName(String name);
    OshTeachers addTeacher(TeacherDTO teacherDTO);
    OshTeachers updateTeacherInfo(long id, TeacherDTO teacherDTO);
    String deleteTeacherById(long id);
}
