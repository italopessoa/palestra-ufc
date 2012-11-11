package br.com.ufc.palestrasufc.xml;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public class PostProcessor<T extends PropertyValuesReader> {
	private ArrayList<T> result;
	private Class<T> elementType;

	public PostProcessor(Class<T> elementType) {
		this.elementType = elementType;
	}

	public Collection<T> getResult() {
		return result;
	}

	public void parser(Collection<Map<String, String>> data) {
		result = new ArrayList<T>();
		for (Map<String, String> map : data) {
			try {
				T t = elementType.newInstance();
				t.readPropertyValues(map);
				result.add(t);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
