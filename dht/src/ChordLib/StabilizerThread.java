package ChordLib;

import java.net.MalformedURLException;
import java.rmi.AccessException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.ExportException;
import java.util.List;
import java.util.Random;

import DistributedStorageApp.AppNodeInt;
import Hash.Key;
import Hash.NodeKey;

public class StabilizerThread implements Runnable {
	private final AppNodeInt node;
	private boolean successorIsDown = false;
	private boolean rmi2IsDown;
	private static final String REF_NAME = "ref";

	public StabilizerThread(AppNodeInt node) {
		this.node = node;
	}

	@Override
	public void run() {
		System.out.println("   STABILIZER IS STARTING");

		boolean rmiIsDown = false;
		Random randomGenerator = new Random();
		//
		// TODO Auto-generated method stub
		Registry registry = null;
		try {
			registry = LocateRegistry.createRegistry(1099);
			// elegxos an uparxei rmi se auto to mixanima
			rmiIsDown = true;
			System.out.println("i am " + node.getNodeKey().getPID()
					+ " and i will create a new registry for you");

		} catch (ExportException e) {
			rmiIsDown = false;
		} catch (RemoteException e) {

			rmiIsDown = false;
		}

		if (rmiIsDown) {
			try {
				Thread.sleep(randomGenerator.nextInt(5000));
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				// e.printStackTrace();
				// do nothing

			}

			// rmi is down means that someone left the ring from thiw ip so
			// everyone from this
			// ip is now unaccesible
			try {
				List<NodeKey> nodeSuccessors = node.getSuccessorList();
				List<NodeKey> nodeFingers = node.getFingerList();
				NodeKey predecessor = node.getPredecessor();

				if (registry != null) {
					System.out.println("i am creating the rmi now..");
					try {

						registry.bind(
								String.valueOf(node.getNodeKey().getPID()),
								node);
						registry.bind(REF_NAME, node);// diplo binding
						node.deleteSuccessors();
						node.addSuccessor(node.getNodeKey(), 0);// o eautos tou
						node.setPredecessor(node.getPredecessor());// pali

					} catch (AlreadyBoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}

				System.out.println("Binding complete in Stabilizer");

				String tmpIP = null;
				int tmpPID = 0;
				boolean otherExistingNode = false;
				NodeKey otherNodeKey = null;
				// psaxnoume allo komvo stin idia ip apo tous successors
				for (int i = 0; i < nodeSuccessors.size(); i++)// gia ka8e ena
																// suc
				{
					tmpIP = nodeSuccessors.get(i).getIP(); // pare tin ip tou

					tmpPID = nodeSuccessors.get(i).getPID();// pare to pid tou

					if (!tmpIP.equals(node.getNodeKey().getIP())) // an i ip
																	// einai
																	// idia me
																	// tin ip
																	// pou
																	// trexeiw
																	// twra
					{
						otherExistingNode = true;
						otherNodeKey = nodeSuccessors.get(i); // pare to id tou
						break;// kai vges
					}
				}
				if (!otherExistingNode) {
					// psaxnoume ta daktula
					for (int i = 0; i < nodeFingers.size(); i++) {
						tmpIP = nodeFingers.get(i).getIP();

						tmpPID = nodeFingers.get(i).getPID();

						if (!tmpIP.equals(node.getNodeKey().getIP())) {
							otherExistingNode = true;
							otherNodeKey = nodeFingers.get(i);
							break;
						}
					}

					if (!otherExistingNode) {

						tmpIP = predecessor.getIP();
						tmpPID = predecessor.getPID();
						if (!tmpIP.equals(node.getNodeKey().getIP())) {
							otherExistingNode = true;
							otherNodeKey = predecessor;
						}
					}

					// an telika vrikame allo komvo stin ip
					if (otherExistingNode) {
						System.out.println("Look up " + "/" + tmpIP + ":1099/"
								+ String.valueOf(tmpPID));
						try {
							AppNodeInt ref = (AppNodeInt) Naming
									.lookup("/" + tmpIP + ":1099/"
											+ String.valueOf(tmpPID));
							try {
								node.join(ref);
							} catch (NullPointerException e) {
								node.addSuccessor(otherNodeKey, 0);
								System.out
										.println("Null pointer thrown in joining ref");
							}
						} catch (MalformedURLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (NotBoundException e) {
							System.out.println("reference does not exist ");
						}

					}

				}

			} catch (RemoteException e) {
				System.out.println("it seems like rmi is up and running");
				rmiIsDown = false;
			}

		}

		if (!rmiIsDown) {
			// kainourio rmi

			Registry newRegistry = null;
			try {
				newRegistry = LocateRegistry.getRegistry(1099);
				newRegistry.lookup(String.valueOf(node.getNodeKey().getPID()));
			} catch (NotBoundException | RemoteException e) {
				// join
				if (newRegistry != null) {
					randomGenerator.setSeed(System.currentTimeMillis());
					int timeToSleep = randomGenerator.nextInt(5000);

					try {
						Thread.sleep(timeToSleep);
					} catch (InterruptedException ex) {

					}

					try {
						newRegistry.bind(
								String.valueOf(node.getNodeKey().getPID()),
								node);

						try {

							AppNodeInt ref;
							ref = (AppNodeInt) newRegistry.lookup(REF_NAME);

							try {
								node.join(ref);
							} catch (NullPointerException nexc) {
							}

						} catch (NotBoundException ex) {

							randomGenerator.setSeed(System.currentTimeMillis());
							timeToSleep = randomGenerator.nextInt(10000);

							if (timeToSleep < 3000) {
								timeToSleep = timeToSleep + 3000;
							}

							try {
								Thread.sleep(timeToSleep);
							} catch (InterruptedException ex1) {

							}

							AppNodeInt ref;

							try {
								ref = (AppNodeInt) newRegistry.lookup(REF_NAME);
								System.out.println("i am about to join");
								try {
									node.join(ref);
								} catch (NullPointerException nexce) {
								}

							} catch (NotBoundException ex1) {
								System.out.println("Not bound");
							}
						}

					} catch (AlreadyBoundException ex) {
						System.out.println("bound already?! ");
					} catch (AccessException ex) {
						System.out.println("Access Exception");
					} catch (RemoteException re) {
						System.out.println("remote exception");
					}

				}

			}

			AppNodeInt successor = null;
			NodeKey successorKey = null;

			try {
				successorKey = node.getSuccessor();

				try {
					successor = (AppNodeInt) Naming.lookup("/"
							+ successorKey.getIP() + ":1099/"
							+ String.valueOf(successorKey.getPID()));
				} catch (RemoteException remote) {
					String hostAddress = successorKey.getIP();
					successorIsDown = true;// check rmi
					System.out.println("Succesor is down lest chsck rmi");
					try {
						registry = LocateRegistry.getRegistry("rmi:/"
								+ hostAddress);
						// if not excpetion rmi is up and running
						rmi2IsDown = false;
					} catch (RemoteException remte) {
						System.out.println("rmi is down too");
						rmi2IsDown = true;
					}
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NotBoundException e) {
					// TODO Auto-generated catch block
					successorIsDown = true;
				}

			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				System.out.println("my successor is down ");
			}

			if (successorIsDown) {// αν ο suc του κομβου
				try {

					NodeKey tmp = node.getSuccessorList().get(0);
					// no assign just to be sure we get/dont a value
					Naming.lookup("/" + tmp.getIP() + ":1099/"
							+ String.valueOf(tmp.getPID()));
				} catch (NotBoundException | RemoteException e) {
					try {
						node.getSuccessorList().remove(0);

						if (node.getSuccessorListSize() == 0) {
							node.addSuccessor(node.getNodeKey(), 0);

							if (node.getSuccessor().getKeyValue()
									.compareTo(node.getNodeKey().getKeyValue()) == 0) {
								node.setPredecessor(node.getNodeKey());
							}
						} else {
							AppNodeInt nStmp;
							try {

								nStmp = (AppNodeInt) Naming.lookup("/"
										+ node.getSuccessor().getIP()
										+ ":1099/"
										+ String.valueOf(node.getSuccessor()
												.getPID()));
								nStmp.setPredecessor(node.getNodeKey());

							} catch (NotBoundException ex) {
								System.out
										.println("New successor is not bound or was unbound. Continue !!!");
							} catch (MalformedURLException ex) {
								ex.printStackTrace();
							}
						}
					} catch (RemoteException ex) {
						System.out
								.println(".......remote exception in catch into if into fuck");
					}
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} else {// successor is up
				if (!rmi2IsDown) {
					NodeKey tmp = null;
					try {
						tmp = successor.getPredecessor();
					} catch (RemoteException e) {
						// TODO Auto-generated catch block
						System.out
								.println("remote exception in rmi2 not down and succ up");

					}

					AppNodeInt newSuccessor = null;
					NodeKey newSuccessorKey = null;

					try {
						newSuccessorKey = successor.getPredecessor();
					} catch (RemoteException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if (newSuccessorKey != null) {
						try {
							newSuccessor = (AppNodeInt) Naming.lookup("/"
									+ newSuccessorKey.getIP() + ":1099/"
									+ String.valueOf(newSuccessorKey.getPID()));
						} catch (MalformedURLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (RemoteException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (NotBoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}

					try {
						System.out.println("successor id is "
								+ node.getSuccessor().toString());
					} catch (RemoteException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					if (tmp != null) {
						System.out.println("successor's pre id is "
								+ tmp.toString());
						// check if temp is between (n, successor)
						try {
							if (!node.getNodeKey().equals(
									successor.getNodeKey())) {
								if (Key.isBetween(tmp, node.getNodeKey(),
										successor.getNodeKey())) {
									node.addSuccessor(tmp, 0);

									if (node.getFingersSize() != 0) {
										node.getFingerList().remove(0);
									}
									node.setFinger(0, tmp);

									if (newSuccessor != null) {
										// newSuccessor.setEntries(successor.giveEntries(tmp));
									}
								}
							} else {

								if (node.getFingersSize() != 0) {
									node.getFingerList().remove(0);
								}

								node.setFinger(0, tmp);
								node.addSuccessor(tmp, 0);

								if (newSuccessor != null) {
									System.out.println(newSuccessor
											.getNodeKey().toString());
									//
									// dinoume ston neo suc ta arxeia
								}
							}
						} catch (RemoteException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}

				} else {
					try {
						System.out.println("no pre yet for "
								+ successor.getNodeKey().toString());
					} catch (RemoteException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			}

		}

		System.out.println("        STABILEZER FINISHES");
	}

}
