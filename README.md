# ReservoirSampling

This Program reads Input from standard input and generate as random representative of it 
The stream is of unknown and  large length 

The program takes following inputs :

n : size of sample
seed : if same seed is required to same output 

 

Tests on reservoir sampling    

echo THEQUICKBROWNFOXJUMPSOVERTHELAZYDOG | java -jar target/reservoir.sampler-1.jar  5

dd if=/dev/urandom count=100 bs=1MB | base64 | java -jar target/reservoir.sampler-1.jar 10


