package adda.utils;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class ListenerHelper {
    public static DocumentListener getSimpleDocumentListener(Runnable func) {
        return new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent documentEvent) {
                process();
            }

            @Override
            public void removeUpdate(DocumentEvent documentEvent) {
                process();
            }

            @Override
            public void changedUpdate(DocumentEvent documentEvent) {
                process();
            }

            private void process() {
                func.run();
            }
        };
    }

    public static FocusListener getSimpleFocusListener(Runnable func) {
        return new FocusListener() {
            @Override
            public void focusGained(FocusEvent focusEvent) {

            }

            @Override
            public void focusLost(FocusEvent focusEvent) {
                func.run();
            }
        };
    }
}
