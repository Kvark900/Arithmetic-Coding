package com.kvark900.entropy.service;

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

    public void uploadTextFileToCompress(InputStream inputStream, File uploadedFile){
        InputStream is =null;
        PrintWriter writer = null;
        BufferedReader reader = null;

        //uploading file
        try {
            is = inputStream;
            reader = new BufferedReader(new InputStreamReader(is));
            writer = new PrintWriter(uploadedFile, "UTF-8");
            String line;

            while ((line = reader.readLine()) != null) {
                //writing lines and newLineCharacters to a new File
                writer.println(line + '|');
            }
            //adding stop character
            writer.print('~');

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            ioStreamsCloser.closeStream(is);
            ioStreamsCloser.closeStream(reader);
            ioStreamsCloser.closeStream(writer);
            ioStreamsCloser.closeStream(inputStream);
        }
    }
    public void uploadFileToDecompress(InputStream inputStream, File uploadedFile){
        InputStream is =null;
        FileOutputStream fileOutputStream = null;

        int ch;
        //uploading file
        try {
            is = inputStream;
            fileOutputStream = new FileOutputStream(uploadedFile);
            while ((ch = is.read()) != -1) {
                fileOutputStream.write(ch);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            ioStreamsCloser.closeStream(is);
            ioStreamsCloser.closeStream(fileOutputStream);
            ioStreamsCloser.closeStream(inputStream);
        }
    }
}
