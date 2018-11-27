// In the name of God

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.LinkedList;
import java.util.Random;


class MasterTest {

    @ParameterizedTest
    @CsvSource({"10, 20, 50"})
    void reportTest(int maxNodeCount, int maxIteration, int repeatTestCase) {
        int counter = 0;
        int failure = 0;
        for (int nodeCount = 2; nodeCount <= maxNodeCount; nodeCount++) {
            for (int iteration = 2; iteration <= maxIteration; iteration++)
                for (int i = 0; i < repeatTestCase; i++) {
                    counter++;
                    if (!makeDecessionTest(nodeCount, maxIteration)) {
                        failure++;
                    }

                }
        }
        System.out.println("total tests:" + counter);
        System.out.println("failed tests:" + failure);
        System.out.println(((float) failure / counter) * 100);
    }


    private boolean makeDecessionTest(int nodeCount, int maxIteration) {
        Random randGenerator = new Random();

        Master master = new Master(nodeCount, maxIteration);
        master.setPacketLoss(randGenerator::nextBoolean);

        LinkedList<OpinionValues> result = master.makeDecession();

        return isValid(nodeCount, result);

    }

    private boolean isValid(int nodeCount, LinkedList<OpinionValues> result) {
        boolean validate = true;

        OpinionValues first = result.get(0);
        for (int i = 1; i < nodeCount; i++)
            if (result.get(i) != first)
                validate = false;

        return validate;
    }

}