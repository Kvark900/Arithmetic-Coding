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

    public void uploadTextFile(InputStream inputStream, String fileLocation){
        InputStream is =null;

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
    public void uploadFileToDecompress(InputStream inputStream, String fileLocation){
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
