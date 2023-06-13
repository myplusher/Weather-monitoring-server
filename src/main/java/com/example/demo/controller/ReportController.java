package com.example.demo.controller;

import com.example.demo.service.DataService;
import com.lowagie.text.pdf.BaseFont;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.xhtmlrenderer.layout.SharedContext;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@RequestMapping("/")
public class ReportController {

    @Autowired
    DataService service;

    @GetMapping("/report")
    public void getReport(@RequestParam int id, @RequestParam(defaultValue = "", required = false) String date) {
        File inputHTML = new File("./src/main/resources/report_sample/report.html");
        try {
            Document document = Jsoup.parse(inputHTML, "UTF-8");
            document.outputSettings().syntax(Document.OutputSettings.Syntax.xml);
            Element table = document.body().getElementById("report_body");

            if (date.length() == 0) {
                Date now = new Date();
                SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd");
                date = dt.format(now);
            }
            table.append(service.buildReport(id, date));

            OutputStream outputStream = new FileOutputStream("./src/main/resources/static/outputPdf.pdf");
            ITextRenderer renderer = new ITextRenderer();
            renderer.getFontResolver().addFont("./src/main/resources/static/verdana.ttf", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
            renderer.setDocumentFromString(document.toString());
            renderer.layout();
            renderer.createPDF(outputStream);
            outputStream.close();



        } catch (IOException e) {
            System.out.println(e);
        }

    }
}
