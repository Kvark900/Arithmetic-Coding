package com.kvark900.test.service;

import com.kvark900.test.service.entropyCoding.IOStreamsCloser;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Keno&Kemo on 07.01.2018..
 */
public class FileUploader {
    private IOStreamsCloser ioStreamsCloser;

    public FileUploader(IOStreamsCloser ioStreamsCloser) {
        this.ioStreamsCloser = ioStreamsCloser;
    }

    public void uploadFile(InputStream inputStream, String originalFileName){
        InputStream is =null;
        FileOutputStream fileOutputStream = null;
        File currDir = new File(".");
        String path = currDir.getAbsolutePath();
        String fileLocation = path.substring(0, path.length() - 1) + originalFileName;

        int ch;
        //uploading file
        try {
            is = inputStream;
            fileOutputStream = new FileOutputStream(fileLocation);
            while ((ch = is.read()) != -1) {
                fileOutputStream.write(ch);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            ioStreamsCloser.closeStream(is);
            ioStreamsCloser.closeStream(fileOutputStream);
            System.gc();
        }
    }
}
