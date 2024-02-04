package impl;

import listener.ReadEasyExeclAsyncListener;
import org.springframework.stereotype.Service;
import pojo.UserInfo;
import service.ReadEasyExeclService;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReadEasyExeclServiceImpl implements ReadEasyExeclService {

    @Resource
    private ReadEasyExeclMapper readEasyExeclMapper;

    @Override
    public void saveDataBatch(List<UserInfo> list) {
        // 通过mybatis入库
        readEasyExeclMapper.saveDataBatch(list);
        // 通过JDBC入库
        // insertByJdbc(list);
        list.clear();
    }

    private void insertByJdbc(List<UserInfo> list){
        List<String> sqlList = new ArrayList<>();
        for (UserInfo u : list){
            StringBuilder sqlBuilder = new StringBuilder();
            sqlBuilder.append("insert into ").append(u.getTableName()).append(" ( UUID,ID,NAME,AGE,ADDRESS,PHONE,OP_TIME ) values ( ");
            sqlBuilder.append("'").append(ReadEasyExeclAsyncListener.uuid()).append("',")
                    .append("'").append(u.getId()).append("',")
                    .append("'").append(u.getName()).append("',")
                    .append("'").append(u.getAge()).append("',")
                    .append("'").append(u.getAddress()).append("',")
                    .append("'").append(u.getPhone()).append("',")
                    .append("sysdate )");
            sqlList.add(sqlBuilder.toString());
        }

        JdbcUtil.executeDML(sqlList);
    }
}
