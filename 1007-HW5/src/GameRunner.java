/**
 * Interface for running the game
 * @author Hannah
 *
 */
public interface GameRunner {
	/**
	 * Returns the current during the game
	 * @return
	 */
	int [][] getWholeGameGrid();	
	
	/**
	 * Sees if a move to be made. If one can, it modifies the grid
	 * @param direction
	 * @return the status-- invalid, possible, or winning move
	 */
	int tryMove(int direction);
}
