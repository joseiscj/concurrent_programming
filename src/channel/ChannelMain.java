package channel;

public class ChannelMain {

	private static final int TAMANHO_BUFFER = 5;
	private static ChannelImpl channel = new ChannelImpl(TAMANHO_BUFFER);

	private static Runnable t1 = new Runnable() {

		@Override
		public void run() {
			for (int i = 0; i < 10000000; i++) {
				System.out.println("t1: i = " + i);
				channel.putMessage("message thread1:" + i);
			}
		}

	};

	private static Runnable t2 = new Runnable() {

		@Override
		public void run() {
			for (int i = 0; i < 10000000; i++) {
				String msg = channel.takeMessage();
				System.out.println("t2 ||" + msg);
			}
		}
	};

	public static void main(String[] args) throws InterruptedException {

		Thread thread1 = new Thread(t1);
		Thread thread2 = new Thread(t2);

		thread1.start();
		thread2.start();

		thread1.join();
		thread2.join();

		System.out.println("programa finalizado");

	}
}
