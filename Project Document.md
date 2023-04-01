# 22932 Distributed Algorithms for Communication Networks - Project Document

## Project Design
### GHS Implementation
To implement the GHS algorithm, my solution makes use of a state variable defined in CustomGlobal. This state variable indicates to the graph nodes the current step in the algorithm and therefore dictates the desired behavior of each node. Of course, it is necessary therefore for each node to be able to compute it - indeed, the state may have one of 7 values, and is determined solely by the current round number. The algorithm states are:
`getMWOE, convergecastMWOE, broadcastMWOE, replaceLeader, connectFragments, updateFragID, finished`

#### State [getMWOE]
In this state, each node obtains its MWOE (Minimal Weight Outgoing Edge) if it exists.
The state lasts 1 round which is the first of each phase.

#### State [convergecastMWOE]
The MWOE of the fragment is propagated to the root. This takes n rounds.


#### State [broadcastMWOE]
The MWOE of the fragment is distributed to all the cluster's nodes. This takes n rounds.


#### State [replaceLeader]
Certain edges are flipped such that a new root is erected for the cluster. This takes n rounds.

#### State [connectFragments]
Initial fragments merging takes place and new roots for the merged fragments are found. This takes 1 round.

#### State [updateFragID]
The new fragment ID is propagated throughout the cluster to every node. This takes n rounds.

#### State [finished]
The GHS algorithm has terminated, this state allows client-server communication functionality.
Note that the algorithm enters this state where log2(n) phases have passed and therefore the algorithm has definitely terminated - in practice it often arrives at the MST many rounds before.

### Client-Server Communication
Some static boolean variables are defined in the GraphNode class to allow for case-switching when determining the desired behavior of the graph nodes in context of client-server communication post-MST computation. They are of little import with regards to the algorithm implementation and mainly manage visual features (i.e. highlighting of traversed edges during message transmission).
In this stage, the message classes used are `ServerMsg` and `InvertEdgeMsg`.

### Message Classes
The implementation makes use of various message classes in different states of the GHS algorithms and stages of the program.

#### Message [ConnectMsg]
Sent from the root of a fragment to a neighbor in a different cluster. Used to merge clusters and updating roots for the resulting fragments.

#### Message [FragmentIdMsg]
Sent from the root of a newly-formed fragment to all of its members - used to update their fragment association.

#### Message [InvertEdgeMsg]
Sent from a newly-erected root a fragment along the path to the previous root so that the edges are inverted and thus all intra-cluster paths point to the new root. 
Also used in the client-server communication stage when choosing a new server, in an identical manner.

#### Message [MinimalEdgeMsg]
Used to propagate the MWOE to the root and then from the root throughout the cluster to all nodes.

#### Message [ServerMsg]
Used in the client-server communication stage to send messages to and from the server.


### Distribution & Connectivity Models
The implementation makes use of Sinalgo's natural partitioning of projects into models. Therefore, the distribution and node connectivity logic is implemented as models in the `models` directory.

#### Distribution Model [RandomDisc]
This Distributes the nodes at random in a disc centered around the origin of the canvas, and radius stretched to its edges.

#### Connectivity Model [RandomConnectivity]
This connects the nodes in a manner where every node selects 7 other nodes at random to which in erects a link, the process continues until every node is processed in this way.

### Visual Features
The implementation makes heavy use of Sinalgo's GUI features. All buttons have custom icons, and the visual panel reflects information using colors and styles. For example, all intrafragment edges are colored the same as the fragment color, and all interfragment edges are colored a dull gray - that creates a stronger separation of fragments.
In the client-server communication stage, traversed edges become red and bold.
These are made able by overriding the draw() and color() methods of the nodes and edges implementations used in the project.

## SINR Networks
The SINR model introduces interference into the network. In our case, this interference will be first modeled as a Sinalgo interference model. It will be dealt with by simulation of each round of our original algorithm with Delta * log(n) rounds in te SINR model. This simulation will be done as we saw in class, and implemented in code as a function that calls our original round function in the manner described in class. This simulation will cause the total number of rounds to be multiplied by (Delta * log(n)), since each round is simulated by that many SINR rounds.

## Performance Table

| Number of Nodes | Total Graph Weight | Computed MST Weight |
| --------------- | ------------------ | ------------------- |
|       290       |    9.808238e+11    |    2.536535e+10     |
|       380       |    1.313013e+12    |    3.143366e+10     |
|       470       |    1.611294e+12    |    4.183506e+10     |
|       560       |    1.934863e+12    |    4.802604e+10     |
|       650       |    2.247262e+12    |    5.712892e+10     |
|       740       |    2.585940e+12    |    6.278623e+10     |
|       830       |    2.863565e+12    |    6.485188e+10     |
|       920       |    3.191010e+12    |    7.642307e+10     |
|      1010       |    3.496714e+12    |    8.049117e+10     |
|      1100       |    3.825837e+12    |    9.432036e+10     |
|      1190       |    4.152952e+12    |    1.012079e+11     |
|      1280       |    4.473263e+12    |    1.064705e+11     |
|      1370       |    4.740246e+12    |    1.110318e+11     |
|      1460       |    5.045069e+12    |    1.185162e+11     |
|      1550       |    5.364693e+12    |    1.251506e+11     |
|      1640       |    5.730129e+12    |    1.394332e+11     |
|      1730       |    6.017397e+12    |    1.456748e+11     |
|      1820       |    6.282957e+12    |    1.472448e+11     |
|      1910       |    6.653756e+12    |    1.594352e+11     |
|      2000       |    7.025307e+12    |    1.727820e+11     |
