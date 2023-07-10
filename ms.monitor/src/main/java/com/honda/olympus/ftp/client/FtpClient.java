package com.honda.olympus.ftp.client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Vector;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.honda.olympus.exception.MonitorException;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.ChannelSftp.LsEntry;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FtpClient {

    private String server;
    private Integer port;
    private String user;
    private String password;
    private String workDir;
    private Channel channel = null;
    private ChannelSftp channelSftp = null;
    private Session session = null;

    public FtpClient(String server, Integer port, String user, String password, String workDir) {
        super();
        this.server = server;
        this.port = port;
        this.user = user;
        this.password = password;
        this.workDir = workDir;
    }

    public void connect() throws MonitorException {

        try {
            String pass = this.password;
            JSch jsch = new JSch();
            this.session = jsch.getSession(this.user, this.server, this.port);
            this.session.setConfig("StrictHostKeyChecking", "no");
            this.session.setPassword(pass);
            this.session.connect();
            log.debug("Connection established.");
            log.debug("Creating SFTP Channel.");

            this.channel = this.session.openChannel("sftp");
            this.channel.connect();

        } catch (JSchException e4) {
            log.info("Monitor:: Exception ocurred due to: {} ", e4.getLocalizedMessage());

            throw new MonitorException(e4.getLocalizedMessage());

        }
    }

    public boolean listFiles(String filer) throws MonitorException {
        try {
            ArrayList<LsEntry> files;

            this.channelSftp = (ChannelSftp) this.channel;
            this.channelSftp.cd(this.workDir);
            Vector<LsEntry> filelist = this.channelSftp.ls(this.workDir);

            files = new ArrayList<>(filelist);

            if (!filelist.isEmpty()) {

                List<LsEntry> filteredFiles = files.stream()
                        .filter(f -> f.getFilename().startsWith(filer))
                        .collect(Collectors.toList());

                filteredFiles.forEach(f -> log.debug("File name: {}, Is file: {}", f.getFilename(), Pattern.matches("^[\\w,\\s-]+\\.[A-Za-z]{3}$", f.getFilename())));
                return Boolean.TRUE;

            } else {
                return Boolean.FALSE;
            }
        } catch (SftpException e4) {
            log.info("Monitor:: Exception ocurred due to: {} ", e4.getLocalizedMessage());
            throw new MonitorException(e4.getLocalizedMessage());
        }
    }

    public LsEntry listFirstFile(String serviceName, String filer) throws MonitorException {

        try {
            ArrayList<LsEntry> files;

            this.channelSftp = (ChannelSftp) this.channel;
            this.channelSftp.cd(this.workDir);
            Vector<LsEntry> filelist = this.channelSftp.ls(this.workDir);

            files = new ArrayList<>(filelist);

            Optional<LsEntry> ftpFile = files.stream()
                    .filter(f -> f.getFilename().startsWith(filer))
                    .collect(Collectors.toList()).stream().findFirst();

            return ftpFile.orElse(null);

        } catch (SftpException e4) {
            log.info("Monitor:: Exception ocurred due to: {} ", e4.getLocalizedMessage());
            throw new MonitorException(e4.getLocalizedMessage());

        } finally {
            this.channelSftp.exit();
        }

    }

    public void close() throws IOException {
        this.channelSftp.disconnect();
        this.channel.disconnect();
        this.session.disconnect();
        log.info("Monitor:: SFTP channel & session disconnected");
    }
}
