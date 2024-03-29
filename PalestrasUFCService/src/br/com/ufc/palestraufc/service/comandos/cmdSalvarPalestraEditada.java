package br.com.ufc.palestraufc.service.comandos;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import br.com.ufc.palestraufc.service.DAO.LectureDao;
import br.com.ufc.palestraufc.service.interfaces.IComando;
import br.com.ufc.palestraufc.service.model.Lecture;
import br.com.ufc.palestraufc.service.util.XmlUtil;

public class cmdSalvarPalestraEditada implements IComando {

	@Override
	public String executa(HttpServletRequest request,
			HttpServletResponse response) {
		
		HttpSession session = request.getSession(true);

		int id = Integer.parseInt(request.getParameter("id"));
		String titulo = request.getParameter("titulo");
		String autor1 = request.getParameter("autor1");
		String autor2 = request.getParameter("autor2");
		String autor3 = request.getParameter("autor3");
		String autor4 = request.getParameter("autor4");
		String autor5 = request.getParameter("autor5");
		String data = request.getParameter("data");
		String hora = request.getParameter("hora");
		

		if (titulo == null || titulo.trim().isEmpty() || autor1 == null
				|| autor1.trim().isEmpty() || autor5 == null
				|| autor5.trim().isEmpty() || data == null
				|| data.trim().isEmpty() || hora == null
				|| hora.trim().isEmpty()) {
			session.setAttribute("erro",
					"Preencha todos os campos obrigatórios.");
			return "/editarPalestra.jsp";
		}

		ArrayList<String> auArr = new ArrayList<String>();
		auArr.add(autor1);
		auArr.add(autor5);

		if (autor2 != null && !autor2.trim().isEmpty()) {
			auArr.add(autor2);
		}
		if (autor3 != null && !autor3.trim().isEmpty()) {
			auArr.add(autor3);
		}
		if (autor4 != null && !autor4.trim().isEmpty()) {
			auArr.add(autor4);
		}

		Lecture palestra = new Lecture();

		palestra.setId(id);
		palestra.setTitle(titulo);
		palestra.setAuthors(auArr);
		palestra.setDate(data);
		palestra.setTime(hora);
		
		LectureDao dao = new LectureDao();
		
		try {
			dao.atualizarLecture(palestra);
			session.setAttribute("palestraAEditar", palestra);
			session.setAttribute("sucesso", "palestra alterada com sucesso");
			
			XmlUtil util = new XmlUtil();
			util.gerarXML();
			
			return "/editarPalestra.jsp"; 
		} catch (Exception e) {
			session.setAttribute("erro",
					"Preencha todos os campos obrigatórios.");
			return "/editarPalestra.jsp";
		}
	}

}
