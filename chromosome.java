package Tetris;

import java.util.Random;

public class chromosome implements Comparable<chromosome>{
	double prop[];
	double fitness=0;
	int numprop;
	boolean explored=false;
	Random dist = new Random();
	
	// Creates a list of properties of numprop size
	chromosome(int tempnumprop){
		numprop=tempnumprop;
		prop = new double[numprop];
	}
	
	// Returns the absolute value of a
	private double abs(double a){
		if (a>=0){
			return a;
		}
		return -1*a;
	}
	
	// Gives each property a random value
	void randomize(double lowbound, double upbound){
		for (int i=0;i<numprop;i++){
			prop[i]=lowbound+dist.nextDouble()*(abs(lowbound)+abs(upbound));
		}
	}
	
	// Sets the fitness of the chromosome to tempfitness
	void setfitness(int tempfitness){
		fitness=tempfitness;
	}
	
	//Comparison functions used for sorting a list of chromosomes
	public int compareTo(chromosome a) {
		if (a.fitness==fitness){
			return 0;
		}
		else if (a.fitness>fitness)
			return -1;
		return 1;
		
	}

}