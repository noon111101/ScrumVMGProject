package com.vmg.scrum.excel;

import com.vmg.scrum.model.User;
import com.vmg.scrum.model.excel.LogDetail;
import com.vmg.scrum.model.furlough.Furlough;
import com.vmg.scrum.model.furlough.FurloughHistory;
import com.vmg.scrum.repository.DepartmentRepository;
import com.vmg.scrum.repository.FurloughHistoryRepository;
import com.vmg.scrum.repository.FurloughRepository;
import com.vmg.scrum.repository.UserRepository;
import com.vmg.scrum.service.impl.FurloughServiceImpl;
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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
@Service
public class FurloughImporter {
    @Autowired
    UserRepository userRepository;
    @Autowired
    DepartmentRepository departmentRepository;
    @Autowired
    FurloughRepository furloughRepository;
    @Autowired
    FurloughHistoryRepository furloughHistoryRepository;
    @Autowired
    FurloughServiceImpl furloughService;
    public static String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";


    public static boolean hasExcelFormat(MultipartFile file) {

        if (!TYPE.equals(file.getContentType())) {
            return false;
        }
        return true;
    }
    public List<Furlough> readFurlough(InputStream inputStream,Long year) throws IOException {

        List<Furlough> furloughs = new ArrayList<Furlough>();

        try {
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);

            XSSFSheet sheet = workbook.getSheet("NV dang lam viec");

            if(sheet==null)
                throw new RuntimeException("File không chứa sheet theo quy định (sheet 1 trống )");
            Iterator<Row> rows = sheet.iterator();


            int rowNumber = 0;

            while (rows.hasNext()) {
                Row currentRow = rows.next();
                if (rowNumber <= 3) {
                    rowNumber++;
                    continue;
                }
                User user = null;
                Furlough furlough = null;
                FurloughHistory furloughHistory = new FurloughHistory();
                furloughHistory.setYear(year);
                Iterator<Cell> cellsInRow = currentRow.iterator();
                int cellIdx = 0;
                int monthIdx = -4;
                while (cellsInRow.hasNext()) {
                    Cell currentCell = cellsInRow.next();
                    switch (cellIdx) {
                        case 0:
                            break;
                        case 1:
                            break;
                        case 2:
                            user = userRepository.findByCode(currentCell.getStringCellValue());
                            furloughHistory.setUser(user);
                            break;
                        case 3:
                            if(furloughHistoryRepository.findByYearAndUserId(year,user.getId())!=null)
                                furloughHistory = furloughHistoryRepository.findByYearAndUserId(year,user.getId());
                            furloughHistory.setAvailibleCurrentYear((float) currentCell.getNumericCellValue());
                            break;
                        case 4:
                            furloughHistory.setLeftFurlough((float) currentCell.getNumericCellValue());
                            furloughHistoryRepository.save(furloughHistory);
                            break;
                        default:
                            furlough = new Furlough((long) monthIdx,year, (float) currentCell.getNumericCellValue(),user,furloughService.calculateAvailableUsedTillMonth((long) monthIdx,year, (float) currentCell.getNumericCellValue(),user));
                            furloughRepository.save(furlough);
                            break;
                    }
                    monthIdx++;
                    cellIdx++;
                }
                furloughs.add(furlough);
            }
            workbook.close();
        } catch (IOException e) {
            throw new RuntimeException("File không đúng quy tắc" );
        }
        return furloughs;
    }
}
