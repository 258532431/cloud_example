package com.cloud.common.excel;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @program: cloud_example
 * @description: excel导入工具类
 * @author: yangchenglong
 * @create: 2019-07-19 14:18
 */
public class ImportExcelUtils {

    /**
     * @Author: yangchenglong on 2019/7/19
     * @Description: 从excel导入数据到List
     * update by:
     * @Param: file：excel文件，titleRows：标题栏row，headerRows：抬头栏row，pojoClass：实体类类型
     * @return:
     */
    public static<T> List<T> importExcel(MultipartFile file, Integer titleRows, Integer headerRows, Class<T> pojoClass) throws Exception {
        if (file == null){
            return null;
        }
        ImportParams params = new ImportParams();
        params.setTitleRows(titleRows);
        params.setHeadRows(headerRows);
        List<T> list = ExcelImportUtil.importExcel(file.getInputStream(), pojoClass, params);

        return list;
    }

}
