package channel;

import java.util.LinkedList;
import java.util.Queue;

public class ChannelImpl implements Channel{
	
	private Queue<String> buffer;
	private int capacidade;
	
	public ChannelImpl(int capacidade) {
		this.buffer = new LinkedList<String>();
		this.capacidade = capacidade;
	}

	@Override
	public void putMessage(String message) {
		if (!(this.buffer.size() + 1 > capacidade)) {
			this.buffer.add(message);
		}
	}

	@Override
	public String takeMessage() {
		return this.buffer.poll();
	}

}
