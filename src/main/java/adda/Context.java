package adda;

import adda.application.MainForm;
import adda.settings.formatters.json.JsonFormatter;
import adda.settings.formatters.plaintext.PlainTextFormatter;
import adda.settings.serializer.AddaSerializer;
import adda.settings.serializer.ISerializer;

import javax.swing.*;

public class Context {

    protected JFrame mainFrame;

    protected MainForm mainForm;

    public MainForm getMainForm() {
        return mainForm;
    }

    public JFrame getMainFrame() {
        return mainFrame;
    }

    public ISerializer getXmlSerializer() {
        return xmlSerializer;
    }

    public ISerializer getJsonSerializer() {
        return jsonSerializer;
    }

    public ISerializer getTextSerializer() {
        return textSerializer;
    }

    ISerializer xmlSerializer = new AddaSerializer();
    ISerializer jsonSerializer = new AddaSerializer(new JsonFormatter());
    ISerializer textSerializer = new AddaSerializer(new PlainTextFormatter());



    private static volatile Context instance;

    public static Context getInstance() {
        Context localInstance = instance;
        if (localInstance == null) {
            synchronized (Context.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new Context();
                }
            }
        }
        return localInstance;
    }


}
