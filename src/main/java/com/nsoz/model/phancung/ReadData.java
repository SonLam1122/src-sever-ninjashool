package com.nsoz.model.phancung;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ReadData {
    public static final int COLUMN_ID = 0;
    public static final int COLUMN_NAME = 1;
    public static final int COLUMN_STATUS = 2;
    public static final int COLUMN_TYPE = 3;

    public static List<UserModel> readFile(String pathFile) throws IOException {
        List<UserModel> userModels = new ArrayList<>();
        InputStream in = Files.newInputStream(new File(pathFile).toPath());
        Workbook workbook = getWorkbook(in, pathFile);
        Sheet sheet = workbook.getSheetAt(0);
        Iterator<Row> iterator = sheet.iterator();
        while (iterator.hasNext()) {
            Row nextRow = iterator.next();
            if (nextRow.getRowNum() == 0) {
                continue;
            }
            Iterator<Cell> cellIterator = nextRow.cellIterator();
            UserModel userModel = new UserModel();
            while (cellIterator.hasNext()) {
                Cell cell = cellIterator.next();
                Object cellValue = getCellValue(cell);
                if (cellValue == null || cellValue.toString().isEmpty()) {
                    continue;
                }
                int columnIndex = cell.getColumnIndex();
                switch (columnIndex) {
                    case COLUMN_ID:
                        userModel.setStt(String.valueOf(getCellValue(cell)));
                        break;
                    case COLUMN_NAME:
                        userModel.setIngame((String) getCellValue(cell));
                        break;
                    case COLUMN_STATUS:
                        userModel.setStatus(String.valueOf(getCellValue(cell)));
                        break;
                    case COLUMN_TYPE:
                        userModel.setType(String.valueOf(getCellValue(cell)));
                        break;
                    default:
                        break;
                }

            }
            userModels.add(userModel);
        }

        workbook.close();
        in.close();
        return userModels;
    }

    private static Workbook getWorkbook(InputStream inputStream, String excelFilePath) throws IOException {
        Workbook workbook = null;
        if (excelFilePath.endsWith("xlsx")) {
            workbook = new XSSFWorkbook(inputStream);
        } else if (excelFilePath.endsWith("xls")) {
            workbook = new HSSFWorkbook(inputStream);
        } else {
            throw new IllegalArgumentException("The specified file is not Excel file");
        }

        return workbook;
    }

    public static boolean update(String path, int row) {
        try {
            FileInputStream file = new FileInputStream(path);

            XSSFWorkbook workbook = new XSSFWorkbook(file);
            XSSFSheet sheet = workbook.getSheetAt(0);
            Cell cell = null;
            XSSFRow sheetrow = sheet.getRow(row);
            if (sheetrow == null) {
                sheetrow = sheet.createRow(row);
            }
            cell = sheetrow.getCell(2);
            if (cell == null) {
                cell = sheetrow.createCell(2);
            }
            cell.setCellValue(1);
            file.close();
            FileOutputStream outFile = new FileOutputStream(new File(path));
            workbook.write(outFile);
            outFile.close();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void updateStatus(String path, String stt, String status) {
        try {
            InputStream in = Files.newInputStream(new File(path).toPath());
            Workbook workbook = new XSSFWorkbook(in);
            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                Cell idCell = row.getCell(0);
                if (idCell != null) {
                    if (idCell.getCellStyle().equals(CellType.NUMERIC)) {
                        String sttUser = String.valueOf(idCell.getNumericCellValue());
                        if (sttUser.equals(stt)) {
                            Cell statusCell = row.createCell(2);
                            statusCell.setCellValue(status);
                            break;
                        }
                    }
                }
            }
            try {
                FileOutputStream outputStream = new FileOutputStream(path);
                workbook.write(outputStream);
                workbook.close();
                in.close();
                outputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static Object getCellValue(Cell cell) {
        CellType cellType = cell.getCellTypeEnum();
        Object cellValue = null;
        switch (cellType) {
            case BOOLEAN:
                cellValue = cell.getBooleanCellValue();
                break;
            case FORMULA:
                Workbook workbook = cell.getSheet().getWorkbook();
                FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
                cellValue = evaluator.evaluate(cell).getNumberValue();
                break;
            case NUMERIC:
                cellValue = cell.getNumericCellValue();
                break;
            case STRING:
                cellValue = cell.getStringCellValue();
                break;
            default:
                break;
        }

        return cellValue;
    }
}
