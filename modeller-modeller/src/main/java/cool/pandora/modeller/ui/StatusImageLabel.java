package cool.pandora.modeller.ui;

import cool.pandora.modeller.model.Status;
import cool.pandora.modeller.model.StatusModel;
import cool.pandora.modeller.ui.util.ApplicationContextUtil;

import javax.swing.JLabel;
import javax.swing.ImageIcon;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Status Image Label
 *
 * @author gov.loc
 */
public class StatusImageLabel extends JLabel implements PropertyChangeListener {

    private static final long serialVersionUID = 1L;

    private static final String PASS_STATUS_ICON = "status.success.icon";
    private static final String FAILURE_STATUS_ICON = "status.fail.icon";
    private static final String UNKNOWN_STATUS_ICON = "status.unknown.icon";

    /**
     * @param model StatusModel
     */
    public StatusImageLabel(final StatusModel model) {
        super("");
        changeIcon(model.getStatus());
        model.addPropertyChangeListener(this);
    }

    /**
     * @param evt PropertyChangeEvent
     */
    @Override
    public void propertyChange(final PropertyChangeEvent evt) {
        final Status newValue = (Status) evt.getNewValue();
        changeIcon(newValue);
    }

    /**
     * @param status Status
     */
    private void changeIcon(final Status status) {
        final ImageIcon icon;
        if (status == Status.PASS) {
            icon = new ImageIcon(ApplicationContextUtil.getImage(PASS_STATUS_ICON));
            setToolTipText(ApplicationContextUtil.getMessage("consolepane.status.pass.help"));
        } else if (status == Status.FAILURE) {
            icon = new ImageIcon(ApplicationContextUtil.getImage(FAILURE_STATUS_ICON));
            setToolTipText(ApplicationContextUtil.getMessage("consolepane.status.fail.help"));
        } else {
            icon = new ImageIcon(ApplicationContextUtil.getImage(UNKNOWN_STATUS_ICON));
            setToolTipText(ApplicationContextUtil.getMessage("consolepane.status.unknown.help"));
        }
        this.setIcon(icon);

    }
}