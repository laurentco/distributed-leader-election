package laurentco.election;


public class RunnableTestNode extends TestNode implements Runnable {

    private final boolean running = true;
    private boolean       online  = false;

    public RunnableTestNode(String id) {
	super(id);
    }

    @Override
    public void receive(Vote<?> vote) {
	if (online)
	    super.receive(vote);
    }

    @Override
    public void run() {
	while (running)
	    try {
		Thread.sleep(2000L);
		if (online)
		    runElection();
		// System.out.println(getElection().getVote());
	    } catch (InterruptedException ie) {
		ie.printStackTrace();
	    }
    }

    public boolean isOnline() {
	return online;
    }

    public void setOnline(boolean online) {
	this.online = online;
    }
}