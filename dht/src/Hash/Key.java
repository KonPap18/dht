package Hash;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Key {
	byte [] number;
	private MessageDigest mD;
	public Key(byte value) {
		try {
			mD = MessageDigest.getInstance("SHA-1");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		mD.reset();
		mD.update(value);
		number=mD.digest();
	}
	public Key(String nodeProcessID, String string) {
		// TODO Auto-generated constructor stub
	}
	public Key(int processID, String string) {
		// TODO Auto-generated constructor stub
	}
	public static boolean isBetweenNotify(Key nodeID, Key predecessor) {
		// TODO Auto-generated method stub
		return false;
	}
	
	
	
}
