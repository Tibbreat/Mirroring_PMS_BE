package sep490.g13.pms_be.utils;

import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import sep490.g13.pms_be.model.request.children.AddChildrenRequest;
import sep490.g13.pms_be.model.request.children.AddParentRequest;
import sep490.g13.pms_be.repository.ChildrenRepo;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.*;

@Service
public class ExcelUtils {

    @Autowired
    ChildrenRepo childrenRepo;

    public static boolean isValidExcelFile(MultipartFile file) {
        String contentType = file.getContentType();
        return "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet".equals(contentType) ||
                "application/vnd.ms-excel".equals(contentType);
    }

    public List<AddChildrenRequest> getChildrenDataFromExcel(InputStream inputStream) {
        List<AddChildrenRequest> lc = new ArrayList<>();
        Set<String> uniqueRowIdentifiers = new HashSet<>();

        try (Workbook workbook = WorkbookFactory.create(inputStream)) {
            Sheet sheet = workbook.getSheet("Sheet1");
            int rowIndex = 0;

            validateColumnCount(sheet, 15);

            for (Row row : sheet) {
                if (rowIndex == 0) {
                    rowIndex++;
                    continue; //Bỏ qua dòng header
                }

                AddChildrenRequest addChildRequest = new AddChildrenRequest();
                AddParentRequest father = new AddParentRequest();
                AddParentRequest mother = new AddParentRequest();

                try {
                    addChildRequest.setChildName(getCellValue(row, 0, rowIndex).trim());
                    addChildRequest.setChildBirthDate(getDateCellValue(row, 1, rowIndex));
                    addChildRequest.setGender(getGenderValue(row, 2, rowIndex).trim());
                    addChildRequest.setNationality(getCellValue(row, 3, rowIndex).trim());
                    addChildRequest.setReligion(getCellValue(row, 4, rowIndex).trim());
                    addChildRequest.setBirthAddress(getCellValue(row, 5, rowIndex).trim());
                    addChildRequest.setChildAddress(getCellValue(row, 6, rowIndex).trim());

                    father.setFullName(getCellValue(row, 7, rowIndex).trim());
                    father.setPhone(getCellValue(row, 8, rowIndex).trim());
                    father.setIdCardNumber(getCellValue(row, 9, rowIndex).trim());

                    mother.setFullName(getCellValue(row, 10, rowIndex).trim());
                    mother.setPhone(getCellValue(row, 11, rowIndex).trim());
                    mother.setIdCardNumber(getCellValue(row, 12, rowIndex).trim());

                    addChildRequest.setIsDisabled(getBooleanValue(row, 13, rowIndex));
                    addChildRequest.setNote(getOptionalCellValue(row, 14) != null ? Objects.requireNonNull(getOptionalCellValue(row, 14)).trim() : null);

                } catch (Exception e) {
                    throw new RuntimeException("Error in row " + rowIndex + ", cell " + getColumnName(row.getLastCellNum()) + ": " + e.getMessage(), e);
                }

                addChildRequest.setFather(father);
                addChildRequest.setMother(mother);

                // Kiểm tra dữ liệu bị trùng
                String uniqueIdentifier = generateRowIdentifier(addChildRequest);
                if (!uniqueRowIdentifiers.contains(uniqueIdentifier)) {
                    uniqueRowIdentifiers.add(uniqueIdentifier);
                    lc.add(addChildRequest);
                }
                for (AddChildrenRequest child : lc) {
                    if (childrenRepo.childrenAlreadyExist(child.getChildName(), child.getFather().getIdCardNumber(), child.getMother().getIdCardNumber())) {
                        child.setDuplicate(true);
                    }
                }
                rowIndex++;
            }
        } catch (IOException e) {
            throw new RuntimeException("Error reading Excel file", e);
        }
        return lc;
    }

    private static String getColumnName(int columnNumber) {
        StringBuilder columnName = new StringBuilder();
        while (columnNumber > 0) {
            int remainder = (columnNumber - 1) % 26;
            columnName.insert(0, (char) (remainder + 'A'));
            columnNumber = (columnNumber - 1) / 26;
        }
        return columnName.toString();
    }

    private static void validateColumnCount(Sheet sheet, int requiredColumnCount) {
        Row headerRow = sheet.getRow(0);
        if (headerRow == null || headerRow.getPhysicalNumberOfCells() != requiredColumnCount) {
            throw new RuntimeException("Excel sheet must have exactly " + requiredColumnCount + " columns.");
        }
    }

    private static String getCellValue(Row row, int cellIndex, int rowIndex) {
        Cell cell = row.getCell(cellIndex);
        if (cell == null || cell.getCellType() != CellType.STRING) {
            throw new RuntimeException("Null or invalid value at column " + cellIndex + " in row " + rowIndex);
        }
        return cell.getStringCellValue().trim();
    }

    private static LocalDate getDateCellValue(Row row, int cellIndex, int rowIndex) {
        Cell cell = row.getCell(cellIndex);
        if (cell == null || cell.getCellType() != CellType.NUMERIC || !DateUtil.isCellDateFormatted(cell)) {
            throw new RuntimeException("Null or invalid date at column " + cellIndex + " in row " + rowIndex);
        }
        return cell.getLocalDateTimeCellValue().toLocalDate();
    }

    private static String getGenderValue(Row row, int cellIndex, int rowIndex) {
        Cell cell = row.getCell(cellIndex);
        if (cell == null || cell.getCellType() != CellType.STRING) {
            throw new RuntimeException("Null or invalid gender at column " + cellIndex + " in row " + rowIndex);
        }
        String gender = cell.getStringCellValue().trim();

        // Kiểm tra giá trị hợp lệ cho giới tính và chuyển đổi
        if ("Nam".equalsIgnoreCase(gender)) {
            return "male";
        } else if ("Nữ".equalsIgnoreCase(gender)) {
            return "female";
        } else {
            throw new RuntimeException("Invalid gender value '" + gender + "' at column " + cellIndex + " in row " + rowIndex);
        }
    }


    private static Boolean getBooleanValue(Row row, int cellIndex, int rowIndex) {
        Cell cell = row.getCell(cellIndex);
        if (cell == null || cell.getCellType() == CellType.BLANK) {
            return false;
        }
        if (cell.getCellType() != CellType.STRING) {
            throw new RuntimeException("Invalid value at column " + cellIndex + " in row " + rowIndex);
        }
        String value = cell.getStringCellValue().trim();
        return "Có".equalsIgnoreCase(value);
    }


    private static String getOptionalCellValue(Row row, int cellIndex) {
        Cell cell = row.getCell(cellIndex);
        if (cell == null || cell.getCellType() == CellType.BLANK) {
            return null;
        }
        return cell.getStringCellValue().trim();
    }

    private static String generateRowIdentifier(AddChildrenRequest addChildRequest) {
        return addChildRequest.getChildName().trim() + "|" +
                addChildRequest.getChildBirthDate() + "|" +
                addChildRequest.getGender().trim() + "|" +
                addChildRequest.getNationality().trim() + "|" +
                addChildRequest.getReligion().trim() + "|" +
                addChildRequest.getBirthAddress().trim() + "|" +
                addChildRequest.getChildAddress().trim() + "|" +
                addChildRequest.getFather().getFullName().trim() + "|" +
                addChildRequest.getMother().getFullName().trim();
    }
}
