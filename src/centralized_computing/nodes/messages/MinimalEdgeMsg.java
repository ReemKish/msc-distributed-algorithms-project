package projects.centralized_computing.nodes.messages;

import sinalgo.nodes.messages.Message;

public class MinimalEdgeMsg extends Message {
  public int weight;

	public MinimalEdgeMsg(int weight) { this.weight = weight; }

	@Override
	public Message clone() { return this; }
}
