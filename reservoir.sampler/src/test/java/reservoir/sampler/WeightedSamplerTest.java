package reservoir.sampler;

import java.io.ByteArrayInputStream;

import org.junit.Assert;
import org.junit.Test;

import reservoir.sampler.simple.SimpleSampler;

public class WeightedSamplerTest {

    @Test
    public void testWithSameSeed() {
	String data = "THEQUICKBROWNFOXJUMPSOVERTHELAZYDOG";
	ByteArrayInputStream inStream = new ByteArrayInputStream(
		data.getBytes());
	System.setIn(inStream);
	String sample = SimpleSampler.create(5, false);
	System.setIn(System.in);
	inStream = new ByteArrayInputStream(data.getBytes());
	System.setIn(inStream);
	String newSample = SimpleSampler.create(5, false);
	System.setIn(System.in);
	Assert.assertEquals("Checking Result for " + sample, sample, newSample);

    }

    @Test
    public void testRandomSeed() {
	String data = "THEQUICKBROWNFOXJUMPSOVERTHELAZYDOG";
	ByteArrayInputStream inStream = new ByteArrayInputStream(
		data.getBytes());
	System.setIn(inStream);
	String sample = SimpleSampler.create(5, true);
	System.setIn(System.in);
	inStream = new ByteArrayInputStream(data.getBytes());
	System.setIn(inStream);
	String newSample = SimpleSampler.create(5, true);
	System.setIn(System.in);
	Assert.assertNotEquals("Checking Result for " + sample, sample, newSample);
    }
    
}
