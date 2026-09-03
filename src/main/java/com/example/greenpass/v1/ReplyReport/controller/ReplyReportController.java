package com.example.greenpass.v1.ReplyReport.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.greenpass.v1.ReplyReport.services.ReplyReportService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/reply-report")
@RequiredArgsConstructor
public class ReplyReportController {

    private final ReplyReportService replyReportService;

}
