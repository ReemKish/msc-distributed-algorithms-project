package projects.centralized_computing.nodes.messages;

import java.util.Stack;
import java.util.LinkedList;

import projects.centralized_computing.nodes.edges.WeightedEdge;
import projects.centralized_computing.nodes.nodeImplementations.GraphNode;
import sinalgo.nodes.messages.Message;

public class ServerMsg extends Message {
  public boolean arrived = false;
  public String msg;
  public Stack<GraphNode> path;


	public ServerMsg(String msg, GraphNode sender) {
    // initialize the path with the sender node
    path = new Stack<GraphNode>();
    path.add(sender);
    this.msg = msg;
  }

  public void appendPath(GraphNode node) {
    path.add(node);
  }


	@Override
	public Message clone() { return this; }
}
