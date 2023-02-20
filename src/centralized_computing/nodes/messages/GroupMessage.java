package projects.centralized_computing.nodes.messages;

import sinalgo.nodes.messages.Message;

public class GroupMessage extends Message {
	
	public boolean inGroup;
	public GroupMessage(boolean inGroup) { this.inGroup = inGroup; }
	@Override
	public Message clone() { return this; }
}
