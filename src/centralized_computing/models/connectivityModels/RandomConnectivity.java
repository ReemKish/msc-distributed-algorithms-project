package projects.centralized_computing.models.connectivityModels;

import java.util.Enumeration;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import sinalgo.models.ConnectivityModel;
import sinalgo.runtime.Runtime;
import sinalgo.runtime.nodeCollection.NodeCollectionInterface;
import sinalgo.nodes.Node;

public class RandomConnectivity extends ConnectivityModel {

	public boolean updateConnections(Node n) {
    NodeCollectionInterface nodeCollection = Runtime.nodes;
    int numNodesToConnect = 7;
    while (numNodesToConnect > 0) {
      Node candidNode = nodeCollection.getRandomNode();
      if (!candidNode.equals(n))
        numNodesToConnect -= n.outgoingConnections.add(n, candidNode, true) ? 1 : 1;
    }
	  return true;
	}
}
