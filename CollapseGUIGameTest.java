import java.awt.*;
import java.io.*;
import java.util.Scanner;
/**
 * Have PlaybackRobot run CollapseGUI then check the high scores list.
 * Assumes board size 8 is set in preferences.ini.
 *
 * @author  jdalbey
 * @version 2014.1.15
 */
public class CollapseGUIGameTest extends junit.framework.TestCase
{
    private CollapseGUI gui;
    /**
     * Default constructor for test class CollapseGUITest
     */
    public CollapseGUIGameTest()
    {
    }
    /**
     * Sets up the test fixture.
     *
     * Called before every test case method.
     */
    protected void setUp()    
    {
        // Delete any hall of fame file before each test.
        try{
            File file = new File("collapse/halloffame.ser");
            file.delete();
        }catch(Exception e){
            e.printStackTrace();
        }        
        // Start the application
        gui = new CollapseGUI();
        gui.run();
    }
    /* Private method that runs the robot */
    private void runTest(String input, String expected, String errMsg) 
    {
        Scanner scan = new Scanner(new StringReader(input));
        PlaybackRobot robot = new PlaybackRobot(scan);
        //robot.setLogging(true);
        robot.runScript();
        String scores = gui.getHighScores();
        // remove blanks and newlines for comparision
        scores = scores.replaceAll(" ","").replaceAll("\\n","");
        assertEquals(errMsg, expected, scores);
    }

    public void testGUIrestart()
    {
        String script = "press_key alt\n type_key r\n release_key alt\n  wait 100\n"
                      + "press_key alt\n type_key q\n release_key alt\n  wait 100\n";
        String expected = "";
        String errMsg = "Failed launching and quitting";
        runTest(script, expected, errMsg);
    }
    
    public void testGUIquit()
    {
        String script = "press_key alt\n type_key q\n release_key alt\n  wait 100\n";
        String expected = "";
        String errMsg = "Failed launching and quitting";
        runTest(script, expected, errMsg);
    }

    public void testGUIcheat()
    {
        String expected = "1Snoopy";
        String errMsg = "Failed cheating";
        runTest(cheatwin+quitGame, expected, errMsg);
    }

    public void testwinBoard50()
    {
        String expected = "12Linus";
        String errMsg = "Failed winning board 50";
        runTest(winBoard50, expected, errMsg);
    }
    
    public void testBoard2()
    {
        String expected = "10Lucy";
        String errMsg = "Failed winning board 2";
        runTest(startBoard2+winBoard2+quitGame, expected, errMsg);
    }        

    public void testNewGame()
    {
        String expected = "10Lucy11Snoopy";
        String errMsg = "Failed winning board 2 after new game";
        runTest(newGame+winBoard2+cheatwin+showHall+quitGame, expected, errMsg);
    }
    
    String newGame = 
          " press_key alt\n type_key g\n release_key alt\n  wait 200\n"         // Select Game 5000
        + " type_string 5000\n type_key enter\n  wait 200\n"
        +  " press_key alt\n type_key n\n release_key alt\n  wait 200\n"         // New game: 1
        +  " press_key alt\n type_key n\n release_key alt\n  wait 200\n";         // New game: 2
        
    String startBoard2 = 
          " press_key alt\n type_key g\n release_key alt\n  wait 200\n"         // Select Game 50
        + " type_string 2\n type_key enter\n  wait 200\n";
        
    String winBoard2 =         
          " type_key right\n type_key space\n"
        + " press_key alt\n type_key r\n release_key alt\n  wait 100\n" // restart        
        + " type_key right\n type_key right\n type_key right\n type_key right\n"                       //  move to H4
        + " type_key down\n type_key down\n type_key down\n type_key down\n type_key down\n type_key down\n type_key down\n"
        + " type_key space\n type_key space\n type_key space\n type_key space\n type_key space\n type_key space\n type_key space\n"
        + " type_key left\n type_key space\n type_key space\n type_key space\n wait 200\n"
        + " type_key y \n type_key enter\n wait 500\n"
        + " type_string Lucy\n type_key enter\n  wait 500\n"
        + " type_key left\n type_key left\n type_key left\n "        // return cursor to upper left
        + " type_key up\n type_key up\n type_key up\n type_key up\n type_key up\n type_key up\n type_key up\n";
        
    String winBoard50 = 
          " press_key alt\n type_key g\n release_key alt\n  wait 200\n"         // Select Game 50
        + " type_string 50\n type_key enter\n  wait 200\n"                      // 
        + " type_key right\n type_key right\n type_key right\n type_key right\n"                       //  tab to H4
        + " type_key down\n type_key down\n type_key down\n type_key down\n type_key down\n type_key down\n type_key down\n"
        + " type_key space\n  type_key tab\n"                                   // click H4, then H5 11 times
        + " type_key space\n type_key space\n type_key space\n type_key space\n type_key space\n type_key space\n type_key space\n type_key space\n type_key space\n type_key space\n type_key space\n wait 200\n"
        + " type_key y \n type_key enter\n wait 500\n"
        + " type_string Linus\n type_key enter\n  wait 500\n"
        + " press_key alt\n type_key q\n release_key alt\n  wait 500\n";

    String showHall = 
           // Show hall of fame dialog for three seconds
          " press_key alt\n type_key s\n release_key alt\n  wait 3000\n type_key space\n  wait 100\n";

    String cheatwin = " press_key alt\n type_key c\n release_key alt\n"
        + "type_key tab\n type_key space\n wait 200\n"
        + " type_key y \n type_key enter\n wait 500\n"
        + " type_string Snoopy\n type_key enter\n  wait 500\n";
        
    String quitGame = " press_key alt\n type_key q\n release_key alt\n  wait 100\n";

}