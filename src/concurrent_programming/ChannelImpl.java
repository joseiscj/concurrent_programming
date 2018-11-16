package concurrent_programming;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ChannelImpl implements Channel{
	
	private BlockingQueue<String> buffer;
	
	public ChannelImpl(int capacidade) {
		this.buffer = new ArrayBlockingQueue<String>(capacidade, true);
	}

	@Override
	public void putMessage(String message) {
		try {
			this.buffer.put(message);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String takeMessage() {
			String message = null;
		try {
			message = this.buffer.take();
		} catch (InterruptedException e) {
 			e.printStackTrace();
		}
		return message;
	}

}
