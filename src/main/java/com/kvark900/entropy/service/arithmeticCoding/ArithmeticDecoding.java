package com.kvark900.entropy.service.arithmeticCoding;

import com.kvark900.entropy.service.Interval;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;

import java.io.*;
import java.math.BigDecimal;
import java.util.Map;

/**
 * Created by Keno&Kemo on 02.01.2018..*/

@Service
public class ArithmeticDecoding extends SimpleProbabilities {

    public ArithmeticDecoding() {
    }

    public void decodeFile (File fileToDecode) throws IOException {
        BigDecimal messageToDecode = getEncodedMessage(fileToDecode);
        String fileName = FilenameUtils.getBaseName(fileToDecode.getName());
        Interval stopCharacterInterval = getStopCharacterInterval();
        Interval newLineCharacter = getNewLineCharacterInterval();
        PrintWriter writer = new PrintWriter(fileName + ".txt");

        while (messageToDecode.compareTo(stopCharacterInterval.getLowerBound()) < 0) {
            for (Map.Entry<Character, Interval> entry : getCharsIntervalsMap().entrySet()) {
                if (stopCharacterInterval.containsWithinBoundaries(messageToDecode))
                    break;

                else if (newLineCharacter.containsWithinBoundaries(messageToDecode)) {
                    writer.println("");
                    BigDecimal subtractMessage = messageToDecode.subtract(newLineCharacter.getLowerBound());
                    BigDecimal subtractIntervals = newLineCharacter.getUpperBound().subtract(newLineCharacter.getLowerBound());
                    messageToDecode = subtractMessage.divide(subtractIntervals, 1000, BigDecimal.ROUND_HALF_UP);
                }
                else if (messageToDecode.compareTo(entry.getValue().getLowerBound()) >= 0 &&
                        messageToDecode.compareTo(entry.getValue().getUpperBound()) <= 0) {
                    writer.print(entry.getKey());
                    BigDecimal subtractMessage = messageToDecode.subtract(entry.getValue().getLowerBound());
                    BigDecimal subtractIntervals = entry.getValue().getUpperBound().subtract(entry.getValue().getLowerBound());
                    messageToDecode = subtractMessage.divide(subtractIntervals, 1000, BigDecimal.ROUND_HALF_UP);
                }
            }
        }
        writer.flush();
        writer.close();
    }

    private BigDecimal getEncodedMessage(File file){
        BigDecimal messageToDecode = null;

        try (FileInputStream fileInputStream = new FileInputStream(file);
             ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream))
        {
            messageToDecode = (BigDecimal) objectInputStream.readObject();

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return messageToDecode;
    }
}
