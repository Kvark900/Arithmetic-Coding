package com.kvark900.test.service;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * Created by Keno&Kemo on 07.01.2018..
 */
@Service
public class FileDownloader {
    private IOStreamsCloser ioStreamsCloser;

    public FileDownloader() {
    }

    @Autowired
    public FileDownloader(IOStreamsCloser ioStreamsCloser) {
        this.ioStreamsCloser = ioStreamsCloser;
    }

    public void downloadFile(File compressedFile, HttpServletResponse response ){
        InputStream is = null;
        try {
            // get your file as InputStream
            is = new BufferedInputStream(new FileInputStream(compressedFile));
            // copy it to response's OutputStream
            IOUtils.copy(is, response.getOutputStream());
            response.setHeader("Content-Disposition", "attachment; filename=\""
                    + compressedFile.getName() +"\"");
            response.flushBuffer();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            ioStreamsCloser.closeStream(is);
        }


    }
}
