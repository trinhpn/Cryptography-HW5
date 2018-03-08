import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

public class Kalai
{
	static BigInteger one = BigInteger.ONE;
	static BigInteger two = new BigInteger("2");
	static BigInteger upperN;
	// Number of bits of N
	static int numBit = 260;
	public static void main(String[] args) throws IOException {
		long start = System.nanoTime();
		DateFormat dateFormat = new SimpleDateFormat("ddMMHHmm");
		Date date = new Date();
		System.out.println(dateFormat.format(date));

		ArrayList<BigInteger> result = sequenceOfRs(two.pow(numBit).subtract(one), two.pow(numBit-1), numBit);
	    int size = result.size();

		// Write result into an output file
		String fileName = "output" + numBit + "bits"+ dateFormat.format(date) +".txt";
	    PrintWriter out = new PrintWriter(new FileWriter(fileName, true), true);
	    out.write("The prime is: " + result.get(size - 1) + " ");
	    for (int i = 0; i < result.size()-1; i++) {
	    	out.write("\nFactor: ");
	    	out.write(result.get(i).toString());
	    }

	    System.out.println("Done finding prime factors of (p-1)");
	    ArrayList<BigInteger> copy = new ArrayList<BigInteger>(result.subList(0, size - 2));
	    BigInteger g = generator(result.get(size-1), copy);
		System.out.println("Generator g = " + g);
	    out.write("\n");
		out.write("Generator g = " + g);
	    out.write("\nRunning time: " + (System.nanoTime() - start)/1000000000 + " seconds.");
	    out.close();
		System.out.println("Running time: " + (System.nanoTime() - start)/1000000000 + " seconds.");

	}
	
	
	private static BigInteger generator(BigInteger p, ArrayList<BigInteger> factors) {
		ArrayList<BigInteger> mArray = new ArrayList<BigInteger>();
		Random rand = new Random();
		BigInteger result = new BigInteger("1");
		// temp = p - 1
		BigInteger temp = p.subtract(one);
		boolean done = false;
		// Create the m's array
	    for(BigInteger factor : factors) {
	    	BigInteger m = temp.divide(factor);
	    	mArray.add(m);

	    }
	    while(!done) {
	    	// Pick a random g
	    	BigInteger g = randomR(p, rand);
	    	if (g.equals(p)) continue;
	    	boolean temp1 = true;
	    	for(BigInteger m : mArray) {
	    		// temp1 = true only if all the g.pow(m) not equal 1
	    		try {
	    			temp1 &= !g.modPow(m, p).equals(one); 
	    		} 
	    		catch (ArithmeticException ex) { // handle overflow when m is too large
	    			if (m.testBit(0)) {
	    				BigInteger m1 = m.divide(two);
		    			temp1 &= !(g.modPow(m1, p).multiply(g.modPow(m1, p))).mod(p).equals(one); 
	    			} else {
	    				BigInteger m1 = m.divide(two);
	    				BigInteger m2 = m.divide(two).add(one);
		    			temp1 &= !(g.modPow(m1, p).multiply(g.modPow(m2, p))).mod(p).equals(one); 
	    			}
	    		}
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
	
	// Return an arraylist in which last element is the prime in given range bits
	// Other elements are prime factors of p - 1
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
					for(BigInteger big : result) {
						System.out.println("Factor: " + big.toString() + " ");
					}
					result.add(n.add(one));
					System.out.println("The prime is: " + n.add(one));
					done = true;
				}
				else { // reset the process
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

