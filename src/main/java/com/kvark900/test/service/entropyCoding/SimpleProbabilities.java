package com.kvark900.test.service.entropyCoding;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Keno&Kemo on 11.01.2018..
 */
@Service
public class SimpleProbabilities {
    //Getting Basic Latin characters
    public List<Character> getBasicLatinCharacters(){
        List<Character> basicLatinChars = new ArrayList<Character>();

        for(int index=32; index<127; index++) {
            basicLatinChars.add((char) index);
        }
        return basicLatinChars;
    }

    //Simple characters map
    public Map<Character, List<BigDecimal>> getCharsSimpleIntervalsMap(){
        List<List<BigDecimal>> intervals = new ArrayList<List<BigDecimal>>();
        BigDecimal characterSimpleProbability = BigDecimal.valueOf(1).
                divide(BigDecimal.valueOf(getBasicLatinCharacters().size()), 1000,
                        BigDecimal.ROUND_HALF_UP);

        Map<Character, List<BigDecimal>> charIntervalsMap = new HashMap<Character, List<BigDecimal>>();
        BigDecimal intervalEndPoint = new BigDecimal(0);


        //Adding intervals
        for (int i = 0; i < getBasicLatinCharacters().size(); i++) {
            if (i == 0) {
                List<BigDecimal> interval = new ArrayList<BigDecimal>();
                interval.add(intervalEndPoint);
                intervalEndPoint = intervalEndPoint.add(characterSimpleProbability);
                interval.add(intervalEndPoint);
                intervals.add(interval);
            } else {
                List<BigDecimal> interval = new ArrayList<BigDecimal>();
                interval.add(intervalEndPoint);
                intervalEndPoint = intervalEndPoint.add(characterSimpleProbability);
                interval.add(intervalEndPoint);
                intervals.add(interval);
            }
        }

        //Populating with characters and intervals charIntervalsMap
        for (int i = 0; i < getBasicLatinCharacters().size(); i++) {
            charIntervalsMap.put(getBasicLatinCharacters().get(i), intervals.get(i));
        }

        return charIntervalsMap;
    }

    public BigDecimal[] getStopCharacterInterval () {
        BigDecimal[] stopCharacterInterval = new BigDecimal[2];

        for (Map.Entry<Character, List<BigDecimal>> entry : getCharsSimpleIntervalsMap().entrySet()) {
            if (entry.getKey().equals('~')) {
                stopCharacterInterval[0] = entry.getValue().get(0);
                stopCharacterInterval[1] = entry.getValue().get(1);
            }
        }
        return stopCharacterInterval;
    }
}
