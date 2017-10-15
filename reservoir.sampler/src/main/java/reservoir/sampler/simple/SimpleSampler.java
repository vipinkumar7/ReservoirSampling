package reservoir.sampler.simple;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;

import joptsimple.OptionParser;
import joptsimple.OptionSet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 
 * @author root
 *
 */
public class SimpleSampler {

    final static Logger logger = LogManager.getLogger(SimpleSampler.class);

    public static String create(int reservoir_size, boolean is_random_seed) {
	char[] reserviour = new char[reservoir_size];
	try {
	    int random_seed = 12345;
	    Random random = is_random_seed ? new Random() : new Random(
		    random_seed);
	    BufferedReader bufferReader = new BufferedReader(
		    new InputStreamReader(System.in));

	    int current_position = 0;
	    String current_line;

	    while ((current_line = bufferReader.readLine()) != null
		    && current_line.length() != 0) {

		for (int i = 0; i < current_line.length(); i++) {
		    if (current_position < reservoir_size) {
			reserviour[current_position] = current_line.charAt(i);
			current_position++;
		    } else {

			int random_value = random.nextInt(current_position + 1);
			if (random_value < reservoir_size)
			    reserviour[random_value] = current_line.charAt(i);
			current_position++;
		    }

		}
	    }

	} catch (IOException e) {
	    logger.error("Stream read fail");
	}
	return String.valueOf(reserviour);
    }

    public static void main(String[] args) throws IOException {
	OptionParser optionParser = new OptionParser();

	optionParser.accepts("random_seed").withOptionalArg()
		.describedAs("is random seed required for random generator ");
	optionParser.accepts("sample_size").withRequiredArg()
		.ofType(Integer.class).describedAs("Sample size for output")
		.defaultsTo(5);
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
	    if (!options.has("h"))
		System.out.println(create(sample_size, random_seed));
	} catch (NumberFormatException nfe) {
	    logger.info("first argument is sample size , it shoud be integer ");
	}

    }

}
