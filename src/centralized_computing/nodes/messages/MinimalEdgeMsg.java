package projects.centralized_computing.nodes.messages;

import projects.centralized_computing.nodes.edges.WeightedEdge;
import sinalgo.nodes.messages.Message;

public class MinimalEdgeMsg extends Message {
  public WeightedEdge mwoe;

	public MinimalEdgeMsg(WeightedEdge mwoe) { this.mwoe = mwoe; }

	@Override
	public Message clone() { return this; }
}
