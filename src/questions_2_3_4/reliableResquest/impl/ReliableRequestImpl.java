package impl;

import java.util.Scanner;

import interfaces.Request;
import interfaces.Response;

public class ReliableRequestImpl implements Request {

    private static Response response = new ResponseImpl();
    
    private static final String[] mirrors = {"mirror1.com", "mirror2.br", "mirror3.edu"};
    private final int MIRROR_1 = 0;
    private final int MIRROR_2 = 1;
    private final int MIRROR_3 = 2;
    private final int SLEEP_2 = 2;
    private final int SLEEP_3000 = 3000;
    private final int SLEEP_30000 = 30000;
    
    private Thread thread1;
    private Thread thread2;
    private Thread thread3;
	private volatile boolean stop = false;
	

    public ReliableRequestImpl() { }

    @Override
    public String request(String nomeServidor) {
        try {
            if (nomeServidor.equals(mirrors[MIRROR_1])) {
                Thread.sleep(20);
            }
            else if (nomeServidor.equals(mirrors[MIRROR_2]))
                Thread.sleep(20);
            else if (nomeServidor.equals(mirrors[MIRROR_3]))
                Thread.sleep(20);
        } catch (InterruptedException e) {
        }

        return nomeServidor;
    }

    private synchronized void setResponse(String novoValor, Thread thread1, Thread thread2) {
        if (response.getResponse() == null) {
            thread1.interrupt();
            thread2.interrupt();
            response.setResponse(novoValor);
        }
    }
    
    // Questão 2
    
    @Override
    public String reliableRequest() {

        thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                String response = request(mirrors[MIRROR_1]);
                setResponse(response, thread2, thread3);
            }
        });
        thread1.start();

        thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                String response = request(mirrors[MIRROR_2]);
                setResponse(response, thread1, thread3);
            }
        });
        thread2.start();

        thread3 = new Thread(new Runnable() {
            @Override
            public void run() {
                String response = request(mirrors[MIRROR_3]);
                setResponse(response, thread1, thread2);
            }
        });
        thread3.start();
        
        try {
			thread1.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
        
    	return response.getResponse();
    }
    
    // Questão 3

    public String reliableRequestTime() throws Exception {
        thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                String response = request(mirrors[MIRROR_1]);
                setResponse(response, thread2, thread3);
            }
        });
        thread1.start();

        thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                String response = request(mirrors[MIRROR_2]);
                setResponse(response, thread1, thread3);
            }
        });
        thread2.start();

        thread3 = new Thread(new Runnable() {
            @Override
            public void run() {
                String response = request(mirrors[MIRROR_3]);
                setResponse(response, thread1, thread2);
            }
        });
        thread3.start();

        try {
            thread1.join(SLEEP_2 * 1000);
        } catch (InterruptedException e) {
        }

        if (response.getResponse() == null) {
            throw new Exception("A resposta demorou mais que 2 segundos...");
        }

        return response.getResponse();
    }
    
    // Questão 4

    public void reliableRequestEvent() {
        Thread entradaUsuario = new Thread(new Runnable() {
            @Override
            public void run() {
                Scanner sc = new Scanner(System.in);

                while (!stop)
                    stop = sc.nextLine().equals("S");
            }
        });

        Thread execucaoRespostaUsuario = new Thread(new Runnable() {

            public void run() {
            	System.out.println("Para parar, digite \"s\"!");
				System.lineSeparator();
                while (!stop) {
                    try {
                        System.out.println(reliableRequestTime());
                    } catch (Exception e) {
                        e.printStackTrace();
                        stop = true;
                    }
                }
            }

        });

        execucaoRespostaUsuario.start();
        entradaUsuario.start();
    }

    public static void main(String[] args) throws InterruptedException {
        ReliableRequestImpl requisicao = new ReliableRequestImpl();

        try {
			//System.out.println("Mirror used: " + requisicao.reliableRequestTime());
    		String x = requisicao.reliableRequest();
    		System.out.println("Mirror utilizado: " + x);
    		
    		x = requisicao.reliableRequest();
    		System.out.println("Mirror utilizado: " + x);
    		
    		x = requisicao.reliableRequest();
    		System.out.println("Mirror utilizado: " + x);
    		
		
//		resposta.reliableRequestEvent();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
