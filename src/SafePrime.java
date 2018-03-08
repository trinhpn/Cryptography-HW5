import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Random;
public class SafePrime
{
	static BigInteger one = BigInteger.ONE;
	static BigInteger two = new BigInteger("2");
	static int numBit = 320;

	public static void main(String[] args) {
		long start = System.nanoTime();

		ArrayList<BigInteger> arr = safePrime(numBit);
		System.out.println("Running time: " + (System.nanoTime() - start)/1000000000.0);

		System.err.println(arr.get(0));
		System.err.println(arr.get(1));

	}

	private static ArrayList<BigInteger> safePrime(int k) {
		Random rand = new Random();
		BigInteger q = new BigInteger(k-1, 55, rand);
		BigInteger p = q.multiply(two).add(one);
		while (q.compareTo(two.pow(k-2)) == -1 || !p.isProbablePrime(50) ) {
			q = new BigInteger(k, 55, rand);
			p = q.multiply(two).add(one);
		}
		
		ArrayList<BigInteger> arr = new ArrayList<BigInteger>();
		arr.add(p);
		arr.add(q);
		return arr;
	}
}
