package com.kvark900.test.controller;

import com.kvark900.test.service.FileDownloader;
import com.kvark900.test.service.FileUploader;
import com.kvark900.test.service.entropyCoding.ArithmeticCoding;
import com.kvark900.test.service.IOStreamsCloser;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;

/**
 * Created by Keno&Kemo on 29.12.2017..
 */
@Controller
public class MainController {

    private IOStreamsCloser ioStreamsCloser;
    private ArithmeticCoding arithmeticCoding;
    private FileUploader fileUploader;
    private FileDownloader fileDownloader;

    @Autowired
    public MainController(IOStreamsCloser ioStreamsCloser, ArithmeticCoding arithmeticCoding, FileUploader fileUploader, FileDownloader fileDownloader) {
        this.ioStreamsCloser = ioStreamsCloser;
        this.arithmeticCoding = arithmeticCoding;
        this.fileUploader = fileUploader;
        this.fileDownloader = fileDownloader;
    }

    @RequestMapping("/")
    public String home(){
        return "home";
    }

    @RequestMapping(value = "/compressFile", method = RequestMethod.POST)
    public void compressFile(@RequestParam("fileToCompress") MultipartFile file, ModelMap modelMap,
                             MultipartHttpServletRequest request, HttpServletResponse response) throws IOException {

        File currDir = new File(".");
        String path = currDir.getAbsolutePath();
        String fileLocation = path.substring(0, path.length() - 1) + file.getOriginalFilename();
        String compressedFilePath = FilenameUtils.getBaseName(fileLocation)+".arit";

        //uploading file
        fileUploader.uploadFile(file.getInputStream(), fileLocation);

        //compressing file
        File compressedFile = new File(compressedFilePath);
        BigDecimal encodedMessage = arithmeticCoding.encodeFile(new File(fileLocation));
        arithmeticCoding.createCompressedFile(compressedFile, encodedMessage);

        //downloading compressed file
        fileDownloader.downloadFile(compressedFile, response);
    }
}
