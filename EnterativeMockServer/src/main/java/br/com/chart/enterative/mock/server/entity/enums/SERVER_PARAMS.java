package br.com.chart.enterative.mock.server.entity.enums;

/**
 *
 * @author William Leite
 */
public enum SERVER_PARAMS {
    ECHO_SUCCESSFUL("ECHO Successful");

    private final String name;

    SERVER_PARAMS(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
