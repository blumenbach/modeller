/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cool.pandora.modeller.ui.jpanel.base;

import cool.pandora.modeller.bag.impl.DefaultBag;
import gov.loc.repository.bagit.Manifest.Algorithm;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.AbstractAction;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.CommandGroup;
import org.springframework.richclient.core.DefaultMessage;
import org.springframework.richclient.dialog.TitlePane;
import org.springframework.richclient.util.GuiStandardUtils;

/**
 * SaveBagFrame.
 *
 * @author gov.loc
 */
public class SaveBagFrame extends JFrame implements ActionListener {
    protected static final Logger log = LoggerFactory.getLogger(SaveBagFrame.class);
    private static final long serialVersionUID = 1L;
    private final transient BagView bagView;
    private File bagFile;
    private String bagFileName = "";
    private final JPanel savePanel;
    private JTextField bagNameField;
    private JLabel urlLabel;
    private JTextField urlField;
    JButton okButton;
    JButton cancelButton;
    private JRadioButton noneButton;
    private JRadioButton zipButton;
    JRadioButton tarButton;
    JRadioButton tarGzButton;
    JRadioButton tarBz2Button;

    /**
     * SaveBagFrame.
     *
     * @param bagView BagView
     * @param title String
     */
    public SaveBagFrame(final BagView bagView, final String title) {
        super(title);
        this.bagView = bagView;
        if (bagView != null) {
            getContentPane().removeAll();
            savePanel = createComponents();
        } else {
            savePanel = new JPanel();
        }
        getContentPane().add(savePanel, BorderLayout.CENTER);
        final Dimension preferredDimension = new Dimension(600, 400);
        setPreferredSize(preferredDimension);
        this.setBounds(300, 200, 600, 400);
        pack();
    }

    /**
     * createButtonBar.
     *
     * @return buttonBar
     */
    protected JComponent createButtonBar() {
        final CommandGroup dialogCommandGroup = CommandGroup.createCommandGroup(null,
                getCommandGroupMembers());
        final JComponent buttonBar = dialogCommandGroup.createButtonBar();
        GuiStandardUtils.attachDialogBorder(buttonBar);
        return buttonBar;
    }

    /**
     * getCommandGroupMembers.
     *
     * @return AbstractCommand
     */
    protected Object[] getCommandGroupMembers() {
        return new AbstractCommand[]{finishCommand, cancelCommand};
    }

    /**
     * initStandardCommands.
     *
     * <p>Initialize the standard commands needed on a Dialog: Ok/Cancel.
     */
    private void initStandardCommands() {
        finishCommand = new ActionCommand(getFinishCommandId()) {
            @Override
            public void doExecuteCommand() {

                new OkSaveBagHandler().actionPerformed(null);

            }
        };

        cancelCommand = new ActionCommand(getCancelCommandId()) {

            @Override
            public void doExecuteCommand() {
                new CancelSaveBagHandler().actionPerformed(null);
            }
        };
    }

    protected static String getFinishCommandId() {
        return DEFAULT_FINISH_COMMAND_ID;
    }

    protected static String getCancelCommandId() {
        return DEFAULT_CANCEL_COMMAND_ID;
    }

    protected static final String DEFAULT_FINISH_COMMAND_ID = "okCommand";

    protected static final String DEFAULT_CANCEL_COMMAND_ID = "cancelCommand";

    private transient ActionCommand finishCommand;

    private transient ActionCommand cancelCommand;

    private JPanel createComponents() {
        final Border border = new EmptyBorder(5, 5, 5, 5);

        final TitlePane titlePane = new TitlePane();
        initStandardCommands();
        final JPanel pageControl = new JPanel(new BorderLayout());
        final JPanel titlePaneContainer = new JPanel(new BorderLayout());
        titlePane.setTitle(bagView.getPropertyMessage("SaveBagFrame.title"));
        titlePane.setMessage(new DefaultMessage(bagView.getPropertyMessage("Define the Bag "
                + "settings")));
        titlePaneContainer.add(titlePane.getControl());
        titlePaneContainer.add(new JSeparator(), BorderLayout.SOUTH);
        pageControl.add(titlePaneContainer, BorderLayout.NORTH);
        final JPanel contentPane = new JPanel();

        // TODO: Add bag name field
        // TODO: Add save name file selection button
        final JLabel location = new JLabel("Save in:");
        final JButton browseButton = new JButton(getMessage("bag.button.browse"));
        browseButton.addActionListener(new SaveBagAsHandler());
        browseButton.setEnabled(true);
        browseButton.setToolTipText(getMessage("bag.button.browse.help"));
        final DefaultBag bag = bagView.getBag();
        if (bag != null) {
            bagNameField = new JTextField(bag.getName());
            bagNameField.setCaretPosition(bag.getName().length());
            bagNameField.setEditable(false);
            bagNameField.setEnabled(false);
        }

        // Holey bag control
        final JLabel holeyLabel = new JLabel(bagView.getPropertyMessage("bag.label.isholey"));
        holeyLabel.setToolTipText(bagView.getPropertyMessage("bag.isholey.help"));
        final JCheckBox holeyCheckbox = new JCheckBox(bagView.getPropertyMessage("bag.checkbox"
                + ".isholey"));
        holeyCheckbox.setBorder(border);
        holeyCheckbox.addActionListener(new HoleyBagHandler());
        holeyCheckbox.setToolTipText(bagView.getPropertyMessage("bag.isholey.help"));

        urlLabel = new JLabel(bagView.getPropertyMessage("baseURL.label"));
        urlLabel.setToolTipText(bagView.getPropertyMessage("baseURL.description"));
        urlField = new JTextField("");
        try {
            assert bag != null;
            urlField.setText(bag.getFetch().getBaseURL());
        } catch (Exception e) {
            log.error("Failed to set url label", e);
        }
        urlField.setEnabled(false);

        // TODO: Add format label
        final JLabel serializeLabel;
        serializeLabel = new JLabel(getMessage("bag.label.ispackage"));
        serializeLabel.setToolTipText(getMessage("bag.serializetype.help"));

        // TODO: Add format selection panel
        noneButton = new JRadioButton(getMessage("bag.serializetype.none"));
        noneButton.setEnabled(true);
        final AbstractAction serializeListener = new SerializeBagHandler();
        noneButton.addActionListener(serializeListener);
        noneButton.setToolTipText(getMessage("bag.serializetype.none.help"));

        zipButton = new JRadioButton(getMessage("bag.serializetype.zip"));
        zipButton.setEnabled(true);
        zipButton.addActionListener(serializeListener);
        zipButton.setToolTipText(getMessage("bag.serializetype.zip.help"));

    /*
     * tarButton = new JRadioButton(getMessage("bag.serializetype.tar"));
     * tarButton.setEnabled(true);
     * tarButton.addActionListener(serializeListener);
     * tarButton.setToolTipText(getMessage("bag.serializetype.tar.help"));
     * 
     * tarGzButton = new JRadioButton(getMessage("bag.serializetype.targz"));
     * tarGzButton.setEnabled(true);
     * tarGzButton.addActionListener(serializeListener);
     * tarGzButton.setToolTipText(getMessage("bag.serializetype.targz.help"));
     * 
     * tarBz2Button = new JRadioButton(getMessage("bag.serializetype.tarbz2"));
     * tarBz2Button.setEnabled(true);
     * tarBz2Button.addActionListener(serializeListener);
     * tarBz2Button.setToolTipText(getMessage("bag.serializetype.tarbz2.help"));
     */

        short mode = 2;
        if (bag != null) {
            mode = bag.getSerialMode();
        }
        if (mode == DefaultBag.NO_MODE) {
            this.noneButton.setEnabled(true);
        } else if (mode == DefaultBag.ZIP_MODE) {
            this.zipButton.setEnabled(true);
        } else {
            this.noneButton.setEnabled(true);
        }

        final ButtonGroup serializeGroup = new ButtonGroup();
        serializeGroup.add(noneButton);
        serializeGroup.add(zipButton);
        // serializeGroup.add(tarButton);
        // serializeGroup.add(tarGzButton);
        // serializeGroup.add(tarBz2Button);
        final JPanel serializeGroupPanel = new JPanel(new FlowLayout());
        serializeGroupPanel.add(serializeLabel);
        serializeGroupPanel.add(noneButton);
        serializeGroupPanel.add(zipButton);
        // serializeGroupPanel.add(tarButton);
        // serializeGroupPanel.add(tarGzButton);
        // serializeGroupPanel.add(tarBz2Button);
        serializeGroupPanel.setBorder(border);
        serializeGroupPanel.setEnabled(true);
        serializeGroupPanel.setToolTipText(bagView.getPropertyMessage("bag.serializetype.help"));

        final JLabel tagLabel = new JLabel(getMessage("bag.label.istag"));
        tagLabel.setToolTipText(getMessage("bag.label.istag.help"));
        final JCheckBox isTagCheckbox = new JCheckBox();
        isTagCheckbox.setBorder(border);
        isTagCheckbox.addActionListener(new TagManifestHandler());
        isTagCheckbox.setToolTipText(getMessage("bag.checkbox.istag.help"));

        final JLabel tagAlgorithmLabel = new JLabel(getMessage("bag.label.tagalgorithm"));
        tagAlgorithmLabel.setToolTipText(getMessage("bag.label.tagalgorithm.help"));
        final ArrayList<String> listModel = new ArrayList<>();
        for (final Algorithm algorithm : Algorithm.values()) {
            listModel.add(algorithm.bagItAlgorithm);
        }
        final JComboBox<String> tagAlgorithmList = new JComboBox<>(listModel.toArray(new
                String[listModel.size()]));
        tagAlgorithmList.setName(getMessage("bag.tagalgorithmlist"));
        tagAlgorithmList.addActionListener(new TagAlgorithmListHandler());
        tagAlgorithmList.setToolTipText(getMessage("bag.tagalgorithmlist.help"));

        final JLabel payloadLabel = new JLabel(getMessage("bag.label.ispayload"));
        payloadLabel.setToolTipText(getMessage("bag.ispayload.help"));
        final JCheckBox isPayloadCheckbox = new JCheckBox();
        isPayloadCheckbox.setBorder(border);
        isPayloadCheckbox.addActionListener(new PayloadManifestHandler());
        isPayloadCheckbox.setToolTipText(getMessage("bag.ispayload.help"));

        final JLabel payAlgorithmLabel = new JLabel(bagView.getPropertyMessage("bag.label"
                + ".payalgorithm"));
        payAlgorithmLabel.setToolTipText(getMessage("bag.payalgorithm.help"));
        final JComboBox<String> payAlgorithmList =
                new JComboBox<String>(listModel.toArray(new String[listModel.size()]));
        payAlgorithmList.setName(getMessage("bag.payalgorithmlist"));
        payAlgorithmList.addActionListener(new PayAlgorithmListHandler());
        payAlgorithmList.setToolTipText(getMessage("bag.payalgorithmlist.help"));

        //only if bag is not null
        if (bag != null) {
            final String fileName = bag.getName();
            bagNameField = new JTextField(fileName);
            bagNameField.setCaretPosition(fileName.length());

            holeyCheckbox.setSelected(bag.isHoley());
            urlLabel.setEnabled(bag.isHoley());
            isTagCheckbox.setSelected(bag.isBuildTagManifest());
            tagAlgorithmList.setSelectedItem(bag.getTagManifestAlgorithm());
            isPayloadCheckbox.setSelected(bag.isBuildPayloadManifest());
            payAlgorithmList.setSelectedItem(bag.getPayloadManifestAlgorithm());
        }

        final GridBagLayout layout = new GridBagLayout();
        final GridBagConstraints glbc = new GridBagConstraints();
        final JPanel panel = new JPanel(layout);
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        int row = 0;

        buildConstraints(glbc, 0, row, 1, 1, 1, 50, GridBagConstraints.NONE, GridBagConstraints
                .WEST);
        layout.setConstraints(location, glbc);
        panel.add(location);

        buildConstraints(glbc, 2, row, 1, 1, 1, 50, GridBagConstraints.NONE, GridBagConstraints
                .EAST);
        glbc.ipadx = 5;
        layout.setConstraints(browseButton, glbc);
        glbc.ipadx = 0;
        panel.add(browseButton);

        buildConstraints(glbc, 1, row, 1, 1, 80, 50, GridBagConstraints.HORIZONTAL,
                GridBagConstraints.WEST);
        glbc.ipadx = 5;
        layout.setConstraints(bagNameField, glbc);
        glbc.ipadx = 0;
        panel.add(bagNameField);

        row++;
        buildConstraints(glbc, 0, row, 1, 1, 1, 50, GridBagConstraints.NONE, GridBagConstraints
                .WEST);
        layout.setConstraints(holeyLabel, glbc);
        panel.add(holeyLabel);
        buildConstraints(glbc, 1, row, 1, 1, 80, 50, GridBagConstraints.WEST, GridBagConstraints
                .WEST);
        layout.setConstraints(holeyCheckbox, glbc);
        panel.add(holeyCheckbox);
        row++;
        buildConstraints(glbc, 0, row, 1, 1, 1, 50, GridBagConstraints.NONE, GridBagConstraints
                .WEST);
        layout.setConstraints(urlLabel, glbc);
        panel.add(urlLabel);
        buildConstraints(glbc, 1, row, 1, 1, 80, 50, GridBagConstraints.HORIZONTAL,
                GridBagConstraints.CENTER);
        layout.setConstraints(urlField, glbc);
        panel.add(urlField);
        row++;
        buildConstraints(glbc, 0, row, 1, 1, 1, 50, GridBagConstraints.NONE, GridBagConstraints
                .WEST);
        layout.setConstraints(serializeLabel, glbc);
        panel.add(serializeLabel);
        buildConstraints(glbc, 1, row, 2, 1, 80, 50, GridBagConstraints.HORIZONTAL,
                GridBagConstraints.WEST);
        layout.setConstraints(serializeGroupPanel, glbc);
        panel.add(serializeGroupPanel);
        row++;
        buildConstraints(glbc, 0, row, 1, 1, 1, 50, GridBagConstraints.NONE, GridBagConstraints
                .WEST);
        layout.setConstraints(tagLabel, glbc);
        panel.add(tagLabel);
        buildConstraints(glbc, 1, row, 2, 1, 80, 50, GridBagConstraints.HORIZONTAL,
                GridBagConstraints.CENTER);
        layout.setConstraints(isTagCheckbox, glbc);
        panel.add(isTagCheckbox);
        row++;
        buildConstraints(glbc, 0, row, 1, 1, 1, 50, GridBagConstraints.NONE, GridBagConstraints
                .WEST);
        layout.setConstraints(tagAlgorithmLabel, glbc);
        panel.add(tagAlgorithmLabel);
        buildConstraints(glbc, 1, row, 2, 1, 80, 50, GridBagConstraints.HORIZONTAL,
                GridBagConstraints.CENTER);
        layout.setConstraints(tagAlgorithmList, glbc);
        panel.add(tagAlgorithmList);
        row++;
        buildConstraints(glbc, 0, row, 1, 1, 1, 50, GridBagConstraints.NONE, GridBagConstraints
                .WEST);
        layout.setConstraints(payloadLabel, glbc);
        panel.add(payloadLabel);
        buildConstraints(glbc, 1, row, 2, 1, 80, 50, GridBagConstraints.HORIZONTAL,
                GridBagConstraints.CENTER);
        layout.setConstraints(isPayloadCheckbox, glbc);
        panel.add(isPayloadCheckbox);
        row++;
        buildConstraints(glbc, 0, row, 1, 1, 1, 50, GridBagConstraints.NONE, GridBagConstraints
                .WEST);
        layout.setConstraints(payAlgorithmLabel, glbc);
        panel.add(payAlgorithmLabel);
        buildConstraints(glbc, 1, row, 2, 1, 80, 50, GridBagConstraints.HORIZONTAL,
                GridBagConstraints.CENTER);
        layout.setConstraints(payAlgorithmList, glbc);
        panel.add(payAlgorithmList);
        row++;
        buildConstraints(glbc, 0, row, 1, 1, 1, 50, GridBagConstraints.NONE, GridBagConstraints
                .WEST);
        buildConstraints(glbc, 1, row, 2, 1, 80, 50, GridBagConstraints.HORIZONTAL,
                GridBagConstraints.CENTER);

        GuiStandardUtils.attachDialogBorder(contentPane);
        pageControl.add(panel);
        final JComponent buttonBar = createButtonBar();
        pageControl.add(buttonBar, BorderLayout.SOUTH);

        this.pack();
        return pageControl;

    }

    /**
     * setBag.
     *
     * @param bag DefaultBag
     */
    public void setBag(final DefaultBag bag) {
        bagNameField.setText(bag.getName());
        final short mode = bag.getSerialMode();
        if (mode == DefaultBag.NO_MODE) {
            noneButton.setEnabled(true);
            noneButton.setSelected(true);
            bagView.infoInputPane.serializeValue.setText(DefaultBag.NO_LABEL);
        } else if (mode == DefaultBag.ZIP_MODE) {
            zipButton.setEnabled(true);
            zipButton.setSelected(true);
            bagView.infoInputPane.serializeValue.setText(DefaultBag.ZIP_LABEL);
        } else {
            noneButton.setEnabled(true);
            noneButton.setSelected(true);
            bagView.infoInputPane.serializeValue.setText(DefaultBag.NO_LABEL);
        }
        savePanel.invalidate();
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        invalidate();
        repaint();
    }

    public class SerializeBagHandler extends AbstractAction {
        private static final long serialVersionUID = 1L;

        @Override
        public void actionPerformed(final ActionEvent e) {
            final JRadioButton cb = (JRadioButton) e.getSource();
            final boolean isSel = cb.isSelected();
            if (isSel) {
                if (cb == noneButton) {
                    bagView.getBag().isSerial(false);
                    bagView.getBag().setSerialMode(DefaultBag.NO_MODE);
                    bagView.infoInputPane.serializeValue.setText(DefaultBag.NO_LABEL);
                } else if (cb == zipButton) {
                    bagView.getBag().isSerial(true);
                    bagView.getBag().setSerialMode(DefaultBag.ZIP_MODE);
                    bagView.infoInputPane.serializeValue.setText(DefaultBag.ZIP_LABEL);
                } else {
                    bagView.getBag().isSerial(false);
                    bagView.getBag().setSerialMode(DefaultBag.NO_MODE);
                    bagView.infoInputPane.serializeValue.setText(DefaultBag.NO_LABEL);
                }
            }
        }
    }

    private class SaveBagAsHandler extends AbstractAction {
        private static final long serialVersionUID = 1L;

        @Override
        public void actionPerformed(final ActionEvent e) {
            final File selectFile = new File(File.separator + ".");
            final JFrame frame = new JFrame();
            final JFileChooser fs = new JFileChooser(selectFile);
            fs.setDialogType(JFileChooser.SAVE_DIALOG);
            fs.setFileSelectionMode(JFileChooser.FILES_ONLY);
            // fs.addChoosableFileFilter(bagView.infoInputPane.tarFilter);
            fs.setDialogTitle("Save Bag As");
            final DefaultBag bag = bagView.getBag();
            fs.setCurrentDirectory(bag.getRootDir());
            if (bag.getName() != null
                    && !bag.getName().equalsIgnoreCase(bagView.getPropertyMessage("bag.label" + ""
                            + ".noname"))) {
                String selectedName = bag.getName();
                if (bag.getSerialMode() == DefaultBag.ZIP_MODE) {
                    selectedName += "." + DefaultBag.ZIP_LABEL;
                }
                fs.setSelectedFile(new File(selectedName));
            }
            final int option = fs.showSaveDialog(frame);

            if (option == JFileChooser.APPROVE_OPTION) {
                bagFile = fs.getSelectedFile();
                bagFileName = bagFile.getAbsolutePath();
                final String name = bagFileName; // bagFile.getName();
                bagView.infoInputPane.setBagName(name);
                // bagView.infoInputPane.bagNameField.setCaretPosition(name.length());
                // bagView.infoInputPane.bagNameField.setEnabled(true);
                bagNameField.setText(bagFileName);
                bagNameField.setCaretPosition(bagFileName.length());
                bagNameField.invalidate();
            }
        }
    }

    private class OkSaveBagHandler extends AbstractAction {
        private static final long serialVersionUID = 1L;

        @Override
        public void actionPerformed(final ActionEvent e) {
            if (bagNameField.getText().trim().isEmpty()
                   || bagNameField.getText().equalsIgnoreCase(bagView.getPropertyMessage("bag.label"
                            + ".noname"))) {
                BagView.showWarningErrorDialog("Error - bag not saved", "The bag must have a file"
                        + " name.");
                return;
            }
            if (bagView.getBag().isHoley()) {
                if (urlField.getText().trim().isEmpty()) {
                    BagView.showWarningErrorDialog("Error - bag not saved", "A holey bag must "
                            + "have a URL value.");
                    return;
                }
                bagView.getBag().getFetch().setBaseURL(urlField.getText().trim());
                bagView.infoInputPane.holeyValue.setText("true");
            } else {
                bagView.infoInputPane.holeyValue.setText("false");
            }
            // bagView.saveBagHandler.setValidateOnSave(bagView.getBag().isValidateOnSave());
            setVisible(false);
            bagView.getBag().setName(bagFileName);
            bagView.saveBagHandler.save(bagFile);
        }
    }

    private class CancelSaveBagHandler extends AbstractAction {
        private static final long serialVersionUID = 1L;

        @Override
        public void actionPerformed(final ActionEvent e) {
            setVisible(false);
        }
    }

    private class TagManifestHandler extends AbstractAction {
        private static final long serialVersionUID = 75893358194076314L;

        @Override
        public void actionPerformed(final ActionEvent e) {
            final JCheckBox cb = (JCheckBox) e.getSource();

            // Determine status
            final boolean isSelected = cb.isSelected();
            if (isSelected) {
                bagView.getBag().isBuildTagManifest(true);
            } else {
                bagView.getBag().isBuildTagManifest(false);
            }
        }
    }

    private class TagAlgorithmListHandler extends AbstractAction {
        private static final long serialVersionUID = 75893358194076314L;

        @Override
        public void actionPerformed(final ActionEvent e) {
            final JComboBox<?> jlist = (JComboBox<?>) e.getSource();
            final String alg = (String) jlist.getSelectedItem();
            bagView.getBag().setTagManifestAlgorithm(alg);
        }
    }

    private class PayloadManifestHandler extends AbstractAction {
        private static final long serialVersionUID = 75893358194076314L;

        @Override
        public void actionPerformed(final ActionEvent e) {
            final JCheckBox cb = (JCheckBox) e.getSource();

            // Determine status
            final boolean isSelected = cb.isSelected();
            if (isSelected) {
                bagView.getBag().isBuildPayloadManifest(true);
            } else {
                bagView.getBag().isBuildPayloadManifest(false);
            }
        }
    }

    private class PayAlgorithmListHandler extends AbstractAction {
        private static final long serialVersionUID = 75893358194076314L;

        @Override
        public void actionPerformed(final ActionEvent e) {
            final JComboBox<?> jlist = (JComboBox<?>) e.getSource();
            final String alg = (String) jlist.getSelectedItem();
            bagView.getBag().setPayloadManifestAlgorithm(alg);
        }
    }

    private class HoleyBagHandler extends AbstractAction {
        private static final long serialVersionUID = 1L;

        @Override
        public void actionPerformed(final ActionEvent e) {
            final JCheckBox cb = (JCheckBox) e.getSource();

            // Determine status
            final boolean isSelected = cb.isSelected();
            if (isSelected) {
                bagView.getBag().isHoley(true);
                bagView.infoInputPane.serializeValue.setText("true");
                urlLabel.setEnabled(true);
                urlField.setEnabled(true);
                urlField.requestFocus();
            } else {
                bagView.getBag().isHoley(false);
                bagView.infoInputPane.serializeValue.setText("false");
                urlLabel.setEnabled(false);
                urlField.setEnabled(false);
            }
        }
    }

    private static void buildConstraints(final GridBagConstraints gbc, final int x, final int y,
                                         final int w,
                                         final int h, final int wx, final int wy, final int fill,
                                         final int anchor) {
        gbc.gridx = x; // start cell in a row
        gbc.gridy = y; // start cell in a column
        gbc.gridwidth = w; // how many column does the control occupy in the row
        gbc.gridheight = h; // how many column does the control occupy in the column
        gbc.weightx = wx; // relative horizontal size
        gbc.weighty = wy; // relative vertical size
        gbc.fill = fill; // the way how the control fills cells
        gbc.anchor = anchor; // alignment
    }

    private String getMessage(final String property) {
        return bagView.getPropertyMessage(property);
    }
}