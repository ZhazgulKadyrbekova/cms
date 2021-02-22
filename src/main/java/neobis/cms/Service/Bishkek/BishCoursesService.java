package neobis.cms.Service.Bishkek;

import neobis.cms.Entity.Bishkek.BishCourses;

public interface BishCoursesService {
    BishCourses findCourseByFormName(String formName);
    BishCourses findCourseById(long id);
}
