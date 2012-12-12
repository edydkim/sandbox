package test;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.Map;

public class Sample {
	public final static String PIGG_CHANNEL = "piggchannel";
	
	private String category;
	private Map<String, Integer> m; 
	
	public Sample() {
		m = new HashMap<String, Integer>();
		m.put("1", 20);
		m.put("2", 30);
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	@Json(name="M")
	public Map<String, Integer> getM() {
		return m;
	}

	public void setM(Map<String, Integer> m) {
		this.m = m;
	}
	
	public void test(String type) throws SecurityException, NoSuchFieldException {
		Field field = Sample.class.getDeclaredField(type);
		ParameterizedType pt = (ParameterizedType)field.getGenericType();
		System.out.println("GenericType is testing OK");
	}
}
