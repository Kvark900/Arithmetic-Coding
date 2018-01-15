package com.kvark900.test.service;

import org.springframework.stereotype.Service;

import java.io.Closeable;
import java.io.IOException;

/**
 * Created by Keno&Kemo on 02.01.2018..
 */

@Service
public class IOStreamsCloser {

    public void closeStream (Closeable closeable){
        try {
            if(closeable != null){
                closeable.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
