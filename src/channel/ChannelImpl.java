package channel;

import java.util.LinkedList;
import java.util.Queue;

public class ChannelImpl implements Channel {

	private Queue<String> buffer;
	private int capacidade;
	private boolean closed;

	public ChannelImpl(int capacidade) {
		this.buffer = new LinkedList<String>();
		this.capacidade = capacidade;
	}

	@Override
	public void putMessage(String message) {

		synchronized (this.buffer) {
			while (this.buffer.size() == this.capacidade) {
				try {
					this.buffer.wait();
				} catch (InterruptedException e) {
				}
			}

			this.buffer.add(message);
			this.buffer.notifyAll();
		}

	}

	@Override
	public String takeMessage() {

		synchronized (this.buffer) {
			while (this.buffer.isEmpty() && !closed) {
				try {
					this.buffer.wait();
				} catch (InterruptedException e) {
				}
			}
			this.buffer.notifyAll();
			return this.buffer.poll();
		}

	}

	@Override
	public int size() {
		return this.buffer.size();
	}
	
	@Override
	public void close() {
		synchronized (this) {
			if (closed) {
				return;
			}
			closed = true;
		}
	}

}
