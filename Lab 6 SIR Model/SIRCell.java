import java.util.Random;

/**
 *  Defines the cells used in SIR model: object-oriented implementation
 * 
 *  Can be run directly for testing purposes. The execution command below will:
 *  Create a 3x3 grid of cells, and progresses through 2 iterations of the game,
 *  demonstrating cells' different states: susceptible, infectious, recovered.
 * 
 *  Compilation:  javac SIRCell.java
 *  Execution:    java SIRCell 
 * 
 *  @author Sami Cemek
 */

public class SIRCell implements Cell {

    // Attributes particular to this Cell
    private SIRState currentState;
    private SIRState nextState;
    private int totalNeighbors;
    private SIRCell[] neighborhood;
    private double infectionRate;
    private double recoveryRate;

    /**
     * generate a random number to compare it with the neighbor's infectionRate
     * creating as a random field for time efficiency
     */
    private Random rand = new Random();

    /**
     * Construct a new cell for the SIR model
     * @param initState SIRState, whether the cell is created infectious or susceptible
     */
    public SIRCell( SIRState initState, double recoveryRate, double infectionRate) {
        this.currentState = initState;
        this.nextState = this.currentState;
        this.totalNeighbors = 0;
        this.recoveryRate = recoveryRate;
        this.infectionRate = infectionRate;
        this.neighborhood = new SIRCell[ NEIGHBORHOOD_SIZE ]; // references are initially null
    }

    /**
     * Accessor for a cell's current state
     * @return currentState SIRState, whether the cell is currently susceptible, infectious or recovered
     */
    public SIRState getCurrentState(){
        return this.currentState;
    }

    /**
     * Accessor for a cell's next state
     * @return nextState SIRState, whether the cell is currently susceptible, infectious or recovered
     */
    public SIRState getNextState(){
        return this.nextState;
    }

    /**
     * Allows the user to add another cell to this cell's neighborhood
     * @param cell SIRCell, a reference to a neighboring cell object
     */
    public void addNeighbor(Cell cell) {
        // Make sure there's room in the neighborhood
        if (this.totalNeighbors < NEIGHBORHOOD_SIZE) {
            // Add the new neighbor to the neighborhood
            this.neighborhood[totalNeighbors] = (SIRCell) cell;
            this.totalNeighbors++;
        } else{
            System.out.println("Warning: new neighbor not added; neighborhood full.");
        }
    }

    /**
     * Accessor for a cell's infection rate
     * @return the infection rate of the cell(double)
     */
    public double getInfectionRate(){
        return this.infectionRate;
    }

    /**
     * Accessor for a cell's recovery rate
     * @return the recovery rate of the cell(double)
     */
    public double getRecoveryRate(){
        return this.recoveryRate;
    }

    /**
     * Updates the next state of the cell based on it's neighors state.
     * For each infectious neighbor, generate a random double between 0.0 and 1.0, 
     * and compare it to the neighbor's infectionRate, to decide if "this" cell will become 
     * infected tomorrow.
     * 
     */
    public void updateNextState(){
        
        SIRCell neighbor; 

        if ((this.currentState == SIRState.INFECTIOUS) && (rand.nextDouble() < this.recoveryRate)){
            this.nextState = SIRState.RECOVERED; //infectious cell recovered if recovery rate is greater than random number
        } else if (currentState == SIRState.SUSCEPTIBLE){
            int n = 0;
            while (n < totalNeighbors && currentState == SIRState.SUSCEPTIBLE){
                neighbor = neighborhood[n];
                if (neighbor.getCurrentState() == SIRState.INFECTIOUS){ 
                    if(rand.nextDouble() < neighbor.getInfectionRate()){
                        this.nextState = SIRState.INFECTIOUS; //cell will be infectious if it's neighbor is infected and random number is smaller than infection rate
                    } 
                }
                n++;
            }
        }
    
        /**
         *  Pseudocode for updateNextState()
         * 
         *  For each neighboor check their state -->(neighbor.getCurrentState())
         *  neighbors will turn their state
         *  if they are susceptible then you are fine --> stay same(SIRState.SUSCEPTIBLE)
         *
         *  if they are infectious, then ask their infection rate --> SIRState.INFECTIOUS
         *  --> neighbor.getInfectionRate()?
         *  neighbors will turn their infectionrate
         *
         *  If the infectionrate is higher than the random number, then you will be infected
         *  rand.nextDouble() < neighbor.getInfectionRate()?
         *
         */     

    }

    /**
     * Updates a cell's current state using a next state previously determined by updateNextState()
     */
    public void updateCurrentState(){
        this.currentState = this.nextState;
    }

    /**
     * Test that Cells can be placed into a 2D structure, and take a closer look at references.
     * @return a 2D array of Cell objects whose Cell[] neighbors fields have not yet been populated
    */
    private static SIRCell[][] testGrid(){
        System.out.println( "\nTEST GRID" );
        SIRCell[][] testGrid = new SIRCell[3][3];
        SIRState initState = SIRState.SUSCEPTIBLE;
        for( int row=0; row<3; row++){
            for( int col=0; col<3; col++){
                if (row%2==0 || col%2==1){
                    initState = SIRState.SUSCEPTIBLE;
                } else {
                    initState = SIRState.INFECTIOUS;
                }
                testGrid[row][col] = new SIRCell(initState, 0.166, 0.037);
            }
        }
        System.out.println( "\ntestGrid = " + testGrid);
        System.out.println( "testGrid[0] = " + testGrid[0] );
        System.out.println( "testGrid[0][0] = " + testGrid[0][0] );
        System.out.println( "testGrid[0][0].neighborhood = " + testGrid[0][0].neighborhood );
        System.out.println( "testGrid[0][0].neighborhood[0] = " + testGrid[0][0].neighborhood[0] );
        return testGrid;
    }

}
