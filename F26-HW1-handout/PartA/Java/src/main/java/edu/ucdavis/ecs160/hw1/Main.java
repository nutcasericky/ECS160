package edu.ucdavis.ecs160.hw1;

public class Main {
    public static void main(String[] args) {
        boolean assertionsEnabled = false;
        assert assertionsEnabled = true;
        if (!assertionsEnabled) {
            throw new IllegalStateException("Assertions are disabled; run with java -ea");
        }

        Configuration config1 = Configuration.getInstance();
        Configuration config2 = Configuration.getInstance();

        // Java does not expose memory addresses; identityHashCode is the closest equivalent.
        System.out.println("config1 identity: " + Integer.toHexString(System.identityHashCode(config1)));
        System.out.println("config2 identity: " + Integer.toHexString(System.identityHashCode(config2)));
        assert config1 == config2;

        System.out.println("All assertions passed");
    }
}
