package com.example.sftp.upload;


import org.apache.sshd.sftp.client.SftpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.expression.common.LiteralExpression;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.file.remote.gateway.AbstractRemoteFileOutboundGateway;
import org.springframework.integration.file.remote.gateway.AbstractRemoteFileOutboundGateway.Command;
import org.springframework.integration.file.remote.session.CachingSessionFactory;
import org.springframework.integration.file.remote.session.SessionFactory;
import org.springframework.integration.sftp.gateway.SftpOutboundGateway;
import org.springframework.integration.sftp.outbound.SftpMessageHandler;
import org.springframework.integration.sftp.session.DefaultSftpSessionFactory;
import org.springframework.messaging.MessageHandler;

import java.time.LocalDateTime;

@Configuration
public class UploadConfiguration {

    @Bean
    public DefaultSftpSessionFactory gimmeFactory(){
        DefaultSftpSessionFactory factory = new DefaultSftpSessionFactory();
        factory.setHost("0.0.0.0");
        factory.setPort(22);
        factory.setAllowUnknownKeys(true);
        factory.setUser("mike");
        factory.setPassword("password123");
        return factory;
    }
    @Bean
    public SessionFactory<SftpClient.DirEntry> sftpSessionFactory() {
        DefaultSftpSessionFactory sf = new DefaultSftpSessionFactory();
        sf.setHost("127.0.0.1");
        sf.setPort(22);
        sf.setAllowUnknownKeys(true);
        sf.setUser("mike");
        sf.setPassword("password123");
        return new CachingSessionFactory<>(sf);
    }

    @Bean
    @ServiceActivator(inputChannel = "uploadfile")
    MessageHandler uploadHandler(DefaultSftpSessionFactory factory){
        SftpMessageHandler messageHandler = new SftpMessageHandler(factory);
        messageHandler.setRemoteDirectoryExpression(new LiteralExpression("/upload/topsecret"));
        messageHandler.setFileNameGenerator(message -> String.format("mytextfile_%s.txt", LocalDateTime.now()));
        return messageHandler;
    }

    @Bean
    @ServiceActivator(inputChannel = "sftpChannel")
    public MessageHandler handler() {
//        return new SftpOutboundGateway(sftpSessionFactory(), "ls","'/upload/'");
        SftpOutboundGateway sftpOutboundGateway = new SftpOutboundGateway(sftpSessionFactory(), "ls", "'/upload/'");
        sftpOutboundGateway.setOptions("-dirs"); // This option forces the ls command to include the trailing slash for directories
        return sftpOutboundGateway;
    }


}