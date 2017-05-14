package cool.pandora.modeller.ui.handlers.iiif;

import cool.pandora.modeller.ModellerClient;
import cool.pandora.modeller.ModellerClientFailedException;
import cool.pandora.modeller.bag.BagInfoField;
import cool.pandora.modeller.bag.impl.DefaultBag;
import cool.pandora.modeller.ui.Progress;
import cool.pandora.modeller.ui.handlers.base.SaveBagHandler;
import cool.pandora.modeller.ui.handlers.common.IIIFObjectURI;
import cool.pandora.modeller.ui.jpanel.base.BagView;
import cool.pandora.modeller.ui.jpanel.iiif.CreateCanvasesFrame;
import cool.pandora.modeller.ui.util.ApplicationContextUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.event.ActionEvent;
import java.net.URI;
import java.util.ArrayList;
import java.util.Map;

import javax.swing.AbstractAction;

import static org.apache.commons.lang3.exception.ExceptionUtils.getMessage;

/**
 * Create Canvases Handler
 *
 * @author Christopher Johnson
 */
public class CreateCanvasesHandler extends AbstractAction implements Progress {
    protected static final Logger log = LoggerFactory.getLogger(SaveBagHandler.class);
    private static final long serialVersionUID = 1L;
    private final BagView bagView;

    /**
     * @param bagView BagView
     */
    public CreateCanvasesHandler(final BagView bagView) {
        super();
        this.bagView = bagView;
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        openCreateCanvasesFrame();
    }

    @Override
    public void execute() {
        final String message = ApplicationContextUtil.getMessage("bag.message.canvascreated");
        final DefaultBag bag = bagView.getBag();
        final Map<String, BagInfoField> map = bag.getInfo().getFieldMap();
        final ResourceIdentifierList idList = new ResourceIdentifierList(bagView);
        final ArrayList<String> resourceIDList = idList.getResourceIdentifierList();
        for (final String resourceID : resourceIDList) {
            final URI canvasObjectURI = IIIFObjectURI.getCanvasObjectURI(map, resourceID);
            try {
                ModellerClient.doPut(canvasObjectURI);
                ApplicationContextUtil.addConsoleMessage(message + " " + canvasObjectURI);
            } catch (final ModellerClientFailedException e) {
                ApplicationContextUtil.addConsoleMessage(getMessage(e));
            }
        }
        bagView.getControl().invalidate();
    }

    void openCreateCanvasesFrame() {
        final DefaultBag bag = bagView.getBag();
        final CreateCanvasesFrame createCanvasesFrame =
                new CreateCanvasesFrame(bagView, bagView.getPropertyMessage("bag" + ".frame.canvas"));
        createCanvasesFrame.setBag(bag);
        createCanvasesFrame.setVisible(true);
    }
}