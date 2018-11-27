// In the name of God

public class Packet {
    private OpinionValues opinion;
    private int informationLevel;
    private int key;

    Packet(OpinionValues opinion, int informationLevel, int key){
        this.opinion = opinion;
        this.informationLevel = informationLevel;
        this.key = key;
    }

    public void setOpinion(OpinionValues opinion) {
        this.opinion = opinion;
    }

    public void setInformationLevel(int informationLevel) {
        this.informationLevel = informationLevel;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public OpinionValues getOpinion() {
        return opinion;
    }

    public int getInformationLevel() {
        return informationLevel;
    }

    public int getKey() {
        return key;
    }

    @Override
    public String toString() {
        return "(Opinion: " + opinion + ", Information Level: "+ informationLevel + ", key: "+ key + ")";
    }

}
