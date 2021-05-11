OOP Lab 06: SIR Model v1
Updated:05/02/2021

Classes
-SIR.java(top-level class)
-SIRCell.java
-SIRGrid.java
-SIRState.java(Enumeration)
-SIRVis.java or SIRVisImg.java

Interfaces
-CellularAutomaton.java
-Cell.java
-Grid.java
-State.java
-Vis.java

Notes:
- If you want to use SIRVis.java class instead of SIRVisImg.java:
  1) On SIR.java comment in line:24 --> //SIRVis vis and 
     comment out line:25 --> SIRVisImg vis;
  2) On SIR.java comment in line:46 --> //vis = new SIRVis( grid );
     and comment out line:47 --> vis = new SIRVisImg( grid );