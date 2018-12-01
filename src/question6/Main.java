package question6;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

import channel.Channel;
import channel.ChannelImpl;

public class Main {
	
	private static final int LIMITE_NUMEROS_GERADOS = 5;
	private static Random gerador;
	private static Channel channel = new ChannelImpl(LIMITE_NUMEROS_GERADOS);
	
	private static Runnable t1 = new Runnable() {
		
		@Override
		public void run() {
			gerador = new Random();
			for (int i = 0; i < LIMITE_NUMEROS_GERADOS; i++) {
				int number_generated = gerador.nextInt();
				String number = Integer.toString(number_generated);
				channel.putMessage(number);
				System.out.println(number);
			}
			System.out.println(" ");
		}
	};
	
	private static Runnable t2 = new Runnable() {
		
		@Override
		public void run() {
			for (int i = 0; i < LIMITE_NUMEROS_GERADOS; i++) {
				String element = channel.takeMessage();
				Integer number = Integer.parseInt(element);
				if (number % 2 == 0) {
					System.out.println(element);
				}
			}
		}
	};
	
	public static void main(String[] args) throws InterruptedException {
		Thread thread1 = new Thread(t1);
		Thread thread2 = new Thread(t2);
		
		thread1.start();
		thread1.join();
		
		thread2.start();
		thread2.join();
		
		System.out.println("Programa finalizado");
	}

}
