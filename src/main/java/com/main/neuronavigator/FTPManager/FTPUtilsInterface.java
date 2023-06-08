package com.main.neuronavigator.FTPManager;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface FTPUtilsInterface {
    List<String> listFiles(String number) throws IOException;

    void uploadFiles(String number, List<File> file) throws IOException;

    void downloadFiles(String number, List<String> files) throws IOException;

    void createDir(String number) throws IOException;

    void deleteFiles(String number, List<String> file) throws IOException;

    void renameFile(String oldName, String newName) throws IOException;

    void delDir(String number) throws IOException;
}
