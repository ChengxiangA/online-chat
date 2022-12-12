package com.chengxiang.chat.controller;

import com.chengxiang.chat.constant.SocketConstant;
import com.chengxiang.chat.pojo.ResultBody;
import com.chengxiang.chat.service.LocalUploadUtil;
import com.chengxiang.chat.util.FileUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author 程祥
 * @date 2022/11/30 12:26
 */
@RestController
@RequestMapping("/upload")
public class FileController {

    @PostMapping("/avatar")
    public ResultBody uploadAvatar(@RequestParam("file") MultipartFile file) throws IOException {
        String pfxName = FileUtils.getMd5(file.getInputStream());
        String extName = FileUtils.getExtName(file.getOriginalFilename());
        String fileName = pfxName + extName;
        LocalUploadUtil.upload(file.getInputStream(), fileName, SocketConstant.AVATAR_PATH);
        return ResultBody.success("成功上传文件",LocalUploadUtil.getFileAccessUrl(SocketConstant.AVATAR_PATH + fileName));
    }

}
