package org.blume.modeller.ui.handlers.base;

import org.blume.modeller.bag.impl.DefaultBag;
import org.blume.modeller.ui.jpanel.BagView;
import org.blume.modeller.ui.Progress;
import org.blume.modeller.ui.util.ApplicationContextUtil;
import gov.loc.repository.bagit.verify.impl.CompleteVerifierImpl;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.SwingUtilities;

public class CompleteBagHandler extends AbstractAction implements Progress {
  private static final long serialVersionUID = 1L;
  private BagView bagView;
  private String messages;

  public CompleteBagHandler(BagView bagView) {
    super();
    this.bagView = bagView;
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    completeBag();
  }

  public void completeBag() {
    bagView.statusBarBegin(this, "Checking if complete...", null);
  }

  @Override
  public void execute() {
    DefaultBag bag = bagView.getBag();
    try {
      CompleteVerifierImpl completeVerifier = new CompleteVerifierImpl();
      completeVerifier.addProgressListener(bagView.task);
      bagView.longRunningProcess = completeVerifier;

      messages = bag.completeBag(completeVerifier);

      if (messages != null && !messages.trim().isEmpty()) {
        bagView.showWarningErrorDialog("Warning - incomplete", "Is complete result: " + messages);
      }
      else {
        bagView.showWarningErrorDialog("Is Complete Dialog", "Bag is complete.");
      }

      SwingUtilities.invokeLater(new Runnable() {
        @Override
        public void run() {
          ApplicationContextUtil.addConsoleMessage(messages);
        }
      });

    }
    catch (Exception e) {
      e.printStackTrace();

      if (bagView.longRunningProcess.isCancelled()) {
        bagView.showWarningErrorDialog("Check cancelled", "Completion check cancelled.");
      }
      else {
        bagView.showWarningErrorDialog("Warning - complete check interrupted", "Error checking bag completeness: " + e.getMessage());
      }
    }
    finally {
      bagView.task.done();
      bagView.statusBarEnd();
    }
  }
}