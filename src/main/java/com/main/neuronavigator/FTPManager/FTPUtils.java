package com.main.neuronavigator.FTPManager;

import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.sftp.RemoteResourceInfo;
import net.schmizz.sshj.sftp.SFTPClient;
import net.schmizz.sshj.transport.verification.OpenSSHKnownHosts;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FTPUtils implements FTPUtilsInterface {
    private final String FTP_HOST;
    private final String FTP_USER;
    private final String FTP_PASS;
    private final String FTP_PORT;

    public FTPUtils(String FTP_HOST, String FTP_PORT, String FTP_USER, String FTP_PASS) {
        this.FTP_HOST = FTP_HOST;
        this.FTP_PORT = FTP_PORT;
        this.FTP_USER = FTP_USER;
        this.FTP_PASS = FTP_PASS;
    }

    @Override
    public List<String> listFiles(String number) throws IOException {
        SSHClient client = new SSHClient();
        client.addHostKeyVerifier(new OpenSSHKnownHosts(new File(String.format("%s/.ssh/known_hosts", System.getProperty("user.home")))));
        client.connect(FTP_HOST, Integer.parseInt(FTP_PORT));
        client.authPassword(FTP_USER, FTP_PASS);
        SFTPClient sftpClient = client.newSFTPClient();
        List<RemoteResourceInfo> files = sftpClient.ls("home/NeuroNavigator/" + number + "/");
        sftpClient.close();
        client.disconnect();
        List<String> result = new ArrayList<>();
        for (RemoteResourceInfo f : files) {
            result.add(f.getName());
        }
        return result;
    }

    @Override
    public void uploadFiles(String number, List<File> file) throws IOException {
        SSHClient client = new SSHClient();
        client.addHostKeyVerifier(new OpenSSHKnownHosts(new File(String.format("%s/.ssh/known_hosts", System.getProperty("user.home")))));
        client.connect(FTP_HOST, Integer.parseInt(FTP_PORT));
        client.authPassword(FTP_USER, FTP_PASS);
        SFTPClient sftpClient = client.newSFTPClient();
        for (File f : file) {
            sftpClient.put(f.getAbsolutePath(), "home/NeuroNavigator/" + number + "/" + f.getName());
        }
        sftpClient.close();
        client.disconnect();
    }

    @Override
    public void downloadFiles(String number, List<String> files) throws IOException {
        SSHClient client = new SSHClient();
        client.addHostKeyVerifier(new OpenSSHKnownHosts(new File(String.format("%s/.ssh/known_hosts", System.getProperty("user.home")))));
        client.connect(FTP_HOST, Integer.parseInt(FTP_PORT));
        client.authPassword(FTP_USER, FTP_PASS);
        SFTPClient sftpClient = client.newSFTPClient();
        File dir = new File(String.format("%s\\Downloads\\NeuroNavigator\\", System.getProperty("user.home")));
        if (!dir.exists()) {
            dir.mkdirs();
        }
        for (String f : files) {
            sftpClient.get("home/NeuroNavigator/" + number + "/" + f, String.format("%s\\Downloads\\NeuroNavigator", System.getProperty("user.home")));
        }
        sftpClient.close();
        client.disconnect();
    }

    @Override
    public void createDir(String number) throws IOException {
        SSHClient client = new SSHClient();
        client.addHostKeyVerifier(new OpenSSHKnownHosts(new File(String.format("%s/.ssh/known_hosts", System.getProperty("user.home")))));
        client.connect(FTP_HOST, Integer.parseInt(FTP_PORT));
        client.authPassword(FTP_USER, FTP_PASS);
        SFTPClient sftpClient = client.newSFTPClient();
        sftpClient.mkdirs("home/NeuroNavigator/" + number + "/");
        sftpClient.close();
        client.disconnect();
    }

    @Override
    public void deleteFiles(String number, List<String> file) throws IOException {
        SSHClient client = new SSHClient();
        client.addHostKeyVerifier(new OpenSSHKnownHosts(new File(String.format("%s/.ssh/known_hosts", System.getProperty("user.home")))));
        client.connect(FTP_HOST, Integer.parseInt(FTP_PORT));
        client.authPassword(FTP_USER, FTP_PASS);
        SFTPClient sftpClient = client.newSFTPClient();
        for (String f : file) {
            sftpClient.rm("home/NeuroNavigator/" + number + "/" + f);
        }
        sftpClient.close();
        client.disconnect();
    }

    @Override
    public void renameFile(String oldName, String newName) throws IOException {
        SSHClient client = new SSHClient();
        client.addHostKeyVerifier(new OpenSSHKnownHosts(new File(String.format("%s/.ssh/known_hosts", System.getProperty("user.home")))));
        client.connect(FTP_HOST, Integer.parseInt(FTP_PORT));
        client.authPassword(FTP_USER, FTP_PASS);
        SFTPClient sftpClient = client.newSFTPClient();
        sftpClient.rename("home/NeuroNavigator/" + oldName, "home/NeuroNavigator/" + newName);
        sftpClient.close();
        client.disconnect();
    }

    @Override
    public void delDir(String number) throws IOException {
        SSHClient client = new SSHClient();
        client.addHostKeyVerifier(new OpenSSHKnownHosts(new File(String.format("%s/.ssh/known_hosts", System.getProperty("user.home")))));
        client.connect(FTP_HOST, Integer.parseInt(FTP_PORT));
        client.authPassword(FTP_USER, FTP_PASS);
        SFTPClient sftpClient = client.newSFTPClient();
        sftpClient.rmdir("home/NeuroNavigator/"+number);
        sftpClient.close();
        client.disconnect();
    }

}
