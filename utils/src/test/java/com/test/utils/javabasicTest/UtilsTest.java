package com.test.utils.javabasicTest;

import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;
import fr.opensagres.xdocreport.core.XDocReportException;
import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.document.registry.XDocReportRegistry;
import fr.opensagres.xdocreport.template.FieldExtractor;
import fr.opensagres.xdocreport.template.FieldsExtractor;
import fr.opensagres.xdocreport.template.TemplateEngineKind;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class UtilsTest {

    private static StringBuilder nodeContent;

    /*
     * @Author Wuyiling
     * @Description 解析odt文件
     * @Date 16:04 2021/10/20
     * @Param []
     * @return void
     **/
    @Test
    void contextLoads() throws IOException, SAXException, ParserConfigurationException {
        ZipFile zipFile = new ZipFile("C:\\Users\\Admin\\Desktop\\ItextpdfTest\\待签署文件测试3.odt");
        Enumeration entries = zipFile.entries();
        ZipEntry entry;
        while(entries.hasMoreElements()) {
            entry = (ZipEntry) entries.nextElement();
            if (entry.getName().equals("content.xml")) {
                DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
                domFactory.setNamespaceAware(true);
                DocumentBuilder docBuilder = domFactory.newDocumentBuilder();
                Document doc = docBuilder.parse(zipFile.getInputStream(entry));
                System.out.println(doc.getNodeValue());
                printDocument(doc);
                NodeList list = doc.getElementsByTagName("body");
                System.out.println(list);
                Node node = list.item(0);
                nodeContent = new StringBuilder();
                prettyPrint(node);
                System.out.println(nodeContent.toString());
            }
        }
    }
    private static void prettyPrint(Node node) {
        if (node.getNodeType() == Node.TEXT_NODE) {
            nodeContent.append(node.getNodeValue());
        } else if (node.getNodeType() == Node.ELEMENT_NODE) {
            nodeContent.append("");
            NodeList kids = node.getChildNodes();
            for (int i = 0; i < kids.getLength(); i++) {
                prettyPrint(kids.item(i));
            }
            nodeContent.append("" + node.getNodeName() + ">");
        }

    }
    private static void printDocument(Document doc) throws IOException {
        OutputFormat format = new OutputFormat(doc);
        format.setIndenting(true);
        XMLSerializer serializer = new XMLSerializer(System.out, format);
        System.out.println("serializer.serialize(doc):=================");
        serializer.serialize(doc);
    }

    /*
     * @Author Wuyiling
     * @Description List 转 int[]
     * @Date 9:40 2021/10/21
     * @Param
     * @return
     **/
    @Test
    public void list2array() {
        int[] a = {1, 2, 3, 4};
        int len = a.length;
        int carry = 1;
        LinkedList<Integer> result = new LinkedList<Integer>();
        for (int i = len - 1; i >= 0; i--) {
            carry = (a[i] + carry) / 10;
            result.add(0, (a[i] + carry) % 10);
            if (carry == 0) {
                break;
            }
        }
        Integer[] objects = (Integer[]) result.toArray();
        Arrays.stream(objects).mapToInt(Integer::valueOf).toArray();
    }





}
