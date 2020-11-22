package adda.application.controls;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.text.BreakIterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public class ActionTableCellRenderer<T> implements TableCellRenderer {
    //        textArea.setBackground(UIManager.getColor("Label.background"));
//        textArea.setFont(UIManager.getFont("Label.font"));
//        textArea.setBorder(UIManager.getBorder("Label.border"));
//    protected static final Font FONT = new Font(Font.SANS_SERIF, Font.PLAIN, 14);

    protected static final Font FONT = UIManager.getFont("Label.font").deriveFont(15);
    protected static final FontRenderContext FONT_RENDER_CONTEXT = new FontRenderContext(FONT.getTransform(), false, false);
    public static final String KEY_FORMAT = "%d%d";
    Function<T, String> action;

    public ActionTableCellRenderer() {
        this(val -> val.toString());
    }

    public ActionTableCellRenderer(Function<T, String> action) {
        this.action = action;





    }

    private static final long serialVersionUID = 1L;

    //private final Map<String, CellArea> cache = new ConcurrentHashMap<>();
    private final Map<String, JTextArea> cache = new ConcurrentHashMap<>();

    private static final int CACHE_ARRAY_CAPACITY = 100;
    private static final int CACHE_ARRAY_MAX_COLUMN = 2;
    private static final JTextArea[][] CACHE_ARRAY = new JTextArea[CACHE_ARRAY_CAPACITY][CACHE_ARRAY_MAX_COLUMN];


    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {

        if (row < CACHE_ARRAY_CAPACITY && col < CACHE_ARRAY_MAX_COLUMN && CACHE_ARRAY[row][col] != null) {

            int maxHeight = 0;

            for (int i = 0; i < CACHE_ARRAY_MAX_COLUMN; i++) {
                if (CACHE_ARRAY[row][i] != null) {
                    maxHeight = Math.max(maxHeight, CACHE_ARRAY[row][i].getMaximumSize().height);
                } else {
                    maxHeight = -1;
                    break;
                }
            }

            if (maxHeight > 0) {
                final JTextArea textArea = CACHE_ARRAY[row][col];
                if (textArea.getText().equals(action.apply((T) value))
                        && textArea.getMaximumSize().width == table.getColumnModel().getColumn(col).getWidth()
                        && maxHeight == table.getRowHeight(row)) {
                    return textArea;
                }
            }
        }

        JTextArea textArea = new JTextArea();
        textArea.setWrapStyleWord(true);
        textArea.setLineWrap(true);
        textArea.setAlignmentX(Component.CENTER_ALIGNMENT);
        textArea.setOpaque(false);
        textArea.setEditable(false);
        textArea.setFocusable(true);
        //textArea.setFont(FONT);
        textArea.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
        textArea.setAlignmentY(Component.CENTER_ALIGNMENT);

        textArea.setText(action.apply((T) value));
        textArea.setMaximumSize(new Dimension(table.getColumnModel().getColumn(col).getWidth(), Integer.MAX_VALUE));
        textArea.setRows(countLines(textArea));
        textArea.setMaximumSize(new Dimension(table.getColumnModel().getColumn(col).getWidth(), textArea.getPreferredSize().height));
        table.setRowHeight(row, textArea.getPreferredSize().height);

        if (row < CACHE_ARRAY_CAPACITY && col < CACHE_ARRAY_MAX_COLUMN) {
            CACHE_ARRAY[row][col] = textArea;
        }

        return textArea;



        //
//        CellArea area = new CellArea(action.apply((T) value), table, row, col, isSelected);
//        return area;
//
        //String key = String.format(KEY_FORMAT, row, col);


//        if (cache.containsKey(key)) {
//            final CellArea cellArea = cache.get(key);
//            if (cellArea.getText().equals(action.apply((T) value))
//                    && cellArea.getWidth() == table.getColumnModel().getColumn(col).getWidth()
//                    && cellArea.getHeight() == table.getRowHeight(row)) {
//                return cellArea;
//            }
//        }
//
//        cache.put(key, new CellArea(action.apply((T) value), table, row, col, isSelected));
//        return cache.get(key);

    }

    private static int countLines(JTextArea textArea)
    {
        AttributedString text = new AttributedString(textArea.getText());
        text.addAttribute(TextAttribute.FONT, textArea.getFont());
        FontRenderContext frc = textArea.getFontMetrics(textArea.getFont()).getFontRenderContext();
        AttributedCharacterIterator charIt = text.getIterator();
        LineBreakMeasurer lineMeasurer = new LineBreakMeasurer(charIt, frc);
        Insets textAreaInsets = textArea.getInsets();
        float formatWidth = textArea.getMaximumSize().width - textAreaInsets.left - textAreaInsets.right;
        lineMeasurer.setPosition(charIt.getBeginIndex());

        int linesCount = 0;
        double drawPosY = 0;
        while (lineMeasurer.getPosition() < charIt.getEndIndex())
        {
            TextLayout layout = lineMeasurer.nextLayout(formatWidth);
//            drawPosY += layout.getAscent();
//            // Draw the TextLayout at (drawPosX, drawPosY).
//layout.draw();
//            // Move y-coordinate in preparation for next layout.
//            drawPosY += layout.getDescent() + layout.getLeading();
            linesCount++;
        }

        return linesCount;
    }

    class CellArea extends DefaultTableCellRenderer {

        private static final long serialVersionUID = 1L;
        private String text;
        protected int rowIndex;
        protected int columnIndex;
        protected JTable table;
        protected int width;
        protected int height;
        private int paragraphStart,paragraphEnd;
        private LineBreakMeasurer lineMeasurer;

        public CellArea(String s, JTable tab, int row, int column,boolean isSelected) {
            text = s;
            rowIndex = row;
            columnIndex = column;
            table = tab;
            width = table.getColumnModel().getColumn(columnIndex).getWidth();
            height = table.getRowHeight(rowIndex);
            if (isSelected) {
                setForeground(table.getSelectionForeground());
                setBackground(table.getSelectionBackground());
            }
        }

        @Override
        public int getWidth() {
            return width;
        }

        @Override
        public int getHeight() {
            return height;
        }

        @Override
        public String getText() {
            return text;
        }

        public void paintComponent(Graphics gr) {
            super.paintComponent(gr);
            if ( text != null && !text.isEmpty() ) {
                Graphics2D g = (Graphics2D) gr;
                if (lineMeasurer == null) {
                    final AttributedString attributedString = new AttributedString(text);
                    attributedString.addAttribute(TextAttribute.FONT, FONT);
                    AttributedCharacterIterator paragraph = attributedString.getIterator();
                    paragraphStart = paragraph.getBeginIndex();
                    paragraphEnd = paragraph.getEndIndex();
                    FontRenderContext frc = g.getFontRenderContext();
//                    FontRenderContext frc = new FontRenderContext(FONT.getTransform(), true, true);
                    lineMeasurer = new LineBreakMeasurer(paragraph, BreakIterator.getWordInstance(), frc);
                }
                float breakWidth = (float)table.getColumnModel().getColumn(columnIndex).getWidth() - 4;
                float drawPosY = 2;
                // Set position to the index of the first character in the paragraph.
                lineMeasurer.setPosition(paragraphStart);
                // Get lines until the entire paragraph has been displayed.
                while (lineMeasurer.getPosition() < paragraphEnd) {
                    // Retrieve next layout. A cleverer program would also cache
                    // these layouts until the component is re-sized.
                    TextLayout layout = lineMeasurer.nextLayout(breakWidth);
                    // Compute pen x position. If the paragraph is right-to-left we
                    // will align the TextLayouts to the right edge of the panel.
                    // Note: this won't occur for the English text in this sample.
                    // Note: drawPosX is always where the LEFT of the text is placed.
                    float drawPosX = layout.isLeftToRight()
                            ? 2 : breakWidth - layout.getAdvance();
                    // Move y-coordinate by the ascent of the layout.
                    drawPosY += layout.getAscent();
                    // Draw the TextLayout at (drawPosX, drawPosY).
                    layout.draw(g, drawPosX, drawPosY);
                    // Move y-coordinate in preparation for next layout.
                    drawPosY += layout.getDescent() + layout.getLeading();
                }
//                if (table.getRowHeight(rowIndex) < (int) drawPosY + 4) {
                    table.setRowHeight(rowIndex,(int) drawPosY + 4);
//                }


            }
        }
    }

}

