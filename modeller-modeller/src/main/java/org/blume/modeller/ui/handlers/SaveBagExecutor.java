package org.blume.modeller.ui.handlers;

import org.springframework.richclient.command.support.AbstractActionCommandExecutor;

import org.blume.modeller.ui.BagView;

public class SaveBagExecutor extends AbstractActionCommandExecutor {
  BagView bagView;

  public SaveBagExecutor(BagView bagView) {
    super();
    this.bagView = bagView;
  }

  @Override
  public void execute() {
    if (bagView.getBagRootPath().exists()) {
      bagView.saveBagHandler.setTmpRootPath(bagView.getBagRootPath());
      bagView.saveBagHandler.confirmWriteBag();
    }
    else {
      bagView.saveBagHandler.saveBag(bagView.getBagRootPath());
    }
  }

}
