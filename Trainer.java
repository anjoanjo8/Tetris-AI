package Tetris;

import java.util.Random;
import java.util.Arrays;


public class Trainer {
	double mutation=0.05;
	Random mutatevari = new Random();
	Random mutatepick = new Random();
	Random mutateamount = new Random();
	
	Random top10pick = new Random();
	
	// Mutates the current population
	void learn(Population old){		
		chromosome strong[] = new chromosome[300];
		
		// Fills in the strong array
		for (int k=0;k<300;k++){
			double topfit = 0;
			double secfit = 0;
			
			int topfitind = 0;
			int secfitind = 0;
			
			// Picks 100 random chromosomes and from these chooses the 2 strongest ones
			for (int i=0;i<100;i++){
				int randpick = top10pick.nextInt(1000);
				chromosome randomchrom=old.pop[randpick];
				if (randomchrom.fitness>topfit){
					topfit=randomchrom.fitness;
					topfitind=randpick;
					continue;
				}
				else if (randomchrom.fitness>secfit){
					secfit=randomchrom.fitness;
					secfitind=randpick;
					continue;
				}
			}
			// For each of these 300 pairs of the strong chromosomes, they are bred into a new chromosome
			strong[k]=breed(old.pop[topfitind],old.pop[secfitind]);
		}
		// Sorts the chromosomes from weakest to strongest
		Arrays.sort(old.pop);
		// Replaces the 300 weakest chromosomes with the newly bred ones
		for (int i=0;i<300;i++){
			old.pop[i]=strong[i];
		}
	}
	
	// Creates a new chromosome from the 2 parent chromosomes
	chromosome breed(chromosome parent1,chromosome parent2){
		chromosome answer = new chromosome(parent1.numprop);
		// The properties of the new chromosome are a combination of the properties from each parent
		for (int i=0;i<parent1.numprop;i++){
			answer.prop[i]=(1/(parent1.fitness+parent2.fitness))*(parent1.fitness*parent1.prop[i]+parent2.fitness*parent2.prop[i]);
		}
		// There is a chance that the chromosome will mutate and one of its properties will be changed by + or - 2%;
		if (mutatevari.nextDouble()<=mutation){
			int propchoice = mutatepick.nextInt(parent1.numprop);
			answer.prop[propchoice]+=answer.prop[propchoice]*(((double)mutateamount.nextInt(41)-20)/100);
		}
		return answer;
		
	}
}
