package pojo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class UserInfo {

    private String tableName;

    private String uuid;

    @ExcelProperty(value = "ID")
    private String id;

    @ExcelProperty(value = "NAME")
    private String name;

    @ExcelProperty(value = "AGE")
    private String age;

    @ExcelProperty(value = "ADDRESS")
    private String address;

    @ExcelProperty(value = "PHONE")
    private String phone;
}
