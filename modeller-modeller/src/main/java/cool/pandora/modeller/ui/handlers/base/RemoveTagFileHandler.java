package cool.pandora.modeller.ui.handlers.base;

import cool.pandora.modeller.bag.impl.DefaultBag;
import cool.pandora.modeller.ui.jpanel.base.BagView;
import cool.pandora.modeller.ui.util.ApplicationContextUtil;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreePath;

/**
 * Remove Tag File Handler
 *
 * @author gov.loc
 */
public class RemoveTagFileHandler extends AbstractAction {
    private static final long serialVersionUID = 1L;
    BagView bagView;

    /**
     * @param bagView BagView
     */
    public RemoveTagFileHandler(final BagView bagView) {
        super();
        this.bagView = bagView;
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        removeTagFile();
    }

    private void removeTagFile() {
        final DefaultBag bag = bagView.getBag();

        final TreePath[] paths = bagView.bagTagFileTree.getSelectionPaths();
        if (paths != null) {
            final DefaultTreeModel model = (DefaultTreeModel) bagView.bagTagFileTree.getModel();
            for (final TreePath path : paths) {
                final Object node = path.getLastPathComponent();
                try {
                    if (node != null) {
                        if (node instanceof MutableTreeNode) {
                            bag.removeBagFile(node.toString());
                            ApplicationContextUtil.addConsoleMessage("Tag file removed: " + node.toString());
                            model.removeNodeFromParent((MutableTreeNode) node);
                        } else {
                            bag.removeBagFile((String) node);
                            ApplicationContextUtil.addConsoleMessage("Tag file removed: " + node.toString());
                            final DefaultMutableTreeNode aNode = new DefaultMutableTreeNode(node);
                            model.removeNodeFromParent(aNode);
                        }
                    }
                } catch (final Exception e) {
                    BagView.showWarningErrorDialog("Error - file not removed",
                            "Error trying to remove file: " + node + "\n" + e.getMessage());
                }
            }
            bagView.bagTagFileTree.removeSelectionPaths(paths);
            bagView.bagTagFileTreePanel.refresh(bagView.bagTagFileTree);
        }
    }
}