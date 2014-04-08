package laurentco.election;

import junit.framework.TestCase;
import laurentco.election.ElectionListener;
import laurentco.election.ElectionTransport;
import laurentco.election.Vote;


/**
 * A simple test node that can display its state and assert whether it is master
 *
 */
public class TestNode implements Comparable<TestNode>, ElectionListener, ElectionTransport.ReceiverCallback {
	
	final private String id;
	private boolean master = false;
	private TestElection election;
	
	public TestNode(String id) {
		this.id = id;
	}
	
	public String getId() {
		return id;
	}
	
	@Override
	public int compareTo(TestNode o) {
		return id.compareTo(o.id);
	}
	
	public void setElection(TestElection election) {
		this.election = election;
		election.setElectionListener(this);
	}
	
	public TestElection getElection() {
		return election;
	}
	
	public void runElection() {
		election.run();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void receive(Vote<?> vote) {
		election.accept((Vote<String>)vote);
	}
	
	@Override
	public String toString() {
		return "Node[id=" + id + "]";
	}
	
	@Override
	public <N> void newMaster(N n) {
		if(master) {
			if(this!=n) {
				master = false;
				System.out.println(this + " gives up master");
			} else {
				TestCase.fail("THIS SHOULD NOT HAPPEN: " + this + " is already master!");
			}
		} else {
			if(getId().equals(n)){
				master = true;
				System.out.println(this + " becomes master");
			} else {
				System.out.println(this + " detects change of master to " + n);
			}
		}
	}
	
	public void assertMaster(boolean assertion) {
		TestCase.assertEquals(assertion, master);
	}
}