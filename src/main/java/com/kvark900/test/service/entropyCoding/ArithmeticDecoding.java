package com.kvark900.test.service.entropyCoding;

import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.math.BigDecimal;

/**
 * Created by Keno&Kemo on 02.01.2018..*/

@Service
public class ArithmeticDecoding extends FileData {

    private IOStreamsCloser ioStreamsCloser = new IOStreamsCloser();

    //Read from compressed file - get message to decode
    public BigDecimal getEncodedMessage(File file){

        FileInputStream fileInputStream = null;
        ObjectInputStream objectInputStream = null;
        BigDecimal messageToDecode = null;

        try {
            fileInputStream = new FileInputStream(file);
            objectInputStream = new ObjectInputStream(fileInputStream);
            messageToDecode = (BigDecimal) objectInputStream.readObject();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            ioStreamsCloser.closeStream(fileInputStream);
            ioStreamsCloser.closeStream(objectInputStream);
        }
        return messageToDecode;
    }

    public void decodeFile (BigDecimal messageToDecode){


    }
}
