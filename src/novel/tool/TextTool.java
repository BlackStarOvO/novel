package novel.tool;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 文本处理工具类,简化封装文本操作
 * 
 * @author 22442
 *
 */
public class TextTool {

	/**
	 * 找到最大值和最小值
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
	 * 查询中心人物和其他人物之间的距离
	 * 
	 * @param list 小说内容
	 * @param center 中心人物
	 * @param persons 人物数组
	 * @return 中心人物和人物数组之间发生关系的次数
	 */
	public static Map<String, Integer> relationStatistic(
			List<String> list, String center, 
			Set<String> persons, int line) 
	{
		Map<String, Integer> map = new HashMap<>();
		/*
		 * 在line个字符中,如果同时存在中心人物和集合人物就判定他们之间存在了一条联系
		 * 联系越多就说明人物关系越紧密
		 */
		// 初始化map
		int index = 0;
		int[] arr = new int[persons.size()];
		for(String person : persons) {
			arr[index] = 0;
			// 向map里面添加数据
			map.put(person, index++); // 开始关系是0条
		}
		// 遍历list,添加人物之间的关系
		for(String str : list) {
			int count = str.length() / line; // str的长度是line的count倍
			// 截取str的前line个字符,判定人物之间的关系
			for(int i=0; i<=count; i++) {
				int start = i * line; // 开始截取的位置
				String s;
				if(i == count)
					s = str.substring(count * line);
				else
					s = str.substring(start, start + line);
				// 如果这句话里面含有center,就遍历map寻找人物集合中是否有人物在这句话里面
				if(s.contains(center)) {
					// 遍历map
					for(String key : map.keySet()) {
						if(s.contains(key)) {
							int value = map.get(key); // 旧的值
							arr[value] ++; // 这样搞快些
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
	 * 统计字符串中关键词的密度
	 * 
	 * @param list 小说内容
	 * @param key 关键人物名称
	 * @param line 一句话包含的字符数
	 * @return 关键人物的活跃密度集合
	 */
	public static List<Boolean> densityStatistic(
			List<String> list, 
			String key, 
			int line) 
	{
		// 需要频繁插入,所以使用链表
		List<Boolean> blist = new LinkedList<Boolean>();
		/*
		 * 按照美国投票法计算,一段话中存在关键词就说明整段话是活跃的，每xxx个字计算为一段话
		 */
		for(String str : list) {
			int count = str.length() / line;
			// 将字符串化为xxx的整数倍
			if(str.length() % line != 0)
				str = str.substring(0, line * count);
			// 遍历计算等级
			boolean flag = false;
			int index = 0;
			while(index < count) {
				// 获xxx个字符
				int start = index * line;
				String s = str.substring(start, start + line);
				// 判断这句话是否是活跃的
				if(s.contains(key)) flag = true;
				else flag = false;
				// 加入到列表中
				blist.add(flag);
				index ++;
			}
		}
		return blist;
	}

	/**
	 * 统计列表中关键词出现的次数
	 * 
	 * @param list
	 * @param key
	 * @return
	 */
	public static int wordStatistic(List<String> list, String key) {
		int count = 0;
		// 设置正则表达式
		Pattern p = Pattern.compile(key);
		// 循环遍历list
		for (String s : list) {
			// 使用正则表达式查询字符串中关键词的个数
			Matcher m = p.matcher(s);
			while (m.find())
				count++;
		}
		return count;
	}
}
