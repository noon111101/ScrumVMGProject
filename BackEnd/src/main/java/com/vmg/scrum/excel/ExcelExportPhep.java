package com.vmg.scrum.excel;

import com.vmg.scrum.model.User;
import com.vmg.scrum.model.furlough.Furlough;
import com.vmg.scrum.model.excel.LogDetail;
import com.vmg.scrum.model.option.Department;
import com.vmg.scrum.payload.response.FurloughReport;
import com.vmg.scrum.repository.DepartmentRepository;
import com.vmg.scrum.repository.LogDetailRepository;
import com.vmg.scrum.repository.UserRepository;
import com.vmg.scrum.service.FurloughService;
import org.apache.poi.hssf.record.PaletteRecord;
import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.Color;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExcelExportPhep {
    private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private long year;

    private DepartmentRepository departmentRepository;

    private UserRepository userRepository;

    private FurloughService furloughService;

    private List<Department> listDeparts;

    private List<User> listUsers;

    private List<FurloughReport> listFurloughs;

    public ExcelExportPhep(long year, DepartmentRepository departmentRepository, UserRepository userRepository, FurloughService furloughService) {
        workbook = new XSSFWorkbook();
        this.year = year;
        this.departmentRepository= departmentRepository;
        this.userRepository = userRepository;
        this.furloughService = furloughService;
    }

    // Create Header
    private void writeHeader() {
        CellStyle styleThin = workbook.createCellStyle();
        styleThin.setVerticalAlignment(VerticalAlignment.CENTER);

        CellStyle styleBold = workbook.createCellStyle();
        styleBold.setVerticalAlignment(VerticalAlignment.CENTER);
        //font
        XSSFFont fontHeaderThin = workbook.createFont();
        fontHeaderThin.setFontName("Arial");
        fontHeaderThin.setFontHeight(10);
        styleThin.setFont(fontHeaderThin);

        XSSFFont fontHeaderBold = workbook.createFont();
        fontHeaderBold.setFontName("Arial");
//        fontHeaderBold.setBold(true);
        fontHeaderBold.setFontHeight(12);
        styleBold.setFont(fontHeaderBold);

        sheet = workbook.createSheet("Bảng Ngày Phép");

        Row row = sheet.createRow(0);
        Cell cell = row.createCell(0);
        cell.setCellValue("CÔNG TY CỔ PHẦN TRUYỀN THÔNG VMG");
        cell.setCellStyle(styleThin);


        row = sheet.createRow(1);
        cell = row.createCell(0);
        cell.setCellValue("TỔNG HỢP NGHỈ PHÉP 2020");
        cell.setCellStyle(styleBold);
    }

    private void writeTitleTable() {
        // font Titles Thin
        XSSFFont fontHeaderThin = workbook.createFont();
        fontHeaderThin.setFontName("Arial");
        fontHeaderThin.setFontHeight(10);

        XSSFFont fontHeaderThinColor = workbook.createFont();
        fontHeaderThinColor.setFontName("Arial");
        fontHeaderThinColor.setFontHeight(10);
        IndexedColorMap colorMap = workbook.getStylesSource().getIndexedColors();
        fontHeaderThinColor.setColor(new XSSFColor(Color.decode("#f54242"), colorMap));

        // font Titles Bold
        XSSFFont fontHeaderBold = workbook.createFont();
        fontHeaderBold.setBold(true);
        fontHeaderBold.setFontName("Arial");
        fontHeaderBold.setFontHeight(10);

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

        // style Titles Thin Red
        CellStyle styleTitleThinRed = workbook.createCellStyle();
        styleTitleThinRed.setBorderBottom(BorderStyle.THIN);
        styleTitleThinRed.setBorderTop(BorderStyle.THIN);
        styleTitleThinRed.setBorderLeft(BorderStyle.THIN);
        styleTitleThinRed.setBorderRight(BorderStyle.THIN);
        styleTitleThinRed.setAlignment(HorizontalAlignment.CENTER);
        styleTitleThinRed.setVerticalAlignment(VerticalAlignment.CENTER);
        styleTitleThinRed.setWrapText(true);
        styleTitleThinRed.setFont(fontHeaderThinColor);


        // style Titles Thin background LIGHT_TURQUOISE
        CellStyle styleTitleThinBackground = workbook.createCellStyle();
        styleTitleThinBackground.setBorderBottom(BorderStyle.THIN);
        styleTitleThinBackground.setBorderTop(BorderStyle.THIN);
        styleTitleThinBackground.setBorderLeft(BorderStyle.THIN);
        styleTitleThinBackground.setBorderRight(BorderStyle.THIN);
        styleTitleThinBackground.setAlignment(HorizontalAlignment.CENTER);
        styleTitleThinBackground.setVerticalAlignment(VerticalAlignment.CENTER);
        styleTitleThinBackground.setFillForegroundColor(IndexedColors.LIGHT_TURQUOISE.getIndex());
        styleTitleThinBackground.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        styleTitleThinBackground.setWrapText(true);
        styleTitleThinBackground.setFont(fontHeaderThin);

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

        // style Titles Bold Background PALE BLUE
        CellStyle styleTitleBoldBackground1 = workbook.createCellStyle();
        styleTitleBoldBackground1.setBorderBottom(BorderStyle.THIN);
        styleTitleBoldBackground1.setBorderTop(BorderStyle.THIN);
        styleTitleBoldBackground1.setBorderLeft(BorderStyle.THIN);
        styleTitleBoldBackground1.setBorderRight(BorderStyle.THIN);
        styleTitleBoldBackground1.setAlignment(HorizontalAlignment.CENTER);
        styleTitleBoldBackground1.setVerticalAlignment(VerticalAlignment.CENTER);
        styleTitleBoldBackground1.setFillForegroundColor(IndexedColors.PALE_BLUE.getIndex());
        styleTitleBoldBackground1.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        styleTitleBoldBackground1.setWrapText(true);
        styleTitleBoldBackground1.setFont(fontHeaderBold);

        // style Titles Bold Background
        CellStyle styleTitleBoldBackground2 = workbook.createCellStyle();
        styleTitleBoldBackground2.setBorderBottom(BorderStyle.THIN);
        styleTitleBoldBackground2.setBorderTop(BorderStyle.THIN);
        styleTitleBoldBackground2.setBorderLeft(BorderStyle.THIN);
        styleTitleBoldBackground2.setBorderRight(BorderStyle.THIN);
        styleTitleBoldBackground2.setAlignment(HorizontalAlignment.CENTER);
        styleTitleBoldBackground2.setVerticalAlignment(VerticalAlignment.CENTER);
        styleTitleBoldBackground2.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        styleTitleBoldBackground2.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        styleTitleBoldBackground2.setWrapText(true);
        styleTitleBoldBackground2.setFont(fontHeaderBold);



        // Edit Title Table
        Row row = sheet.createRow(2);
        row.setHeight((short) 500);
        Cell cell;
        for (int i = 6; i <= 17; i++) {
            cell = row.createCell(i);
            cell.setCellStyle(styleTitleBold);
        }

        cell = row.createCell(0);
        cell.setCellValue("TT");
        cell.setCellStyle(styleTitleThin);

        cell = row.createCell(1);
        cell.setCellValue("Họ và tên");
        cell.setCellStyle(styleTitleThin);
        sheet.setColumnWidth(1, 5000);

        cell = row.createCell(2);
        cell.setCellValue("Mã nhân sự");
        cell.setCellStyle(styleTitleThin);
        sheet.setColumnWidth(2, 3500);

        cell = row.createCell(3);
        cell.setCellValue("Thời gian tính phép");
        cell.setCellStyle(styleTitleThin);
        sheet.setColumnWidth(3, 3000);

        cell = row.createCell(4);
        cell.setCellValue("Số ngày được nghỉ trong năm 2022");
        cell.setCellStyle(styleTitleThin);
        sheet.setColumnWidth(4, 2000);

        cell = row.createCell(5);
        cell.setCellValue("Số ngày phép dư đầu kỳ năm 2022");
        cell.setCellStyle(styleTitleBold);
        sheet.setColumnWidth(5, 2000);

        cell = row.createCell(6);
        cell.setCellValue("Số ngày nghỉ");
        cell.setCellStyle(styleTitleThinBackground);

        cell = row.createCell(18);
        cell.setCellValue("Tổng số ngày đã nghỉ trước tháng 4");
        cell.setCellStyle(styleTitleThinRed);
        sheet.setColumnWidth(18, 2500);

        cell = row.createCell(19);
        cell.setCellValue("Tổng số ngày nghỉ phép của năm 2021");
        cell.setCellStyle(styleTitleThinRed);
        sheet.setColumnWidth(19, 2500);

        cell = row.createCell(20);
        cell.setCellValue("Số ngày phép còn lại của năm 2021");
        cell.setCellStyle(styleTitleBold);
        sheet.setColumnWidth(20, 2500);

        cell = row.createCell(21);
        cell.setCellValue("Số ngày phép còn lại năm 2022");
        cell.setCellStyle(styleTitleBoldBackground2);
        sheet.setColumnWidth(21, 2500);

        cell = row.createCell(22);
        cell.setCellValue("Trả phép");
        cell.setCellStyle(styleTitleBoldBackground2);
        sheet.setColumnWidth(22, 2500);

        cell = row.createCell(23);
        cell.setCellValue("Còn lại");
        cell.setCellStyle(styleTitleBoldBackground2);
        sheet.setColumnWidth(23, 2500);

        cell = row.createCell(24);
        cell.setCellValue("Ngày phép CL 2019 của NV nghỉ việc tháng 7");
        cell.setCellStyle(styleTitleBoldBackground1);
        sheet.setColumnWidth(24, 3000);


        row = sheet.createRow(3);
        int month = 1;
        for (int i = 6; i <= 17; i++) {
            cell = row.createCell(i);
            cell.setCellValue("Tháng "+ (month++));
            cell.setCellStyle(styleTitleThin);
            sheet.setColumnWidth(i, 2400);

        }

        for (int i = 0; i <= 5; i++) {
            cell = row.createCell(i);
            cell.setCellStyle(styleTitleBold);
        }

        for(int i = 18; i<=24; i++){
            cell = row.createCell(i);
            cell.setCellStyle(styleTitleBold);
            row.setHeight((short) 1500);
        }



    }

    // Create Body Table
    private void writeBodyTable() {
        // font Titles Thin
        XSSFFont fontHeaderThin = workbook.createFont();
        fontHeaderThin.setFontName("Arial");
        fontHeaderThin.setFontHeight(10);

        XSSFFont fontHeaderThinColor = workbook.createFont();
        fontHeaderThinColor.setFontName("Arial");
        fontHeaderThinColor.setFontHeight(10);
        IndexedColorMap colorMap = workbook.getStylesSource().getIndexedColors();
        fontHeaderThinColor.setColor(new XSSFColor(Color.decode("#f54242"), colorMap));

        // font Titles Bold
        XSSFFont fontHeaderBold = workbook.createFont();
        fontHeaderBold.setBold(true);
        fontHeaderBold.setFontName("Arial");
        fontHeaderBold.setFontHeight(10);

        // style Titles Thin
        CellStyle styleTitleThinLeft = workbook.createCellStyle();
        styleTitleThinLeft.setBorderBottom(BorderStyle.THIN);
        styleTitleThinLeft.setBorderTop(BorderStyle.THIN);
        styleTitleThinLeft.setBorderLeft(BorderStyle.THIN);
        styleTitleThinLeft.setBorderRight(BorderStyle.THIN);
        styleTitleThinLeft.setAlignment(HorizontalAlignment.LEFT);
        styleTitleThinLeft.setVerticalAlignment(VerticalAlignment.CENTER);
        styleTitleThinLeft.setWrapText(true);
        styleTitleThinLeft.setFont(fontHeaderThin);

        // style Titles Thin
        CellStyle styleTitleThinRight = workbook.createCellStyle();
        styleTitleThinRight.setBorderBottom(BorderStyle.THIN);
        styleTitleThinRight.setBorderTop(BorderStyle.THIN);
        styleTitleThinRight.setBorderLeft(BorderStyle.THIN);
        styleTitleThinRight.setBorderRight(BorderStyle.THIN);
        styleTitleThinRight.setAlignment(HorizontalAlignment.LEFT);
        styleTitleThinRight.setVerticalAlignment(VerticalAlignment.CENTER);
        styleTitleThinRight.setWrapText(true);
        styleTitleThinRight.setFont(fontHeaderThin);

        // style Titles Thin
        CellStyle styleTitleThinCenter = workbook.createCellStyle();
        styleTitleThinCenter.setBorderBottom(BorderStyle.THIN);
        styleTitleThinCenter.setBorderTop(BorderStyle.THIN);
        styleTitleThinCenter.setBorderLeft(BorderStyle.THIN);
        styleTitleThinCenter.setBorderRight(BorderStyle.THIN);
        styleTitleThinCenter.setAlignment(HorizontalAlignment.CENTER);
        styleTitleThinCenter.setVerticalAlignment(VerticalAlignment.CENTER);
        styleTitleThinCenter.setWrapText(true);
        styleTitleThinCenter.setFont(fontHeaderThin);

        // style Titles Thin Background color
        CellStyle styleTitleThinLeftBGC = workbook.createCellStyle();
        styleTitleThinLeftBGC.setBorderBottom(BorderStyle.THIN);
        styleTitleThinLeftBGC.setBorderTop(BorderStyle.THIN);
        styleTitleThinLeftBGC.setBorderLeft(BorderStyle.THIN);
        styleTitleThinLeftBGC.setBorderRight(BorderStyle.THIN);
        styleTitleThinLeftBGC.setAlignment(HorizontalAlignment.LEFT);
        styleTitleThinLeftBGC.setVerticalAlignment(VerticalAlignment.CENTER);
        styleTitleThinLeftBGC.setFillForegroundColor(IndexedColors.LIME.getIndex());
        styleTitleThinLeftBGC.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        styleTitleThinLeftBGC.setWrapText(true);
        styleTitleThinLeftBGC.setFont(fontHeaderThin);

        // style Titles Bold
        CellStyle styleTitleBold = workbook.createCellStyle();
        styleTitleBold.setBorderBottom(BorderStyle.THIN);
        styleTitleBold.setBorderTop(BorderStyle.THIN);
        styleTitleBold.setBorderLeft(BorderStyle.THIN);
        styleTitleBold.setBorderRight(BorderStyle.THIN);
        styleTitleBold.setAlignment(HorizontalAlignment.RIGHT);
        styleTitleBold.setVerticalAlignment(VerticalAlignment.CENTER);
        styleTitleBold.setWrapText(true);
        styleTitleBold.setFont(fontHeaderBold);

        CellStyle styleTitleBoldBackground2 = workbook.createCellStyle();
        styleTitleBoldBackground2.setBorderBottom(BorderStyle.THIN);
        styleTitleBoldBackground2.setBorderTop(BorderStyle.THIN);
        styleTitleBoldBackground2.setBorderLeft(BorderStyle.THIN);
        styleTitleBoldBackground2.setBorderRight(BorderStyle.THIN);
        styleTitleBoldBackground2.setAlignment(HorizontalAlignment.CENTER);
        styleTitleBoldBackground2.setVerticalAlignment(VerticalAlignment.CENTER);
        styleTitleBoldBackground2.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        styleTitleBoldBackground2.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        styleTitleBoldBackground2.setWrapText(true);
        styleTitleBoldBackground2.setFont(fontHeaderBold);


        listDeparts = departmentRepository.findAll();
        listUsers = userRepository.findAll();
        Row row = null;
        Cell cell;
        int tt = 1;

        listFurloughs = furloughService.getFurloughsByYear(year);

        for(Department department: listDeparts){
            row = sheet.createRow(sheet.getLastRowNum()+1);
            sheet.addMergedRegion(new CellRangeAddress(sheet.getLastRowNum(), sheet.getLastRowNum(), 0, 23));

            cell = row.createCell(0);
            cell.setCellValue(department.getName());
            cell.setCellStyle(styleTitleThinLeftBGC);


            for(FurloughReport fur: listFurloughs){
                if(fur.getUser().getDepartments().getId() == department.getId()){
                    row = sheet.createRow(sheet.getLastRowNum()+1);
                    cell = row.createCell(0);
                    cell.setCellValue(tt++);
                    cell.setCellStyle(styleTitleThinCenter);

                    cell = row.createCell(1);
                    cell.setCellValue(fur.getUser().getFullName());
                    cell.setCellStyle(styleTitleThinLeft);

                    cell = row.createCell(2);
                    cell.setCellValue(fur.getUser().getCode());
                    cell.setCellStyle(styleTitleThinLeft);

                    cell = row.createCell(3);
                    cell.setCellValue(fur.getUser().getStartWork().toString());
                    cell.setCellStyle(styleTitleThinCenter);

                    cell = row.createCell(4);
                    cell.setCellValue(fur.getAvailibleCurrentYear());
                    cell.setCellStyle(styleTitleThinCenter);

                    cell = row.createCell(5);
                    cell.setCellValue(fur.getOddCurrentYear());
                    cell.setCellStyle(styleTitleThinCenter);

                    for(int i=0;i<=11;i++){
                        cell = row.createCell(i+6);
                        for (Furlough fur1: fur.getFurloughs()) {
                            if(fur1.getMonthInYear()==i+1){
                                cell.setCellValue(fur1.getUsedInMonth());
                            }
                        }
                        cell.setCellStyle(styleTitleThinCenter);
                    }

                    int rowCount = sheet.getLastRowNum()+1;
                    cell = row.createCell(18);
                    cell.setCellFormula("SUM(G"+rowCount+":I"+rowCount+")");
                    cell.setCellStyle(styleTitleThinCenter);

                    cell = row.createCell(19);
                    cell.setCellFormula("SUM(J"+rowCount+":R"+rowCount+")");
                    cell.setCellFormula("IF(F"+rowCount+"-SUM(G"+rowCount+":I"+rowCount+")<=0," +
                            "SUM(G"+rowCount+":I"+rowCount+")-F"+rowCount+"+SUM(J"+rowCount+":R"+rowCount+")," +
                            "SUM(J"+rowCount+":R"+rowCount+"))");
                    cell.setCellStyle(styleTitleThinCenter);

                    cell = row.createCell(20);
                    cell.setCellFormula("IF(F"+rowCount+"-SUM(G"+rowCount+":I"+rowCount+")" +
                                            "<=0,0,F"+rowCount+"-SUM(G"+rowCount+":I"+rowCount+"))");
                    cell.setCellStyle(styleTitleThinCenter);

                    cell = row.createCell(21);
                    cell.setCellFormula("IF(F"+rowCount+"-SUM(G"+rowCount+":I"+rowCount+")" +
                                    "<0,E"+rowCount+"+F"+rowCount+"-SUM(G"+rowCount+":I"+rowCount+"),E"+rowCount+")" +
                                    "-SUM(J"+rowCount+":R"+rowCount+")");
                    cell.setCellStyle(styleTitleBoldBackground2);

                    cell = row.createCell(22);
                    cell.setCellValue(fur.getOddCurrentYear());
                    cell.setCellStyle(styleTitleBoldBackground2);

                    cell = row.createCell(23);
                    cell.setCellFormula("U"+rowCount+"+V"+rowCount+"-W"+rowCount);
                    cell.setCellStyle(styleTitleBoldBackground2);
                }
            }
//            for(User user: listUsers){
//                if(user.getDepartments().getId() == department.getId()){
//                    row = sheet.createRow(sheet.getLastRowNum()+1);
//                    cell = row.createCell(0);
//                    cell.setCellValue(tt++);
//                    cell.setCellStyle(styleTitleThinCenter);
//
//                    cell = row.createCell(1);
//                    cell.setCellValue(user.getFullName());
//                    cell.setCellStyle(styleTitleThinLeft);
//
//                    cell = row.createCell(2);
//                    NumberFormat numberFormat = NumberFormat.getInstance();
//                    DecimalFormat dcf = new DecimalFormat("####");
//                    cell.setCellValue("VMG_"+dcf.format(user.getCode()));
//                    cell.setCellStyle(styleTitleThinLeft);
//                }
//            }

        }
//        sheet.addMergedRegion(new CellRangeAddress(5, 5, 0, 23));

    }

    // Create Footer
    private void writeFooter() {



        // merge Header table
        sheet.addMergedRegion(CellRangeAddress.valueOf("A3:A4"));
        sheet.addMergedRegion(CellRangeAddress.valueOf("B3:B4"));
        sheet.addMergedRegion(CellRangeAddress.valueOf("C3:C4"));
        sheet.addMergedRegion(CellRangeAddress.valueOf("D3:D4"));
        sheet.addMergedRegion(CellRangeAddress.valueOf("E3:E4"));
        sheet.addMergedRegion(CellRangeAddress.valueOf("F3:F4"));
        sheet.addMergedRegion(CellRangeAddress.valueOf("G3:R3"));
        sheet.addMergedRegion(CellRangeAddress.valueOf("S3:S4"));
        sheet.addMergedRegion(CellRangeAddress.valueOf("T3:T4"));
        sheet.addMergedRegion(CellRangeAddress.valueOf("U3:U4"));
        sheet.addMergedRegion(CellRangeAddress.valueOf("V3:V4"));
        sheet.addMergedRegion(CellRangeAddress.valueOf("W3:W4"));
        sheet.addMergedRegion(CellRangeAddress.valueOf("X3:X4"));
        sheet.addMergedRegion(CellRangeAddress.valueOf("Y3:Y4"));

        // freeze Pane
        sheet.createFreezePane(6,4);


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
