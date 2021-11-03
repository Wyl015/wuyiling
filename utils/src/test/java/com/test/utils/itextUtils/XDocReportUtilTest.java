package com.test.utils.itextUtils;

import fr.opensagres.xdocreport.core.XDocReportException;
import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.document.registry.XDocReportRegistry;
import fr.opensagres.xdocreport.template.FieldExtractor;
import fr.opensagres.xdocreport.template.FieldsExtractor;
import fr.opensagres.xdocreport.template.TemplateEngineKind;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class XDocReportUtilTest {

    @Test
    public void listFreemarkFields() throws IOException, XDocReportException {
        String templatePath = "C:\\Users\\Admin\\Desktop\\ItextpdfTest\\xDoc测试.docx";
        InputStream inputStream = new FileInputStream(templatePath);
        final XDocReportRegistry registry = XDocReportRegistry.getRegistry();
        final IXDocReport ixDocReport = registry.loadReport(inputStream, TemplateEngineKind.Freemarker);
        FieldsExtractor extractor = new FieldsExtractor();
        ixDocReport.extractFields(extractor);
        final List fields = extractor.getFields();
        for (Object field : fields) {
            FieldExtractor e = (FieldExtractor) field;
            System.out.println(e.getName());
        }
    }
}
