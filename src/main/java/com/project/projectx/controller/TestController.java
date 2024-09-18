package com.project.projectx.controller;

import com.project.projectx.core.model.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/test")
public class TestController {


    @GetMapping
    public ApiResponse<Object> test(){
        return ApiResponse.success("Hello World", "Hello World");
    }
}
