package com.lmt.Kanban.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController // 1. Đánh dấu đây là nơi nhận request
public class TestController {

    @GetMapping("/hello") // 2. Khi truy cập đường dẫn /hello
    public String sayHello() {
        return "Xin chào! Main App đã kết nối thành công với Controller!";
    }
}