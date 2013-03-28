import edu.anno.DefaultConfig;
import edu.anno.MyConfig;


public class MyConfigTest {

	@MyConfig("insertText")
	public void myMethod(){
		
	}
	
	
	@DefaultConfig(item="Figure out the amount of interest per month",
			assignedTo="Brett McLaughlin",
			dateAssigned="08/04/2004")
	public void calculateInterest(float amount, float rate){
		// Need to finish this method later
		
	}
}
