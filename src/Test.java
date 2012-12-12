import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
//		Matcher m = Pattern.compile("[0-9]{2,}$").matcher("piggchannel_ch1_001");
//		while (m.find())
//			System.out.println(Integer.parseInt(m.group()));
//		
//		Queue<String> queue = new LinkedList<String>();
//		queue.clear();
//		List<String> lc = new ArrayList<String>();
//		Iterator<String> it = queue.iterator();
//		
//		while (it.hasNext())
//			lc.add(it.next());
//
//		queue.clear();
//		queue.clear();
//		
//		System.out.println(lc.toString());
//		
//		lc.add("test");
//		lc.remove("test");
//		System.out.println(lc.size());
		
//		long l = 0L;
//		l = l - 10L;
//		System.out.println(l);
		
		Date today = getDay(getDayKey(System.currentTimeMillis()));
		Calendar ca = Calendar.getInstance();
		ca.set(2012, 5, 29, 0, 0, 0);
		Date anotherDay = getDay(getDayKey(ca.getTimeInMillis()));
		long now = System.currentTimeMillis();	// <- Calendar.getInstance().getTimeInMillis();
		long diff = now - ca.getTimeInMillis();
		System.out.println(diff / 60 / 1000L);
		System.out.println("Big Test : " + ca.getTime() + ", " + today + ", " + anotherDay + ", " + ca.getTime().compareTo(today));
		System.out.println("Big Test : " + ca.getTime() + ", " + today + ", " + anotherDay + ", " + anotherDay.compareTo(today));
		ca.add(Calendar.DATE, 7);
		System.out.println("Big Test : " + ca.getTime());
		System.out.println("Big Test : " + Calendar.getInstance().getTime());
		
		LinkedHashMap lHashMap = new LinkedHashMap();
		lHashMap.put(1, "test1");
		lHashMap.put(1, "test2");
		lHashMap.put(2, "test1");
		System.out.println(lHashMap.get(1));
		System.out.println(lHashMap.get(1));
		
		InnerTest test = new InnerTest();
		test.str = "test";
		test.list.add("4");
		test.list.add("2");
		test.list.add("4");
		test.list.add("3");
		
		int cnt = 0;
		for (String s : test.list) {
			if (s.equals("2"))	test.list.set(cnt, "0");
			cnt++;
		}
		
		for (String s : test.list) {
			System.out.println(test.list.indexOf("4") + "," + s);
		}
		
		Calendar calendar = Calendar.getInstance();
		Calendar calendar2 = Calendar.getInstance();
		calendar.add(Calendar.DATE, 2);
		System.out.println("Compare time : " + calendar.getTime().compareTo(calendar2.getTime()));
		
		List<InnerTest> l = new ArrayList<InnerTest>();
		InnerTest i1 = new InnerTest();
		i1.time = calendar.getTimeInMillis();
		i1.isRecommended = true;
		i1.str = "2";
		i1.isVjArea = false;
		i1.isOnline = true;
		InnerTest i2 = new InnerTest();
		i2.time = calendar2.getTimeInMillis();
		i2.str = "1";
		i2.isVjArea = true;
		i2.isOnline = true;
		InnerTest i3 = new InnerTest();
		i3.time = calendar.getTimeInMillis();
		i3.isVjArea = false;
		i3.isOnline = false;
		i3.str = "3";

		l.add(i1);
		l.add(i2);
		l.add(i3);
		
		l.add(i2);
		l.add(i3);
		l.add(i1);
		
		l.add(i3);
		l.add(i2);
		l.add(i1);
		
		l.add(i1);
		l.add(i3);
		l.add(i2);
		
		l.add(i2);
		l.add(i1);
		l.add(i3);
		
		l.add(i3);
		l.add(i1);
		l.add(i2);
		
		Collections.sort(l, new Comparator<InnerTest>() {
			public int compare(InnerTest c1, InnerTest c2) {
				// <- return c2.isCampaign ? 1 : c1.time - c2.time == 0 ? c2.isRecommended ? 1 : (int) (c1.time - c2.time) : (int) (c1.time - c2.time);
				// <- return c2.isRecommended ? 1 : (int) (c1.time - c2.time);
				return c1.isVjArea ? -1 : c1.isOnline && !c2.isVjArea ? -1 : 1;
			}
		});
		
		// <- InnerTest i4 = i3;
		// <- l.add(i4);
		
		for (int i = 0; i < l.size(); i++) {
			InnerTest it = l.get(i);
			System.out.println("result sort now : " + it.str);
		}
		
		for (InnerTest it : l)
			System.out.println("result sort now : " + it.str);
		
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < 4; i++)
			list.add(null);
		list.set(3, "TEST");
		for (String str : list)
			if (str == null) System.out.println("List Test..");
		
		CharSequence cs = "vj";
		if ("piggchannel_vj_001".toLowerCase().contains(cs))
			System.out.println("CharSequence is passed.");
		
		int page = 1;
		int aNumberOfProgram = 10;
		int fromIndex = page * aNumberOfProgram;
		int max = 50;
		int toIndex = fromIndex + (max / aNumberOfProgram == 0 ? max : max % aNumberOfProgram);
				// (max % aNumberOfProgram == 0 ? aNumberOfProgram : max % aNumberOfProgram);
		System.out.println("fromIndex : " + fromIndex + ", toIndex : " + toIndex);
		if (fromIndex >= 0 && toIndex <= max && fromIndex < toIndex)
			System.out.println("Index Check done..");
		
		for (int i = page * aNumberOfProgram; i < (page + 1) * aNumberOfProgram && i < max; i++) {
			System.out.print(i + " ");
		}
		System.out.println("page count done..");
		
		int seats = 4;
		int bitMask = seats - 1;
		int mySeat = 0;
		for (int i = 0; i < seats; i++)
			System.out.println(mySeat = mySeat + 1 & bitMask);
		
		List<String> ls = new LinkedList<String>();
		ls.add(null);
		ls.add("test2");
		ls.add(null);
		boolean bool = ls.set(0, "test1") == null ? true : false;
		System.out.println("List Test : " + bool + ", " + ls.get(1));
		
		List<Integer> ints = new LinkedList<Integer>();
		System.out.println(ints.size());
		
		String str = "0,11,13,2,1,2";
		String[] strs = str.split(",");
		Arrays.sort(strs, new Comparator<String>() {
			@Override
			public int compare(String arg0, String arg1) {
				return Integer.parseInt(arg1) - Integer.parseInt(arg0);
			}
		});
		
		for (String s : strs)
			System.out.println("order : " + s);
		
		List<String> sl = new LinkedList<String>();
		sl.add("3");
		sl.add(null);
		sl.add(null);
		sl.remove(1);
		
		for (String s : sl)
			System.out.println("linked : " + s);
		
		System.out.println(Integer.parseInt(sl.get(0)) * -1 < 0 ? true : false);
		
		for (int i = 0; i < 15 + 1; i++)
			System.out.println("Count : " + i + "," + i % 5);
		
		Set<String> set = new HashSet<String>();
		set.add("test1");
		set.add("test1");
		set.add("test2");
		set.add("test1");
		if (set.contains("test1")) set.remove("test1");
		Iterator<String> iterator = set.iterator();
		while (iterator.hasNext())
			System.out.println("HashSet value : " + iterator.next());
		
		System.out.println("123_vj".toLowerCase());
		
	}
	
	static class InnerTest {
		public String str = "";
		public long time;
		public boolean b = false;
		public boolean isCampaign = false;
		public boolean isRecommended = false;
		public boolean isVjArea = false;
		public boolean isOnline = false;
		List<String> list = new ArrayList<String>();
	}

	public static int getDayKey(long time) {
		long offset = TimeZone.getDefault().getRawOffset();
		return (int) ((time + offset) / (1000L * 60L * 60L * 24L));
	}
	
	public static Date getDay(int dayKey){
		long time = dayKey * (1000L * 60L * 60L * 24L);
		return new Date(time);
	}
}
