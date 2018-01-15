package com.kvark900.test.service.entropyCoding;

import com.kvark900.test.service.IOStreamsCloser;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;

import java.io.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Created by Keno&Kemo on 02.01.2018..*/

@Service
public class ArithmeticDecoding extends SimpleProbabilities {

    FileData fileData = new FileData();
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

    public void decodeFile (File fileToDecode) throws FileNotFoundException, UnsupportedEncodingException {
        BigDecimal messageToDecode = getEncodedMessage(fileToDecode);
        String fileName = FilenameUtils.getBaseName(fileToDecode.getName());
        BigDecimal stopCharacterInterval[] = getStopCharacterInterval();
        BigDecimal newLineCharacter[] = getNewLineCharacterInterval();
        PrintWriter writer = new PrintWriter(fileName + ".txt", "UTF-8");

        while (messageToDecode.compareTo(stopCharacterInterval[0])<0){
            for (Map.Entry<Character, List<BigDecimal>> entry : getCharsSimpleIntervalsMap().entrySet()) {
                if (messageToDecode.compareTo(stopCharacterInterval[0])>0 &&
                        messageToDecode.compareTo(stopCharacterInterval[1])<0) {
                    break;
                }
                else if (messageToDecode.compareTo(newLineCharacter[0])>0 &&
                        messageToDecode.compareTo(newLineCharacter[1])<0) {
                    writer.println("");
                    BigDecimal subtractMessage = messageToDecode.subtract(newLineCharacter[0]);
                    BigDecimal subtractIntervals = newLineCharacter[1].subtract(newLineCharacter[0]);
                    messageToDecode = subtractMessage.divide(subtractIntervals, 1000, BigDecimal.ROUND_HALF_UP);
                }
                else if(messageToDecode.compareTo(entry.getValue().get(0))>=0 &&
                        messageToDecode.compareTo(entry.getValue().get(1))<=0){
                    writer.print(entry.getKey());
                    BigDecimal subtractMessage = messageToDecode.subtract(entry.getValue().get(0));
                    BigDecimal subtractIntervals = entry.getValue().get(1).subtract(entry.getValue().get(0));
                    messageToDecode = subtractMessage.divide(subtractIntervals, 1000, BigDecimal.ROUND_HALF_UP);
                }
            }
        }
        writer.close();
    }
}
