package br.com.ufc.palestraufc.service.DAO;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import br.com.ufc.palestraufc.service.model.Lecture;
import br.com.ufc.palestraufc.service.util.HibernateUtil;

public class LectureDao {

	public LectureDao() {

	}

	public void salvarLecture(Lecture Lecture) {
		Session session = HibernateUtil.getSession();
		Transaction t = session.beginTransaction();
		try {
			session.save(Lecture);
			t.commit();
		} catch (Exception e) {
			t.rollback();
		} finally {
			session.close();
		}
	}

	public void atualizarLecture(Lecture Lecture) {
		Session session = HibernateUtil.getSession();
		Transaction t = session.beginTransaction();
		try {
			session.update(Lecture);
			t.commit();
		} catch (Exception e) {
			t.rollback();
		} finally {
			session.close();
		}
	}

	public void removerLecture(Lecture Lecture) {
		Session session = HibernateUtil.getSession();
		Transaction t = session.beginTransaction();
		try {
			session.delete(Lecture);
			t.commit();
		} catch (Exception e) {
			t.rollback();
		} finally {
			session.close();
		}
	}

	public List<Lecture> listarLectures() {
		Session session = HibernateUtil.getSession();
		try {
			return session.createCriteria(Lecture.class).list();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return null;
	}

	public Lecture carregarLecture(int id) {
		Session session = HibernateUtil.getSession();
		try {
			return (Lecture) session.get(Lecture.class, id);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return null;
	}
}
