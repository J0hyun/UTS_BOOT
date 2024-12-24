package com.mbc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ReviewController {

    @GetMapping(value = "/review")
    public String review(){


        return "review/reviewForm";

    }
}
