package br.com.ufc.palestraufc.service.teste;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import br.com.ufc.palestraufc.service.DAO.LectureDao;
import br.com.ufc.palestraufc.service.model.Lecture;

public class testes {

	public static void main(String[] args) {
		Lecture l = new Lecture();

		ArrayList<String> a = new ArrayList<String>();

		a.add("a1");
		a.add("a2");

		l.setId(1);
		l.setTitle("title");
		l.setFavorite(false);
		l.setTime("1:00");
		l.setAuthors(a);
		l.setDate("date");
		l.setContent("content");

		Path path = Paths
				.get("c:/Users/Filipe/workspace/PalestrasUFCService/lectures.xml");

		try {
			LectureDao lectureDao = new LectureDao();
			List<Lecture> lectures = lectureDao.listarLectures();
			String s = "<?xml version=\"1.0\" encoding=\"UTF-8\"?> \n<root> \n";
			for (Lecture lecture : lectures) {
				s += "		<lecture " + lecture.toString() + "/>\n";
			}
			s += "</root>";
			Files.write(path, s.getBytes());// cria limpa e escreve
			String r = new String(Files.readAllBytes(path));
			System.out.println(r);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
