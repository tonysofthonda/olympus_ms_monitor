package com.honda.olympus.service;

import java.io.IOException;

import org.apache.commons.net.ftp.FTPFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.honda.olympus.exception.MonitorException;
import com.honda.olympus.ftp.client.FtpClient;
import com.honda.olympus.utils.MonitorConstants;
import com.honda.olympus.vo.EventVO;
import com.honda.olympus.vo.MessageVO;
import com.honda.olympus.vo.TransferFileVO;
import com.jcraft.jsch.ChannelSftp.LsEntry;
import com.jcraft.jsch.JSchException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
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

	@Value("${app.file.filter}")
	private String filter;

	public void checkFiles() throws MonitorException,IOException {



		FtpClient ftpClient = new FtpClient(server, port, user, password, inBound);
		EventVO event;

		try {

			ftpClient.connect();

			log.info("Monitor:: Conexión exitosa al MFTP: {}", host);
			event = new EventVO(serviceName, MonitorConstants.ONE_STATUS, "Conexión al MFTP: " + host, " fué exitosa");
			logEventService.sendLogEvent(event);

			if (ftpClient.listFiles(filter)) {

				LsEntry ftpFile = ftpClient.listFirstFile(serviceName, filter);


				log.debug("Monitor:: First file: {}",ftpFile);

				if (ftpFile != null) {
					log.info("Monitor:: Existe archivo en MFTP");
					event = new EventVO(serviceName, MonitorConstants.ONE_STATUS, "SUCCESS", ftpFile.getFilename());
					logEventService.sendLogEvent(event);

					TransferFileVO transeferMessage = new TransferFileVO(1L, "SUCCESS", ftpFile.getFilename());

					transferFileService.sendTransferFileEvent(transeferMessage);
				}else {
					log.info("Monitor:: NO Existen archivos en MFTP: {}", host);
					event = new EventVO(serviceName, MonitorConstants.THREE_STATUS, "No existen archivos para procesar", "");
					logEventService.sendLogEvent(event);
				}
			} else {

				log.info("Monitor:: NO Existen archivos en MFTP: {}", host);
				event = new EventVO(serviceName, MonitorConstants.THREE_STATUS, "No existen archivos para procesar", "");
				logEventService.sendLogEvent(event);

			}

		} catch (MonitorException e) {

			log.info("Fallo en la conexión al MFTP: {}",host);
			event = new EventVO(serviceName, MonitorConstants.ZERO_STATUS, "Fallo en la conexión al MFTP: " + host, "");
			logEventService.sendLogEvent(event);

			MessageVO messageEvent = new MessageVO(serviceName, MonitorConstants.ZERO_STATUS,
					"Fallo en la conexión al MFTP: " + host, "");
			notificationService.generatesNotification(messageEvent);

			throw new MonitorException("Fallo en la conexión al MFTP: " + host);
		} finally {
			ftpClient.close();
		}




	}

}
