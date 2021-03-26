package neobis.cms.Util;

import neobis.cms.Entity.Bishkek.BishClient;
import neobis.cms.Entity.Bishkek.BishOccupation;
import neobis.cms.Entity.Osh.OshClient;
import neobis.cms.Entity.Osh.OshOccupation;
import neobis.cms.Repo.Bishkek.BishLeavingReasonRepo;
import neobis.cms.Repo.Bishkek.BishOccupationRepo;
import neobis.cms.Repo.Bishkek.BishStatusesRepo;
import neobis.cms.Repo.Bishkek.BishUTMRepo;
import neobis.cms.Repo.Osh.OshLeavingReasonRepo;
import neobis.cms.Repo.Osh.OshOccupationRepo;
import neobis.cms.Repo.Osh.OshStatusesRepo;
import neobis.cms.Repo.Osh.OshUTMRepo;
import neobis.cms.Service.Bishkek.BishCoursesService;
import neobis.cms.Service.Osh.OshCoursesService;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class ExcelUtilHelper {
    public static String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    static String[] HEADERS = { "ID", "Date Created", "Date Updated", "Phone Number", "Name", "Surname", "Email", "Status", "Occupation",
            "Target", "Experience", "Laptop", "Course", "UTM", "Description", "City", "Form Name", "Timer", "Prepayment", "Leaving Reason"};
    static String SHEET = "Bishkek Clients";

    static private BishStatusesRepo bishStatusesRepo;
    static private OshStatusesRepo oshStatusesRepo;
    static private BishOccupationRepo bishOccupationRepo;
    static private OshOccupationRepo oshOccupationRepo;
    static private BishCoursesService bishCoursesService;
    static private OshCoursesService oshCoursesService;
    static private BishUTMRepo bishUTMRepo;
    static private OshUTMRepo oshUTMRepo;
    static private BishLeavingReasonRepo bishLeavingReasonRepo;
    static private OshLeavingReasonRepo oshLeavingReasonRepo;

    public ExcelUtilHelper(BishStatusesRepo bishStatusesRepo, OshStatusesRepo oshStatusesRepo, BishOccupationRepo bishOccupationRepo,
        OshOccupationRepo oshOccupationRepo, BishCoursesService bishCoursesService, OshCoursesService oshCoursesService, BishUTMRepo 
        bishUTMRepo, OshUTMRepo oshUTMRepo, BishLeavingReasonRepo bishLeavingReasonRepo, OshLeavingReasonRepo oshLeavingReasonRepo) {
        this.bishStatusesRepo = bishStatusesRepo;
        this.oshStatusesRepo = oshStatusesRepo;
        this.bishOccupationRepo = bishOccupationRepo;
        this.oshOccupationRepo = oshOccupationRepo;
        this.bishCoursesService = bishCoursesService;
        this.oshCoursesService = oshCoursesService;
        this.bishUTMRepo = bishUTMRepo;
        this.oshUTMRepo = oshUTMRepo;
        this.bishLeavingReasonRepo = bishLeavingReasonRepo;
        this.oshLeavingReasonRepo = oshLeavingReasonRepo;
    }

    public static ByteArrayInputStream bishClientsToExcel(List<BishClient> clients) {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream();) {
            Sheet sheet = workbook.createSheet(SHEET);

            Row headerRow = sheet.createRow(0);

            for (int col = 0; col < HEADERS.length; col++) {
                Cell cell = headerRow.createCell(col);
                cell.setCellValue(HEADERS[col]);
            }

            int rowIdx = 1;

            for (BishClient client : clients) {
                Row row = sheet.createRow(rowIdx++);

                row.createCell(0).setCellValue(client.getClient_id());
                row.createCell(1).setCellValue(client.getDateCreated() != null ? client.getDateCreated().toString() : null);
                row.createCell(2).setCellValue(client.getDateUpdated() != null ? client.getDateUpdated().toString() : null);
                row.createCell(3).setCellValue(client.getPhoneNo() != null ? client.getPhoneNo() : null);
                row.createCell(4).setCellValue(client.getName());
                row.createCell(5).setCellValue(client.getSurname());
                row.createCell(6).setCellValue(client.getEmail());
                row.createCell(7).setCellValue(client.getStatus() != null ? client.getStatus().getName() : null);
                row.createCell(8).setCellValue(client.getOccupation() != null ? client.getOccupation().getName() : null);
                row.createCell(9).setCellValue(client.getTarget());
                row.createCell(10).setCellValue(client.isExperience());
                row.createCell(11).setCellValue(client.isLaptop());
                row.createCell(12).setCellValue(client.getCourse() != null ? client.getCourse().getName() : null);
                row.createCell(13).setCellValue(client.getUtm() != null ? client.getUtm().getName() : null);
                row.createCell(14).setCellValue(client.getDescription());
                row.createCell(15).setCellValue(client.getCity());
                row.createCell(16).setCellValue(client.getFormName());
                row.createCell(17).setCellValue(client.getTimer() != null ? client.getTimer().toString() : null);
                row.createCell(18).setCellValue(client.getPrepayment() != null ? client.getPrepayment().toString() : null);
                row.createCell(19).setCellValue(client.getLeavingReason() != null ? client.getLeavingReason().getName() : null);
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("fail to import data to Excel file: " + e.getMessage());
        }
    }

    public static ByteArrayInputStream oshClientsToExcel(List<OshClient> clients) {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream();) {
            Sheet sheet = workbook.createSheet(SHEET);

            Row headerRow = sheet.createRow(0);

            for (int col = 0; col < HEADERS.length; col++) {
                Cell cell = headerRow.createCell(col);
                cell.setCellValue(HEADERS[col]);
            }

            int rowIdx = 1;

            for (OshClient client : clients) {
                Row row = sheet.createRow(rowIdx++);

                row.createCell(0).setCellValue(client.getClient_id());
                row.createCell(1).setCellValue(client.getDateCreated().toString());
                row.createCell(2).setCellValue(client.getDateUpdated() != null ? client.getDateUpdated().toString() : null);
                row.createCell(3).setCellValue(client.getPhoneNo() != null ? client.getPhoneNo() : null);
                row.createCell(4).setCellValue(client.getName());
                row.createCell(5).setCellValue(client.getSurname());
                row.createCell(6).setCellValue(client.getEmail());
                row.createCell(7).setCellValue(client.getStatus() != null ? client.getStatus().getName() : null);
                row.createCell(8).setCellValue(client.getOccupation() != null ? client.getOccupation().getName() : null);
                row.createCell(9).setCellValue(client.getTarget());
                row.createCell(10).setCellValue(client.isExperience());
                row.createCell(11).setCellValue(client.isLaptop());
                row.createCell(12).setCellValue(client.getCourse() != null ? client.getCourse().getName() : null);
                row.createCell(13).setCellValue(client.getUtm() != null ? client.getUtm().getName() : null);
                row.createCell(14).setCellValue(client.getDescription());
                row.createCell(15).setCellValue(client.getCity());
                row.createCell(16).setCellValue(client.getFormName());
                row.createCell(17).setCellValue(client.getTimer() != null ? client.getTimer().toString() : null);
                row.createCell(18).setCellValue(client.getPrepayment() != null ? client.getPrepayment().toString() : null);
                row.createCell(19).setCellValue(client.getLeavingReason() != null ? client.getLeavingReason().getName() : null);
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("fail to import data to Excel file: " + e.getMessage());
        }
    }

    public static boolean hasExcelFormat(MultipartFile file) {
        if (!TYPE.equals(file.getContentType())) {
            return false;
        }
        return true;
    }

    public static List<BishClient> excelToBishClients(InputStream is) {
        try {
            Workbook workbook = new XSSFWorkbook(is);

            Sheet sheet = workbook.getSheet(SHEET);
            Iterator<Row> rows = sheet.iterator();
            List<BishClient> clients = new ArrayList<BishClient>();

            int rowNumber = 0;
            while (rows.hasNext()) {
                Row currentRow = rows.next();

                if (rowNumber == 0) {
                    rowNumber++;
                    continue;
                }
                Iterator<Cell> cellsInRow = currentRow.iterator();

                BishClient bishClient = new BishClient();

                int cellIDx = 0;
                while (cellsInRow.hasNext()) {
                    Cell currentCell = cellsInRow.next();

                    switch (cellIDx) {
                        case 0:
//                            bishClient.setClient_id((long) currentCell.getNumericCellValue());
                            break;
                        case 1:
//                            bishClient.setDateCreated(LocalDateTime.parse(currentCell.getStringCellValue()));
                            break;
                        case 2:
//                            bishClient.setDateUpdated(currentCell.getStringCellValue().isEmpty() ? null :
//                                    LocalDateTime.parse(currentCell.getStringCellValue()));
                            break;
                        case 3:
                            bishClient.setPhoneNo(currentCell.getStringCellValue());
                            break;
                        case 4:
                            bishClient.setName(currentCell.getStringCellValue());
                            break;
                        case 5:
                            bishClient.setSurname(currentCell.getStringCellValue());
                            break;
                        case 6:
                            bishClient.setEmail(currentCell.getStringCellValue());
                            break;
                        case 7:
                            System.out.println("status\t\t" + currentCell.getStringCellValue());
                            if (!currentCell.getStringCellValue().isEmpty() && !currentCell.getStringCellValue().equals("") && currentCell.getStringCellValue() != null)
                            bishClient.setStatus(bishStatusesRepo.findByNameContainingIgnoringCase(currentCell.getStringCellValue()));
                            break;
                        case 8:
                            System.out.println("occupation\t\t" + currentCell.getStringCellValue());
                            if (currentCell.getStringCellValue() != null && !currentCell.getStringCellValue().isEmpty() && !currentCell.getStringCellValue().equals("")) {
                                String occupationName = currentCell.getStringCellValue();
                                System.out.println("occupationName\t\t" + occupationName);
                                System.out.println("all occupations\t\t" + bishOccupationRepo.findAllByNameContainingIgnoringCase("Студент").toString());
                                BishOccupation occupation = bishOccupationRepo.findByNameContainingIgnoringCase(occupationName);
                                System.out.println("occupation object\t\t" + occupation.toString());
                                bishClient.setOccupation(occupation);
                            }
                            break;
                        case 9:
                            bishClient.setTarget(currentCell.getStringCellValue());
                            break;
                        case 10:
                            bishClient.setExperience(currentCell.getBooleanCellValue());
                            break;
                        case 11:
                            bishClient.setLaptop(currentCell.getBooleanCellValue());
                            break;
                        case 12:
                            System.out.println("course\t\t" + currentCell.getStringCellValue());
                            if (currentCell.getStringCellValue() != null && !currentCell.getStringCellValue().isEmpty() && !currentCell.getStringCellValue().equals(""))
                                bishClient.setCourse(bishCoursesService.findCourseByName(currentCell.getStringCellValue()));
                            break;
                        case 13:
                            System.out.println("utm\t\t" + currentCell.getStringCellValue());
                            if (currentCell.getStringCellValue() != null && !currentCell.getStringCellValue().isEmpty() && !currentCell.getStringCellValue().equals(""))
                                bishClient.setUtm(bishUTMRepo.findByNameContainingIgnoringCase(currentCell.getStringCellValue()).orElse(null));
                            break;
                        case 14:
                            bishClient.setDescription(currentCell.getStringCellValue());
                            break;
                        case 15:
                            bishClient.setCity("OSH");
                            break;
                        case 16:
                            bishClient.setFormName(currentCell.getStringCellValue());
                            break;
                        case 17:
                            bishClient.setTimer(currentCell.getStringCellValue().isEmpty() ? null :
                                    LocalDateTime.parse(currentCell.getStringCellValue()));
                            break;
                        case 18:
                            System.out.println("prepayment\t\t" + currentCell.getStringCellValue());
                            if (currentCell.getStringCellValue() != null && !currentCell.getStringCellValue().isEmpty() && !currentCell.getStringCellValue().equals(""))
                                bishClient.setPrepayment(BigDecimal.valueOf(Integer.parseInt(currentCell.getStringCellValue())));
                            break;
                        case 19:
                            System.out.println("leaving reason\t\t" + currentCell.getStringCellValue());
                            if (currentCell.getStringCellValue() != null && !currentCell.getStringCellValue().isEmpty() && !currentCell.getStringCellValue().equals(""))
                                bishClient.setLeavingReason(currentCell.getStringCellValue().isEmpty() ? null :
                                    bishLeavingReasonRepo.findByNameContainingIgnoringCase(currentCell.getStringCellValue()).orElse(null));
                            break;
                        default:
//                            throw new IllegalArgumentException("Unrecognized data " + currentCell.getStringCellValue());
                            break;
                    }
                    cellIDx++;
                }
                clients.add(bishClient);
            }
            workbook.close();
            return clients;
        } catch (IOException e) {
            throw new RuntimeException("fail to parse Excel file: " + e.getMessage());
        }
    }
    
    public static List<OshClient> excelToOshClients(InputStream is) {
        try {
            Workbook workbook = new XSSFWorkbook(is);

            Sheet sheet = workbook.getSheet(SHEET);
            Iterator<Row> rows = sheet.iterator();
            List<OshClient> clients = new ArrayList<OshClient>();

            int rowNumber = 0;
            while (rows.hasNext()) {
                Row currentRow = rows.next();

                if (rowNumber == 0) {
                    rowNumber++;
                    continue;
                }
                Iterator<Cell> cellsInRow = currentRow.iterator();

                OshClient client = new OshClient();

                int cellIDx = 0;
                while (cellsInRow.hasNext()) {
                    Cell currentCell = cellsInRow.next();

                    switch (cellIDx) {
                        case 0:
//                            client.setClient_id((long) currentCell.getNumericCellValue());
                            break;
                        case 1:
//                            client.setDateCreated(LocalDateTime.parse(currentCell.getStringCellValue()));
                            break;
                        case 2:
//                            client.setDateUpdated(LocalDateTime.parse(currentCell.getStringCellValue()));
                            break;
                        case 3:
                            client.setPhoneNo(currentCell.getStringCellValue());
                            break;
                        case 4:
                            client.setName(currentCell.getStringCellValue());
                            break;
                        case 5:
                            client.setSurname(currentCell.getStringCellValue());
                            break;
                        case 6:
                            client.setEmail(currentCell.getStringCellValue());
                            break;
                        case 7:
                            if (!currentCell.getStringCellValue().isEmpty() && !currentCell.getStringCellValue().equals("") && currentCell.getStringCellValue() != null)
                                client.setStatus(oshStatusesRepo.findByNameContainingIgnoringCase(currentCell.getStringCellValue()));
                            break;
                        case 8:
                            System.out.println("occupation\t\t" + currentCell.getStringCellValue());
                            if (currentCell.getStringCellValue() != null && !currentCell.getStringCellValue().isEmpty() && !currentCell.getStringCellValue().equals("")) {
                                OshOccupation occupation = oshOccupationRepo.findByNameContainingIgnoringCase((String) currentCell.getStringCellValue());
                                client.setOccupation(occupation);
                                // client.setOccupation(oshOccupationRepo.findByNameContainingIgnoringCase(currentCell.getStringCellValue()).orElse(null));
                            }
                            break;
                        case 9:
                            client.setTarget(currentCell.getStringCellValue());
                            break;
                        case 10:
                            client.setExperience(currentCell.getBooleanCellValue());
                            break;
                        case 11:
                            client.setLaptop(currentCell.getBooleanCellValue());
                            break;
                        case 12:
                            System.out.println("Course\t\t" + currentCell.getStringCellValue());
                            if (currentCell.getStringCellValue() != null && !currentCell.getStringCellValue().isEmpty() && !currentCell.getStringCellValue().equals(""))
                                client.setCourse(oshCoursesService.findCourseByName(currentCell.getStringCellValue()));
                            break;
                        case 13:
                            System.out.println("UTM\t\t" + currentCell.getStringCellValue());
                            if (currentCell.getStringCellValue() != null && !currentCell.getStringCellValue().isEmpty() && !currentCell.getStringCellValue().equals(""))
                                client.setUtm(oshUTMRepo.findByNameContainingIgnoringCase(currentCell.getStringCellValue()).orElse(null));
                            break;
                        case 14:
                            client.setDescription(currentCell.getStringCellValue());
                            break;
                        case 15:
                            client.setCity("OSH");
                            break;
                        case 16:
                            client.setFormName(currentCell.getStringCellValue());
                            break;
                        case 17:
                            client.setTimer(currentCell.getStringCellValue().isEmpty() ? null :
                                    LocalDateTime.parse(currentCell.getStringCellValue()));
                            break;
                        case 18:
                            System.out.println("Prepayment\t\t" + currentCell.getStringCellValue());
                            if (currentCell.getStringCellValue() != null && !currentCell.getStringCellValue().isEmpty() && !currentCell.getStringCellValue().equals(""))
                                client.setPrepayment(BigDecimal.valueOf(Integer.parseInt(currentCell.getStringCellValue())));
                            break;
                        case 19:
                            System.out.println("LeavingReaso\t\t" + currentCell.getStringCellValue());
                            if (currentCell.getStringCellValue() != null && !currentCell.getStringCellValue().isEmpty() && !currentCell.getStringCellValue().equals(""))
                                client.setLeavingReason(oshLeavingReasonRepo.findByNameContainingIgnoringCase(currentCell.getStringCellValue()).orElse(null));
                            break;
                        default:
//                            throw new IllegalArgumentException("Unrecognized data " + currentCell.getStringCellValue());
                            break;
                    }
                    cellIDx++;
                }
                clients.add(client);
            }
            workbook.close();
            return clients;
        } catch (IOException e) {
            throw new RuntimeException("fail to parse Excel file: " + e.getMessage());
        }
    }
}
