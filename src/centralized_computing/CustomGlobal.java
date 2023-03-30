package projects.centralized_computing;

import projects.centralized_computing.nodes.nodeImplementations.GraphNode;

import sinalgo.runtime.AbstractCustomGlobal;
import sinalgo.tools.Tools;
import sinalgo.runtime.Runtime;



public class CustomGlobal extends AbstractCustomGlobal {


  public enum State {
      getMWOE,
      convergecastMWOE,
      broadcastMWOE,
      replaceLeader,
      connectFragments,
      updateFragID
  }

  public static boolean showWeights = false;
  public static State state = State.getMWOE;
  public static int phase = 0;
  public static int round = 1;

  public boolean hasTerminated() {
    return phase >= Math.log(getNumNodes()) / Math.log(2);
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

  public void preRound() {
    if(round > getNumNodes()) updatePhaseState();
    // System.out.println("========== Round " + round + " ==========");
    // if(phase>0) System.out.println("Phase: " + phase + ", State: " + state);
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

  @AbstractCustomGlobal.CustomButton(buttonText = "Toggle Weights", toolTipText = "Toggle visibility of the edges' weights.")
  public void toggleWeightsButton() {
    showWeights = !showWeights;
    Tools.repaintGUI();
  }

}

