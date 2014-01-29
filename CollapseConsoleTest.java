import java.io.*;

/**
 * The test class CollapseConsoleTest.
 *
 * @author  Erik Owen
 * @version 1
 */
public class CollapseConsoleTest extends junit.framework.TestCase
{
    /**
     * Default constructor for test class CollapseConsoleTest
     */
    public CollapseConsoleTest()
    {

    }

    /**
     * Tests the highscores functionality
     */
    public void testHighScores()
    {
        try
        {
            CollapseConsole console1 = new CollapseConsole();
            console1.addHighScore("Frank", 2);
            console1.addHighScore("Susan", 1);
        
            assertEquals("1 Susan\n2 Frank\n", console1.getHighScores());
        
            CollapseConsole console = new CollapseConsole();
            console.addHighScore("Erik", 10);
            console.addHighScore("Ferguson", 8);
            console.addHighScore("Andrea", 1);
            console.addHighScore("Dingus", 100);
        
            String expected = "1 Andrea\n1 Susan\n2 Frank\n8 Ferguson\n10 Erik\n";
            String actual = console.getHighScores();
            assertEquals(expected, actual);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    
    
    /**
     * Mainly tests the run method
     */
	public void test2()
	{
	    try
	    {
	        CollapseConsole app = new CollapseConsole();
	        app.setIOsources(new FileReader(new File("testData/testInput.txt")), new OutputStreamWriter(System.out));
	        app.run();
	    }
	    catch(Exception e)
	    {
	        System.out.println("Test failed");
	    }
	}
}


