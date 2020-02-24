package novel.tool;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * �ı���������,�򻯷�װ�ı�����
 * 
 * @author 22442
 *
 */
public class TextTool {

	/**
	 * �ҵ����ֵ����Сֵ
	 * @param map
	 * @return
	 */
	public static int[] findMaxMin(Map<String, Integer> map) {
		int max = 0;
		int min = Integer.MAX_VALUE;
		for(int value : map.values()) {
			if(max < value) max = value;
			if(min > value) min = value;
		}
		int[] arr = {max, min};
		return arr;
	}
	
	/**
	 * ��ѯ�����������������֮��ľ���
	 * 
	 * @param list С˵����
	 * @param center ��������
	 * @param persons ��������
	 * @return �����������������֮�䷢����ϵ�Ĵ���
	 */
	public static Map<String, Integer> relationStatistic(
			List<String> list, String center, 
			Set<String> persons, int line) 
	{
		Map<String, Integer> map = new HashMap<>();
		/*
		 * ��line���ַ���,���ͬʱ������������ͼ���������ж�����֮�������һ����ϵ
		 * ��ϵԽ���˵�������ϵԽ����
		 */
		// ��ʼ��map
		int index = 0;
		int[] arr = new int[persons.size()];
		for(String person : persons) {
			arr[index] = 0;
			// ��map�����������
			map.put(person, index++); // ��ʼ��ϵ��0��
		}
		// ����list,�������֮��Ĺ�ϵ
		for(String str : list) {
			int count = str.length() / line; // str�ĳ�����line��count��
			// ��ȡstr��ǰline���ַ�,�ж�����֮��Ĺ�ϵ
			for(int i=0; i<=count; i++) {
				int start = i * line; // ��ʼ��ȡ��λ��
				String s;
				if(i == count)
					s = str.substring(count * line);
				else
					s = str.substring(start, start + line);
				// �����仰���溬��center,�ͱ���mapѰ�����Ｏ�����Ƿ�����������仰����
				if(s.contains(center)) {
					// ����map
					for(String key : map.keySet()) {
						if(s.contains(key)) {
							int value = map.get(key); // �ɵ�ֵ
							arr[value] ++; // �������Щ
						}
					}
				}
			}
		}
		index = 0;
		for(String person : persons) {
			map.replace(person, arr[index++]); 
		}
		return map;
	}

	/**
	 * ͳ���ַ����йؼ��ʵ��ܶ�
	 * 
	 * @param list С˵����
	 * @param key �ؼ���������
	 * @param line һ�仰�������ַ���
	 * @return �ؼ�����Ļ�Ծ�ܶȼ���
	 */
	public static List<Boolean> densityStatistic(
			List<String> list, 
			String key, 
			int line) 
	{
		// ��ҪƵ������,����ʹ������
		List<Boolean> blist = new LinkedList<Boolean>();
		/*
		 * ��������ͶƱ������,һ�λ��д��ڹؼ��ʾ�˵�����λ��ǻ�Ծ�ģ�ÿxxx���ּ���Ϊһ�λ�
		 */
		for(String str : list) {
			int count = str.length() / line;
			// ���ַ�����Ϊxxx��������
			if(str.length() % line != 0)
				str = str.substring(0, line * count);
			// ��������ȼ�
			boolean flag = false;
			int index = 0;
			while(index < count) {
				// ��xxx���ַ�
				int start = index * line;
				String s = str.substring(start, start + line);
				// �ж���仰�Ƿ��ǻ�Ծ��
				if(s.contains(key)) flag = true;
				else flag = false;
				// ���뵽�б���
				blist.add(flag);
				index ++;
			}
		}
		return blist;
	}

	/**
	 * ͳ���б��йؼ��ʳ��ֵĴ���
	 * 
	 * @param list
	 * @param key
	 * @return
	 */
	public static int wordStatistic(List<String> list, String key) {
		int count = 0;
		// ����������ʽ
		Pattern p = Pattern.compile(key);
		// ѭ������list
		for (String s : list) {
			// ʹ��������ʽ��ѯ�ַ����йؼ��ʵĸ���
			Matcher m = p.matcher(s);
			while (m.find())
				count++;
		}
		return count;
	}
}
