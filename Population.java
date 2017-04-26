package Tetris;

public class Population {
	chromosome pop[];
	
	// Creates a list of size chromosomes
	Population(int size){
		pop = new chromosome[size];
	}
	
	// Creates a random chromosome for the each chromosome of the population with numprop properties bounded from lowbound to upbound
	void randomize(int numprop, int lowbound, int upbound){
		for (int i=0;i<1000;i++){
			chromosome temp = new chromosome(numprop);
			temp.randomize(lowbound,upbound);
			pop[i]=temp;
		}
	}
	
}
