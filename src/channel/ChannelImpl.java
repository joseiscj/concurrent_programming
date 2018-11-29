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
        while (true) {
            synchronized (this.buffer) {
                while (this.buffer.size() == this.capacidade) {
                    try {
                        this.buffer.wait();
                    } catch (InterruptedException e) { }
                }
                
                this.buffer.add(message);
                this.buffer.notifyAll();
            }
        }
    }
	

	@Override
	public String takeMessage() {
		while (true) {
            synchronized (this.buffer) {
                while (this.buffer.isEmpty()) {
                    try {
                        this.buffer.wait();
                    } catch (InterruptedException e) { }
                }
                this.buffer.notifyAll();
                return this.buffer.poll();
            }
        }
    }

}
