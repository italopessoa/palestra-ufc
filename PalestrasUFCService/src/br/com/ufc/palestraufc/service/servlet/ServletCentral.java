package br.com.ufc.palestraufc.service.servlet;

import java.io.IOException;
import java.util.Date;
import java.util.Hashtable;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.ufc.palestraufc.service.comandos.cmdAdicioanrPalestra;
import br.com.ufc.palestraufc.service.comandos.cmdEditarPalestra;
import br.com.ufc.palestraufc.service.comandos.cmdExcluirPalestra;
import br.com.ufc.palestraufc.service.comandos.cmdListarPalestras;
import br.com.ufc.palestraufc.service.comandos.cmdSalvarPalestraEditada;
import br.com.ufc.palestraufc.service.interfaces.IComando;

/**
 * Servlet implementation class ServletCentral
 */
public class ServletCentral extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ServletCentral() {
		super();
		// TODO Auto-generated constructor stub
	}

	private Hashtable<String, IComando> comandos;

	protected void procesRequest(HttpServletRequest request,
			HttpServletResponse response) throws IOException {

		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		String cmd = request.getParameter("cmd");

		IComando comando = (IComando) comandos.get(cmd);

		try {
			String tela = comando.executa(request, response);
			if (tela != null && !tela.trim().equals("")) {
				if (true) {
                    System.out.print("Tela:" + tela + " - ");
                    System.out.print("IP Máquima" + request.getRemoteAddr() + " - ");
                    System.out.println("Hora" + new Date());
                }
				
				response.sendRedirect(request.getContextPath() + tela);
			}
		} catch (Exception e) {
			response.sendRedirect(request.getContextPath() + "/Index.jsp");
			e.printStackTrace();
		}
	}

	public void init() {
		IComando cmdo;
		
		comandos = new Hashtable<String, IComando>();
		cmdo = new cmdAdicioanrPalestra();
		comandos.put("cmdCadastrarPalestra", cmdo);
		
		cmdo = new cmdListarPalestras();
		comandos.put("cmdListarPalestras", cmdo);
		
		cmdo = new cmdEditarPalestra();
		comandos.put("cmdEditarPalestra", cmdo);
		
		cmdo = new cmdExcluirPalestra();
		comandos.put("cmdExcluirPalestra", cmdo);

		cmdo = new cmdSalvarPalestraEditada();
		comandos.put("cmdSalvarPalestraEditada", cmdo);
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		procesRequest(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		procesRequest(request, response);
	}

}
