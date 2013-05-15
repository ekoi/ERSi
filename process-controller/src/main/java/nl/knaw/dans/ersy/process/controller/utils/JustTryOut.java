package nl.knaw.dans.ersy.process.controller.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JustTryOut {

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {

		List<Map<Integer, Integer>> list = new ArrayList<Map<Integer, Integer>>();
		String files[] = { "/Users/akmi/Desktop/std.csv",
				"/Users/akmi/Desktop/abr.csv",
				"/Users/akmi/Desktop/std-abr.csv" };
		for (int i = 0; i < 3; i++) {
			Map<Integer, Integer> map = new HashMap<Integer, Integer>();
			for (int j = 1; j < 5248; j++) {
				map.put(j, 0);
			}

			BufferedReader br = new BufferedReader(new FileReader(files[i]));
			try {
				String line = br.readLine();
				while ((line = br.readLine()) != null) {
					String l[] = line.split(",");
					int key = Integer.parseInt(l[0]);
					int value = Integer.parseInt(l[1]);
					map.put(key, value);
					
				}
			} finally {
				br.close();
			}
			list.add(map);
		}
		//for (Map<Integer, Integer> m : list) {
		Map<Integer, Integer> m = list.get(2);
			for(int k=1; k<5248; k++) {
				System.out.println(m.get(k));
			}
			
		//}
	}

}
