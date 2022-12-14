package com.vmg.scrum.excel;


import com.vmg.scrum.model.User;
import com.vmg.scrum.model.excel.LogDetail;
import com.vmg.scrum.payload.response.UserLogDetail;
import com.vmg.scrum.repository.DepartmentRepository;
import com.vmg.scrum.repository.LogDetailRepository;
import com.vmg.scrum.repository.UserRepository;
import org.apache.poi.sl.usermodel.Notes;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class ExcelExporterReport {
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

    static int rowIndex = 0;

    public ExcelExporterReport(List<LogDetail> listLogs, Long id, int month, DepartmentRepository departmentRepository,
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

    public ExcelExporterReport(List<LogDetail> listLogs, int month, DepartmentRepository departmentRepository,
                               UserRepository userRepository,
                               LogDetailRepository logDetailRepository) {
        this.listLogs = listLogs;
        workbook = new XSSFWorkbook();
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


        sheet = workbook.createSheet("B???ng Ch???m C??ng");


        Row row = sheet.createRow(1);
        Cell cell = row.createCell(0);
        cell.setCellValue("B???NG CH???M C??NG");
        cell.setCellStyle(style);


        row = sheet.createRow(2);
        cell = row.createCell(0);
        cell.setCellValue("Th??ng " + month + "/" + 2022);
        cell.setCellStyle(style);

        row = sheet.createRow(3);
        cell = row.createCell(1);
        if (id == 0) {
            cell.setCellValue("T???t c??? c??c b??? ph???n");
        } else {
            cell.setCellValue("B??? ph???n: " + departmentRepository.getById(id).getName());
        }
//        cell.setCellValue("B??? ph???n: " + departmentRepository.getById(id).getName());
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
        cell.setCellValue("H??? v?? t??n");
        cell.setCellStyle(styleTitle);
        sheet.setColumnWidth(1, 5000);

        cell = row.createCell(2);
        cell.setCellValue("Ng??y trong th??ng");
        cell.setCellStyle(styleTitle);

        cell = row.createCell(33);
        cell.setCellValue("T???ng s???\n" + " ng??y\n" + " l??m vi???c \n" + "th???c t???");
        cell.setCellStyle(styleTitle);
        sheet.setColumnWidth(33, 2000);

        cell = row.createCell(34);
        cell.setCellValue("T???ng s???\n" + " ng??y h?????ng\n" + " l????ng");
        cell.setCellStyle(styleTitle);
        sheet.setColumnWidth(34, 2000);

        cell = row.createCell(35);
        cell.setCellValue("Ngh??? ph??p");
        cell.setCellStyle(styleTitle);
        sheet.setColumnWidth(35, 2000);

        cell = row.createCell(36);
        cell.setCellValue("Ph??p t???n");
        cell.setCellStyle(styleTitle);
        sheet.setColumnWidth(36, 4000);


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

        cell = row.createCell(35);
        cell.setCellStyle(styleTitle);
        sheet.setColumnWidth(35, 2000);
        row.setHeight((short) 900);

        cell = row.createCell(36);
        cell.setCellStyle(styleTitle);
        sheet.setColumnWidth(36, 4000);
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

        //comment
        CreationHelper creationHelper = (XSSFCreationHelper) workbook.getCreationHelper();
        XSSFDrawing drawing = sheet.createDrawingPatriarch();


        // style  Body
        CellStyle styleBody = workbook.createCellStyle();
        styleBody.setBorderBottom(BorderStyle.THIN);
        styleBody.setBorderTop(BorderStyle.THIN);
        styleBody.setBorderLeft(BorderStyle.THIN);
        styleBody.setBorderRight(BorderStyle.THIN);
        styleBody.setAlignment(HorizontalAlignment.CENTER);
        styleBody.setVerticalAlignment(VerticalAlignment.CENTER);
        styleBody.setWrapText(true);

        CellStyle styleBodyCenter = workbook.createCellStyle();
        styleBodyCenter.setBorderBottom(BorderStyle.THIN);
        styleBodyCenter.setBorderTop(BorderStyle.THIN);
        styleBodyCenter.setBorderLeft(BorderStyle.THIN);
        styleBodyCenter.setBorderRight(BorderStyle.THIN);
        styleBodyCenter.setAlignment(HorizontalAlignment.CENTER);
        styleBodyCenter.setVerticalAlignment(VerticalAlignment.CENTER);

        CellStyle styleBodyColor = workbook.createCellStyle();
        styleBodyColor.setBorderBottom(BorderStyle.THIN);
        styleBodyColor.setBorderTop(BorderStyle.THIN);
        styleBodyColor.setBorderLeft(BorderStyle.THIN);
        styleBodyColor.setBorderRight(BorderStyle.THIN);
        styleBodyColor.setFillForegroundColor(IndexedColors.TAN.getIndex());
        styleBodyColor.setAlignment(HorizontalAlignment.CENTER);
        styleBodyColor.setVerticalAlignment(VerticalAlignment.CENTER);
        styleBodyColor.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        // font Body
        XSSFFont fontBody = workbook.createFont();
        fontBody.setFontName("Times New Roman");
        fontBody.setFontHeight(10);
        styleBody.setFont(fontBody);
        styleBodyCenter.setFont(fontBody);
        styleBodyColor.setFont(fontBody);


        // Edit Body Table
        int rowCount = 6;
        int tt = 1;
        String tinhNgayLamViec = "COUNTIF(C" + rowCount + ":AG" + rowCount + ", \"*H*\")" +
                "-COUNTIF(C" + rowCount + ":AG" + rowCount + ",\"*/H*\")/2" +
                "-COUNTIF(C" + rowCount + ":AG" + rowCount + ",\"*H/*\")/2" +
                "+COUNTIF(C" + rowCount + ":AG" + rowCount + ", \"*CT*\")" +
                "+COUNTIF(C" + rowCount + ":AG" + rowCount + ", \"*LB*\")";

        String tinhNgayHuongLuong = "AH" + rowCount + "" +
                "+COUNTIF(C" + rowCount + ":AG" + rowCount + ",\"*??*\")" +
                "+COUNTIF(C" + rowCount + ":AG" + rowCount + ",\"L\")" +
                "+COUNTIF(C" + rowCount + ":AG" + rowCount + ",\"*TC*\")" +
                "+(COUNTIF(C" + rowCount + ":AG" + rowCount + ",\"*P*\")" +
                "+(COUNTIF(C" + rowCount + ":AG" + rowCount + ",\"*P/*\")/2)" +
                "+(COUNTIF(C" + rowCount + ":AG" + rowCount + ",\"*/P*\")/2))";
        List<User> listUsers = new ArrayList<>();
//        int count = departmentRepository.findById(id).get().getUsers().size();
        if (id == 0) {
            listUsers = userRepository.findAll();
        } else {
            listUsers = userRepository.findAllByDepartments_Id(id);
        }
//        List<User> listUsers = userRepository.findAllByDepartments_Id(id);
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

        // in t???t c??? t??n c??c nh??n vi??n
        for (User user : listUsers) {
            row = sheet.createRow(rowCount++);
            cell = row.createCell(0);
            cell.setCellValue(tt++);
            cell.setCellStyle(styleBody);
            sheet.setColumnWidth(0, 1200);

            cell = row.createCell(1);
            cell.setCellValue(user.getFullName());
            cell.setCellStyle(styleBody);

            // in g???ch -
            for (int i = 0; i <= 30; i++) {
                if (user != null) {
                    sheet.setColumnWidth(i + 2, 1500);

                    cell = row.createCell(i + 2);
                    cell.setCellValue("-");
                    cell.setCellStyle(styleBody);

                    // T???ng
                    for (int k = 33; k <= 35; k++) {
                        cell = row.createCell(k);
                        cell.setCellStyle(styleBody);
                        if (k == 33) {
                            cell.setCellFormula("COUNTIF(C" + rowCount + ":AG" + rowCount + ", \"*H*\")" +
                                    "-COUNTIF(C" + rowCount + ":AG" + rowCount + ",\"*/H*\")/2" +
                                    "-COUNTIF(C" + rowCount + ":AG" + rowCount + ",\"*H/*\")/2" +
                                    "+COUNTIF(C" + rowCount + ":AG" + rowCount + ", \"*CT*\")" +
                                    "+COUNTIF(C" + rowCount + ":AG" + rowCount + ", \"*LB*\")"); // T???ng ng??y l??m vi???c
                            cell.setCellStyle(styleBodyCenter);
                        }
                        if (k == 34) {
                            // T???ng ng??y h?????ng l????ng
                            cell.setCellFormula("AH" + rowCount + "" +
                                    "+COUNTIF(C" + rowCount + ":AG" + rowCount + ",\"*??*\")" +
                                    "+COUNTIF(C" + rowCount + ":AG" + rowCount + ",\"L\")" +
                                    "+COUNTIF(C" + rowCount + ":AG" + rowCount + ",\"*TC*\")" +
                                    "+(COUNTIF(C" + rowCount + ":AG" + rowCount + ",\"*P*\")" +
                                    "+(COUNTIF(C" + rowCount + ":AG" + rowCount + ",\"*P/*\")/2)" +
                                    "+(COUNTIF(C" + rowCount + ":AG" + rowCount + ",\"*/P*\")/2))"); // T???ng ng??y h?????ng l????ng
                            cell.setCellStyle(styleBodyCenter);
                        }
                        if (k == 35) {
                            cell.setCellFormula("(COUNTIF(C" + rowCount + ":AG" + rowCount + ",\"P\")" +
                                    "+(COUNTIF(C" + rowCount + ":AG" + rowCount + ",\"P/*\")/2)" +
                                    "+(COUNTIF(C" + rowCount + ":AG" + rowCount + ",\"*/P\")/2))"); // T???ng ng??y h?????ng l????ng
                            cell.setCellStyle(styleBodyCenter);
                        }
                    }
                }
            }

            // G??n c??c k?? t??? ch???m c??ng v??o c???t t????ng ???ng
            for (UserLogDetail ul : userLogDetails) {
                if (user.getCode() == ul.getCode()) {
                    String sign = null;
                    List<LogDetail> logDetails = ul.getLogDetail();
                    for (LogDetail logDetail : logDetails) {
                        cell = row.createCell(logDetail.getDateLog().getDayOfMonth() + 1);
                        if (logDetail.getSigns() == null) {
                            cell.setCellValue("-");
                            cell.setCellStyle(styleBodyCenter);
                        } else if (logDetail.getSigns().getName().toString().equals("H_KL")) {
                            cell.setCellValue("H/KL");
                            cell.setCellStyle(styleBodyCenter);
                            //Comment
//                            if (logDetail.getReason() != null) {
////                                ClientAnchor clientAnchor = drawing.createAnchor(0, 0, 0, 0, 0, 2, 7, 7);
//                                ClientAnchor clientAnchor = creationHelper.createClientAnchor();
//                                clientAnchor.setCol1(cell.getColumnIndex());
//                                clientAnchor.setCol2(cell.getColumnIndex() + 5);
//                                clientAnchor.setRow1(row.getRowNum());
//                                clientAnchor.setRow2(row.getRowNum() + 2);
//
//                                Comment comment = (Comment) drawing.createCellComment(clientAnchor);
//                                RichTextString richTextString = creationHelper.createRichTextString(logDetail.getReason());
//                                comment.setString(richTextString);
//                                comment.setAuthor("Apache POI");
//                                cell.setCellComment(comment);
//                            }
                        } else if (logDetail.getSigns().getName().toString().equals("KL_H")) {
                            cell.setCellValue("KL/H");
                            cell.setCellStyle(styleBodyCenter);
                            //Comment
//                            if (logDetail.getReason() != null) {
//                                ClientAnchor clientAnchor = creationHelper.createClientAnchor();
//                                clientAnchor.setCol1(cell.getColumnIndex());
//                                clientAnchor.setCol2(cell.getColumnIndex() + 5);
//                                clientAnchor.setRow1(row.getRowNum());
//                                clientAnchor.setRow2(row.getRowNum() + 2);
//
//
//                                Comment comment = (Comment) drawing.createCellComment(clientAnchor);
//                                RichTextString richTextString = creationHelper.createRichTextString(logDetail.getReason());
//                                comment.setString(richTextString);
//                                comment.setAuthor("Apache POI");
//                                cell.setCellComment(comment);
//                            }
                        } else {
                            cell.setCellValue(logDetail.getSigns().getName().toString());
                            cell.setCellStyle(styleBodyCenter);
                            //Comment
//                            if (logDetail.getReason() != null) {
//                                ClientAnchor clientAnchor = creationHelper.createClientAnchor();
//                                clientAnchor.setCol1(cell.getColumnIndex());
//                                clientAnchor.setCol2(cell.getColumnIndex() + 5);
//                                clientAnchor.setRow1(row.getRowNum());
//                                clientAnchor.setRow2(row.getRowNum() + 2);
//
//
//                                Comment comment = (Comment) drawing.createCellComment(clientAnchor);
//                                RichTextString richTextString = creationHelper.createRichTextString(logDetail.getReason());
//                                comment.setString(richTextString);
//                                comment.setAuthor("Apache POI");
//                                cell.setCellComment(comment);
//                            }
                        }


                        if (logDetail.getDateLog().getDayOfWeek().toString() == "SATURDAY" ||
                                logDetail.getDateLog().getDayOfWeek().toString() == "SUNDAY") {
                            if (logDetail.getSigns() == null) {
                                cell.setCellValue("-");
                                cell.setCellStyle(styleBodyColor);
                            } else if (logDetail.getSigns().getName().toString().equals("H_KL")) {
                                cell.setCellValue("H/KL");
                                cell.setCellStyle(styleBodyColor);
                            } else if (logDetail.getSigns().getName().toString().equals("KL_H")) {
                                cell.setCellValue("KL/H");
                                cell.setCellStyle(styleBodyColor);
                            } else {
                                cell.setCellValue(logDetail.getSigns().getName().toString());
                                cell.setCellStyle(styleBodyColor);
                            }

                        }

                    }

                    // T???ng
                    for (int k = 33; k <= 35; k++) {
                        cell = row.createCell(k);
                        cell.setCellStyle(styleBody);
                        if (k == 33) {
                            cell.setCellFormula("COUNTIF(C" + rowCount + ":AG" + rowCount + ", \"*H*\")" +
                                    "-COUNTIF(C" + rowCount + ":AG" + rowCount + ",\"*/H*\")/2" +
                                    "-COUNTIF(C" + rowCount + ":AG" + rowCount + ",\"*H/*\")/2" +
                                    "+COUNTIF(C" + rowCount + ":AG" + rowCount + ", \"*CT*\")" +
                                    "+COUNTIF(C" + rowCount + ":AG" + rowCount + ", \"*LB*\")"); // T???ng ng??y l??m vi???c
                            cell.setCellStyle(styleBodyCenter);
                        }

                        if (k == 34) {
                            // T???ng ng??y h?????ng l????ng
                            cell.setCellFormula("AH" + rowCount + "" +
                                    "+COUNTIF(C" + rowCount + ":AG" + rowCount + ",\"*??*\")" +
                                    "+COUNTIF(C" + rowCount + ":AG" + rowCount + ",\"L\")" +
                                    "+COUNTIF(C" + rowCount + ":AG" + rowCount + ",\"*TC*\")" +
                                    "+(COUNTIF(C" + rowCount + ":AG" + rowCount + ",\"*P*\")" +
                                    "+(COUNTIF(C" + rowCount + ":AG" + rowCount + ",\"*P/*\")/2)" +
                                    "+(COUNTIF(C" + rowCount + ":AG" + rowCount + ",\"*/P*\")/2))"); // T???ng ng??y h?????ng l????ng
                            cell.setCellStyle(styleBodyCenter);
                        }
                        if (k == 35) {
                            cell.setCellFormula("(COUNTIF(C" + rowCount + ":AG" + rowCount + ",\"P\")" +
                                    "+(COUNTIF(C" + rowCount + ":AG" + rowCount + ",\"P/*\")/2)" +
                                    "+(COUNTIF(C" + rowCount + ":AG" + rowCount + ",\"*/P\")/2))");
                            cell.setCellStyle(styleBodyCenter);
                        }
                    }
                }

            }

        }

        row = sheet.createRow(rowCount++);
        for (int i = 0; i <= 32; i++) {
            cell = row.createCell(i);
            cell.setCellStyle(styleBodyCenter);
        }
        cell = row.createCell(0);
        cell.setCellValue("T???ng c???ng");
        cell.setCellStyle(styleBodyCenter);


        rowIndex = sheet.getLastRowNum();
        cell = row.createCell(33);
        cell.setCellFormula("SUM(AH7:AH" + rowIndex + ")");
        cell.setCellStyle(styleBodyCenter);


        cell = row.createCell(34);
        cell.setCellFormula("SUM(AI7:AI" + rowIndex + ")");
        cell.setCellStyle(styleBodyCenter);

        cell = row.createCell(35);
        cell.setCellFormula("SUM(AJ7:AJ" + rowIndex + ")");
        cell.setCellStyle(styleBodyCenter);

        rowIndex = sheet.getLastRowNum();

        System.out.println(rowIndex);
        for (int i = 6; i < sheet.getLastRowNum(); i++) {
            sheet.getRow(i).setHeight((short) 500);
        }
        sheet.getRow(sheet.getLastRowNum()).setHeight((short) 600);
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
        cell.setCellValue("K?? hi???u: ");
        cell.setCellStyle(styleBold);

        row = sheet.createRow(++rowCurrent);
        cell = row.createCell(2);
        cell.setCellValue("H - L??m h??nh ch??nh");
        cell.setCellStyle(styleThinLeft);
        cell = row.createCell(20);
        cell.setCellValue("C - L??m ca chi???u");
        cell.setCellStyle(styleThinLeft);

        row = sheet.createRow(++rowCurrent);
        cell = row.createCell(2);
        cell.setCellValue("P - Ngh??? ph??p");
        cell.setCellStyle(styleThinLeft);
        cell = row.createCell(20);
        cell.setCellValue("?? - Ngh??? ???m");
        cell.setCellStyle(styleThinLeft);

        row = sheet.createRow(++rowCurrent);
        cell = row.createCell(2);
        cell.setCellValue("CT - C??ng t??c");
        cell.setCellStyle(styleThinLeft);
        cell = row.createCell(20);
        cell.setCellValue("C?? - Ngh??? Ch??? ????? ( Ngh??? ?????, Kh??m thai,S???y thai,..)");
        cell.setCellStyle(styleThinLeft);

        row = sheet.createRow(++rowCurrent);
        cell = row.createCell(2);
        cell.setCellValue("TC - Ngh??? ti??u chu???n (C?????i, T??? th??n ph??? m???u m???t,...)");
        cell.setCellStyle(styleThinLeft);

        ++rowCurrent;

        // Ch??? k??
        row = sheet.createRow(++rowCurrent);
        cell = row.createCell(0);
        cell.setCellValue("Gi??m ?????c TT/ B??? ph???n");
        cell.setCellStyle(styleBold);


        cell = row.createCell(25);
        cell.setCellValue("TP. QTNNL&DVNB");
        cell.setCellStyle(styleBold);
        //Merge footer

        sheet.addMergedRegion(new CellRangeAddress(rowCurrent, rowCurrent, 0, 3));
        sheet.addMergedRegion(new CellRangeAddress(rowCurrent, rowCurrent, 25, 34));

        row = sheet.createRow(++rowCurrent);
        cell = row.createCell(0);
        cell.setCellValue("K?? v?? Ghi r?? H??? T??n");
        cell.setCellStyle(styleThinCenter);

        cell = row.createCell(25);
        cell.setCellValue("K?? v?? Ghi r?? H??? T??n");
        cell.setCellStyle(styleThinCenter);

        //Merge footer
        sheet.addMergedRegion(new CellRangeAddress(rowCurrent, rowCurrent, 0, 3));
        sheet.addMergedRegion(new CellRangeAddress(rowCurrent, rowCurrent, 25, 34));


        //Freeze Pane
//        sheet.createFreezePane(0, 6, 0, 6);
        sheet.createFreezePane(2, 6);


        // merge in header (row 1-4)
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 34));
        sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 34));
        sheet.addMergedRegion(new CellRangeAddress(2, 2, 0, 34));
        sheet.addMergedRegion(new CellRangeAddress(3, 3, 1, 6));
        sheet.addMergedRegion(new CellRangeAddress(3, 3, 7, 34));


        // Merge in table
        // C??CH 1
        // "TT"
        sheet.addMergedRegion(CellRangeAddress.valueOf("B5:B6"));
        // "H??? v?? t??n"
        sheet.addMergedRegion(CellRangeAddress.valueOf("A5:A6"));
        // "T???ng s??? ng??y l??m vi???c th???c t???"
        sheet.addMergedRegion(CellRangeAddress.valueOf("AH5:AH6"));
        // "T???ng s??? ng??y h?????ng l????ng"
        sheet.addMergedRegion(CellRangeAddress.valueOf("AI5:AI6"));

        // C??CH 2
        // "Ng??y trong th??ng"
        sheet.addMergedRegion(new CellRangeAddress(4, 4, 2, 32));
        // ?? "t???ng c???ng"
        sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, 0, 32));
        // ?? "Ngh??? ph??p"
        sheet.addMergedRegion(CellRangeAddress.valueOf("AJ5:AJ6"));
        // ?? "Ph??p t???n"
        sheet.addMergedRegion(CellRangeAddress.valueOf("AK5:AK6"));

        rowIndex = sheet.getLastRowNum();

//        sheet.getRow(6).setHeight((short) 10000);
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
