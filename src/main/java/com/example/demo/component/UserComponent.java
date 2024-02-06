package com.example.demo.component;


import com.example.demo.excel.ExcelUsers;
import com.example.demo.model.User;
import com.example.demo.service.UserService;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserComponent {


    private static UserService userService;

    private static ExcelUsers excelUsers;

    @Autowired
    public UserComponent(UserService userService,ExcelUsers excelUsers) {
        UserComponent.userService = userService;
        UserComponent.excelUsers = excelUsers;
    }

    public static void save(User user){
        userService.save(user);
    }

    public static boolean getById(Long id){
        return userService.getUserById(id);
    }

    public static List<User> getByRegion(String id){
        return userService.getUserByRegion(id);
    }

    public static List<User> getList(){
        return userService.getUsers();
    }

    public static User getUserById(Long id){
        return userService.getById(id);
    }
    public static void getUsersInExcel(XSSFWorkbook workbook){
        excelUsers.getUsersInExcel(workbook);
    }
}
