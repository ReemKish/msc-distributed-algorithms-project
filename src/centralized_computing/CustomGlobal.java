package projects.centralized_computing;

import sinalgo.runtime.AbstractCustomGlobal;
import sinalgo.tools.Tools;

public class CustomGlobal extends AbstractCustomGlobal {

  public static boolean showWeights = false;
  public static int round = 0;

  public boolean hasTerminated() {
    return (round > 10);
  }

  public void preRound() {
    System.out.println("Round " + round);
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

