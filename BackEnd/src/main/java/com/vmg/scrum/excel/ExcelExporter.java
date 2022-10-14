package com.vmg.scrum.excel;

import com.vmg.scrum.entity.User;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.Color;
import java.io.IOException;
import java.util.List;

public class ExcelExporter {
    private XSSFWorkbook workbook;
    private XSSFSheet sheet;

    private List<User> listUsers;

    public ExcelExporter(List<User> listUsers) {
        this.listUsers = listUsers;
        workbook = new XSSFWorkbook();

    }

    private void createCell(Row row, int columnCount, Object value, CellStyle style) {
        sheet.autoSizeColumn(columnCount);
        Cell cell = row.createCell(columnCount);
        if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        } else {
            cell.setCellValue((String) value);
        }
        cell.setCellStyle(style);
    }

    static int rowIndex = 0;
    // Create Header
    private void writeHeader () {
        //style
        CellStyle style = workbook.createCellStyle();
        style.setBorderBottom(BorderStyle.NONE);
        style.setBorderTop(BorderStyle.NONE);
        style.setBorderLeft(BorderStyle.NONE);
        style.setBorderRight(BorderStyle.NONE);
        style.setAlignment(HorizontalAlignment.CENTER);
        //font
        XSSFFont fontHeader = workbook.createFont();
        fontHeader.setColor(new XSSFColor(Color.decode("#000080")));
        fontHeader.setBold(true);
        fontHeader.setFontName("Times New Roman");
        fontHeader.setFontHeight(10);
        style.setFont(fontHeader);


        sheet = workbook.createSheet("Employees");


        Row row = sheet.createRow(1);
        Cell cell = row.createCell(0);
        cell.setCellValue("BẢNG CHẤM CÔNG");
        cell.setCellStyle(style);

        row = sheet.createRow(2);
        cell = row.createCell(0);
        cell.setCellValue("Tháng "+9+"/"+2022);
        cell.setCellStyle(style);

        row = sheet.createRow(3);
        cell = row.createCell(1);
        cell.setCellValue("Bộ phận: "+ "TTS PTPM");
        cell.setCellStyle(style);



        rowIndex = sheet.getLastRowNum();
    }

    private void writeTitleTable(){
        // style Titles
        CellStyle styleTitle = workbook.createCellStyle();
        styleTitle.setBorderBottom(BorderStyle.THIN);
        styleTitle.setBorderTop(BorderStyle.THIN);
        styleTitle.setBorderLeft(BorderStyle.THIN);
        styleTitle.setBorderRight(BorderStyle.THIN);
        styleTitle.setAlignment(HorizontalAlignment.CENTER);
        styleTitle.setVerticalAlignment(VerticalAlignment.CENTER);
        styleTitle.setWrapText(true);
        // font Titles
        XSSFFont fontHeader = workbook.createFont();
        fontHeader.setColor(new XSSFColor(Color.decode("#000080")));
        fontHeader.setBold(true);
        fontHeader.setFontName("Times New Roman");
        fontHeader.setFontHeight(10);
        styleTitle.setFont(fontHeader);

        // Edit Title Table
        Row row = sheet.createRow(4);
        for (int i=2;i<=32;i++){
            Cell cell = row.createCell(i);
            cell.setCellStyle(styleTitle);
        }
        Cell cell = row.createCell(0);
        cell.setCellValue("ID");
        cell.setCellStyle(styleTitle);

        cell = row.createCell(1);
        cell.setCellValue("Họ và tên");
        cell.setCellStyle(styleTitle);
        sheet.setColumnWidth(1, 5000);

        cell = row.createCell(2);
        cell.setCellValue("Ngày trong tháng");
        cell.setCellStyle(styleTitle);

        cell = row.createCell(33);
        cell.setCellValue("Tổng số\n" +
                " ngày\n" +
                " làm việc \n" +
                "thực tế");
        cell.setCellStyle(styleTitle);
        sheet.setColumnWidth(33, 2000);

        cell = row.createCell(34);
        cell.setCellValue("Tổng số\n" +
                " ngày hưởng\n" +
                " lương");
        cell.setCellStyle(styleTitle);
        sheet.setColumnWidth(34, 2000);


        row = sheet.createRow(5);
        int day = 1;
        for (int i=2;i<=32;i++){
            cell = row.createCell(i);
            cell.setCellValue(day++);
            cell.setCellStyle(styleTitle);
        }
        cell = row.createCell(0);
        cell.setCellStyle(styleTitle);
        sheet.setColumnWidth(0, 2000);

        cell = row.createCell(1);
        cell.setCellStyle(styleTitle);


        cell = row.createCell(33);
        cell.setCellStyle(styleTitle);
        sheet.setColumnWidth(33, 2000);

        cell = row.createCell(34);
        cell.setCellStyle(styleTitle);
        sheet.setColumnWidth(34, 2000);
        row.setHeight((short) 900);


        for (int i=2;i<=32;i++){
            sheet.setColumnWidth(i, 1500);

        }

        rowIndex = sheet.getLastRowNum();
    }


    // Create Body Table
    private void writeBodyTable() {
        Row row = null;
        Cell cell;

        // style  Body
        CellStyle styleBody = workbook.createCellStyle();
        styleBody.setBorderBottom(BorderStyle.THIN);
        styleBody.setBorderTop(BorderStyle.THIN);
        styleBody.setBorderLeft(BorderStyle.THIN);
        styleBody.setBorderRight(BorderStyle.THIN);
        styleBody.setWrapText(true);

        CellStyle styleBodyCenter = workbook.createCellStyle();
        styleBodyCenter.setBorderBottom(BorderStyle.THIN);
        styleBodyCenter.setBorderTop(BorderStyle.THIN);
        styleBodyCenter.setBorderLeft(BorderStyle.THIN);
        styleBodyCenter.setBorderRight(BorderStyle.THIN);
        styleBodyCenter.setAlignment(HorizontalAlignment.CENTER);

        CellStyle styleBodyColor = workbook.createCellStyle();
        styleBodyColor.setBorderBottom(BorderStyle.THIN);
        styleBodyColor.setBorderTop(BorderStyle.THIN);
        styleBodyColor.setBorderLeft(BorderStyle.THIN);
        styleBodyColor.setBorderRight(BorderStyle.THIN);
        styleBodyColor.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());

        // font Body
        XSSFFont fontBody = workbook.createFont();
        fontBody.setFontName("Times New Roman");
        fontBody.setFontHeight(10);
        styleBody.setFont(fontBody);
        styleBodyCenter.setFont(fontBody);
        styleBodyColor.setFont(fontBody);


        // Edit Body Table

        int rowCount = 6;
        for (User e : listUsers) {
            row = sheet.createRow(rowCount++);

            cell = row.createCell(0);
            cell.setCellValue(e.getId());
            cell.setCellStyle(styleBody);
            sheet.setColumnWidth(0, 1500 );

            cell = row.createCell(1);
            cell.setCellValue(e.getFullName());
            cell.setCellStyle(styleBody);


            for (int i=2;i<=34;i++){
                // Ký tự tính công
                cell = row.createCell(i);
                cell.setCellValue("H");
                cell.setCellStyle(styleBody);
//                if(i==3||i==4||i==10||i==11||i==17||i==18||i==24||i==25){
//                    cell.setCellStyle(styleBodyColor);
//                }
                if(i==33){
                    cell.setCellValue(12); // Tổng ngày làm việc
                    cell.setCellStyle(styleBodyCenter);
                }
                if(i==34){
                    cell.setCellValue(15); // Tổng ngày hưởng lương
                    cell.setCellStyle(styleBodyCenter);
                }
            }
        }

        row = sheet.createRow(rowCount++);
        for (int i=0;i<=32;i++){
            cell = row.createCell(i);
            cell.setCellStyle(styleBodyCenter);
        }
        cell = row.createCell(0);
        cell.setCellValue("Tổng cộng");
        cell.setCellStyle(styleBodyCenter);


        rowIndex = sheet.getLastRowNum();
        cell = row.createCell(33);
        cell.setCellFormula("SUM(AH7:AH"+rowIndex+")");
        cell.setCellStyle(styleBodyCenter);


        cell = row.createCell(34);
        cell.setCellFormula("SUM(AI7:AI"+rowIndex+")");
        cell.setCellStyle(styleBodyCenter);

        rowIndex = sheet.getLastRowNum();

    }
    // Create Footer
    private void writeFooter() {
        Row row = null;
        Cell cell;
        int rowCurrent = rowIndex+1;

        //style Bold
        CellStyle styleBold = workbook.createCellStyle();
        styleBold.setAlignment(HorizontalAlignment.CENTER);
        //font Bold
        XSSFFont fontBold = workbook.createFont();
        fontBold.setColor(new XSSFColor(Color.decode("#000080")));
        fontBold.setBold(true);
        fontBold.setFontName("Times New Roman");
        fontBold.setFontHeight(10);
        styleBold.setFont(fontBold);

        //style Thin Center
        CellStyle styleThinCenter = workbook.createCellStyle();
        styleThinCenter.setAlignment(HorizontalAlignment.CENTER);
        //style Thin Left
        CellStyle styleThinLeft = workbook.createCellStyle();
        styleThinCenter.setAlignment(HorizontalAlignment.CENTER);
        //font Thin
        XSSFFont fontThin = workbook.createFont();
        fontThin.setColor(new XSSFColor(Color.decode("#000080")));
        fontThin.setFontName("Times New Roman");
        fontThin.setFontHeight(10);
        styleThinCenter.setFont(fontThin);
        styleThinLeft.setFont(fontThin);


        row = sheet.createRow(rowCurrent);
        cell = row.createCell(1);
        cell.setCellValue("Ký hiệu: ");
        cell.setCellStyle(styleBold);

        row = sheet.createRow(++rowCurrent);
        cell = row.createCell(2);
        cell.setCellValue("H- Làm hành chính");
        cell.setCellStyle(styleThinLeft);

        row = sheet.createRow(++rowCurrent);
        cell = row.createCell(2);
        cell.setCellValue("P-  Nghỉ phép");
        cell.setCellStyle(styleThinLeft);

        row = sheet.createRow(++rowCurrent);
        cell = row.createCell(2);
        cell.setCellValue("CT - Công tác");
        cell.setCellStyle(styleThinLeft);

        row = sheet.createRow(++rowCurrent);
        cell = row.createCell(2);
        cell.setCellValue("TC - Nghỉ tiêu chuẩn (Cưới, Tứ thân phụ mẫu mất,...)");
        cell.setCellStyle(styleThinLeft);

        row = sheet.createRow(++rowCurrent);
        cell = row.createCell(2);
        cell.setCellValue("C- Làm ca chiều");
        cell.setCellStyle(styleThinLeft);

        row = sheet.createRow(++rowCurrent);
        cell = row.createCell(2);
        cell.setCellValue("Ô - Nghỉ Ốm");
        cell.setCellStyle(styleThinLeft);

        row = sheet.createRow(++rowCurrent);
        cell = row.createCell(2);
        cell.setCellValue("CĐ - Nghỉ Chế độ ( Nghỉ đẻ, Khám thai,Sảy thai,..)");
        cell.setCellStyle(styleThinLeft);

        ++rowCurrent;

        // Chữ ký
        row = sheet.createRow(++rowCurrent);
        cell = row.createCell(0);
        cell.setCellValue("Giám đốc TT/ Bộ phận");
        cell.setCellStyle(styleBold);



        cell = row.createCell(25);
        cell.setCellValue("TP. QTNNL&DVNB");
        cell.setCellStyle(styleBold);
        //Merge footer

        sheet.addMergedRegion(new CellRangeAddress(rowCurrent, rowCurrent, 0, 3));
        sheet.addMergedRegion(new CellRangeAddress(rowCurrent, rowCurrent, 25, 34));

        row = sheet.createRow(++rowCurrent);
        cell = row.createCell(0);
        cell.setCellValue("Ký và Ghi rõ Họ Tên");
        cell.setCellStyle(styleThinCenter);

        cell = row.createCell(25);
        cell.setCellValue("Ký và Ghi rõ Họ Tên");
        cell.setCellStyle(styleThinCenter);

        //Merge footer
        sheet.addMergedRegion(new CellRangeAddress(rowCurrent, rowCurrent, 0, 3));
        sheet.addMergedRegion(new CellRangeAddress(rowCurrent, rowCurrent, 25, 34));



        //Freeze Pane
        sheet.createFreezePane(0,6,0,6);

        // merge in header (row 1-4)
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 34));
        sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 34));
        sheet.addMergedRegion(new CellRangeAddress(2, 2, 0, 34));
        sheet.addMergedRegion(new CellRangeAddress(3, 3, 1, 6));
        sheet.addMergedRegion(new CellRangeAddress(3, 3, 7, 34));


        // Merge in table
        // CÁCH 1
        // "TT"
        sheet.addMergedRegion(CellRangeAddress.valueOf("B5:B6"));
        // "Họ và tên"
        sheet.addMergedRegion(CellRangeAddress.valueOf("A5:A6"));
        // "Tổng số ngày làm việc thực tế"
        sheet.addMergedRegion(CellRangeAddress.valueOf("AH5:AH6"));
        // "Tổng số ngày hưởng lương"
        sheet.addMergedRegion(CellRangeAddress.valueOf("AI5:AI6"));

        // CÁCH 2
        // "Ngày trong tháng"
        sheet.addMergedRegion(new CellRangeAddress(4, 4, 2, 32));
        // Ô "tổng cộng"
        sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, 0, 32));



        rowIndex = sheet.getLastRowNum();

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