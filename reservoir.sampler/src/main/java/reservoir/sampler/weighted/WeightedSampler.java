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
public class WeightedSampler {

    public static String create(int sample_size, boolean is_random_seed) {
	PriorityQueue<Tuple> minHeap = new PriorityQueue<Tuple>(sample_size,
		new TupleComparator());
	try {
	    int random_seed = 12345, current_random = 0, current_reservoir_size = 0;
	    boolean is_first_line = true;
	    Random random = is_random_seed ? new Random() : new Random(
		    random_seed);
	    BufferedReader bufferReader = new BufferedReader(
		    new InputStreamReader(System.in));
	    String current_line;

	    while ((current_line = bufferReader.readLine()) != null
		    && current_line.length() != 0) {

		if (is_first_line) {
		    for (int i = 0; i < current_line.length()
			    && i < sample_size; i++, current_reservoir_size++) {
			current_random = random.nextInt();
			minHeap.add(new Tuple(current_line.charAt(i),
				current_random));

		    }
		    for (int i = current_reservoir_size; i < current_line
			    .length(); i++, current_reservoir_size++) {

			current_random = random.nextInt();
			if (current_random > minHeap.peek().key) {
			    minHeap.poll();
			    minHeap.add(new Tuple(current_line.charAt(i),
				    current_random));
			}

		    }
		    is_first_line = false;
		} else {
		    int local_current_position = 0;
		    if (current_reservoir_size < sample_size) {

			for (int i = 0; i < current_line.length()
				&& current_reservoir_size < sample_size; i++, current_reservoir_size++, local_current_position++) {
			    current_random = random.nextInt();
			    minHeap.add(new Tuple(current_line.charAt(i),
				    current_random));

			}

		    }
		    for (int i = local_current_position; i < current_line
			    .length(); i++, current_reservoir_size++) {
			current_random = random.nextInt();
			if (current_random > minHeap.peek().key) {
			    minHeap.poll();
			    minHeap.add(new Tuple(current_line.charAt(i),
				    current_random));
			}

		    }
		}
	    }

	} catch (IOException e) {

	}
	StringBuffer buffer = new StringBuffer();
	minHeap.forEach(x -> buffer.append(x.character));
	return buffer.toString();

    }

    public static void main(String[] args) {
	if (args.length > 0) {
	    int sample_size = Integer.parseInt(args[0]);
	    System.out.println(create(sample_size, false));
	}
    }

}
