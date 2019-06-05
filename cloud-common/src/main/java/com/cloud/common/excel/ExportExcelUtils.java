package com.cloud.common.excel;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @program: common
 * @description: Excel导出工具类
 * @author: yangchenglong
 * @create: 2019-06-04 11:50
 */
@Slf4j
public class ExportExcelUtils {

    //普通导出
    public void exportListEntity(HttpServletResponse response, List datas, String headers, String attrs, String sheetName, String dateFormat, String fileName, int colWidth) {
        new ExportListEntity(response, datas, headers, attrs, sheetName, dateFormat, fileName, colWidth).export();
    }

    //多线程导出
    public void exportListEntityThread(HttpServletResponse response, List datas, String headers, String attrs, String sheetName, String dateFormat, String fileName, int colWidth) {
        new ExportListEntityThread(response, datas, headers, attrs, sheetName, dateFormat, fileName, colWidth).run();
    }

    //普通导出
    class ExportListEntity {
        private HttpServletResponse response;
        private List datas;//数据列表
        private String headers;//列标题，英文逗号分隔
        private String attrs;//对应列标题的字段属性名称，英文逗号分隔
        private String sheetName;//sheet名称
        private String dateFormat = "yyyy-MM-dd HH:mm:ss";//默认日期格式：yyyy-MM-dd HH:mm:ss
        private String fileName = "default";//文件名称：考勤记录表
        private int colWidth = 8;//列宽：默认8个汉字宽度

        public ExportListEntity(HttpServletResponse response, List datas, String headers, String attrs, String sheetName, String dateFormat, String fileName, int colWidth) {
            this.response = response;
            this.datas = datas;
            this.headers = headers;
            this.attrs = attrs;
            this.sheetName = sheetName;
            if (!StringUtils.isBlank(dateFormat)) {
                this.dateFormat = dateFormat;
            }
            this.fileName = fileName;
            if(colWidth > 0){
                this.colWidth = colWidth;
            }
        }

        public void export(){
            SXSSFWorkbook wb = process(datas, headers, attrs, sheetName, dateFormat, fileName, colWidth);
            if(wb != null){
                try {
                    Date startDate = new Date();
                    log.info("ExportListEntity 开始输出文件:"+ DateFormatUtils.format(startDate, "yyyy-MM-dd HH:mm:ss"));

                    //写入文件-测试用
                    /*FileOutputStream out = new FileOutputStream("D:\\"+fileName+".xlsx");
                    wb.write(out);
                    out.close();*/

                    //写入流
                    response.setHeader("content-Type","application/vnd.ms-excel");// 下载文件的默认名称       
                    response.setHeader("Content-disposition","attachment;filename="+ URLEncoder.encode(""+fileName+".xlsx","utf-8"));
                    OutputStream out = response.getOutputStream();
                    wb.write(out);
                    out.close();

                    log.info("ExportListEntity 输出完成:"+ DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
                    long time = new Date().getTime() - startDate.getTime();
                    log.info("ExportListEntity 输出文件耗时:"+(time/1000)+"秒,");
                } catch (IOException io){
                    log.error("ExportListEntity Error:", io);
                }
            }
        }

    }

    //多线程导出
    class ExportListEntityThread implements Runnable {
        private HttpServletResponse response;
        private List datas;//数据列表
        private String headers;//列标题，英文逗号分隔
        private String attrs;//对应列标题的字段属性名称，英文逗号分隔
        private String sheetName;//sheet名称
        private String dateFormat = "yyyy-MM-dd HH:mm:ss";//默认日期格式：yyyy-MM-dd HH:mm:ss
        private String fileName = "default";//文件名称：考勤记录表
        private int colWidth = 8;//列宽：默认8个汉字宽度

        public ExportListEntityThread(HttpServletResponse response, List datas, String headers, String attrs, String sheetName, String dateFormat, String fileName, int colWidth) {
            this.response = response;
            this.datas = datas;
            this.headers = headers;
            this.attrs = attrs;
            this.sheetName = sheetName;
            if (!StringUtils.isBlank(dateFormat)) {
                this.dateFormat = dateFormat;
            }
            this.fileName = fileName;
            if(colWidth > 0){
                this.colWidth = colWidth;
            }
        }

        @Override
        public void run() {
            SXSSFWorkbook wb = process(datas, headers, attrs, sheetName, dateFormat, fileName, colWidth);
            if(wb != null){
                try {
                    Date startDate = new Date();
                    log.info("ExportListEntityThread 开始输出文件:"+ DateFormatUtils.format(startDate, "yyyy-MM-dd HH:mm:ss"));

                    //写入文件-测试用
                    /*FileOutputStream out = new FileOutputStream("D:\\"+fileName+".xlsx");
                    wb.write(out);
                    out.close();*/

                    //写入流
                    response.setHeader("content-Type","application/vnd.ms-excel");// 下载文件的默认名称       
                    response.setHeader("Content-disposition","attachment;filename="+ URLEncoder.encode(""+fileName+".xlsx","utf-8"));
                    OutputStream out = response.getOutputStream();
                    wb.write(out);
                    out.close();

                    log.info("ExportListEntityThread 输出完成:"+ DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
                    long time = new Date().getTime() - startDate.getTime();
                    log.info("ExportListEntityThread 输出文件耗时:"+(time/1000)+"秒,");
                } catch (IOException io) {
                    log.error("ExportListEntityThread Error:", io);
                }
            }
        }
    }

    //导出过程处理
    private SXSSFWorkbook process(List datas, String headers, String attrs, String sheetName, String dateFormat, String fileName, int colWidth) {
        try {
            Date startDate = new Date();
            log.info("ExportExcelUtils start time :"+ DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
            //数据检查
            if (datas == null || datas.isEmpty()) {
                log.error("ExportExcelUtils Error: datas not be empty!");
                return null;
            }
            log.info("ExportExcelUtils datas.size={}", datas.size());
            if (StringUtils.isBlank(headers) || StringUtils.isBlank(attrs)) {
                log.error("ExportExcelUtils Error: headers or attrs not be empty!");
                return null;
            }
            if (StringUtils.isBlank(sheetName)) {
                log.error("ExportExcelUtils Error: sheetName not be empty!");
                return null;
            }

            //定义工作簿
            SXSSFWorkbook wb = new SXSSFWorkbook(1000);//超过1000行将之前的数据写入硬盘，防止内存溢出
            String[] headerArr = headers.split(",");
            String[] attrArr = attrs.split(",");
            List<String> attrList = Arrays.asList(attrArr);

            //字体
            Font font = wb.createFont();
            font.setFontHeightInPoints((short)12);//设置字号
            //font.setColor(IndexedColors.DARK_BLUE.getIndex());//设置字体颜色
            font.setBoldweight(Font.BOLDWEIGHT_BOLD);

            //样式1
            CellStyle styleBOLD = createBorderedStyle(wb);
            styleBOLD.setFont(font);
            styleBOLD.setVerticalAlignment(CellStyle.VERTICAL_CENTER);//居中
            styleBOLD.setWrapText(false); //自动换行

            //样式2
            CellStyle styleWrap = createBorderedStyle(wb);
            styleWrap.setWrapText(false);
            styleWrap.setVerticalAlignment(CellStyle.VERTICAL_TOP);

            //表
            SXSSFSheet sheet = wb.createSheet(sheetName);

            //行
            Row row = sheet.createRow(0);

            //单元格
            Cell cell;
            //写入标题行
            for (int i = 0; i < headerArr.length; i++) {
                cell = row.createCell(i);
                cell.setCellValue(headerArr[i]);
                cell.setCellStyle(styleBOLD);
            }

            //写入数据
            Object obj;     //实体类
            Field[] fields; //属性数组
            Field field;    //属性
            Object value;   //属性值
            DateFormat df = new SimpleDateFormat(dateFormat); //默认yyyy-MM-dd HH:mm:ss
            for (int i = 0; i < datas.size(); i++) {
                row = sheet.createRow(i + 1);
                obj = datas.get(i);
                // 利用反射，获取属性数组
                fields = obj.getClass().getDeclaredFields();
                for (int f = 0; f < fields.length; f++) {
                    field = fields[f];
                    if (attrList.contains(field.getName())) {
                        cell = row.createCell(attrList.indexOf(field.getName()));
                        cell.setCellStyle(styleWrap);
//                            cell.setCellType(Cell.CELL_TYPE_STRING);
                        field.setAccessible(true); //设置些属性是可以访问的
                        value = field.get(obj);      //得到此属性的值
                        if(value == null){
                            continue;
                        }
                        if(value instanceof Date){
                            cell.setCellValue(df.format((Date)value));
                        }else {
                            cell.setCellValue(value.toString());
                        }
                    }
                }
            }

            // 自动调整列宽
            sheet.trackAllColumnsForAutoSizing();
            for (int i = 0; i < headerArr.length; i++) {
//                    sheet.autoSizeColumn(i); // 自动列宽度
                sheet.setColumnWidth(i, colWidth * 256 * 2);  //设置列宽，50个字符宽度。宽度参数为1/256，故乘以256，中文的要再乘以2
//                    sheet.setColumnWidth(i, 3000);
            }

            log.info("ExportExcelUtils end time :"+ DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
            long time = new Date().getTime() - startDate.getTime();
            log.info("ExportExcelUtils 数据处理耗时:"+(time/1000)+"秒, 下面开始输出文件");

            return wb;
        } catch (Exception e) {
            log.error("ExportExcelUtils Error:", e);
            return null;
        }
    }

    //创建样式
    private CellStyle createBorderedStyle(Workbook wb) {
        CellStyle style = wb.createCellStyle();
        style.setBorderRight(CellStyle.BORDER_THIN);
        style.setRightBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderBottom(CellStyle.BORDER_THIN);
        style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderLeft(CellStyle.BORDER_THIN);
        style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderTop(CellStyle.BORDER_THIN);
        style.setTopBorderColor(IndexedColors.BLACK.getIndex());
        return style;
    }

}
