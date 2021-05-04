package br.com.chart.enterative.vo.epay;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author William Leite
 */
@XStreamAlias("GetAttachmentResponse")
public class EpayGetAttachmentResponse implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @XStreamAlias("GetAttachmentResult")
    @Getter @Setter private byte[] result;
}