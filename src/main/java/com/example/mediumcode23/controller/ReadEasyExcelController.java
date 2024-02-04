package controller;

import com.alibaba.excel.EasyExcel;
import listener.ReadEasyExeclAsyncListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import pojo.UserInfo;
import lombok.extern.slf4j.Slf4j;
import service.ReadEasyExeclService;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ReadEasyExcelController {
    private static final Logger logger = LogManager.getLogger(ReadEasyExcelController.class);

    @Autowired
    private ReadEasyExeclService readEasyExeclService;

    @RequestMapping(value = "/readEasyExcel", method = RequestMethod.POST)
    @ResponseBody
    public String readEasyExcel() {
        try {
            String path = "G:\\测试\\data\\";
            String[] xlsxArr = new File(path).list();
            for (int i = 0; i < xlsxArr.length; i++) {
                String filePath = path + xlsxArr[i];
                File fileTemp = new File(path + xlsxArr[i]);
                String fileName = fileTemp.getName().replace(".xlsx", "");
                List<UserInfo> list = new ArrayList<>();
                EasyExcel.read(filePath, UserInfo.class, new ReadEasyExeclAsyncListener(readEasyExeclService, fileName, batchCount, list)).sheet().doRead();
            }
        }catch (Exception e){
            logger.error("readEasyExcel 异常：",e);
            return "error";
        }
        return "suceess";
    }

}
