
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

/**
 * A class to create a grid for the maze, with the beginning, the ending, and where there is corn and where there is a path.
 * @author Nathan Booth
 * 
 */
public class MazeCreator_old implements Maze, Serializable{		


	/**
	 * @return Returns an n x n array of integers representing the entire maze.
	 */
	public int[][] getWholeGrid() {
		int size = 20;
		ArrayList<int[]> previousSteps = new ArrayList<int[]>();
		int[][] grid = new int[size][size];
		int[] startLocation = {size-1, size/2};
		int[] currentLocation = startLocation.clone();
		int[] endLocation = new int[2];
		for (int y = 0; y < size; y++) {
			for (int x = 0; x < size; x++) {
				int value = 1;
				if (x==size/2 && y==size-1) {
					value = 0;
				}
				grid[y][x] = value;
			}
		}

		Random generator = new Random();
		int count = 0;
		int numberOfMoves = (int)((Math.pow(size, 2))/4);
		for (int i = 0; i < numberOfMoves; i++) {

			previousSteps.add(currentLocation);
			boolean needMove = true;
			int[] prospieMove = new int[2];
			boolean turnAround = false;
			while (needMove) {
				prospieMove = currentLocation.clone();
				int direction = generator.nextInt(4);
				if (direction==0) {
					prospieMove[0] = currentLocation[0];
					prospieMove[1] = currentLocation[1]+1;
				}
				if (direction==1) {
					prospieMove[0] = currentLocation[0]-1;
					prospieMove[1] = currentLocation[1];
				}					
				if (direction==2) {
					prospieMove[0] = currentLocation[0];
					prospieMove[1] = currentLocation[1]-1;
				}					
				if (direction==3) {
					prospieMove[0] = currentLocation[0]+1;
					prospieMove[1] = currentLocation[1];

				}
				if (0 < prospieMove[0] && prospieMove[0] < size - 1 && 0 < prospieMove[1] && prospieMove[1] < size - 1) {
					
					if ((grid[prospieMove[0]+1][prospieMove[1]] == 0 && grid[prospieMove[0]+1][prospieMove[1]-1] == 0
							&& grid[prospieMove[0]+1][prospieMove[1]+1] == 0 && grid[prospieMove[0]][prospieMove[1]-1] == 0)
							|| (grid[prospieMove[0]+1][prospieMove[1]] == 0 && grid[prospieMove[0]+1][prospieMove[1]-1] == 0 
							&& grid[prospieMove[0]][prospieMove[1]+1] == 0 && grid[prospieMove[0]-1][prospieMove[1]-1] == 0)
							|| (grid[prospieMove[0]+1][prospieMove[1]-1] == 0 && grid[prospieMove[0]][prospieMove[1]-1] == 0 
							&& grid[prospieMove[0]-1][prospieMove[1]-1] == 0 && grid[prospieMove[0]-1][prospieMove[1]] == 0) 
							|| (grid[prospieMove[0]][prospieMove[1]-1] == 0 && grid[prospieMove[0]-1][prospieMove[1]-1] == 0 
							&& grid[prospieMove[0]-1][prospieMove[1]] == 0 && grid[prospieMove[0]-1][prospieMove[1]+1] == 0) 
							|| (grid[prospieMove[0]-1][prospieMove[1]-1] == 0 && grid[prospieMove[0]-1][prospieMove[1]] == 0 
							&& grid[prospieMove[0]-1][prospieMove[1]+1] == 0 && grid[prospieMove[0]][prospieMove[1]+1] == 0) 
							|| (grid[prospieMove[0]-1][prospieMove[1]] == 0 && grid[prospieMove[0]-1][prospieMove[1]+1] == 0 
							&& grid[prospieMove[0]][prospieMove[1]+1] == 0 && grid[prospieMove[0]+1][prospieMove[1]+1] == 0) 
							|| (grid[prospieMove[0]-1][prospieMove[1]+1] == 0 && grid[prospieMove[0]][prospieMove[1]+1] == 0 
							&& grid[prospieMove[0]+1][prospieMove[1]+1] == 0 && grid[prospieMove[0]+1][prospieMove[1]] == 0) 
							|| (grid[prospieMove[0]][prospieMove[1]+1] == 0 && grid[prospieMove[0]+1][prospieMove[1]+1] == 0 
							&& grid[prospieMove[0]+1][prospieMove[1]] == 0 && grid[prospieMove[0]+1][prospieMove[1]-1] == 0)) {
					}
					else {
						needMove = false;	
					}
				}
				count++;
				if (count > 1000) {
					turnAround = true;
					break;
				}

			}
			if (turnAround) {
				turnAround = false;

				prospieMove = previousSteps.get(generator.nextInt(previousSteps.size())).clone();
				currentLocation = prospieMove.clone();
				if (i == numberOfMoves - 1) {
					int[] endSpot = previousSteps.get(previousSteps.size() - 1).clone();
					endLocation[0] = endSpot[0];
					endLocation[1] = endSpot[1];
				}
				continue;
			}
			currentLocation = prospieMove.clone();

			if (grid[currentLocation[0]][currentLocation[1]] == 0) {
				i -= 1;
			}
			if (i == numberOfMoves - 1) {
				endLocation[0] = currentLocation[0];
				endLocation[1] = currentLocation[1];
			}
			else {
				grid[currentLocation[0]][currentLocation[1]] = 0;
			}


		}

		int i = previousSteps.size() - 1;
		int[] newEndLocation = endLocation.clone();
		while (Math.sqrt(Math.pow(newEndLocation[0]-startLocation[0],2) + Math.pow(newEndLocation[1]-startLocation[1], 2)) < size / 2) {
			newEndLocation = previousSteps.get(i-1).clone();
			i -= 1;
			if (i==0) {
				newEndLocation = endLocation.clone();
				break;
			}
		}
		grid[newEndLocation[0]][newEndLocation[1]] = 3;
		return grid;
	}
}
