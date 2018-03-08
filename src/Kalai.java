import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Kalai
{
	static BigInteger one = BigInteger.ONE;
	static BigInteger two = new BigInteger("2");
	static BigInteger upperN;
	// Number of bits of N
	static int numBit = 150;
	public static void main(String[] args) throws IOException {
		long start = System.nanoTime();
		DateFormat dateFormat = new SimpleDateFormat("HHmmss");
		Date date = new Date();
		System.out.println(dateFormat.format(date));

		ArrayList<BigInteger> result = sequenceOfRs(two.pow(numBit).subtract(one), two.pow(numBit-1), numBit);
		System.out.println("Running time: " + (System.nanoTime() - start)/1000000000.0);
	    int size = result.size();

		// Write result into an output file
		String fileName = "output" + numBit + "bits"+ dateFormat.format(date) +".txt";
	    PrintWriter out = new PrintWriter(new FileWriter(fileName, true), true);
	    out.write("The prime is: " + result.get(size - 1).add(one) + " ");
	    out.write("Running time: " + (System.nanoTime() - start)/1000000000.0 + " seconds.");
	    for(BigInteger big : result) {
	    	out.write("\n ");
	    	out.write(big.toString());
		}

	    System.out.println("Done finding prime factors of (p-1)");
	    ArrayList<BigInteger> copy = new ArrayList<BigInteger>(result.subList(0, size - 2));
	    BigInteger g = generator(result.get(size-1).add(one), copy);
		System.out.println("Generator g = " + g);
	    out.write("\n");
		out.write("Generator g = " + g);
	    out.close();

	}
	
	
	private static BigInteger generator(BigInteger p, ArrayList<BigInteger> factors) {
		ArrayList<Integer> mArray = new ArrayList<Integer>();
		Random rand = new Random();
		BigInteger result = new BigInteger("1");
		// temp = p - 1
		BigInteger temp = p.subtract(one);
		boolean done = false;
		// Create the m's array
	    for(BigInteger factor : factors) {
	    	int m = temp.divide(factor).intValue();
	    	mArray.add(m);

	    }
	    while(!done) {
	    	// Pick a random g
	    	BigInteger g = randomR(p, rand);
	    	if (g.equals(p)) continue;
	    	boolean temp1 = true;
	    	try {
		    	for(Integer m : mArray) {
		    		// temp1 = true only if all the g.pow(m) not equal 1
		    		temp1 &= !g.pow(m).mod(p).equals(one); 
		    	}
	    	} catch (ArithmeticException ex) {
	    		temp1 = false;
	    		break;
	    	}
	    	// Temp1 true means we found generator, stop while loop 
	    	done = temp1;
	    	// record the g generator
	    	result = g.add(BigInteger.ZERO);
	    } 
	    
	    return result;
	}
	// Method to generate a random BigInteger which is less than the given upper
	private static BigInteger randomR(BigInteger upper, Random rand) {
		int bitLen = upper.bitLength();
		BigInteger r;
		// Keep generating till r is less than or equal upper
		do {
			r = new BigInteger(bitLen, rand);
		} while (r.compareTo(upper) == 1 || r.equals(BigInteger.ZERO));
		return r;
	}
	private static ArrayList<BigInteger> sequenceOfRs(BigInteger upperN, BigInteger lowerN, int bitLen) {
		ArrayList<BigInteger> result = new ArrayList<>();
		Kalai.upperN = upperN.add(BigInteger.ZERO);
		Random rand = new Random();
		// The n holds the prime's product
		BigInteger n = new BigInteger("1");
		// The random m to check on randomness
		BigInteger m = new BigInteger(bitLen, rand);
		boolean done = false;

		while(!done) {
			BigInteger r = randomR(Kalai.upperN, rand);
			Kalai.upperN = r.add(BigInteger.ZERO);
			if (r.isProbablePrime(40) || r.equals(one)) {
				n = n.multiply(r);
				// add the r into array
				if (!r.equals(one)) result.add(r);	
			}
			if (r.equals(one) ){
				if (n.compareTo(lowerN) > -1 && n.compareTo(upperN) < 1 && n.compareTo(m) >= 0 && n.add(one).isProbablePrime(55)) {
					System.out.println("\nDone");
					result.add(n);
					for(BigInteger big : result) {
						System.err.println(big.toString() + " ");
					}
					done = true;
				}
				else {
					Kalai.upperN = upperN.add(BigInteger.ZERO);
					m = new BigInteger(bitLen, rand);
					n = new BigInteger("1");
					result.clear();
				}
			}
		}

		return result;
	}
	
	
	
}

