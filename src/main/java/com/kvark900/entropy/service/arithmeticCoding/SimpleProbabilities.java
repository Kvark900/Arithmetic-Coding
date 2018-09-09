package com.kvark900.entropy.service.arithmeticCoding;

import com.kvark900.entropy.service.Interval;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Keno&Kemo on 11.01.2018..
 */
@Service
public class SimpleProbabilities {
    List<Character> getBasicLatinCharacters(){
        List<Character> basicLatinChars = new ArrayList<>();
        for(int index=32; index<127; index++) basicLatinChars.add((char) index);
        return basicLatinChars;
    }

    Map<Character, Interval> getCharsIntervalsMap(){
        List<Interval> intervals = new ArrayList<>();
        Map<Character, Interval> charIntervalsMap = new HashMap<>();
        BigDecimal intervalEndPoint = new BigDecimal(0);

        BigDecimal characterSimpleProbability = BigDecimal.ONE.divide(new BigDecimal(getBasicLatinCharacters().size()),
                1000, RoundingMode.HALF_UP);

        //Adding intervals
        for (int i = 0; i < getBasicLatinCharacters().size(); i++) {
            Interval interval = new Interval();
            interval.setLowerBound(intervalEndPoint);
            intervalEndPoint = intervalEndPoint.add(characterSimpleProbability);
            interval.setUpperBound(intervalEndPoint);
            intervals.add(interval);
            charIntervalsMap.put(getBasicLatinCharacters().get(i), intervals.get(i));
        }

        return charIntervalsMap;
    }

    Interval getStopCharacterInterval() {
        Interval stopCharacterInterval = new Interval();

        for (Map.Entry<Character, Interval> entry : getCharsIntervalsMap().entrySet()) {
            if (entry.getKey().equals('~')) {
                stopCharacterInterval.setLowerBound(entry.getValue().getLowerBound());
                stopCharacterInterval.setUpperBound(entry.getValue().getUpperBound());
            }
        }
        return stopCharacterInterval;
    }

    Interval getNewLineCharacterInterval() {
        Interval newLineCharacterInterval = new Interval();

        for (Map.Entry<Character, Interval> entry : getCharsIntervalsMap().entrySet()) {
            if (entry.getKey().equals('|')) {
                newLineCharacterInterval.setLowerBound(entry.getValue().getLowerBound());
                newLineCharacterInterval.setUpperBound(entry.getValue().getUpperBound());
            }
        }
        return newLineCharacterInterval;
    }
}
