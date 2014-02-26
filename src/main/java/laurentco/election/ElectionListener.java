package laurentco.election;

public interface ElectionListener {
	<E> void newMaster(E e);
}