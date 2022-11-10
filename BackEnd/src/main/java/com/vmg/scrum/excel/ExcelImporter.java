package com.vmg.scrum.excel;


import com.vmg.scrum.model.ERole;
import com.vmg.scrum.model.Role;
import com.vmg.scrum.model.User;
import com.vmg.scrum.model.excel.LogDetail;
import com.vmg.scrum.model.excel.LogDetailTotal;
import com.vmg.scrum.model.option.Department;
import com.vmg.scrum.model.option.Shift;
import com.vmg.scrum.payload.request.SignupRequest;
import com.vmg.scrum.repository.DepartmentRepository;
import com.vmg.scrum.repository.RoleRepository;
import com.vmg.scrum.repository.ShiftRepository;
import com.vmg.scrum.repository.UserRepository;
import com.vmg.scrum.service.impl.UserServiceImpl;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@Service
public class ExcelImporter {

    @Autowired
    UserRepository userRepository;
    @Autowired
    ShiftRepository shiftRepository;
    @Autowired
    UserServiceImpl userService;
    @Autowired
    DepartmentRepository departmentRepository;
    public static String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";


    public static boolean hasExcelFormat(MultipartFile file) {

        if (!TYPE.equals(file.getContentType())) {
            return false;
        }

        return true;
    }
    public List<LogDetail> read(InputStream inputStream) throws IOException {
        List<LogDetail> logDetails;
        try {
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);

            XSSFSheet sheet = workbook.getSheet("Sheet1");

            Iterator<Row> rows = sheet.iterator();

            logDetails = new ArrayList<LogDetail>();

            int rowNumber = 0;

            while (rows.hasNext()) {
                Row currentRow = rows.next();
                if (rowNumber == 0) {
                    rowNumber++;
                    continue;
                }
                Iterator<Cell> cellsInRow = currentRow.iterator();
                boolean check = false;
                LogDetail logDetail = new LogDetail();
                User user = new User();
                int cellIdx = 0;
                while (cellsInRow.hasNext()) {
                    Cell currentCell = cellsInRow.next();
                    switch (cellIdx) {
                        case 0:
                            if(currentCell.getStringCellValue().contains("(")){
                                check = true;
                                System.out.println("Skip Total");
                                break;
                            }
                            else
                                user.setFullName(currentCell.getStringCellValue());
                            break;
                        case 1:
                            if(check){
                                break;
                            }
                            user.setCode(currentCell.getNumericCellValue());
                            System.out.println(currentCell.getNumericCellValue());
                            break;
                        case 2:
                            break;
                        case 3:
                            if(check){
                                break;
                            }
                            logDetail.setRegularHour(currentCell.getLocalDateTimeCellValue().toLocalTime());
                            break;
                        case 4:
                            if(check){
                                break;
                            }
                            logDetail.setOverTime(currentCell.getLocalDateTimeCellValue().toLocalTime());
                            break;
                        case 5:
                            if(check){
                                break;
                            }
                            logDetail.setTotalWork(currentCell.getLocalDateTimeCellValue().toLocalTime());
                            break;
                        case 6:
                            LocalDate localDate = currentCell.getLocalDateTimeCellValue().toLocalDate();
                            logDetail.setDate_log(localDate);
                            break;
                        case 7:
                            logDetail.setShift(shiftRepository.findByName(currentCell.getStringCellValue()));
                            break;
                        case 8:
                            logDetail.setLeave_status(currentCell.getStringCellValue());
                            break;
                        case 9:
                            if(currentCell.getCellType()==CellType.STRING){
                                logDetail.setTimeIn(null);
                                break;
                            }
                            logDetail.setTimeIn(currentCell.getLocalDateTimeCellValue().toLocalTime());
                            break;
                        case 10:
                            if(currentCell.getCellType()==CellType.STRING){
                                logDetail.setTimeOut(null);
                                break;
                            }
                            logDetail.setTimeOut(currentCell.getLocalDateTimeCellValue().toLocalTime());
                            break;
                        case 11:
                            logDetail.setException(currentCell.getStringCellValue());
                            break;
                        default:
                            break;
                    }
                    cellIdx++;

                }
                if(user.getFullName()!=null){
                    logDetail.setUser(userRepository.findByCode(user.getCode()));
                    logDetails.add(logDetail);
                }

            }
            workbook.close();
        } catch (IOException e) {
            throw new RuntimeException("fail to parse Excel file: " + e.getMessage());
        }
        return logDetails;
    }
    public List<User> readUser(InputStream inputStream) throws IOException {
        List<User> users;
        try {
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);

            XSSFSheet sheet = workbook.getSheet("Sheet2");

            Iterator<Row> rows = sheet.iterator();

            users = new ArrayList<User>();

            int rowNumber = 0;
            while (rows.hasNext()) {
                Row currentRow = rows.next();
                if (rowNumber == 0) {
                    rowNumber++;
                    continue;
                }
                Iterator<Cell> cellsInRow = currentRow.iterator();
                SignupRequest signupRequest = new SignupRequest();
                int cellIdx = 0;
                while (cellsInRow.hasNext()) {
                    Cell currentCell = cellsInRow.next();
                    switch (cellIdx) {
                        case 0:
                            signupRequest.setFullName(currentCell.getStringCellValue());
                            break;
                        case 1:
                            signupRequest.setCode(currentCell.getNumericCellValue());
                            break;
                        case 2:
                            signupRequest.setDepartment(currentCell.getStringCellValue());
                            break;
                        case 3:
                            signupRequest.setUsername(currentCell.getStringCellValue());
                            break;
                        case 4:
                            String role = currentCell.getStringCellValue();
                            Set<String> roles = new HashSet<>();
                            roles.add(role);
                            signupRequest.setRole(roles);
                            break;
                        case 5:
                            signupRequest.setGender(currentCell.getStringCellValue());
                            break;
                        default:
                            break;
                    }
                    cellIdx++;
                }
                userService.registerUser(signupRequest);
            }
            workbook.close();
        } catch (IOException e) {
            throw new RuntimeException("fail to parse Excel file: " + e.getMessage());
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
        return users;
    }
}

