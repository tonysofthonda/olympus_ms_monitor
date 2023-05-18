package com.honda.olympus.service;

import java.io.IOException;
import java.util.Collection;

import org.springframework.stereotype.Service;

import com.honda.olympus.ftp.client.FtpClient;

@Service
public class MonitorService {

	private FtpClient ftpClient;

	public void listFiles() throws IOException {

		ftpClient = new FtpClient();
		try {

			ftpClient.open();
			Collection<String> files = ftpClient.listFiles();

		} catch (IOException e) {

			e.printStackTrace();
		} finally {
			ftpClient.close();
		}

	}

}
