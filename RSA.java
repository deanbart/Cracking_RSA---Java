
import java.math.BigInteger;
import java.security.SecureRandom;

public class RSA {
	// Initialize BigIntegers for Factorization
	private final static BigInteger ZERO = new BigInteger("0");
	private final static BigInteger ONE = new BigInteger("1");
	private final static BigInteger TWO = new BigInteger("2");
	private final static SecureRandom random = new SecureRandom();
	private static final Object NULL = null;

	/*
	 * This code was extracted from Github, which was created by 'Odzhan' (2019)
	 * https://github.com/odzhan/tinycrypt/blob/master/tbnlib/binmath/utils/
	 * PollardRho.java
	 */
	public static BigInteger factor(BigInteger N) {
		if (N.compareTo(ONE) == 0)
			return ZERO;
		if (N.isProbablePrime(20)) {
			return N;
		}
		BigInteger divisor = rho(N);
		factor(divisor);
		return factor(N.divide(divisor));
	}

	/*
	 * This code was extracted fromGithub, which was created by 'Odzhan'
	 * (2019)https://github.com/odzhan/tinycrypt/blob/master/tbnlib/binmath/utils/
	 * PollardRho.java
	 */
	public static BigInteger rho(BigInteger N) {
		BigInteger divisor;
		BigInteger c = new BigInteger(N.bitLength(), random);
		BigInteger x = new BigInteger(N.bitLength(), random);
		BigInteger xx = x;

		if (N.mod(TWO).compareTo(ZERO) == 0)
			return TWO;

		do { // Floyd's cycle finding algorithm
			x = x.multiply(x).mod(N).add(c).mod(N); // Tortoise move
			xx = xx.multiply(xx).mod(N).add(c).mod(N); // Hare move
			xx = xx.multiply(xx).mod(N).add(c).mod(N);
			divisor = x.subtract(xx).gcd(N);
		} while ((divisor.compareTo(ONE)) == 0);
		return divisor;
	}

	public static void main(String[] args) throws Exception {
		long startTime = System.nanoTime();
		// ACTUAL DATA
		BigInteger alicemodulus = new BigInteger("107182711767121947041078387099");
		BigInteger bobmodulus = new BigInteger("82854283828672072885142508889");

		BigInteger alicepub = new BigInteger("71745641618727161555775583937");
		BigInteger bobpub = new BigInteger("41633641303373798990175324983");

		String cipherText = "3epzsv0kmi6mb4sl2xs";

		// TEST DATA
		/*
		 * BigInteger alicemodulus = new BigInteger("66056083785421544972111685239");
		 * BigInteger bobmodulus = new BigInteger("71994651332404115788173195239");
		 * 
		 * BigInteger alicepub = new BigInteger("38933338385103628492607145193");
		 * BigInteger bobpub = new BigInteger("28763302913765661132800185637");
		 * 
		 * String cipherText = "5b8sot9g2168mp3nw51";
		 */
		// Calc Q - applying the factoring algorithm
		BigInteger aliceQ = factor(alicemodulus);
		BigInteger bobQ = factor(bobmodulus);
		System.out.println("aliceQ " + aliceQ);
		// Calc P -> p = n/q
		BigInteger aliceP = alicemodulus.divide(aliceQ);
		BigInteger bobP = bobmodulus.divide(bobQ);
		System.out.println("aliceP" + aliceP);
		// Used to calc r
		BigInteger one = new BigInteger("1");

		// Calc p-1
		BigInteger p = aliceP.subtract(one);
		BigInteger p1 = bobP.subtract(one);
		// Calc q-1
		BigInteger q = aliceQ.subtract(one);
		BigInteger q1 = bobQ.subtract(one);
		// r or Phi = (p-1)*(q-1)
		BigInteger alicetotient = p.multiply(q);
		BigInteger bobtotient = p1.multiply(q1);
		System.out.println("alicetotient " + alicetotient);
		// Calc private key -> de = mod phi -> d = e^-1 mod phi
		BigInteger aliceprivate = alicepub.modInverse(alicetotient);
		BigInteger bobprivate = bobpub.modInverse(bobtotient);
		System.out.println("aliceprivate" + aliceprivate);
		// Decrypt cipher text -> m = c^d mod n
		BigInteger cipher = encode(cipherText);
		System.out.println("cipher " + cipher);
		// Perform modular exponentiation
		BigInteger decrypt = cipher.modPow(bobprivate, bobmodulus);
		System.out.println("decrypt " + decrypt);
		BigInteger removesignature = decrypt.modPow(alicepub, alicemodulus);
		System.out.println("removesignature " + removesignature);
		String message1 = decode(removesignature);

		System.out.println("Alice:");
		System.out.println("p: " + aliceP);
		System.out.println("q: " + aliceQ);
		System.out.println("r: " + alicetotient);
		System.out.println("modulus: " + alicemodulus);
		System.out.println("privateKey: " + aliceprivate);
		System.out.println("publicKey: " + alicepub);

		System.out.println("\nBob:");
		System.out.println("p: " + bobP);
		System.out.println("q: " + bobQ);
		System.out.println("r: " + bobtotient);
		System.out.println("modulus: " + bobmodulus);
		System.out.println("privateKey: " + bobprivate);
		System.out.println("publicKey: " + bobpub);

		// System.out.println("\nplainText: " + message);
		System.out.println("\nplainText: " + message1);
		System.out.println("cipherText: " + cipherText);

		long endTime = System.nanoTime();
		long totalTime = endTime - startTime;
		System.out.println("\nPROCESSING TIME: " + totalTime / 1000000000 + " seconds");
	}

	public static BigInteger encode(final String text) {
		return new BigInteger(text, Character.MAX_RADIX);
	}

	public static String decode(final BigInteger number) {
		return number.toString(Character.MAX_RADIX);
	}
}