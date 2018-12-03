package question_6;

import java.util.Random;

import channel.Channel;
import channel.ChannelImpl;

public class Main {
	
	private static final int LIMITE_NUMEROS = 5;
	private static Random gerador;
	private static Channel channel = new ChannelImpl(LIMITE_NUMEROS);
	
	private static Runnable t1 = new Runnable() {
		
		@Override
		public void run() {
			gerador = new Random();
			//o limite do for indica por quanto tempo (quantidade de iteracoes)
			//se deseja rodar o canal
			for (int i = 0; i < 10; i++) { 
				int number_generated = gerador.nextInt();
				String number = Integer.toString(number_generated);
				channel.putMessage(number);
			}
		}
	};
	
	private static Runnable t2 = new Runnable() {
		
		@Override
		public void run() {
			for (int i = 0; i < 10; i++) {
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
		thread2.start();
		
		thread1.join();
		thread2.join();
		
		System.out.println("Programa finalizado");
	}

}
