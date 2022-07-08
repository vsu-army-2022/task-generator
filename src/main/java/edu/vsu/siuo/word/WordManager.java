package edu.vsu.siuo.word;


import com.spire.doc.*;
import com.spire.doc.documents.HorizontalAlignment;
import com.spire.doc.documents.Paragraph;
import com.spire.doc.documents.TableRowHeightType;
import com.spire.doc.documents.VerticalAlignment;
import com.spire.doc.fields.TextRange;
import edu.vsu.siuo.domains.TaskDto;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class WordManager {
    private String fileName;
    private Document document;

    private String path;

    public WordManager(String path, String fileName) {
        this.document = new Document();
        this.fileName = fileName;
        this.path = path;
        new File(fileName);
    }

    public void Write(List<TaskDto> taskDtos){
        for (TaskDto taskDto : taskDtos) {

            Section sec = this.document.addSection();
            //add a paragraph
            Paragraph paragraph = sec.addParagraph();
            TextRange tr = paragraph.appendText(
                    String.format("Тема %d. Вариант №%d от %s", taskDto.getTaskTopic(), taskDto.getTaskNumber(), getFormatTimeNow())
            );
            tr.getCharacterFormat().setFontSize(12);
            tr.getCharacterFormat().setBold(true);
            paragraph.getFormat().setAfterSpacing(10f);

            paragraph = sec.addParagraph();

            //формат для плюсов и минусов доделать
            tr = paragraph.appendText(String.format("Батарея 122мм гаубицы Д-30 занимает боевой порядок:\n" +
                    "ОП:  \tХ = %d; \tУ = %d; \th = %d м («Дон»)\n" +
                    "КНП: \tХ = %d; \tУ = %d; \th = %d м («Амур»)\n" +
                    "КНП адн: Х = test; У = test; \th = test м («Лена»)\n" +
                    "α он = 16-00\n" +
                    "В батарее рассчитаны поправки для заряда «test» на %d, %d, %d км.\n" +
                    "В дальности: %d; %d; %d. В направлении: %d; %d; %d.\n" +
                    "Командир дивизиона («Лена») передал: «Амур», стой! Цель 21, «радиолокационная станция полевой артиллерии». Дивизионный: αц = 8-66, Дк = 2201, εц = +0-09. Подавить! Я «Лена».\n" +
                    "В должности командира батареи провести пристрелку и стрельбу на поражение цели 21, если в ходе стрельбы получены следующие наблюдения:\n" +
                    "1) Л77, «-»; 2) П42, «+»; 3) П18, «+»; 4) П20, Все «+», Фр. 0-06; 5) П11, Преобладание «+», Фр. 0-12; 6) Цель подавлена. \n",
                    taskDto.getXOp(), taskDto.getYOp(), taskDto.getHOp(),
                    taskDto.getXKnp(), taskDto.getYKnp(), taskDto.getHKnp(),
                    taskDto.getDistance().get(0), taskDto.getDistance().get(1), taskDto.getDistance().get(2),
                    taskDto.getRange().get(0), taskDto.getRange().get(1), taskDto.getRange().get(2),
                    taskDto.getDistance().get(0), taskDto.getDistance().get(1), taskDto.getDistance().get(2)

            ));
            tr.getCharacterFormat().setFontSize(12);
            paragraph.getFormat().setLineSpacing(5);

            paragraph = sec.addParagraph();
            tr = paragraph.appendText(
                    String.format("Тема %d. Вариант №%d от %s", taskDto.getTaskTopic(), taskDto.getTaskNumber(), getFormatTimeNow())
            );
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

            List<String[]> data = new ArrayList<>();

            for (int i = 0; i < taskDto.getCommands().size(); i++) {
                data.add(new String[]{
                        String.format("%d", i + 1),
                        taskDto.getCommands().get(i).getDescription(),
                        String.format("%d", taskDto.getCommands().get(i).getPR()),
                        String.format("%d", taskDto.getCommands().get(i).getYR()),
                        taskDto.getCommands().get(i).getDe(),
                        taskDto.getCommands().get(i).getObservation()
                });
            }

            //Add a table
            Table table = section.addTable(true);
            table.resetCells(data.size() + 1, header.length);

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
            for (int r = 0; r < data.size(); r++) {
                TableRow dataRow = table.getRows().get(r + 1);
                dataRow.setHeight(25);
                dataRow.setHeightType(TableRowHeightType.Exactly);
//            dataRow.getRowFormat().setBackColor(Color.GRAY);
                for (int c = 0; c < data.get(r).length; c++) {
                    dataRow.getCells().get(c).getCellFormat().setVerticalAlignment(VerticalAlignment.Middle);
                    dataRow.getCells().get(c).addParagraph().appendText(data.get(r)[c]);
                }
            }
        }
        //save
        this.document.saveToFile(path+'/'+fileName, FileFormat.Docm_2010);
    }

    public static String GenerateNameFile(String taskTopic) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM-dd-yyyy hh-mm");
        String text = dtf.format( LocalDateTime.now() );
        String fileName = String.format("%s %s.docx", taskTopic, text);
        return fileName;
    }

    public String getFormatTimeNow(){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM-dd-yyyy hh-mm");
        return dtf.format( LocalDateTime.now() );
    }

}
