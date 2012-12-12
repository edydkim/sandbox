package test;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Test {
	
	/**
	 * @param args
	 * @throws NoSuchFieldException 
	 * @throws SecurityException 
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 * @throws NoSuchMethodException 
	 * @throws InvocationTargetException 
	 */
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
		System.out.println(Test.class.getName());
		
		Map<String, Integer> furnitureCount = new ConcurrentHashMap<String, Integer>();
		furnitureCount.put("test1", 1);
		furnitureCount.put("test2", furnitureCount.get("test2") == null ? 1 : furnitureCount.get("test2").intValue() + 1);
		
		System.out.println(furnitureCount.get("test2"));
		
		List<String> list = new LinkedList<String>();
		for (int i = 0; i < 3; i++)
			list.add(null);
		
		list.set(2, "linkedListTest1");
		list.set(0, list.get(2));
		list.set(2, null);
		list.set(2, "linkedListTest2");
		list.set(1, list.get(2));
		list.set(2, null);
		for (String s : list)
			System.out.println(s);
		
		final int A_NUMBER_OF_HISTORY = 1 << 3;
		final int BIT_MASK = A_NUMBER_OF_HISTORY - 1;
		int pointer = 2;
		pointer = pointer + 1 & BIT_MASK;
		System.out.println("pointer : " + pointer);
		
		Stack<SubTest> stack = new Stack<SubTest>();
		
		stack.push(new SubTest("1"));
		stack.push(new SubTest("2"));
		stack.peek().str = "3";
		stack.remove(0);
		for (SubTest s : stack)
			System.out.println(s.str);
		
		String str = "1";
		System.out.println("Split : " + str.split(",")[0]);
	
		List<String> ls = new ArrayList<String>();
		ls.add("1");
		ls.add("2");
			
		for (String string : ls)
			System.out.println("result : " + string);
		
		System.out.println("index : " + ls.indexOf(null));
		
		list = new LinkedList<String>();
		list.add(0, "test0");
		System.out.println(list.get(0));
		list.add(1, "test1");
		System.out.println(list.get(1));
		
		Map<Integer, Integer> map = new HashMap<Integer, Integer>();
		map.put(2, 1);
		map.put(3, 2);
		System.out.println("map : " + map.toString());
		
		StringBuilder sb = new StringBuilder();
		sb.append("t");
		sb.append(",");
		System.out.println("StringBuilder :" + sb.toString());
		
		String[] vjUsers = ",12,,".split(",");
		System.out.println("vjUsers.length : " + vjUsers.length);
		for (int i = 0; i < vjUsers.length; i++) {
			if (vjUsers[i].length() == 0)	System.out.println("vjUsers[" + i + "] : " + vjUsers[i]);
			else 
				System.out.println("vjUsers[" + i + "] encrypted : " + Integer.parseInt(vjUsers[i]));
		}
		
		Set<String> set = new HashSet<String>();
		set.add("1");
		set.add("2");
		System.out.println("Set : " + set.toString());
		
		String zero = "0";
		for (int i = 1; i < 3; i++)
			zero += "0";
		System.out.println("DecimalFormat : " + new DecimalFormat(zero).format(1));
		
		System.out.println("length : " + "123".split(",").length + ", " + "123,test".split(",")[0]);
		
		List<String> l0 = new LinkedList<String>();
		l0.add("1");
		List<String> l1 = new LinkedList<String>();
		l1.addAll(l0);
		
		System.out.println("hashCode : l0 = " + l0.hashCode() + ", l1 = " + l1.hashCode());
		if (l0.equals(l1) && l0.hashCode() == l1.hashCode())	System.out.println("Same");
		System.out.println("hashCode : l0 = " + l0.get(0).hashCode() + ", l1 = " + l1.get(0).hashCode());
		if (l0.get(0).equals(l1.get(0)) && l0.get(0).hashCode() == l1.get(0).hashCode())	System.out.println("Same");
		
		Map<String, Integer> tagMap = new HashMap<String, Integer>();
		tagMap.put("1", 4);
		tagMap.put("2", 1);
		tagMap.put("3", 2);
		System.out.println("TagMap : " + tagMap.toString());
		
		List<Entry<String, Integer>> entries = new ArrayList<Entry<String, Integer>>(tagMap.entrySet());
		Collections.sort(entries, new Comparator<Entry<String, Integer>>() {
			@Override
			public int compare(Entry<String, Integer> arg0, Entry<String, Integer> arg1) {
				return arg0.getValue().compareTo(arg1.getValue());	// arg1.getValue() - arg0.getValue();
			}
		});
		System.out.println("TagMap : " + entries.toString());
		
		// <- fill();
		
		CharSequence vj = "vj";
		CharSequence vjco = "vjco";
		if ("piggchannel_vjco001_001".contains(vjco)) {
			System.out.println("Contained : vjco");
		}
		if ("piggchannel_vjco001_001".contains(vj))	System.out.println("Contained : vj");
		
		String json = "{}"; // <- "{\"2\":10, \"1\":20}";
		try {
			JSONObject jsonObject = new JSONObject(json);
			JSONArray jsonArray = jsonObject.names();
			Iterator<String> iterator = jsonObject.sortedKeys();
			while (iterator.hasNext()) {
				String next = iterator.next();
				System.out.println("JSON : " + next);
				System.out.println("JSON : " + jsonObject.getString(next));
			}
			System.out.println("JSON sorted : " + jsonObject.toString());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
        String s = "methodName";      
        final StringBuilder result = new StringBuilder(s.length());    
        String words[] = s.split("\\ "); // space found then split it  
        for (int i = 0; i < words.length; i++) {
			if (i > 0)
				result.append(" ");
			result.append(Character.toUpperCase(words[i].charAt(0))).append(words[i].substring(1));
		}
		System.out.println(result); 
		
		Sample sample = new Sample();
		sample.test("m");
		
		Field[] fields = Sample.class.getDeclaredFields();
		for (Field f : fields) {
			System.out.println("Before Field : " + f.getName() + ", isAccessible : " + f.isAccessible() + ", getModifier : " + f.getModifiers());
			if (f.getType().equals(String.class))	continue;
			// <- if (f.getName().equals("category") || Modifier.isStatic(f.getModifiers()) )	continue;
			sample.test(f.getName());
			System.out.println("After Field : " + f.getName());
		}
		
		
		Method method = Sample.class.getDeclaredMethod("getM");
		Json j = method.getAnnotation(Json.class);
		System.out.println("Annotation name : " + j.name());
		
		// <- Field field = Sample.class.getDeclaredField("m");
		// <- ParameterizedType pt = (ParameterizedType)field.getGenericType();
		
		ParameterizedType pt = (ParameterizedType)method.getGenericReturnType();
		
		StringBuilder sb0 = new StringBuilder("{");
		
		/* <-
		List<Entry<String, ?>> e0 = new ArrayList<Entry<String, ?>>(m.entrySet());
		Collections.sort(e0, new Comparator<Entry<String, ?>>(){
		    public int compare(Entry<String, ?> c1, Entry<String, ?> c2){
		    	if (pt.getActualTypeArguments()[1].equals(Integer.class)) {
		    		return c1.getValue() - c2.getValue();
		    	}
		    }
		});
		*/
		
		// <- for (Entry<String, ?> entry : ((Map<String, ?>)field.get(sample)).entrySet()) {
		for (Entry<String, ?> entry : ((Map<String, ?>)method.invoke(sample)).entrySet()) {			
			StringBuilder sb1 = new StringBuilder();
			sb1.append("\"").append(entry.getKey()).append("\"").append(":");
			if (pt.getActualTypeArguments()[1].equals(Integer.class)) {
				sb1.append(entry.getValue());
			}
			sb1.append(",");
			sb0.insert(1, sb1);
		}
		sb0.append("}");
		if (sb0.length() > 2)	sb0.replace(sb0.length() - 2, sb0.length() - 1, "");

		System.out.println("New Json : " + sb0.toString() + ", length : " + sb0.length());
		Map<String, String> map0 = new HashMap<String, String>();
		map0.put("1", "2");
		map0.clear();
		System.out.println("Map cleard : " + map0);
		
	}
	
	static class SubTest {
		public String str;
		
		public SubTest(String str) {
			this.str = str;
		}
	}
	
	private static int maxSubCategory = 11;
	private static int maxAreaCode = 100;
	private static String subCategoryPrefix = "vjco";
	
	private static String makeSubCategory(int num) {
		String zero = "0";
		for (int i = 1; i < pricise(maxSubCategory); i++)
			zero += "0";
		return new DecimalFormat(zero).format(num);
	}
	
	private static String makeAreaCode(int num) {
		String zero = "0";
		for (int i = 1; i < pricise(maxAreaCode); i++)
			zero += "0";
		return new DecimalFormat(zero).format(num);
	}
	
	public static void fill() {
		for (int i = 1; i <= maxSubCategory; i++) {
			for (int j = 1; j <= maxAreaCode; j++) {
				String areaCode = subCategoryPrefix + makeSubCategory(i) + "_" + makeAreaCode(j);
				System.out.println("AreaCode : " + areaCode);
			}
		}
	}
	
	private static int pricise (int num) {
		if (num == 0)	return 0;
		return pricise(num / 10) + 1;
	}
}
