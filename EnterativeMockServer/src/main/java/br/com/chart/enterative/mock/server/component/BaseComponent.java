package br.com.chart.enterative.mock.server.component;

/**
 *
 * @author William Leite
 */
public class BaseComponent {

    public void printVerbose(String message) {
        System.out.println(String.format("[VERBOSE] %s", message));
    }

    public void printVerbose(String pattern, String... args) {
        this.printVerbose(String.format(pattern, args));
    }
}
