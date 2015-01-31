package Hash;






public abstract class Key
{

	public static boolean isBetween(Key id, Key first, Key last)
	{
		Number160Bit idBig = id.getKeyValue();
		Number160Bit firstBig = first.getKeyValue();
		Number160Bit lastBig = last.getKeyValue();

		if (firstBig.compareTo(lastBig) == -1)
		{
			
			if (idBig.compareTo(firstBig) == 1 && idBig.compareTo(lastBig) == -1)
			//an id>first kai id<last
			{
				return true;
			}
		}

		if (firstBig.compareTo(lastBig) == 1)
		{
			//first>last
			if (idBig.compareTo(firstBig) == -1 && idBig.compareTo(lastBig) == -1)
			{
				//an to id<katw kai id<panw 
				return true;
			}
			if (idBig.compareTo(firstBig) == 1)
			{
				//an id>first true
				return true;
			}
		}

		return false;
	}

	public static boolean isBetweenNotify(Key id, Key first, Key last)
	{
		Number160Bit idBig = id.getKeyValue();
		Number160Bit firstBig = first.getKeyValue();
		Number160Bit lastBig = last.getKeyValue();

		if (firstBig.compareTo(lastBig) == -1)
		{
			//first<big
			if (idBig.compareTo(firstBig) == 1 && idBig.compareTo(lastBig) == -1)
			{
				//id>first kai id<last
				return true;
			}
		}

		if (firstBig.compareTo(lastBig) == 1)
		{
			//first>last
			if (idBig.compareTo(firstBig) == -1 && idBig.compareTo(lastBig) == -1)
			{
				//id<first kai id<last
				return true;
			}
			if (idBig.compareTo(firstBig) == 1)
			{
				//id>first
				return true;
			}
		}

		if (firstBig.compareTo(lastBig) == 0)
		{
			//first=last
			return true;
		}

		return false;
	}

	public static boolean isBetweenSuccessor(Key id, Key first, Key last)
	{
		Number160Bit idBig = id.getKeyValue();
		Number160Bit firstBig = first.getKeyValue();
		Number160Bit lastBig = last.getKeyValue();

		if (firstBig.compareTo(lastBig) == 1)
		{
			//first>last
			if (idBig.compareTo(firstBig) == -1 && idBig.compareTo(lastBig) <= 0)
			{
				//id<first kai id<=last
				return true;
			}

			if (idBig.compareTo(firstBig) == 1)
			{
			//id>first
				return true;
			}
		}

		if (firstBig.compareTo(lastBig) == -1)
			//first<last
			
		{
			if (idBig.compareTo(firstBig) == 1 && idBig.compareTo(lastBig) <= 0)
			{
				//id>first k id<=last
				return true;
			}
		}

		if (firstBig.compareTo(lastBig) == 0 && (firstBig.compareTo(idBig) > 0 || firstBig.compareTo(idBig) < 0))
		{
			//first=last k first>id H first<id 
			return true;
		}

		return false;
	}

	public abstract boolean equals(Key k);

	/**
	 * @return byte representation of a hashed key
	 */
	public abstract byte[] getByteKey();

	public abstract Number160Bit getKeyValue();

	public abstract String hashKeytoHexString();
	
}
