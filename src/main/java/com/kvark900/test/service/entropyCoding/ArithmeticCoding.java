package com.kvark900.test.service.entropyCoding;

import com.kvark900.test.service.IOStreamsCloser;
import org.springframework.stereotype.Service;

import java.io.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Created by Keno&Kemo on 31.12.2017..
 */
@Service
public class ArithmeticCoding extends FileData{

    private IOStreamsCloser ioStreamsCloser = new IOStreamsCloser();

    //Algorithm for encoding the file
    public BigDecimal encodeFile(File file) throws FileNotFoundException {
        Scanner scanner = new Scanner(file);
        BigDecimal subIntervalStart = new BigDecimal(0);
        BigDecimal width = new BigDecimal(0);
        int countLines = 0;

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            countLines++;
            for (int i = 0; i < line.length(); i++) {
                Character character = line.charAt(i);
                //For the first character in the file do:
                if(countLines==1 && i==0){
                    for (Map.Entry<Character, List<BigDecimal>> entry : getCharsIntervalsMap(file).entrySet()) {
                        if(entry.getKey().equals(character)){
                            subIntervalStart = subIntervalStart.add(entry.getValue().get(0));
//                            System.out.println("subIntervalStart: "+subIntervalStart);

                        }
                    }
                    for (Map.Entry<Character, BigDecimal> entry : getCharProbabilityMap(file).entrySet()) {
                        if(entry.getKey().equals(character)){
                            width = width.add(entry.getValue());
//                            System.out.println("width: "+width);
                        }
                    }
                }
                //Else:
                else {
                    for (Map.Entry<Character, List<BigDecimal>> entry : getCharsIntervalsMap(file).entrySet()) {
                        if(entry.getKey().equals(character)){
                            subIntervalStart = subIntervalStart.add (entry.getValue().get(0).multiply(width));
//                            System.out.println("subIntervalStart: "+subIntervalStart);
                        }
                    }
                    for (Map.Entry<Character, BigDecimal> entry : getCharProbabilityMap(file).entrySet()) {
                        if(entry.getKey().equals(character)){
                            width = width.multiply(entry.getValue());
//                            System.out.println("width: "+width);
                        }
                    }
                }
            }
        }
        return subIntervalStart.setScale(20, BigDecimal.ROUND_HALF_UP);
    }

    //Write encoded message to the new compressed file
    public void createCompressedFile(File file, BigDecimal bigDecimal){
        FileOutputStream fileOutputStream = null;
        ObjectOutputStream objectOutputStream = null;

        try {
            fileOutputStream = new FileOutputStream(file);
            objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(bigDecimal);

        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            ioStreamsCloser.closeStream(fileOutputStream);
            ioStreamsCloser.closeStream(objectOutputStream);
        }
    }
}
