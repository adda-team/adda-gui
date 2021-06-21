package adda.item.root.numberedText;

import adda.base.events.IModelPropertyChangeEvent;
import adda.base.models.IModel;
import adda.base.views.ViewBase;
import adda.item.root.lineChart.LineChartModel;

import javax.swing.*;
import java.awt.*;
import java.beans.*;
import java.util.HashMap;
import javax.swing.event.*;
import javax.swing.text.*;

public class NumberedTextView extends ViewBase {

    JLabel displayNameLabel;
    JTextPane descriptionTextPane;
    JTextPane textPane;
    JScrollPane scroll;

    protected void initPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        this.panel = panel;
    }

    public void initFromModel(IModel model) {
        initPanel();

        NumberedTextModel numberedTextModel = (NumberedTextModel) model;
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());

        displayNameLabel = new JLabel();
        displayNameLabel.setFont(new Font(displayNameLabel.getName(), Font.BOLD, 20));
        displayNameLabel.setText(numberedTextModel.getDisplayName());
        displayNameLabel.setHorizontalAlignment(JLabel.CENTER);

        topPanel.add(displayNameLabel, BorderLayout.NORTH);

        descriptionTextPane = new JTextPane();

        descriptionTextPane.setText(numberedTextModel.getDescription());
        //descriptionTextArea.setWrapStyleWord(true);
        descriptionTextPane.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);

        //descriptionTextArea.setLineWrap(true);
        descriptionTextPane.setAlignmentX(Component.CENTER_ALIGNMENT);
        descriptionTextPane.setOpaque(false);
        descriptionTextPane.setEditable(false);
        descriptionTextPane.setFocusable(true);
        descriptionTextPane.setBackground(UIManager.getColor("Label.background"));
        descriptionTextPane.setFont(UIManager.getFont("Label.font"));
        descriptionTextPane.setBorder(UIManager.getBorder("Label.border"));
        descriptionTextPane.setAlignmentY(Component.CENTER_ALIGNMENT);
        descriptionTextPane.setBorder(BorderFactory.createEmptyBorder(0, 50, 0, 50));
        StyledDocument style = descriptionTextPane.getStyledDocument();
        SimpleAttributeSet align = new SimpleAttributeSet();
        StyleConstants.setAlignment(align, StyleConstants.ALIGN_CENTER);
        style.setParagraphAttributes(0, style.getLength(), align, false);

        topPanel.add(descriptionTextPane);
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 10));

        panel.add(topPanel, BorderLayout.NORTH);

        JPanel textViewerPanel = new JPanel(new BorderLayout());
        textPane = new JTextPane();
        //textPane.setOpaque(false);
        textPane.setEditable(false);
        textPane.setBackground(Color.white);
        textPane.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));
        JTextLineNumber textLineNumber = new JTextLineNumber(textPane);
        textViewerPanel.add(textLineNumber, BorderLayout.WEST);
        textViewerPanel.add(textPane, BorderLayout.CENTER);

        scroll = new JScrollPane(textViewerPanel,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.getHorizontalScrollBar().setUnitIncrement(16);
        panel.add(scroll);
    }

    public void modelPropertyChanged(IModel sender, IModelPropertyChangeEvent event) {
        NumberedTextModel numberedTextModel = (NumberedTextModel) sender;
        if (NumberedTextModel.DISPLAY_NAME_FIELD_NAME.equals(event.getPropertyName())) {
            displayNameLabel.setText(numberedTextModel.getDisplayName());
        }

        if (NumberedTextModel.DESCRIPTION_FIELD_NAME.equals(event.getPropertyName())) {
            descriptionTextPane.setText(numberedTextModel.getDescription());
        }

        if (NumberedTextModel.APPEND_FIELD_NAME.equals(event.getPropertyName())) {
            try {
                double percent = 1.0*scroll.getVerticalScrollBar().getValue()/scroll.getVerticalScrollBar().getMaximum();
                int position = textPane.getCaretPosition();
                textPane.getDocument().insertString(textPane.getDocument().getLength(), event.getPropertyValue().toString(), null);
                scroll.getVerticalScrollBar().setValue((int) Math.round(percent * scroll.getVerticalScrollBar().getMaximum()));
                textPane.setCaretPosition(position);
            } catch (BadLocationException ignored) {
            }

        }

    }


    /**
     * This class will display line numbers for a related text component. The text     component must use the same line height for each line. TextLineNumber supports wrapped lines and will highlight the line number of the current line in the text component.
     * <p>
     * This class was designed to be used as a component added to the row header of a     JScrollPane.
     */
    class JTextLineNumber extends JPanel implements CaretListener, DocumentListener, PropertyChangeListener {
        private static final long serialVersionUID = 1L;
        public final static float LEFT = 0.0f;
        public final static float CENTER = 0.5f;
        public final static float RIGHT = 1.0f;


// Text component this TextTextLineNumber component is in sync with

        private JTextPane component;

// Properties that can be changed

        private boolean updateFont;
        private int borderGap;
        private Color currentLineForeground;
        private float digitAlignment;
        private int minimumDisplayDigits;

// Keep history information to reduce the number of times the component
// needs to be repainted

        private int lastDigits;
        private int lastHeight;
        private int lastLine;

        private HashMap<String, FontMetrics> fonts;

        /**
         * Create a line number component for a text component. This minimum display width will be based on 3 digits.
         *
         * @param component the related text component
         */
        public JTextLineNumber(JTextPane component) {
            this(component, 3);
        }

        /**
         * Create a line number component for a text component.
         *
         * @param component            the related text component
         * @param minimumDisplayDigits the number of digits used to calculate the minimum width of the component
         */
        public JTextLineNumber(JTextPane component,
                               int minimumDisplayDigits) {
            this.component = component;
            setBorder(null);

            //setFont(component.getFont());

            setBorder(null);
            setBorderGap(5);
            setCurrentLineForeground(Color.BLACK);
            setDigitAlignment(RIGHT);
            setMinimumDisplayDigits(minimumDisplayDigits);

            component.getDocument().addDocumentListener(this);
            component.addCaretListener(this);
            component.addPropertyChangeListener("font", this);
        }

        /**
         * Gets the update font property
         *
         * @return the update font property
         */
        public boolean getUpdateFont() {
            return updateFont;
        }

        /**
         * Set the update font property. Indicates whether this Font should be updated automatically when the Font of the related text component is changed.
         *
         * @param updateFont when true update the Font and repaint the line numbers, otherwise just repaint the line numbers.
         */
        public void setUpdateFont(boolean updateFont) {
            this.updateFont = updateFont;
        }

        /**
         * Gets the border gap
         *
         * @return the border gap in pixels
         */
        public int getBorderGap() {
            return borderGap;
        }

        /**
         * The border gap is used in calculating the left and right insets of the border. Default value is 5.
         *
         * @param borderGap the gap in pixels
         */
        public void setBorderGap(int borderGap) {
            this.borderGap = borderGap;
            lastDigits = 0;
            setPreferredWidth();
        }

        /**
         * Gets the current line rendering Color
         *
         * @return the Color used to render the current line number
         */
        public Color getCurrentLineForeground() {
            return currentLineForeground == null ? getForeground() : currentLineForeground;
        }

        /**
         * The Color used to render the current line digits. Default is Coolor.RED.
         *
         * @param currentLineForeground the Color used to render the current line
         */
        public void setCurrentLineForeground(Color currentLineForeground) {
            this.currentLineForeground = currentLineForeground;
        }

        /**
         * Gets the digit alignment
         *
         * @return the alignment of the painted digits
         */
        public float getDigitAlignment() {
            return digitAlignment;
        }

        /**
         * Specify the horizontal alignment of the digits within the component. Common values would be:
         * <ul>
         * <li>TextLineNumber.LEFT
         * <li>TextLineNumber.CENTER
         * <li>TextLineNumber.RIGHT (default)
         * </ul>
         *
         * @param digitAlignment the Color used to render the current line
         */
        public void setDigitAlignment(float digitAlignment) {
            this.digitAlignment = digitAlignment > 1.0f ? 1.0f : digitAlignment < 0.0f ? -1.0f : digitAlignment;
        }

        /**
         * Gets the minimum display digits
         *
         * @return the minimum display digits
         */
        public int getMinimumDisplayDigits() {
            return minimumDisplayDigits;
        }

        /**
         * Specify the mimimum number of digits used to calculate the preferred width of the component. Default is 3.
         *
         * @param minimumDisplayDigits the number digits used in the preferred width calculation
         */
        public void setMinimumDisplayDigits(int minimumDisplayDigits) {
            this.minimumDisplayDigits = minimumDisplayDigits;
            setPreferredWidth();
        }

        /**
         * Calculate the width needed to display the maximum line number
         */
        private void setPreferredWidth() {
            // Define the font to display the numbers.
            Font componentFont = component.getFont();
            Font font = new Font(componentFont.getFamily(), Font.BOLD, componentFont.getSize());

            Element root = component.getDocument().getDefaultRootElement();
            int lines = root.getElementCount();
            int digits = Math.max(String.valueOf(lines).length(), minimumDisplayDigits);

            // Update sizes when number of digits in the line number changes

            if (lastDigits != digits) {
                lastDigits = digits;
                FontMetrics fontMetrics = getFontMetrics(font);
                int iPreferredWidth = 0;

                if (digits <= 1) {
                    iPreferredWidth = 10 + fontMetrics.stringWidth("0");
                } else if (digits == 2) {
                    iPreferredWidth = 10 + fontMetrics.stringWidth("00");
                } else if (digits == 3) {
                    iPreferredWidth = 10 + fontMetrics.stringWidth("000");
                } else if (digits == 4) {
                    iPreferredWidth = 10 + fontMetrics.stringWidth("0000");
                } else if (digits == 5) {
                    iPreferredWidth = 10 + fontMetrics.stringWidth("00000");
                } else if (digits == 6) {
                    iPreferredWidth = 10 + fontMetrics.stringWidth("000000");
                } else if (digits == 7) {
                    iPreferredWidth = 10 + fontMetrics.stringWidth("0000000");
                } else {
                    iPreferredWidth = 10 + fontMetrics.stringWidth("00000000");
                }

                Dimension dimension = new Dimension(iPreferredWidth, 0);
                setPreferredSize(dimension);
                setSize(dimension);
            }
        }

        /**
         * Draw the line numbers
         */
        @Override
        public void paintComponent(Graphics g) {
            // Paint the background.
            Graphics2D g2d = (Graphics2D) g;
            g2d.setColor(component.getBackground());
            g2d.fillRect(0, 0, getWidth(), getHeight());
            // Paint a vertical line at right.
            g2d.setStroke(new BasicStroke(1));
            g2d.setColor(new Color(0, 0, 0, 64));
            g2d.drawLine(getWidth() - 1, 0, getWidth() - 1, getHeight());
            // Define the font.
            Font componentFont = component.getFont();
            Font font = new Font(componentFont.getFamily(), Font.BOLD, componentFont.getSize());

            // Determine the width of the space available to draw the line number

            FontMetrics fontMetrics = component.getFontMetrics(component.getFont());
            int iRightAlignement = getSize().width - 5;

            // Determine the rows to draw within the clipped bounds.

            Rectangle clip = g.getClipBounds();
            int rowStartOffset = component.viewToModel(new Point(0, clip.y));
            int endOffset = component.viewToModel(new Point(0, clip.y + clip.height));
            g2d.setFont(font);

            while (rowStartOffset <= endOffset) {
                try {
                    if (isCurrentLine(rowStartOffset))
                        g2d.setColor(new Color(0, 0, 0, 160));
                    else
                        g2d.setColor(new Color(0, 0, 0, 100));

                    // Get the line number as a string and then determine the
                    // "X" and "Y" offsets for drawing the string.

                    String lineNumber = getTextLineNumber(rowStartOffset);
                    int stringWidth = ((Graphics2D) g).getFontMetrics().stringWidth(lineNumber);
                    int iX = iRightAlignement - stringWidth;
                    int iY = getOffsetY(rowStartOffset, fontMetrics);
                    g2d.drawString(lineNumber, iX, iY);

                    // Move to the next row

                    rowStartOffset = Utilities.getRowEnd(component, rowStartOffset) + 1;
                } catch (Exception e) {
                    break;
                }
            }
        }

        /*
         * We need to know if the caret is currently positioned on the line we are about to paint so the line number can be highlighted.
         */
        private boolean isCurrentLine(int rowStartOffset) {
            int caretPosition = component.getCaretPosition();
            Element root = component.getDocument().getDefaultRootElement();

            if (root.getElementIndex(rowStartOffset) == root.getElementIndex(caretPosition))
                return true;
            else
                return false;
        }

        /*
         * Get the line number to be drawn. The empty string will be returned when a line of text has wrapped.
         */
        protected String getTextLineNumber(int rowStartOffset) {
            Element root = component.getDocument().getDefaultRootElement();
            int index = root.getElementIndex(rowStartOffset);
            Element line = root.getElement(index);

            if (line.getStartOffset() == rowStartOffset)
                return String.valueOf(index + 1);
            else
                return "";
        }

        /*
         * Determine the Y offset for the current row
         */
        private int getOffsetY(int rowStartOffset,
                               FontMetrics fontMetrics)
                throws BadLocationException {
            // Get the bounding rectangle of the row

            Rectangle r = component.modelToView(rowStartOffset);
            int lineHeight = fontMetrics.getHeight();
            int y = r.y + r.height;
            int descent = 0;

            // The text needs to be positioned above the bottom of the bounding
            // rectangle based on the descent of the font(s) contained on the row.

            if (r.height == lineHeight) // default font is being used
            {
                descent = fontMetrics.getDescent();
            } else // We need to check all the attributes for font changes
            {
                if (fonts == null)
                    fonts = new HashMap<String, FontMetrics>();

                Element root = component.getDocument().getDefaultRootElement();
                int index = root.getElementIndex(rowStartOffset);
                Element line = root.getElement(index);

                for (int i = 0; i < line.getElementCount(); i++) {
                    Element child = line.getElement(i);
                    AttributeSet as = child.getAttributes();
                    String fontFamily = (String) as.getAttribute(StyleConstants.FontFamily);
                    Integer fontSize = (Integer) as.getAttribute(StyleConstants.FontSize);
                    String key = fontFamily + fontSize;

                    FontMetrics fm = fonts.get(key);

                    if (fm == null) {
                        Font font = new Font(fontFamily, Font.PLAIN, fontSize);
                        fm = component.getFontMetrics(font);
                        fonts.put(key, fm);
                    }

                    descent = Math.max(descent, fm.getDescent());
                }
            }

            return y - descent;
        }

        //
// Implement CaretListener interface
//
        @Override
        public void caretUpdate(CaretEvent e) {
            // Get the line the caret is positioned on

            int caretPosition = component.getCaretPosition();
            Element root = component.getDocument().getDefaultRootElement();
            int currentLine = root.getElementIndex(caretPosition);

            // Need to repaint so the correct line number can be highlighted

            if (lastLine != currentLine) {
                repaint();
                lastLine = currentLine;
            }
        }

        //
// Implement DocumentListener interface
//
        @Override
        public void changedUpdate(DocumentEvent e) {
            documentChanged();
        }

        @Override
        public void insertUpdate(DocumentEvent e) {
            documentChanged();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            documentChanged();
        }

        /*
         * A document change may affect the number of displayed lines of text. Therefore the lines numbers will also change.
         */
        private void documentChanged() {
            // View of the component has not been updated at the time
            // the DocumentEvent is fired

            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    try {
                        int endPos = component.getDocument().getLength();
                        Rectangle rect = component.modelToView(endPos);

                        if (rect != null && rect.y != lastHeight) {
                            setPreferredWidth();
                            repaint();
                            lastHeight = rect.y;
                        }
                    } catch (BadLocationException ex) {
                        /* nothing to do */
                    }
                }
            });
        }

        //
// Implement PropertyChangeListener interface
//
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            if (evt.getNewValue() instanceof Font) {
                if (updateFont) {
                    Font newFont = (Font) evt.getNewValue();
                    setFont(newFont);
                    lastDigits = 0;
                    setPreferredWidth();
                } else {
                    repaint();
                }
            }
        }
    }
}
