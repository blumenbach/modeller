package org.blume.modeller.ui.handlers.iiif;

import org.springframework.richclient.command.support.AbstractActionCommandExecutor;

import org.blume.modeller.ui.jpanel.base.BagView;

public class PatchResourceExecutor extends AbstractActionCommandExecutor {
    BagView bagView;

    public  PatchResourceExecutor(BagView bagView) {
        super();
        this.bagView = bagView;
    }

    @Override
    public void execute() {
        bagView.patchResourceHandler.openPatchResourceFrame();
    }

}