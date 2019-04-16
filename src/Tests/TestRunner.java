package Tests;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;


class TestRunner {

    public static void main(String[] args) {
        Result result1 = JUnitCore.runClasses(JunitDAOTests.class);
        for (Failure failure : result1.getFailures()){
            System.out.println("\u001B[31m" +failure.toString());
            System.out.println("\u001b[0m");
        }

        Result result2 = JUnitCore.runClasses(JunitServiceTests.class);

        for (Failure failure : result2.getFailures()){
            System.out.println("\u001B[31m" +failure.toString());
            System.out.println("\u001b[0m");
        }
        if (result1.wasSuccessful()) System.out.println("DAO Tests: " + "\u001B[32m" + result1.wasSuccessful());
        else System.out.println("DAO Tests: " + "\u001B[31m" + result1.wasSuccessful());
        System.out.println("\u001b[0m");
        if (result2.wasSuccessful()) System.out.println("Service Tests: " + "\u001B[32m" + result2.wasSuccessful());
        else System.out.println("Service Tests: " + "\u001B[31m" + result2.wasSuccessful());
        System.out.println("\u001b[0m");
    }
}
