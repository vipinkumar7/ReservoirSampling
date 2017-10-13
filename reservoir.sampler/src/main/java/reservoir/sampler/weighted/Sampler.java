package reservoir.sampler.weighted;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.PriorityQueue;
import java.util.Random;

/**
 * 
 * @author Vipin Kumar
 *
 */
public class Sampler {

	public static void main(String[] args) {
		long time1 = System.currentTimeMillis();
		try {
			int k, random_seed = 12345;
			boolean first = true;
			if (args.length > 0)
				k = Integer.parseInt(args[0]);
			else
				k = 5;
			Random random = (args.length > 1) ? new Random(random_seed) : new Random();
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			String line;
			PriorityQueue<Tuple> minHeap = new PriorityQueue<Tuple>(k, new TupleComparator());
			while ((line = br.readLine()) != null && line.length() != 0) {

				int r = random.nextInt();
				if (first) {
					for (int i = 0; i < k; i++) {
						minHeap.add(new Tuple(line.charAt(i), r));
					}
					for (int i = k; i < line.length(); i++) {
						if (r > minHeap.peek().key) {
							minHeap.poll();
							minHeap.add(new Tuple(line.charAt(i), r));
						}
					}
					first = false;
				} else {
					for (int i = k; i < line.length(); i++) {
						if (r > minHeap.peek().key) {
							minHeap.poll();
							minHeap.add(new Tuple(line.charAt(i), r));
						}
					}
				}
			}

			long time2 = System.currentTimeMillis();
			minHeap.forEach(x -> System.out.print(x.character));
			System.out.println();
			System.out.println((time2 - time1));

		} catch (IOException e) {

		}

	}

}
