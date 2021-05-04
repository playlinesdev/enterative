package br.com.chart.enterative.vo;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author William
 */
public class ChangePasswordVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Getter @Setter private String oldPassword;
    @Getter @Setter private String newPassword;
    @Getter @Setter private String confirmNewPassword;
}
