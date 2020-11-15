package adda.application.controls;

import adda.Context;
import adda.base.models.IModel;
import adda.base.models.IModelObserver;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.*;

public abstract class CustomOkCancelModalDialog extends JDialog implements IModelObserver {
    protected JPanel wrapperPanel;
    protected JButton buttonOK;
    protected JButton buttonCancel;


    protected JPanel contentPanel;
    protected boolean isOkPressed = false;
    protected IModel model;

    public boolean isOkPressed() {
        return isOkPressed;
    }


    public CustomOkCancelModalDialog(IModel model, String title) {
        super(Context.getInstance().getMainFrame(), title, ModalityType.APPLICATION_MODAL);
        setLocationRelativeTo(Context.getInstance().getMainFrame());
        this.model = model;
        this.model.addObserver(this);
        setMinimumSize(new Dimension(200, 200));
        setContentPane(wrapperPanel);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        contentPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), title, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, new Color(-16777216)));

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        wrapperPanel.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        buttonOK.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

            }
        });



    }


    protected JPanel surroundWithPanel(Component component) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.add(component);
        return panel;
    }

    public JPanel getDialogContentPanel() {
        return contentPanel;
    }


    private void onOK() {
        isOkPressed = true;
        setVisible(false);
        this.model.removeObserver(this);
        dispose();
    }

    private void onCancel() {
        isOkPressed = false;
        setVisible(false);
        this.model.removeObserver(this);
        dispose();
    }

//    public void modelPropertyChanged(IModel sender, IModelPropertyChangeEvent event) {
//
//    }

    {
        wrapperPanel = new JPanel();
        wrapperPanel.setLayout(new GridBagLayout());
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new BorderLayout(0, 0));
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        wrapperPanel.add(panel1, gbc);
        panel1.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5), null));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridBagLayout());
        panel1.add(panel2, BorderLayout.EAST);
        buttonOK = new JButton();
        buttonOK.setText("OK");//todo localization
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel2.add(buttonOK, gbc);
        buttonCancel = new JButton();
        buttonCancel.setText("Cancel");//todo localization
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel2.add(buttonCancel, gbc);
        final JPanel spacer1 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel2.add(spacer1, gbc);
        contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout(0, 0));
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        wrapperPanel.add(contentPanel, gbc);
    }




}

