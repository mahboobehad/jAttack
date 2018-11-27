// In the name of God

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class Node {
    private int nodeCount;
    private int maxIterations;
    private int key;
    private int uid;
    private final ArrayList<Packet> receivedPackets;

    Node(int uid, int nodeCount, int maxIterations, boolean keyGenerator) {
        this.nodeCount = nodeCount;
        this.maxIterations = maxIterations;
        this.receivedPackets = new ArrayList<>(this.nodeCount);
        this.uid = uid;

        initializeStates();

        this.key = keyGenerator ? generateKey() : -1;
    }

    private void initializeStates() {

        for (int i = 0; i < nodeCount; i++)
            receivedPackets.add(new Packet(OpinionValues.UNKNOWN, -1, -1));

        receivedPackets.get(uid).setOpinion(OpinionValues.getRandomAttackValue());
        receivedPackets.get(uid).setInformationLevel(0);
    }

    private int generateKey() {
        key = ThreadLocalRandom.current().nextInt(1, maxIterations + 1);
        receivedPackets.get(uid).setKey(key);
        return key;
    }

    ArrayList<Packet> getSystemInfo() {
        return receivedPackets;
    }

    void setReceivedPackets(ArrayList<Packet> otherNodesOpinion) {
        synchronized (receivedPackets) {
            int i = 0;
            int minLevel = maxIterations + 10;

            for (Packet currentState : otherNodesOpinion) {
                if (i != uid) {

                    updateNodeState(i, currentState);

                    // upon receiving a message containing a valid key value update this node's key
                    if (this.key == -1 && currentState.getKey() != -1) {
                        this.key = currentState.getKey();
                        this.receivedPackets.get(uid).setKey(this.key);
                    }

                    minLevel = Math.min(minLevel, currentState.getInformationLevel());
                }
                i += 1;
            }
            receivedPackets.get(uid).setInformationLevel(minLevel + 1);
        }
    }

    private void updateNodeState(int nodeId, Packet message) {

        // update Information level
        receivedPackets.get(nodeId).setInformationLevel(Math.max(receivedPackets.get(nodeId).getInformationLevel(),
                message.getInformationLevel()));

        // update Opinion value
        if (message.getOpinion() != OpinionValues.UNKNOWN)
            receivedPackets.get(nodeId).setOpinion(message.getOpinion());
    }

    OpinionValues getOpinion() {
        if (key != -1 && receivedPackets.get(uid).getInformationLevel() >= key) {
            for (Packet state : receivedPackets) {
                if (state.getOpinion() == OpinionValues.ABORT)
                    return OpinionValues.ABORT;
            }
            return OpinionValues.COMMIT;
        }
        return OpinionValues.ABORT;
    }

    public void sendInfoTo(Node otherNode) {
        if (otherNode.getUid() != this.uid)
            otherNode.setReceivedPackets(this.receivedPackets);
    }

    public int getUid() {
        return uid;
    }

    @Override
    public String toString() {
        return "node id: " + uid + ", receivedPackets:" + receivedPackets.stream()
                .map(Packet::toString)
                .collect(Collectors.joining(","));
    }

}
