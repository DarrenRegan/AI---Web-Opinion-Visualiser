package ie.gmit.sw.ai.search;

// Darren Regan - G00326934 - Group C - 4th yr AI Project - https://jsoup.org/cookbook/
import ie.gmit.sw.ai.cloud.WordFrequency;
import ie.gmit.sw.WordScorer;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentSkipListSet;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class NodeParser implements Callable<WordFrequency[]> {
	// Constants
	// MAX_PAGES_VISITED = Num of pages visted
	// TITLE_WEIGHT = If search term is found in the title it will be the num of times found in title Multiple by 50, or TITLE_WEIGHT.
	// HEADING1_WEIGHT = If search tern is found in h1 etc, it will be num of times found multiple by HEADING1_WEIGHT.
	// PARAGRAPH_WEIGHT = If search term is found in body, give it weight of PARAGRAPH_WEIGHT.
	private static final int MAX_PAGES_VISITED = 100;
	private static final int TITLE_WEIGHT = 50; 
	private static final int HEADING_WEIGHT = 20;
	private static final int PARAGRAPH_WEIGHT = 1; 
	private String term;
	private String url;
	int counter = 0;
	int counter1 = 0;
	private Set<String> closed = new ConcurrentSkipListSet<>();
	private Queue<Node> queue = new PriorityQueue<>(Comparator.comparing(Node::getScore));
	WordCounter wordFreq = WordCounter.getInstance();
	
	
	public NodeParser(String url, String searchTerm) {
        this.term = searchTerm;
        this.url = url;
	}
	
	private int getHeuristicScore(Document doc) {
		String title = doc.title();
		String body = doc.body().text();
		
		Elements headings = doc.select("h1, h2, h3");
		
		String heading = "";
		int score = 0;
		int headingScore = 0;
		int bodyScore = 0;
		int titleScore = getFrequency(title, term) * TITLE_WEIGHT;
		
		for(Element h : headings) {
			heading = h.text();
			headingScore += getFrequency(heading, term) * HEADING_WEIGHT;
		}
		
        bodyScore += getFrequency(body, term) * PARAGRAPH_WEIGHT;
        score = Fuzzy.getScore(titleScore, headingScore, bodyScore);
		//System.out.println(closed.size() + "--> " + title);
		//System.out.println("Test all scores: " + " " + titleScore, headingScore, bodyScore, score);
        //System.out.println("Test single score: " + " " + headingScore);
        
		return score;
	}
	
	// https://jsoup.org/cookbook/
	// Start with an URL + Search term connect to it, start to score it, add it to the Closed list, throw it onto the PriorityQueue and start Process()
	// While the queue is empty and the closed list is not over the max pages limit, get the next element from the queue, get its document, get its edges
	// and for each edge get its link, if the link is not null and closed list is not over max pages limit, connect to the link connected to the webpage(child) 
	// and score it and throw it onto the queue
	public void process() {
		int counter2 = 0;
		while(!queue.isEmpty() && closed.size() <= MAX_PAGES_VISITED) {
            Node node = queue.poll();
            Document doc = node.getDocument();
            
            Elements edges = doc.select("a[href]"); // Selects hyperlinks
            for (Element e : edges) {
            	// Grabs URL
            	String link = e.absUrl("href");
            	// Check to see if in closed list
            	if (link != null && closed.size() <=  MAX_PAGES_VISITED || !closed.contains(link) && closed.size() <=  MAX_PAGES_VISITED) {
            		try {
                        //closed.add(link);
            			Document child = Jsoup.connect(link).get();
            			int score = getHeuristicScore(child);
            			if (score > 25) {
            				wordFreq.add(child.body().text());
                            counter++;
                            System.out.println("... " + link + counter);
                            //queue.offer(new Node(child, score));
            			}
					} catch (IOException ex) {
						System.err.println("Error: Could not open link" + ex.getMessage() + " - " + link);
					}            		                     		
            	}// If
            	counter2++;
                if (counter2 >= 100) {
                    break;
                }
            }// For
		}// While
	}// Process()
	
	// Gets the frequency of the search term appearing on page and returns a value to be used in scoring
    public int getFrequency(String s, String target) {
        return (int) Arrays.stream(s.split("[ ,\\.]")).filter(e -> e.equals(target)).count();
    }
    
    @Override
    public WordFrequency[] call() {
        // duckduckgo links
        Document doc;
        int score;

        try {
            doc = Jsoup.connect(url)
                    .timeout(50000)
                    .ignoreHttpErrors(true)
                    .get();
            score = getHeuristicScore(doc);
            closed.add(url);
            queue.offer(new Node(doc, score));
            process();
            Arrays.stream(closed.toArray()).forEach(System.out::println);
        } catch (IOException e) {
            System.err.println("JSoup couldn't connect: " + e.getMessage());
            e.printStackTrace();
        }
        return wordFreq.getFrequency();
    }
	
}
