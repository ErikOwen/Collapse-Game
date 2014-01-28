import java.io.*;
import java.util.*;
import org.ini4j.*;

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
    private static InputStreamReader reader;
    private static OutputStreamWriter writer;
    /*How many hall of fame entries to return */
    public final static int kHallSize = 5;
    /*Path to high scores directory*/
    private final static String kHallOfFameDirPath = "collapse";
    /*Path to high scores file*/
    private final static String kHallOfFamePath = "collapse/halloffame.ser";
    private final static String kPreferencesPath = "collapse/Preferences.ini";
    private final static int kNumBoards = 5001;
    private final static int kRestart = 1;
    private final static int kNewGame = 2;
    private final static int kSelectGame = 3;
    private final static int kScores = 4;
    private final static int kCheet = 5;
    private final static int kQuit = 6;
    private final static int kCharToInt = 65;
    private final static int kMaxNameLength = 20;
    private final static int kDefaultBoardSize = 8;
    private int boardNum;
    private CollapseGame game;
    private int boardPrefSize;
    
    /** Entry point for the application.
     *
     *  @param args ignored     
     */
    public static void main(String[] args)
    {                   
        try
        {
            CollapseConsole app = new CollapseConsole(); 
            app.setIOsources(new InputStreamReader(System.in), 
                         new OutputStreamWriter(System.out));
            app.run();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    /** Construct a new instance of this class.
     * Create the game components and initialize them,
     * create the user interface and connect it to the game.
     */
    public CollapseConsole() throws IOException
    {
        this.boardNum = new Random().nextInt(kNumBoards);
        this.boardPrefSize = getPreferenceSize();
    }
    
    /** Set input/output sources for Stream-based user interfaces.
     * @param rdr A Reader from which user input is obtained.
     * @param wtr A Writer to which program output is displayed.
     */
    public void setIOsources(Reader rdr, Writer wtr) 
    {
        this.reader = (InputStreamReader)rdr;
        this.writer = (OutputStreamWriter)wtr;
    }

    /** Run the console user interface, using the i/o streams provided by
     *  setIOsources();
     *  @pre setIOsources() has been called.
     */
    public void run() throws IOException
    {
        this.game = new CollapseGame(this.boardPrefSize, this.boardNum);
        String userInput;
        int userChoice = 0;
        boolean gameOver = false;
        Scanner scan = new Scanner(reader);

        displayBoardAndOptions();
        
        /*Keep accepting user input until the user's choice is to quit*/
        while(userChoice != kQuit)
        {   
            try
            {
                userInput = scan.nextLine();
                
                /*Determines if the user enters the correct format for choosing a tile*/
                if(Character.isLetter(userInput.charAt(0)) && userInput.length() == 2)
                {

                    int row = userInput.substring(0, 1)
                        .toUpperCase().charAt(0) - kCharToInt;
                    int column = Integer.parseInt(userInput.substring(1, 2)) - 1;
                    
                    /*Determines if the chosen spot is a valid spot on the board*/
                    if (row < boardPrefSize && column <
                        boardPrefSize && !game.cellEmpty(row, column))
                    {
                        gameOver = game.takeTurn(row, column);
                        displayBoardAndOptions();
                        
                        /*If the game is over then ask if user wants to enter high score*/
                        if(gameOver)
                        {
                            gameOver(scan, game);
                            //this.game = new CollapseGame(boardPrefSize, this.boardNum);
                        }
                    }
                }
                else
                {
                    userChoice = Integer.parseInt(userInput);
                    executeCommand(scan, userChoice);
                }
            }
            catch(Exception e) {
                userChoice = 0;
            }
        }
        
    }
    
    private void executeCommand(Scanner scan, int userChoice) throws IOException
    {
        switch(userChoice)
        {
            case 1:
                //restart the game
                this.game = new CollapseGame(this.boardPrefSize, this.boardNum);
                displayBoardAndOptions();
                break;
            case 2:
                //Start a new game on this board
                this.boardNum++;
                this.game = new CollapseGame(this.boardPrefSize, this.boardNum);
                displayBoardAndOptions();
                break;
            case 3:
                //Select a game
                selectGame(scan);
                displayBoardAndOptions();
                break;
            case 4:
                //View high scores
                writer.write(getHighScores());
                writer.flush();           
                break;
            case 5:
                //cheat
                this.game.cheat();
                displayBoardAndOptions();
                break;
            case 6:
                //exit
                scan.close();
                System.exit(0);
                break;
        }
    }
    
    private void gameOver(Scanner scan, CollapseGame game) throws IOException
    {
        String input;
        String name;

        writer.write("Game Won Notification: Game " + this.boardNum + " Cleared! \n");
        writer.write("Save your score? (y/n)\n");
        writer.flush();
        
        input = scan.nextLine();
        
        if(input.equals("y"))
        {
            writer.write("Name Entry: Your score of " + game.getNumberOfMoves() + " will be entered into the Hall of Fame. \n");
            writer.write("Enter your name: \n");
            writer.flush();
                
            name = scan.nextLine();
            
            if(name.length() > kMaxNameLength)
            {
                name = name.substring(0, kMaxNameLength);
            }
                
            this.addHighScore(name, this.game.getNumberOfMoves());
        }
    }
    
    private void selectGame(Scanner scan) throws IOException
    {
        String boardNumberString;
        int boardNumber;

        writer.write("Select Game: Enter desired game number (1 - 5000):\n");
        writer.flush();
        boardNumberString = scan.nextLine();
        boardNumber = Integer.parseInt(boardNumberString);
            
        if(boardNumber > 0 && boardNumber < kNumBoards)
        {
            this.game = new CollapseGame(this.boardPrefSize, boardNumber);
            this.boardNum = boardNumber;
        }
    }
    
    private void displayBoardAndOptions() throws IOException
    {
        char[][] board = game.getCharacterBoard();
        writer.write("Collapse - board " + this.boardNum + "\n");
        writer.write("Tiles left: " + game.getTilesLeft() + "    Moves: " + game.getNumberOfMoves() + "\n");
        String colString = "     ";
        for(int colIter = 1; colIter < board[0].length; colIter++)
        {
            colString = colString.concat(colIter + "  ");
        }
        colString = colString.concat(board[0].length + "\n");
        writer.write(colString);
        
        char curLetter = 'A';
        String curRow = "";
            
        for(int rowIter = 0; rowIter < board.length; rowIter++, curLetter++)
        {
            curRow = curRow.concat(" " + curLetter + ":  ");
                
            for(int colIter = 0; colIter < board[0].length; colIter++)
            {
                curRow = curRow.concat(new Character(board[rowIter][colIter]).toString());
                if(colIter < board[0].length - 1)
                {
                    curRow = curRow.concat("  ");
                }
            }
                
            curRow = curRow.concat("\n");
            writer.write(curRow);
            curRow = "";
                
        }
            
        String dashedLine = " ----";
        for(int dashNdx = 0; dashNdx < board[0].length - 1; dashNdx++)
        {
            dashedLine = dashedLine.concat("---");
        }
            
        dashedLine = dashedLine.concat("-\n");
        writer.write(dashedLine);
        writer.write("1)Restart 2)New Game 3)Select Game 4)Scores 5)Cheat 6)Quit \n");
        writer.flush();
    }

    /** Adds a new high score to the high scores file
     * 
     * @param name The name of the score with the high score
     * @param score The score the player receieved
     */
    protected void addHighScore(String name, int score) throws IOException
    {
        File highScoresDirectory = new File(kHallOfFameDirPath);
        File highScoresFile = new File(kHallOfFamePath);
        boolean createdDir = false, createdFile = false;

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
    
    private String parseAndSortHighScores(File highScoresFile) throws IOException
    {
        String highScoresString = "";
        ArrayList<HighScore> scoresList = new ArrayList<HighScore>();
        
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
            //highScoresString = highScoresString.concat("         " + curScore.getScore() + "    " + curScore.getName() + "\n");
            highScoresString = highScoresString.concat(String.format("%10s", curScore.getScore()) + "    " + curScore.getName() + "\n");
        }
        highScoresString = highScoresString.concat("\n");
        
        return highScoresString;
    }
    
    /** Return a string representation of the top five high scores. 
     *  @return string is the top scores, one per line, with the
     *  score and name (in that order), separated by one or more blanks.
     *  Name is twenty characters max.  Leading blanks are allowed.
     */
    public String getHighScores() throws IOException
    {
        File highScoresFile = new File(kHallOfFamePath);
        String highScoresString = "";
        
        if (highScoresFile.exists())
        {
            highScoresString = parseAndSortHighScores(highScoresFile);
        }
        
        return highScoresString;
    }
    
    private int getPreferenceSize() throws IOException
    {
        int prefSize = kDefaultBoardSize;
        Ini ini = new Ini();
        
        ini.load(new FileReader(new File(kPreferencesPath)));
        Ini.Section section = ini.get("Board Size");
        prefSize = Integer.parseInt(section.get("small"));
        
        return prefSize;
    }
}