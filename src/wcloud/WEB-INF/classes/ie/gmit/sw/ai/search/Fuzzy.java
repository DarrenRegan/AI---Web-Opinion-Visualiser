package ie.gmit.sw.ai.search;
//Darren Regan - G00326934 - Group C - 4th yr AI Project
import net.sourceforge.jFuzzyLogic.FIS;
import net.sourceforge.jFuzzyLogic.FunctionBlock;

public class Fuzzy{
	
	  public static int getScore(int title, int heading, int body) {
	        FIS fis = FIS.load("score.fcl", true);
	        FunctionBlock fb = fis.getFunctionBlock("score");

	        fis.setVariable("title", title);
	        fis.setVariable("heading", heading);
	        fis.setVariable("body", body);

	        fis.evaluate();
	        return (int) fis.getVariable("score").defuzzify();
	    }
	  
	    public static void main(String[] args) {
	        new Fuzzy();
			System.out.println("Scoring: " + Fuzzy.getScore(100, 60, 500));
	    }
}
