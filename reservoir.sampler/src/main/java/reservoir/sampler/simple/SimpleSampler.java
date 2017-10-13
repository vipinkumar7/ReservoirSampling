package reservoir.sampler.simple;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;

/**
 * 
 * @author root
 *
 */
public class SimpleSampler {

    public static String create(int sample_size, boolean is_random_seed) {
	char[] reserviour = new char[sample_size];
	try {
	    int random_seed = 12345;
	    Random random = is_random_seed ? new Random() : new Random(
		    random_seed);
	    BufferedReader bufferReader = new BufferedReader(
		    new InputStreamReader(System.in));

	    boolean is_first_line = true;
	    int current_position = 0;
	    String current_line;

	    while ((current_line = bufferReader.readLine()) != null
		    && current_line.length() != 0) {

		if (is_first_line) {

		    for (int i = 0; i < current_line.length()
			    && i < sample_size; i++, current_position++) {
			reserviour[i] = current_line.charAt(i);
		    }

		    // if sample size is smaller than line
		    for (int i = current_position; i < current_line.length(); i++, current_position++) {
			int random_value = random.nextInt(i + 1);
			if (random_value < sample_size)
			    reserviour[random_value] = current_line.charAt(i);

		    }
		    is_first_line = false;
		} else {

		    int local_current_position = 0;
		    // if sample size spills over next line
		    if (current_position < sample_size) {
			for (int i = 0; i < current_line.length()
				&& current_position < sample_size; i++, current_position++, local_current_position++) {
			    reserviour[current_position] = current_line
				    .charAt(i);
			}

		    }
		    for (int i = local_current_position; i < current_line
			    .length(); i++, current_position++) {
			int random_value = random.nextInt(i + 1);
			if (random_value < sample_size)
			    reserviour[random_value] = current_line.charAt(i);
		    }
		}
	    }

	} catch (IOException e) {

	}
	return String.valueOf(reserviour);
    }

    public static void main(String[] args) {
	if (args.length > 0) {
	    int sample_size = Integer.parseInt(args[0]);
	    System.out.println(create(sample_size, false));
	}

    }

}
