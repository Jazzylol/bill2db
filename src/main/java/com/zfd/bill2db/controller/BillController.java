package com.zfd.bill2db.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zfd.bill2db.config.DataSourceConfig;
import com.zfd.bill2db.config.DataSourceProperties;
import com.zfd.bill2db.mapper.TransactionMapper;
import com.zfd.bill2db.service.TransactionService;
import com.zfd.bill2db.utils.OsUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/bill")
public class BillController {


    @Resource
    private TransactionService transactionService;
    @Resource
    private TransactionMapper transactionMapper;


    @PostMapping("/upload")
    public ResponseEntity<Map<String, String>> uploadFiles(
            @RequestParam("files") MultipartFile[] files,
            @RequestParam(value = "tag", required = false) String tag) {

        File newFile = null;
        try {
            List<String> uploadedFiles = new ArrayList<>();
            Map<String, String> response = new HashMap<>();
            for (MultipartFile file : files) {
                if (file == null || !file.getContentType().equals("text/csv")) {
                    continue;
                }

                String originalFilename = file.getOriginalFilename();
                uploadedFiles.add(originalFilename);
                newFile = new File(OsUtils.getTmpPath() + originalFilename.replace(".csv", "") + "_tmp.csv");
                file.transferTo(newFile);
                boolean result = transactionService.bill2db(newFile, tag != null ? tag.trim() : null);
                log.info("bill2db file name:{},result:{},tag:{}", originalFilename, result, tag);
            }
            response.put("message", "成功上传 " + uploadedFiles.size() + " 个文件: " + String.join(", ", uploadedFiles));
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("uploadFiles error", e);
            Map<String, String> response = new HashMap<>();
            response.put("error", "批量文件上传失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        } finally {
            if (newFile != null && newFile.exists()) {
                newFile.delete();
            }
        }
    }

    @PostMapping("/cleaDatabase")
    public ResponseEntity<Map<String, String>> clearDatabase() {
        Map<String, String> response = new HashMap<>();
        transactionMapper.truncateTable();
        response.put("message", "数据库已清空");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/exportSql")
    public ResponseEntity<byte[]> exportSQL() {
        try {


            StringBuilder sql = new StringBuilder();
            sql.append("SET NAMES utf8mb4;\n\n");

            Map<String, Object> tableInfo;
            if(DataSourceConfig.useMysql) {
                tableInfo = transactionMapper.getCreateTableSQL4Mysql();
            }else {
                tableInfo = transactionMapper.getCreateTableSQL4Sqlite();
                tableInfo.put("Create Table", tableInfo.get("sql"));
                tableInfo.remove("sql");
            }
            String createTableSQL = (String) tableInfo.get("Create Table");
            sql.append("DROP TABLE IF EXISTS `transactions`;\n\n");
            sql.append(createTableSQL).append(";\n\n");

            // 获取所有数据
            List<Map<String, Object>> records = transactionMapper.selectMaps(new QueryWrapper<>());

            if (CollectionUtils.isNotEmpty(records)) {
                sql.append("LOCK TABLES `transactions` WRITE;\n");
                sql.append("/*!40000 ALTER TABLE `transactions` DISABLE KEYS */;\n\n");

                sql.append("INSERT INTO `transactions` (");
                Map<String, Object> firstRecord = records.get(0);
                sql.append(String.join(", ", firstRecord.keySet()));
                sql.append(") VALUES\n");

                for (int i = 0; i < records.size(); i++) {
                    Map<String, Object> record = records.get(i);
                    sql.append("\t(");
                    sql.append(record.values().stream()
                            .map(value -> value == null ? "NULL" :
                                    value instanceof String ? "'" + ((String) value).replace("'", "\\'") + "'" :
                                            value.toString())
                            .collect(Collectors.joining(", ")));
                    sql.append(i == records.size() - 1 ? ");" : "),\n");
                }

                sql.append("\n\nUNLOCK TABLES;");
            }

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            String filename = "transactions_" + LocalDate.now() + ".sql";
            headers.setContentDispositionFormData("attachment", filename);

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(sql.toString().getBytes(StandardCharsets.UTF_8));

        } catch (Exception e) {
            log.error("Export SQL error", e);
            throw new RuntimeException("导出SQL失败: " + e.getMessage());
        }
    }
} 