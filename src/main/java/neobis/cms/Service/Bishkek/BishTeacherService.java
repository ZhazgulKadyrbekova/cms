package neobis.cms.Service.Bishkek;

import neobis.cms.Dto.TeacherDTO;
import neobis.cms.Entity.Bishkek.BishTeachers;

import java.util.List;

public interface BishTeacherService {
    List<BishTeachers> getAllTeachers();
    BishTeachers getTeacherById(long id);
    List<BishTeachers> getTeachersByName(String name);
    BishTeachers addTeacher(TeacherDTO teacherDTO);
    BishTeachers updateTeacherInfo(long id, TeacherDTO teacherDTO);
    String deleteTeacherById(long id);
}
