package laurentco.election;

import java.util.Collection;
import java.util.Collections;

/**
 * This class implements a basic distributed consensus majority algorithm where
 * an
 * arbitrary number of nodes independently elect a leader (Master)
 * 
 * <code>ElectionListener</code> can be used to observe changes of Master over
 * time.
 * 
 * This class manages multi-round elections, 2 elections are held concurrently
 * on each
 * round: Master and Candidate
 * The Candidate is the node that would become Master should the Master become
 * unavailable
 * 
 * @author Laurent Comparet
 * 
 * @param <E> the type of candidate in the election (typically a node in a
 *            distributed setup)
 */
public class MajorityElection<E extends Object & Comparable<? super E>> {

    private final int           threshold;
    private final Collection<E> allNodes;
    private final E             myNode;

    private Election<E>         masterElection;
    private Election<E>         candidateElection;

    private Vote<E>             myVote;
    private E                   assumedMaster;
    private E                   consensusMaster;
    private ElectionListener    electionListener;
    private ElectionTransport   electionTransport;

    public MajorityElection(Collection<E> allNodes, E myNode) {
	this.allNodes = allNodes;
	this.myNode = myNode;
	this.threshold = allNodes.size() / 2 + 1;		// majority
	init();
    }

    /**
     * run a round of election
     */
    public void run() {

	E previousConsensusMaster = consensusMaster;

	// save elections and init next round
	Election<E> savedMasterElection = masterElection;
	Election<E> savedCandidateElection = candidateElection;

	// init next round
	init();

	// compute consensus votes
	E winner = savedMasterElection.getWinner();
	consensusMaster = winner != null ? winner : previousConsensusMaster;

	if (previousConsensusMaster != consensusMaster && electionListener != null) {
	    assumedMaster = consensusMaster;
	    electionListener.newMaster(consensusMaster);
	}

	// compute my vote for the next round
	myVote = computeVote(savedCandidateElection, myVote, assumedMaster);

	// broadcast to other nodes
	electionTransport.broadcast(myVote);
    }

    private Vote<E> computeVote(Election<E> election, Vote<E> previousVote, E master) {

	Collection<E> online = election.getVoters();

	// master vote changes only when the previous master is down
	E myMasterVote = (previousVote != null) ? previousVote.getMaster() : null;
	if (myMasterVote == null || !online.contains(master)) {
	    E consensusCandidate = election.getWinner();
	    if (consensusCandidate != null)
		myMasterVote = consensusCandidate;
	}

	/*
	// Avoid voting for assumed master in the candidate election
	if(assumedMaster!=null)
		online.remove(assumedMaster);
	*/

	// candidate vote is always for the online node with a minimal id
	E myCandidateVote = online.size() > 0 ? Collections.min(online) : null;

	Vote<E> myVote = new Vote<E>(myNode, myMasterVote, myCandidateVote);
	return myVote;
    }

    /**
     * Initialize a round of election
     * 
     */
    private void init() {
	this.masterElection = new Election<E>(allNodes, threshold);
	this.candidateElection = new Election<E>(allNodes, threshold);
    }

    public void accept(Vote<E> vote) {
	if (allNodes.contains(vote.getVoter())) {
	    masterElection.vote(vote.getMaster(), vote.getVoter());
	    candidateElection.vote(vote.getCandidate(), vote.getVoter());
	}
    }

    public E getMyNode() {
	return myNode;
    }

    protected Vote<E> getVote() {
	return myVote;
    }

    public ElectionListener getElectionListener() {
	return electionListener;
    }

    public void setElectionListener(ElectionListener electionListener) {
	this.electionListener = electionListener;
    }

    public ElectionTransport getElectionTransport() {
	return electionTransport;
    }

    public void setElectionTransport(ElectionTransport electionTransport) {
	this.electionTransport = electionTransport;
    }
}
