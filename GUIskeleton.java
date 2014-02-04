import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;


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
    private Object[][] myBoard; 
    private String[] columns = {"","","","","","","","","","",};
    private JLabel myStatus = null;
    private JMenuBar menuBar;
    private JTable table;
    private int clickCount;
    /* Square dimensions */
    private int TileWidth = 100;
    private int TileHeight = 50;
    private ImageIcon [] images = new ImageIcon[10];
    private CollapseGame game;

    /** Create a GUI.
     * Will use the System Look and Feel when possible.
     */
    public GUIskeleton(CollapseGame game)
    {
        super();
        this.game = game;
        myBoard = new Integer[game.getTileBoard().length][game.getTileBoard()[0].length];
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
        //newGame();
        table = new JTable(new GameTableModel(game)); 
        
        TableColumn column = null;
        if (myBoard != null)
        {
            // Set the dimensions for each column in the board to match the image */
            for (int i = 0; i < game.getTileBoard()[0].length; i++)
            {
                column = table.getColumnModel().getColumn(i);
                column.setMaxWidth(TileWidth);
                column.setMinWidth(TileWidth);
            }
        }
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
        myStatus = new JLabel("###");
        statusPane.add(myStatus);
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
                // Is it a right mouse click?
                if (SwingUtilities.isRightMouseButton(ev)) 
                {
                    row = (int) (ev.getPoint().getY()/TileHeight);
                    col = (int) (ev.getPoint().getX()/TileWidth);
                    // Turn the value negative
                    myBoard[row][col] = -(Integer) myBoard[row][col];
                }
                else
                {
                    // left mouse clicks turn the value to zero
                    myBoard[row][col] = 0;
                    clickCount++;
                    myStatus.setText("" + clickCount);
                }
                repaint();
            }
        }
        );
        
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
   

    } // end layout

    
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
        Integer cellvalue = new Integer((int)(Math.random()*10));
        for (int i=0;i<10;i++)
        {
            for (int j=0;j<10;j++)
            {
                myBoard[i][j] = cellvalue;
            }
        }
        clickCount = 0;
    }
    
    // Local main to launch the GUI
    public static void main(String[] args)
    {
        // Create the GUI 
    	CollapseGame game = new CollapseGame(8, 2);
        GUIskeleton frame = new GUIskeleton(game);

        frame.layoutGUI();   // do the layout of widgets
               
        // Make the GUI visible and available for user interaction
        frame.pack();
        frame.setVisible(true);
    }
}  // end class