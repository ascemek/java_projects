import java.util.Random;

/**
 *  Defines the grid used in SIR model: object-oriented implementation 
 * 
 *  Can be run directly for testing purposes. The execution command below will:
 *  Creates a 10x10 grid of cells, and progresses through 10 iterations of the game,
 *  demonstrating cell states: susceptible, infectious, and recovered.
 * 
 *  Compilation:  javac SIRGrid.java 
 *  Execution:    java SIRGrid 
 * 
 *  @author Sami Cemek
 *  Updated: 05/02/2021
 */

public class SIRGrid implements Grid{

    // State-related grid attributes
    private final int size;
    private SIRCell[][] population;
    private int sus, inf, rec;

    /**
     * Construct a new grid for the SIR model.
     * @param size int, the width (and height) of the grid, in cells
     */
    public SIRGrid(int size) {
        this.size = size;
        this.inf = 0;
        this.population = new SIRCell[size][size]; // references are initially null
    }

    /**
     * Read the size of the square population array.
     * @return size int, the number of cells along the width (and height) of the square population array.
     */
    public int getSize() {
        return this.size;
    }

    /**
     * Randomly initialize the grid's population.
     * @param percentInfected (double) the percantage of people infected.
     * @param infectionRate (double) the probability that a susceptible individual will become infected by each infected neighbor, each day.
     * @param recoveryRate (double) the probability that an infected individual will recover, each day.
     */
    public void populate(double percentInfected, double infectionRate, double recoveryRate) {
        double occupancy;
        Random occupier = new Random();
        for (int row = 0; row < this.size; row++) {
            for (int col = 0; col < this.size; col++) {
                occupancy = occupier.nextDouble(); 
                if (occupancy < percentInfected) { //If the percentInfected is higher than the random number, then you will be infected
                    this.population[row][col] = new SIRCell(SIRState.INFECTIOUS, infectionRate, recoveryRate );
                    this.inf++;
                } else { // else you will stay the same(susceptible)
                    this.population[row][col] = new SIRCell(SIRState.SUSCEPTIBLE, infectionRate, recoveryRate);
                }      
            }
        }

        formNeighborhoods();
    }         
                
    /**
     * Connect cells to their neighbors.
     */
    private void formNeighborhoods() {

        // Each cell in the grid has its own neighborhood
        for (int row = 0; row < this.size; row++) {
            for (int col = 0; col < this.size; col++) {

                // Add this cell's 8 Moore neighbors to its neighborhood.
                for (int dr = -1; dr < 2; dr++) {
                    for (int dc = -1; dc < 2; dc++) {

                        // A cell shouldn't be its own neighbor.
                        if (dr != 0 || dc != 0) {

                            // Watch out for out of bounds exceptions.
                            if ((row + dr >= 0 && row + dr < this.size) && (col + dc >= 0 && col + dc < this.size)) {

                                // This neighbor is safe to add!
                                this.population[row][col].addNeighbor(this.population[row + dr][col + dc]);
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Pretty printing grid objects.
     * @return summary String, a description of the SIRGrid object's current state
     */
    public String toString() {
        String summary = "\nState: ";
        for (int row = 0; row < this.size; row++) {
            summary += "\n";
            for (int col = 0; col < this.size; col++) {
                if ( this.population[row][col] == null ) {
                    summary += 'N';
                } else if ( this.population[row][col].getCurrentState() == SIRState.SUSCEPTIBLE ) {
                    summary += 'S';
                } else if ( this.population[row][col].getCurrentState() == SIRState.INFECTIOUS )  {
                    summary += ' ';
                } else if ( this.population[row][col].getCurrentState() == SIRState.RECOVERED )  {
                    summary += 'R';   
                } else {
                    summary += '?';
                }
            }
        }
        return summary;
    }

    

    /**
     * Apply the rules of SIR model to progress the simulation by 1 time step.
     */
    public void update( ){

        // Ask each cell to predict its own next state, tracking the number of cells that will change
        int sus = 0;
        int inf = 0;
        int rec = 0;
        
        for (int row = 0; row < this.size; row++) {
            for (int col = 0; col < this.size; col++) {
                this.population[row][col].updateNextState();
                // Keep track of changing cells in order to detect stasis
                if (this.population[row][col].getNextState() == SIRState.INFECTIOUS) {
                    inf++; //if next state of a cell is infectious, then add one to the inf counter.
                }
                else if (this.population[row][col].getNextState() == SIRState.SUSCEPTIBLE){
                    sus++; //if next state of a cell is susceptible, then add one to the sus counter.
                }
                else{
                    rec++; //if next state of a cell is recovered, then add one to the rec counter.
                }
            }
        }

        // Ask each cell to update its own state
        for (int row = 0; row < this.size; row++) {
            for (int col = 0; col < this.size; col++) {
                this.population[row][col].updateCurrentState();
            }
        }

        // Update grid-level attributes related to recent changes in the cell population
        this.sus = sus;
        this.inf = inf;
        this.rec = rec;

    }

    /**
     * Retrieve a copy of the population's current state fields, using a boolean 2D array to protect the cells
     * themselves from unwanted manipulation.
     * @return snapshot boolean[][], a grid of current states
     */
    public SIRState[][] getPopulation(){
        SIRState[][] snapshot = new SIRState[this.size][this.size];
        for (int row=0; row<this.size; row++){
            for (int col=0; col<this.size; col++){
                snapshot[row][col] = this.population[row][col].getCurrentState();
            }
        }
        return snapshot;
    }

    /**
     * Since a SIR object's job is to simulate the grid over time, the grid itself does not need
     * time information or a simulate() function. It only needs to update() itself.
     * ... But a "private" simulation-like behavior is still helpful for testing the update() method.
     */
    // public void simulate( boolean verbose ) {
    private void testUpdate( ){
        System.out.println( "\nTEST UPDATE:\n");
        int generation = 0;
        int maxGenerations = 10;
        while((this.inf > 0) && (generation < maxGenerations)){
            update();
            generation++;
            System.out.println( "\nGENERATION: " + generation );
            System.out.println( this );
        }
    }

    /**
     * Test the SIRCell class by running a small simulation.
     * @param args String[], command line arguments
     */
    public static void main( String[] args ){
        SIRGrid testGrid = new SIRGrid( 10 );
        System.out.println( "\nNEWLY CONSTRUCTED GRID OBJECT:\n" + testGrid );
        //testGrid.setMaxGenerations( 10 );
        testGrid.populate(  0.05, 0.166, 0.037 ); //Default rates, can change later
        System.out.println( "\nINITIAL POPULATION:\n" + testGrid );
        testGrid.testUpdate( );
        System.out.println( "\nFINAL POPULATION:\n" + testGrid );
    }

    /**
     * Get the numbers of cells on grid for each state(S,I,R)
     * @return numbers of cells on grid for each state(S,I,R)
     */
    public int[] getDemographics() {

        int[] totals = new int[3];
        for (int i = 0; i < totals.length; i++){
            totals[0] = sus; //S 
            totals[1] = inf; //I
            totals[2] = rec; //R
        }
        return totals;
    }
    
    /**
     * Accessor for susceptible cells
     * @return this.sus, number of susceptible cells
     */
    public int getsus(){ //SUSCEPTIBLE
        return this.sus;
    }

    /**
     * Accessor for infectious cells
     * @return this.inf, number of infectious cells
     */
    public int getinf(){ //INFECTIOUS
        return this.inf;
    }
    
    /**
     * Accessor for recovered cells
     * @return this.rec, number of recovered cells
     */
    public int getrec(){ //RECOVERED
        return this.inf;
    }

    /**
     * It is similar to getDemographics method but this one returns the percentages of cells on grid for each state(S,I,R)
     * @return percentages, percentages of cells on grid for each state(S,I,R)
     */
    public double[] getPercentages(){
        int[] demo = getDemographics();
        int popSize = getSize() * getSize();
        
        double s = (double) demo[ SIRState.SUSCEPTIBLE.ordinal() ] / popSize * 100;
        double i = (double) demo[ SIRState.INFECTIOUS.ordinal() ] / popSize * 100;
        double r = (double) demo[ SIRState.RECOVERED.ordinal() ] / popSize * 100;

        double[] percentages = {s, i, r};
    
        return percentages;
    }
}