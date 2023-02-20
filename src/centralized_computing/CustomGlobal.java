package projects.centralized_computing;

import java.util.Vector;

import projects.centralized_computing.nodes.nodeImplementations.GraphNode;

import sinalgo.configuration.Configuration;
import sinalgo.runtime.AbstractCustomGlobal;
import sinalgo.runtime.Runtime;
import sinalgo.tools.Tools;
import sinalgo.tools.statistics.UniformDistribution;

public class CustomGlobal extends AbstractCustomGlobal {

  /*
   * (non-Javadoc)
   * 
   * @see runtime.AbstractCustomGlobal#hasTerminated()
   */
  public boolean hasTerminated() {
    return (GraphNode.n_round - GraphNode.max_round - 7 > 0);
  }

  public void preRound() {
    if (GraphNode.n_round <= GraphNode.max_round)
      System.out
          .println("ROUND #" + GraphNode.n_round + "(coloring " + GraphNode.n_round + "/" + GraphNode.max_round + ")");
    else if (GraphNode.n_round - GraphNode.max_round - 1 <= 6)
      System.out.println("ROUND #" + GraphNode.n_round + "(mis " + (GraphNode.n_round - GraphNode.max_round - 1) + "/6)");
    else
      System.out.println("MIS Algorithm Finished.");
  }

  public void postRound() {
    GraphNode.n_round++;

  }

  public double logStar(double x) {
    if (x < 2)
      return 0;
    return 1 + logStar(Math.log(x) / Math.log(2));
  }

  /*
   * Build graph button.
   */
  @AbstractCustomGlobal.CustomButton(buttonText = "Build Graph", toolTipText = "Builds the graph")
  public void sampleButton() {
    int numNodes = Integer.parseInt(Tools.showQueryDialog("Number of nodes:"));
    buildGraph(numNodes);
  }

  // a vector of all non-leaf nodes
  Vector<GraphNode> nodes = new Vector<GraphNode>();

  public void buildGraph(int numNodes) {
    if (numNodes <= 0) {
      Tools.showMessageDialog("The number of nodes needs to be at least 1.\nCreation of graph aborted.");
      return;
    }
    Runtime.clearAllNodes();
    nodes.clear();
    GraphNode.smallIdCounter = 0;
    double angle, distance = 0;
    double posX, posY = 0;
    double radius = (double) Math.min(Configuration.dimX, Configuration.dimY) / 2.1;
    UniformDistribution uniform = new UniformDistribution(0, 1);
    GraphNode gn;
    for (int i = 0; i < numNodes; i++) {
      angle = uniform.nextSample() * 2 * Math.PI;
      distance = Math.sqrt(uniform.nextSample()) * radius;
      posX = distance * Math.cos(angle) + Configuration.dimX / 2;
      posY = distance * Math.sin(angle) + Configuration.dimY / 2;
      gn = new GraphNode();
      gn.finishInitializationWithDefaultModels(true);
      nodes.add(gn);
      gn.setPosition(posX, posY, 0);
    }


    Tools.repaintGUI();
  }
}

