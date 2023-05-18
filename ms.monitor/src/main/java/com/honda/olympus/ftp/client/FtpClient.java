package com.honda.olympus.ftp.client;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class FtpClient {

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

	private FTPClient ftp;

	public void open() throws IOException {
		ftp = new FTPClient();

		ftp.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));

		ftp.connect(server, port);
		int reply = ftp.getReplyCode();
		if (!FTPReply.isPositiveCompletion(reply)) {
			ftp.disconnect();
			throw new IOException("Exception in connecting to FTP Server");
		}

		ftp.login(user, password);

	}

	public Collection<String> listFiles() throws IOException {
		FTPFile[] files = ftp.listFiles(workDir);
		return Arrays.stream(files).map(FTPFile::getName).collect(Collectors.toList());
	}

	public void close() throws IOException {
		ftp.disconnect();
	}
}
