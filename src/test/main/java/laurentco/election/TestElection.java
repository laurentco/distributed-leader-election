package laurentco.election;

import java.util.Collection;

import junit.framework.TestCase;
import laurentco.election.MajorityElection;
import laurentco.election.Vote;


/**
 * An implementation of the Election type using the TestNode.id (String) for voting
 * and able to assert expectations on the votes in current elections
 *
 */
		
class TestElection extends MajorityElection<String> {
	
	public TestElection(Collection<String> nodes, String myNode) {
		super(nodes, myNode);
	}

	public void assertVote(String master, String candidate) {
		Vote<String> v = super.getVote();
		TestCase.assertEquals(master, v.getMaster());
		TestCase.assertEquals(candidate, v.getCandidate());
	}
}