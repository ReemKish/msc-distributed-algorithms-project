package projects.centralized_computing.nodes.messages;

import sinalgo.nodes.messages.Message;

public class FragmentIdMsg extends Message {
	
	public FragmentIdMsg(int fragID) { this.fragID = fragID; }
	@Override
	public Message clone() { return this; }
}
