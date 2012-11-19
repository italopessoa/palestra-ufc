package br.com.ufc.palestraufc.service.model;

import java.util.ArrayList;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;


@Entity
public class Lecture {

	@Id
	@GeneratedValue
	private int id;
	
	private String title;

	private String date;

	private String time;
	// private byte[] imagem;

	private ArrayList<String> authors;

	private boolean favorite = false;

	private String content = "contet";

	private int imageId = 0;

	public Lecture() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Lecture(String title, int imageId) {
		this.title = title;
		this.imageId = imageId;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	// public byte[] getImagem() {
	// return imagem;
	// }
	//
	// public void setImagem(byte[] imagem) {
	// this.imagem = imagem;
	// }

	public ArrayList<String> getAuthors() {
		return authors;
	}

	public void setAuthors(ArrayList<String> authors) {
		this.authors = authors;
	}

	public boolean isFavorite() {
		return favorite;
	}

	public void setFavorite(boolean favorite) {
		this.favorite = favorite;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public boolean equals(Object o) {
		if (o != null && ((Lecture) o).getId() == this.getId()) {
			return true;
		}

		return false;
	}

	public int getImageId() {
		return imageId;
	}

	public void setImageId(int imageId) {
		this.imageId = imageId;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	@Override
	public String toString() {
		return "id=" + "\"" + id + "\"" + " title=" + "\"" + title + "\""
				+ " date=" + "\"" + date + "\"" + " time=" + "\"" + time + "\""
				+ " authors=" + "\"" + authors + "\"" + " favorite=" + "\""
				+ favorite + "\"" + " content=" + "\"" + content + "\""
				+ " imageId=" + "\"" + imageId + "\"";
	}

}
