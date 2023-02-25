package projects.centralized_computing.nodes.messages;

import projects.centralized_computing.nodes.edges.WeightedEdge;
import sinalgo.nodes.messages.Message;

public class MinimalEdgeMsg extends Message {
  public int minWeight;

	public MinimalEdgeMsg(int minWeight) { this.minWeight = minWeight; }

	@Override
	public Message clone() { return this; }
}
