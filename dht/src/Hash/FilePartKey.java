package Hash;



public class FilePartKey extends Key {
	
	private final String filepartName;
	private final Number160Bit keyValue;
	
	public FilePartKey(String filepartName)
	{		
		this.filepartName=filepartName;
		this.keyValue=sha_1.hash(filepartName.getBytes());
	}
	
	@Override
	public byte[] getByteKey() {		
		return keyValue.getBytes();
	}

	@Override
	public boolean equals(Key k) {		
		return keyValue.equals(k.getKeyValue());
	}

	@Override
	public Number160Bit getKeyValue() {
		
		return keyValue;
	}

	@Override
	public String hashKeytoHexString() {
		// TODO Auto-generated method stub
		return null;
	}

}
