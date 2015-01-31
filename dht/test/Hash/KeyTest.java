package Hash;

import static org.junit.Assert.*;

import java.math.BigInteger;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

public class KeyTest {

	private NodeKey one;
	private NodeKey three;
	private NodeKey two;

	@Before
	public void setUp() throws Exception {
		 one=new NodeKey(new Number160Bit(BigInteger.valueOf(new Integer(1))), 0, null);
		 two=new NodeKey(new Number160Bit(BigInteger.valueOf(new Integer(2))), 0, null);
		 three=new NodeKey(new Number160Bit(BigInteger.valueOf(new Integer(3))), 0, null);
		
	}

	@Test
	public void test1() {
		Assert.assertTrue(Key.isBetween(two, one, three));
	}
	@Test
	public void test2() {
		Assert.assertTrue(Key.isBetweenNotify(two, one, three));
	}
	
	@Test
	public void test3() {
		Assert.assertTrue(Key.isBetweenSuccessor(two, one, three));
	}
	@Test
	public void test4() {
		Assert.assertTrue(Key.isBetween(two, one, two));
	}
	@Test
	public void test5() {
		Assert.assertTrue(Key.isBetweenNotify(two, one, two));
	}
	
	@Test
	public void test6() {
		Assert.assertTrue(Key.isBetweenSuccessor(two, one, two));
	}
	@Test
	public void test7() {
		Assert.assertTrue(Key.isBetween(two, two, three));
	}
	@Test
	public void test8() {
		Assert.assertTrue(Key.isBetweenNotify(two, two, three));
	}
	
	@Test
	public void test9() {
		Assert.assertTrue(Key.isBetweenSuccessor(two, two, three));
	}
	//an einai iso me to katw orio ola vgazoun false isNOT between
	
	//an einai anamesa ola vgazoun true isBetween
	
	//an akoumpaei to panw orio to 4 kai to 5 (between kai betweenNotify)vgazoun false

}
