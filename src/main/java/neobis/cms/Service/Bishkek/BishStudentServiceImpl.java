package neobis.cms.Service.Bishkek;

import neobis.cms.Dto.StudentDTO;
import neobis.cms.Dto.StudentShowDTO;
import neobis.cms.Entity.Bishkek.BishClient;
import neobis.cms.Repo.Bishkek.BishClientRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BishStudentServiceImpl implements BishStudentService{

    @Autowired
    private BishClientService clientService;

    @Autowired
    private BishPaymentService paymentService;

    @Override
    public List<StudentShowDTO> getAll() {
        List<BishClient> clients = clientService.getAllByStatus("student");
        List<StudentShowDTO> students = new ArrayList<>();
        for (BishClient client : clients) {
            StudentShowDTO student = new StudentShowDTO(client.getName(), client.getPhoneNo(), paymentService.getAllByClient(client.getClient_id()), client.getPrepayment());
            students.add(student);
        }

        return students;
    }

    @Override
    public StudentShowDTO create(StudentDTO studentDTO) {
        BishClient client = new BishClient();
        client.setName(studentDTO.getName());
        return new StudentShowDTO(studentDTO.getName(), studentDTO.getPhoneNo(), new ArrayList<>(), studentDTO.getPrepayment());
    }

    @Override
    public StudentShowDTO getStudentByClientID(long id) {
        BishClient client = clientService.getClientById(id);
        return new StudentShowDTO(client.getName(), client.getPhoneNo(), new ArrayList<>(), client.getPrepayment());
    }
}
