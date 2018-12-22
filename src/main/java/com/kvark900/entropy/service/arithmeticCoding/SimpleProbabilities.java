package com.kvark900.entropy.service.arithmeticCoding;

import com.kvark900.entropy.Interval;
import com.kvark900.entropy.SpecialCharacters;
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

    List<Character> getBasicLatinCharacters() {
        List<Character> basicLatinChars = new ArrayList<>();
        for (int index = 32; index < 127; index++)
            basicLatinChars.add((char) index);
        basicLatinChars.add(SpecialCharacters.NEW_LINE_CHARACTER);
        basicLatinChars.add(SpecialCharacters.STOP_CHARACTER);
        return basicLatinChars;
    }

    Map<Character, Interval> getCharsIntervalsMap() {
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

        if (getCharsIntervalsMap().containsKey(SpecialCharacters.STOP_CHARACTER)) {
            stopCharacterInterval.setLowerBound(getCharsIntervalsMap().get(SpecialCharacters.STOP_CHARACTER).getLowerBound());
            stopCharacterInterval.setUpperBound(getCharsIntervalsMap().get(SpecialCharacters.STOP_CHARACTER).getUpperBound());
        }

        return stopCharacterInterval;
    }

    Interval getNewLineCharacterInterval() {
        Interval newLineCharacterInterval = new Interval();

        if (getCharsIntervalsMap().containsKey(SpecialCharacters.NEW_LINE_CHARACTER)) {
            newLineCharacterInterval.setLowerBound(getCharsIntervalsMap().get(SpecialCharacters.NEW_LINE_CHARACTER).getLowerBound());
            newLineCharacterInterval.setUpperBound(getCharsIntervalsMap().get(SpecialCharacters.NEW_LINE_CHARACTER).getUpperBound());
        }

        return newLineCharacterInterval;
    }
}
