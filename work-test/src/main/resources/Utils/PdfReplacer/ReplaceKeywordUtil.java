package com.wuyiling.worktest.Utils.PdfReplacer;

import com.itextpdf.awt.geom.Rectangle2D;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.pdf.parser.*;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class ReplaceKeywordUtil {

    public static final float defaultHeight = 16;
    public static final float fixHeight = 10;


    public void replace (String src, String des, String targetWord,String replaceWord, float frontSize) throws IOException {
        //1.给定文件
        File pdfFile = new File(src);

        //2.定义一个byte数组，长度为文件的长度
        byte[] pdfData = new byte[(int) pdfFile.length()];


        //3.IO流读取文件内容到byte数组
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(pdfFile);
            inputStream.read(pdfData);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                }
            }
        }
        //5.调用方法，给定关键字和文件
        List<float[]> positions = null;
        try {
            positions = findKeywordPostions(pdfData, targetWord);
        } catch (IOException e) {
            e.printStackTrace();
        }

        PdfReader pdfReader = null;
        PdfStamper stamper = null;
        try {
            pdfReader = new PdfReader(pdfData);
            stamper = new PdfStamper(pdfReader, new FileOutputStream(des));

            if (positions != null) {
                for (int i = 0; i < positions.size(); i++) {
                    float[] position = positions.get(i);
                    PdfContentByte canvas = stamper.getOverContent((int) position[0]);
                    //修改背景色
                    canvas.saveState();
                    canvas.setColorFill(BaseColor.WHITE);
                    canvas.rectangle(position[1], position[2]-4, position[3]+5, position[4]);
                    canvas.fill();
                    canvas.restoreState();
                    //替换关键字
                    canvas.beginText();
                    BaseFont bf = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.EMBEDDED);
                    canvas.setFontAndSize(bf, frontSize);
                    canvas.setTextMatrix(position[1], position[2]-1);
                    canvas.showText(replaceWord);
                    canvas.endText();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        } finally {
            if (stamper != null){
                try {
                    stamper.close();
                } catch (DocumentException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (pdfReader != null) {
                pdfReader.close();
            }
        }
    }


    public static void main(String[] args) throws IOException, FileNotFoundException {
        //1.给定文件
        File pdfFile = new File("C:\\Users\\Admin\\Desktop\\ItextpdfTest\\1.pdf");
        String out = "C:\\Users\\Admin\\Desktop\\ItextpdfTest\\1.pdf";
        //2.定义一个byte数组，长度为文件的长度
        byte[] pdfData = new byte[(int) pdfFile.length()];


        //3.IO流读取文件内容到byte数组
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(pdfFile);
            inputStream.read(pdfData);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                }
            }
        }

        //4.指定关键字
        String keyword = "清楚";

        String replace = "小白";

        //5.调用方法，给定关键字和文件
        List<float[]> positions = null;
        try {
            positions = findKeywordPostions(pdfData, keyword);
        } catch (IOException e) {
            e.printStackTrace();
        }

        PdfReader pdfReader = null;
        PdfStamper stamper = null;
        try {
            pdfReader = new PdfReader(pdfData);
            stamper = new PdfStamper(pdfReader, new FileOutputStream(out));

            if (positions != null) {
                for (int i = 0; i < positions.size(); i++) {
                    float[] position = positions.get(i);

                    PdfContentByte canvas = stamper.getOverContent((int) position[0]);
                    //修改背景色
                    canvas.saveState();
                    canvas.setColorFill(BaseColor.WHITE);
                    // canvas.setColorFill(BaseColor.BLUE);
                    // 以左下点为原点，x轴的值，y轴的值，总宽度，总高度：
                    // canvas.rectangle(mode.getX() - 1, mode.getY(),
                    // mode.getWidth() + 2, mode.getHeight());
                    canvas.rectangle(position[1], position[2], position[3], position[4]);

                    canvas.fill();
                    canvas.restoreState();

                    //替换关键字
                    canvas.beginText();
                    BaseFont bf = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.EMBEDDED);
                    canvas.setFontAndSize(bf, 10F);
                    canvas.setTextMatrix(position[1], position[2] + 2);
                    canvas.showText(replace);
                    canvas.endText();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        } finally {
            if (stamper != null){
                try {
                    stamper.close();
                } catch (DocumentException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (pdfReader != null) {
                pdfReader.close();
            }
        }

        //6.返回值类型是  List<float[]> 每个list元素代表一个匹配的位置，分别为 float[0]所在页码  float[1]所在x轴 float[2]所在y轴 float[3]关键字宽度 floatt[4]关键字高度
        System.out.println("total:" + positions.size());
        if (positions != null && positions.size() > 0)

        {
            for (float[] position : positions) {
                System.out.print("pageNum: " + (int) position[0]);
                System.out.print("\tx: " + position[1]);
                System.out.println("\ty: " + position[2]);
                System.out.println("\tw: " + position[3]);
                System.out.println("\th: " + position[4]);
            }
        }
    }


    /**
     * findKeywordPostions
     *
     * @param pdfData 通过IO流 PDF文件转化的byte数组
     * @param keyword 关键字
     * @return List<float [ ]> : float[0]:pageNum float[1]:x float[2]:y
     * @throws IOException
     */
    public static List<float[]> findKeywordPostions(byte[] pdfData, String keyword) throws IOException {
        List<float[]> result = new ArrayList<float[]>();
        List<PdfPageContentPositions> pdfPageContentPositions = getPdfContentPostionsList(pdfData);


        for (PdfPageContentPositions pdfPageContentPosition : pdfPageContentPositions) {
            List<float[]> charPositions = findPositions(keyword, pdfPageContentPosition);
            if (charPositions == null || charPositions.size() < 1) {
                continue;
            }
            result.addAll(charPositions);
        }
        return result;
    }


    private static List<PdfPageContentPositions> getPdfContentPostionsList(byte[] pdfData) throws IOException {
        PdfReader reader = new PdfReader(pdfData);

        List<PdfPageContentPositions> result = new ArrayList<PdfPageContentPositions>();

        int pages = reader.getNumberOfPages();
        for (int pageNum = 1; pageNum <= pages; pageNum++) {
            float width = reader.getPageSize(pageNum).getWidth();
            float height = reader.getPageSize(pageNum).getHeight();


            PdfRenderListener pdfRenderListener = new PdfRenderListener(pageNum, width, height);


            //解析pdf，定位位置
            PdfContentStreamProcessor processor = new PdfContentStreamProcessor(pdfRenderListener);
            PdfDictionary pageDic = reader.getPageN(pageNum);
            PdfDictionary resourcesDic = pageDic.getAsDict(PdfName.RESOURCES);
            try {
                processor.processContent(ContentByteUtils.getContentBytesForPage(reader, pageNum), resourcesDic);
            } catch (IOException e) {
                reader.close();
                throw e;
            }


            String content = pdfRenderListener.getContent();
            List<CharPosition> charPositions = pdfRenderListener.getcharPositions();


            List<float[]> positionsList = new ArrayList<float[]>();
            for (CharPosition charPosition : charPositions) {
                float[] positions = new float[]{charPosition.getPageNum(), charPosition.getX(), charPosition.getY(), charPosition.getWidth(), charPosition.getHeight()};
                positionsList.add(positions);
            }


            PdfPageContentPositions pdfPageContentPositions = new PdfPageContentPositions();
            pdfPageContentPositions.setContent(content);
            pdfPageContentPositions.setPostions(positionsList);


            result.add(pdfPageContentPositions);
        }
        reader.close();
        return result;
    }


    private static List<float[]> findPositions(String keyword, PdfPageContentPositions pdfPageContentPositions) {


        List<float[]> result = new ArrayList<float[]>();


        String content = pdfPageContentPositions.getContent();
        List<float[]> charPositions = pdfPageContentPositions.getPositions();


        for (int pos = 0; pos < content.length(); ) {
            int positionIndex = content.indexOf(keyword, pos);
            if (positionIndex == -1) {
                break;
            }
            float[] postions = charPositions.get(positionIndex);
            //此处较为关键通过第一个关键字计算出整个关键字的宽度
            for (int i = 1; i < keyword.length(); i++) {
                float[] postionsNew = charPositions.get(positionIndex + i);
                postions[3] = postions[3] + postionsNew[3];
            }
            result.add(postions);
            pos = positionIndex + 1;
        }
        return result;
    }


    private static class PdfPageContentPositions {
        private String content;
        private List<float[]> positions;


        public String getContent() {
            return content;
        }


        public void setContent(String content) {
            this.content = content;
        }


        public List<float[]> getPositions() {
            return positions;
        }


        public void setPostions(List<float[]> positions) {
            this.positions = positions;
        }
    }


    private static class PdfRenderListener implements RenderListener {
        private int pageNum;
        private float pageWidth;
        private float pageHeight;
        private StringBuilder contentBuilder = new StringBuilder();
        private List<CharPosition> charPositions = new ArrayList<CharPosition>();


        public PdfRenderListener(int pageNum, float pageWidth, float pageHeight) {
            this.pageNum = pageNum;
            this.pageWidth = pageWidth;
            this.pageHeight = pageHeight;
        }

        @Override
        public void beginTextBlock() {
        }

        @Override
        public void renderText(TextRenderInfo renderInfo) {
            List<TextRenderInfo> characterRenderInfos = renderInfo.getCharacterRenderInfos();
            for (TextRenderInfo textRenderInfo : characterRenderInfos) {
                String word = textRenderInfo.getText();
                if (word.length() > 1) {
                    word = word.substring(word.length() - 1, word.length());
                }
                Rectangle2D.Float rectangle = textRenderInfo.getAscentLine().getBoundingRectange();

                float x = (float) rectangle.getX();
                float y = (float) rectangle.getY();
//                float x = (float)rectangle.getCenterX();
//                float y = (float)rectangle.getCenterY();
//                double x = rectangle.getMinX();
//                double y = rectangle.getMaxY();


                //这两个是关键字在所在页面的XY轴的百分比
                float xPercent = Math.round(x / pageWidth * 10000) / 10000f;
                float yPercent = Math.round((1 - y / pageHeight) * 10000) / 10000f;


//                CharPosition charPosition = new CharPosition(pageNum, xPercent, yPercent);
                CharPosition charPosition = new CharPosition(pageNum, x, y - fixHeight, (float) rectangle.getWidth(), (float) (rectangle.getHeight() == 0 ? defaultHeight : rectangle.getHeight()));
                charPositions.add(charPosition);
                contentBuilder.append(word);
            }
        }


        @Override
        public void endTextBlock() {
        }

        @Override
        public void renderImage(ImageRenderInfo renderInfo) {
        }


        public String getContent() {
            return contentBuilder.toString();
        }


        public List<CharPosition> getcharPositions() {
            return charPositions;
        }
    }


    private static class CharPosition {
        private int pageNum = 0;
        private float x = 0;
        private float y = 0;
        private float width;
        private float height;


        public CharPosition(int pageNum, float x, float y, float width, float height) {
            this.pageNum = pageNum;
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }


        public int getPageNum() {
            return pageNum;
        }


        public float getX() {
            return x;
        }


        public float getY() {
            return y;
        }

        public float getWidth() {
            return width;
        }

        public float getHeight() {
            return height;
        }

        @Override
        public String toString() {
            return "CharPosition{" +
                    "pageNum=" + pageNum +
                    ", x=" + x +
                    ", y=" + y +
                    ", width=" + width +
                    ", height=" + height +
                    '}';
        }
    }

}
