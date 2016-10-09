package org.blume.modeller.ui.handlers.base;

import java.awt.event.ActionEvent;
import java.util.Collection;
import java.util.Iterator;

import javax.swing.AbstractAction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.blume.modeller.Profile;
import org.blume.modeller.bag.impl.DefaultBag;
import org.blume.modeller.ui.BagTree;
import org.blume.modeller.ui.jpanel.base.BagView;
import org.blume.modeller.ui.jpanel.base.NewBagFrame;
import org.blume.modeller.ui.util.ApplicationContextUtil;
import gov.loc.repository.bagit.BagFactory;
import gov.loc.repository.bagit.BagFile;

public class StartNewBagHandler extends AbstractAction {
  private static final long serialVersionUID = 1L;
  protected static final Logger log = LoggerFactory.getLogger(StartNewBagHandler.class);

  BagView bagView;

  public StartNewBagHandler(BagView bagView) {
    super();
    this.bagView = bagView;
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    newBag();
  }

  public void newBag() {
    NewBagFrame newBagFrame = new NewBagFrame(bagView, bagView.getPropertyMessage("bag.frame.new"));
    newBagFrame.setVisible(true);
  }

  public void createNewBag(String profileName) {
    log.info("Creating a new bag with version: {}, profile: {}", BagFactory.LATEST.versionString, profileName);

    bagView.clearBagHandler.clearExistingBag();
    DefaultBag bag = bagView.getBag();
    bagView.infoInputPane.bagInfoInputPane.enableForms(true);

    String bagName = bagView.getPropertyMessage("bag.label.noname");
    bag.setName(bagName);
    bagView.infoInputPane.setBagName(bagName);

    bagView.bagTagFileTree = new BagTree(bagView, bag.getName());
    Collection<BagFile> tags = bag.getTags();
    for (Iterator<BagFile> it = tags.iterator(); it.hasNext();) {
      BagFile bf = it.next();
      bagView.bagTagFileTree.addNode(bf.getFilepath());
    }
    bagView.bagTagFileTreePanel.refresh(bagView.bagTagFileTree);
    bagView.updateBaggerRules();
    bag.setRootDir(bagView.getBagRootPath());

    bagView.infoInputPane.bagInfoInputPane.populateForms(bag);
    ApplicationContextUtil.addConsoleMessage("A new bag has been created in memory.");
    bagView.updateNewBag();

    // set bagItVersion
    bagView.infoInputPane.bagVersionValue.setText(BagFactory.LATEST.versionString);

    // change profile
    changeProfile(profileName);
  }

  // TODO refactor
  private void changeProfile(String selected) {
    Profile profile = bagView.getProfileStore().getProfile(selected);
    log.info("bagProject: {}", profile.getName());
    DefaultBag bag = bagView.getBag();
    bag.setProfile(profile, true);
    bagView.infoInputPane.bagInfoInputPane.updateProject(bagView);

    bagView.infoInputPane.setProfile(bag.getProfile().getName());
  }
}
