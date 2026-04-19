package com.sky.controller.admin;


import com.sky.constant.MessageConstant;
import com.sky.result.Result;
import com.sky.utils.AliOssUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/admin/common")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "通用接口")
public class CommonController {
    private final AliOssUtil aliOssUtil;

    @PostMapping("/upload")
    @Operation(summary = "文件上传")
    public Result<String> upload(MultipartFile file){
        log.info("文件上传：{}", file);
        try{
            //获取原文件名
            String originalFileName =file.getOriginalFilename();
            //截取其拓展名
            //原理：获取原文件名最后一个.的位置，然后截取包括其在内之后的字符串，即.jpg或.png之类的
            String extensionName = originalFileName.substring(originalFileName.lastIndexOf("."));
            //构建新文件名-用UUID随机生成字符串，加上拓展名
            String newFileName = UUID.randomUUID().toString() + extensionName;
            //文件上传的网址路径
            String URL =aliOssUtil.upload(file.getBytes(), newFileName);
            return Result.success(URL);
        }catch(IOException e){
            log.error("文件上传失败！{}",e);
        }
        return Result.error(MessageConstant.UPLOAD_FAILED);
    }
}
