package projects.centralized_computing.models.distributionModels;

import sinalgo.configuration.Configuration;
import sinalgo.models.DistributionModel;
import sinalgo.nodes.Position;
import sinalgo.tools.statistics.UniformDistribution;

public class RandomDisc extends DistributionModel {
  private UniformDistribution uniform = new UniformDistribution(0, 1);

	public Position getNextPosition() {
    // Runtime.clearAllNodes();
    // nodes.clear();
    // GraphNode.nodeIdCounter = 0;
    double radius = (double) Math.min(Configuration.dimX, Configuration.dimY) / 2.1;
    double angle = uniform.nextSample() * 2 * Math.PI;
    double distance = Math.sqrt(uniform.nextSample()) * radius;
    double posX = distance * Math.cos(angle) + Configuration.dimX / 2;
    double posY = distance * Math.sin(angle) + Configuration.dimY / 2;
    // gn = new GraphNode();
    // gn.finishInitializationWithDefaultModels(true);
    // nodes.add(gn);
    // gn.setPosition(posX, posY, 0);
    // Generate graph edges.
    // for (int i = 0; i < numNodes; i++) {
    //   Set<Integer> neighbors = sampleIntegersInRange(0, numNodes-1, 3);
    //   for (int neigh : neighbors) {
    //     randomEdgeWeight = rand.nextInt(1000000000 - 1) + 1;
    //     nodes.get(i).addConnectionTo(nodes.get(neigh));
    //   }
    // }
    // Tools.repaintGUI();
		return new Position(posX, posY, 0);
	}
}
