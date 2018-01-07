package com.kvark900.test.controller;

import com.kvark900.test.service.entropyCoding.ArithmeticCoding;
import com.kvark900.test.service.entropyCoding.IOStreamsCloser;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;

/**
 * Created by Keno&Kemo on 29.12.2017..
 */
@Controller
public class MainController {

    private IOStreamsCloser ioStreamsCloser;
    private ArithmeticCoding arithmeticCoding;

    @Autowired
    public MainController(IOStreamsCloser ioStreamsCloser, ArithmeticCoding arithmeticCoding) {
        this.ioStreamsCloser = ioStreamsCloser;
        this.arithmeticCoding = arithmeticCoding;
    }

    @RequestMapping("/")
    public String home(){
        return "home";
    }

    @RequestMapping(value = "/compressFile", method = RequestMethod.POST)
    public void compressFile(@RequestParam("fileToCompress") MultipartFile file, ModelMap modelMap,
                             MultipartHttpServletRequest request, HttpServletResponse response) {

        InputStream inputStream =null;
        FileOutputStream fileOutputStream = null;
        File currDir = new File(".");
        String path = currDir.getAbsolutePath();
        String fileLocation = path.substring(0, path.length() - 1) + file.getOriginalFilename();
        String compressedFilePath = FilenameUtils.getBaseName(fileLocation)+".arit";
        File compressedFile = null;
        int ch;

        //uploading file
        try {
            inputStream = file.getInputStream();
            fileOutputStream = new FileOutputStream(fileLocation);
            while ((ch = inputStream.read()) != -1) {
                fileOutputStream.write(ch);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            ioStreamsCloser.closeStream(inputStream);
            ioStreamsCloser.closeStream(fileOutputStream);
            System.gc();
        }

        //compressing file
        try {
            compressedFile = new File(compressedFilePath);
            BigDecimal encodedMessage = arithmeticCoding.encodeFile(new File(fileLocation));
            arithmeticCoding.createCompressedFile(compressedFile, encodedMessage);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        //downloading compressed file
        try {
            // get your file as InputStream
            InputStream is = new BufferedInputStream(new FileInputStream(compressedFile));            // copy it to response's OutputStream
            IOUtils.copy(is, response.getOutputStream());
            response.setHeader("Content-Disposition", "attachment; filename=\"" + compressedFile.getName() +"\"");
            response.flushBuffer();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
