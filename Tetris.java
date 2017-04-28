package Tetris;
//Made by Andrew Xue
//a3xue@edu.uwaterloo.ca
// TETRIS! Why play the game when you can have a computer play the game for you? This
//  AI uses a genetic algorithm to systematically choose the best possible move for every
//  block. No special libraries or third party software beyond the standard Eclipse build
//  was used in this project. For every block, the computer considers based on its heuristics
//  the objectively "best" move to make. These heuristics were generated through machine learning,
//  specifically using a genetic algorithm.
//Part of a project to learn Java over the winter break and create retro video games

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;
import javax.swing.JComponent;
import javax.swing.JFrame;

public class Tetris {
	JFrame window = new JFrame("Tetris");
	static int gamestate[][] = new int [21][10];
	//2-d Array representing the game board. A 0 represents an empty square, A 1 represents a placed block, a 2 is an active block, a 3 is a pivot point
	Random blockchooser = new Random();
	tetgrid screen = new tetgrid();
	int blocktype;
	int score=0;
	int gamescreen=1;
	double prop[];
	int fitness=0;
	int move;
	int rotate;
	int speed=110;
	boolean trans = false;
	boolean draw;
	
	/*public static void main(String[] args) {
		double temp[] = {1,1,1,1,1};
		new Tetris().go(temp, true);
	}*/
	
	int go(double tempprop[],boolean tempdraw) { // making the initial window and implementing a KeyListener
		for (int x=0; x<=20; x++){
			for (int y=0; y<=9; y++){
				if (x==20)gamestate[x][y]=1;
				else gamestate[x][y]=0;
			}
		}	
		prop=tempprop;
		draw=tempdraw;
		if (draw){
		window.setSize(507,735);
		//game space is (100-400)x(50,650)
		//30x30 sized squares
		window.setTitle("Tetris");
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setVisible(true);
		window.setResizable(false);
		window.add(screen);
		window.addKeyListener(new keyevents());
		}
		while(true){
			// spawns a new block at the top of the grid. the block will
			// be placed on the grid and will all be the number "2" which signifies they are moving
			// There will also be a single block "3" which indicates the pivot point at which
			// the block will rotate around. If the grid point in which a new piece is being
			// spawned at is occupied already, gameover().
			
			
		//	fitness++;
			blocktype = blockchooser.nextInt(7);
			if (blocktype<=3){
			if (gamestate[1][4]==1||gamestate[1][5]==1||
				gamestate[1][6]==1)return gameover();
			else{
			gamestate[1][4]= 2;
			gamestate[1][5]= 3;
			gamestate[1][6] =2;
			if (blocktype==0) {
				if (gamestate[1][3]==1)return gameover();
				else gamestate[1][3]=2;}
			else {
				if (gamestate[2][3+blocktype]==1)return gameover();
				gamestate[2][3+blocktype]=2;}}}
			
			else {
			if (gamestate[1][4]==1||gamestate[1][5]==1||
			gamestate[2][-1+blocktype]==1||gamestate[2][blocktype]==1)return gameover();
			else if (blocktype==4){
			  gamestate[1][4]= 3;
			  gamestate[1][5]= 2;}
			else if (blocktype==6){
			  gamestate[1][4]= 2;
			  gamestate[1][5] =3;}// hardcoded in order to place correct pivot points
			else if (blocktype==5) {
			  gamestate[1][4]= 2;
			  gamestate[1][5]= 2;}
			
			// Pieces are randomly generated out of the 7 possible combinations of 4 blocks
			
			gamestate[2][-1+blocktype]= 2;
			gamestate[2][blocktype]= 2;}
			window.repaint();
			findbestmove(gamestate);
			blockdrop();// makes the piece drop
			if (fitness>=5000){
				return fitness;
			}
		}
	}

	private class tetgrid extends JComponent {
		public void paintComponent(Graphics g){	
			Graphics2D grap = (Graphics2D) g;
			grap.setColor(Color.BLACK);
			grap.fillRect(0, 0, 1000,1000);//setting up black background
			if (gamescreen==1){
			grap.setColor(Color.WHITE);
			for (int x=100; x<=400; x+=30){//setting up vertical grid lines
				grap.drawLine(x, 50, x, 650);}
			for (int y=50; y<=650; y+=30){//setting up horizontal grid lines
				grap.drawLine(100, y, 400, y);}
			grap.setFont(new Font("Arial Black", Font.BOLD, 30));
			grap.drawString("Lines Cleared: "+Integer.toString(score/100),103, 40);
			for (int x=0; x<20;x++){
				for (int y=0; y<10; y++){
					if (gamestate[x][y]>=1)grap.fillRect(100+(y*30), (50+(x*30)), 30, 30);
				}//game space is (100-400)x(50,650)
			}
			grap.setFont(new Font("Arial Black", Font.BOLD, 15));
			grap.drawString("J and L to control speed of simulation, K to toggle", 15, 670);
			grap.drawString("whether transformations are shown", 85, 690);
			}
			else if (gamescreen==2){//Game Over screen
				//game space is (100-400)x(50,650)
				grap.setColor(Color.WHITE);	
				for (int x=0; x<=4; x++){
					grap.fillRect(20, 50+(20*x), 20, 20);
					grap.fillRect(140, 50+(20*x), 20, 20);
					grap.fillRect(220, 50+(20*x), 20, 20);
					grap.fillRect(260, 50+(20*x), 20, 20);
					grap.fillRect(340, 50+(20*x), 20, 20);
					grap.fillRect(380, 50+(20*x), 20, 20);
					
					grap.fillRect(20, 200+(20*x), 20, 20);
					grap.fillRect(100, 200+(20*x), 20, 20);
					grap.fillRect(140, 200+(20*x), 20, 20);
					grap.fillRect(220, 200+(20*x), 20, 20);
					grap.fillRect(260, 200+(20*x), 20, 20);
					grap.fillRect(380, 200+(20*x), 20, 20);
				}
				for (int x=1; x<=4; x++){
					grap.fillRect(20+(20*x), 50, 20, 20);
					grap.fillRect(20+(20*x), 130, 20, 20);
					grap.fillRect(140+(20*x), 50, 20, 20);
					grap.fillRect(140+(20*x), 90, 20, 20);
					grap.fillRect(380+(20*x), 50, 20, 20);
					grap.fillRect(380+(20*x), 90, 20, 20);
					grap.fillRect(380+(20*x), 130, 20, 20);
					
					grap.fillRect(20+(20*x), 200, 20, 20);
					grap.fillRect(20+(20*x), 280, 20, 20);
					grap.fillRect(120+(20*x), 280, 20, 20);
					grap.fillRect(260+(20*x), 200, 20, 20);
					grap.fillRect(260+(20*x), 240, 20, 20);
					grap.fillRect(260+(20*x), 280, 20, 20);
					grap.fillRect(380+(20*x), 200, 20, 20);
					grap.fillRect(380+(20*x), 240, 20, 20);
				}
				grap.fillRect(460, 220, 20, 20);
				grap.fillRect(440, 260, 20, 20);
				grap.fillRect(460, 280, 20, 20);
				for (int x=0; x<=1; x++){
					grap.fillRect(80+(20*x), 90, 20, 20);
					grap.fillRect(100, 90+(20*x), 20, 20);
					grap.fillRect(280, 70+(20*x), 20, 20);
					grap.fillRect(300, 90+(20*x), 20, 20);
					grap.fillRect(320, 70+(20*x), 20, 20);
				}
				grap.setFont(new Font("Arial Black", Font.BOLD, 30));
				grap.drawString("Score: "+Integer.toString(score), 140, 380);
				}
			}
		}
		
	private void moveright(){// Shifts all the blocks of the active piece one block to the right
		for (int x=0; x<20; x++){
			for (int y=0; y<10; y++){
				if (gamestate[x][9-y]>=2){
					gamestate[x][10-y]= gamestate[x][9-y];
					gamestate[x][9-y]=0;
					window.repaint();}}}
	}
	
	private void moveleft(){// Shifts all the blocks of the active piece one block to the left
		for (int x=0; x<20; x++){
			for (int y=0; y<10; y++){
				if (gamestate[x][y]>=2){
					gamestate[x][y-1]=gamestate[x][y];
					gamestate[x][y]= 0;
					window.repaint();
					}
				}
			}
		}
	
	
	private class keyevents implements KeyListener{
		public void keyPressed(KeyEvent event) {// Accepts button presses from the user
			// and implements various functions according to the button pressed
			if (event.getKeyCode()==KeyEvent.VK_RIGHT&&!blocked("right", 2))moveright();
			else if (event.getKeyCode()==KeyEvent.VK_LEFT&&!blocked("left", 2))moveleft();
			else if (event.getKeyCode()==KeyEvent.VK_DOWN){
				finishdrop(gamestate,instantblockdrop(gamestate));
				window.repaint();}
			else if (event.getKeyCode()==KeyEvent.VK_UP&&blocktype!=5){
				for (int x=0; x<20; x++){
					for (int y=0; y<10; y++){
						if (gamestate[x][y]==3&&x>=1){rotate(gamestate,x, y);
						break;}
					}
				}
			}
			else if (event.getKeyCode()==KeyEvent.VK_J){
				speed+=15;
			}
			else if (event.getKeyCode()==KeyEvent.VK_L&&speed!=5){
				speed-=15;
			}
			else if (event.getKeyCode()==KeyEvent.VK_K){
				trans=!trans;
			}
		}
		public void keyReleased(KeyEvent event) {}
		public void keyTyped(KeyEvent event) {}
	}
	
	private void rotate(int gamestate[][],int xcoord, int ycoord){//makes adjustments to the active piece as
		// needed and rotates it. For example if a piece is right up against the edge of the
		// grid this function might shift it a square to the left and rotate it
		if (blocktype==0&&(blocked("left",3))){
			if (!blocked("right", 3)){
				moveright();
				boolean result = canrotate(xcoord, ycoord+1);
				if (result&&gamestate[xcoord-2][ycoord+1]!=2){actuallymove(gamestate, xcoord, ycoord+1);}
				else if (!result&&gamestate[xcoord-2][ycoord+1]!=2)moveleft();
				else{
				if (!blocked("right",2)){
					moveright();
					if (canrotate(xcoord, ycoord+2))actuallymove(gamestate, xcoord, ycoord+2);
					else {moveleft();
						  moveleft();}
					}
				}
			}
		}
		
		else if (blocktype==0&&(blocked("right",3))){
			if (!blocked("left", 2)){
				moveleft();
				boolean result = canrotate(xcoord, ycoord-1);
				if (result&&gamestate[xcoord+2][ycoord-1]!=2){actuallymove(gamestate, xcoord, ycoord-1);}
				else if (!result&&gamestate[xcoord+2][ycoord-1]!=2)moveright();
				else{
				if (!blocked("left",2)){
					moveleft();
					if (canrotate(xcoord, ycoord-2))actuallymove(gamestate, xcoord, ycoord-2);
					else {moveright();
						  moveright();}
					}
				}
				}
			}
		
		else if (ycoord==8&&blocktype==0&&gamestate[xcoord+2][ycoord]==2){
			if (!blocked("left",2)){
				moveleft();
				if (canrotate(xcoord, ycoord-1)){actuallymove(gamestate, xcoord, ycoord-1);}
				else moveright();
			}
		}
		
		else if (ycoord==1&&blocktype==0&&gamestate[xcoord-2][ycoord]==2){
			if (!blocked("right",2)){
				moveright();
				if (canrotate(xcoord, ycoord+1)){actuallymove(gamestate, xcoord, ycoord+1);}
				else moveleft();
			}
		}
		
		else if (blocked("right",3)){
			if (!blocked("left", 2)){
				moveleft();
				if (canrotate(xcoord, ycoord-1))actuallymove(gamestate,xcoord, ycoord-1);
				else moveright();}
		}
		else if (blocked("left",3)){
			if (!blocked("right", 2)){
				moveright();
				if (canrotate(xcoord, ycoord+1))actuallymove(gamestate,xcoord, ycoord+1);
				else moveleft();}
		}
		else if (canrotate(xcoord, ycoord)){
			actuallymove(gamestate,xcoord, ycoord);}}
		
	private void actuallymove(int gamestate[][],int xcoord, int ycoord){// if canrotate is true, this function
		// carries out the actual movement on the grid and repaints it.
		
		if (gamestate[xcoord-1][ycoord]==2){
			gamestate[xcoord-1][ycoord]=0;
			gamestate[xcoord][ycoord-1]=gamestate[xcoord][ycoord-1]+3;}
		
		if (gamestate[xcoord][ycoord+1]==2){
			gamestate[xcoord][ycoord+1]=0;
			gamestate[xcoord-1][ycoord]=2;}
		
		if (gamestate[xcoord+1][ycoord]==2){
			gamestate[xcoord+1][ycoord]=0;
			gamestate[xcoord][ycoord+1]=2;}
		
		if (gamestate[xcoord][ycoord-1]==2){
			gamestate[xcoord][ycoord-1]=0;
			gamestate[xcoord+1][ycoord]=2;}
		
		if (gamestate[xcoord][ycoord-1]==5){
			gamestate[xcoord][ycoord-1]=2;
			gamestate[xcoord+1][ycoord]=2;}

		if (gamestate[xcoord][ycoord-1]==3){
			gamestate[xcoord][ycoord-1]=2;}
		
		if (blocktype!=0){	

		//diagonals
		if (gamestate[xcoord-1][ycoord+1]==2){
			gamestate[xcoord-1][ycoord+1]=0;
			gamestate[xcoord-1][ycoord-1]=gamestate[xcoord-1][ycoord-1]+3;}
		
		if (gamestate[xcoord+1][ycoord+1]==2){
			gamestate[xcoord+1][ycoord+1]=0;
			gamestate[xcoord-1][ycoord+1]=2;}
		
		if (gamestate[xcoord+1][ycoord-1]==2){
			gamestate[xcoord+1][ycoord-1]=0;
			gamestate[xcoord+1][ycoord+1]=2;}
		
		if (gamestate[xcoord-1][ycoord-1]==2){
			gamestate[xcoord-1][ycoord-1]=0;
			gamestate[xcoord+1][ycoord-1]=2;}
		
		if (gamestate[xcoord-1][ycoord-1]==5){
			gamestate[xcoord-1][ycoord-1]=2;
			gamestate[xcoord+1][ycoord-1]=2;}
		
		if (gamestate[xcoord-1][ycoord-1]==3){
			gamestate[xcoord-1][ycoord-1]=2;}}
		else {						
			
			//2 units out
			if (xcoord-2>=0&&gamestate[xcoord-2][ycoord]==2){
				gamestate[xcoord-2][ycoord]=0;
				gamestate[xcoord][ycoord-2]=gamestate[xcoord][ycoord-2]+3;}
			
			
			if (xcoord-2>=0&&ycoord+2<=9){
			if (gamestate[xcoord][ycoord+2]==2){
				gamestate[xcoord][ycoord+2]=0;
				gamestate[xcoord-2][ycoord]=2;}}
			
			if (ycoord+2<=9)
			if (gamestate[xcoord+2][ycoord]==2){
				gamestate[xcoord+2][ycoord]=0;
				gamestate[xcoord][ycoord+2]=2;}
			
			
			if (ycoord-2>=0){
			if (gamestate[xcoord][ycoord-2]==2){
				gamestate[xcoord][ycoord-2]=0;
				gamestate[xcoord+2][ycoord]=2;}
			
			if (gamestate[xcoord][ycoord-2]==5){
				gamestate[xcoord][ycoord-2]=2;
				gamestate[xcoord+2][ycoord]=2;}
			
			if (gamestate[xcoord][ycoord-2]==3){
				gamestate[xcoord][ycoord-2]=2;}}
		}
		
		window.repaint();
	}
	
	private boolean canrotate(int x, int y){// checks if the piece is able to rotate. Special 
		// circumstances for the long piece are used as well
		
		if ((gamestate[x][y+1]==2&&gamestate[x-1][y]==1)
				|| (gamestate[x][y-1]==2&gamestate[x+1][y]==1)
				|| (gamestate[x+1][y]==2&&gamestate[x][y+1]==1)
				|| (gamestate[x-1][y]==2&&gamestate[x][y-1]==1)
				)return false;
		
		if (blocktype==0){
			if (y+2<=9&&x==1&&gamestate[x][y+2]==2)return false;
			if (y-2>=0&&(gamestate[x][y-2]==2&&gamestate[x+2][y]==1
					|| (x-2>=0&&gamestate[x-2][y]==2&&gamestate[x][y-2]==1)))
				return false;
			if (y+2<=9&& 
					(gamestate[x+2][y]==2&&gamestate[x][y+2]==1
					|| (x-2>=0&&gamestate[x][y+2]==2&&gamestate[x-2][y]==1)))
				return false;
		}
		else 
		if ((gamestate[x+1][y+1]==2&&gamestate[x-1][y+1]==1)
		|| (gamestate[x+1][y-1]==2&&gamestate[x+1][y+1]==1)
		|| (gamestate[x-1][y-1]==2&&gamestate[x+1][y-1]==1)
		|| (gamestate[x-1][y+1]==2&&gamestate[x-1][y-1]==1))return false;
		
		return true;}
	
	private int instantblockdrop(int gamestate[][]){// finds the number of spaces the active piece needs to fall
		// to stop moving
		for (int j=0; j<21; j++) if (endblockdrop(gamestate,j)) return j;
		
		return 0;
	}
	
	private void finishdrop(int gamestate[][],int drop){// drops the active piece "drop" spaces, instantly stopping
		// its fall
		if (drop!=0){
			for (int x=0;x<20;x++){
				for (int y=0; y<10; y++){
					if (gamestate[19-x][y]>=2){
						gamestate[drop+19-x][y]= gamestate[19-x][y];
						gamestate[19-x][y]=0;
					}
				}
			}
		}
	}
	
	private boolean endblockdrop(int gamestate[][],int j){// if the current block piece is touching either
		// the bottom of the grid or another already placed piece, it will stop moving
		boolean end=false;
		for (int x=0; x<20; x++){
			for (int y=0; y<10; y++){
				if ((gamestate[x][y]>=2)&&(gamestate[x+j+1][y]==1)){
					end=true;
				}
			}
		}
		return end;
	}
	
	private boolean blocked(String rightorleft, int numb){// Checks if the current piece
		// if able to move to the left or the right. The parameter numb is used for rotations
		for (int x=0; x<20; x++){
			for (int y=0; y<10;y++){
				if (rightorleft=="right"){
				if (gamestate[x][y]>=numb&&y==9) return true;
				if (gamestate[x][y]>=numb&&gamestate[x][y+1]==1) return true;
				}
				else if (rightorleft=="left") {
					if (gamestate[x][y]>=numb&&y==0) return true;
					if (gamestate[x][y]>=numb&&gamestate[x][y-1]==1) return true;
				}}}
		return false;
		}
	
	private int gameover(){// switches screen to game over screen
		gamescreen=2;
		window.repaint();
		window.setVisible(false);
		window.dispose();
		return fitness;}
	
	private void blockdrop(){// creates a block and drops it
		// If an AI is playing, it rotates the block and moves the block to the optimal position
		window.repaint();
		if (trans){
			try{
				if (!draw){Thread.sleep(0);}
				else Thread.sleep(300);
			}
			catch(Exception exp){
				System.out.println("Runtime Error");
			}
		}
		if (rotate!=0){
			for (int i=0;i<rotate;i++){
				for (int x=0; x<20; x++){
					for (int y=0; y<10; y++){
						if (gamestate[x][y]==3&&x>=1){rotate(gamestate,x, y);
						break;}
					}
				}
			}
		}
		if(move!=0){
			if (move<0){
				for (int i=0;i<-1*move;i++){
					if (!blocked("right", 2))moveright();
				}
			}
			if (move>0){
				for (int i=0;i<move;i++){
					if (!blocked("left", 2))moveleft();
				}
			}
		}
		rotate=0;
		move=0;
		window.repaint();
		if (trans){
			try{
				if (!draw){Thread.sleep(0);}
				else Thread.sleep(300);
			}
			catch(Exception exp){
				System.out.println("Runtime Error");
			}
		}
		while (!endblockdrop(gamestate,0)){//until the dropping piece hits the bottom or another piece
			// it will drop block by block
			for (int x=0; x<20; x++){
				for (int y=0; y<10; y++){
					if (gamestate[19-x][y]>=2){
						gamestate[20-x][y]=gamestate[19-x][y];
						gamestate[19-x][y]=0;
					}
				}
			}
			
			try{
				if (!draw){Thread.sleep(0);}
				else Thread.sleep(speed);
			}
			catch(Exception exp){
				System.out.println("Runtime Error");
			}
			
			// If the AI is playing it instantly places the block
			if (!draw)finishdrop(gamestate,instantblockdrop(gamestate));
			window.repaint();
		}
		// when the piece hits the bottom, it connects with all the other pieces
		for (int x=0; x<20; x++){
			for (int y=0; y<10; y++){
				if (gamestate[x][y]>=2){
					gamestate[x][y]=1;
				}
			}
		}
		scored();//checks if the dropped block has made a line
		}
		
	private void scored(){//checks if the current gamestate contains a line of blocks
		for (int x=0; x<20; x++){
			boolean scored=true;
			for (int y=0; y<10; y++){
				if (gamestate[x][y]!=1){
					scored=false;
				}
			}
			if (scored){// if there is a line of blocks, the line is deleted
				fitness++;
				score+=100;
				for (int y=0; y<10; y++) gamestate[x][y]= 0;
				window.repaint();
				try{if (!draw)Thread.sleep(400);}catch(Exception exp){System.out.println("Runtime Error");}
				linecreated(x);}
		}
	}
	
	private void linecreated(int lineindex){// shifts all the blocks above the deleted line
		// down one block. Follows the original design of Tetris which did not have "gravity"
		// or chain reactions
		for (int x=(lineindex-1); x>=0; x--){
			for (int y=0; y<10; y++){
				gamestate[x+1][y]=gamestate[x][y];
			}
		}
	}
	
	// Tries all possible moves with the currently spawned block and chooses the best one
	private void findbestmove(int gamestatee[][]){
		double maxcost = -9999;
		move=0;
		rotate=0;
		int rotnum=4;
		if (blocktype==0||blocktype==4||blocktype==6){
			rotnum=2;
		}
		if (blocktype==5){
			rotnum=1;
		}
		int rotated[][]=new int [21][10];
		
		for (int a=0;a<21;a++){
			for (int b=0;b<10;b++){
				rotated[a][b]=gamestatee[a][b];
			}
		}
		
		for (int z=0;z<rotnum;z++){
			boolean done = false;
			int gamestate[][]=new int [21][10];
			for (int a=0;a<21;a++){
				for (int b=0;b<10;b++){
					gamestate[a][b]=rotated[a][b];
				}
			}
			for (int q=0;q<5;q++){
				for (int i=0;i<20;i++){
					for (int k=0;k<10;k++){
						if (gamestate[i][k]>=2&&k-1<0){
							done=true;
							break;
						}
						if (gamestate[i][k]>=2&&gamestate[i][k-1]==1){
							done=true;
							break;
						}
						if (gamestate[i][k]>=2){
							gamestate[i][k-1]=gamestate[i][k];
							gamestate[i][k]=0;
						}
					}
				}
				if (!done){
					double tempcost = cost(gamestate);
					if (tempcost>maxcost){
						maxcost = tempcost;
						move=q+1;
						rotate=z;
					}
				}
				if (done){
					break;
				}
			}
			
			for (int a=0;a<21;a++){
				for (int b=0;b<10;b++){
					gamestate[a][b]=rotated[a][b];
				}
			}
			double temp = cost(gamestate);
			if (temp>maxcost){
				maxcost=temp;
				move=0;
				rotate=z;
			}
			done=false;
			for (int q=0;q<5;q++){
				for (int i=0;i<20;i++){
					for (int k=9;k>=0;k--){
						if (gamestate[i][k]>=2&&k+1>=10){
							done=true;
							break;
						}
						if (gamestate[i][k]>=2&&gamestate[i][k+1]==1){
							done=true;
							break;
						}
						if (gamestate[i][k]>=2){
							gamestate[i][k+1]=gamestate[i][k];
							gamestate[i][k]=0;
						}
					}
				}
				if (!done){
					double tempcost = cost(gamestate);
					if (tempcost>maxcost){
						move=-1-q;
						maxcost = tempcost;
						rotate=z;
					}
				}
				if (done){
					break;
				}
			}
			for (int x=0; x<20; x++){
				for (int y=0; y<10; y++){
					if (rotated[x][y]==3&&x>=1){rotate(rotated,x, y);
					break;}
				}
			}
		}
	}
	
	// Finds the total cost of making a move. In this case, a greater cost is a better move
	private double cost(int gamestatee[][]){
		int gamestate[][] = new int[21][10];
		for (int i=0;i<21;i++){
			for (int k=0;k<10;k++){
				gamestate[i][k]=gamestatee[i][k];
			}
		}
		finishdrop(gamestate,instantblockdrop(gamestate));
		/*for (int i=0;i<20;i++){
			for (int k=0;k<10;k++){
				System.out.print(gamestate[i][k]);
			}
			System.out.println();
		}
		System.out.println(holes(gamestate));*/
		return prop[0]*sumheight(gamestate)+prop[1]*lines(gamestate)+prop[2]*holes(gamestate)+prop[3]*bump(gamestate)+prop[4]*blockade(gamestate);
	}
	
	// Counts the total height of the blocks in gamestate
	private int sumheight(int gamestate[][]){
		int answer=0;
		for (int i=0;i<10;i++){
			int hei = 0;
			for (int k=19;k>=0;k--){
				if (gamestate[k][i]!=0){
					hei = 20-k;
				}
			}
			answer+=hei;
		}
		return answer;
	}
	
	// Counts the number of completed lines in gamestate
	private int lines(int gamestate[][]){
		int answer=0;
		for (int i=0;i<20;i++){
			boolean line = true;
			for (int k=0;k<10;k++){
				if (gamestate[i][k]==0){
					line=false;
				}
			}
			if (line){
				answer++;
			}
		}
		return answer;
	}
	
	// Counts the number of holes in the game state
	private int holes(int gamestate[][]){
		int answer=0;
		for (int i=0;i<10;i++){
			int count=0;
			int temp=0;
			for (int k=19;k>=0;k--){
				if (gamestate[k][i]>=1){
					count+=temp;
					temp=0;
				}
				else if (gamestate[k][i]==0){
					temp++;
				}
			}
			answer+=count;
		}
		return answer;
	}
	
	// Returns the absolute value of a
	private int abs(int a){
		if (a>=0){
			return a;
		}
		return -1*a;
	}
	
	// Finds the bumpiness of gamestate
	private int bump(int gamestate[][]){
		int height[]=new int[10];
		for (int i=0;i<10;i++){
			int hei = 0;
			for (int k=19;k>=0;k--){
				if (gamestate[k][i]!=0){
					hei = 20-k;
				}
			}
			height[i]=hei;
		}
		int answer=0;
		for (int i=1;i<10;i++){
			answer+=abs(height[i]-height[i-1]);
		}
		return answer;
	}
	
	// Finds the number of blocks blocking off holes in gamestate
	private int blockade(int gamestate[][]){
		int answer=0;
		for (int i=0;i<10;i++){
			boolean hol=false;
			for (int k=19;k>=0;k--){
				if (gamestate[k][i]==0){
					hol=true;
				}
				if (hol&&gamestate[k][i]>=1){
					answer++;
				}
			}
		}
		return answer;
	}
}
