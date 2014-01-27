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
    private int boardSize;

    /**
     * Constructor for objects of class CollapseGame
     */
    public CollapseGame(int dimension, int boardNumber)
    {
        this.boardSize = dimension;
        this.tileBoard = new CollapsePiece[boardSize][boardSize];
        this.characterBoard = new char[boardSize][boardSize];
        
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
        return this.characterBoard;
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
                    characterBoard[row][col] = 'G';
                }
                if(tileBoard[row][col] == CollapsePiece.purple)
                {
                    characterBoard[row][col] = 'P';
                }
                if(tileBoard[row][col] == CollapsePiece.cyan)
                {
                    characterBoard[row][col] = 'C';
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
            }
            //Shifts the necesssary cells downwards to fill in blank spots
            //shiftCellsDownwards();
            //Shifts the columns to the center if there are any empty columns on the board
            //shiftColumnsToCenter();
        }
        
        return isGameOver();
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
            
            if(curSpot.getRowCoordinate() < tileBoard.length -1 && tileBoard[curSpot.getRowCoordinate() + 1][curSpot.getColumnCoordinate()] == curColor)
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
                    while(tileBoard[curSpot][colIter] == CollapsePiece.empty && curSpot < tileBoard.length - 1)
                    {
                        curSpot++;
                    }
                    
                    //Shift the current cell down one
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
                while(columnIsEmpty(curCol) && curCol < boardSize)
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
        
        //Shift the cells left of the center to the center, if needed
        for(int ndx = centerCol; ndx > 0; ndx--)
        {
            if(columnIsEmpty(ndx))
            {
                int curCol = ndx - 1;
                while(columnIsEmpty(curCol) && curCol >= 0)
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
}