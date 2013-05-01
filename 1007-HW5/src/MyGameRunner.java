/***
 * The class that controls the actual play of the game
 * @author Hannah
 *
 */
public class MyGameRunner implements GameRunner{

	private Maze maze;
	private int[][] gameGrid;
	private int status;
	private int locX;
	private int locY;
	/**
	 * Constructs the game using a maze that has 1s for walls, 0s for empty spaces,
	 * 2 for location of the player, and 3 for end location;=
	 * @param maze 
	 */
	public MyGameRunner(Maze maze){
		this.maze = maze;
		gameGrid = maze.getWholeGrid();
		status = 0; 
		locX = getStartX();
		locY = getStartY();
		
	}
	
	/**
	 * Gets the starting value of x from the maze
	 * @return x
	 */
	public int getStartX(){
		int x = 0;
		//for (int i = 0; i< gameGrid.length; i++){
			//for (int j = 0; j< gameGrid.length; j++){
				//if (gameGrid[i][j] == 2){
					//x = j;
				//}
			//}
		//}
		
		for (int i = 0; i< gameGrid.length; i++){
			if(gameGrid[gameGrid.length-1][i] == 0){
				gameGrid[gameGrid.length-1][i] = 2;
				x = i;
			}
		}
		return x;
	}
	
	/**
	 * Gets the starting value of y from the maze
	 * @return y
	 */
	public int getStartY(){
		int y = 0;
		for (int i = 0; i< gameGrid.length; i++){
			for (int j = 0; j< gameGrid.length; j++){
				if (gameGrid[i][j] == 2){
					y = i;
				}
			}
		}
		return y;
	}
	@Override
	public int[][] getWholeGameGrid() {
		return gameGrid;
	}
	
	/**
	 * Tests if you can move in a direction, and manipulates the grid
	 * For input, 0 is right, 1 is up, 2 is left, 3 is down
	 * @return 0 if couldn't move that way, 1 if could, 2 if you won
	 */
	public int tryMove(int direction){
		if (direction == 0){
			if (locX+1 >= gameGrid.length){
				status = 0;
			}
			else if(gameGrid[locY][locX + 1] == 1){
				status = 0;
			}
			else if(gameGrid[locY][locX + 1] == 3){
				status = 2;
			} 
			else{
				status = 1;
				gameGrid[locY][locX] = 0;
				gameGrid[locY][locX + 1] = 2;
				locX = locX+1;
			}
		}
		else if (direction == 1){
			if (locY-1 < 0){
				status = 0;
			}
			else if(gameGrid[locY-1][locX] == 1){
				status = 0;
			}
			else if(gameGrid[locY-1][locX] == 3){
				status = 2;
			} 
			else{
				status = 1;
				gameGrid[locY][locX] = 0;
				gameGrid[locY-1][locX] = 2;
				locY = locY-1;
			}
		}
		else if (direction == 2){
			if (locX-1 < 0){
				status = 0;
			}
			else if(gameGrid[locY][locX-1] == 1){
				status = 0;
			}
			else if(gameGrid[locY][locX-1] == 3){
				status = 2;
			} 
			else{
				status = 1;
				gameGrid[locY][locX] = 0;
				gameGrid[locY][locX-1] = 2;
				locX = locX-1;
			}
		}
		else if (direction == 3){
			if (locY+1 >= gameGrid.length){
				status = 0;
			}
			else if(gameGrid[locY+1][locX] == 1){
				status = 0;
			}
			else if(gameGrid[locY+1][locX] == 3){
				status = 2;
			} 
			else{
				status = 1;
				gameGrid[locY][locX] = 0;
				gameGrid[locY+1][locX] = 2;
				locY = locY+1;
			}
		}
		return status;
	}
	
	/**
	 * Gets the current X coordinate of the player
	 * @return x coordinate of the player
	 */
	public int getLocX(){
		return locX;
	}
	
	/**
	 * Gets the current Y coordinate of the player
	 * @return y coordinate of the player
	 */
	public int getLocY(){
		return locY;
	}
	
}
