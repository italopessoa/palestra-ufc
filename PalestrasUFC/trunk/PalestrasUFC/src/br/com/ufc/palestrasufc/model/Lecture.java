package br.com.ufc.palestrasufc.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

import br.com.ufc.palestrasufc.xml.PropertyValuesReader;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.misc.BaseDaoEnabled;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class Lecture extends BaseDaoEnabled<Lecture, Integer> implements PropertyValuesReader, Serializable {

	private static final long serialVersionUID = -1228118764362062737L;

	@DatabaseField(generatedId = true)
	private int id;

	@DatabaseField
	private String title;

	@DatabaseField(dataType = DataType.STRING)
	private String date;

	@DatabaseField(dataType = DataType.BYTE_ARRAY)
	private byte[] imagem;

	@DatabaseField(dataType = DataType.SERIALIZABLE)
	private ArrayList<String> authors;

	@DatabaseField(columnName="favorite", dataType=DataType.BOOLEAN)
	private boolean favorite;
	
	@DatabaseField(dataType = DataType.LONG_STRING)
	private String content;

	private int imageId;
	
	public Lecture() {
	}

	public Lecture(String title, int imageId) {
		this.title=title;
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

	public byte[] getImagem() {
		return imagem;
	}

	public void setImagem(byte[] imagem) {
		this.imagem = imagem;
	}

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

	@Override
	public void readPropertyValues(Map<String, String> map) {

		if (map.containsKey("id") && !"".equalsIgnoreCase(map.get("id")) ) {
			this.setId(Integer.parseInt(map.get("id")));
		}
		if (map.containsKey("title") && !"".equalsIgnoreCase(map.get("title")) ) {
			this.setTitle(map.get("title"));
		}
		if (map.containsKey("isFavorite") && !"".equalsIgnoreCase(map.get("isFavorite")) ) {
			this.setFavorite(Boolean.parseBoolean(map.get("isFavorite")));
		}
		if (map.containsKey("authors") && !"".equalsIgnoreCase(map.get("authors")) ) {
			String[] temp = map.get("authors").split(",");
			ArrayList<String> authors = new ArrayList<String>();
			for (String string : temp) {
				authors.add(string);
			}
			this.setAuthors(authors);
		}
		if (map.containsKey("date") && !"".equalsIgnoreCase(map.get("date")) ) {
			this.setDate(map.get("date"));
		}
		if (map.containsKey("content") && !"".equalsIgnoreCase(map.get("content")) ) {
			this.setContent(map.get("content"));
		}
	}
}