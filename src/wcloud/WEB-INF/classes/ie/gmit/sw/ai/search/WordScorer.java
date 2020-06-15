package ie.gmit.sw.ai.search;
//Darren Regan - G00326934 - Group C - 4th yr AI Project
public enum WordScorer {
	   	URL_TEXT(50),   // Text in url scoring
	    TITLE(30),      // Title scoring
	    HEADER_1(20),   // Text in <h1> tag scoring
	    HEADER_2_3(10), // Text in <h2><h3> tag scoring
	    HEADERS_4_5(5), // Text in <h4><h5> tag scoring
	    HIGHLIGHTED(3), // Bold text, underline etc scoring
	    PARAGRAPH(1),   // Paragraph Scoring
	    ;
	
	int value;
	 
	WordScorer(int value){
	    this.value = value;
	}
	    
    public int getValue(){
        return value;
    }
    
    public int applyAsInt(int i) {
        return i * value;
    }

}
