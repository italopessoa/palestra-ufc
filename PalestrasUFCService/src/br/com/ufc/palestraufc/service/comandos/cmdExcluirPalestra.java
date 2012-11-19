package br.com.ufc.palestraufc.service.comandos;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import br.com.ufc.palestraufc.service.DAO.LectureDao;
import br.com.ufc.palestraufc.service.interfaces.IComando;
import br.com.ufc.palestraufc.service.model.Lecture;
import br.com.ufc.palestraufc.service.util.XmlUtil;

public class cmdExcluirPalestra implements IComando {

	@Override
	public String executa(HttpServletRequest request,
			HttpServletResponse response) {
		// TODO Auto-generated method stub

		HttpSession session = request.getSession();
		try {
			int id = Integer.parseInt(request.getParameter("id"));

			LectureDao dao = new LectureDao();

			Lecture lecture = dao.carregarLecture(id);

			dao.removerLecture(lecture);
			ArrayList<Lecture> lectures = (ArrayList<Lecture>) dao
					.listarLectures();

			XmlUtil util = new XmlUtil();
			util.gerarXML();

			session.setAttribute("sucesso", "palestra remivoda com sucesso");
			session.setAttribute("palestras", lectures);
			return "/listarPalestras.jsp";
		} catch (Exception e) {
			session.setAttribute("erro", "Erro ao remover Palestra");
			return "/listarPalestras.jsp";
		}
	}

}
