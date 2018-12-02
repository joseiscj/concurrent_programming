package question7;

import java.util.Random;

import channel.Channel;
import channel.ChannelImpl;

public class Main {
	private static final int QUANTIDADE_STRINGS_GERADAS = 50;
	private static Channel channel = new ChannelImpl(QUANTIDADE_STRINGS_GERADAS);
	private static Channel stringsFiltradas = new ChannelImpl(QUANTIDADE_STRINGS_GERADAS);
	
	private static String generateStrings(int length) {
		String alphabet = new String("0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"); // 9
		int n = alphabet.length();

		String result = new String();
		Random r = new Random();

		for (int i = 0; i < length; i++)
			result = result + alphabet.charAt(r.nextInt(n));

		return result;
	}
	
	private static Runnable t1 = new Runnable() {
		
		@Override
		public void run() {
			for (int i = 0; i < 20; i++) {
				String strGenerated = generateStrings(4); //AQUI VC INDICA O TAMANHO DA STRING A SER GERADA
				//System.out.println(strGenerated);
				channel.putMessage(strGenerated);;
			}
		}
	};
	
	private static Runnable t2 = new Runnable() {
		
		@Override
		public void run() {
			for (int i = 0; i < 20; i++) {
				String element = channel.takeMessage();
				if (element.matches("[A-Za-z]+")) {
					//System.out.println("1) " + element);
					stringsFiltradas.putMessage(element);
				}
			}
		}
	};
	
	private static Runnable t3 = new Runnable() {
		
		@Override
		public void run() {
			//int limite = stringsFiltradas.size();
			for (int i = 0; i < 20; i++) {
				String str = stringsFiltradas.takeMessage();
				System.out.println("elemento filtrado: " + str);
			}
		}
	};
	
	public static void main(String[] args) throws InterruptedException {
		Thread thread1 = new Thread(t1);
		Thread thread2 = new Thread(t2);
		Thread thread3 = new Thread(t3);
		
		thread1.start();
		thread2.start();
		thread3.start();
		
		thread1.join();
		thread2.join();
		thread3.join();
		
		System.out.println("Programa finalizado");
	}

}
