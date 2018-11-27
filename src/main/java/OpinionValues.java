import java.util.concurrent.ThreadLocalRandom;

public enum OpinionValues {
    UNKNOWN, ABORT, COMMIT;

    public static OpinionValues getRandomAttackValue(){
        int rand = ThreadLocalRandom.current().nextInt(0, 2);
        if (rand == 0)
            return OpinionValues.COMMIT;
        else
            return OpinionValues.ABORT;
    }

}
