package src;


import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import interfaces.Request;

public class ReliableRequestImplExecutor implements Request {

	private ExecutorService executor = Executors.newFixedThreadPool(3);
	
	// O valor "stop" será compartilhado para todas as threads de forma atômica
	private volatile boolean stop = false;
	
	private static final String[] mirrors = {"mirror1.com", "mirror2.br", "mirror3.edu"};
	private final int MIRROR_1 = 0;
	private final int MIRROR_2 = 1;
	private final int MIRROR_3 = 2;
	private final int SLEEP_2000 = 2000;
	private final int SLEEP_30000 = 30000;
	
	public ReliableRequestImplExecutor() { }
	
	@Override
	public String request(String nomeServidor) {
		
		try {
			if (nomeServidor.equals(mirrors[MIRROR_1])) {
				Thread.sleep(this.SLEEP_30000);
			}
			else if (nomeServidor.equals(mirrors[MIRROR_2]))
				Thread.sleep(this.SLEEP_2000);
			else if (nomeServidor.equals(mirrors[MIRROR_3]))
				Thread.sleep(this.SLEEP_2000);
		} catch (InterruptedException e) { }
		
		return nomeServidor;
	}
	
	@Override
	public String reliableRequest() {

		List<Callable<String>> tasks = createRequests();

		try {
			String result = executor.invokeAny(tasks);
			executor.shutdown();
			return result;
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	// Questão 3
	
	private List<Callable<String>> createRequests() {
		
		Callable<String> request1 = () -> {
			return request(mirrors[MIRROR_1]);
		};
		Callable<String> request2 = () -> {
			return request(mirrors[MIRROR_2]);
		};
		Callable<String> request3 = () -> {
			return request(mirrors[MIRROR_3]);
		};

		List<Callable<String>> tasks = new ArrayList<>();
		tasks.add(request1);
		tasks.add(request2);
		tasks.add(request3);
		return tasks;
	}
	
	public String reliableRequestTime() throws Exception {
		
		List<Callable<String>> tasks = createRequests();
		try {
			String result = executor.invokeAny(tasks, this.SLEEP_2000, TimeUnit.MILLISECONDS);
			executor.shutdown();
			return result;
		} catch (InterruptedException | ExecutionException | TimeoutException e) {
			executor.shutdown();
			System.out.println("A mensagem => " + e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

	// Questão 4
	
	public void reliableRequestEvent() {
		
		Thread entradaUsuario = new Thread(new Runnable() {

			@Override
			public void run() {
				Scanner sc = new Scanner(System.in);
				
				while (!stop)
					stop = sc.nextLine().equalsIgnoreCase("S");
				
				sc.close();

			}
		});
		
		Thread executaResposta = new Thread(new Runnable() {

			@Override
			public void run() {
				
				System.out.println("Para parar, digite \"s\"!");
				System.lineSeparator();
				
				while (!stop) {
					try {
						System.out.println(reliableRequestTime());
						executor = Executors.newFixedThreadPool(3);
					} catch (Exception e) {
						System.out.println("A mensagem => " + e.getMessage());
						e.printStackTrace();
					}
				}
			}
			
		});
		executaResposta.start();
		entradaUsuario.start();
	}

	public static void main(String[] args) throws InterruptedException {
		
		ReliableRequestImplExecutor requisicao = new ReliableRequestImplExecutor();
		
		try {
			//System.out.println("Mirror used: " + requisicao.reliableRequestTime());
				System.out.println("Mirror utilizado: " + requisicao.reliableRequest());
			
//			resposta.reliableRequestEvent();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
