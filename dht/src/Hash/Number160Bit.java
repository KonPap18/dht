package Hash;



public class Number160Bit {
	private final byte[] value;
	
	public Number160Bit(byte[] digest) {
		// TODO Auto-generated constructor stub
		this.value=digest;
	}
	public Number160Bit add(byte[] numToAdd) {
		int length;
		if(this.value.length>numToAdd.length) {
			length=this.value.length;
		}else {
			length=numToAdd.length;
		}
		int low;
		if(this.value.length<numToAdd.length) {
			low=this.value.length;
		}else {
			low=numToAdd.length;
		}
		
		byte []res=new byte[length];
		
		byte[] temp=new byte[length];
		System.arraycopy(numToAdd, 0, temp, length-low, low);
		
		for (int i = 0; i < length - low; i++){
			temp[i] = 0;
		}
		
		
		int sum;
		int kratoumeno=0;
		
		for (int i =  0; i < 20; i++ )
		{
			if (temp[i] == 0)
			{
				sum = (int)value[i] + (int)temp[i] + kratoumeno;
				res[i] = value[i];
			}
			else
			{
				sum = (int)value[i] + (int)temp[i] + kratoumeno;

				kratoumeno = sum >> 8;
				res[i] = (byte) (sum & 0xFF);
			}
		}
		
		return new Number160Bit(res);
	}
	public Number160Bit add(Number160Bit numToAdd) {
		return this.add(numToAdd.value);
	}
	public Number160Bit subtraction(byte[] secNumber)
	{
		int kratoumeno = 0;
		int thisLength = this.value.length;
		int secLength = secNumber.length;
		byte[] temp;
		byte[] result = null;
		int i;
		int sum;
		int diff , totalSub;

		if (thisLength > secLength)
		{
			temp = new byte[thisLength];
			System.arraycopy(secNumber, 0, temp, thisLength - secLength, secLength);

			for (i = 0; i < thisLength - secLength; i++)
			{
				temp[i] = 0;
			}

			result = new byte[thisLength];

			for (i = 0; i < thisLength; i++)
			{
				totalSub = temp[i] + kratoumeno;
				if (this.value[i] < totalSub)
				{
					diff = (this.value[i] + 0x100) - totalSub;
					kratoumeno = 1;
				}
				else
				{
					diff = this.value[i] - totalSub;
					kratoumeno = 0;
				}

				result[i] = (byte)diff;
			}
		}
		else if (thisLength < secLength)
		{
			temp = new byte[secLength];
			System.arraycopy(this.value, 0, temp, secLength - thisLength, thisLength);

			for (i = 0; i < secLength - thisLength; i++)
			{
				temp[i] = 0;
			}

			result = new byte[secLength];

			for (i = 0; i < secLength; i++)
			{
				totalSub = secNumber[i] + kratoumeno;
				if (temp[i] < totalSub)
				{
					diff = (temp[i] + 0x100) - totalSub;
					kratoumeno = 1;
				}
				else
				{
					diff = temp[i] - totalSub;
					kratoumeno = 0;
				}
				result[i] = (byte)diff;
			}
		}
		else
		{
			result = new byte[secLength];

			for (i = 0; i < secLength; i++)
			{
				totalSub = secNumber[i] + kratoumeno;
				if (this.value[i] < totalSub)
				{
					diff = (this.value[i] + 0x100) - totalSub;
					kratoumeno = 1;
				}
				else
				{
					diff = this.value[i] - totalSub;
					kratoumeno = 0;
				}
				result[i] = (byte)diff;
			}

		}

		return new Number160Bit(result);
	}
	public byte[] getBytes()
	{
		byte[] nBigNum = new byte[this.value.length];

		System.arraycopy(this.value, 0, nBigNum, 0, this.value.length);

		return nBigNum;
		
	}
	public int compareTo(Number160Bit lastBig) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	
	
}
