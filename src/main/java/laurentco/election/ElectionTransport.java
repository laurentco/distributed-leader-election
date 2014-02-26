package laurentco.election;

public interface ElectionTransport {

	void send(Vote<?> message, Object to);
	void broadcast(Vote<?> vote);
	
	public interface ReceiverCallback {
		void receive(Vote<?> vote);
	}
}
