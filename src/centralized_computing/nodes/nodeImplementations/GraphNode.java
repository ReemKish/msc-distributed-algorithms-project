package projects.centralized_computing.nodes.nodeImplementations;



import java.awt.Color;

import projects.centralized_computing.nodes.messages.ColorMessage;
import projects.centralized_computing.nodes.messages.GroupMessage;
import sinalgo.configuration.WrongConfigurationException;
import sinalgo.nodes.Node;
import sinalgo.nodes.messages.Inbox;
import sinalgo.nodes.messages.Message;

/**
 * An internal node (or leaf node) of the tree.
 * Note that the leaves are instances of LeafNode, a subclass of this class. 
 */
public class GraphNode extends Node {

	public GraphNode parent = null; // the parent in the tree, null if this node is the root
	// A counter that may be reset by the user
	public static int smallIdCounter = 0;
	public static int n_round = 0;
	public static int max_round;
	public int smallID;
	public static Color[] colors = {Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW, Color.CYAN, Color.MAGENTA};
	public boolean inGroup = true;
	
	public void colorMe() {
		if (smallID <= 5) {
			this.setColor(colors[smallID]);
		} else {
			this.setColor(Color.BLACK);
		}
	}
	
	public GraphNode() {
		smallID = ++smallIdCounter;
	}
	
	@Override
	public void checkRequirements() throws WrongConfigurationException {
	}
	
	public static int findFirstDifferingBit(int x, int y) {
	    int diff = x ^ y;
	    int bitIndex = 0;
	    while (diff > 0) {
	        if ((diff & 1) == 1) {
	            return bitIndex;
	        }
	        diff = diff >> 1;
	        bitIndex++;
	    }
	    return -1;
	}

	@Override
	public void handleMessages(Inbox inbox) {
		if (GraphNode.n_round <= GraphNode.max_round) {
			while(inbox.hasNext()) {
				Message m = inbox.next();
				if(m instanceof ColorMessage) {
					if(parent == null || !inbox.getSender().equals(parent)) {
						continue;// don't consider color messages sent by children
					}
					int pId = ((ColorMessage)m).id;
					int ind = findFirstDifferingBit(smallID, pId);
					this.smallID = (ind << 1) | ((this.smallID >> ind) & 1);
				}
			}
		}
		else {
			int round = GraphNode.n_round - GraphNode.max_round - 1;
			boolean neighInGroup;
			while(inbox.hasNext()) {
				Message m = inbox.next();
				if(m instanceof GroupMessage) {
//					if(parent == null || !inbox.getSender().equals(parent)) {
//						continue;// don't consider color messages sent by children
//					}
					neighInGroup = ((GroupMessage)m).inGroup;
					if (neighInGroup) {
						this.inGroup = false;
					}
				}
			}
		}
		
	}

	@Override
	public void init() {
		if(this.parent != null) {
			this.setColor(Color.RED);
		}
	}

	@Override
	public void neighborhoodChange() {
	}

	@Override
	public void preStep() {
		if(this.parent == null) {
			this.smallID = 0;
		}
	}

	@Override
	public void postStep() {
		if (GraphNode.n_round <= GraphNode.max_round) {
			colorMe();
			ColorMessage m = new ColorMessage(this.smallID);
			broadcast(m);
		}
		else
		{
			int round = GraphNode.n_round - GraphNode.max_round - 1;
			if (round == smallID) {
				broadcast(new GroupMessage(this.inGroup));
				if (this.inGroup) {
					this.setColor(Color.ORANGE);
				} else {
					this.setColor(Color.GRAY);
				}
			}
		}
	}
}
