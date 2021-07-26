package adda.item.root.numberedText;

import adda.Context;
import adda.base.events.IModelPropertyChangeEvent;
import adda.base.models.IModel;
import adda.base.models.IModelObserver;
import adda.base.models.ModelBase;
import adda.item.root.projectTree.ProjectTreeModel;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import java.io.File;
import java.nio.file.*;
import java.nio.file.WatchEvent.Kind;

import static java.nio.file.StandardWatchEventKinds.*;

public class NumberedTextModel extends ModelBase {

    public static final String DISPLAY_NAME_FIELD_NAME = "displayName";
    public static final String DESCRIPTION_FIELD_NAME = "description";
    public static final String TEXT_FIELD_NAME = "text";
    public static final String REFRESH_FIELD_NAME = "REFRESH";
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

    Thread worker;
    Thread watcher;

    String path;

    public void bindWithFile(String path) {

        this.path = path;
        javax.swing.SwingUtilities.invokeLater(() ->
                Context.getInstance().getProjectTreeModel().addObserver(new IModelObserver() {
                    @Override
                    public void modelPropertyChanged(IModel sender, IModelPropertyChangeEvent event) {
                        if (ProjectTreeModel.FILE_MODIFIED_FIELD_NAME.equals(event.getPropertyName()) && event.getPropertyValue() != null) {
                            String generalizedPath = path.replace("\\", "/");
                            String generalizedEventPath = event.getPropertyValue().toString().replace("\\", "/");
                            if (generalizedPath.startsWith(generalizedEventPath)) {
                                loadFromFileAsync(path);
                            }
                        }
                    }
                })
        );

//        final FileWatcher fileWatcher = new FileWatcher(path) {
//            @Override
//            public void onModified() {
//                javax.swing.SwingUtilities.invokeLater(() -> loadFromFileAsync(path));
//            }
//        };
//        watcher = new Thread(() -> {
//            try {
//                fileWatcher.watchFile();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        });
//        watcher.start();



        loadFromFileAsync(this.path);
    }

    public void loadFromFileAsync(String path) {
        if (worker != null && worker.isAlive()) {
            worker.interrupt();
        }

        worker = new Thread(() -> {
            try (BufferedReader in = new BufferedReader(new FileReader(path))) {
                String str;
                int bulkVolume = 1000;
                int counter = 0;
                StringBuilder part = new StringBuilder();
                StringBuilder full = new StringBuilder();
                text = "";
                javax.swing.SwingUtilities.invokeLater(() -> notifyObservers(REFRESH_FIELD_NAME, text));
                while ((str = in.readLine()) != null) {
                    if(Thread.interrupted()){
                        return;
                    }
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
            }
            catch (IOException ignored) {}
        });
        worker.start();
    }

//    public abstract class FileWatcher
//    {
//        private Path folderPath;
//        private String watchFile;
//
//        public FileWatcher(String watchFile)
//        {
//            Path filePath = Paths.get(watchFile);
//
//            boolean isRegularFile = Files.isRegularFile(filePath);
//
//            if (!isRegularFile)
//            {
//                // Do not allow this to be a folder since we want to watch files
//                throw new IllegalArgumentException(watchFile + " is not a regular file");
//            }
//
//            // This is always a folder
//            folderPath = filePath.getParent();
//
//            // Keep this relative to the watched folder
//            this.watchFile = watchFile.replace(folderPath.toString() + File.separator, "");
//        }
//
//        public void watchFile() throws Exception
//        {
//            // We obtain the file system of the Path
//            FileSystem fileSystem = folderPath.getFileSystem();
//
//            // We create the new WatchService using the try-with-resources block
//            try (WatchService service = fileSystem.newWatchService())
//            {
//                // We watch for modification events
//                folderPath.register(service, ENTRY_MODIFY);
//
//                // Start the infinite polling loop
//                while (true)
//                {
//                    // Wait for the next event
//                    WatchKey watchKey = service.take();
//
//                    for (WatchEvent<?> watchEvent : watchKey.pollEvents())
//                    {
//                        // Get the type of the event
//                        Kind<?> kind = watchEvent.kind();
//
//                        if (kind == ENTRY_MODIFY)
//                        {
//                            Path watchEventPath = (Path) watchEvent.context();
//
//                            // Call this if the right file is involved
//                            if (watchEventPath.toString().equals(watchFile))
//                            {
//                                onModified();
//                            }
//                        }
//                    }
//
//                    if (!watchKey.reset())
//                    {
//                        // Exit if no longer valid
//                        break;
//                    }
//                }
//            }
//        }
//
//        public abstract void onModified();
//    }
}