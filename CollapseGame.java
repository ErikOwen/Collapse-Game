import java.util.*;
/**
 * This class represents the underlying data and functionality of
 * a collapse game.
 * 
 * @author Erik Owen
 * @version 1
 */
public class CollapseGame
{
    private CollapsePiece[][] tileBoard;
    private char[][] characterBoard;
    private CollapsePiece[][] savedTileBoard;
    private char[][] savedCharacterBoard;
    private int boardSize;
    private int numMoves;
    boolean isCheating;

    /**
     * Constructor for objects of class CollapseGame
     */
    public CollapseGame(int dimension, int boardNumber)
    {
        this.boardSize = dimension;
        this.tileBoard = new CollapsePiece[boardSize][boardSize];
        this.characterBoard = new char[boardSize][boardSize];
        this.savedTileBoard = new CollapsePiece[boardSize][boardSize];
        this.savedCharacterBoard = new char[boardSize][boardSize];
        this.numMoves = 0;
        this.isCheating = false;
        
        this.generateBoard(boardNumber);
    }

    /**
     * Accessor method to get the game board.
     * 
     * @return the game board object.
     */
    public CollapsePiece[][] getTileBoard()
    {
        return this.tileBoard;
    }
    
    /**
     * Accessor method to get the game board.
     * 
     * @return the game board object.
     */
    public char[][] getCharacterBoard()
    {
        return this.characterBoard.clone();
    }
    
    /**
     * Private method which generates a board from a given number, representing
     * the random seed.
     * 
     * @param boardNum the seed which the random generator will use to generate the board.
     */
    private void generateBoard(int boardNum)
    {

        CollapsePiece[] pieces = {CollapsePiece.green,CollapsePiece.purple,CollapsePiece.cyan,};
        java.util.Random generator = new java.util.Random(boardNum);
        for (int row = 0; row < tileBoard.length; row++)
        {
            for (int col = 0; col < tileBoard[0].length; col++)
            {
                // Generate one of three kinds of tiles
                tileBoard[row][col] = pieces[generator.nextInt(3)];
                
                if(tileBoard[row][col] == CollapsePiece.green)
                {
                    characterBoard[row][col] = '+';
                }
                if(tileBoard[row][col] == CollapsePiece.purple)
                {
                    characterBoard[row][col] = 'x';
                }
                if(tileBoard[row][col] == CollapsePiece.cyan)
                {
                    characterBoard[row][col] = 'o';
                }
            }
        }
    }
    
    /**
     * Determines if the player has won the game.
     * 
     * @return returns true if the player has one, false otherwise.
     */
    private boolean isGameOver()
    {
        boolean gameIsOver = true;
        for (int rowIter = 0; rowIter < characterBoard.length && gameIsOver; rowIter++)
        {
            for(int colIter = 0; colIter < characterBoard[0].length && gameIsOver; colIter++)
            {
                if(characterBoard[rowIter][colIter] != ' ')
                {
                    gameIsOver = false;
                }
            }
        }
        return gameIsOver;
    }
    
    /**
     * A turn is taken on the board.
     * 
     * @return boolean: true if game is over, false if it is still going
     */
    public boolean takeTurn(int rowPos, int colPos)
    {   
        if(tileBoard[rowPos][colPos] != CollapsePiece.empty)
        {
            if(hasAdjacentTiles(rowPos, colPos))
            {
                //Removes cell and all adjacent tiles of the same color
                removeSelection(rowPos, colPos);
                //Shifts the necesssary cells downwards to fill in blank spots
                shiftCellsDownwards();
                //Shifts the columns to the center if there are any empty columns on the board
                shiftColumnsToCenter();
            }
            
            numMoves++;
        }
        
        return isGameOver();
    }
    
    /**
     * Returns the number of tiles left the player needs to clear
     */
    public int getTilesLeft()
    {
        int tilesLeft = 0;
        
        for (int rowIter = 0; rowIter < tileBoard.length; rowIter++)
        {
            for(int colIter = 0; colIter < tileBoard[0].length; colIter++)
            {
                if(tileBoard[rowIter][colIter] != CollapsePiece.empty)
                {
                    tilesLeft++;
                }
            }
        }
        return tilesLeft;
    }
    
    /**
     * Returns the number of moves the play has made
     */
    public int getNumberOfMoves()
    {
        return this.numMoves;
    }
    
    /**
     * Checks to see if any of the adjacent tiles are of the same type
     */
    private boolean hasAdjacentTiles(int row, int col)
    {
        boolean hasAdjacentTiles = false;
        CollapsePiece color = tileBoard[row][col];
        
        if(row > 0 && tileBoard[row - 1][col] == color)
        {
            hasAdjacentTiles = true;
        }
        if(row < tileBoard.length - 1 && tileBoard[row + 1][col] == color)
        {
            hasAdjacentTiles = true;
        }
        if(col > 0 && tileBoard[row][col - 1] == color)
        {
            hasAdjacentTiles = true;   
        }
        if(col < tileBoard[0].length - 1 && tileBoard[row][col + 1] == color)
        {
            hasAdjacentTiles = true;
        }
        
        return hasAdjacentTiles;
    }
    
    /**
     * Removes the block clicked along with any adjacent pieces.
     */
    private void removeSelection(int rowPos, int colPos)
    {
        CollapsePiece curColor = tileBoard[rowPos][colPos];
        TileCoordinates curSpot;
        LinkedList<TileCoordinates> queue = new LinkedList<TileCoordinates>();
        queue.add(new TileCoordinates(rowPos, colPos));
        
        while(!queue.isEmpty())
        {
            curSpot = queue.remove();
            
            if(curSpot.getRowCoordinate() < tileBoard.length - 1 && tileBoard[curSpot.getRowCoordinate() + 1][curSpot.getColumnCoordinate()] == curColor)
            {
                queue.add(new TileCoordinates(curSpot.getRowCoordinate() + 1, curSpot.getColumnCoordinate()));
            }
            if(curSpot.getRowCoordinate() > 0 && tileBoard[curSpot.getRowCoordinate() - 1][curSpot.getColumnCoordinate()] == curColor)
            {
                queue.add(new TileCoordinates(curSpot.getRowCoordinate() - 1, curSpot.getColumnCoordinate()));
            }
            if(curSpot.getColumnCoordinate() < tileBoard[0].length - 1 && tileBoard[curSpot.getRowCoordinate()][curSpot.getColumnCoordinate() + 1] == curColor)
            {
                queue.add(new TileCoordinates(curSpot.getRowCoordinate(), curSpot.getColumnCoordinate() + 1));
            }
            if(curSpot.getColumnCoordinate() > 0 && tileBoard[curSpot.getRowCoordinate()][curSpot.getColumnCoordinate() - 1] == curColor)
            {
                queue.add(new TileCoordinates(curSpot.getRowCoordinate(), curSpot.getColumnCoordinate() - 1));
            }
            
            tileBoard[curSpot.getRowCoordinate()][curSpot.getColumnCoordinate()] = CollapsePiece.empty;
            characterBoard[curSpot.getRowCoordinate()][curSpot.getColumnCoordinate()] = ' ';
        }
    }
    
    /**
     * Shifts the remaining cells downwards if there is an open spot below them
     */
    private void shiftCellsDownwards()
    {
        for(int rowIter = tileBoard.length - 2; rowIter >= 0; rowIter--)
        {
            for(int colIter = 0; colIter < tileBoard[0].length; colIter++)
            {
                if(tileBoard[rowIter][colIter] != CollapsePiece.empty && tileBoard[rowIter + 1][colIter] == CollapsePiece.empty)
                {
                    int curSpot = rowIter + 1;
                    while(curSpot < tileBoard.length - 1 && tileBoard[curSpot + 1][colIter] == CollapsePiece.empty)
                    {
                        curSpot++;
                    }
                    
                    
                    tileBoard[curSpot][colIter] = tileBoard[rowIter][colIter];
                    tileBoard[rowIter][colIter] = CollapsePiece.empty;
                    
                    characterBoard[curSpot][colIter] = characterBoard[rowIter][colIter];
                    characterBoard[rowIter][colIter] = ' ';
                }

            }
        }
    }
    
    /**
     * Shfits the columns towards the center if there are any empty columns
     */
    private void shiftColumnsToCenter()
    {
        int centerCol = boardSize / 2;
        
        //Shift the cells right of the center to the center if needed
        for(int ndx = centerCol; ndx < boardSize - 1; ndx++)
        {
            if(columnIsEmpty(ndx))
            {
                int curCol = ndx + 1;
                while(curCol < boardSize && columnIsEmpty(curCol))
                {
                    curCol++;
                }
                
                if(curCol < boardSize)
                {
                    for(int rowIter = 0; rowIter < tileBoard.length; rowIter++)
                    {
                        tileBoard[rowIter][ndx] = tileBoard[rowIter][curCol];
                        tileBoard[rowIter][curCol] = CollapsePiece.empty;
                        
                        characterBoard[rowIter][ndx] = characterBoard[rowIter][curCol];
                        characterBoard[rowIter][curCol] = ' ';
                    }
                }
            }
        }
        
        if(boardSize % 2 == 0)
        {
            centerCol--;
        }
        
        //Shift the cells left of the center to the center, if needed
        for(int ndx = centerCol; ndx > 0; ndx--)
        {
            if(columnIsEmpty(ndx))
            {
                int curCol = ndx - 1;
                while(curCol >= 0 && columnIsEmpty(curCol))
                {
                    curCol--;
                }
            
                if(curCol >= 0)
                {
                    for(int rowIter = 0; rowIter < tileBoard.length; rowIter++)
                    {
                        tileBoard[rowIter][ndx] = tileBoard[rowIter][curCol];
                        tileBoard[rowIter][curCol] = CollapsePiece.empty;
                        
                        characterBoard[rowIter][ndx] = characterBoard[rowIter][curCol];
                        characterBoard[rowIter][curCol] = ' ';
                    }
                }
            }
        }
    }
    
    /**
     * Checks to see if a column is empty.
     */
    private boolean columnIsEmpty(int column)
    {
        boolean isEmpty = true;
        
        for(int rowIter = 0; rowIter < tileBoard.length && isEmpty; rowIter++)
        {
            if(tileBoard[rowIter][column] != CollapsePiece.empty)
            {
                isEmpty = false;
            }
        }
        
        return isEmpty;
    }
    
    /**
     * Allows the player to cheat by removing the majority of the board
     */
    public void cheat()
    {
        if(!isCheating)
        {
            //Save both the boards in case if player undoes cheat
            for (int rowIter = 0; rowIter < tileBoard.length; rowIter++)
            {
                for(int colIter = 0; colIter < tileBoard[0].length; colIter++)
                {
                    savedTileBoard[rowIter][colIter] = tileBoard[rowIter][colIter];
                    savedCharacterBoard[rowIter][colIter] = characterBoard[rowIter][colIter];
                
                    tileBoard[rowIter][colIter] = CollapsePiece.empty;
                    characterBoard[rowIter][colIter] = ' ';
                }
            }
        
            tileBoard[0][0] = CollapsePiece.green;
            characterBoard[0][0] = '+';
            tileBoard[0][1] = CollapsePiece.green;
            characterBoard[0][1] = '+';
        
            isCheating = true;
        }
        else
        {
            for (int rowIter = 0; rowIter < tileBoard.length; rowIter++)
            {
                for(int colIter = 0; colIter < tileBoard[0].length; colIter++)
                {
                    tileBoard[rowIter][colIter] = savedTileBoard[rowIter][colIter];
                    characterBoard[rowIter][colIter] = savedCharacterBoard[rowIter][colIter];
                }
            }
            
            isCheating = false;
        }
    }
    
    /**
     * Determines if a the cell specified is empty or not.
     * 
     * @param row the row of the cell being checked
     * @param col the column of the cell being checked
     */
    public boolean cellEmpty(int row, int col)
    {
        return tileBoard[row][col] == CollapsePiece.empty;
    }
}
