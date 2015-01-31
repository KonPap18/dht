package Hash;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Arrays;



public class Number160Bit implements Serializable{
	private final byte[] value;
	private BigInteger bigIntegerRepresentation;
	
	public Number160Bit(BigInteger bI) {
		// TODO Auto-generated constructor stub
		this.value=bI.toByteArray();
		this.bigIntegerRepresentation=bI;
	}
	
	public Number160Bit(byte[] digest) {
		// TODO Auto-generated constructor stub
		this.value=digest;
		bigIntegerRepresentation=new BigInteger(value);
	}
	/*public Number160Bit add(byte[] numToAdd) {
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
	}*/

	public Number160Bit add(Number160Bit numToAdd) {
		return this.add(numToAdd.value);
	}	

	public BigInteger getBigIntegerRepresentation() {
		return bigIntegerRepresentation;
	}

	public byte[] getBytes()
	{
		byte[] nBigNum = new byte[this.value.length];

		System.arraycopy(this.value, 0, nBigNum, 0, this.value.length);

		return nBigNum;
		
	}

	public void setBigIntegerRepresentation(BigInteger bigIntegerRepresentation) {
		this.bigIntegerRepresentation = bigIntegerRepresentation;
	}
	/*public Number160Bit subtraction(byte[] secNumber)
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
	}*/
	
	public Number160Bit subtract(Number160Bit sub) {
		return new Number160Bit(this.bigIntegerRepresentation.subtract(sub.bigIntegerRepresentation));
	}
	
	public String toBinaryString(){
		String str = "";
		String temp;
		boolean eightZeros = true;

		for (byte b : value)
		{
			temp = Integer.toBinaryString(b & 0xFF);


			while( temp.length() % 8 != 0)
				temp = "0" + temp;

			str += temp;
		}

		for(int i = 0; i < 8; i++)
		{
			if (str.charAt(i) == '1')
			{
				eightZeros = false;
				break;
			}
		}

		if (!eightZeros)
			return str;
		else
			return str.substring(8);
	}
	public String toBinaryStringWithoutZeros(){
		String s = this.toBinaryString();
		int num = 0;

		for (int i = 0; i < s.length(); i++)
			if (s.charAt(i) == '0')
				num++;

		return s.substring(num);
	}

	public String toString() {
		return this.bigIntegerRepresentation.toString();
		//return Arrays.toString(value);
	}
	private Number160Bit add(byte[] value2) {
		// TODO Auto-generated method stub
		return new Number160Bit(bigIntegerRepresentation.add(new BigInteger(value2)));
	}
	
	public Number160Bit squared() {
		return new Number160Bit(this.bigIntegerRepresentation.pow(2));
	}

	public Number160Bit add(BigInteger valueOf) {
		// TODO Auto-generated method stub
		return this.add(new Number160Bit(valueOf));
	}
	
	public int compareTo(Number160Bit other) {
		return this.bigIntegerRepresentation.compareTo(other.bigIntegerRepresentation);
		
	}
}
