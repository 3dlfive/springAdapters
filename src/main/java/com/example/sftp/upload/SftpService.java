package com.example.sftp.upload;

import jakarta.websocket.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.sftp.session.DefaultSftpSessionFactory;
import org.springframework.integration.sftp.session.SftpSession;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class SftpService {
    @Autowired
    private DefaultSftpSessionFactory gimmeFactory;
    @Autowired
    private DefaultSftpSessionFactory sftpSessionFactory;
    public void uploadFile(String filename, String path) {
        SftpSession session = gimmeFactory.getSession();
        try {
            FileInputStream fileInputStream = new FileInputStream(new File(path));
            session.write(fileInputStream,"upload"+filename);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        session.close();
    }
    public List<File> listFiles(String remoteDir){
        SftpSession session = sftpSessionFactory.getSession();

        try {
            // Get the list of file names from the remote directory
            String[] fileNames = session.listNames(remoteDir);

            // Create a list to hold File objects
            List<File> fileList = new ArrayList<>();

            // Convert file names to File objects
            for (String fileName : fileNames) {
                fileList.add(new File(remoteDir, fileName));
            }

            return fileList;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
