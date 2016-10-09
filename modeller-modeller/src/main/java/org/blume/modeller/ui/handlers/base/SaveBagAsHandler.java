package org.blume.modeller.ui.handlers.base;

import org.blume.modeller.bag.impl.DefaultBag;
import org.blume.modeller.ui.jpanel.base.BagView;
import org.blume.modeller.ui.jpanel.base.SaveBagFrame;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

public class SaveBagAsHandler extends AbstractAction {
  private static final long serialVersionUID = 1L;
  private SaveBagFrame saveBagFrame;
  BagView bagView;
  DefaultBag bag;

  public SaveBagAsHandler(BagView bagView) {
    super();
    this.bagView = bagView;
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    openSaveBagAsFrame();
  }

  public void openSaveBagAsFrame() {
    bag = bagView.getBag();
    saveBagFrame = new SaveBagFrame(bagView, bagView.getPropertyMessage("bag.frame.save"));
    saveBagFrame.setBag(bag);
    saveBagFrame.setVisible(true);
  }
}
