package com.kvark900.test.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;

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

        //uploading file
        try {
            is = inputStream;
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            PrintWriter writer = new PrintWriter(fileLocation, "UTF-8");
            String line;

            while ((line = reader.readLine()) != null) {
                //writing lines and newLineCharacters on the new File
                writer.println(line + '|');
            }
            //adding stop character
            writer.print('~');
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            ioStreamsCloser.closeStream(is);
            System.gc();
        }
    }
}
