package com.kvark900.entropy.controller;

import com.kvark900.entropy.service.FileDownloader;
import com.kvark900.entropy.service.FileUploader;
import com.kvark900.entropy.service.arithmeticCoding.ArithmeticCodingSimple;
import com.kvark900.entropy.service.arithmeticCoding.ArithmeticDecoding;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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

    private ArithmeticDecoding arithmeticDecoding;
    private ArithmeticCodingSimple arithmeticCodingSimple;
    private FileUploader fileUploader;
    private FileDownloader fileDownloader;

    @Autowired
    public MainController(ArithmeticDecoding arithmeticDecoding, ArithmeticCodingSimple arithmeticCodingSimple,
                          FileUploader fileUploader, FileDownloader fileDownloader) {
        this.arithmeticDecoding = arithmeticDecoding;
        this.arithmeticCodingSimple = arithmeticCodingSimple;
        this.fileUploader = fileUploader;
        this.fileDownloader = fileDownloader;
    }

    @RequestMapping("/")
    public String home(){
        return "home";
    }

    @RequestMapping(value = "/compressFile", method = RequestMethod.POST)
    public String compressFile(@RequestParam("fileToCompress") MultipartFile file, Model model,
                             MultipartHttpServletRequest request, HttpServletResponse response) throws IOException {

        if(file.isEmpty()){
            model.addAttribute("noFileSelectedToCompress", true);
            return "home";
        }

        if(!file.getContentType().equals("text/plain")){
            model.addAttribute("notTextFile", true);
            return "home";
        }

        else {
            File currDir = new File(".");
            String path = currDir.getAbsolutePath();
            String fileLocation = path.substring(0, path.length() - 1) + file.getOriginalFilename();
            String compressedFilePath = FilenameUtils.getBaseName(fileLocation) + ".arit";

            //uploading file

            fileUploader.uploadTextFile(file.getInputStream(), fileLocation);


            //compressing file
            File compressedFile = new File(compressedFilePath);
            BigDecimal encodedMessage;

            encodedMessage = arithmeticCodingSimple.encodeWithSimpleProbabilities(new File(fileLocation));

            arithmeticCodingSimple.createCompressedFile(compressedFile, encodedMessage);

            //downloading compressed file
            fileDownloader.downloadFile(compressedFile, response);
            return "home";
        }

    }

    @RequestMapping(value = "/decompressFile", method = RequestMethod.POST)
    public String decompressFile(@RequestParam("fileToDecompress") MultipartFile file, Model model,
                               MultipartHttpServletRequest request, HttpServletResponse response  ) throws IOException {

        if(file.isEmpty()){
            model.addAttribute("noFileSelectedToDeCompress", true);
            return "home";
        }

        else{
            File currDir = new File(".");
            String path = currDir.getAbsolutePath();
            String fileLocation = path.substring(0, path.length() - 1) + file.getOriginalFilename();
            String decompressedFilePath = FilenameUtils.getBaseName(fileLocation)+".txt";

            //uploading file
            fileUploader.uploadFileToDecompress(file.getInputStream(), fileLocation);

            //decompressing file
            File decompressedFile = new File(decompressedFilePath);
            arithmeticDecoding.decodeFile(new File(fileLocation));

            //downloading decompressed file
            fileDownloader.downloadFile(decompressedFile, response);
            return "home";
        }


    }
}
