package wfsnrnet.util;

import java.util.ArrayList;
import java.util.List;

public class ListUtils {
	
	public static <T> List<T> flat(List<? extends List<? extends T>> lists)
	{	
		List<T> v = new ArrayList<T>();		
		for (List<? extends T> list : lists)
			v.addAll(list);
		return v;
	}
	
	public static String[] toArray_String(List<String> list){
		String[] ret = new String[list.size()];
		int i=0;
		for (String el : list){
			ret[i++]=el;
		}
		return ret;
	}
	
	public static double[] toArray_Double(List<Double> list){
		double[] ret = new double[list.size()];
		int i=0;
		for (Double el : list){
			ret[i++]=el.doubleValue();
		}
		return ret;
	}
	
	public static int[] toArray_Integer(List<Integer> list){
		int[] ret = new int[list.size()];
		int i=0;
		for (Integer el : list){
			ret[i++]=el.intValue();
		}
		return ret;
	}
	
}