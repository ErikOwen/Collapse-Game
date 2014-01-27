import java.io.*;
import java.util.*;

/**
 * CollapseConsole is the text-based console user interface to the Collapse game.
 * The main() method starts the application running using System.in and System.out.
 * CollapseConsole can also return the top five high scores from the Hall of Fame.
 * 
 * @author Erik Owen
 * @version 1
 */
public class CollapseConsole
{
    private static InputStreamReader rdr;
    private static OutputStreamWriter wtr;
    /*How many hall of fame entries to return */
    public final static int kHallSize = 5;
    /*Path to high scores directory*/
    private final static String kHallOfFameDirPath = "collapse";
    /*Path to high scores file*/
    private final static String kHallOfFamePath = "collapse/halloffame.ser";
    private final static int kNumBoards = 5001;
    private final static int kRestart = 1;
    private final static int kNewGame = 2;
    private final static int kSelectGame = 3;
    private final static int kScores = 4;
    private final static int kCheet = 5;
    private final static int kQuit = 6;
    private final static int kCharToInt = 65;
    private int boardNum;
    
    /** Entry point for the application.
     *
     *  @param args ignored     
     */
    public static void main(String[] args)
    {   
        CollapseConsole app = new CollapseConsole(); 
        app.setIOsources(new InputStreamReader(System.in), 
                         new OutputStreamWriter(System.out));
        app.run();
    }

    /** Construct a new instance of this class.
     * Create the game components and initialize them,
     * create the user interface and connect it to the game.
     */
    public CollapseConsole()
    {
        this.boardNum = new Random().nextInt(kNumBoards);
    }
    
    /** Set input/output sources for Stream-based user interfaces.
     * @param rdr A Reader from which user input is obtained.
     * @param wtr A Writer to which program output is displayed.
     */
    public void setIOsources(Reader rdr, Writer wtr) 
    {
        this.rdr = (InputStreamReader)rdr;
        this.wtr = (OutputStreamWriter)wtr;
    }

    /** Run the console user interface, using the i/o streams provided by
     *  setIOsources();
     *  @pre setIOsources() has been called.
     */
    public void run()
    {
        CollapseGame game = new CollapseGame(8, this.boardNum);
        char[][] board;
        String userInput;
        int userChoice = 0;
        Scanner scan = new Scanner(rdr);
        
        try
        {
            while(userChoice != kQuit)
            {
                board = game.getCharacterBoard();
                wtr.write("Collapse - board " + this.boardNum + "\n");
                wtr.write("Tiles left: 64    Moves: 0  \n");
                displayBoard(game, board);
                wtr.write("1)Restart 2)New Game 3)Select Game 4)Scores 5)Cheat 6)Quit \n");
                wtr.flush();
                
                userInput = scan.nextLine();
                
                if(Character.isLetter(userInput.charAt(0)))
                {
                    if(userInput.length() == 2)
                    {
                        int row = userInput.substring(0, 1).toUpperCase().charAt(0) - kCharToInt;
                        int column = Integer.parseInt(userInput.substring(1, 2)) - 1;
                        if (row < board.length && column < board[0].length)
                        {
                            game.takeTurn(row, column);
                        }
                    }
                }
                else
                {
                    userChoice = Integer.parseInt(userInput);
                    
                    switch(userChoice)
                    {
                        case 1:
                            //restart the game
                        
                        break;
                        case 2:
                        //Start a new game on this board
                        
                        break;
                        case 3:
                            //Select a game
                        
                        break;
                        case 4:
                            //View high scores
                        
                        break;
                        case 5:
                            //cheat
                        
                        break;
                        case 6:
                            //exit
                            System.exit(0);
                        break;
                }
                }
            }
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        
    }
    
    private void displayBoard(CollapseGame game, char[][] board)
    {
        try
        {
            String colString = "     ";
            for(int colIter = 1; colIter <= board[0].length; colIter++)
            {
                colString = colString.concat(colIter + "  ");
            }
            colString = colString.concat("\n");
            wtr.write(colString);
        
            char curLetter = 'A';
            String curRow = "";
            
            for(int rowIter = 0; rowIter < board.length; rowIter++, curLetter++)
            {
                curRow = curRow.concat(" " + curLetter + ":  ");
                
                for(int colIter = 0; colIter < board[0].length; colIter++)
                {
                    curRow = curRow.concat(board[rowIter][colIter] + "  ");
                }
                
                curRow = curRow.concat("\n");
                wtr.write(curRow);
                curRow = "";
                
            }
            
            String dashedLine = " ----";
            for(int dashNdx = 0; dashNdx < board[0].length - 1; dashNdx++)
            {
                dashedLine = dashedLine.concat("---");
            }
            
            dashedLine = dashedLine.concat("-\n");
            wtr.write(dashedLine);
            
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    /** Adds a new high score to the high scores file
     * 
     * @param name The name of the score with the high score
     * @param score The score the player receieved
     */
    protected void addHighScore(String name, int score)
    {
        File highScoresDirectory = new File(kHallOfFameDirPath);
        File highScoresFile = new File(kHallOfFamePath);
        boolean createdDir = false, createdFile = false;
        try
        {
            if(!highScoresFile.exists())
            {
                /*Creates the high scores directory if it does not exist*/
                if(!highScoresDirectory.exists())
                {
                    createdDir = highScoresDirectory.mkdir();
                }
                
                createdFile = highScoresFile.createNewFile();
            }
            
            /*Writes to the high score file*/
            FileWriter highScoreWriter = new FileWriter(highScoresFile, true);
            highScoreWriter.write(score + " " + name + "\n");
            highScoreWriter.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

    }
    
    private String parseAndSortHighScores(File highScoresFile)
    {
        String highScoresString = "";
        ArrayList<HighScore> scoresList = new ArrayList<HighScore>();
        
        try
        {
            Scanner scan = new Scanner(highScoresFile);
            while(scan.hasNextLine())
            {
                String curLine = scan.nextLine();
                scoresList.add(new HighScore(Integer.parseInt(curLine.substring(0, curLine.indexOf(" "))), curLine.substring(curLine.indexOf(" ") + 1, curLine.length())));
            }
            
            Collections.sort(scoresList, new HighScoreComparator());
            
            for(int scoreNdx = 0; scoreNdx < kHallSize && scoreNdx < scoresList.size(); scoreNdx++)
            {
                HighScore curScore = scoresList.get(scoreNdx);
                highScoresString = highScoresString.concat(curScore.getScore() + " " + curScore.getName() + "\n");
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        
        return highScoresString;
    }
    
    /** Return a string representation of the top five high scores. 
     *  @return string is the top scores, one per line, with the
     *  score and name (in that order), separated by one or more blanks.
     *  Name is twenty characters max.  Leading blanks are allowed.
     */
    public String getHighScores()
    {
        File highScoresFile = new File(kHallOfFamePath);
        String highScoresString = "Hall of Fame not found: collapse/halloffame.ser.";
        
        try
        {
            if (highScoresFile.exists())
            {
                highScoresString = parseAndSortHighScores(highScoresFile);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        
        return highScoresString;
    }
}