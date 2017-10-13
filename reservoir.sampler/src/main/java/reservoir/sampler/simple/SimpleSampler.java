package reservoir.sampler.simple;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.PriorityQueue;
import java.util.Random;

import reservoir.sampler.weighted.Tuple;
import reservoir.sampler.weighted.TupleComparator;

/**
 * 
 * @author root
 *
 */
public class SimpleSampler {

	public static void main(String[] args) {
		long time1 = System.currentTimeMillis();
		try {
			int k, random_seed = 12345;
			if (args.length > 0)
				k = Integer.parseInt(args[0]);
			else
				k = 5;
			Random random = (args.length > 1) ? new Random(random_seed) : new Random();
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			char[] reserviour = new char[k];
			boolean first = true;
			int length = 0;
			String line;
			while ((line = br.readLine()) != null && line.length() != 0) {

				if (first) {
					for (int i = 0; i < k; i++) {
						reserviour[i] = line.charAt(i);
					}
					for (int i = k; i < line.length(); i++) {
						int j = random.nextInt(i + 1);
						if (j < k)
							reserviour[j] = line.charAt(i);

					}
					length = line.length();
					first = false;
				} else {

					for (int i = 0; i < line.length(); i++) {
						length++;
						int j = random.nextInt(length + 1);
						if (j < k)
							reserviour[j] = line.charAt(i);
					}
				}
			}

			long time2 = System.currentTimeMillis();
			System.out.println(reserviour);
			System.out.println();
			System.out.println((time2 - time1));

		} catch (IOException e) {

		}
	}

}
