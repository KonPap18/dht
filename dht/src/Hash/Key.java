package Hash;






public abstract class Key
{

	/**
	 * @return byte representation of a hashed key
	 */
	public abstract byte[] getByteKey();

	public abstract boolean equals(Key k);

	public abstract Number160Bit getKeyValue();

	public abstract String hashKeytoHexString();

	public static boolean isBetweenSuccessor(Key id, Key first, Key last)
	{
		Number160Bit idBig = id.getKeyValue();
		Number160Bit firstBig = first.getKeyValue();
		Number160Bit lastBig = last.getKeyValue();

		if (firstBig.compareTo(lastBig) == 1)
		{
			if (idBig.compareTo(firstBig) == -1 && idBig.compareTo(lastBig) <= 0)
			{
				return true;
			}

			if (idBig.compareTo(firstBig) == 1)
			{
				return true;
			}
		}

		if (firstBig.compareTo(lastBig) == -1)
		{
			if (idBig.compareTo(firstBig) == 1 && idBig.compareTo(lastBig) <= 0)
			{
				return true;
			}
		}

		if (firstBig.compareTo(lastBig) == 0 && (firstBig.compareTo(idBig) > 0 || firstBig.compareTo(idBig) < 0))
		{
			return true;
		}

		return false;
	}

	public static boolean isBetween(Key id, Key first, Key last)
	{
		Number160Bit idBig = id.getKeyValue();
		Number160Bit firstBig = first.getKeyValue();
		Number160Bit lastBig = last.getKeyValue();

		if (firstBig.compareTo(lastBig) == -1)
		{
			if (idBig.compareTo(firstBig) == 1 && idBig.compareTo(lastBig) == -1)
			{
				return true;
			}
		}

		if (firstBig.compareTo(lastBig) == 1)
		{
			if (idBig.compareTo(firstBig) == -1 && idBig.compareTo(lastBig) == -1)
			{
				return true;
			}
			if (idBig.compareTo(firstBig) == 1)
			{
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
			if (idBig.compareTo(firstBig) == 1 && idBig.compareTo(lastBig) == -1)
			{
				return true;
			}
		}

		if (firstBig.compareTo(lastBig) == 1)
		{
			if (idBig.compareTo(firstBig) == -1 && idBig.compareTo(lastBig) == -1)
			{
				return true;
			}
			if (idBig.compareTo(firstBig) == 1)
			{
				return true;
			}
		}

		if (firstBig.compareTo(lastBig) == 0)
		{
			return true;
		}

		return false;
	}
	
}
