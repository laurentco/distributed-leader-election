package laurentco.election;

import java.util.Map;

import laurentco.election.ElectionTransport;
import laurentco.election.Vote;

/**
 * In-JVM transport used for unit testing.
 *
 */
public class LocalTransport implements ElectionTransport {
	
	private Map<? extends Object,? extends ReceiverCallback> callbacks;
	
	public LocalTransport(Map<? extends Object, ? extends ReceiverCallback> callbacks) {
		this.callbacks = callbacks;
	}

	@Override
	public void send(Vote<?> vote, Object to) {
		ReceiverCallback callback = callbacks.get(to);
		if(callback!=null)
			callback.receive(vote);
	}

	@Override
	public void broadcast(Vote<?> vote) {
		for(Object recipient: callbacks.keySet()) {
			send(vote, recipient);
		}
	}
}
