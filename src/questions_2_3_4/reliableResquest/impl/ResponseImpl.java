package impl;

import interfaces.Response;

public class ResponseImpl implements Response {

	private String resposta;
	
	public ResponseImpl() { 
		this.resposta = null;
	}

	@Override
	public synchronized String getResponse() {
		return this.resposta;
	}

	@Override
	public synchronized void setResponse(String novaResposta) {
		this.resposta = novaResposta;
	}
	
	
}
