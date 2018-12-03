package question_5;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {

	// Para verificar o tempo de operacoes com base no tamanho do pool	
	// de threads, basta mudar o valor desta constante e executar. 
	public final static int THREAD_POOL_SIZE = 4;
	public final static int ENTRIES_SIZE = 100000;

	public static Map<String, Integer> concurrentHashMapObject = null;
	public static Map<String, Integer> synchronizedMapObject = null;
	public static List<Integer> copyOnWriteListObject = null;
	public static List<Integer> synchronizedListObject = null;

	public static void main(String[] args) throws InterruptedException {

		// Teste com objeto do tipo ConcurrentHashMap
		concurrentHashMapObject = new ConcurrentHashMap<String, Integer>();
		performTestMap(concurrentHashMapObject);
		//
		// // Teste com objeto do tipo SynchronizedMap
		synchronizedMapObject = Collections.synchronizedMap(new HashMap<String, Integer>());
		performTestMap(synchronizedMapObject);

		// Teste com objeto do tipo CopyOnWriteList
		copyOnWriteListObject = new CopyOnWriteArrayList<>();
		performTestList(copyOnWriteListObject);

		// Teste com objeto do tipo SynchronizedList
		synchronizedListObject = Collections.synchronizedList(new ArrayList<Integer>());
		performTestList(synchronizedListObject);

	}

	public static void performTestMap(final Map<String, Integer> mapaEstudado) throws InterruptedException {

		System.out.println("Teste iniciado para: " + mapaEstudado.getClass() + ", com pool de threads de tamanho "
				+ THREAD_POOL_SIZE + ".");
		long tempoMedio = 0;
		for (int i = 0; i < THREAD_POOL_SIZE; i++) {

			long tempoInicial = System.nanoTime();

			// Criacao das threads...
			ExecutorService executorServer = Executors.newFixedThreadPool(THREAD_POOL_SIZE);

			for (int j = 0; j < THREAD_POOL_SIZE; j++) {
				executorServer.execute(new Runnable() {
					@Override
					public void run() {

						for (int i = 0; i < ENTRIES_SIZE; i++) {
							Integer numeroQualquer = (int) Math.ceil(Math.random() * 550000);

							// Insere o valor...
							mapaEstudado.put(String.valueOf(numeroQualquer), numeroQualquer);

							// Recupera o valor (nao utilizamos aqui, apenas para fins de analise de tempo
							// de operacao).
							Integer crunchifyValue = mapaEstudado.get(String.valueOf(numeroQualquer));

						}
					}
				});
			}

			// Inicia um encerramento ordenado no qual as tarefas enviadas anteriormente são
			// executadas, mas nenhuma nova tarefa será aceita. A invocação não tem efeito
			// adicional se já estiver encerrada. Este método não espera por tarefas
			// submetidas anteriormente para concluir a execução. Use o "awaitTermination"
			// para fazer isso.
			executorServer.shutdown();

			// Bloqueia até que todas as tarefas tenham concluído a execução após uma
			// solicitação de desligamento ou o tempo limite ocorra ou o encadeamento atual
			// seja interrompido, o que ocorrer primeiro.
			executorServer.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);

			long tempoFinal = System.nanoTime();
			long tempoTotal = (tempoFinal - tempoInicial) / 1000000L;
			tempoMedio += tempoTotal;

			System.out.println("100 mil entradas adicionadas/recuperadas em \"" + tempoTotal + " ms\".");
		}
		System.out.println("Para " + mapaEstudado.getClass() + ", o tempo total foi de \"" + tempoMedio
				+ " ms\" e o tempo medio de \"" + tempoMedio / THREAD_POOL_SIZE + " ms\".\n");
	}

	public static void performTestList(final List<Integer> listaEstudada) throws InterruptedException {

		System.out.println("Teste iniciado para: " + listaEstudada.getClass() + ", com pool de threads de tamanho "
				+ THREAD_POOL_SIZE + ".");
		long tempoMedio = 0;
		for (int i = 0; i < THREAD_POOL_SIZE; i++) {

			long tempoInicial = System.nanoTime();

			// Criacao das threads...
			ExecutorService executorServer = Executors.newFixedThreadPool(THREAD_POOL_SIZE);

			for (int j = 0; j < THREAD_POOL_SIZE; j++) {
				executorServer.execute(new Runnable() {
					@Override
					public void run() {

						for (int i = 0; i < ENTRIES_SIZE; i++) {
							Integer numeroQualquer = (int) Math.ceil(Math.random() * 550000);

							// Insere o valor...
							listaEstudada.add(i, numeroQualquer);

							// Recupera o valor (nao utilizamos aqui, apenas para fins de analise de tempo
							// de operacao).
							Integer crunchifyValue = listaEstudada.get(i);

						}
					}
				});
			}

			// Inicia um encerramento ordenado no qual as tarefas enviadas anteriormente são
			// executadas, mas nenhuma nova tarefa será aceita. A invocação não tem efeito
			// adicional se já estiver encerrada. Este método não espera por tarefas
			// submetidas anteriormente para concluir a execução. Use o "awaitTermination"
			// para fazer isso.
			executorServer.shutdown();

			// Bloqueia até que todas as tarefas tenham concluído a execução após uma
			// solicitação de desligamento ou o tempo limite ocorra ou o encadeamento atual
			// seja interrompido, o que ocorrer primeiro.
			executorServer.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);

			long tempoFinal = System.nanoTime();
			long tempoTotal = (tempoFinal - tempoInicial) / 1000000L;
			tempoMedio += tempoTotal;

			System.out.println("100 mil entradas adicionadas/recuperadas em \"" + tempoTotal + " ms\".");
		}
		System.out.println("Para " + listaEstudada.getClass() + ", o tempo total foi de \"" + tempoMedio
				+ " ms\" e o tempo medio de \"" + tempoMedio / THREAD_POOL_SIZE + " ms\".\n");

	}

}
