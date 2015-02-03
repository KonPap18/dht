package Hash;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class sha_1 {
	private static String DEFAULT_METHOD = "SHA-1";
	private static String hashMethod;
	private static MessageDigest md;

	public static Number160Bit hash(byte[] key) {
		// SHA - 1 implementation
		if (hashMethod == null) {
			hashMethod = DEFAULT_METHOD;
		}

		try {
			md = MessageDigest.getInstance(hashMethod);
		} catch (NoSuchAlgorithmException ex) {
		}

		md.reset();
		md.update(key);

		return new Number160Bit(md.digest());
		// }
	}
}
