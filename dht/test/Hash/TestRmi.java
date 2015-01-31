package Hash;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.ExportException;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

public class TestRmi {

	private Registry rmiRegistry;

	@Before
	public void setUp() throws Exception {
		
		//rmiRegistry = LocateRegistry.createRegistry(1099);
	}

	@Test
	public void test() throws RemoteException {
		boolean Thrown=false;
		try {
		Registry r=LocateRegistry.createRegistry(1099);
		}catch(ExportException e) {
			Thrown=true;
		}
		Assert.assertTrue(Thrown);
	}
	@Test
	public void test2() throws RemoteException {
		boolean Thrown=false;
		Registry r;
		
		 r=LocateRegistry.createRegistry(1099);
		
		
		Assert.assertNotNull(r);
	}

}
