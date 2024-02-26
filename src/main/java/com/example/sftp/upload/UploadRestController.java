package com.example.sftp.upload;
import org.apache.sshd.sftp.client.SftpClient;
import org.apache.sshd.sftp.client.SftpClient.DirEntry;

import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.file.remote.FileInfo;
import org.springframework.integration.file.remote.session.SessionFactory;
import org.springframework.integration.sftp.session.DefaultSftpSessionFactory;
import org.springframework.integration.sftp.session.SftpFileInfo;
import org.springframework.integration.sftp.session.SftpSession;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class UploadRestController {
    private final UploadMessagingGateway gateway;
    @Autowired
    private final SessionFactory<DirEntry> sftpSessionFactory;


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
    public String listFiles() {
        List<SftpFileInfo> fileList = gateway.listFiles("remoteDir");
        StringBuilder result = new StringBuilder("Hello... List of files:");

        for (SftpFileInfo fileInfo : fileList) {

            result.append("<br>").append("<b>").append(fileInfo.getRemoteDirectory())
                    .append(fileInfo.getFilename()).append("</b>       ")
                    .append("isDirecoty "+fileInfo.isDirectory()+ "     ")
                    .append(fileInfo.getFileInfo().getAttributes().getModifyTime()
                    );
        }
// Чтение байтов работает
//        InputStream inputStream = null;
//        try {
//            byte[] buffer = new byte[2048]; // Создаем буфер для чтения байтов
//            int bytesRead;
//            inputStream = sftpSessionFactory.getSession().readRaw("/devdt/test/F2005536_clb_05.prm");
//            long skipped = inputStream.skip(2040);
//            if (skipped != 2040) {
//                System.out.println("Не удалось пропустить нужное количество байтов");
//               return "Fail to read";
//            }
//            bytesRead = inputStream.read(buffer, 0, 8);
//            if (bytesRead != 8) {
//                System.out.println("Не удалось прочитать нужное количество байтов");
//                return "Не удалось прочитать нужное количество байтов";
//            }
//            // Конвертируем прочитанные байты в строку и выводим на экран
//            String bytesAsString = new String(buffer, 0, bytesRead);
//            System.out.println("Байты с 2040 по 2047: " + bytesAsString);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }


//        //Создание директории
//        try {
//            sftpSessionFactory.getSession().mkdir("/devdt/test/mktest/");
//            System.out.println("Good test dir created.");
//            result.append("<br> Logs: Dir was created");
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }

        return result.toString();
        // пробую прочитать файл

    }


    @GetMapping("/test")
    public String test() {


        return "Its works";
    }


}