package com.vmg.scrum.excel;


import com.vmg.scrum.model.User;
import com.vmg.scrum.model.excel.LogDetail;
import com.vmg.scrum.payload.response.UserLogDetail;
import com.vmg.scrum.repository.DepartmentRepository;
import com.vmg.scrum.repository.LogDetailRepository;
import com.vmg.scrum.repository.UserRepository;
import com.vmg.scrum.service.LogDetailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.Color;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class ExcelExporter {
    private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private List<LogDetail> listLogs;

    private LogDetailRepository logDetailRepository;
    private long id;

    private int dayInMonth;
    private int month;

    private int year = 2022;
    private DepartmentRepository departmentRepository;

    private UserRepository userRepository;

    public ExcelExporter(List<LogDetail> listLogs, Long id, int month, DepartmentRepository departmentRepository,
                         UserRepository userRepository,
                         LogDetailRepository logDetailRepository) {
        this.listLogs = listLogs;
        workbook = new XSSFWorkbook();
        this.id = id;
        this.month = month;
        this.departmentRepository = departmentRepository;
        this.userRepository = userRepository;
        this.logDetailRepository = logDetailRepository;
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
    private void writeHeader() {
        //style
        CellStyle style = workbook.createCellStyle();
        style.setBorderBottom(BorderStyle.NONE);
        style.setBorderTop(BorderStyle.NONE);
        style.setBorderLeft(BorderStyle.NONE);
        style.setBorderRight(BorderStyle.NONE);
        style.setAlignment(HorizontalAlignment.CENTER);
        //font
        XSSFFont fontHeader = workbook.createFont();
        IndexedColorMap colorMap = workbook.getStylesSource().getIndexedColors();
        fontHeader.setColor(new XSSFColor(Color.decode("#000080"), colorMap));
        fontHeader.setBold(true);
        fontHeader.setFontName("Times New Roman");
        fontHeader.setFontHeight(10);
        style.setFont(fontHeader);


        sheet = workbook.createSheet("Bảng Chấm Công");


        Row row = sheet.createRow(1);
        Cell cell = row.createCell(0);
        cell.setCellValue("BẢNG CHẤM CÔNG");
        cell.setCellStyle(style);

        row = sheet.createRow(2);
        cell = row.createCell(0);
        cell.setCellValue("Tháng " + month + "/" + 2022);
        cell.setCellStyle(style);

        row = sheet.createRow(3);
        cell = row.createCell(1);
        cell.setCellValue("Bộ phận: " + departmentRepository.getById(id).getName());
        cell.setCellStyle(style);


        rowIndex = sheet.getLastRowNum();
    }

    private void writeTitleTable() {
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
        IndexedColorMap colorMap = workbook.getStylesSource().getIndexedColors();
        fontHeader.setColor(new XSSFColor(Color.decode("#000080"), colorMap));
        fontHeader.setBold(true);
        fontHeader.setFontName("Times New Roman");
        fontHeader.setFontHeight(10);
        styleTitle.setFont(fontHeader);

        // Edit Title Table
        Row row = sheet.createRow(4);
        for (int i = 2; i <= 32; i++) {
            Cell cell = row.createCell(i);
            cell.setCellStyle(styleTitle);
        }
        Cell cell = row.createCell(0);
        cell.setCellValue("STT");
        cell.setCellStyle(styleTitle);

        cell = row.createCell(1);
        cell.setCellValue("Họ và tên");
        cell.setCellStyle(styleTitle);
        sheet.setColumnWidth(1, 5000);

        cell = row.createCell(2);
        cell.setCellValue("Ngày trong tháng");
        cell.setCellStyle(styleTitle);

        cell = row.createCell(33);
        cell.setCellValue("Tổng số\n" + " ngày\n" + " làm việc \n" + "thực tế");
        cell.setCellStyle(styleTitle);
        sheet.setColumnWidth(33, 2000);

        cell = row.createCell(34);
        cell.setCellValue("Tổng số\n" + " ngày hưởng\n" + " lương");
        cell.setCellStyle(styleTitle);
        sheet.setColumnWidth(34, 2000);


        row = sheet.createRow(5);
        int day = 1;
        for (int i = 2; i <= 32; i++) {
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


        for (int i = 2; i <= 32; i++) {
            sheet.setColumnWidth(i, 1500);

        }

        rowIndex = sheet.getLastRowNum();
    }


    // Create Body Table
    private void writeBodyTable() {
        Row row = null;
        Cell cell;
        CreationHelper creationHelper = (XSSFCreationHelper) workbook.getCreationHelper();

        XSSFDrawing drawing = sheet.createDrawingPatriarch();
        ClientAnchor clientAnchor = drawing.createAnchor(0, 0, 0, 0, 0, 2, 7, 12);

        Comment comment = (Comment) drawing.createCellComment(clientAnchor);
        RichTextString richTextString = creationHelper.createRichTextString("We can put a long comment here with \n a new line text followed by another \n new line text");

        comment.setString(richTextString);
        comment.setAuthor("Soumitra");


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
        styleBodyColor.setFillForegroundColor(IndexedColors.TAN.getIndex());
//        styleBodyColor.setFillForegroundColor(IndexedColors.GREEN.getIndex());
        styleBodyColor.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        // font Body
        XSSFFont fontBody = workbook.createFont();
        fontBody.setFontName("Times New Roman");
        fontBody.setFontHeight(10);
        styleBody.setFont(fontBody);
        styleBodyCenter.setFont(fontBody);
        styleBodyColor.setFont(fontBody);


        // Edit Body Table
        List<LogDetail> listLogsByUser = listLogs;
        int rowCount = 6;
        int tt = 1;
        int count = departmentRepository.findById(id).get().getUsers().size();
        List<User> listUsers = userRepository.findAllByDepartments_Id(id);
        List<UserLogDetail> userLogDetails = new ArrayList<>();


        for (User user : listUsers) {
            UserLogDetail userLogDetail = new UserLogDetail();
            List<LogDetail> list = new ArrayList<>();
            for (LogDetail logDetail : listLogs) {
                if (logDetail.getUser() == user) {
                    list.add(logDetail);
                } else continue;
                userLogDetail.setCode(user.getCode());
                userLogDetail.setName(user.getFullName());
                userLogDetail.setLogDetail(list);
            }
            if (userLogDetail.getName() != null) {
                userLogDetails.add(userLogDetail);
            }
            if (userLogDetail.getName() == null) continue;
        }


        for (User user : listUsers) {
            row = sheet.createRow(rowCount++);
            cell = row.createCell(0);
            cell.setCellValue(tt++);
            cell.setCellStyle(styleBody);
            sheet.setColumnWidth(0, 1200);

            cell = row.createCell(1);
            cell.setCellValue(user.getFullName());
            cell.setCellStyle(styleBody);


            for (int i = 0; i <= 30; i++) {
                if (user != null) {
                    sheet.setColumnWidth(i + 2, 1500);

                    cell = row.createCell(i + 2);
                    cell.setCellValue("-");
                    cell.setCellStyle(styleBody);
                    cell.setCellComment(comment);

                    // Tổng
                    for (int k = 33; k <= 34; k++) {
                        cell = row.createCell(k);
                        cell.setCellStyle(styleBody);
                        if (k == 33) {
                            cell.setCellFormula("COUNTIF(C" + rowCount + ":AG" + rowCount + ", \"*H*\")" +
                                    "-COUNTIF(C" + rowCount + ":AG" + rowCount + ",\"*/H*\")/2" +
                                    "-COUNTIF(C" + rowCount + ":AG" + rowCount + ",\"*H/*\")/2" +
                                    "+COUNTIF(C" + rowCount + ":AG" + rowCount + ", \"*CT*\")" +
                                    "+COUNTIF(C" + rowCount + ":AG" + rowCount + ", \"*LB*\")"); // Tổng ngày làm việc
                            cell.setCellStyle(styleBodyCenter);
                            cell.setCellStyle(styleBodyCenter);
                        }
                        if (k == 34) {
                            cell.setCellFormula("AH" + rowCount + ""); // Tổng ngày hưởng lương
                            cell.setCellStyle(styleBodyCenter);
                        }
                    }
                }
            }


            for (UserLogDetail ul : userLogDetails) {
                if (user.getCode() == ul.getCode()) {
                    String sign = null;
                    List<LogDetail> logDetails = ul.getLogDetail();
                    for (LogDetail logDetail : logDetails) {
                        cell = row.createCell(logDetail.getDate_log().getDayOfMonth() + 1);
                        if (logDetail.getSigns().getName().toString() == null) {
                            cell.setCellValue("-");
                            cell.setCellStyle(styleBody);
                        } else if (logDetail.getSigns().getName().toString() == "H_KL") {
                            cell.setCellValue("H/KL");
                            cell.setCellStyle(styleBody);
                        } else if (logDetail.getSigns().getName().toString() == "KL_H") {
                            cell.setCellValue("KL/H");
                            cell.setCellStyle(styleBody);
                        } else if(logDetail.getDate_log().getDayOfWeek().toString() == "SATURDAY" ||
                                logDetail.getDate_log().getDayOfWeek().toString() == "SUNDAY"){
                            cell.setCellValue("NT");
                            cell.setCellStyle(styleBodyColor);
                        }
                        else {
                            cell.setCellValue(logDetail.getSigns().getName().toString());
                            cell.setCellStyle(styleBody);
                        }

                    }

                    // Tổng
                    for (int k = 33; k <= 34; k++) {
                        cell = row.createCell(k);
                        cell.setCellStyle(styleBody);
                        if (k == 33) {
                            cell.setCellFormula("COUNTIF(C" + rowCount + ":AG" + rowCount + ", \"*H*\")" +
                                    "-COUNTIF(C" + rowCount + ":AG" + rowCount + ",\"*/H*\")/2" +
                                    "-COUNTIF(C" + rowCount + ":AG" + rowCount + ",\"*H/*\")/2" +
                                    "+COUNTIF(C" + rowCount + ":AG" + rowCount + ", \"*CT*\")" +
                                    "+COUNTIF(C" + rowCount + ":AG" + rowCount + ", \"*LB*\")"); // Tổng ngày làm việc
                            cell.setCellStyle(styleBodyCenter);
                            cell.setCellStyle(styleBodyCenter);
                        }
                        if (k == 34) {
                            cell.setCellFormula("AH" + rowCount + ""); // Tổng ngày hưởng lương
                            cell.setCellStyle(styleBodyCenter);
                        }
                    }
                }

            }

        }


//        for (UserLogDetail ul : userLogDetails) {
//            System.out.println(ul.getName());
//            row = sheet.createRow(rowCount++);
//            cell = row.createCell(0);
//            cell.setCellValue(tt++);
//            cell.setCellStyle(styleBody);
//            sheet.setColumnWidth(0, 1200);
//
//            cell = row.createCell(1);
//            cell.setCellValue(ul.getName());
//            cell.setCellStyle(styleBody);
//            for (int i = 0; i <= 30; i++) {
//                System.out.println(ul.getLogDetail().get(i).getSigns().getName());
//                sheet.setColumnWidth(i+2, 1500);
//
//                if(ul.getLogDetail().get(i).getDate_log().getDayOfWeek().toString()=="SATURDAY" ||
//                        ul.getLogDetail().get(i).getDate_log().getDayOfWeek().toString()=="SUNDAY" ){
//                    cell = row.createCell(i+2);
//                    cell.setCellValue("NT");
//                    cell.setCellStyle(styleBodyColor);
//
//                }
//                else{
//                    cell = row.createCell(i+2);
//                    if(ul.getLogDetail().get(i).getSigns().getName().toString()=="H_KL"){
//                        cell.setCellValue("H/KL");
//                    }
//                    else if(ul.getLogDetail().get(i).getSigns().getName().toString()=="KL_H"){
//                        cell.setCellValue("KL/H");
//                    }
//                    else{
//                        cell.setCellValue(ul.getLogDetail().get(i).getSigns().getName().toString());
//                    }
//                    cell.setCellStyle(styleBody);
//                }
//                cell.setCellComment(comment);
//            }
//
//            // Tổng
//            for (int i = 33; i <= 34; i++) {
//                cell = row.createCell(i);
//                cell.setCellStyle(styleBody);
//                if (i == 33) {
//                    cell.setCellFormula("COUNTIF(C" + rowCount + ":AG" + rowCount + ", \"*H*\")" +
//                            "-COUNTIF(C" + rowCount + ":AG" + rowCount + ",\"*/H*\")/2" +
//                            "-COUNTIF(C" + rowCount + ":AG" + rowCount + ",\"*H/*\")/2" +
//                            "+COUNTIF(C" + rowCount + ":AG" + rowCount + ", \"*CT*\")" +
//                            "+COUNTIF(C" + rowCount + ":AG" + rowCount + ", \"*LB*\")"); // Tổng ngày làm việc
//                    cell.setCellStyle(styleBodyCenter);
//                }
//                if (i == 34) {
//                    cell.setCellFormula("AH" + rowCount + ""); // Tổng ngày hưởng lương
//                    cell.setCellStyle(styleBodyCenter);
//                }
//            }
//        }


//        for (int k = 0; k < count; k++) {
//            row = sheet.createRow(rowCount++);
//            cell = row.createCell(0);
//            cell.setCellValue(tt++);
//            cell.setCellStyle(styleBody);
//            sheet.setColumnWidth(0, 1500);
//
//            cell = row.createCell(1);
//            cell.setCellValue(listUsers.get(k).getFullName());
//            cell.setCellStyle(styleBody);
//
//
//            List<String> signs = new ArrayList<>();
//            for (LogDetail l : listLogs) {
//                if (l.getDate_log().getMonthValue() == month) {
//                    if (l.getUser().getCode() == listUsers.get(k).getCode()) {
//                        if (l.getDate_log().getDayOfWeek().toString().equals("SATURDAY") ||
//                                l.getDate_log().getDayOfWeek().toString().equals("SUNDAY")) {
//                            signs.add("NT");
//                        } else if (l.getSigns().getName().toString() == "") {
//                            signs.add("KL");
//                        } else {
//                            if (l.getSigns().getName().toString() == "H_KL") {
//                                signs.add("H/KL");
//                            } else if (l.getSigns().getName().toString() == "KL_H") {
//                                signs.add("KL/H");
//                            } else {
//                                signs.add(l.getSigns().getName().toString());
//                            }
//                        }
//                    }
//                }
//            }
//
//
//
//
//
//            if (signs.size() == 29) {
//                for (int i = 2; i <= 32; i++) {
//                    // Ký tự tính công
//                    if (i <= 30) {
//                        cell = row.createCell(i);
//                        cell.setCellValue(signs.get(i - 2));
//                        cell.setCellStyle(styleBody);
//                    }
//                    if (i >= 31) {
//                        cell = row.createCell(i);
//                        cell.setCellValue("");
//                        cell.setCellStyle(styleBody);
//                    }
//                }
//            }
//
//            if (signs.size() == 28) {
//                for (int i = 2; i <= 32; i++) {
//                    // Ký tự tính công
//                    if (i <= 29) {
//                        cell = row.createCell(i);
//                        cell.setCellValue(signs.get(i - 2));
//                        cell.setCellStyle(styleBody);
//                    }
//                    if (i >= 30) {
//                        cell = row.createCell(i);
//                        cell.setCellValue("");
//                        cell.setCellStyle(styleBody);
//                    }
//                }
//            }
//            if (signs.size() == 30) {
//                for (int i = 2; i <= 32; i++) {
//                    // Ký tự tính công
//                    if (i <= 31) {
//                        cell = row.createCell(i);
//                        cell.setCellValue(signs.get(i - 2));
//                        cell.setCellStyle(styleBody);
//                    }
//                    if (i >= 32) {
//                        cell = row.createCell(i);
//                        cell.setCellValue("");
//                        cell.setCellStyle(styleBody);
//                    }
//                }
//            }
//            if (signs.size() == 31) {
//                for (int i = 2; i <= 32; i++) {
//                    // Ký tự tính công
//                    if (i <= 32) {
//                        cell = row.createCell(i);
//                        cell.setCellValue(signs.get(i - 2));
//                        cell.setCellStyle(styleBody);
//                    }
//
//                }
//            }
//
//
//            // Tổng
//            for (int i = 33; i <= 34; i++) {
//                cell = row.createCell(i);
//                cell.setCellStyle(styleBody);
//                if (i == 33) {
//                    cell.setCellFormula("COUNTIF(C" + rowCount + ":AG" + rowCount + ", \"*H*\")" +
//                            "-COUNTIF(C" + rowCount + ":AG" + rowCount + ",\"*/H*\")/2" +
//                            "-COUNTIF(C" + rowCount + ":AG" + rowCount + ",\"*H/*\")/2" +
//                            "+COUNTIF(C" + rowCount + ":AG" + rowCount + ", \"*CT*\")" +
//                            "+COUNTIF(C" + rowCount + ":AG" + rowCount + ", \"*LB*\")"); // Tổng ngày làm việc
//                    cell.setCellStyle(styleBodyCenter);
//                }
//                if (i == 34) {
//                    cell.setCellFormula("AH" + rowCount + ""); // Tổng ngày hưởng lương
//                    cell.setCellStyle(styleBodyCenter);
//                }
//            }
//
//        }


        row = sheet.createRow(rowCount++);
        for (int i = 0; i <= 32; i++) {
            cell = row.createCell(i);
            cell.setCellStyle(styleBodyCenter);
        }
        cell = row.createCell(0);
        cell.setCellValue("Tổng cộng");
        cell.setCellStyle(styleBodyCenter);


        rowIndex = sheet.getLastRowNum();
        cell = row.createCell(33);
        cell.setCellFormula("SUM(AH7:AH" + rowIndex + ")");
        cell.setCellStyle(styleBodyCenter);


        cell = row.createCell(34);
        cell.setCellFormula("SUM(AI7:AI" + rowIndex + ")");
        cell.setCellStyle(styleBodyCenter);

        rowIndex = sheet.getLastRowNum();

    }

    // Create Footer
    private void writeFooter() {
        Row row = null;
        Cell cell;
        int rowCurrent = rowIndex + 1;

        //style Bold
        CellStyle styleBold = workbook.createCellStyle();
        styleBold.setAlignment(HorizontalAlignment.CENTER);
        //font Bold
        XSSFFont fontBold = workbook.createFont();
        IndexedColorMap colorMap = workbook.getStylesSource().getIndexedColors();
        fontBold.setColor(new XSSFColor(Color.decode("#000080"), colorMap));
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
        fontThin.setColor(new XSSFColor(Color.decode("#000080"), colorMap));
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
        sheet.createFreezePane(0, 6, 0, 6);

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
