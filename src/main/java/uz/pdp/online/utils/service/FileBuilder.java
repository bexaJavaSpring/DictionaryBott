package uz.pdp.online.utils.service;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import uz.pdp.online.utils.UserPerson;
import uz.pdp.online.utils.WordHistory;

import java.io.*;
import java.lang.reflect.Type;
import java.util.List;
import java.util.stream.Collectors;

import static uz.pdp.online.Database.userList;
import static uz.pdp.online.Database.wordHistories;

public class FileBuilder {
    public static void getCheck(UserPerson guestUser) {
        try (PdfWriter writer = new PdfWriter("src/main/resources/BotCheck.pdf")) {
            PdfDocument document = new PdfDocument(writer);
            document.addNewPage();
            Document document1 = new Document(document);
            FileOutputStream out = new FileOutputStream("src/main/resources/BotCheck.pdf");

            Paragraph paragraph = new Paragraph("Check for costumer");
            paragraph.setTextAlignment(TextAlignment.CENTER);

            document1.add(paragraph);

            float[] floats = {400f, 400f};
            Table table = new Table(floats);
            table.addCell("Language");
            table.addCell("Word");

            for (WordHistory wordHistory : wordHistories) {
                if (wordHistory.getChatId().equals(guestUser.getChatId())) {
                    table.addCell(wordHistory.getLanguage());
                    table.addCell(wordHistory.getWord());
                }
            }

            document1.add(table);
            out.close();
            document1.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void getExcelFile() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(new File("src/main/resources/userList.json")));
            String data = reader.lines().collect(Collectors.joining());
            Type type = new TypeToken<List<UserPerson>>() {
            }.getType();
            userList = new Gson().fromJson(data, type);

            XSSFWorkbook workbook = new XSSFWorkbook();
            FileOutputStream outputStream = new FileOutputStream("src/main/resources/userList.xlsx");

            XSSFSheet sheet = workbook.createSheet("sheet-1");

            sheet.setColumnWidth(0, 7000);
            sheet.setColumnWidth(1, 7000);

            XSSFRow row = sheet.createRow(1);


            row.createCell(0).setCellValue("first name");
            row.createCell(1).setCellValue("Phone number");


            for (int i = 0; i < userList.size(); i++) {
                UserPerson excelfile = userList.get(i);
                row = sheet.createRow(i + 2);
                row.createCell(0).setCellValue(excelfile.getFirstName());
                row.createCell(1).setCellValue(excelfile.getPhoneNumber());

            }
            workbook.write(outputStream);
            outputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void getAllWordsForAdmin(UserPerson guestUser) {
        try (PdfWriter writer = new PdfWriter("src/main/resources/AllWord.pdf")) {
            PdfDocument pdfDocument = new PdfDocument(writer);
            FileOutputStream outputStream = new FileOutputStream("src/main/resources/AllWord.pdf");
            pdfDocument.addNewPage();
            Document document = new Document(pdfDocument);
            Paragraph paragraph = new Paragraph("Check for costumer");
            paragraph.setTextAlignment(TextAlignment.CENTER);

            document.add(paragraph);

            float[] floats = {400f, 400f};
            Table table = new Table(floats);
            table.addCell("Language");
            table.addCell("Word");

            for (WordHistory wordHistory : wordHistories) {
                table.addCell(wordHistory.getLanguage());
                table.addCell(wordHistory.getWord());
            }

            document.add(table);
            outputStream.close();
            document.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
