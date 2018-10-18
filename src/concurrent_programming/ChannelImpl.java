package concurrent_programming;

public class ChannelImpl implements Channel{
	
	private int capacidade;
	
	public ChannelImpl(int capacidade) {
		this.capacidade = capacidade;
	}
	
	public int getCapacidade() {
		return capacidade;
	}

	@Override
	public void putMessage(String message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String takeMessage() {
		// TODO Auto-generated method stub
		return null;
	}

}
