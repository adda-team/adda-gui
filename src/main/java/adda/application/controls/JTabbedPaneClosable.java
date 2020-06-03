package adda.application.controls;

import javax.swing.*;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class JTabbedPaneClosable <T> extends JTabbedPane {
    protected List<ICloseTabListener> listeners = new ArrayList<>();
    protected List<T> associatedObjects = new ArrayList<>();


    public void addCloseTabListener(ICloseTabListener listener) {
        listeners.add(listener);
    }

    public void removeCloseTabListener(ICloseTabListener listener) {
        listeners.remove(listener);
    }

    protected void notifyCloseTabListeners(T associatedObject, int index) {
        for (ICloseTabListener listener : listeners)
            listener.tabClosed( new CloseTabEvent(this, associatedObject, index));
    }

    protected void closeTab(int index) {
        //exactly this sequence! because of change event
        T associatedObject = associatedObjects.get(index);
        associatedObjects.remove(index);
        remove(index);
        notifyCloseTabListeners(associatedObject, index);
    }


//
//    public Component add(Component component) {
//        associatedObjects.add(null);
//        return super.add(component);
//    }
//
//    public Component add(String title, Component component) {
//        associatedObjects.add(null);
//        return super.add(title, component);
//    }

    public void  addClosable(String title, Component component, T associatedObject) {
        //exactly this sequence! because of change event
        associatedObjects.add(associatedObject);
        add(title, component);
        setSelectedComponent(component);
        setTabComponentAt(associatedObjects.size() - 1, new ButtonTabComponent(this, title));
    }

    public T getSelectedTabAssociatedObject() {
        if (getSelectedIndex() < 0
                || associatedObjects.size() < 1
                || associatedObjects.size() - 1 < getSelectedIndex()) {
            return null;
        }
        return associatedObjects.get(getSelectedIndex());
    }


}

class ButtonTabComponent extends JPanel {
    private final JTabbedPaneClosable pane;

    public ButtonTabComponent(final JTabbedPaneClosable pane, String title) {
        //unset default FlowLayout' gaps
        super(new FlowLayout(FlowLayout.LEFT, 0, 0));
        if (pane == null) {
            throw new NullPointerException("TabbedPane is null");
        }
        this.pane = pane;
        setOpaque(false);

        //make JLabel read titles from JTabbedPane
        JLabel label = new JLabel(title);

        add(label);
        //add more space between the label and the button
        label.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));
        //tab button
        JButton button = new TabButton(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int i = pane.indexOfTabComponent(ButtonTabComponent.this);
                if (i != -1) {
                    pane.closeTab(i);
                }
            }
        });
        add(button);
        //add more space to the top of the component
        setBorder(BorderFactory.createEmptyBorder(2, 0, 0, 0));
    }

    private class TabButton extends JButton {

        //private ActionListener closeActionListener;

        public TabButton(ActionListener closeActionListener) {
            int size = 17;
            setPreferredSize(new Dimension(size, size));
            setToolTipText("close this tab");
            //Make the button looks the same for all Laf's
            setUI(new BasicButtonUI());
            //Make it transparent
            setContentAreaFilled(false);
            //No need to be focusable
            setFocusable(false);
            setBorder(BorderFactory.createEtchedBorder());
            setBorderPainted(false);
            //Making nice rollover effect
            //we use the same listener for all buttons
            addMouseListener(buttonMouseListener);
            setRolloverEnabled(true);
            //Close the proper tab by clicking the button
            addActionListener(closeActionListener);
        }



        //we don't want to update UI for this button
        public void updateUI() {
        }

        //paint the cross
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            //shift the image for pressed buttons
            if (getModel().isPressed()) {
                g2.translate(1, 1);
            }
            g2.setStroke(new BasicStroke(2));
            g2.setColor(Color.BLACK);
            if (getModel().isRollover()) {
                g2.setColor(Color.MAGENTA);
            }
            int delta = 6;
            g2.drawLine(delta, delta, getWidth() - delta - 1, getHeight() - delta - 1);
            g2.drawLine(getWidth() - delta - 1, delta, delta, getHeight() - delta - 1);
            g2.dispose();
        }
    }

    private final static MouseListener buttonMouseListener = new MouseAdapter() {
        public void mouseEntered(MouseEvent e) {
            Component component = e.getComponent();
            if (component instanceof AbstractButton) {
                AbstractButton button = (AbstractButton) component;
                button.setBorderPainted(true);
            }
        }

        public void mouseExited(MouseEvent e) {
            Component component = e.getComponent();
            if (component instanceof AbstractButton) {
                AbstractButton button = (AbstractButton) component;
                button.setBorderPainted(false);
            }
        }
    };
}

