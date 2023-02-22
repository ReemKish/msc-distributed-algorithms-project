package projects.centralized_computing;

import java.util.Vector;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import projects.centralized_computing.nodes.nodeImplementations.GraphNode;

import sinalgo.configuration.Configuration;
import sinalgo.runtime.AbstractCustomGlobal;
import sinalgo.runtime.Runtime;
import sinalgo.tools.Tools;

public class CustomGlobal extends AbstractCustomGlobal {

  public static java.util.Random rand = sinalgo.tools.Tools.getRandomNumberGenerator(); 
  public static boolean showWeights = true;
  public static int round = 0;
  public static int randomEdgeWeight;

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

  public static Set<Integer> sampleIntegersInRange(int start, int end, int count) {
        if (end < start || count > (end - start + 1)) {
            throw new IllegalArgumentException("Invalid range or count specified.");
        }
        java.util.Random rand = sinalgo.tools.Tools.getRandomNumberGenerator(); 
        Set<Integer> sampled = new HashSet<>();
        while (sampled.size() < count) {
            int candidate = rand.nextInt(end - start + 1) + start;
            sampled.add(candidate);
        }
        return sampled;
    }

  @AbstractCustomGlobal.CustomButton(buttonText = "Build Graph", toolTipText = "Builds the graph")
  public void buildGraphButton() {
    int numNodes = Integer.parseInt(Tools.showQueryDialog("Number of nodes:"));
    buildGraph(numNodes);
  }

  @AbstractCustomGlobal.CustomButton(buttonText = "Toggle Weights", toolTipText = "Toggle visibility of the edges' weights.")
  public void toggleWeightsButton() {
    showWeights = !showWeights;
    Tools.repaintGUI();
  }

  // a vector of all non-leaf nodes
  Vector<GraphNode> nodes = new Vector<GraphNode>();

  public void buildGraph(int numNodes) {
  }
}

