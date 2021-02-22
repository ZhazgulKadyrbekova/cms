package neobis.cms.Service.Bishkek;

import neobis.cms.Entity.Bishkek.BishCourses;
import neobis.cms.Exception.ResourceNotFoundException;
import neobis.cms.Repo.Bishkek.BishCoursesRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BishCoursesServiceImpl implements BishCoursesService{
    @Autowired
    private BishCoursesRepo coursesRepo;
    @Override
    public BishCourses findCourseByFormName(String formName) {
        if (formName.equals("Заявка с quiz"))
            return null;
        formName = formName.substring(7);
        if (formName.contains("PM"))
            return coursesRepo.findByNameIgnoringCase("project manager");
        if (formName.contains("Java"))
            return coursesRepo.findByNameIgnoringCase("java");
        if (formName.contains("JS"))
            return coursesRepo.findByNameIgnoringCase("javascript");
        if (formName.contains("python"))
            return coursesRepo.findByNameIgnoringCase("python");
        if (formName.contains("design"))
            return coursesRepo.findByNameIgnoringCase("design");
        if (formName.contains("olympiad"))
            return coursesRepo.findByNameIgnoringCase("olympiad");
        return null;
    }

    @Override
    public BishCourses findCourseById(long id) {
        return coursesRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course id " + id + " not found"));
    }
}
