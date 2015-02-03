package Hash;

import java.io.Serializable;
import java.math.BigInteger;

public class NodeKey extends Key implements Serializable {

	private final Number160Bit keyValue;
	private final int processID;
	private final String ip;

	// private BigInteger bigIntegerRepresentation;

	public NodeKey(int processID, String ip) {
		this.processID = processID;
		this.ip = ip;
		this.keyValue = sha_1.hash((ip + "|" + processID).getBytes());

	}

	public NodeKey(Number160Bit keyvalue, int processID, String ip) {
		this.processID = processID;
		this.ip = ip;
		this.keyValue = keyvalue;
	}

	@Override
	public boolean equals(Key k) {
		return keyValue.equals(k.getKeyValue());
	}

	@Override
	public Number160Bit getKeyValue() {
		return keyValue;
	}

	public String getIP() {
		return this.ip;
	}

	public int getPID() {
		return this.processID;
	}

	public String toString() {
		return ip + "|" + processID;
	}

	@Override
	public int compareTo(Key arg0) {

		return this.keyValue.getBigIntegerRepresentation().compareTo(
				arg0.getKeyValue().getBigIntegerRepresentation());
	}

}
