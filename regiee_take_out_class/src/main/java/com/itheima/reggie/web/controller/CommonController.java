package com.itheima.reggie.web.controller;


import com.itheima.reggie.common.R;
import com.itheima.reggie.utils.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.UUID;

/**
 * @Author swk
 * @Date 2022/6/11 15:32
 * @Version 1.0
 */
/*文件上传下载控制器*/
    @Slf4j
@RestController
@RequestMapping("/common")
public class CommonController {

    @Value("${reggie.path}")
    private String basePath;
    /*处理上传请求*/
    @PostMapping("/upload")
    public R<String> upload(MultipartFile file) throws IOException {
        //接收上传数据
        //1.获取文件名原始文件名,获取其真实的后缀（可以不用）,截取后缀名
        String ofn = file.getOriginalFilename();
        int dotIndex = ofn.lastIndexOf(".");
        String suffiexName = ofn.substring(dotIndex);
        // 2. 使用`UUID`生成新的文件名，避免重名；
        String fileNameWithPath = FileUtil.getFileNameWithPath();
        // 3. 通过随机算法分配创建多级目录，避免一个目录中存放过多文件
        FileUtil.makeDirs(fileNameWithPath, basePath);
        // 4. 将上传的临时文件转存到指定位置
        file.transferTo(new File(basePath, fileNameWithPath + "." + suffiexName));

        // 5. 组织数据，响应数据
        return R.success("文件是上传成功", fileNameWithPath + "." + suffiexName);



    }


    /*下载*/
    @GetMapping("/download")
    public void downLoad(HttpServletResponse response,String name) throws Exception {
        //1.定义输入流，关联拼接后的对应文件,如果图片文件不存在，就直接结束请求

            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(new File(basePath + name)));
        //2.通过response对象，获取输出流
        ServletOutputStream os = response.getOutputStream();
       //3.通过response对象设置响应数据格式（“image/jpeg”）
        response.setContentType("image/jpeg");
        //4. 通过输入流读取文件数据，然后通过上述的输出流写回浏览器
        int length =0;
        byte[] bytes = new byte[1024];
        while((length = bis.read(bytes))>0){
            os.write(bytes,0,length);
            os.flush();
        }

        //5.关闭资源
        os.close();

    }
}
