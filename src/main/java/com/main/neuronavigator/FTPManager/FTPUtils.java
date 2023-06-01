package com.main.neuronavigator.FTPManager;

import com.main.neuronavigator.models.Patient;
import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.sftp.RemoteResourceInfo;
import net.schmizz.sshj.sftp.SFTPClient;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FTPUtils {
    private final String FTP_HOST;
    private final String FTP_USER;
    private final String FTP_PASS;
    private final String FTP_PORT;
    private final String FTP_FINGERPRINT;

    public FTPUtils(String FTP_HOST, String FTP_PORT, String ftpFingerprint, String FTP_USER, String FTP_PASS) {
        this.FTP_HOST = FTP_HOST;
        this.FTP_PORT = FTP_PORT;
        this.FTP_FINGERPRINT = ftpFingerprint;
        this.FTP_USER = FTP_USER;
        this.FTP_PASS = FTP_PASS;
    }

    public List<String> listFiles(Patient p) throws IOException {
        SSHClient client = new SSHClient();
        client.addHostKeyVerifier(FTP_FINGERPRINT);
        client.connect(FTP_HOST, Integer.parseInt(FTP_PORT));
        client.authPassword(FTP_USER, FTP_PASS);
        SFTPClient sftpClient = client.newSFTPClient();
        List<RemoteResourceInfo> files = sftpClient.ls("home/NeuroNavigator/" + p.getPhone() + "/");
        sftpClient.close();
        client.disconnect();
        List<String> result = new ArrayList<>();
        for (RemoteResourceInfo f : files) {
            result.add(f.getName());
        }
        return result;
    }

    public void uploadFiles(Patient p, List<File> file) throws IOException {
        SSHClient client = new SSHClient();
        client.addHostKeyVerifier(FTP_FINGERPRINT);
        client.connect(FTP_HOST, Integer.parseInt(FTP_PORT));
        client.authPassword(FTP_USER, FTP_PASS);
        SFTPClient sftpClient = client.newSFTPClient();
        for (File f : file) {
            sftpClient.put(f.getAbsolutePath(), "home/NeuroNavigator/" + p.getPhone() + "/" + f.getName());
        }
        sftpClient.close();
        client.disconnect();
    }

    public void downloadFiles(Patient p, String[] files) throws IOException {
        SSHClient client = new SSHClient();
        client.addHostKeyVerifier(FTP_FINGERPRINT);
        client.connect(FTP_HOST, Integer.parseInt(FTP_PORT));
        client.authPassword(FTP_USER, FTP_PASS);
        SFTPClient sftpClient = client.newSFTPClient();
        for (String f : files) {
            sftpClient.get("home/NeuroNavigator/" + p.getPhone() + "/" + f, String.format("%s\\Downloads\\NeuroNavigator\\", System.getProperty("user-home")));
        }
        sftpClient.close();
        client.disconnect();
    }

    public void createDir(Patient p) throws IOException {
        SSHClient client = new SSHClient();
        client.addHostKeyVerifier(FTP_FINGERPRINT);
        client.connect(FTP_HOST, Integer.parseInt(FTP_PORT));
        client.authPassword(FTP_USER, FTP_PASS);
        SFTPClient sftpClient = client.newSFTPClient();
        sftpClient.mkdirs("home/NeuroNavigator/" + p.getPhone() + "/");
        sftpClient.close();
        client.disconnect();
    }

    public void deleteFiles(Patient p, List<String> file) throws IOException {
        SSHClient client = new SSHClient();
        client.addHostKeyVerifier(FTP_FINGERPRINT);
        client.connect(FTP_HOST, Integer.parseInt(FTP_PORT));
        client.authPassword(FTP_USER, FTP_PASS);
        SFTPClient sftpClient = client.newSFTPClient();
        for (String f : file) {
            sftpClient.rm("home/NeuroNavigator/" + p.getPhone() + "/" + f);
        }
        sftpClient.close();
        client.disconnect();
    }

    public void renameFile(String oldName, String newName) throws IOException {
        SSHClient client = new SSHClient();
        client.addHostKeyVerifier(FTP_FINGERPRINT);
        client.connect(FTP_HOST, Integer.parseInt(FTP_PORT));
        client.authPassword(FTP_USER, FTP_PASS);
        SFTPClient sftpClient = client.newSFTPClient();
        sftpClient.rename("home/NeuroNavigator/" + oldName, "home/NeuroNavigator/" + newName);
        sftpClient.close();
        client.disconnect();
    }

}
