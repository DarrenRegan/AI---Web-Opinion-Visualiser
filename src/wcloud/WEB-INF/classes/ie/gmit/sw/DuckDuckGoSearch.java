package ie.gmit.sw;
//Darren Regan - G00326934 - Group C - 4th yr AI Project
import java.io.IOException;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class DuckDuckGoSearch {
    private final static String DUCK_URL = "https://duckduckgo.com/html/?q=";
    private final static int MAX = 60;
    private Set<String> urls = new ConcurrentSkipListSet<>();
    
    //https://medium.com/@sethsubr/fetch-duckduckgo-web-search-results-in-20-lines-of-java-code-3a34ea9da085
    public Set<String> getSearchResults(String query) throws IOException {
        Document doc;
        try {
        	//Connect to duckduckgo url, add the query to the end following the ?q=, for example https://duckduckgo.com/html/?q=Galway
            doc = Jsoup.connect(DUCK_URL + query)
                    .timeout(15000)
                    .ignoreHttpErrors(true)
                    .get();
            //Fetch links
            Elements results = doc.getElementById("links").getElementsByClass("results_links");
            
            for (Element result : results) {
                Element e = result.getElementsByClass("links_main").first().getElementsByTag("a").first();
                //System.out.println("\nURL:" + e.attr("href"));
                //System.out.println("Title:" + e.text());
                //System.out.println("Snippet:" + result.getElementsByClass("result__snippet").first().text());
                String link = e.absUrl("href");
                if (link != null && urls.size() <= MAX && !urls.contains(link)) {
                	urls.add(link);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return urls;
    }
    
    // dummy for testing before doing a duckduckgo search query
    public Set<String> dummy(String query) {
    	urls.add("https://en.wikipedia.org/wiki/Galway");
    	urls.add("https://galway2020.ie/en/");
    	urls.add("https://www.galwaycity.ie");
    	urls.add("https://www.galwayraces.com/");
    	urls.add("https://www.irishtimes.com/topics/galway");
    	urls.add("https://www.galwaygaa.ie/");
    	urls.add("https://wikitravel.org/en/Galway");
    	
        return urls;
    }
    
  /*  public static void main(String[] args) {
    	  String query = "Galway";
    	  getSearchResults(query);
    	 }*/
}
