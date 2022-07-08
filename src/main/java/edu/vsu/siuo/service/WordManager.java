package edu.vsu.siuo.service;

import com.spire.doc.Document;
import com.spire.doc.FileFormat;
import com.spire.doc.Section;
import com.spire.doc.documents.Paragraph;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class WordManager {
    private String fileName;
    private Document document;

    public WordManager(String nameTask) {
        this.document = new Document();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM-dd-yyyy hh-mm");
        String text = dtf.format( LocalDateTime.now() );
        this.fileName = String.format("%s %s.docx", nameTask, text);
        new File(fileName);
    }

    //принимаем dto (формат dto?)
    public void Write(String text){
        Section sec = this.document.addSection();

        //метод,
        Paragraph paragraph = sec.addParagraph();
        paragraph.appendText("Here is a paragraph with various character styles. This is ");

        //save
        this.document.saveToFile(fileName, FileFormat.Docm_2010);
    }
}
