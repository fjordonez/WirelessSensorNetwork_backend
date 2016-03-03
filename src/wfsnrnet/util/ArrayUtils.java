package wfsnrnet.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ArrayUtils{
	
	public static int sum(int[] a){
		int ret=0;
		for (int i=0;i<a.length;i++)
			ret+=a[i];
		return ret;
	}
	
	public static int[] sum(int[] a, int[] b){
		
		if (a.length != b.length)
			throw new IllegalArgumentException("ArrayUtils::sum Both arrays must be of the same length");
		
		int[] ret = new int[a.length];
		for (int i=0;i<a.length;i++)
			ret[i]=a[i]+b[i];
		return ret;
	}
	
	public static double[] sum(double[] a, double[] b){
		
		if (a.length != b.length)
			throw new IllegalArgumentException("ArrayUtils::sum Both arrays must be of the same length");
		
		double[] ret = new double[a.length];
		for (int i=0;i<a.length;i++)
			ret[i]=a[i]+b[i];
		return ret;
	}
	
	public static double sum(double[] a){
		double ret=0;
		for (int i=0;i<a.length;i++)
			ret+=a[i];
		return ret;
	}
	
	public static int sumCol(int[][] a, int index){
		int ret=0;
		for (int i=0;i<a.length;i++)
			ret+=a[i][index];
		return ret;
	}
	
	public static double sumCol(double[][] a, int index){
		double ret=0;
		for (int i=0;i<a.length;i++)
			ret+=a[i][index];
		return ret;
	}

	public static double[] normalize(double[] a){
		double[] ret = new double[a.length];
		for (int i=0;i<a.length;i++)
			ret[i]=a[i]/sum(a);
		return ret;
	}
	
	public static void fill(int[] a, int val){
		Arrays.fill(a, val);
	}
	
	public static void fill(double[] a, double val){
		Arrays.fill(a, val);
	}
	
	public static void fill(int[][] a, int val){
		for (int i=0;i<a.length;i++)
			fill(a[i],val);
	}
	
	public static void fill(double[][] a, double val){
		for (int i=0;i<a.length;i++)
			fill(a[i],val);
	}
	
	public static String toString(int[] a){
		return Arrays.toString(a);
	}
	
	public static String toString(double[] a){
		return Arrays.toString(a);
	}
	
	public static String toString(String[] a){
		String ret= "";
		for (int i=0;i<a.length;i++)
			ret+=a[i]+" ";
		return ret;
	}
	
	public static String toString(int[][] a){
		String ret= "";
		for (int i=0;i<a.length;i++)
			ret+=toString(a[i])+"\n";
		return ret;
	}
	
	public static String toString(double[][] a){
		String ret= "";
		for (int i=0;i<a.length;i++)
			ret+=toString(a[i])+"\n";
		return ret;
	}
	
	public static String toString(double[][][] a){
		String ret= "";
		for (int i=0;i<a.length;i++)
			ret+=toString(a[i])+"\n";
		return ret;
	}
	
	public static boolean contains(double[] a, double valueToFind){
		boolean ret = false;
		for (int i=0;i<a.length;i++){
			if (valueToFind==a[i])
				ret = true;
		}
		return ret;
	}
	
	/**
	 * Adds all the elements of the given arrays into a new array.
	 * @param array Original array
	 * @param array2Copy Array to copym the elements of this array will be included at the beginning of the
	 * original array
	 * @return Modified Array
	 */
	public static int[] addAll(int[] array, int[] array2Copy){
		if (array.length < array2Copy.length)
			throw new IllegalArgumentException("ArrayUtils::addAll Lenght of the original array must be at least of the same lenght");
		
		for (int i = 0; i < array2Copy.length; i++)
			array[i] = array2Copy[i];
		
		return array;
	}
	
	public static double[] addAll(double[] array, double[] array2Copy){
		if (array.length < array2Copy.length)
			throw new IllegalArgumentException("ArrayUtils::addAll Lenght of the original array must be at least of the same lenght");
		
		for (int i = 0; i < array2Copy.length; i++)
			array[i] = array2Copy[i];
		
		return array;
	}

	/**
	 * Copies the given array and adds the element at the end of the new array.
	 * 
	 * @param array The original array
	 * @param element The element to add to the original array
	 * @return The extended array
	 */
	public static int[] add(int[] array, int element) {
		int[] ret = new int[array.length + 1];
		ret=addAll(ret,array);
		ret[array.length]=element;
		return ret;
	}
	
	public static double[] add(double[] array, double element) {
		double[] ret = new double[array.length + 1];
		ret=addAll(ret,array);
		ret[array.length]=element;
		return ret;
	}
	
	public static List<Integer> asList(int[] array){
		List<Integer> ret = new ArrayList<Integer>();
		for (int i=0; i<array.length; i++)
			ret.add(new Integer(array[i]));
		return ret;
	}
	
}
