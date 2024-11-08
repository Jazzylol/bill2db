package com.zfd.bill2db.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/bill")
public class BillController {

    @PostMapping("/upload")
    public ResponseEntity<Map<String, String>> uploadFiles(@RequestParam("files") MultipartFile[] files) {
        try {

            System.out.println("batch exec ");
            List<String> uploadedFiles = new ArrayList<>();
            
            for (MultipartFile file : files) {
                String originalFilename = file.getOriginalFilename();
                // TODO: 在这里处理每个文件的保存逻辑
                uploadedFiles.add(originalFilename);
            }
            
            Map<String, String> response = new HashMap<>();
            response.put("message", "成功上传 " + uploadedFiles.size() + " 个文件: " + String.join(", ", uploadedFiles));
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("error", "批量文件上传失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
} 