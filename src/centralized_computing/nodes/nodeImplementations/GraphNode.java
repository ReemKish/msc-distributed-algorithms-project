package projects.centralized_computing.nodes.nodeImplementations;

import java.awt.Color;

import projects.centralized_computing.nodes.edges.WeightedEdge;
import projects.centralized_computing.CustomGlobal;

import sinalgo.configuration.WrongConfigurationException;
import sinalgo.nodes.Node;
import sinalgo.nodes.edges.Edge;
import sinalgo.nodes.messages.Inbox;
import sinalgo.nodes.messages.Message;

public class GraphNode extends Node {

	public static int nodeIdCounter = 0;
	public static Color[] colors = {Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW, Color.CYAN, Color.MAGENTA};
	public int nodeID;
  public int fragID;

	public GraphNode() {
		nodeID = ++nodeIdCounter;
    fragID = nodeID;
	}

	@Override
	public void init() {
		this.setColor(Color.RED);
	}
	
	
	@Override
	public void checkRequirements() throws WrongConfigurationException {
	}
	
	@Override
	public void handleMessages(Inbox inbox) {
    for (Edge edge : this.outgoingConnections) {
      
    }

    while(inbox.hasNext()) {
      Message m = inbox.next();
      // if(m instanceof ColorMessage) {}
    }
	}


	@Override
	public void neighborhoodChange() {
	}

	@Override
	public void preStep() {
	}

	@Override
	public void postStep() {
	}
}

