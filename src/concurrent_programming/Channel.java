package concurrent_programming;

public interface Channel {
	
	public void putMessage(String message);
	public String takeMessage();
}
