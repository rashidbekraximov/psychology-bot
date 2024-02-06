package com.example.demo.bot;


public class Data {

    public static final String token = "6583193423:AAHSpclQid3joMVyuluOthrSTGIgiGLhmII";
    public static final String username = "@azizam_supportBot";
//    public static final String token = "6290505373:AAHh9lw7GvR42MIJLQ7bfqUhPuDqtUC1SzQ";
//    public static final String username = "@Genius0420Bot";

    public static void printMessage() {
        System.out.println("Bot ishga tushdi!");
    }

    public static void printError(Exception e) {
        System.out.println("Xatolik: " + e.getMessage());
    }
}
