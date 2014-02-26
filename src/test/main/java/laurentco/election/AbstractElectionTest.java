package laurentco.election;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import junit.framework.TestCase;

public abstract class AbstractElectionTest<N extends TestNode> extends TestCase {

	private final Map<String,N> knownNodes = new LinkedHashMap<String,N>();
	
	/** 
	 * Constructor to be used by JUnit test cases
	 * Ensure the list of node is initialized on setUp()
	 * 
	 */
	public AbstractElectionTest() {

	}
	
	/** 
	 * Constructor to be used in other tests
	 * Ensure the list of node is passed
	 * 
	 */
	public AbstractElectionTest(N[] nodes) {
		super();
		addNodes(nodes);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.knownNodes.clear();
	}
	
	public Collection<N> getKnownNodes() {
		return knownNodes.values();
	}
	
	public Map<String,N> getKnownNodeMap() {
		return knownNodes;
	}
	
	public N getNode(String nodeId) {
		return knownNodes.get(nodeId);
	}
	
	public Collection<String> getKnownNodeIds() {
		return knownNodes.keySet();
	}
	
	public void addNode(N node) {
		knownNodes.put(node.getId(), node);
	}
	
	public void addNodes(N[] nodes) {
		for(int i=0 ; i<nodes.length ; i++)
			addNode(nodes[i]);
	}
	
	public void addNodes(Collection<N> nodes) {
		Iterator<N> i = nodes.iterator();
		while(i.hasNext())
			addNode(i.next());
	}
}
