package laurentco.election;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

class Election<E> {
	
    private final int quorum;
    private final Map<E,Integer> votes;
    private final Set<E> voters;
    
    private E winner = null;
    
    public Election(Collection<E> candidates, int quorum) {
        this.quorum = quorum;
        this.votes = new HashMap<E,Integer>(candidates.size());
        Iterator<E> i=candidates.iterator();
        while(i.hasNext())
            votes.put(i.next(), Integer.valueOf(0));
        this.voters = new HashSet<E>(candidates.size());
    }
    
    public boolean vote(E vote, E voter) {
    	if(voters.contains(voter))
    		return hasWinner();		// voter already voted in this election
    	voters.add(voter);
        Integer n = votes.get(vote);
        if(n==null)
            return hasWinner();   	// not a valid candidate: ignore vote
        n = Integer.valueOf(n+1);
        votes.put(vote, n);
        if(!hasWinner() && n>=quorum)
            winner = vote;
        return hasWinner();
    }
    
    public boolean hasWinner() {
        return winner!=null;
    }
    
    public E getWinner() {
        return winner;
    }
    
    public Set<E> getVoters() {
        return voters;
    }
}
