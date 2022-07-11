package edu.vsu.siuo.word;


import com.spire.doc.*;
import com.spire.doc.documents.HorizontalAlignment;
import com.spire.doc.documents.Paragraph;
import com.spire.doc.documents.TableRowHeightType;
import com.spire.doc.documents.VerticalAlignment;
import com.spire.doc.fields.TextRange;
import edu.vsu.siuo.domains.dto.TaskDto;

import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
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

    public void Write(List<TaskDto> taskDtos) {
        for (TaskDto taskDto : taskDtos) {
            Section sec = this.document.addSection();
            //add a paragraph
            Paragraph paragraph = sec.addParagraph();
            TextRange tr = paragraph.appendText(
                    String.format("Тема %d. Вариант №%d от %s", taskDto.getTaskTopic(), taskDto.getTaskNumber(), getFormatTimeNow())
            );
            tr.getCharacterFormat().setFontSize(12);
            tr.getCharacterFormat().setBold(true);

            paragraph = sec.addParagraph();

            //формат для плюсов и минусов доделать
            tr = paragraph.appendText(String.format("Батарея 122мм гаубицы Д-30 занимает боевой порядок:\n" +
                            "ОП:  \tХ = %d; \tУ = %d; \th = %d м («Дон»)\n" +
                            "КНП: \tХ = %d; \tУ = %d; \th = %d м («Амур»)\n" +
                            "КНП адн: Х = %d; У = %d; \th = %d м («Лена»)\n" +
                            "α он = %s\n" +
                            "В батарее рассчитаны поправки для заряда «%s» на %d, %d, %d км.\n" +
                            "В дальности: %s; %s; %s. В направлении: %s; %s; %s.\n" +
                            "Командир дивизиона («Лена») передал: «Амур», стой! Цель 21, «%s». Дивизионный: αц = %s, Дк = %d, εц = %s. Подавить! Я «Лена».\n" +
                            "В должности командира батареи провести пристрелку и стрельбу на поражение цели 21, если в ходе стрельбы получены следующие наблюдения:\n" +
                            "1) Л77, «-»; 2) П42, «+»; 3) П18, «+»; 4) П20, Все «+», Фр. 0-06; 5) П11, Преобладание «+», Фр. 0-12; 6) Цель подавлена. \n",
                    //ОП
                    taskDto.getProblemDto().getOp().getX(), taskDto.getProblemDto().getOp().getY(), taskDto.getProblemDto().getOp().getH(),
                    //КНП
                    taskDto.getProblemDto().getKnp().getX(), taskDto.getProblemDto().getKnp().getY(), taskDto.getProblemDto().getKnp().getH(),
                    //КНП адн
                    taskDto.getProblemDto().getKnp().getX(), taskDto.getProblemDto().getKnp().getY(), taskDto.getProblemDto().getKnp().getH(),
                    //альфа ОН
                    String.format("%s-%s",
                            String.format("%d", taskDto.getProblemDto().getOp().getMainDirection()).substring(0, taskDto.getProblemDto().getOp().getMainDirection() > 999 ? 2 : 1),
                            String.format("%d", taskDto.getProblemDto().getOp().getMainDirection()).substring(taskDto.getProblemDto().getOp().getMainDirection() > 999 ? 2 : 1,
                                    taskDto.getProblemDto().getOp().getMainDirection() > 999 ? 4 : 3)
                    ),
                    //заряд
                    taskDto.getProblemDto().getPower().getDescription(),
                    //расстояние
                    taskDto.getProblemDto().getDistance().get(0),
                    taskDto.getProblemDto().getDistance().get(1),
                    taskDto.getProblemDto().getDistance().get(2),
                    //дальность
                    formatTextValueWithSigned(taskDto.getProblemDto().getRange().get(0)),
                    formatTextValueWithSigned(taskDto.getProblemDto().getRange().get(1)),
                    formatTextValueWithSigned(taskDto.getProblemDto().getRange().get(2)),

                    //направление
                    formatTextDivision(taskDto.getProblemDto().getDirection().get(0)),
                    formatTextDivision(taskDto.getProblemDto().getDirection().get(1)),
                    formatTextDivision(taskDto.getProblemDto().getDirection().get(2)),

                    //цель тип
                    taskDto.getProblemDto().getTarget().getType().getDescription(),
                    //αц
                    formatTextWithSeparation(taskDto.getProblemDto().getTarget().getAngleFromKNPtoTarget()),
                    //Дк
                    taskDto.getProblemDto().getTarget().getDistanceFromKNPtoTarget(),
                    //εц
                    formatTextDivision(taskDto.getProblemDto().getTarget().getAngularMagnitude_target())
            ));
            tr.getCharacterFormat().setFontSize(12);

            paragraph = sec.addParagraph();
            tr = paragraph.appendText(
                    String.format("Тема %d. Вариант №%d от %s", taskDto.getTaskTopic(), taskDto.getTaskNumber(), getFormatTimeNow())
            );
            tr.getCharacterFormat().setFontSize(12);
            tr.getCharacterFormat().setBold(true);
            paragraph = sec.addParagraph();
            tr = paragraph.appendText(String.format("Дцт = %d\t∆Д = %d\tДци = %d\t∂цт = +0-%d\t∆∂ = -0-%d\t∂ци = +0-%d\nКу = %d\tШу = 0-%d\t" +
                    "ПС = %d\tОП (кнп) - Слева\t∆Xтыс = %d\tВд = %d",
                    taskDto.getSolutionDto().getDCt(),
                    taskDto.getSolutionDto().getDeltaDCt(),
                    taskDto.getSolutionDto().getDCi(),
                    taskDto.getSolutionDto().getDCt(),
                    taskDto.getSolutionDto().getDeltaDeCt(),
                    taskDto.getSolutionDto().getDeCi(),
                    taskDto.getSolutionDto().getKY(),
                    taskDto.getSolutionDto().getShY(),
                    taskDto.getSolutionDto().getPs(),
                    taskDto.getSolutionDto().getDeltaX(),
                    taskDto.getSolutionDto().getVD()));
            tr.getCharacterFormat().setFontSize(12);


            //Define the data for table
            String[] header = {"№", "Команда на ОП.", "Пр.", "Ур.", "Дов.", "Наблюдения."};

            List<String[]> data = new ArrayList<>();

            for (int i = 0; i < taskDto.getSolutionDto().getCommands().size(); i++) {
                data.add(new String[]{
                        String.format("%d", i + 1),
                        taskDto.getSolutionDto().getCommands().get(i).getDescription(),
                        taskDto.getSolutionDto().getCommands().get(i).getPR() != 0
                                ? formatTextValueWithSigned(taskDto.getSolutionDto().getCommands().get(i).getPR())
                                : "",
                        taskDto.getSolutionDto().getCommands().get(i).getYR() != 0
                                ? formatTextWithSeparation(taskDto.getSolutionDto().getCommands().get(i).getYR())
                                : "",
                        taskDto.getSolutionDto().getCommands().get(i).getDe() != ""
                                ? taskDto.getSolutionDto().getCommands().get(i).getDe()
                                : "",
                        taskDto.getSolutionDto().getCommands().get(i).getObservation() != ""
                                ? taskDto.getSolutionDto().getCommands().get(i).getObservation()
                                : ""
                });
            }

            //Add a table
            Table table = sec.addTable(true);
            table.resetCells(data.size() + 1, header.length);

            //Set the first row as table header
            TableRow row = table.getRows().get(0);
            row.isHeader(true);
            row.setHeightType(TableRowHeightType.Auto);
            for (int i = 0; i < header.length; i++) {
                row.getCells().get(i).getCellFormat().setVerticalAlignment(VerticalAlignment.Middle);
                Paragraph p = row.getCells().get(i).addParagraph();
                p.getFormat().setHorizontalAlignment(HorizontalAlignment.Center);
                TextRange txtRange = p.appendText(header[i]);
                txtRange.getCharacterFormat().setBold(true);
            }

            table.setPreferredWidth(new PreferredWidth(WidthType.Percentage, (short) 100));
            //Add data to the rest of rows
            for (int r = 0; r < data.size(); r++) {
                table.get(r+1, 0).setCellWidth(0.03f, CellWidthType.Percentage);
                table.get(r+1, 1).setCellWidth(0.45f, CellWidthType.Percentage);
                table.get(r+1, 2).setCellWidth(0.06f, CellWidthType.Percentage);
                table.get(r+1, 3).setCellWidth(0.09f, CellWidthType.Percentage);
                table.get(r+1, 4).setCellWidth(0.15f, CellWidthType.Percentage);
                table.get(r+1, 5).setCellWidth(0.25f, CellWidthType.Percentage);
                TableRow dataRow = table.getRows().get(r + 1);
                dataRow.setHeightType(TableRowHeightType.Auto);
                for (int c = 0; c < data.get(r).length; c++) {
                    dataRow.getCells().get(c).getCellFormat().setVerticalAlignment(VerticalAlignment.Middle);
                    dataRow.getCells().get(c).addParagraph().appendText(data.get(r)[c]);

                }
            }
        }
        //save
        this.document.saveToFile(path + '/' + fileName, FileFormat.Docx);
    }

    public static String GenerateNameFile(String taskTopic) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM-dd-yyyy hh-mm");
        String text = dtf.format(LocalDateTime.now());
        return String.format("%s %s.docx", taskTopic, text);
    }

    public String getFormatTimeNow() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM-dd-yyyy hh-mm");
        return dtf.format(LocalDateTime.now());
    }

    private String formatTextDivision(int value) {
        return value > 0
                ? String.format("+0-%s",
                    value >= 10
                            ? value
                            : String.format("0%d", value))
                : String.format("-0-%s",
                    Math.abs(value) >= 10
                            ? Math.abs(value)
                            : String.format("0%d", Math.abs(value))
        );
    }

    private String formatTextValueWithSigned(int value){
        return value > 0 ?  String.format("+%d", value) : String.format("%d", value);
    }

    private String formatTextWithSeparation(int value){
        return String.format("%s-%s",
             String.format("%d", value).substring(0, value > 999 ? 2 : 1),
             String.format("%d",value).substring(value > 999 ? 2 : 1,
                     value > 999 ? 4 : 3)
        );
    }

}
