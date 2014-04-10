package laurentco.election;

import laurentco.election.ElectionTransport;

public class MajorityElectionTest extends AbstractElectionTest<TestNode> {

    private static final TestNode   node1 = new TestNode("1");
    private static final TestNode   node2 = new TestNode("2");
    private static final TestNode   node3 = new TestNode("3");
    private static final TestNode   rogue = new TestNode("4");
    private static final TestNode[] nodes = {
	    node1, node2, node3
	                                  };

    @Override
    protected void setUp() throws Exception {
		super.setUp();
		addNodes(nodes);
		setupNode(node1);
		setupNode(node2);
		setupNode(node3);
    }

    public void setupNode(TestNode node) {
		ElectionTransport transport = new LocalTransport(getKnownNodeMap());
		TestElection election = new TestElection(getKnownNodeIds(), node.getId());
		election.setElectionTransport(transport);
		node.setElection(election);
    }

    public void testAllNodesOnlineStartup() {

		node1.runElection();
		// node1 has not received anything yet, votes null
		node1.getElection().assertVote(null, null);
		node1.assertMaster(false);
	
		node2.runElection();
		// node2 has received node1's vote hence votes for node1 in candidate election
		node2.getElection().assertVote(null, "1");
		node2.assertMaster(false);
	
		node3.runElection();
		// node3 has received node1 and node2 votes hence votes for node1 in candidate election
		node3.getElection().assertVote(null, "1");
		node3.assertMaster(false);
	
		node1.runElection();
		// node1 has received all 3 votes and wins candidate vote hence gets vote in master election
		node1.getElection().assertVote("1", "1");
		node1.assertMaster(false);
	
		node2.runElection();
		// node2 has received all 3 votes and node1 wins candidate vote hence gets vote in master election
		node2.getElection().assertVote("1", "1");
		node2.assertMaster(false);
	
		node3.runElection();
		// node1 wins master and candidate vote
		node3.getElection().assertVote("1", "1");
		node3.assertMaster(false);
	
		node1.runElection();
		// node1 wins master and candidate vote
		node1.getElection().assertVote("1", "1");
		// node1 becomes master
		node1.assertMaster(true);
	
		node2.runElection();
		// node1 still wins master and candidate vote
		node2.getElection().assertVote("1", "1");
		node2.assertMaster(false);
	
		node3.runElection();
		// node1 still wins master and candidate vote
		node3.getElection().assertVote("1", "1");
		node3.assertMaster(false);
    }

    public void testMasterGoesOffline() {

		System.out.println(node1 + " goes offline");
		node2.runElection();
		node3.runElection();
	
		node2.runElection();
		node3.runElection();
	
		node2.runElection();
		node3.runElection();
	
		System.out.println(node1 + " comes back online");
		node1.runElection();
		node2.runElection();
		node3.runElection();
	
		node1.runElection();
		node2.runElection();
		node3.runElection();
	
		node1.runElection();
		node2.runElection();
		node3.runElection();
	
		node1.runElection();
		node2.runElection();
		node3.runElection();
	
		System.out.println(node2 + " goes offline");
		node1.runElection();
		node3.runElection();
	
		node1.runElection();
		node3.runElection();
	
		node1.runElection();
		node3.runElection();
    }
}