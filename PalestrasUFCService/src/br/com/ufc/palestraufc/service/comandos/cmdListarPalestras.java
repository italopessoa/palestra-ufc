package br.com.ufc.palestraufc.service.comandos;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import br.com.ufc.palestraufc.service.DAO.LectureDao;
import br.com.ufc.palestraufc.service.interfaces.IComando;
import br.com.ufc.palestraufc.service.model.Lecture;

public class cmdListarPalestras implements IComando {

	@Override
	public String executa(HttpServletRequest request,
			HttpServletResponse response) {

		HttpSession session = request.getSession(true);
		
		LectureDao lectureDao = new LectureDao();
		
		List<Lecture> palestras = lectureDao.listarLectures();
		
		session.setAttribute("palestras", palestras);
		return "/listarPalestras.jsp";
	}

}
