package br.com.chart.enterative.vo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 *
 * @author William
 */
public class ForgotPasswordVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Getter @Setter private String email;
    @Getter @Setter private String login;
}
