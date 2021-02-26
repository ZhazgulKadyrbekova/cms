package neobis.cms.Service.Bishkek;

import neobis.cms.Dto.StudentDTO;
import neobis.cms.Dto.StudentShowDTO;

import java.util.List;

public interface BishStudentService {
    List<StudentShowDTO> getAll();
    StudentShowDTO create(StudentDTO studentDTO);
    StudentShowDTO getStudentByClientID(long id);
}
