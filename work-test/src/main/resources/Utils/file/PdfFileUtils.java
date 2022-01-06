package com.wuyiling.worktest.Utils.file;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;
import com.yuuwei.faceview.domain.bean.PdfSizeBean;
import com.yuuwei.faceview.enums.http.ResponseCodeEnum;
import com.yuuwei.faceview.exception.GlobalBaseException;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.pdfbox.io.MemoryUsageSetting;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * @author: lingjun.jlj
 * @date: 2020/11/5 14:37
 * @description: PDF 文件操作工具
 */
@Slf4j
public class PdfFileUtils {

    /**
     * pdf合并拼接
     *
     * @param files      文件列表
     * @param targetPath 合并到
     * @return
     * @throws IOException
     */
    public static void mulFileToOne(List<File> files, String targetPath) throws IOException {
        // pdf合并工具类
        PDFMergerUtility mergePdf = new PDFMergerUtility();
        for (File f : files) {
            if (f.exists() && f.isFile()) {
                // 循环添加要合并的pdf
                mergePdf.addSource(f);
            }
        }
        // 设置合并生成pdf文件名称
        mergePdf.setDestinationFileName(targetPath);
        // 合并pdf
        mergePdf.mergeDocuments(MemoryUsageSetting.setupMainMemoryOnly());
        log.info("多个PDF合并成一个PDF文件");
    }

    public static void pdfRotate() {
        Document document = new Document(PageSize.A4.rotate());
        try {
            PdfWriter.getInstance(document, new FileOutputStream("D:\\Document\\rotate.pdf"));
            document.open();
            for (int i = 0; i < 1000; i++) {
                document.add(new Paragraph("Hello Word!" + i));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        document.close();
    }

    public static void pdfCreate() {
        Document document = new Document(PageSize.A4);
        try {
            PdfWriter.getInstance(document, new FileOutputStream("D:\\Document\\new_test.pdf"));
            document.open();
            for (int i = 0; i < 50; i++) {
                document.add(new Paragraph("Hello Word!" + i));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        document.close();
    }

    /**
     * 设置PDF页面，将PDF设置为横版
     * todo: 因为页面设置横版问题，会丢失底部的几行数据，待完善
     *
     * @param pdfPath    原始文件地址
     * @param targetPath 转换后的文件地址
     */
    public static void rotatePDFPages(String pdfPath, String targetPath) {
        if (StringUtils.isBlank(pdfPath) || StringUtils.isBlank(targetPath)) {
            log.error("PDF页面设置横版，文件地址不能为空");
            throw new GlobalBaseException(ResponseCodeEnum.PARAM_ERROR);
        }
        Document document = new Document(PageSize.A4.rotate());
        try {
            PdfReader pdfReader = new PdfReader(new FileInputStream(pdfPath));
            PdfWriter pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(targetPath));
            document.open();
            int pageCount = pdfReader.getNumberOfPages();
            for (int i = 1; i <= pageCount; i++) {
                document.newPage();
                PdfImportedPage page = pdfWriter.getImportedPage(pdfReader, i);
                Image image = Image.getInstance(page);
                document.add(image);
            }

        } catch (Exception e) {
            e.printStackTrace();
            log.error("PDF页面设置横版出现错误：{}", e.getMessage(), e);
        } finally {
            document.close();
        }
    }

    /**
     * 获取pdf的宽和高
     * @param pdfPath
     * @return
     * @throws IOException
     */
    public static PdfSizeBean getPdfSize(String pdfPath) throws IOException {
        PdfReader pdfReader = new PdfReader(pdfPath);
        Rectangle pageSize = pdfReader.getPageSize(1);
        float height = pageSize.getHeight();
        float width = pageSize.getWidth();
        return PdfSizeBean.builder()
                .width(width)
                .height(height)
                .build();
    }

    /**
     * 获取pdf的宽和高
     * @param inputStream
     * @return
     * @throws IOException
     */
    public static PdfSizeBean getPdfSize(InputStream inputStream) throws IOException {
        PdfReader pdfReader = new PdfReader(inputStream);
        Rectangle pageSize = pdfReader.getPageSize(1);
        float height = pageSize.getHeight();
        float width = pageSize.getWidth();
        return PdfSizeBean.builder()
                .width(width)
                .height(height)
                .build();
    }


    /**
     * pdf转化txt工具类
     * @param file（文件路径）
     * @throws Exception
     */
    public static void readPdf(String file) throws Exception {
        // 是否排序
        boolean sort = false;
        // pdf文件名
        String pdfFile = file;
        // 输入文本文件名称
        String textFile = null;
        // 编码方式
        String encoding = "UTF-8";
        // 开始提取页数
        int startPage = 1;
        // 结束提取页数
        int endPage = Integer.MAX_VALUE;
        // 文件输入流，生成文本文件
        Writer output = null;
        // 内存中存储的PDF Document
        PDDocument document = null;
        try {
            try {
                // 首先当作一个URL来装载文件，如果得到异常再从本地文件系统//去装载文件
                URL url = new URL(pdfFile);
                //注意参数已不是以前版本中的URL.而是File。
                document = PDDocument.load(new File(pdfFile));
                // 获取PDF的文件名
                String fileName = url.getFile();
                // 以原来PDF的名称来命名新产生的txt文件
                if (fileName.length() > 4) {
                    File outputFile = new File(fileName.substring(0, fileName.length() - 4)+ ".txt");
                    textFile ="F:/"+outputFile.getName();
                }
            } catch (MalformedURLException e) {
                // 如果作为URL装载得到异常则从文件系统装载
                //注意参数已不是以前版本中的URL.而是File。
                document = PDDocument.load(new File(pdfFile));
                if (pdfFile.length() > 4) {
                    textFile = pdfFile.substring(0, pdfFile.length() - 4)+ ".txt";
                }
            }
            // 文件输入流，写入文件倒textFile
            output = new OutputStreamWriter(new FileOutputStream(textFile),encoding);
            // PDFTextStripper来提取文本
            PDFTextStripper stripper = null;
            stripper = new PDFTextStripper();
            // 设置是否排序
            stripper.setSortByPosition(sort);
            // 设置起始页
            stripper.setStartPage(startPage);
            // 设置结束页
            stripper.setEndPage(endPage);
            // 调用PDFTextStripper的writeText提取并输出文本
            stripper.writeText(document, output);

            System.out.println(textFile + " 输出成功！");
        } finally {
            if (output != null) {
                // 关闭输出流
                output.close();
            }
            if (document != null) {
                // 关闭PDF Document
                document.close();
            }
        }
    }

    @SneakyThrows
    public static void main(String[] args) throws IOException {
        // PDF 合成
//        List<File> files = new ArrayList();
//        files.add(new File("D:\\Document\\temp\\1589337883715_791433.pdf"));
//        files.add(new File("D:\\Document\\temp\\1621219095831_008518.pdf"));
//        files.add(new File("D:\\Document\\temp\\1621222509985_124370.pdf"));
//        String targetPath = "D:\\Document\\new_file.pdf";
//        mulFileToOne(files, targetPath);
//        readPdf("D:\\1629369213762_533319.pdf");
    }
}
