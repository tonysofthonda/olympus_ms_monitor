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
import org.springframework.stereotype.Component;

public class FtpClient {

	private String server;

	private Integer port;

	private String user;

	private String password;

	private String workDir;

	private FTPClient ftp;

	public FtpClient(String server, Integer port, String user, String password, String workDir) {
		super();
		this.server = server;
		this.port = port;
		this.user = user;
		this.password = password;
		this.workDir = workDir;
	}

	public void open() throws IOException {
		ftp = new FTPClient();

		System.out.println("Connection FTP server: " + server);
		
		System.out.println("port: " + port);
		System.out.println("user: " + user);
		System.out.println("workDir: " + workDir);

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
