package com.kvark900.entropy.service;

import org.springframework.stereotype.Service;

import java.io.*;

/**
 * Created by Keno&Kemo on 07.01.2018..
 */
@Service
public class FileUploader {

    public void uploadTextFileToCompress(InputStream inputStream, File uploadedFile){
        //uploading file
        try (
             InputStream is = inputStream;
             BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
             PrintWriter printWriter = new PrintWriter(uploadedFile))
        {
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                //writing lines and newLineCharacters to a new File
                printWriter.println(line + '|');
            }
            //adding stop character
            printWriter.print('~');

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void uploadFileToDecompress(InputStream inputStream, File uploadedFile){
        int ch;
        //uploading file
        try(InputStream is = inputStream;
            FileOutputStream fileOutputStream = new FileOutputStream(uploadedFile)) {
            while ((ch = is.read()) != -1) {
                fileOutputStream.write(ch);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
