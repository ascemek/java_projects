import java.awt.*;
import javax.swing.*;

/**
 *  Defines a visualization tool useful for John Conway's Game of Life: object-oriented implementation
 *
 *  Can be run directly for testing purposes. The execution command below will:
 *  Create a 10x10 grid of cells, and progresses through 10 iterations of the game,
 *  demonstrating cell survival, birth, and death.
 *
 *  Compilation:  javac LifeVis.java
 *  Execution:    java LifeVis
 *
 *  @author Caitrin Eaton, 2021
 */
public class LifeVis {

    // Dimensions
    private final int windowSize = 600;   // in pixels
    private final int cellSize;           // in pixels
    private final int gridSize;           // in cells
    private long frameTime = 500;

    // Graphics components
    private JFrame window;
    private JPanel[][] panel;

    // Life components
    private LifeGrid grid;
    private boolean[][] state;

    // Cell states
    private final static Color ALIVE = Color.BLACK;
    private final static Color DEAD = Color.WHITE;
    private final static Color ERROR = Color.RED;
    private final static Color GRIDLINE = Color.LIGHT_GRAY;

    /**
     * LifeVis constructor, creates an animated visualization for the given grid of cells.
     * @param world LifeGrid, the 2D collection of cells that defines this particular Game of Life
     */
    public LifeVis( LifeGrid world ) {
        // Determine the dimensions of components that will be visualized
        this.grid = world;
        this.gridSize = world.getSize();
        this.cellSize = this.windowSize / this.gridSize;

        // Configure the graphics window
        this.window = new JFrame();
        this.panel = new JPanel[ this.gridSize ][ this.gridSize ];
        init_window();
    }

    /**
     * Initialize the graphics window and its contents.
     */
    private void init_window() {

        // Configure the window itself
        this.window.setSize( this.windowSize, this.windowSize );
        this.window.setTitle("Functional Life: Generation " + this.grid.getGeneration() + ", Flux " + this.grid.getFlux() );

        // Configure the collection of rectangles that visualize all the cells
        this.window.setLayout( new GridLayout(this.gridSize, this.gridSize) );
        this.state = this.grid.getPopulation();
        for (int row = 0; row < this.gridSize; row++) {
            for (int col = 0; col < this.gridSize; col++ ){
                this.panel[row][col] = new JPanel();
                this.panel[row][col].setBorder(BorderFactory.createLineBorder( GRIDLINE ));
                this.window.add( this.panel[row][col] );
                if ( this.state[row][col] == LifeCell.ALIVE ) {
                    this.panel[row][col].setBackground( ALIVE );  // color occupied cells black
                } else if ( this.state[row][col] == LifeCell.DEAD ) {
                    this.panel[row][col].setBackground( DEAD );  // color unoccupied cells white
                } else {
                    this.panel[row][col].setBackground( ERROR );    // color unrecognized cell states red
                }
            }
        }

        // Make the graphics window visible
        this.window.setLocationRelativeTo(null);
        this.window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.window.setVisible(true);
        long initial_delay_ms = 2000;
        this.window.repaint( initial_delay_ms );

        // Hold the initial population steady for 2 seconds before starting the simulation
        try {
            Thread.sleep(initial_delay_ms);
        } catch (InterruptedException e) { }
    }

    /**
     * Control the speed of the animation.
     * @param milliseconds long, the duration of a single frame, in milliseconds
     */
    public void setFrameTime(long milliseconds ){
        this.frameTime = milliseconds;
    }

    /**
     * Update the graphical representation of the grid to reflect the grid's current state.
     */
    public void update( ) {
        // Update the current state map
        this.state = this.grid.getPopulation();

        // Use the window's title to track the number of generations that have passed and the current flux
        this.window.setTitle( "Functional Life: Generation " + this.grid.getGeneration() + ", Flux " + this.grid.getFlux() );

        // Configure the color of each cell according to its state: occupied or unoccupied
        for (int row = 0; row < gridSize; row++) {
            for (int col = 0; col < gridSize; col++ ){
                if ( this.state[row][col] == LifeCell.ALIVE ) {
                    this.panel[row][col].setBackground( ALIVE );  // color occupied cells black
                } else if ( this.state[row][col] == LifeCell.DEAD ) {
                    this.panel[row][col].setBackground( DEAD );  // color unoccupied cells white
                } else {
                    this.panel[row][col].setBackground( ERROR );    // color unrecognized cell states red
                }
            }
        }
        this.window.repaint( this.frameTime );
        try {
            Thread.sleep(this.frameTime);
        } catch (InterruptedException e) { }
    }

    /**
     * Test the LifeVis class by simulating a small, short Game of Life.
     * @param args String[] command line arguments
     */
    public static void main(String[] args) {

        // Initialize a LifeGrid object to drive the game mechanics
        double populationDensity = 0.60;
        int gridSize = 10;
        int simLength = 10;
        LifeGrid testGrid = new LifeGrid(gridSize);
        testGrid.setMaxGenerations(simLength);
        testGrid.populate(populationDensity);

        // Initialize a LifeVis object to show the user what's going on in the game
        LifeVis testVis = new LifeVis( testGrid );
        testVis.setFrameTime( 1000 );

        // Run the simulation
        while( testGrid.getFlux() > 0 && testGrid.getGeneration() < testGrid.getMaxGenerations() ){
            testGrid.step();
            System.out.println( testGrid );
            testVis.update();
        }
    }
}
