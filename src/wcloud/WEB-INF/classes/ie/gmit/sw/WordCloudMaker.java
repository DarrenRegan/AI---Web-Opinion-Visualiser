package ie.gmit.sw;
//Darren Regan - G00326934 - Group C - 4th yr AI Project
import ie.gmit.sw.ai.cloud.LogarithmicSpiralPlacer;
import ie.gmit.sw.ai.cloud.WeightedFont;
import ie.gmit.sw.ai.cloud.WordFrequency;
import ie.gmit.sw.ai.cloud.CollisionDetector;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.function.BinaryOperator;
/*
 * This class creates the word cloud and word cloud pictures
 * */
public class WordCloudMaker {
    // Display WORD_CLOUD_WORDS in word cloud
    private static final int WORD_CLOUD_WORDS = 70;
    // Create image every IMAGE_EVERY words
    private static final int IMAGE_EVERY = 200;
    // Prune words buffer queue to PRUNE_KEEP
    private static final int PRUNE_KEEP = 500;

    public static AtomicInteger linkCounter = new AtomicInteger();
    int count;
    private String query;
    private ExecutorService es = Executors.newWorkStealingPool();
    private List<Future<WordFrequency[]>> wfList = new LinkedList<>();
    
    public WordCloudMaker(String query) {
        this.query = query;
    }
       
    public static void main(String[] args)  {
        String query = args[0];
        WordCloudMaker cloud = new WordCloudMaker(query);
        WordFrequency[] words = new WordFrequency[0];
        try {       
            words = cloud.createWordCloud();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        Arrays.stream(words).forEach(System.out::println);
    }
    
    public WordFrequency[] createWordCloud() throws IOException, InterruptedException {
        DuckDuckGoSearch search = new DuckDuckGoSearch();
        WordFrequency[] words;

        //Set<String> dummylinks = search.dummy(query);
        Set<String> links = search.getSearchResults(query);
        count = links.size();
        for (String link : links) {
            NodeParser nodeParser = new NodeParser(link, query);
            Future<WordFrequency[]> wf = es.submit(nodeParser);
            wfList.add(wf);
        }

        WordCounter wordFrequencyCounter = WordCounter.getInstance();

        while (!wfList.isEmpty()) {
        	wfList.removeIf(this::isDone);
            Thread.sleep(10);
        }
        words = wordFrequencyCounter.getFrequency();
        words = new WeightedFont().getFontSizes(words);
        Arrays.sort(words, Comparator.comparing(WordFrequency::getFrequency, Comparator.reverseOrder()));
        return words;
    }
    
    private boolean isDone(Future<WordFrequency[]> future) {
    	//Returns true if this task completed
        if (future.isDone()) {
            count--;
            System.out.println("--- " + count);
            return true;
        }
        return false;
    }
    
    // Encode image string
    private String encodeToString(BufferedImage image) {
        String s = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        try {
            ImageIO.write(image, "png", bos);
            byte[] bytes = bos.toByteArray();

            Base64.Encoder encoder = Base64.getEncoder();
            s = encoder.encodeToString(bytes);
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return s;
    }
   
    // decode image string to picture
    private BufferedImage decodeToImage(String imageString) {
        BufferedImage image = null;
        byte[] bytes;
        try {
            Base64.Decoder decoder = Base64.getDecoder();
            bytes = decoder.decode(imageString);
            ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
            image = ImageIO.read(bis);
            bis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return image;
    }
}
