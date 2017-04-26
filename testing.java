package Tetris;


public class testing {
	public static void main(String[] args) {
		Trainer train = new Trainer();
		Population tst = new Population(1000);
		tst.randomize(5, -10, 10);
		int generation=0;
		int iterationperchromo = 3;
		// FIrst generation: -2.4226888543963154, 9.715614711223356, -2.524818969066649, -6.492232043727199
		//sumheight, lines, holes, bump
		//4 para 1 gen fitness is blocks spawned 1 time each chromo 
		//double temp[] = {-9.901583906846557, 9.318094147169571, -3.9771499497317535, -3.3116729987570537};
		//4 para 1 gen fitness is lines done 10 times each chromo 
		//double temp[] = {-7.898982555717331, 6.464839907717273, 1.9601283458943257, -0.8181518405524635};
		
		//sumheight, lines, holes, bump, blockade
		//5 para fitness is lines done 1 time each 320 fit
		//double temp[] = {-8.933325726242076, 3.8005955196273895, -6.518116181705671, -3.538284947520049, -9.512094169303895}; 
		//5 para fitness is lines done 1 time each 787 fit
		//double temp[] = {-4.196318167941128, 3.046546572612774, -6.147211902885769, -2.3616552692261337, -1.086585793402568}; 
		
		// 5 para fitness is blocks spawned 3 time each
		
		
		//new Tetris().go(temp,true,false);
		System.out.println("5 paras fitness is lines done "+iterationperchromo+" times each");
		while(true){
			double bestfit = 0;
			int bestchrom=-1;
			for (int i=0;i<1000;i++){
				if (i%100==0){
					System.out.println("Chromosome: "+i);
				}
				tst.pop[i].fitness=0;
				for (int k=0;k<iterationperchromo;k++){
				tst.pop[i].fitness +=new Tetris().go(tst.pop[i].prop,false,true);
				}
				
				if (tst.pop[i].fitness>bestfit){
					bestfit=tst.pop[i].fitness;
					bestchrom=i;
				}
				
			}
			System.out.print("Generation: "+generation+" ");
			for (int i=0;i<5;i++){
				System.out.print(tst.pop[bestchrom].prop[i]+", ");
			}
			System.out.println("Fitness: "+bestfit);
			generation++;
			train.learn(tst);
		}
	}
	
}
