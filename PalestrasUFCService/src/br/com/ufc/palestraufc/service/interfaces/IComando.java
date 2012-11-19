package br.com.ufc.palestraufc.service.interfaces;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface IComando {
	public String executa(HttpServletRequest request,HttpServletResponse response);
}
