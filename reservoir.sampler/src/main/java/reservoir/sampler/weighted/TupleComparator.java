package reservoir.sampler.weighted;

import java.util.Comparator;


/**
 * 
 * @author Vipin Kumar
 *
 */
public class TupleComparator implements Comparator<Tuple> {

	public int compare(Tuple o1, Tuple o2) {
		return o1.key - o2.key;
	}
}
