// In the name of God

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.stream.IntStream;


public class Master {
    private int nodeCount;
    private ArrayList<Node> nodes;
    private int maxIteration;
    private FailureFunction packetLoss;

    Master(int nodeCount, int maxIteration) {
        this.nodeCount = nodeCount;
        this.maxIteration = maxIteration;
        this.nodes = new ArrayList<>(nodeCount);

        initializeNodes();
    }

    private void initializeNodes() {
        // first node is key generator
        nodes.add(0, new Node(0, nodeCount, maxIteration, true));

        for (int id = 1; id < nodeCount; id++)
            nodes.add(new Node(id, nodeCount, maxIteration, false));

    }

    private void simulateOnePhase() {
        nodes.parallelStream()
                .forEach((Node currentNode) ->
                    IntStream.range(0, nodeCount)
                            .filter(i -> !packetLoss.ignore())
                            .mapToObj(i -> nodes.get(i)).forEach(currentNode::sendInfoTo));
    }

    public void setPacketLoss(FailureFunction packetLoss) {
        this.packetLoss = packetLoss;
    }

    public LinkedList<OpinionValues> makeDecession() {

        for (int i = 0; i < maxIteration; i++)
            simulateOnePhase();

        LinkedList<OpinionValues> opinions = new LinkedList<>();

        for (Node node : nodes)
            opinions.add(node.getOpinion());

        return opinions;
    }

}
