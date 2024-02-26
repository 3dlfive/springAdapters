package com.example.sftp.upload;


import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FileUtils;
import org.springframework.integration.sftp.session.SftpFileInfo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class UploadRestController {
    private final UploadMessagingGateway gateway;

    @GetMapping("/api/upload/{content}")
    public String uploadFile(@PathVariable String content, @RequestParam("extra") String extra) throws IOException {
        String resultcontent = String.format("Hello... We have uploaded... %s \nwith some extra stuff: %s", content,
                extra);

        File file = new File("tmp/mytmpfile.txt");
        if (file.exists())
            file.delete();

        FileUtils.writeStringToFile(file, resultcontent, "UTF-8");
        gateway.uploadFile(file);

        return resultcontent;
    }
    @GetMapping("/listFiles")
    public String listFiles(@RequestParam String remoteDir) {
        List<SftpFileInfo> fileList = gateway.listFiles(remoteDir);
        StringBuilder result = new StringBuilder("Hello... List of files:\n");

        for (SftpFileInfo fileInfo : fileList) {
            result.append(fileInfo.getFilename()).append("\n");
        }

        return result.toString();
    }
    @GetMapping("/test")
    public String test() {


        return "result.toString()";
    }

}