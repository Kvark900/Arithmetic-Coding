package com.kvark900.entropy.service.arithmeticCoding;

import com.kvark900.entropy.service.IOStreamsCloser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Created by Keno&Kemo on 11.01.2018..
 */
@Service
public class ArithmeticCodingSimple extends SimpleProbabilities {

    private IOStreamsCloser ioStreamsCloser;

    public ArithmeticCodingSimple() {

    }

    @Autowired
    public ArithmeticCodingSimple(IOStreamsCloser ioStreamsCloser) {
        this.ioStreamsCloser = ioStreamsCloser;
    }



    //Algorithm for encoding the file
    public BigDecimal encodeWithSimpleProbabilities(File file) throws FileNotFoundException {
        Scanner scanner = new Scanner(file);
        BigDecimal subIntervalStart = new BigDecimal(0);
        BigDecimal width = new BigDecimal(0);
        int countLines = 0;
        BigDecimal simpleProbability = BigDecimal.ONE.divide(new BigDecimal(getBasicLatinCharacters().size()),
                                        1000, BigDecimal.ROUND_HALF_UP);

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            countLines++;
            for (int i = 0; i < line.length(); i++) {
                Character character = line.charAt(i);
                //For the first character in the file do:
                if(countLines==1 && i==0){
                    //Start of the sub-interval
                    for (Map.Entry<Character, List<BigDecimal>> entry : getCharsSimpleIntervalsMap().entrySet()) {
                        if(entry.getKey().equals(character)){
                            subIntervalStart = subIntervalStart.add(entry.getValue().get(0));
//                            System.out.println("subIntervalStart: "+subIntervalStart);
                        }
                    }

                    //Width of the sub-interval
                    width = width.add(simpleProbability);
//                   System.out.println("width: "+width);
                }
                //Else:
                else {
                    //Start of the sub-interval
                    for (Map.Entry<Character, List<BigDecimal>> entry : getCharsSimpleIntervalsMap().entrySet()) {
                        if(entry.getKey().equals(character)){
                            subIntervalStart = subIntervalStart.add (entry.getValue().get(0).multiply(width));
//                            System.out.println("subIntervalStart: "+subIntervalStart);
                        }
                    }

                    //Width of the sub-interval
                    width = width.multiply(simpleProbability);
//                  System.out.println("width: "+width);
                }

            }
        }
        scanner.close();
        return subIntervalStart = subIntervalStart.setScale(1000, BigDecimal.ROUND_HALF_UP);
    }

    @SuppressWarnings("Duplicates")
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
