package br.com.ufc.palestraufc.service.comandos;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import br.com.ufc.palestraufc.service.DAO.LectureDao;
import br.com.ufc.palestraufc.service.interfaces.IComando;
import br.com.ufc.palestraufc.service.model.Lecture;

public class cmdEditarPalestra implements IComando {

	@Override
	public String executa(HttpServletRequest request,
			HttpServletResponse response) {

		HttpSession session = request.getSession();

		int id = Integer.parseInt(request.getParameter("id"));

		LectureDao lectureDao = new LectureDao();

		Lecture lecture = new Lecture();

		lecture = lectureDao.carregarLecture(id);

		session.setAttribute("palestraAEditar", lecture);
		return "/editarPalestra.jsp";
	}

}
