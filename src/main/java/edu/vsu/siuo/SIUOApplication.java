package edu.vsu.siuo;

import com.spire.doc.Document;
import java.io.IOException;
import edu.vsu.siuo.service.WordManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import com.spire.doc.*;
import com.spire.doc.documents.*;
import com.spire.doc.fields.TextRange;

public class SIUOApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(SIUOApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {

        WordManager wordManager = new WordManager("Tema8 (DALNOMER-PS-bolshoe)");
        wordManager.Write("test");
        //Create a Document object
        Document document = new Document();

        Section sec = document.addSection();
        //add a paragraph
        Paragraph paragraph = sec.addParagraph();
        TextRange tr = paragraph.appendText("Тема 7. Вариант №1 от 04.07.2022 - 10:14");
        tr.getCharacterFormat().setFontSize(12);
        tr.getCharacterFormat().setBold(true);
        paragraph.getFormat().setAfterSpacing(10f);

        paragraph = sec.addParagraph();
        tr = paragraph.appendText("Батарея 122мм гаубицы Д-30 занимает боевой порядок:\n" +
                "ОП:  \tХ = 32290; \tУ = 91238; \th = 170 м («Дон»)\n" +
                "КНП: \tХ = 30031; \tУ = 95266; \th = 77 м («Амур»)\n" +
                "КНП адн: Х = 30031; У = 95266; \th = 77 м («Лена»)\n" +
                "α он = 16-00\n" +
                "В батарее рассчитаны поправки для заряда «2» на 4, 6, 8 км.\n" +
                "В дальности: +133; +288; +406. В направлении: -0-09; -0-13; -0-16.\n" +
                "Командир дивизиона («Лена») передал: «Амур», стой! Цель 21, «радиолокационная станция полевой артиллерии». Дивизионный: αц = 8-66, Дк = 2201, εц = +0-09. Подавить! Я «Лена».\n" +
                "В должности командира батареи провести пристрелку и стрельбу на поражение цели 21, если в ходе стрельбы получены следующие наблюдения:\n" +
                "1) Л77, «-»; 2) П42, «+»; 3) П18, «+»; 4) П20, Все «+», Фр. 0-06; 5) П11, Преобладание «+», Фр. 0-12; 6) Цель подавлена. \n");
        tr.getCharacterFormat().setFontSize(12);
        paragraph.getFormat().setLineSpacing(5);

        paragraph = sec.addParagraph();
        tr = paragraph.appendText("Тема 7. Вариант №1 от 04.07.2022 - 10:14");
        tr.getCharacterFormat().setFontSize(12);
        tr.getCharacterFormat().setBold(true);
        paragraph.getFormat().setAfterSpacing(10f);

        paragraph = sec.addParagraph();
        tr = paragraph.appendText("Дцт = 5831\t∆Д = +295\tДци = 6126\t∂цт = +0-48\t∆∂ = -0-13\t∂ци = +0-35\n" +
                "ПС = 7-82\tОП (кнп) - Слева\t∆Xтыс = 15\tВд = 14");
        tr.getCharacterFormat().setFontSize(12);
        paragraph.getFormat().setLineSpacing(5);


        Section section = document.addSection();

        //Define the data for table
        String[] header = {"№", "Команда на ОП.", "Пр.", "Ур.", "Дов.", "Наблюдения."};
        String[][] data =
                {
                        new String[]{"1", "Argentina", "Buenos Aires", "South America", "2777815", "32300003"},
                        new String[]{"2","Bolivia", "La Paz", "South America", "1098575", "7300000"},
                        new String[]{"3","Brazil", "Brasilia", "South America", "8511196", "150400000"},
                        new String[]{"4","Canada", "Ottawa", "North America", "9976147", "26500000"},
                };

        //Add a table
        Table table = section.addTable(true);
        table.resetCells(data.length + 1, header.length);

        //Set the first row as table header
        TableRow row = table.getRows().get(0);
        row.isHeader(true);
        row.setHeight(20);
        row.setHeightType(TableRowHeightType.Exactly);
//        row.getRowFormat().setBackColor(Color.gray);
        for (int i = 0; i < header.length; i++) {
            row.getCells().get(i).getCellFormat().setVerticalAlignment(VerticalAlignment.Middle);
            Paragraph p = row.getCells().get(i).addParagraph();
            p.getFormat().setHorizontalAlignment(HorizontalAlignment.Center);
            TextRange txtRange = p.appendText(header[i]);
            txtRange.getCharacterFormat().setBold(true);
        }

        //Add data to the rest of rows
        for (int r = 0; r < data.length; r++) {
            TableRow dataRow = table.getRows().get(r + 1);
            dataRow.setHeight(25);
            dataRow.setHeightType(TableRowHeightType.Exactly);
//            dataRow.getRowFormat().setBackColor(Color.GRAY);
            for (int c = 0; c < data[r].length; c++) {
                dataRow.getCells().get(c).getCellFormat().setVerticalAlignment(VerticalAlignment.Middle);
                dataRow.getCells().get(c).addParagraph().appendText(data[r][c]);
            }
        }


        document.saveToFile("hello.docx", FileFormat.Docx_2010);
    }
}