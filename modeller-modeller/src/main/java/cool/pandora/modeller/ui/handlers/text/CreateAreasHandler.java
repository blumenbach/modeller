package cool.pandora.modeller.ui.handlers.text;

import org.apache.commons.lang.StringUtils;
import cool.pandora.modeller.ModellerClient;
import cool.pandora.modeller.ModellerClientFailedException;
import cool.pandora.modeller.DocManifestBuilder;
import cool.pandora.modeller.hOCRData;
import cool.pandora.modeller.bag.BagInfoField;
import cool.pandora.modeller.bag.impl.DefaultBag;
import cool.pandora.modeller.ui.Progress;
import cool.pandora.modeller.ui.handlers.common.TextObjectURI;
import cool.pandora.modeller.ui.jpanel.base.BagView;
import cool.pandora.modeller.ui.jpanel.text.CreateAreasFrame;
import cool.pandora.modeller.ui.util.ApplicationContextUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.AbstractAction;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Map;

import static org.apache.commons.lang3.exception.ExceptionUtils.getMessage;
import static cool.pandora.modeller.DocManifestBuilder.getAreaIdList;

/**
 * Create Areas Handler
 *
 * @author Christopher Johnson
 */
public class CreateAreasHandler extends AbstractAction implements Progress {
    protected static final Logger log = LoggerFactory.getLogger(CreateAreasHandler.class);
    private static final long serialVersionUID = 1L;
    private final BagView bagView;

    /**
     * @param bagView BagView
     */
    public CreateAreasHandler(final BagView bagView) {
        super();
        this.bagView = bagView;
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        openCreateAreasFrame();
    }

    @Override
    public void execute() {
        final String message = ApplicationContextUtil.getMessage("bag.message.areacreated");
        final DefaultBag bag = bagView.getBag();
        final Map<String, BagInfoField> map = bag.getInfo().getFieldMap();
        final String url = bag.gethOCRResource();
        List<String> areaIdList = null;
        try {
            final hOCRData hocr = DocManifestBuilder.gethOCRProjectionFromURL(url);
            areaIdList = getAreaIdList(hocr);
        } catch (final IOException e) {
            e.printStackTrace();
        }
        assert areaIdList != null;
        for (String resourceID : areaIdList) {
            resourceID = StringUtils.substringAfter(resourceID, "_");
            final URI areaObjectURI = TextObjectURI.getAreaObjectURI(map, resourceID);
            try {
                ModellerClient.doPut(areaObjectURI);
                ApplicationContextUtil.addConsoleMessage(message + " " + areaObjectURI);
            } catch (final ModellerClientFailedException e) {
                ApplicationContextUtil.addConsoleMessage(getMessage(e));
            }
        }
        bagView.getControl().invalidate();
    }

    void openCreateAreasFrame() {
        final DefaultBag bag = bagView.getBag();
        final CreateAreasFrame createAreasFrame =
                new CreateAreasFrame(bagView, bagView.getPropertyMessage("bag.frame" + ".areas"));
        createAreasFrame.setBag(bag);
        createAreasFrame.setVisible(true);
    }
}