package projects.centralized_computing.nodes.nodeImplementations;

import java.awt.Color;

import projects.centralized_computing.nodes.edges.WeightedEdge;
import projects.centralized_computing.nodes.messages.*;
import projects.centralized_computing.CustomGlobal;
import projects.centralized_computing.CustomGlobal.State;

import sinalgo.configuration.WrongConfigurationException;
import sinalgo.nodes.Node;
import sinalgo.nodes.edges.Edge;
import sinalgo.nodes.messages.Inbox;
import sinalgo.nodes.messages.Message;

public class GraphNode extends Node {

	public static int nodeIdCounter = 0;
	public static Color[] colors = {Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW, Color.CYAN, Color.MAGENTA};
  public int fragID;
  public int parentID;

	public GraphNode() {
    fragID = ID;
    // parentID = ID - 1;
	}

  public boolean isParentOf(GraphNode other) {
    return ID == other.parentID;
  }

	@Override
	public void init() {
		this.setColor(Color.RED);
	}
	
	
	@Override
	public void checkRequirements() throws WrongConfigurationException {
	}
  
  /* Returns the Minimal Weight Outgoing Edge (MWOE) that connects to a node in another fragment */
  public WeightedEdge getMWOE() {
    int minW = Integer.MAX_VALUE;
    WeightedEdge mwoe = null;
    for (Edge edge_ : outgoingConnections) {
      WeightedEdge edge = (WeightedEdge) edge_;
      if (edge.weight < minW && edge.endNode.fragID != fragID) mwoe = edge;
    }
    return mwoe;
  }
	
	@Override
	public void handleMessages(Inbox inbox) {
    handleState(CustomGlobal.state);
    // broadcast(new FragmentIdMsg(fragID));
    // WeightedEdge mwoe = getMWOE();
    // while(inbox.hasNext()) {
    //   Message msg = inbox.next();
    //   GraphNode sender = (GraphNode) inbox.getSender();
    //   WeightedEdge edge = (WeightedEdge) inbox.getIncomingEdge();
    //   // if(msg instanceof ColorMessage) {}
    // }
	}

  public void handleState(State state) {
    switch(CustomGlobal.state) {
      case getMWOE:
        handleStateGetMWOE();
        break;
      case convergecastMWOE:
        handleStateConvergecastMWOE();
        break;
			case broadcastMWOE:
        handleStateBroadcastMWOE();
				break;
			case replaceLeader:
        handleStateReplaceLeader();
				break;
			case connectFragments:
        handleStateConnectFragments();
				break;
			case updateFragID:
        handleStateUpdateFragID();
				break;
    }
  }

  public void handleStateGetMWOE() {
    ;
  }

  public void handleStateConvergecastMWOE() {
    ;
  }

  public void handleStateBroadcastMWOE() {
    ;
  }

  public void handleStateReplaceLeader() {
    ;
  }

  public void handleStateConnectFragments() {
    ;
  }


  public void handleStateUpdateFragID() {
    ;
  }

	@Override
  public String toString() {
    return "Graph" + super.toString();
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

