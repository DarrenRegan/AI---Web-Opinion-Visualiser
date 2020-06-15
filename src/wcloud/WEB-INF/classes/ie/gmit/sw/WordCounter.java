package ie.gmit.sw;
//Darren Regan - G00326934 - Group C - 4th yr AI Project
import ie.gmit.sw.ai.cloud.WordFrequency;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.io.InputStreamReader;

public class WordCounter {
    private static final int MAX = 32;
    private static Map<String, Integer> freqMap = new ConcurrentHashMap<>();
    private static WordFrequency[] wordCount = new WordFrequency[MAX];
    private static WordCounter instance = new WordCounter();
    int mapCounter = 0;

    private WordCounter() {
    }

    public static WordCounter getInstance(){
        return instance;
    }
    
    public WordFrequency[] getFrequency(){
        List<WordFrequency> wcArrayList = new ArrayList<>();

        freqMap.entrySet().forEach(entry -> {
        	wcArrayList.add(new WordFrequency(entry.getKey(), entry.getValue()));
        });
        Collections.sort(wcArrayList);

        for (int i = 0; i < wordCount.length; i++) {
        	wordCount[i] = wcArrayList.get(i);
        }
        return wordCount;
    }// getFrequency()
    
    // Read text from URL and adds to map, returns num of pages and prune + sort map
    public void add(String text) throws IOException {
        String[] wordsToIgnore = FileParser.getIgnoreWords().toArray(new String[0]);
        BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(text.toLowerCase().getBytes(StandardCharsets.UTF_8))));
        String line;

        while ((line = reader.readLine()) != null) {
            String[] words = line.split("[^a-z]");
            for (String word : words) {
                if (word.isEmpty()) {
                    continue;
                }

                if (Arrays.asList(wordsToIgnore).contains(word.toLowerCase()) || word.length() < 4) {
                } else {
                    synchronized (freqMap) {
                        if (freqMap.containsKey(word)) {
                            int count = freqMap.get(word);
                            count++;
                            freqMap.put(word, count);
                        } else {
                        	freqMap.put(word, 1);
                        }
                    }
                }
            }
        }
        mapCounter++;
		System.out.println("Number of Pages: " + mapCounter);
		reader.close();
		
		List<Map.Entry<String, Integer>> list  = new LinkedList<>(freqMap.entrySet());
		Collections.sort(list, (f, s) -> s.getValue() - f.getValue());
		
		list.stream()
		        .skip(100)
		        .map(Map.Entry::getKey)
		        .forEach(freqMap::remove);
    }//add         
}
