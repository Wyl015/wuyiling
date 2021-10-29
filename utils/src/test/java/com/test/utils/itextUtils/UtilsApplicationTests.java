package com.test.utils.itextUtils;

import com.test.utils.itextUtils.Employee;
import com.test.utils.itextUtils.Project;
import fr.opensagres.xdocreport.core.XDocReportException;
import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.document.images.FileImageProvider;
import fr.opensagres.xdocreport.document.images.IImageProvider;
import fr.opensagres.xdocreport.document.registry.XDocReportRegistry;
import fr.opensagres.xdocreport.template.FieldExtractor;
import fr.opensagres.xdocreport.template.FieldsExtractor;
import fr.opensagres.xdocreport.template.IContext;
import fr.opensagres.xdocreport.template.TemplateEngineKind;
import fr.opensagres.xdocreport.template.formatter.FieldsMetadata;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class UtilsApplicationTests {
    //查找字符串
    @Test
    public void findString() throws IOException, XDocReportException {
        //模板加载
        String templatePath = "C:\\Users\\Admin\\Desktop\\ItextpdfTest\\ODTUTILS功能测试1.odt";
        InputStream inputStream = new FileInputStream(templatePath);
        final XDocReportRegistry registry = XDocReportRegistry.getRegistry();
        //模板路径+引擎:freemarket、velocity
        final IXDocReport ixDocReport = registry.loadReport(inputStream, TemplateEngineKind.Freemarker);
        FieldsExtractor extractor = new FieldsExtractor();
        ixDocReport.extractFields(extractor);
        final List fields = extractor.getFields();
        for (Object field : fields) {
            FieldExtractor e = (FieldExtractor) field;
            System.out.println(e.getName());
        }
    }



    //填充
    @Test
    public void replaceString() throws IOException, XDocReportException {
        //1.通过freemarker模板引擎加载文档，并缓存到registry中
        InputStream in = new FileInputStream(
                "C:\\Users\\Admin\\Desktop\\ItextpdfTest\\ODTUTILS功能测试.odt");
        IXDocReport report = XDocReportRegistry
                .getRegistry()
                .loadReport(in, TemplateEngineKind.Freemarker);

        //2.设置填充字段、填充类以及是否为list。
        FieldsMetadata fieldsMetadata = report.createFieldsMetadata();
        fieldsMetadata.load("project", Project.class);
        fieldsMetadata.load("employees", Employee.class, true);
        fieldsMetadata.addFieldAsImage("logo");
        report.setFieldsMetadata(fieldsMetadata);

        //3.模拟填充数据
        String title = "项目开发报告";
        Project project = new Project("网站开发", "截至2018年年底");
        List<Employee> employees = new ArrayList<Employee>();
        employees.add(new Employee("张三", "产品", "任务完成"));
        employees.add(new Employee("李四", "开发", "任务完成"));
        IImageProvider logo = new FileImageProvider(
                new File("C:\\Users\\Admin\\Desktop\\ItextpdfTest\\1.jpg"),
                true);
        logo.setSize(200f, 100f);

        //4.匹配填充字段和填充数据，进行填充
        IContext context = report.createContext();
        context.put("title", title);
        context.put("project", project);
        context.put("employees", employees);
        context.put("logo", logo);
        OutputStream out = new FileOutputStream(
                new File("C:\\Users\\Admin\\Desktop\\ItextpdfTest\\ODTUTILS功能测试1.odt"));

        report.process(context, out);

    }

    //填充
    @Test
    public void replaceString1() throws IOException, XDocReportException {
        //1.通过freemarker模板引擎加载文档，并缓存到registry中
//        InputStream in = new FileInputStream("C:\\Users\\Admin\\Desktop\\ItextpdfTest\\xDoc测试.docx");
        InputStream in = new FileInputStream("C:\\Users\\Admin\\Desktop\\ItextpdfTest\\ODTUTILS功能测试1.odt");
        IXDocReport report = XDocReportRegistry
                .getRegistry()
                .loadReport(in, TemplateEngineKind.Freemarker);
        //2.设置填充字段、填充类以及是否为list。
        FieldsMetadata fieldsMetadata = report.createFieldsMetadata();
        fieldsMetadata.load("project", Project.class);
        fieldsMetadata.load("employees", Employee.class, true);
        fieldsMetadata.addFieldAsImage("logo");
        report.setFieldsMetadata(fieldsMetadata);

        //3.模拟填充数据
        String title = "项目开发报告";
        Project project = new Project("网站开发", "截至2018年年底");
        List<Employee> employees = new ArrayList<Employee>();
        employees.add(new Employee("张三", "产品", "任务完成"));
        employees.add(new Employee("李四", "开发", "任务完成"));
        IImageProvider logo = new FileImageProvider(
                new File("C:\\Users\\Admin\\Desktop\\ItextpdfTest\\1.jpg"),
                true);
//        logo.setSize(200f, 100f);
        //4.匹配填充字段和填充数据，进行填充
        IContext context = report.createContext();
        context.put("title", title);
        context.put("project", project);
        context.put("lists", employees);
        context.put("logo", logo);
//        OutputStream out = new FileOutputStream(new File("C:\\Users\\Admin\\Desktop\\ItextpdfTest\\xDoc测试1.docx"));
        OutputStream out = new FileOutputStream(new File("C:\\Users\\Admin\\Desktop\\ItextpdfTest\\ODTUTILS功能测试2.odt"));
        report.process(context, out);
    }

}