package laurentco.election;

/**
 * Immutable class holding master vote and candidate vote
 * @param <E> the actual type of element voters vote for
 */
class Vote<E> {
	
	final private E voter;
	final private E master;
	final private E candidate;
   
	public Vote(E voter, E master, E candidate) {
		this.voter = voter;
		this.master = master;
		this.candidate = candidate;
	}
   
	public E getVoter() {
		return voter;
	}
	
	public E getMaster() {
		return master;
	}
   
	public E getCandidate() {
		return candidate;
	}

	@Override
	public String toString() {
		return "Vote [voter=" + voter + ", master=" + master + ", candidate="
				+ candidate + "]";
	}
	
	
}
