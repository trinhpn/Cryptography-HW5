import java.math.BigInteger;
import java.util.Random;

public class Test
{	
	static BigInteger one = BigInteger.ONE;
	static BigInteger two = new BigInteger("2");
	
	public static void main(String[] args) {
		Random rand = new Random();
		BigInteger N = two.pow(7);
		System.out.print(N.bitLength());
		
	/*	do {
			r = randomR(N, rand);
			N = r;
			System.out.print(r + " ");
		} while (!r.equals(one));
	*/
	
	}
	
	
	
		

	
	private static BigInteger randomR(BigInteger upper, Random rand) {
		int bitLen = upper.bitLength();
		BigInteger r;
		// Keep generating till r is less than or equal upper
		do {
			r = new BigInteger(bitLen, rand);
		} while (r.compareTo(upper) == 1); 
		return r;
	}

}
