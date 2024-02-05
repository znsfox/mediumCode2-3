package com.example.mediumcode23.listener;



import com.alibaba.excel.context.AnalysisContext;
import com.example.mediumcode23.pojo.UserInfo;
import com.example.mediumcode23.service.ReadEasyExeclService;

import java.util.List;
import java.util.UUID;

public class ReadEasyExeclAsyncListener {
    public ReadEasyExeclService readEasyExeclService;
    // 表名
    public String TABLE_NAME;
    // 批量插入阈值
    private int BATCH_COUNT;
    // 数据集合
    private List<UserInfo> LIST;

    public ReadEasyExeclAsyncListener(ReadEasyExeclService readEasyExeclService, String tableName, int batchCount, List<UserInfo> list) {
        this.readEasyExeclService = readEasyExeclService;
        this.TABLE_NAME = tableName;
        this.BATCH_COUNT = batchCount;
        this.LIST = list;
    }

    public void invoke(UserInfo data, AnalysisContext analysisContext) {
        data.setUuid(uuid());
        data.setTableName(TABLE_NAME);
        LIST.add(data);
        if(LIST.size() >= BATCH_COUNT){
            // 批量入库
            readEasyExeclService.saveDataBatch(LIST);
        }
    }

    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        if(LIST.size() > 0){
            // 最后一批入库
            readEasyExeclService.saveDataBatch(LIST);
        }
    }

    public static String uuid() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}

