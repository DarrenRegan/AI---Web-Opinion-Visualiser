package ie.gmit.sw;
//Darren Regan - G00326934 - Group C - 4th yr AI Project
import org.jsoup.nodes.Document;

public class Node {
	
	private final Document document;
    private final int score;

    public Node(Document d, int score) {
        this.document = d;
        this.score = score;
    }

    public Document getDocument() {
        return document;
    }

    public int getScore() {
        return score;
    }
     
}
