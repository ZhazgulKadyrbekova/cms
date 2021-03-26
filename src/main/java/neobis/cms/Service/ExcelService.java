package neobis.cms.Service;

import neobis.cms.Entity.Bishkek.BishClient;
import neobis.cms.Entity.Osh.OshClient;
import neobis.cms.Repo.Bishkek.BishClientRepo;
import neobis.cms.Repo.Osh.OshClientRepo;
import neobis.cms.Util.ExcelUtilHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.stereotype.Service;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

@Service
public class ExcelService {
    @Autowired
    private BishClientRepo bishClientRepo;
    @Autowired
    private OshClientRepo oshClientRepo;

    public ByteArrayInputStream loadBishClients() {
        List<BishClient> clients = bishClientRepo.findAll();

        ByteArrayInputStream in = ExcelUtilHelper.bishClientsToExcel(clients);
        return in;
    }

    public ByteArrayInputStream loadOshClients() {
        List<OshClient> clients = oshClientRepo.findAll();

        ByteArrayInputStream in = ExcelUtilHelper.oshClientsToExcel(clients);
        return in;
    }

    public void saveBishClients(MultipartFile file) {
        try {
            List<BishClient> clients = ExcelUtilHelper.excelToBishClients(file.getInputStream());
            bishClientRepo.saveAll(clients);
        } catch (IOException e) {
            throw new RuntimeException("fail to store excel data: " + e.getMessage());
        }
    }

    public void saveOshClients(MultipartFile file) {
        try {
            List<OshClient> clients = ExcelUtilHelper.excelToOshClients(file.getInputStream());
            oshClientRepo.saveAll(clients);
        } catch (IOException e) {
            throw new RuntimeException("fail to store excel data: " + e.getMessage());
        }
    }
}
