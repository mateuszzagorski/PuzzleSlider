package slidingPuzzle;

/*
 * Puzzle przesuwne (n^2-1)
 * Zamiast BestFirstSearch() uzyc A*
 * 
 * POBRAC SAC-userguide.pdf
*/

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Arrays;
import java.util.Iterator;

import sac.State;
import sac.StateFunction;
import sac.graph.AStar;
import sac.graph.GraphSearchAlgorithm;
import sac.graph.GraphState;
import sac.graph.GraphStateImpl;

public class Puzzle extends GraphStateImpl {
	public final int n = 3;
	protected byte[][] board = null;
	
	private int moveUp = 1, moveDown = 2, moveLeft = 3, moveRight = 4;
	private String SMoveUp = "Up", SMoveDown = "Down", SMoveLeft = "Left", SMoveRight = "Right";
    private int x = 0, y = 0;
	int posX = 1;
	int posY = 1;
	protected byte emptyIndex;
    
	public Puzzle() {
		board = new byte[n][n];
		byte value = 0;
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				board[i][j] = value++;
			}
		}
		x = 0;
		y = 0;
	}

	public Puzzle(Puzzle parent) {
		board = new byte[n][n];
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				board[i][j] = parent.board[i][j];
			}
		}
        x = parent.x;
        y = parent.y;
	}
    public int getN()
    {
        return this.n;
    }
    public int getX()
    {
        return this.x;
    }
    public int getY()
    {
        return this.y;
    }
    public byte[][] getBoard()
    {
        return this.board;
}
	@Override
	public int hashCode() {
		byte[] linear = new byte[n*n];
		int k = 0;
		for (int i = 0; i < n; i++) {
			for(int j = 0; j < n; j++) {
				linear[k++] = board[i][j];
			}
		}
		return Arrays.hashCode(linear);
	}
	
	@Override
	public boolean isSolution() {
		int x = 0;
		for (int i = 0; i < n; i++)
			for (int j = 0; j < n; j++) {
				if (board[i][j] != x) {
					return false;
				}
				x++;
			}
		return true;
	}
	
	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		int x = 1, y = 1;

		for (int i = 0; i < n; i++) {
			result.append("            ");
			for (int j = 0; j < n; j++) {
				result.append(board[i][j]);
				result.append(" ");
				if (x % n == 0) {
					result.append("  ");
				}
				x++;
			}
			y++;
			result.append("\n");
		}
		return result.toString();
	}
	
	public void FromString(String txt) {
		int k = 0;
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				board[i][j] = Byte.valueOf(txt.substring(k, k + 1));
				k++;
			}
		}
	}
	
	@Override
	public List<GraphState> generateChildren() {
		List<GraphState> list = new ArrayList<GraphState>();
		Iterator<Integer> it = SearchForAvailableMoves().listIterator();
		while (it.hasNext()) {
			Puzzle child = new Puzzle(this);
			child.makeMove(it.next());
			String moveName = "";
			if (x - 1 == child.x)
				moveName = "L";
			else if (x + 1 == child.x)
				moveName = "R";
			else if (y - 1 == child.y)
				moveName = "D";
			else if (y + 1 == child.y)
				moveName = "U";
			child.setMoveName(moveName);
			list.add(child);
		}
		return list;
	}

    public ArrayList<Integer> SearchForAvailableMoves() {
        List<Integer> AvailableMoves = new ArrayList<Integer>();
        if(x-1>=0) {
        	AvailableMoves.add(moveUp);
        }
        if (x+1<n) {
        	AvailableMoves.add(moveDown);
        }
        if (y-1>=0) {
        	AvailableMoves.add(moveLeft);
        }
        if (y+1<n) {
        	AvailableMoves.add(moveRight);
        }
        return (ArrayList<Integer>) AvailableMoves;
    }
    
	public void Mixing(Puzzle puzzle, int steps) {
    	byte counter = 0;
    	for(int i=0; i<n; i++) {
    		for(int j=0; j<n; j++) {
    			board[i][j]=counter;
    			counter++;
    		}
    	}
    	Random random = new Random();
    	for(int i = 1; i<=steps; i++) {
    		System.out.println("-------------------------------");
    		System.out.println(" --STEP " + i + " OUT OF " + steps + " STEPS--");
    		System.out.println("-------------------------------");

			ArrayList <Integer> possibleSteps = puzzle.SearchForAvailableMoves();
			int step = possibleSteps.get(random.nextInt(possibleSteps.size()));
			
			makeMove(step);
			
            if (step == moveUp) {
                System.out.println("        |    " + SMoveUp + "    |");
                System.out.println("         __________");
                posY--;
            } else if (step == moveDown) {
                System.out.println("        |   " + SMoveDown + "   |");
                System.out.println("         __________");
                posY++;
            } else if (step == moveRight) {
                System.out.println("        |   " + SMoveRight + "  |");
                System.out.println("         __________");
                posX++;
            } else if (step == moveLeft) {
                System.out.println("        |   " + SMoveLeft + "   |");
                System.out.println("         __________");
                posX--;
            }
            System.out.println(puzzle.toString());
            System.out.println("Pozycja 0:  X: " + posX + "  Y: " + posY + "\n");
    	}
    }
    
    public void makeMove(int step) {
    	int temp;
        if(step == moveUp) {
            temp = x-1;
            board[x][y] = board[x-1][y];
            board[x-1][y] = 0;
            x = temp;
        }
        if (step == moveDown) {
            temp = x+1;
            board[x][y] = board[x+1][y];
            board[x+1][y] = 0;
            x = temp;
        }
        if(step == moveLeft) {
            temp = y-1;
            board[x][y] = board[x][y-1];
            board[x][y-1] = 0;
            y = temp;
        }
        if(step == moveRight) {
            temp = y+1;
            board[x][y] = board[x][y+1];
            board[x][y+1] = 0;
            y = temp;
        }
    }
    static
    {
        setHFunction(new StateFunction());
    }
	public static void main(String[] arg) {
		for (int i = 0; i < 4; i++) {

			Puzzle puzzle = new Puzzle();
			
			puzzle.Mixing(puzzle, 10);
			StateFunction[] heuristics = {new HFunctionMisplacedTiles(), new HFunctionManhattan()};
			for(StateFunction h: heuristics)
			{
				Puzzle.setHFunction(h);
				GraphSearchAlgorithm alg = new AStar(puzzle);
				alg.execute();
				
				Puzzle solution = (Puzzle)alg.getSolutions().get(0);
				System.out.println("\n\nSOLUTION:\n");
				System.out.println(solution);
				//System.out.println("Wrong placed tiles: " + puzzle.MissPlacedtiles());
				
				System.out.println("Time: " + alg.getDurationTime() );
				System.out.println("Closed: " + alg.getClosedStatesCount());
				System.out.println("Open: " + alg.getOpenSet().size());
				System.out.println("Solution: " + alg.getSolutions().size());
				System.out.println("Path length: " + solution.getPath().size());
				System.out.println(solution.getG());
				//System.out.println(solution.getPath());
				System.out.println(solution.getMovesAlongPath());
                System.out.println("Heuristic: " + h.getClass());
System.out.println("H: " + solution.getH() + "\n-------------------------------");
			}
		}
	}
}
