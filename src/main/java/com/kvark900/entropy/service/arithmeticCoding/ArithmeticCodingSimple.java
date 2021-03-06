package com.kvark900.entropy.service.arithmeticCoding;

import org.springframework.stereotype.Service;

import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Scanner;

/**
 * Created by Keno&Kemo on 11.01.2018..
 */
@Service
public class ArithmeticCodingSimple extends SimpleProbabilities {

    public BigDecimal encodeWithSimpleProbabilities(File file) throws FileNotFoundException {
        Scanner scanner = new Scanner(file);
        BigDecimal subIntervalStart = new BigDecimal(0);
        BigDecimal width = new BigDecimal(0);
        int lineCounter = 0;
        BigDecimal simpleProbability = BigDecimal.ONE.divide(new BigDecimal(getBasicLatinCharacters().size()),
                1000, RoundingMode.HALF_UP);

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            lineCounter++;
            for (int charPosition = 0; charPosition < line.length(); charPosition++) {
                char character = line.charAt(charPosition);
                if (lineCounter == 1 && charPosition == 0) {
                    if (getCharsIntervalsMap().containsKey(character)) {
                        subIntervalStart = subIntervalStart.add(getCharsIntervalsMap().get(character).getLowerBound());
                    }
                    width = width.add(simpleProbability);
                } else {
                    if (getCharsIntervalsMap().containsKey(character)) {
                        subIntervalStart = subIntervalStart.add(getCharsIntervalsMap().get(character).getLowerBound().multiply(width));
                    }
                    width = width.multiply(simpleProbability);
                }
            }
        }
        scanner.close();
        return subIntervalStart = subIntervalStart.setScale(1000, RoundingMode.HALF_UP);
    }

    public void createCompressedFile(File file, BigDecimal bigDecimal) {
        try (FileOutputStream fileOutputStream = new FileOutputStream(file);
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream)) {
            objectOutputStream.writeObject(bigDecimal);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
