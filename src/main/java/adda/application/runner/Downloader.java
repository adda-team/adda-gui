package adda.application.runner;

import java.io.*;
import java.net.*;
import java.util.*;

// This class downloads a file from a URL.
public class Downloader extends Observable implements Runnable {

    // Max size of download buffer.
    private static final int MAX_BUFFER_SIZE = 1024;

    // These are the status names.
    public static final String STATUSES[] = {"Downloading",
            "Paused", "Complete", "Cancelled", "Error"};

    // These are the status codes.
    public static final int DOWNLOADING = 0;
    public static final int PAUSED = 1;
    public static final int COMPLETE = 2;
    public static final int CANCELLED = 3;
    public static final int ERROR = 4;

    private URL url; // download URL
    private int size; // size of download in bytes
    private int downloaded; // number of bytes downloaded
    private int status; // current status of download

    private String path;



    // Constructor for Download.
    public Downloader(URL url, String path) {
        this.url = url;
        this.path = path;
        size = -1;
        downloaded = 0;
        status = DOWNLOADING;

        // Begin the download.
        download();
    }

    // Get this download's URL.
    public String getUrl() {
        return url.toString();
    }

    public int getDownloaded() {
        return downloaded;
    }

    // Get this download's size.
    public int getSize() {
        return size;
    }

    // Get this download's progress.
    public float getProgress() {
        return ((float) downloaded / size) * 100;
    }

    // Get this download's status.
    public int getStatus() {
        return status;
    }

    // Pause this download.
    public void pause() {
        status = PAUSED;
        stateChanged();
    }

    // Resume this download.
    public void resume() {
        status = DOWNLOADING;
        stateChanged();
        download();
    }

    // Cancel this download.
    public void cancel() {
        status = CANCELLED;
        stateChanged();
    }

    // Mark this download as having an error.
    private void error() {
        status = ERROR;
        stateChanged();
    }

    // Start or resume downloading.
    private void download() {
        Thread thread = new Thread(this);
        thread.start();
    }

    // Get file name portion of URL.
    private String getFileName(URL url) {
        String fileName = url.getFile();
        return fileName.substring(fileName.lastIndexOf('/') + 1);
    }

    // Download file.
    public void run() {
//        RandomAccessFile file = null;
//        InputStream stream = null;
//
//        try {
//            // Open connection to URL.
//            HttpURLConnection connection =
//                    (HttpURLConnection) url.openConnection();
//
//            // Specify what portion of file to download.
//            connection.setRequestProperty("Range",
//                    "bytes=" + downloaded + "-");
//
//            // Connect to server.
//            connection.connect();
//
//            // Make sure response code is in the 200 range.
//            if (connection.getResponseCode() / 100 != 2) {
//                error();
//            }
//
//            // Check for valid content length.
//            int contentLength = connection.getContentLength();
//            if (contentLength < 1) {
//                error();
//            }
//
//  /* Set the size for this download if it
//     hasn't been already set. */
//            if (size == -1) {
//                size = contentLength;
//                stateChanged();
//            }
//
//            // Open file and seek to the end of it.
//            //file = new RandomAccessFile(getFileName(url), "rw");
//            file = new RandomAccessFile(path, "rw");
//            file.seek(downloaded);
//
//            stream = connection.getInputStream();
//            while (status == DOWNLOADING) {
//    /* Size buffer according to how much of the
//       file is left to download. */
//                byte buffer[];
//                if (size - downloaded > MAX_BUFFER_SIZE) {
//                    buffer = new byte[MAX_BUFFER_SIZE];
//                } else {
//                    buffer = new byte[size - downloaded];
//                }
//
//                // Read from server into buffer.
//                int read = stream.read(buffer);
//                if (read == -1)
//                    break;
//
//                // Write buffer to file.
//                file.write(buffer, 0, read);
//                downloaded += read;
//                stateChanged();
//            }
//
//  /* Change status to complete if this point was
//     reached because downloading has finished. */
//            if (status == DOWNLOADING) {
//                status = COMPLETE;
//                stateChanged();
//            }
//        } catch (Exception e) {
//            error();
//        } finally {
//            // Close file.
//            if (file != null) {
//                try {
//                    file.close();
//                } catch (Exception e) {}
//            }
//
//            // Close connection to server.
//            if (stream != null) {
//                try {
//                    stream.close();
//                } catch (Exception e) {}
//            }
//        }



        // This will get input data from the server
        InputStream inputStream = null;

        // This will read the data from the server;
        OutputStream outputStream = null;

        try {

            // This user agent is for if the server wants real humans to visit
            String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36";
            // Setting the user agent

            // This socket type will allow to set user_agent
            URLConnection con = url.openConnection();
            con.setRequestProperty("User-Agent", USER_AGENT);
//            // Make sure response code is in the 200 range.
//            if (con.getResponseCode() / 100 != 2) {
//                error();
//            }

            // Check for valid content length.
            int contentLength = con.getContentLength();
            if (contentLength < 1) {
                error();
            }



            System.out.println("File contentLength = " + contentLength + " bytes");


            // Requesting input data from server
            inputStream = con.getInputStream();

            // Open local file writer
            outputStream = new FileOutputStream(path);

            // Limiting byte written to file per loop
            byte[] buffer = new byte[2048];

            // Increments file size
            int length;


            // Looping until server finishes
            while ((length = inputStream.read(buffer)) != -1)
            {
                // Writing data
                outputStream.write(buffer, 0, length);
                downloaded+=length;
                //System.out.println("Downlad Status: " + (downloaded * 100) / (contentLength * 1.0) + "%");
            }

            if (status == DOWNLOADING) {
                status = COMPLETE;
                stateChanged();
            }
        } catch (Exception ex) {
            //Logger.getLogger(WebCrawler.class.getName()).log(Level.SEVERE, null, ex);
            error();
        } finally {
            try {
                outputStream.flush();
                outputStream.close();
            } catch (Exception e) {
            }

            try {
                inputStream.close();
            } catch (Exception e) {
            }
        }

        // closing used resources
        // The computer will not be able to use the image
        // This is a must

    }

    // Notify observers that this download's status has changed.
    private void stateChanged() {
        setChanged();
        notifyObservers();
    }
}