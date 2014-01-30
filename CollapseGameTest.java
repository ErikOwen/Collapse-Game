

/**
 * The test class CollapseGameTest.
 *
 * @author  Erik Owen
 * @version 1
 */
public class CollapseGameTest extends junit.framework.TestCase
{
    /**
     * Default constructor for test class CollapseGameTest
     */
    public CollapseGameTest()
    {
    }

     /**
     * Sets up the test fixture.
     *
     * Called before every test case method.
     */
    public void test1()
    {
        CollapseGame game = new CollapseGame(2, 8);
        CollapsePiece[][] tBoard = game.getTileBoard();
        char[][] cBoard = game.getCharacterBoard();
        assertFalse(game.isGameOver());
        assertEquals(tBoard[0][0], CollapsePiece.purple);
        assertEquals(cBoard[0][0], 'x');
        assertEquals(tBoard[0][1], CollapsePiece.purple);
        assertEquals(cBoard[0][1], 'x');
        assertEquals(tBoard[1][0], CollapsePiece.purple);
        assertEquals(cBoard[1][0], 'x');
        assertEquals(tBoard[1][1], CollapsePiece.purple);
        assertEquals(cBoard[1][1], 'x');
        game.takeTurn(0, 0);
        assertEquals(tBoard[0][0], CollapsePiece.empty);
        assertEquals(cBoard[0][0], ' ');
        assertEquals(tBoard[0][1], CollapsePiece.empty);
        assertEquals(cBoard[0][1], ' ');
        assertEquals(tBoard[1][0], CollapsePiece.empty);
        assertEquals(cBoard[1][0], ' ');
        assertEquals(tBoard[1][1], CollapsePiece.empty);
        assertEquals(cBoard[1][1], ' ');
        assertTrue(game.isGameOver());
        CollapseGame game2 = new CollapseGame(2, 2);
        CollapsePiece[][] tBoard2 = game2.getTileBoard();
        char[][] cBoard2 = game2.getCharacterBoard(); 
        assertEquals(tBoard2[0][0], CollapsePiece.purple);
        assertEquals(cBoard2[0][0], 'x');
        assertEquals(tBoard2[0][1], CollapsePiece.green);
        assertEquals(cBoard2[0][1], '+');
        assertEquals(tBoard2[1][0], CollapsePiece.cyan);
        assertEquals(cBoard2[1][0], 'o');
        assertEquals(tBoard2[1][1], CollapsePiece.purple);
        assertEquals(cBoard2[1][1], 'x');    
    }

    /**
     * Tests some of the CollapseGame functionality
     */
    public void test2()
    {
        CollapseGame game = new CollapseGame(8, 2);
        CollapsePiece[][] tBoard = game.getTileBoard();
        char[][] cBoard = game.getCharacterBoard();
        assertEquals(0, game.getNumberOfMoves());
        game.takeTurn(7, 4);
        game.takeTurn(7, 4);
        game.takeTurn(7, 4);
        game.takeTurn(7, 4);
        game.takeTurn(7, 4);
        game.takeTurn(7, 4);
        game.takeTurn(7, 4);
        assertFalse(game.isGameOver());
        assertFalse(game.takeTurn(-1, -1));
        assertFalse(game.takeTurn(11, 11));
        game.takeTurn(7, 3);
        game.takeTurn(7, 3);
        assertFalse(game.isGameOver());
        game.takeTurn(7, 3);
        assertEquals(10, game.getNumberOfMoves());
        assertTrue(game.isGameOver());
    }

    /**
     * Tests some of the CollapseGame functionality
     */
    public void test3()
    {
        CollapseGame game = new CollapseGame(8, 44);
        CollapsePiece[][] tBoard = game.getTileBoard();
        char[][] cBoard = game.getCharacterBoard();
        assertFalse(game.hasAdjacentTiles(0, 0));
        assertTrue(game.hasAdjacentTiles(7, 7));
        game.takeTurn(0, 7);    
        assertFalse(game.hasAdjacentTiles(1, 4));
    }

    /**
     * Tests some of the CollapseGame functionality
     */
    public void test4()
    {
        CollapseGame game = new CollapseGame(8, 4616);
        CollapsePiece[][] tBoard = game.getTileBoard();
        char[][] cBoard = game.getCharacterBoard();  
        game.takeTurn(6, 0);
        game.takeTurn(6, 0);
        game.takeTurn(6, 0);
        game.takeTurn(6, 0);
        game.takeTurn(7, 7);
        game.takeTurn(7, 7);
        game.takeTurn(7, 5);
        game.takeTurn(7, 5);
        game.takeTurn(7, 5);
        game.takeTurn(7, 5);
        game.takeTurn(7, 5);
        game.takeTurn(7, 5);
        game.takeTurn(7, 5);
        game.takeTurn(7, 4);
        game.takeTurn(7, 4);
        game.takeTurn(7, 4);
        game.takeTurn(7, 4);
        game.takeTurn(7, 4);  
        assertFalse(game.isGameOver());
        CollapseGame game1 = new CollapseGame(3, 3);
        game.takeTurn(0, 0);
        game.takeTurn(0, 2);
        game.takeTurn(2, 0);
        game.takeTurn(2, 2);
    }
}




