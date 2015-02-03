package Hash;

import static org.junit.Assert.*;

import java.math.BigInteger;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

public class Number160BitTest {

	private BigInteger big1;
	private BigInteger big2;

	@Before
	public void setUp() throws Exception {
		Integer int1=new Integer(1201);
		
		 big1=BigInteger.valueOf(int1);
		 big2=BigInteger.valueOf(new Integer(1001));
		
	}

	@Test
	public void testConstructor1() {
		Number160Bit n160b1=new Number160Bit(big1);
		//Assert.assertNotNull(n160b1.getBytes());
		
		
	}
	@Test
	public void testAdd() {
		Number160Bit n160b1=new Number160Bit(big1);
		Number160Bit n160b2=new Number160Bit(big2);
		Number160Bit sum=n160b1.add(n160b2);
		//Assert.assertEquals(2202, sum.getBigIntegerRepresentation().intValue());
	}
	
	@Test
	public void testSub() {
		Number160Bit n160b1=new Number160Bit(big1);
		Number160Bit n160b2=new Number160Bit(big2);
		Number160Bit sub=n160b1.subtract(n160b2);
		Assert.assertEquals(200,  sub.getBigIntegerRepresentation().intValue());
	}
	@Test 
	public void testSquared() {
		Number160Bit nb1=new Number160Bit(BigInteger.valueOf(new Integer(6)));
		Assert.assertEquals(36,  nb1.squared().getBigIntegerRepresentation().intValue());
		
	}

}
