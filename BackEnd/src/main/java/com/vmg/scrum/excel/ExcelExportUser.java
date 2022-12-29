package com.vmg.scrum.excel;

import com.vmg.scrum.model.Role;
import com.vmg.scrum.model.User;
import com.vmg.scrum.model.option.Department;
import com.vmg.scrum.payload.response.FurloughReport;
import com.vmg.scrum.repository.DepartmentRepository;
import com.vmg.scrum.repository.RoleRepository;
import com.vmg.scrum.repository.UserRepository;
import com.vmg.scrum.service.FurloughService;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.Color;
import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
public class ExcelExportUser {
    private XSSFWorkbook workbook;

    private XSSFSheet sheet;

    private UserRepository userRepository;

    private DepartmentRepository departmentRepository;

    private List<Department> listDeparts;

    private List<User> listUsers;

    private Long departId;

    public ExcelExportUser( List<User> listUsers, long departId,DepartmentRepository departmentRepository){
        this.departmentRepository = departmentRepository;
        workbook = new XSSFWorkbook();
        this.departId = departId;
        this.listUsers = listUsers;
        this.listDeparts = departmentRepository.findAll();
    }

    public ExcelExportUser(List<User> listUsers, DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
        workbook = new XSSFWorkbook();
        this.listUsers = listUsers;
        this.listDeparts = departmentRepository.findAll();

    }


    private void writeHeader() {


        XSSFFont fontHeaderBold = workbook.createFont();
        fontHeaderBold.setFontName("Arial");
        fontHeaderBold.setBold(true);
        fontHeaderBold.setFontHeight(16);


        CellStyle styleBold = workbook.createCellStyle();
        styleBold.setVerticalAlignment(VerticalAlignment.CENTER);
        styleBold.setFont(fontHeaderBold);

        sheet = workbook.createSheet("Danh sách nhân viên");

        Row row = sheet.createRow(0);
        Cell cell = row.createCell(0);
        cell.setCellValue("DANH SÁCH NHÂN VIÊN");
        cell.setCellStyle(styleBold);
    }

    private void writeTitleTable() {
        XSSFFont fontHeaderBold = workbook.createFont();
        fontHeaderBold.setBold(true);
        fontHeaderBold.setFontName("Arial");
        fontHeaderBold.setFontHeight(10);
        IndexedColorMap colorMap = workbook.getStylesSource().getIndexedColors();
        fontHeaderBold.setColor(new XSSFColor(Color.decode("#2596be"), colorMap));


        // style Titles Bold
        CellStyle styleTitleBold = workbook.createCellStyle();
        styleTitleBold.setBorderBottom(BorderStyle.THIN);
        styleTitleBold.setBorderTop(BorderStyle.THIN);
        styleTitleBold.setBorderLeft(BorderStyle.THIN);
        styleTitleBold.setBorderRight(BorderStyle.THIN);
        styleTitleBold.setAlignment(HorizontalAlignment.CENTER);
        styleTitleBold.setVerticalAlignment(VerticalAlignment.CENTER);
        styleTitleBold.setWrapText(true);
        styleTitleBold.setFont(fontHeaderBold);

        Row row = sheet.createRow(1);
        Cell cell;
        cell = row.createCell(0);
        cell.setCellValue("TT");
        cell.setCellStyle(styleTitleBold);

        cell = row.createCell(1);
        cell.setCellValue("Mã nhân viên");
        cell.setCellStyle(styleTitleBold);
        sheet.setColumnWidth(1, 3500);

        cell = row.createCell(2);
        cell.setCellValue("Họ và tên");
        cell.setCellStyle(styleTitleBold);
        sheet.setColumnWidth(2, 9000);

        cell = row.createCell(3);
        cell.setCellValue("Ngày vào làm");
        cell.setCellStyle(styleTitleBold);
        sheet.setColumnWidth(3, 5000);

        cell = row.createCell(4);
        cell.setCellValue("Phòng ban");
        cell.setCellStyle(styleTitleBold);
        sheet.setColumnWidth(4, 8000);

        cell = row.createCell(5);
        cell.setCellValue("Email");
        cell.setCellStyle(styleTitleBold);
        sheet.setColumnWidth(5, 9000);

        cell = row.createCell(6);
        cell.setCellValue("Vị trí");
        cell.setCellStyle(styleTitleBold);
        sheet.setColumnWidth(6, 5000);

        cell = row.createCell(7);
        cell.setCellValue("Trạng thái");
        cell.setCellStyle(styleTitleBold);
        sheet.setColumnWidth(7, 5000);

    }

    private void writeBodyTable() {
        XSSFFont fontHeaderThin = workbook.createFont();
        fontHeaderThin.setFontName("Arial");
        fontHeaderThin.setFontHeight(10);

        // style Titles Thin
        CellStyle styleTitleThin = workbook.createCellStyle();
        styleTitleThin.setBorderBottom(BorderStyle.THIN);
        styleTitleThin.setBorderTop(BorderStyle.THIN);
        styleTitleThin.setBorderLeft(BorderStyle.THIN);
        styleTitleThin.setBorderRight(BorderStyle.THIN);
        styleTitleThin.setAlignment(HorizontalAlignment.CENTER);
        styleTitleThin.setVerticalAlignment(VerticalAlignment.CENTER);
        styleTitleThin.setWrapText(true);
        styleTitleThin.setFont(fontHeaderThin);

        CellStyle styleTitleThinLeftBGC = workbook.createCellStyle();
        styleTitleThinLeftBGC.setBorderBottom(BorderStyle.THIN);
        styleTitleThinLeftBGC.setBorderTop(BorderStyle.THIN);
        styleTitleThinLeftBGC.setBorderLeft(BorderStyle.THIN);
        styleTitleThinLeftBGC.setBorderRight(BorderStyle.THIN);
        styleTitleThinLeftBGC.setAlignment(HorizontalAlignment.LEFT);
        styleTitleThinLeftBGC.setVerticalAlignment(VerticalAlignment.CENTER);
        styleTitleThinLeftBGC.setFillForegroundColor(IndexedColors.TAN.getIndex());
        styleTitleThinLeftBGC.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        styleTitleThinLeftBGC.setWrapText(true);
        styleTitleThinLeftBGC.setFont(fontHeaderThin);

        Row row = null;
        Cell cell;
        int tt = 1;
        for(Department department: listDeparts){
            row = sheet.createRow(sheet.getLastRowNum()+1);
            sheet.addMergedRegion(new CellRangeAddress(sheet.getLastRowNum(), sheet.getLastRowNum(), 0, 7));

            cell = row.createCell(0);
            cell.setCellValue(department.getName());
            cell.setCellStyle(styleTitleThinLeftBGC);
            for(User user: listUsers){
                if(user.getDepartments().getId() == department.getId()){
                    row = sheet.createRow(sheet.getLastRowNum()+1);
                    cell = row.createCell(0);
                    cell.setCellValue(tt++);
                    cell.setCellStyle(styleTitleThin);

                    cell = row.createCell(1);
                    cell.setCellValue(user.getCode());
                    cell.setCellStyle(styleTitleThin);

                    cell = row.createCell(2);
                    cell.setCellValue(user.getFullName());
                    cell.setCellStyle(styleTitleThin);

                    cell = row.createCell(3);
                    cell.setCellValue(user.getStartWork().toString());
                    cell.setCellStyle(styleTitleThin);

                    cell = row.createCell(4);
                    cell.setCellValue(user.getDepartments().getName());
                    cell.setCellStyle(styleTitleThin);

                    cell = row.createCell(5);
                    cell.setCellValue(user.getUsername());
                    cell.setCellStyle(styleTitleThin);

                    cell = row.createCell(6);
                    for(Role role: user.getRoles()){
                        if(role.getId() == 1){
                            cell.setCellValue("Nhân viên");
                        }
                        if(role.getId() == 2){
                            cell.setCellValue("Trưởng phòng");
                        }
                        if(role.getId() == 3) {
                            cell.setCellValue("Admin");
                        }
                    }
                    cell.setCellStyle(styleTitleThin);

                    cell = row.createCell(7);
                    if(user.getAvalible()){
                        cell.setCellValue("A");
                    }
                    else  cell.setCellValue("A");

                    cell.setCellStyle(styleTitleThin);
                }

            }
        }

    }

    private void writeFooter() {
    }

    public void export(HttpServletResponse response) throws IOException {
        writeHeader();
        writeTitleTable();
        writeBodyTable();
        writeFooter();

        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
    }
}
