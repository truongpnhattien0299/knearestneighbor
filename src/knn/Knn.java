package knn;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.StringTokenizer;
import java.util.TreeMap;

public class Knn {

	public static String file = "D:\\dataset\\Book2.csv";
	public static int MAX_ROW = 20;
	public static int MAX_COLUMN = 12;
	public static int percen = 66;
	public static int k = 11;
	public static double[][] trainset, testset;
	public static double[] resultset;

	public static void main(String[] args) {
		DocFile();
		resultset = new double[MAX_ROW * (100 - percen) / 100+1];
		for (int i = 0; i < testset.length; i++) {
			ArrayList<Distanc> knn = kNearestNeighbor(trainset, testset[i], k);
			SoQuyetDinh(knn, i);
		}
		for(int i=0; i<testset.length;i++)
		{
			System.out.print(testset[i][MAX_COLUMN-1]+"  ");
		}
		System.out.println();
		for(int i=0; i<resultset.length;i++)
		{
			System.out.print(resultset[i]+"  ");
		}
		System.out.println();
		TileDung(testset, resultset);
	}

	public static void DocFile() {
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
			String c;
			int i = 0;
			boolean check = false; // kiem tra dong dau tien
			double[][] arr = new double[MAX_ROW][MAX_COLUMN];
			while ((c = in.readLine()) != null) {
				if (!check) {
					check = true;
					continue;
				}
				int j = 0;
				while (j < MAX_COLUMN) {
					StringTokenizer st = new StringTokenizer(c, ",");
					while (st.hasMoreTokens()) {
						arr[i][j] = Double.parseDouble(st.nextToken());
						j++;
					}
				}
				i++;
//				System.out.println(c);
			}
			
			trainset = new double[MAX_ROW * percen / 100][MAX_COLUMN];
			for (int r = 0; r < i * percen / 100; r++) {
				for (int cl = 0; cl < MAX_COLUMN; cl++) {
					trainset[r][cl] = arr[r][cl];
				}
			}
			testset = new double[MAX_ROW * (100 - percen) / 100+1][MAX_COLUMN];
			for (int r = 0; r < i - i * percen / 100; r++) {
				for (int cl = 0; cl < MAX_COLUMN; cl++) {
					testset[r][cl] = arr[i * percen / 100 + r][cl];
				}
			}
//			System.out.println();
		} catch (FileNotFoundException e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
	}

	public static double TinhKhoangCach(double[] a, double[] b) {
		double tmp = 0;
		for (int i = 0; i < a.length - 1; i++) {
			tmp += Math.pow(a[i] - b[i], 2);
		}
		return Math.sqrt(tmp);
	}

	public static ArrayList<Distanc> kNearestNeighbor(double[][] arrtrain, double[] testrec, int k) {
		ArrayList<Distanc> list = new ArrayList<Distanc>();
		for (int i = 0; i < arrtrain.length-1; i++) {
			list.add(new Distanc(i, TinhKhoangCach(arrtrain[i], testrec)));
		}
		Collections.sort(list);
		ArrayList<Distanc> arr_result = new ArrayList<Distanc>();
		for (int i = 0; i < k; i++) {
			arr_result.add(list.get(i));
		}
		return arr_result;
	}

	public static void SoQuyetDinh(ArrayList<Distanc> list, int index) {
		TreeMap<Double, Integer> map = new TreeMap<Double, Integer>();
		for (Distanc e : list) {
//			System.out.println(e.index);
			addMap(map, trainset[e.index][MAX_COLUMN-1]);
		}
		for (Double key : map.keySet()) {
//			System.out.println(key+" xuat hien "+map.get(key));
			if (map.get(key) == Collections.max(map.values()))
				resultset[index] = key;
		}
	}
	
	public static void TileDung(double[][] arr_test, double[] arr_result)
	{
		double count=0, length = arr_test.length;
		for(int i=0; i<length; i++)
		{
			if(arr_test[i][MAX_COLUMN-1] == arr_result[i])
				count++;
		}
		System.out.println(count/length);
	}

	public static void addMap(TreeMap<Double, Integer> map, double key) {
		if (map.containsKey(key)) {
			int count = map.get(key) + 1;
			map.put(key, count);
		} else
			map.put(key, 1);
	}
}

class Distanc implements Comparable<Distanc> {
	int index;
	double value;

	public Distanc(int index, double value) {
		super();
		this.index = index;
		this.value = value;
	}

	@Override
	public int compareTo(Distanc o) {
		return o.getValue() > value ? -1 : 1;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}
}
