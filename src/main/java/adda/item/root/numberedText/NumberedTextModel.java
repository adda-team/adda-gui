package adda.item.root.numberedText;

import adda.base.models.ModelBase;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class NumberedTextModel extends ModelBase {

    public static final String DISPLAY_NAME_FIELD_NAME = "displayName";
    public static final String DESCRIPTION_FIELD_NAME = "description";
    public static final String TEXT_FIELD_NAME = "text";
    public static final String APPEND_FIELD_NAME = "append";

    public String displayName;
    public String description;
    public String text = "";

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        if ((this.displayName != null && !this.displayName.equals(displayName)) || (this.displayName == null && displayName != null)) {
            this.displayName = displayName;
            notifyObservers(DISPLAY_NAME_FIELD_NAME, displayName);
        }
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        if ((this.description != null && !this.description.equals(description)) || (this.description == null && description != null)) {
            this.description = description;
            notifyObservers(DESCRIPTION_FIELD_NAME, description);
        }
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        if ((this.text != null && !this.text.equals(text)) || (this.text == null && text != null)) {
            this.text = text;
            notifyObservers(TEXT_FIELD_NAME, text);
        }
    }

    public void loadFromFileAsync(String path) {
        Thread t = new Thread(() -> {
            try (BufferedReader in = new BufferedReader(new FileReader(path))) {
                String str;
                int bulkVolume = 1000;
                int counter = 0;
                StringBuilder part = new StringBuilder();
                StringBuilder full = new StringBuilder();
                while ((str = in.readLine()) != null) {
                    //jtextArea.append(str);
                    part.append(str);
                    part.append("\n");
                    counter++;
                    if (counter >= bulkVolume) {
                        full.append(part);
                        text = full.toString();
                        final String s = part.toString();
                        javax.swing.SwingUtilities.invokeLater(() -> notifyObservers(APPEND_FIELD_NAME, s));
                        part = new StringBuilder();
                        counter = 0;
                    }
                }

                full.append(part);
                text = full.toString();
                final String s = part.toString();
                javax.swing.SwingUtilities.invokeLater(() -> notifyObservers(APPEND_FIELD_NAME, s));
            } catch (IOException ignored) {}
        });
        t.start();
    }
}