package com.honda.olympus.service;

import java.io.IOException;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.honda.olympus.ftp.client.FtpClient;

@Service
public class MonitorService {

	@Value("${mftp.credentials.host}")
	private String server;

	@Value("${mftp.credentials.port}")
	private Integer port;

	@Value("${mftp.credentials.user}")
	private String user;

	@Value("${mftp.credentials.pass}")
	private String password;
	
	@Value("${monitor.workdir.inbound}")
	private String workDir;

	public void listFiles() throws IOException {
		
		
		FtpClient ftpClient = new FtpClient(server, port, user, password, workDir);
		
		try {

			ftpClient.open();
			Collection<String> files = ftpClient.listFiles();
			
			System.out.println("files.size(): "+files.size());

		} catch (IOException e) {

			e.printStackTrace();
		} finally {
			ftpClient.close();
		}

	}

}
