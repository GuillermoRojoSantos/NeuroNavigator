package com.main.neuronavigator.DAOMongoDB;


import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.sftp.SFTPClient;
import net.schmizz.sshj.transport.verification.OpenSSHKnownHosts;
import net.schmizz.sshj.transport.verification.PromiscuousVerifier;

import java.io.File;
import java.io.IOException;

public class testFTP {
    public static void main(String[] args) throws IOException {
        //test sftp connection on port 10025
        SSHClient sshClient = new SSHClient();
        /*sshClient.addHostKeyVerifier(new PromiscuousVerifier());
        sshClient.connect("79.144.96.38", 10025);
        sshClient.authPassword("Deltha", "1234");
        SFTPClient sftpClient = sshClient.newSFTPClient();
        sftpClient.get("home/TestFTP.docx", "ftp.docx");
        sftpClient.close();
        sshClient.disconnect();*/

        //list all files on the sftp server with the same connection
       /* sshClient = new SSHClient();
        sshClient.addHostKeyVerifier(new PromiscuousVerifier());
        sshClient.connect("79.144.96.38", 10025);
        sshClient.authPassword("Deltha", "1234");
        sftpClient = sshClient.newSFTPClient();
        sftpClient.ls("home/").forEach(file -> System.out.println(file.getName()));
        sftpClient.close();
        sshClient.disconnect();*/

        //list all files on the sftp server with their corresponding last modified date
        sshClient = new SSHClient();
        sshClient.addHostKeyVerifier(new OpenSSHKnownHosts(new File(String.format("%s/.ssh/known_hosts", System.getProperty("user.home")))));
        sshClient.connect("79.144.96.38", 10025);
        sshClient.authPassword("Deltha", "1234");
        SFTPClient sftpClient = sshClient.newSFTPClient();
        sftpClient.ls("home/").forEach(file -> System.out.println(file.getName() + " " + file.getAttributes().getType().name()));
        sftpClient.close();
        sshClient.disconnect();

        //upload a file to the sftp server
        /*sshClient = new SSHClient();
        sshClient.addHostKeyVerifier(new PromiscuousVerifier());
        sshClient.connect("79.144.96.38", 10025);
        sshClient.authPassword("Deltha", "1234");
        sftpClient = sshClient.newSFTPClient();
        sftpClient.put("ftp.txt", "home/Test/ftp.txt");
        sftpClient.close();
        sshClient.disconnect();*/

    }

}
