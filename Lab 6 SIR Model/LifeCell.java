/**
 *  Defines the cells used in John Conway's Game of Life: object-oriented implementation
 *
 *  Can be run directly for testing purposes. The execution command below will:
 *  Create a 3x3 grid of cells, and progresses through 2 iterations of the game,
 *  demonstrating cell survival, birth, and death.
 *
 *  Compilation:  javac LifeCell.java
 *  Execution:    java LifeCell
 *
 *  @author Caitrin Eaton, 2021
 *
 */
public class LifeCell implements Cell {

    // Possible states common to all Cells
    // public final static boolean DEAD = false;
    // public final static boolean ALIVE = true;
    // public final static boolean[] STATES = { DEAD, ALIVE };
    public final static int NEIGHBORHOOD_SIZE = 8;

    // Attributes particular to this Cell
    private LifeState currentState;
    private LifeState nextState;
    private int totalNeighbors;
    private int livingNeighbors;
    private LifeCell[] neighborhood;

    /**
     * Construct a new cell for the Game of Life
     * @param initState LifeState, whether the cell is created alive or dead
     */
    public LifeCell( LifeState initState ) {
        this.currentState = initState;
        this.nextState = this.currentState;
        this.totalNeighbors = 0;
        this.livingNeighbors = 0;
        this.neighborhood = new LifeCell[ NEIGHBORHOOD_SIZE ]; // references are initially null
    }

    /**
     * Accessor for a cell's current state
     * @return currentState LifeState, whether the cell is currently alive or dead
     */
    public LifeState getCurrentState(){
        return this.currentState;
    }

    /**
     * Accessor for a cell's next state
     * @return nextState LifeState, whether the cell is currently alive or dead
     */
    public LifeState getNextState(){
        return this.nextState;
    }

    /**
     * Accessor for a cell's current state
     * @return livingNeighbors int, how many neighbors the cell believes are currently alive
     */
    public int getLivingNeighbors(){
        return this.livingNeighbors;
    }

    /**
     * Allows the user to add another cell to this cell's neighborhood
     * @param cell LifeCell, a reference to a neighboring cell object
     */
    public void addNeighbor(Cell cell) {
        // Make sure there's room in the neighborhood
        if (this.totalNeighbors < NEIGHBORHOOD_SIZE) {
            // Add the new neighbor to the neighborhood
            this.neighborhood[totalNeighbors] = (LifeCell) cell;
            this.totalNeighbors++;
            // Keep the living neighbors counter up to date
            if (cell.getCurrentState == LifeState.ALIVE) {
                this.livingNeighbors++;
            }
        } else{
            System.out.println("Warning: new neighbor not added; neighborhood full.");
        }
    }

    /**
     * Updates a cell's next state according to the rules of the Game of Life:
     *   -- A currently living cell will survive with exactly 2 or 3 currently living neighbors.
     *   -- A currently living cell will die with too few (less than 2) or too many (greater than 3) currently living neighbors.
     *   -- A currently dead cell will be born with exactly 3 currently living neighbors.
     *   -- A currently dead cell will remain dead without exactly 3 currently living neighbors.
     */
    public void updateNextState(){
        this.countLivingNeighbors();
        if ((this.livingNeighbors == 3) || (this.currentState == LifeState.ALIVE && this.livingNeighbors == 2)) {
            this.nextState = LifeState.ALIVE;
        } else {
            this.nextState = LifeState.DEAD;
        }
    }

    /**
     * Updates a cell's current state using a next state previously determined by updateNextState()
     */
    public void updateCurrentState(){
        this.currentState = this.nextState;
    }

    /**
     * Iterates over the neighborhood, counting how many neighbors are currently alive.
     */
    private void countLivingNeighbors(){
        int living = 0;
        for(int i = 0; i<this.totalNeighbors; i++ ){
            if (this.neighborhood[i].getCurrentState() == LifeState.ALIVE ) {
                living++;
            }
        }
        this.livingNeighbors = living;
    }

    /**
     * Test that we cna construct a Cell object, and take a look at references
     */
    private static void testCell(){
        System.out.println( "\nTEST CELL" );
        LifeCell testCell = new LifeCell(LifeState.DEAD);
        System.out.println( "\nConstructed new " + testCell);
        System.out.println( "after definition, neighborhood = " + testCell.neighborhood );
        System.out.println( "neighborhood[0] = " + testCell.neighborhood[0] );
    }

    /**
     * Test that Cells can be placed into a 2D structure, and take a closer look at references.
     * @return a 2D array of Cell objects whose Cell[] neighbors fields have not yet been populated
     */
    private static LifeCell[][] testGrid(){
        System.out.println( "\nTEST GRID" );
        LifeCell[][] testGrid = new LifeCell[3][3];
        LifeState initState = LifeState.DEAD;
        for( int row=0; row<3; row++){
            for( int col=0; col<3; col++){
                if ((row%2==0 || col%2==1)){
                    initState = LifeState.ALIVE;
                } else{
                    initState = LifeState.DEAD;
                }
                
                testGrid[row][col] = new LifeCell(initState);
            }
        }
        System.out.println( "\ntestGrid = " + testGrid);
        System.out.println( "testGrid[0] = " + testGrid[0] );
        System.out.println( "testGrid[0][0] = " + testGrid[0][0] );
        System.out.println( "testGrid[0][0].neighborhood = " + testGrid[0][0].neighborhood );
        System.out.println( "testGrid[0][0].neighborhood[0] = " + testGrid[0][0].neighborhood[0] );
        return testGrid;
    }


    /**
     * Test that Cells in a 2D grid structure can track their neighbros in the Cell[] neighbors field.
     * @param testGrid (Cell[][]) a 2D array of Cell objects whose Cell[] neighbors fields have not yet been populated
     */
    private static void testNeighbors( LifeCell[][] testGrid ) {
        System.out.println( "\nTEST NEIGHBORS" );
        // Populate each cell's Cell[] neighbors field
        for( int row=0; row<3; row++){
            for( int col=0; col<3; col++){
                for(int dr=-1; dr<2; dr++) {
                    for (int dc = -1; dc < 2; dc++){
                        if( dr!=0 || dc!=0 ){    // A cell shouldn't be its own neighbor.
                            if((row+dr>=0 && row+dr<3) && (col+dc>=0 && col+dc<3)) {    // Watch out for out of bounds exceptions.
                                // This neighbor is safe to add
                                testGrid[row][col].addNeighbor(testGrid[row + dr][col + dc]);
                            }
                        }
                    }
                }
            }
        }

        System.out.println( "\ntestGrid = " + testGrid);
        System.out.println( "testGrid[0] = " + testGrid[0] );
        System.out.println( "testGrid[0][0] = " + testGrid[0][0] );
        System.out.println( "testGrid[0][0].neighborhood = " + testGrid[0][0].neighborhood );
        System.out.println( "testGrid[0][0].neighborhood[0] = " + testGrid[0][0].neighborhood[0] );
    }

    /**
     * Test that cell update works properly, according to each cell object's neighbors and current state.
     *  @param testGrid (Cell[][]) a 2D array of Cell objects whose Cell[] neighbors fields have not yet been populated
     */
    private static void testUpdate( LifeCell[][] testGrid ){
        System.out.println( "\nTEST UPDATE" );

        // Initial State
        System.out.println("\nINITIAL STATE, T = 0");
        for( int row=0; row<3; row++){
            for( int col=0; col<3; col++){
                testGrid[row][col].updateCurrentState();
                System.out.print(testGrid[row][col].getCurrentState() + "    ");
            }
            System.out.println("");
        }

        // Simulate the next 2 generations by updating twice
        for(int t=0; t<2; t++) {
            // Calculate each cell's next state based on current state + living neigbors
            System.out.println("\nNEXT STATE, T = " + t);
            for (int row = 0; row < 3; row++) {
                for (int col = 0; col < 3; col++) {
                    testGrid[row][col].updateNextState();
                    System.out.print(testGrid[row][col].nextState + "    ");
                }
                System.out.println("");
            }
            // Update each cell's current state to match its previously determined next state
            System.out.println("\nCURRENT STATE, T = " + (t+1));
            for (int row = 0; row < 3; row++) {
                for (int col = 0; col < 3; col++) {
                    testGrid[row][col].updateCurrentState();
                    System.out.print(testGrid[row][col].getCurrentState() + "    ");
                }
                System.out.println("");
            }
        }
    }

    /**
     * Test the LifeCell class's functionality.
     * @param args String[], command line arguments
     */
    public static void main( String[] args ){
        testCell();
        LifeCell[][] testGrid = testGrid();
        testNeighbors( testGrid );
        testUpdate( testGrid );
    }
}
