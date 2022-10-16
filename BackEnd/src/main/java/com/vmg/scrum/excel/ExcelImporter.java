package com.vmg.scrum.excel;


import com.vmg.scrum.model.User;
import com.vmg.scrum.model.excel.LogDetail;
import com.vmg.scrum.model.excel.LogDetailTotal;
import com.vmg.scrum.model.option.Department;
import com.vmg.scrum.model.option.Shift;
import com.vmg.scrum.repository.ShiftRepository;
import com.vmg.scrum.repository.UserRepository;
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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
@Service
public class ExcelImporter {

    @Autowired
    UserRepository userRepository;
    @Autowired
    ShiftRepository shiftRepository;
    public String getPathExcelFile(){
    return "C:/Users/ADMIN/Documents/ExcelDataLogTest.xlsx";
    }
    public static String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";


    public static boolean hasExcelFormat(MultipartFile file) {

        if (!TYPE.equals(file.getContentType())) {
            return false;
        }

        return true;
    }
//    public List<LogDetailTotal> logDetailTotals;
    public List<LogDetail> read(InputStream inputStream) throws IOException {
        List<LogDetail> logDetails;
        List<LogDetailTotal> logDetailTotals;
        try {
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);

//            XSSFSheet sheet = workbook.getSheet("2. TTS T9.2022");
            XSSFSheet sheet = workbook.getSheet("Sheet1");

            Iterator<Row> rows = sheet.iterator();

            logDetails = new ArrayList<LogDetail>();

            logDetailTotals= new ArrayList<LogDetailTotal>();
            int rowNumber = 0;

            while (rows.hasNext()) {
                Row currentRow = rows.next();
                if (rowNumber == 0) {
                    rowNumber++;
                    continue;
                }
                Iterator<Cell> cellsInRow = currentRow.iterator();

                LogDetail logDetail = new LogDetail();
                LogDetailTotal logDetailTotal= new LogDetailTotal();
                User user = new User();
                int cellIdx = 0;
                while (cellsInRow.hasNext() && cellIdx<=11) {
                    Cell currentCell = cellsInRow.next();
                    switch (cellIdx) {
                        case 0:
                            if(currentCell.getStringCellValue().contains("(")){
                                logDetailTotal.setName(currentCell.getStringCellValue());
                                System.out.println("Skip Total");
                                break;
                            }
                            else
                                user.setFullName(currentCell.getStringCellValue());
                            break;
                        case 1:
                            if(logDetailTotal.getName()!=null){
                                logDetailTotal.setRegularHour(currentCell.getLocalDateTimeCellValue());
                                break;
                            }
                            user.setCode(currentCell.getNumericCellValue());
                            System.out.println(currentCell.getNumericCellValue());
                            break;
                        case 2:
                            if(logDetailTotal.getName()!=null){
                                logDetailTotal.setOverTime(currentCell.getLocalDateTimeCellValue());
                                break;
                            }
                            Department department = new Department();
                            department.setName(currentCell.getStringCellValue());
                            user.setDepartments(department);
                            break;
                        case 3:
                            if(logDetailTotal.getName()!=null){
                                logDetailTotal.setTotalWork(currentCell.getLocalDateTimeCellValue());
                                break;
                            }
                            logDetail.setRegularHour(currentCell.getLocalDateTimeCellValue());
                            break;
                        case 4:
                            logDetail.setOverTime(currentCell.getLocalDateTimeCellValue());
                            break;
                        case 5:
                            logDetail.setTotalWork(currentCell.getLocalDateTimeCellValue());
                            break;
                        case 6:
                            logDetail.setDate_log(currentCell.getDateCellValue());
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
                            logDetail.setTimeIn(currentCell.getLocalDateTimeCellValue());
                            break;
                        case 10:
                            if(currentCell.getCellType()==CellType.STRING){
                                logDetail.setTimeOut(null);
                                break;
                            }
                            logDetail.setTimeOut(currentCell.getLocalDateTimeCellValue());
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
                    logDetailTotals.add(logDetailTotal);
                }
//                this.logDetailTotals=logDetailTotals;

            }
            workbook.close();
        } catch (IOException e) {
            throw new RuntimeException("fail to parse Excel file: " + e.getMessage());
        }
        return logDetails;
    }
}

