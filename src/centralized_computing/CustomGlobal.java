package projects.centralized_computing;

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
    int step = (round - 1) % (4*n + 2);
    this.phase = (round - 1) / (4*n+2);
    if(step == 0) { this.state = State.getMWOE; }
    else if(0 < step && step <= 1*n) { this.state = State.convergecastMWOE; }
    else if(n < step && step <= 2*n) { this.state = State.broadcastMWOE; }
    else if(2*n < step && step <= 3*n) { this.state = State.replaceLeader; }
    else if(step == 3*n+1) { this.state = State.connectFragments; }
    else if(3*n+1 < step && step <= 4*n+1) { this.state = State.updateFragID; }

  }

  public int getNumNodes() {
    return Runtime.nodes.size();
  }

  public void preRound() {
    updatePhaseState();
    System.out.println("========== Round " + round + "==========");
    System.out.println("Phase: " + phase + ", State: " + state);
  }

  public void postRound() {
    round++;
  }

  public double logStar(double x) {
    if (x < 2)
      return 0;
    return 1 + logStar(Math.log(x) / Math.log(2));
  }


  @AbstractCustomGlobal.CustomButton(buttonText = "Toggle Weights", toolTipText = "Toggle visibility of the edges' weights.")
  public void toggleWeightsButton() {
    showWeights = !showWeights;
    Tools.repaintGUI();
  }

}

