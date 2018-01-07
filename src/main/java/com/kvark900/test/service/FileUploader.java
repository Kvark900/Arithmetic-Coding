package com.kvark900.test.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Keno&Kemo on 07.01.2018..
 */
@Service
public class FileUploader {
    private IOStreamsCloser ioStreamsCloser;

    public FileUploader() {
    }

    @Autowired
    public FileUploader(IOStreamsCloser ioStreamsCloser) {
        this.ioStreamsCloser = ioStreamsCloser;
    }

    public void uploadFile(InputStream inputStream, String fileLocation){
        InputStream is =null;
        FileOutputStream fileOutputStream = null;

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
