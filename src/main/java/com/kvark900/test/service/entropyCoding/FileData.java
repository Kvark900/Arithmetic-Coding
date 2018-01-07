package com.kvark900.test.service.entropyCoding;

import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created by Keno&Kemo on 31.12.2017..
 */
//Class for analysing file data, getting all characters, characters probabilities of
// occurrences, characters intervals
@Service
public class FileData {



    public int countNumberOfAllCharacters(File file) throws FileNotFoundException {
        Scanner scanner;
        int numberOfAllCharacters = 0;
        scanner = new Scanner(file);
        while (scanner.hasNextLine()){
            String line = scanner.nextLine();
            numberOfAllCharacters += line.length();
        }
        return numberOfAllCharacters;
    }

    public  List<Character> getListOfCharacters(File file) throws FileNotFoundException {
        Scanner scanner = new Scanner(file);
        List<Character> chars = new ArrayList<Character>();
        while (scanner.hasNextLine()){
            String line = scanner.nextLine();
            for (int i = 0; i<line.length(); i++){
                if (!chars.contains(line.charAt(i)))
                {
                    chars.add(line.charAt(i));
                }
            }
        }
        return chars;
    }

    public  int countCharOccurrences(File file, char symbol) throws FileNotFoundException {
        Scanner scanner = new Scanner(file);
        int count = 0;
        while (scanner.hasNextLine()){
            String line = scanner.nextLine();
            for (int i = 0; i<line.length(); i++){
                if (line.charAt(i) == symbol) {
                    count++;}
            }
        }
        return count;
    }

    public  BigDecimal countCharProbability(File file, char symbol) throws FileNotFoundException {

        return BigDecimal.valueOf(countCharOccurrences(file, symbol)).divide(
                BigDecimal.valueOf(countNumberOfAllCharacters(file)), 20, BigDecimal.ROUND_HALF_UP);
    }

    public  Map<Character, Integer> getCharOccurrencesMap(File file) throws FileNotFoundException {
        List<Character> charactersList = getListOfCharacters(file);
        Map<Character, Integer> characterOccurrences= new HashMap<Character, Integer>();

        for(char character : charactersList){
            characterOccurrences.put(character, countCharOccurrences(file, character));
        }
        return characterOccurrences;
    }

    public  Map<Character, BigDecimal> getCharProbabilityMap(File file) throws FileNotFoundException {
        List<Character> charactersList = getListOfCharacters(file);
        Map<Character, BigDecimal> characterProbability= new HashMap<Character, BigDecimal>();
        for(char character : charactersList){
            characterProbability.put(character, countCharProbability(file, character));
        }
        return characterProbability;

    }

    public  BigDecimal getSumOfProbabilities(File file) throws FileNotFoundException {
        BigDecimal bigDecimal = new BigDecimal(0);
        for(char c : getListOfCharacters(file)){
            bigDecimal = bigDecimal.add(countCharProbability(file, c));
        }
        return bigDecimal;
    }


    public Map<Character, List<BigDecimal>> getCharsIntervalsMap(File file) throws FileNotFoundException {
        List<Character> characters = new ArrayList<Character>();
        List<BigDecimal> probabilities = new ArrayList<BigDecimal>();
        List<List<BigDecimal>> intervals = new ArrayList<List<BigDecimal>>();

        Map<Character, List<BigDecimal>> charIntervalsMap = new HashMap<Character, List<BigDecimal>>();
        BigDecimal intervalEndPoint = new BigDecimal(0);

        //Adding characters and probabilities
        for (Map.Entry<Character, BigDecimal> entry : getCharProbabilityMap(file).entrySet()) {
            characters.add(entry.getKey());
            probabilities.add(entry.getValue());
        }

        //Adding intervals
        for (int i = 0; i < probabilities.size(); i++) {
            if (i == 0) {
                List<BigDecimal> interval = new ArrayList<BigDecimal>();
                interval.add(intervalEndPoint);
                intervalEndPoint = intervalEndPoint.add(probabilities.get(0));
                interval.add(intervalEndPoint);
                intervals.add(interval);
            } else {
                List<BigDecimal> interval = new ArrayList<BigDecimal>();
                interval.add(intervalEndPoint);
                intervalEndPoint = intervalEndPoint.add(probabilities.get(i));
                interval.add(intervalEndPoint);
                intervals.add(interval);
            }
        }

        //Populating with characters and intervals charIntervalsMap
        for (int i = 0; i < probabilities.size(); i++) {
            charIntervalsMap.put(characters.get(i), intervals.get(i));
        }

        return charIntervalsMap;
    }

}
