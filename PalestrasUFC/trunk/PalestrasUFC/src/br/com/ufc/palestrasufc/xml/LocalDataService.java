package br.com.ufc.palestrasufc.xml;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import android.content.Context;

public class LocalDataService {
	/**
	 * List of xml elements
	 */
	public static ArrayList<Map<String, String>> collection = new ArrayList<Map<String, String>>();
	private Context ctx;

	public LocalDataService(Context ctx) {
		this.ctx = ctx;
	}

	public boolean selectAll(String entity, PostProcessor<?> pp) {
		try {
			InputStream is = ctx.getAssets().open("xml/" + entity + ".xml");

			BufferedReader reader = new BufferedReader(new InputStreamReader(is));
			StringBuilder sb = new StringBuilder();
			String text = "";

			while ((text = reader.readLine()) != null) {
				sb.append(text + "\n");
			}

			text = sb.toString();
			is.close();

			parserProcessResult(text);
			pp.parser(collection);
		} catch (Throwable e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	/**
	 * Parser process result.
	 * 
	 * @param serviceResponse the service xml response
	 */
	private void parserProcessResult(String serviceResponse) throws Throwable {
		collection.clear();
		XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
		factory.setNamespaceAware(true);
		XmlPullParser xpp = factory.newPullParser();
		xpp.setInput(new StringReader(serviceResponse));
		int eventType = xpp.getEventType();
		while (eventType != XmlPullParser.END_DOCUMENT) {
			HashMap<String, String> map = new HashMap<String, String>();

			for (int i = 0; i < xpp.getAttributeCount(); i++) {
				map.put(xpp.getAttributeName(i), xpp.getAttributeValue(i));
			}

			if (!map.isEmpty()) {
				collection.add(map);
			}

			eventType = xpp.next();
		}
	}

}
