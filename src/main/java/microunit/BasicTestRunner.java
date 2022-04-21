package microunit;

import example.ExampleTest;
import org.tinylog.Logger;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Class for running unit tests without support for expected exceptions.
 */
public class BasicTestRunner extends TestRunner {

    /**
     * Creates a {@code BasicTestRunner} object for executing the test methods
     * of the class specified.
     *
     * @param testClass the class whose test methods will be executed
     */
    public BasicTestRunner(Class<?> testClass) { super(testClass); Logger.info("basic test runner object created"); }

    @Override
    public void invokeTestMethod(Method testMethod, Object instance, TestResultAccumulator results)
            throws IllegalAccessException {
        Logger.debug("Testing {}", testMethod);
        try {
            testMethod.invoke(instance);
            results.onSuccess(testMethod);
        } catch (InvocationTargetException e) {
            Throwable cause = e.getCause();
            Logger.error(cause.getMessage(), cause);
            if (cause instanceof AssertionError) {
                results.onFailure(testMethod);
            } else {
                results.onError(testMethod);
            }
        }
    }

    // CHECKSTYLE:OFF
    public static void main(String[] args) throws Exception {
        Class<?> testClass = Class.forName(args[0]);
        new BasicTestRunner(testClass).runTestMethods();
        new BasicTestRunner(example.ExampleTest.class).runTestMethods();
    }

}