package projects.centralized_computing;

import java.io.FileWriter;
import java.io.IOException;

import projects.centralized_computing.nodes.nodeImplementations.GraphNode;
import projects.centralized_computing.nodes.edges.WeightedEdge;


import sinalgo.runtime.AbstractCustomGlobal;
import sinalgo.tools.Tools;
import sinalgo.nodes.Node;
import sinalgo.nodes.edges.Edge;
import sinalgo.runtime.Runtime;



public class CustomGlobal extends AbstractCustomGlobal {


  public enum State {
      getMWOE,
      convergecastMWOE,
      broadcastMWOE,
      replaceLeader,
      connectFragments,
      updateFragID,
      finished
  }

  public static boolean showWeights = false;
  public static State state = State.getMWOE;
  public static int phase = 0;
  public static int round = 1;
  public static boolean declaredResults = false;

  public boolean hasTerminated() {
    // Returns whether the algorithm has terminated, i.e. - when all nodes have the same fragment ID
    boolean algoTerminated = phase >= Math.log(getNumNodes()) / Math.log(2);
    if (!algoTerminated || !GraphNode.serverReady || !GraphNode.taskCompleted) return false;
    if (state == State.finished && !declaredResults) {
      long totalWeight = 0;
      long treeWeight = 0;
      for (Node n : Tools.getNodeList()) {
        GraphNode node = (GraphNode) n;
        for (Edge e : node.outgoingConnections) {
          WeightedEdge edge = (WeightedEdge) e;
          totalWeight += edge.weight;
          if (edge.startNode.isParentOf(edge.endNode)) {
            treeWeight += edge.weight;
          }
        }
      }
      totalWeight /= 2;
      Tools.appendToOutput("GHS Terminated with MST weight: " + treeWeight + "\n");
      declaredResults = true;
      // write results to a csv file where each line is of the form: <num_nodes>, <total_weight>, <tree_weight>
      String filename = "/home/reem/results.csv";
      try {
        FileWriter fw = new FileWriter(filename, true);
        fw.write(Integer.toString(getNumNodes()) + ", " + Long.toString(totalWeight) + ", " + Long.toString(treeWeight) + "\n");
        fw.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    return true;
  }

  public void updatePhaseState() {
    int n = getNumNodes();
    int step = (round - n - 1) % (4*n + 2);
    phase = 1 + (round - n - 1) / (4*n+2);
    if(step == 0) { state = State.getMWOE; }
    else if(0 < step && step <= 1*n) { state = State.convergecastMWOE; }
    else if(n < step && step <= 2*n) { state = State.broadcastMWOE; }
    else if(2*n < step && step <= 3*n) { state = State.replaceLeader; }
    else if(step == 3*n+1) { state = State.connectFragments; }
    else if(3*n+1 < step && step <= 4*n+1) { state = State.updateFragID; }
  }

  public int getNumNodes() {
    return Runtime.nodes.size();
  }

  public boolean isFinished() {
    // Returns whether the algorithm is finished, i.e. - when all nodes have the same fragment ID
    int fragID = -1;
    for(Node n : Runtime.nodes) {
      GraphNode node = (GraphNode) n;
      if(fragID == -1) fragID = node.fragID;
      else if(fragID != node.fragID) return false;
    }
    return true;
  }

  public void preRound() {
    if(round > getNumNodes()) updatePhaseState();
    if(isFinished()) state = State.finished;
    else {
      System.out.println("========== Round " + round + " ==========");
      if(phase>0) System.out.println("Phase: " + phase + ", State: " + state);
    }
  }

  public void postRound() {
    round++;
  }

  public double logStar(double x) {
    if (x < 2)
      return 0;
    return 1 + logStar(Math.log(x) / Math.log(2));
  }


	@CustomButton(imageName="Node.gif", toolTipText="Clear the nodes' labels.")
  public void setNodeLabelNone() { GraphNode.setLabelNone(); }

	@CustomButton(imageName="NodeWithID.gif", toolTipText="Set the nodes' labels to their IDs.")
  public void setNodeLabelID() { GraphNode.setLabelID(); }

	@CustomButton(imageName="NodeWithFragID.gif", toolTipText="Set the nodes' labels to their Fragment IDs.")
  public void setNodeLabelFragID() { GraphNode.setLabelFragID(); }

	@CustomButton(imageName="Weights.gif", toolTipText="Toggle visibility of the edges' weights.")
  public void toggleWeightsButton() {
    showWeights = !showWeights;
    Tools.repaintGUI();
  }

}

