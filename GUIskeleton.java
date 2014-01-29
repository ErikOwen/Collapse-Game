import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.net.*;


/**  Skeleton GUI for grid-based games. 
 *   GUI has a menu, a status area, and a 2d playing area.
 *   The GUI will display the game and handle mouse clicks, dispatching
 *   them to the appropriate button or cell of the board.
* @author JD 
* @version 2014.1.12
*/
public class GUIskeleton extends JFrame 
{

    /* Main components of the GUI */
    private ImageIcon[][] myBoard;
    private ImageIcon[][] origBoard;
    private ImageIcon[] images;
    private Integer[][] values;
    private String[] columns = {"", "", "", "", "" , "", "", "", "", "", };
    private JLabel myStatus = null;
    private JMenuBar menuBar;
    private JTable table;
    private int score;
    private int clickCount;
    /* Square dimensions */
    private int tileWidth = 60;
    private int tileHeight = 30;
    

    /** Create a GUI.
     * Will use the System Look and Feel when possible.
     */
    public GUIskeleton()
    {
        super();
        myBoard = new ImageIcon[10][10];
        origBoard = new ImageIcon[10][10];
        images = new ImageIcon[3];
        values = new Integer[10][10];
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
        newGame();
        table = new JTable(myBoard, columns); 
        
        TableColumn column = null;
        
        /*Determines if the board is null or not*/
        if (myBoard != null)
        {
            // Set the dimensions for each column in the board to match the image */
            for (int iter = 0; iter < 10; iter++)
            {
                column = table.getColumnModel().getColumn(iter);
                column.setMaxWidth(tileWidth);
                column.setMinWidth(tileWidth);
            }
        }
         // remove editor makes table not editable
        table.setDefaultEditor(Object.class, null);

        // Define the layout manager that will control order of components
        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        
        myStatus = new JLabel("0");
        
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
                myStatus.setText("" + score);
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
        
        /*New stuff belwo*/
        JMenuItem mnuRestart = new JMenuItem("Restart");
        mnuRestart.setMnemonic('R');
        mnuRestart.setAccelerator(
                KeyStroke.getKeyStroke('R', ActionEvent.ALT_MASK));
        mnuRestart.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                restart();
            }
        });
        mnuGame.add(mnuRestart);
        setJMenuBar(menuBar);
        
        // Create a panel for the status information
        JPanel statusPane = new JPanel();
        //myStatus = new JLabel("###");
        statusPane.add(myStatus);
        statusPane.setAlignmentX(Component.CENTER_ALIGNMENT);
        getContentPane().add(statusPane);

        // Define the characteristics of the table that shows the game board        
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setCellSelectionEnabled(false);
        table.setRowHeight(tileHeight);

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
                // Is it a right mouse click?
                if (SwingUtilities.isRightMouseButton(ev)) 
                {
                    row = (int) (ev.getPoint().getY()/tileHeight);
                    col = (int) (ev.getPoint().getX()/tileWidth);
                    // Turn the value negative
                    values[row][col] = -(Integer) values[row][col];
                }
                else
                {
                    score += values[row][col];
                    // left mouse clicks turn the value to zero
                    values[row][col] = 0;
                    clickCount++;
                    myStatus.setText("" + score);
                }
                
                repaint();
            }
        }
        );
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
   

    } // end layout

    private void restart()
    {
        score = 0;
        /*Iterates trhough all of the rows*/
        for(int xVal = 0; xVal < myBoard.length; xVal++)
        {
            /*Iterates through all of the columns*/
            for(int yVal = 0; yVal < myBoard[0].length; yVal++)
            {
                myBoard[xVal][yVal] = origBoard[xVal][yVal];
            }
        }
        
        myStatus.setText("" + score);
        repaint();
    }
    
    protected void loadImages()
    {
        CollapsePiece [] lights = CollapsePiece.values();
        
        // Any images will be loaded here
        for (int ndx = 0; ndx < lights.length; ndx++)
        {
            String file = "icons/" + lights[ndx].name().toLowerCase() + ".jpg";
            URL url = getClass().getResource(file);
            ImageIcon img = new ImageIcon(url);
            images[ndx] = img;
        }
    }

    /** Start a new game by putting new values in the board */
    private void newGame()
    {
        score = 0;
        
        /*Iterates through all of the rows*/
        for (int row = 0; row < 10; row++)
        {
            /*Iterates through all of the columns*/
            for (int col = 0; col < 10; col++)
            {
                Integer cellValue = new Integer((int)(-5 + (Math.random()*10)));
                Integer picValue = new Integer((int)(0 + (Math.random()*3)));
                values[row][col] = cellValue;
                ImageIcon img = images[picValue];
                origBoard[row][col] = img;
                myBoard[row][col] = img;
            }
        }
        clickCount = 0;
    }
    
    /**
     * Local main to launch the GUI
     * 
     * @param args parameters to the program
     */
    public static void main(String[] args)
    {
        // Create the GUI 
        GUIskeleton frame = new GUIskeleton();

        frame.layoutGUI();   // do the layout of widgets
               
        // Make the GUI visible and available for user interaction
        frame.pack();
        frame.setVisible(true);
    }
}  // end class