package br.com.ufc.palestraufc.service.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import br.com.ufc.palestraufc.service.DAO.LectureDao;
import br.com.ufc.palestraufc.service.model.Lecture;

public class XmlUtil {
	
	public void gerarXML() {
		Path path = Paths.get("c:/Users/Filipe/workspace/PalestrasUFCService/lectures.xml");

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
