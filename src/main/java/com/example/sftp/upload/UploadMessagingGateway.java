package com.example.sftp.upload;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.sftp.session.SftpFileInfo;

import java.io.File;
import java.util.List;

@MessagingGateway
public interface UploadMessagingGateway {

    @Gateway(requestChannel = "uploadfile")
    public void uploadFile(File file);
    @Gateway(requestChannel = "sftpChannel")
    public List<SftpFileInfo> listFiles(String remoteDir);

}