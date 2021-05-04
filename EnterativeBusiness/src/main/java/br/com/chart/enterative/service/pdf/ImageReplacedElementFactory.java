package br.com.chart.enterative.service.pdf;

import com.lowagie.text.BadElementException;
import com.lowagie.text.Image;
import java.io.IOException;
import java.util.Base64;
import java.util.Objects;
import org.w3c.dom.Element;
import org.xhtmlrenderer.extend.FSImage;
import org.xhtmlrenderer.extend.ReplacedElement;
import org.xhtmlrenderer.extend.ReplacedElementFactory;
import org.xhtmlrenderer.extend.UserAgentCallback;
import org.xhtmlrenderer.layout.LayoutContext;
import org.xhtmlrenderer.pdf.ITextFSImage;
import org.xhtmlrenderer.pdf.ITextImageElement;
import org.xhtmlrenderer.render.BlockBox;
import org.xhtmlrenderer.simple.extend.FormSubmissionListener;

/**
 *
 * @author William Leite
 */
public class ImageReplacedElementFactory implements ReplacedElementFactory {

    private final ReplacedElementFactory superFactory;

    public ImageReplacedElementFactory(ReplacedElementFactory superFactory) {
        this.superFactory = superFactory;
    }

    @Override
    public ReplacedElement createReplacedElement(LayoutContext lc, BlockBox blockBox, UserAgentCallback uac, int cssWidth, int cssHeight) {
        Element element = blockBox.getElement();
        if (element == null) {
            return null;
        }

        String nodeName = element.getNodeName();
        String className = element.getAttribute("class");
        if ("div".equals(nodeName) && className.contains("report_image")) {
            try {
                byte[] bytes = Base64.getDecoder().decode(element.getAttribute("dataurl"));
                Image image = Image.getInstance(bytes);
                FSImage fsImage = new ITextFSImage(image);
                if (Objects.nonNull(fsImage)) {
                    if ((cssWidth != -1) || (cssHeight != -1)) {
                        fsImage.scale(cssWidth, cssHeight);
                    }
                    return new ITextImageElement(fsImage);
                }
            } catch (BadElementException | IOException ex) {
                ex.printStackTrace();
            }
        }

        return superFactory.createReplacedElement(lc, blockBox, uac, cssWidth, cssHeight);
    }

    @Override
    public void reset() {
        this.superFactory.reset();
    }

    @Override
    public void remove(Element elmnt) {
        this.superFactory.remove(elmnt);
    }

    @Override
    public void setFormSubmissionListener(FormSubmissionListener fl) {
        this.superFactory.setFormSubmissionListener(fl);
    }

}
