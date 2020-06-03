package adda.settings.formatters.plaintext;

import adda.settings.formatters.IFormatter;
import adda.settings.formatters.IFormatterItem;

import java.io.*;
import java.util.Collections;
import java.util.List;

public class PlainTextFormatter implements IFormatter {
    private final String TAB = "\t";
    private IFormatterItem rootItem = new PlainTextFormatterItem("", "");


    @Override
    public IFormatterItem createFormatterItem(String name, String value) {
        return new PlainTextFormatterItem(name, value);
    }

    @Override
    public IFormatterItem createFormatterItem(String name) {
        return new PlainTextFormatterItem(name, null);
    }

    @Override
    public IFormatterItem getRootFormatterItem() {
        return rootItem;
    }

    @Override
    public void parse(String str) {
        rootItem = new PlainTextFormatterItem("", "");
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(str.getBytes("UTF-8"))));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        try {
            parseInner(rootItem, reader, 0);
        } catch (IOException exc) {
            //TODO EXCEPTION PROCESSING
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private void parseInner(IFormatterItem item, BufferedReader reader, int level) throws IOException {
        String line = null;
        IFormatterItem actualItem = item;
        IFormatterItem lastItem = null;
        while ((line = reader.readLine()) != null) {
            int currentLevel = getLevel(line);
            String[] sp = line.split(" : ");
            IFormatterItem currentItem = new PlainTextFormatterItem(sp[0].trim(), sp.length > 1 ? sp[1] : null);
            if (currentLevel == level) {
                actualItem.appendChild(currentItem);
            }
            if (currentLevel < level) {
                actualItem = actualItem.getParent();
                actualItem.appendChild(currentItem);
            }
            if (currentLevel > level) {
                actualItem = lastItem;
                actualItem.appendChild(currentItem);

            }
            level = currentLevel;
            lastItem = currentItem;

        }
    }

    private int getLevel(String str) {
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) != '\t') {
                return i;
            }
        }
        return 0;
    }

    @Override
    public void init() {
        rootItem = new PlainTextFormatterItem("", "");
    }

    @Override
    public String getFormattedText() {
        StringBuilder builder = new StringBuilder();
        processFormattedTextInner(rootItem.getChildren(), builder, 0);
        return builder.toString();
    }

    private void processFormattedTextInner(List<IFormatterItem> items, StringBuilder builder, int level) {
        for (IFormatterItem item : items) {
            builder.append(String.join("", Collections.nCopies(Math.max(0, level), TAB)));
            builder.append(item.getName());
            builder.append(" : ");
            builder.append(item.getValue() != null ? item.getValue() : "");
            builder.append("\n");
            processFormattedTextInner(item.getChildren(), builder, level + 1);
        }
    }
}
