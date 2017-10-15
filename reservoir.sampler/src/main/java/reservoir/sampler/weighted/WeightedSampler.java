package reservoir.sampler.weighted;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.PriorityQueue;
import java.util.Random;

import joptsimple.OptionParser;
import joptsimple.OptionSet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 
 * @author Vipin Kumar
 *
 */
public class WeightedSampler {

    final static Logger logger = LogManager.getLogger(WeightedSampler.class);

    public static void generate(int num_char) {

	SecureRandom random = new SecureRandom();
	byte[] values = new byte[num_char];
	random.nextBytes(values);
	String randomVal = new String(Base64.getEncoder().encode(values),
		StandardCharsets.UTF_8);
	System.out.println(randomVal);
    }

    public static String create(int reservoir_size, boolean is_random_seed) {
	PriorityQueue<Tuple> minHeap = new PriorityQueue<Tuple>(reservoir_size,
		new TupleComparator());
	try {
	    int random_seed = 12345, current_random = 0, current_position = 0;
	    Random random = is_random_seed ? new Random() : new Random(
		    random_seed);
	    BufferedReader bufferReader = new BufferedReader(
		    new InputStreamReader(System.in));
	    String current_line;

	    while ((current_line = bufferReader.readLine()) != null
		    && current_line.length() != 0) {

		for (int i = 0; i < current_line.length(); i++) {
		    if (current_position < reservoir_size) {
			current_random = random.nextInt();
			minHeap.add(new Tuple(current_line.charAt(i),
				current_random));
			current_position++;
		    } else {
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
	    logger.error("Stream read fail");

	}
	StringBuffer buffer = new StringBuffer();
	minHeap.forEach(x -> buffer.append(x.character));
	return buffer.toString();

    }

    public static void main(String[] args) throws IOException {
	OptionParser optionParser = new OptionParser();

	optionParser.accepts("random_seed").withOptionalArg()
		.describedAs("is random seed required for random generator ");
	optionParser.accepts("sample_size").withRequiredArg()
		.ofType(Integer.class).describedAs("Sample size for output")
		.defaultsTo(5);
	optionParser.accepts("generate").withRequiredArg()
		.ofType(Integer.class)
		.describedAs("Generate a random string of length n ")
		.defaultsTo(20);
	optionParser.accepts("h", "show help").forHelp();

	int sample_size = 5;
	boolean random_seed = false;
	try {
	    optionParser.printHelpOn(System.out);
	    OptionSet options = optionParser.parse(args);
	    if (options.has("sample_size"))
		sample_size = Integer.parseInt(options.valueOf("sample_size")
			.toString());
	    if (options.has("random_seed"))
		random_seed = true;
	    if (options.has("generate")) {
		int num_char = Integer.parseInt(options.valueOf("generate")
			.toString());
		generate(num_char);
	    } else if (!options.has("h"))
		System.out.println(create(sample_size, random_seed));
	} catch (NumberFormatException nfe) {
	    logger.info("first argument is sample size , it shoud be integer ");
	}
    }

}
