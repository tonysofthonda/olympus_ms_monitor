package com.honda.olympus.service;

import java.io.IOException;
import java.util.Optional;

import org.apache.commons.net.ftp.FTPFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.honda.olympus.ftp.client.FtpClient;
import com.honda.olympus.utils.MonitorConstants;
import com.honda.olympus.vo.EventVO;
import com.honda.olympus.vo.MessageVO;
import com.honda.olympus.vo.TransferFileVO;

@Service
public class MonitorService {

	@Autowired
	LogEventService logEventService;

	@Autowired
	NotificationService notificationService;
	
	@Autowired
	TransferFileService transferFileService;

	@Value("${service.name}")
	private String serviceName;

	@Value("${mftp.credentials.host}")
	private String server;

	@Value("${mftp.credentials.port}")
	private Integer port;

	@Value("${mftp.credentials.user}")
	private String user;

	@Value("${mftp.credentials.pass}")
	private String password;

	@Value("${monitor.workdir.inbound}")
	private String inBound;

	@Value("${mftp.credentials.host}")
	private String host;

	public void listFiles() throws IOException {

		FtpClient ftpClient = new FtpClient(server, port, user, password, inBound);
		EventVO event;

		try {

			ftpClient.open();

			event = new EventVO(serviceName, MonitorConstants.ONE_STATUS, "Conexión al MFTP: " + host, " fué exitosa");
			logEventService.sendLogEvent(event);

			if(ftpClient.listFiles()) {
				
				FTPFile ftpFile = ftpClient.listFirstFile();
				
				System.out.println("First file: " + ftpClient.listFirstFile());
				
				event = new EventVO(serviceName, MonitorConstants.TWO_STATUS, "SUCCESS", "");
				logEventService.sendLogEvent(event);
				
				TransferFileVO transeferMessage = new TransferFileVO(1L,"SUCCESS",ftpFile.getName());
				
				transferFileService.sendTransferFileEvent(transeferMessage);
				
			}else {
				
				System.out.println("End second altern flow, empty directory");
				event = new EventVO(serviceName, MonitorConstants.TWO_STATUS, "No existen archivos para procesar", "");
				logEventService.sendLogEvent(event);

			}
			
			

		} catch (IOException e) {

			System.out.println("End first cancel/change altern flow");
			event = new EventVO(serviceName, MonitorConstants.ZERO_STATUS, "Fallo en la conexión al MFTP: " + host, "");
			logEventService.sendLogEvent(event);

			MessageVO messageEvent = new MessageVO(serviceName, MonitorConstants.ONE_STATUS,
					"Fallo en la conexión al MFTP: " + host, "");
			notificationService.generatesNotification(messageEvent);

			e.printStackTrace();
		} finally {
			ftpClient.close();
		}

	}

}
