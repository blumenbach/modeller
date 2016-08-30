package org.blume.modeller.ui.handlers.iiif;

import java.awt.event.ActionEvent;
import java.io.File;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.swing.AbstractAction;

import gov.loc.repository.bagit.impl.AbstractBagConstants;
import org.blume.modeller.bag.BagInfoField;
import org.blume.modeller.bag.BaggerFileEntity;
import org.blume.modeller.ui.util.ImageIOUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.blume.modeller.bag.impl.DefaultBag;
import org.blume.modeller.ui.jpanel.BagView;
import org.blume.modeller.ui.Progress;
import org.blume.modeller.ui.util.ApplicationContextUtil;
import org.blume.modeller.ui.jpanel.UploadBagFrame;
import org.blume.modeller.ModellerClient;


public class UploadBagHandler extends AbstractAction implements Progress {
    protected static final Logger log = LoggerFactory.getLogger(UploadBagHandler.class);
    private static final long serialVersionUID = 1L;
    private UploadBagFrame uploadBagFrame;
    DefaultBag bag;
    private BagView bagView;
    List<String> payload = null;
    HashMap<String, BagInfoField> map;
    BagInfoField baseURI, collectionRoot, objektID, IIIFResourceContainer;
    private File tmpRootPath;
    private boolean clearAfterSaving = false;
    private String messages;

    public UploadBagHandler(BagView bagView) {
        super();
        this.bagView = bagView;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
    }

    @Override
    public void execute() {
        String message = ApplicationContextUtil.getMessage("bag.message.fileuploaded");
        DefaultBag bag = bagView.getBag();
        payload = bag.getPayloadPaths();
        map = bag.getInfo().getFieldMap();
        String resourceContainer = getResourceContainer(map);
        ModellerClient client = new ModellerClient();
        ImageIOUtil imageioutil = new ImageIOUtil();
        String basePath = AbstractBagConstants.DATA_DIRECTORY;
        Path rootDir = bagView.getBagRootPath().toPath();
        for (String filePath : payload) {
            String normalPath = BaggerFileEntity.removeBasePath(basePath, filePath);
            String destinationURI = getDestinationURI(resourceContainer, normalPath);
            Path absoluteFilePath = rootDir.resolve(filePath);
            File resourceFile = absoluteFilePath.toFile();
            String contentType = imageioutil.getImageMIMEType(resourceFile);
            try {
                client.doBinaryPut(destinationURI, resourceFile, contentType);
            } finally {
                ApplicationContextUtil.addConsoleMessage(message + " " + destinationURI);
            }
        }
        bagView.getControl().invalidate();
    }

    public void openUploadBagFrame() {
        bag = bagView.getBag();
        uploadBagFrame = new UploadBagFrame(bagView, bagView.getPropertyMessage("bag.frame.upload"));
        uploadBagFrame.setBag(bag);
        uploadBagFrame.setVisible(true);
    }

    public String getDestinationURI(String resourceContainer, String normalPath) {
        return resourceContainer +
                normalPath;
    }

    public String getResourceContainer(HashMap<String, BagInfoField> map) {
        baseURI = map.get("FedoraBaseURI");
        collectionRoot = map.get("CollectionRoot");
        objektID = map.get("ObjektID");
        IIIFResourceContainer = map.get("IIIFResourceContainer");
        return baseURI.getValue() +
                collectionRoot.getValue() +
                objektID.getValue() +
                IIIFResourceContainer.getValue();
    }
}