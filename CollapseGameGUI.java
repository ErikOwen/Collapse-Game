import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.URL;
import java.util.Random;


/**  Skeleton GUI for grid-based games. 
 *   GUI has a menu, a status area, and a 2d playing area.
 *   The GUI will display the game and handle mouse clicks, dispatching
 *   them to the appropriate button or cell of the board.
* @author JD 
* @version 2014.1.12
*/
public class CollapseGameGUI extends JFrame 
{

    /* Main components of the GUI */ 
    private JLabel gameStatus = null;
    private JMenuBar menuBar;
    private JTable table;
    private GameTableModel model;
    private int clickCount;
    /* Tile dimensions */
    private int TileWidth = 75;
    private int TileHeight = 55;
    private ImageIcon [] images = new ImageIcon[10];
    private CollapseGame game;
    private int boardPrefSize, boardNum;
    private final static int kNumBoards = 5000;

    /** Create a GUI.
     * Will use the System Look and Feel when possible.
     */
    public CollapseGameGUI() throws IOException
    {
        super();
        
        this.boardNum = new Random().nextInt(kNumBoards);
        this.game = new CollapseGame(Preferences.getPreferenceSize(), this.boardNum);
        
        try
        {
            UIManager.setLookAndFeel(
                UIManager.getSystemLookAndFeelClassName());
        }
        catch (Exception ex)
        {
            System.err.println(ex);
        }
    }


    /** Place all the Swing widgets in the frame of this GUI.
     * @post the GUI is visible.  
     */
    public void layoutGUI()
    {
        loadImages();
        this.model = new GameTableModel(game);
        this.table = new JTable(model); 
        
        table.setDefaultEditor(Object.class, null);  // remove editor makes table not editable
        table.setDefaultRenderer(CollapsePiece.class, new GameCellRenderer(images));
        
        // Define the layout manager that will control order of components
        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        
        // Add a menubar
        menuBar = new javax.swing.JMenuBar();
        JMenu mnuGame = new JMenu("Game");
        menuBar.add(mnuGame);
        JMenuItem mnuNew = new JMenuItem("New");
        mnuNew.setMnemonic('N');
        mnuNew.setAccelerator(
                KeyStroke.getKeyStroke('N', ActionEvent.ALT_MASK));
        mnuNew.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                newGame();
                repaint();                
            }
        });
        mnuGame.add(mnuNew);
        
        JMenuItem mnuQuit = new JMenuItem("Quit");
        mnuQuit.setMnemonic('Q');
        mnuQuit.setAccelerator(
                KeyStroke.getKeyStroke('Q', ActionEvent.ALT_MASK));
        mnuQuit.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                dispose();
            }
        });
        mnuGame.add(mnuQuit);
        setJMenuBar(menuBar);        
        
        // Create a panel for the status information
        JPanel statusPane = new JPanel();
        gameStatus = new JLabel("Tiles left: " + game.getTilesLeft() + "\t\tMoves: " + game.getNumberOfMoves());
        statusPane.add(gameStatus);
        statusPane.setAlignmentX(Component.CENTER_ALIGNMENT);
        getContentPane().add(statusPane);

        // Define the characteristics of the table that shows the game board        
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setCellSelectionEnabled(false);
        table.setRowHeight(TileHeight);

        table.setOpaque(false);
        table.setShowGrid(false);

        table.setAlignmentX(Component.CENTER_ALIGNMENT);
        getContentPane().add(table);

        // Define the mouse listener that will handle player's clicks.
        table.addMouseListener(new MouseAdapter()
        {
            public void mouseReleased(MouseEvent ev)
            {
                int col = table.getSelectedColumn();
                int row = table.getSelectedRow();
                // Is it a left mouse click?
                if (SwingUtilities.isLeftMouseButton(ev)) 
                {
                    row = (int) (ev.getPoint().getY()/TileHeight);
                    col = (int) (ev.getPoint().getX()/TileWidth);
                    System.out.println("Clicked X: " + ev.getPoint().getX() + ", " + "Clicked Y: " + ev.getPoint().getY());
                    System.out.println("Row: " + row + ", Column: " + col);
                    game.takeTurn(row, col);
                    //clickCount++;
                    gameStatus.setText("Tiles left: " + game.getTilesLeft() + "\t\tMoves: " + game.getNumberOfMoves());
                }
                repaint();
            }
        }
        );
        
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
   

    }
    
    protected void loadImages()
    {
        CollapsePiece [] lights = CollapsePiece.values();
        
        // Any images will be loaded here
        for (int ndx = 0; ndx < lights.length - 1; ndx++)
        {
            String file = "icons/" + lights[ndx].name().toLowerCase() + ".png";
            URL url = getClass().getResource(file);
            //System.out.println(url.getPath());
            ImageIcon original = new ImageIcon(url);
            Image scaled = original.getImage().getScaledInstance(TileWidth, TileHeight, Image.SCALE_DEFAULT);
            ImageIcon icon = new ImageIcon(scaled);
            images[ndx] = icon;
        }
    }

    /** Start a new game by putting new values in the board */
    private void newGame()
    {
        //Logic for new game goes here
    }
    
    // Local main to launch the GUI
    public static void main(String[] args)
    {
    	try
    	{
        // Create the GUI 
        CollapseGameGUI frame = new CollapseGameGUI();

        frame.layoutGUI();   // do the layout of widgets
               
        // Make the GUI visible and available for user interaction
        frame.pack();
        frame.setVisible(true);
    	}
    	catch(IOException e)
    	{
    		e.printStackTrace();
    		System.out.println("Error reading from preferences file.");
    	}
    }
}