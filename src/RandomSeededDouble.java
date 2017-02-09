import java.util.*;

public class RandomSeededDouble {
	
	private Random generator; 
	public RandomSeededDouble(long seed){
		generator = new Random(seed);
	}
	
	public double generateDouble(){
		return generator.nextDouble();
	}
}