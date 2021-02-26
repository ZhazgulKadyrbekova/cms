package neobis.cms.Service.Osh;

import neobis.cms.Dto.TeacherDTO;
import neobis.cms.Entity.Osh.OshTeachers;

import java.util.List;

public interface OshTeacherService {
    List<OshTeachers> getAllTeachers();
    OshTeachers getTeacherById(long id);
    OshTeachers getTeacherByName(String name);
    OshTeachers addTeacher(TeacherDTO teacherDTO);
    OshTeachers updateTeacherInfo(long id, TeacherDTO teacherDTO);
    String deleteTeacherById(long id);
}
