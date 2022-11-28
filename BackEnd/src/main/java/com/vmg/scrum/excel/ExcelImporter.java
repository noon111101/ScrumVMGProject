package com.vmg.scrum.excel;


import com.vmg.scrum.model.User;
import com.vmg.scrum.model.excel.LogDetail;
import com.vmg.scrum.payload.request.SignupRequest;
import com.vmg.scrum.repository.DepartmentRepository;
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

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
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

            if(sheet==null)
                throw new RuntimeException("File không chứa sheet theo quy định (sheet 1 trống )");
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
                            user.setCode(currentCell.getStringCellValue());
                            System.out.println(currentCell.getStringCellValue());
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
                            logDetail.setDateLog(localDate);
                            break;
                        case 7:
                            logDetail.setShift(shiftRepository.findByName(currentCell.getStringCellValue()));
                            break;
                        case 8:
                            logDetail.setLeaveStatus(currentCell.getStringCellValue());
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
                    if(userRepository.findByCode(user.getCode())==null)
                        throw new RuntimeException("Danh sách chấm công chứa người dùng không tồn tại , bạn cần thêm người dùng trước");
                    logDetail.setUser(userRepository.findByCode(user.getCode()));
                    logDetails.add(logDetail);
                }

            }
            workbook.close();
        } catch (IOException e) {
            throw new RuntimeException("File không đúng quy tắc" );
        }
        return logDetails;
    }
    public List<SignupRequest> readUser(InputStream inputStream) throws IOException {
        List<SignupRequest> list = new ArrayList<>();
        try {
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);

            XSSFSheet sheet = workbook.getSheet("Sheet2");
            if(sheet==null)
                throw new RuntimeException("File không chứa sheet theo quy định (sheet 2 trống )");
            Iterator<Row> rows = sheet.iterator();


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
                            signupRequest.setCode(currentCell.getStringCellValue());
                            break;
                        case 2:
                            if(departmentRepository.findByName(currentCell.getStringCellValue())==null)
                                throw new RuntimeException("Thông tin phòng ban không đúng");
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
                list.add(signupRequest);
            }
            workbook.close();
        } catch (IOException e) {
            throw new RuntimeException("File không đúng quy tắc" );
        }
        return list;
    }
}

