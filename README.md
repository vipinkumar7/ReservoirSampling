# ReservoirSampling

This Program reads Input from standard input and generate as random representative of it 
The stream is of unknown and  large length 

There are two different implementations :

1.  reservoir.sampler
It has  two different approach 

1. SimpleSampler
	Keep the first item in memory.
	When the i-th item arrives (for  i>1):
		with probability 1/i, keep the new item (discard the old one)
		with probability  1-1/i , keep the old item (ignore the new one)
The program takes following inputs :

 sample_size [Size of sample output] 
 random_seed [ to use or not to use seed for same output , its optional argument ]


2. WeightedSampler
	A simple reservoir-based algorithm can be designed using random sort and implemented using priority queue data structure. This algor	       ithm assigns random number as keys to each item and maintain k items with minimum value for keys. In essence, this is equivalent to ass	      igning a random number to each item as key, sorting items using these keys and taking top k items. The worse case run time of the algor	     ithm is O(nlog k) while the best case runtime is  O(n)
	
eg. 

echo THEQUICKBROWNFOXJUMPSOVERTHELAZYDOG | java  -jar target/reservoir.sampler-1-jar-with-dependencies.jar -sample_size=5

echo THEQUICKBROWNFOXJUMPSOVERTHELAZYDOG | java  -jar target/reservoir.sampler-1-jar-with-dependencies.jar -sample_size=5 -random_seed

dd if=/dev/urandom count=100 bs=1MB | base64  | java  -jar target/reservoir.sampler-1-jar-with-dependencies.jar -sample_size=5 -random_seed

for help 

echo THEQUICKBROWNFOXJUMPSOVERTHELAZYDOG | java  -jar target/reservoir.sampler-1-jar-with-dependencies.jar -h 



[To generate random string we use /dev/urandom  to generate random string ]

eg.
echo THEQUICKBROWNFOXJUMPSOVERTHELAZYDOG | java  -jar target/reservoir.sampler-1-jar-with-dependencies.jar -generate 200


 

2. distributed.sampler

The another version is for very large dataset:

	1. Distribute data among m machines.
	2. Each machine does its own weighted sampling using key  as described in previous section and produces a sample of size <= k items.
	3. Collects all m samples of size <= k. We should have total items n'<=mk.
	4. Now sample k items from  n' items from step 3 using key that was already computed in Step 2. This means instead of re-generating key 	using random number generator in sampling algorithm, we use the key we already had assigned in step 2.

eg. 






