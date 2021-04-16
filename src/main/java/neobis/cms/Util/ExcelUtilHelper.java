package neobis.cms.Util;

import neobis.cms.Entity.Bishkek.*;
import neobis.cms.Entity.Osh.*;
import neobis.cms.Repo.Bishkek.*;
import neobis.cms.Repo.Osh.*;
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
    static private BishTargetRepo bishTargetRepo;
    static private OshTargetRepo oshTargetRepo;
    static private BishMethodRepo bishMethodRepo;
    static private OshMethodRepo oshMethodRepo;
    public ExcelUtilHelper(BishStatusesRepo bishStatusesRepo, OshStatusesRepo oshStatusesRepo, BishOccupationRepo bishOccupationRepo,
                           OshOccupationRepo oshOccupationRepo, BishCoursesService bishCoursesService, OshCoursesService oshCoursesService, BishUTMRepo
                                   bishUTMRepo, OshUTMRepo oshUTMRepo, BishLeavingReasonRepo bishLeavingReasonRepo, OshLeavingReasonRepo oshLeavingReasonRepo, BishTargetRepo bishTargetRepo, OshTargetRepo oshTargetRepo, BishMethodRepo bishMethodRepo, OshMethodRepo oshMethodRepo) {
        ExcelUtilHelper.bishStatusesRepo = bishStatusesRepo;
        ExcelUtilHelper.oshStatusesRepo = oshStatusesRepo;
        ExcelUtilHelper.bishOccupationRepo = bishOccupationRepo;
        ExcelUtilHelper.oshOccupationRepo = oshOccupationRepo;
        ExcelUtilHelper.bishCoursesService = bishCoursesService;
        ExcelUtilHelper.oshCoursesService = oshCoursesService;
        ExcelUtilHelper.bishUTMRepo = bishUTMRepo;
        ExcelUtilHelper.oshUTMRepo = oshUTMRepo;
        ExcelUtilHelper.bishLeavingReasonRepo = bishLeavingReasonRepo;
        ExcelUtilHelper.oshLeavingReasonRepo = oshLeavingReasonRepo;
        ExcelUtilHelper.bishTargetRepo = bishTargetRepo;
        ExcelUtilHelper.oshTargetRepo = oshTargetRepo;
        ExcelUtilHelper.bishMethodRepo = bishMethodRepo;
        ExcelUtilHelper.oshMethodRepo = oshMethodRepo;
    }

    public static ByteArrayInputStream bishClientsToExcel(List<BishClient> clients) {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet(SHEET);

            Row headerRow = sheet.createRow(0);

            for (int col = 0; col < HEADERS.length; col++) {
                Cell cell = headerRow.createCell(col);
                cell.setCellValue(HEADERS[col]);
            }

            int rowIdx = 1;

            for (BishClient client : clients) {
                Row row = sheet.createRow(rowIdx++);

                row.createCell(0).setCellValue(client.getClientID());
                row.createCell(1).setCellValue(client.getDateCreated() != null ? client.getDateCreated().toString() : null);
                row.createCell(2).setCellValue(client.getDateUpdated() != null ? client.getDateUpdated().toString() : null);
                row.createCell(3).setCellValue(client.getPhoneNo() != null ? client.getPhoneNo() : null);
                row.createCell(4).setCellValue(client.getName());
                row.createCell(5).setCellValue(client.getSurname());
                row.createCell(6).setCellValue(client.getPatronymic());
                row.createCell(7).setCellValue(client.getEmail());
                row.createCell(8).setCellValue(client.getStatus() != null ? client.getStatus().getName() : null);
                row.createCell(9).setCellValue(client.getOccupation() != null ? client.getOccupation().getName() : null);
                row.createCell(10).setCellValue(client.getTarget() != null ? client.getTarget().getName() : null);
                row.createCell(11).setCellValue(client.isExperience());
                row.createCell(12).setCellValue(client.isLaptop());
                row.createCell(13).setCellValue(client.getCourse() != null ? client.getCourse().getName() : null);
                row.createCell(14).setCellValue(client.getUtm() != null ? client.getUtm().getName() : null);
                row.createCell(15).setCellValue(client.getDescription());
                row.createCell(16).setCellValue(client.getCity());
                row.createCell(17).setCellValue(client.getFormName());
                row.createCell(18).setCellValue(client.getTimer() != null ? client.getTimer().toString() : null);
                row.createCell(19).setCellValue(client.getPrepayment() != null ? client.getPrepayment().toString() : null);
                row.createCell(20).setCellValue(client.getLeavingReason() != null ? client.getLeavingReason().getName() : null);
                int rowNo = 21;
                for (BishPayment payment : client.getPayments()) {
                    row.createCell(rowNo++).setCellValue(payment.toString());
                }
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("fail to import data to Excel file: " + e.getMessage());
        }
    }

    public static ByteArrayInputStream oshClientsToExcel(List<OshClient> clients) {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet(SHEET);

            Row headerRow = sheet.createRow(0);

            for (int col = 0; col < HEADERS.length; col++) {
                Cell cell = headerRow.createCell(col);
                cell.setCellValue(HEADERS[col]);
            }

            int rowIdx = 1;

            for (OshClient client : clients) {
                Row row = sheet.createRow(rowIdx++);

                row.createCell(0).setCellValue(client.getClientID());
                row.createCell(1).setCellValue(client.getDateCreated() != null ? client.getDateCreated() .toString() : null);
                row.createCell(2).setCellValue(client.getDateUpdated() != null ? client.getDateUpdated().toString() : null);
                row.createCell(3).setCellValue(client.getPhoneNo() != null ? client.getPhoneNo() : null);
                row.createCell(4).setCellValue(client.getName());
                row.createCell(5).setCellValue(client.getSurname());
                row.createCell(6).setCellValue(client.getPatronymic());
                row.createCell(7).setCellValue(client.getEmail());
                row.createCell(8).setCellValue(client.getStatus() != null ? client.getStatus().getName() : null);
                row.createCell(9).setCellValue(client.getOccupation() != null ? client.getOccupation().getName() : null);
                row.createCell(10).setCellValue(client.getTarget() != null ? client.getTarget().getName() : null);
                row.createCell(11).setCellValue(client.isExperience());
                row.createCell(12).setCellValue(client.isLaptop());
                row.createCell(13).setCellValue(client.getCourse() != null ? client.getCourse().getName() : null);
                row.createCell(14).setCellValue(client.getUtm() != null ? client.getUtm().getName() : null);
                row.createCell(15).setCellValue(client.getDescription());
                row.createCell(16).setCellValue(client.getCity());
                row.createCell(17).setCellValue(client.getFormName());
                row.createCell(18).setCellValue(client.getTimer() != null ? client.getTimer().toString() : null);
                row.createCell(19).setCellValue(client.getPrepayment() != null ? client.getPrepayment().toString() : null);
                row.createCell(20).setCellValue(client.getLeavingReason() != null ? client.getLeavingReason().getName() : null);
                int rowNo = 21;
                for (OshPayment payment : client.getPayments()) {
                    row.createCell(rowNo++).setCellValue(payment.toString());
                }
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("fail to import data to Excel file: " + e.getMessage());
        }
    }

    public static boolean hasExcelFormat(MultipartFile file) {
        return TYPE.equals(file.getContentType());
    }

    public static List<BishClient> excelToBishClients(InputStream is) {
        try {
            Workbook workbook = new XSSFWorkbook(is);

            Sheet sheet = workbook.getSheet(SHEET);
            Iterator<Row> rows = sheet.iterator();
            List<BishClient> clients = new ArrayList<>();

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
                    List<BishPayment> payments = new ArrayList<>();
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
                            bishClient.setPatronymic(currentCell.getStringCellValue());
                            break;
                        case 7:
                            bishClient.setEmail(currentCell.getStringCellValue());
                            break;
                        case 8:
//                            System.out.println("status\t\t" + currentCell.getStringCellValue());
                            if (!currentCell.getStringCellValue().isEmpty() && !currentCell.getStringCellValue().equals("") && currentCell.getStringCellValue() != null)
                            bishClient.setStatus(bishStatusesRepo.findByNameContainingIgnoringCase(currentCell.getStringCellValue()));
                            break;
                        case 9:
//                            System.out.println("occupation\t\t" + currentCell.getStringCellValue());
                            if (currentCell.getStringCellValue() != null && !currentCell.getStringCellValue().isEmpty() && !currentCell.getStringCellValue().equals("")) {
                                String occupationName = currentCell.getStringCellValue();
//                                System.out.println("occupationName\t\t" + occupationName);
//                                System.out.println("all occupations\t\t" + bishOccupationRepo.findAllByNameContainingIgnoringCase("Студент").toString());
                                BishOccupation occupation = bishOccupationRepo.findByNameContainingIgnoringCase(occupationName);
//                                System.out.println("occupation object\t\t" + occupation.toString());
                                bishClient.setOccupation(occupation);
                            }
                            break;
                        case 10:
                            if (currentCell.getStringCellValue() != null && !currentCell.getStringCellValue().isEmpty() && !currentCell.getStringCellValue().equals("")) {
                                String targetName = currentCell.getStringCellValue();
                                BishTarget target = bishTargetRepo.findByNameContainingIgnoringCase(targetName);
                                bishClient.setTarget(target);
                            }
                            break;
                        case 11:
                            bishClient.setExperience(currentCell.getBooleanCellValue());
                            break;
                        case 12:
                            bishClient.setLaptop(currentCell.getBooleanCellValue());
                            break;
                        case 13:
//                            System.out.println("course\t\t" + currentCell.getStringCellValue());
                            if (currentCell.getStringCellValue() != null && !currentCell.getStringCellValue().isEmpty() && !currentCell.getStringCellValue().equals(""))
                                bishClient.setCourse(bishCoursesService.findCourseByName(currentCell.getStringCellValue()));
                            break;
                        case 14:
//                            System.out.println("utm\t\t" + currentCell.getStringCellValue());
                            if (currentCell.getStringCellValue() != null && !currentCell.getStringCellValue().isEmpty() && !currentCell.getStringCellValue().equals(""))
                                bishClient.setUtm(bishUTMRepo.findByNameContainingIgnoringCase(currentCell.getStringCellValue()).orElse(null));
                            break;
                        case 15:
                            bishClient.setDescription(currentCell.getStringCellValue());
                            break;
                        case 16:
                            bishClient.setCity("OSH");
                            break;
                        case 17:
                            bishClient.setFormName(currentCell.getStringCellValue());
                            break;
                        case 18:
                            bishClient.setTimer(currentCell.getStringCellValue().isEmpty() ? null :
                                    LocalDateTime.parse(currentCell.getStringCellValue()));
                            break;
                        case 19:
//                            System.out.println("prepayment\t\t" + currentCell.getStringCellValue());
                            if (currentCell.getStringCellValue() != null && !currentCell.getStringCellValue().isEmpty() && !currentCell.getStringCellValue().equals(""))
                                bishClient.setPrepayment(BigDecimal.valueOf(Integer.parseInt(currentCell.getStringCellValue())));
                            break;
                        case 20:
//                            System.out.println("leaving reason\t\t" + currentCell.getStringCellValue());
                            if (currentCell.getStringCellValue() != null && !currentCell.getStringCellValue().isEmpty() && !currentCell.getStringCellValue().equals(""))
                                bishClient.setLeavingReason(currentCell.getStringCellValue().isEmpty() ? null :
                                    bishLeavingReasonRepo.findByNameContainingIgnoringCase(currentCell.getStringCellValue()).orElse(null));
                            break;
                        default:
                           if (currentCell.getStringCellValue() != null && !currentCell.getStringCellValue().isEmpty() && !currentCell.getStringCellValue().equals("")) {
                               String paymentText = currentCell.getStringCellValue();
                               BishPayment payment = new BishPayment();
                               for (String item : paymentText.split(", ")) {
                                   if (item.contains("month")) {
                                       payment.setMonth(item.substring(6));
                                   } else if (item.contains("method")) {
                                       BishMethod bishMethod = bishMethodRepo.findByNameContainingIgnoringCase(item.substring(7));
                                       payment.setMethod(bishMethod);
                                   } else if (item.contains("done")) {
                                       payment.setDone(Boolean.parseBoolean(item.substring(7)));
                                   } else if (item.contains("price")) {
                                       payment.setPrice(new BigDecimal(item.substring(6)));
                                   }
                               }
                               payments.add(payment);
                           }
                           break;
                    }
                    cellIDx++;
                    bishClient.setPayments(payments);
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
            List<OshClient> clients = new ArrayList<>();

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
                    List<OshPayment> payments = new ArrayList<>();
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
                            client.setPatronymic(currentCell.getStringCellValue());
                            break;
                        case 7:
                            client.setEmail(currentCell.getStringCellValue());
                            break;
                        case 8:
                            if (!currentCell.getStringCellValue().isEmpty() && !currentCell.getStringCellValue().equals("") && currentCell.getStringCellValue() != null)
                                client.setStatus(oshStatusesRepo.findByNameContainingIgnoringCase(currentCell.getStringCellValue()));
                            break;
                        case 9:
//                            System.out.println("occupation\t\t" + currentCell.getStringCellValue());
                            if (currentCell.getStringCellValue() != null && !currentCell.getStringCellValue().isEmpty() && !currentCell.getStringCellValue().equals("")) {
                                OshOccupation occupation = oshOccupationRepo.findByNameContainingIgnoringCase(currentCell.getStringCellValue());
                                client.setOccupation(occupation);
                                // client.setOccupation(oshOccupationRepo.findByNameContainingIgnoringCase(currentCell.getStringCellValue()).orElse(null));
                            }
                            break;
                        case 10:
                            if (currentCell.getStringCellValue() != null && !currentCell.getStringCellValue().isEmpty() && !currentCell.getStringCellValue().equals("")) {
                                String targetName = currentCell.getStringCellValue();
                                OshTarget target = oshTargetRepo.findByNameContainingIgnoringCase(targetName);
                                client.setTarget(target);
                            }
                            break;
                        case 11:
                            client.setExperience(currentCell.getBooleanCellValue());
                            break;
                        case 12:
                            client.setLaptop(currentCell.getBooleanCellValue());
                            break;
                        case 13:
//                            System.out.println("course\t\t" + currentCell.getStringCellValue());
                            if (currentCell.getStringCellValue() != null && !currentCell.getStringCellValue().isEmpty() && !currentCell.getStringCellValue().equals(""))
                                client.setCourse(oshCoursesService.findCourseByName(currentCell.getStringCellValue()));
                            break;
                        case 14:
//                            System.out.println("UTM\t\t" + currentCell.getStringCellValue());
                            if (currentCell.getStringCellValue() != null && !currentCell.getStringCellValue().isEmpty() && !currentCell.getStringCellValue().equals(""))
                                client.setUtm(oshUTMRepo.findByNameContainingIgnoringCase(currentCell.getStringCellValue()).orElse(null));
                            break;
                        case 15:
                            client.setDescription(currentCell.getStringCellValue());
                            break;
                        case 16:
                            client.setCity("OSH");
                            break;
                        case 17:
                            client.setFormName(currentCell.getStringCellValue());
                            break;
                        case 18:
                            client.setTimer(currentCell.getStringCellValue().isEmpty() ? null :
                                    LocalDateTime.parse(currentCell.getStringCellValue()));
                            break;
                        case 19:
                            if (currentCell.getStringCellValue() != null && !currentCell.getStringCellValue().isEmpty() && !currentCell.getStringCellValue().equals(""))
                                client.setPrepayment(new BigDecimal(currentCell.getStringCellValue()));
                            break;
                        case 20:
                            if (currentCell.getStringCellValue() != null && !currentCell.getStringCellValue().isEmpty() && !currentCell.getStringCellValue().equals(""))
                                client.setLeavingReason(oshLeavingReasonRepo.findByNameContainingIgnoringCase(currentCell.getStringCellValue()).orElse(null));
                            break;
                        default:
                            if (currentCell.getStringCellValue() != null && !currentCell.getStringCellValue().isEmpty() && !currentCell.getStringCellValue().equals("")) {
                                String paymentText = currentCell.getStringCellValue();
                                OshPayment payment = new OshPayment();
                                for (String item : paymentText.split(", ")) {
                                    if (item.contains("month")) {
                                        payment.setMonth(item.substring(6));
                                    } else if (item.contains("method")) {
                                        OshMethod oshMethod = oshMethodRepo.findByNameContainingIgnoringCase(item.substring(7));
                                        payment.setMethod(oshMethod);
                                    } else if (item.contains("done")) {
                                        payment.setDone(Boolean.parseBoolean(item.substring(7)));
                                    } else if (item.contains("price")) {
                                        payment.setPrice(new BigDecimal(item.substring(6)));
                                    }
                                }
                                payments.add(payment);
                            }
                            break;
                    }
                    cellIDx++;
                    client.setPayments(payments);
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
