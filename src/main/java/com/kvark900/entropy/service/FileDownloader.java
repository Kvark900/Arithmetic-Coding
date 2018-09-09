package com.kvark900.entropy.service;

import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * Created by Keno&Kemo on 07.01.2018..
 */
@Service
public class FileDownloader {

    public void downloadFile(File compressedFile, HttpServletResponse response ){
        try (InputStream inputStream = new BufferedInputStream(new FileInputStream(compressedFile)))
        {
            IOUtils.copy(inputStream, response.getOutputStream());
            response.setHeader("Content-Disposition", "attachment; filename=\""
                               + compressedFile.getName() +"\"");
            response.flushBuffer();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
