package laurentco.election;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import laurentco.election.ElectionTransport;

public class MajorityElectionScriptTest extends AbstractElectionTest<RunnableTestNode> {
	
	private static final RunnableTestNode node1 = new RunnableTestNode("1");
	private static final RunnableTestNode node2 = new RunnableTestNode("2");
	private static final RunnableTestNode node3 = new RunnableTestNode("3");
	private static final RunnableTestNode node4 = new RunnableTestNode("4");
	private static final RunnableTestNode node5 = new RunnableTestNode("5");
	private static final RunnableTestNode[] nodes = {
		node1, node2, node3, node4, node5
	};
	
	public enum Command {
		online, offline, exit, list
	}
	
	public static void main(String[] args) throws Exception {
		
		MajorityElectionScriptTest test = new MajorityElectionScriptTest();
		test.startThreads();
		test.executeScript(System.in);
	}
	
	private Set<RunnableTestNode> online = new HashSet<RunnableTestNode>();
	
	public MajorityElectionScriptTest() {
		super(nodes);
		for(int i=0 ; i<nodes.length ; i++)
			setupNode(nodes[i]);
	}
	
	public void setupNode(RunnableTestNode node) {
		ElectionTransport transport = new LocalTransport(getKnownNodeMap());
		TestElection election = new TestElection(getKnownNodeIds(), node.getId());
		election.setElectionTransport(transport);
		node.setElection(election);
	}

	
	public void startThreads() {
		for(int i=0 ; i<nodes.length ; i++)
			new Thread(nodes[i]).start();
	}

	
	public void executeScript(InputStream script) {
		
		String command;
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		try {
			while((command = readCommand(in))!=null && executeCommand(command));
		} catch(IOException ioe) {
			ioe.printStackTrace();
		} finally {
			try {
				in.close();
			} catch(IOException ioe) {}
		}
	}
	
	private String readCommand(BufferedReader in) throws IOException {
		System.out.print(">");
		return in.readLine();
	}

	public boolean executeCommand(String commandLine) {
		
		String commandArgs[] = commandLine.split(" ");
		if(commandArgs.length == 0)
			return false;
		try {
			Command command = Command.valueOf(commandArgs[0]);
			if(command==Command.exit) {
				System.out.println("Quit.");
				return false;
			}
			Collection<RunnableTestNode> nodes = parseNodes(commandArgs[1]);
			switch(command) {
				case online:
					Iterator<RunnableTestNode> on = nodes.iterator();
					while(on.hasNext()) {
						RunnableTestNode node = on.next();
						if(node.isOnline()) {
							on.remove();
						} else {
							node.setOnline(true);
							online.add(node);
						}
					}
					System.out.println(nodes + " onlined");
					return true;
				case offline:
					Iterator<RunnableTestNode> off = nodes.iterator();
					while(off.hasNext()) {
						RunnableTestNode node = off.next();
						if(node.isOnline()) {
							node.setOnline(false);
							online.remove(node);
						} else {
							off.remove();
						}
					}
					System.out.println(nodes + " offlined");
					return true;
				case list:
					System.out.println(nodes);
					return true;
			}
		} catch(IllegalArgumentException iae) {
			System.out.println("Unknown command!");
		} catch(ArrayIndexOutOfBoundsException aobe) {
			System.out.println("Node is unspecified or does not exist!");
		}
		return true;
	}

	private Collection<RunnableTestNode> parseNodes(String nodeArg) {
		
		Collection<RunnableTestNode> nodes;
		
		if("all".equals(nodeArg))
			nodes=new ArrayList<RunnableTestNode>(getKnownNodes());
		else if("online".equals(nodeArg))
			nodes=new ArrayList<RunnableTestNode>(online);
		else {
			String indexes[] = nodeArg.split(",");
			nodes = new ArrayList<RunnableTestNode>(indexes.length);
			for(int i=0 ; i<indexes.length ; i++)
				nodes.add(getNode(indexes[i]));
		}
		
		return nodes;
	}
}

