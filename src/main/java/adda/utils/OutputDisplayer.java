package adda.utils;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;


public class OutputDisplayer implements Runnable {

    protected final JTextArea textArea_;
    protected InputStream reader_ = null;
    protected Thread thread_ = null;
    protected Process proc_;

    public OutputDisplayer(JTextArea textArea) {
        textArea_ = textArea;

    }

    public void cancel() {
        if (thread_ != null) {
            thread_.interrupt();
        }

        if (proc_ != null) {
            proc_.destroy();
        }
    }

    public void commence(Process proc) {
        cancel();
        proc_ = proc;
        reader_ = proc_.getInputStream();
        thread_ = new Thread(this);
        thread_.start();
    }

    public void run() {
        StringBuilder buf = new StringBuilder();
        try {
            final Thread currentThread = Thread.currentThread();
            setText("");
            byte[] buffer = new byte[100];
            while (isAlive(proc_) || reader_.available() > 0) {
                Thread.sleep(25);
                int no = reader_.available();
                if (no > 0) {
                    int n = reader_.read(buffer, 0, Math.min(no, buffer.length));
                    appendText(new String(buffer, 0, n));
                }
            }
        } catch (IOException | InterruptedException ioe ) {
            buf.append("\n\nERROR:\n"+ioe.toString());
            setText( buf.toString() );
        } finally {
            try {
                reader_.close();
            } catch ( IOException ioe ) {
                ioe.printStackTrace();
            }
        }
    }
    public static boolean isAlive(Process p) {
        try {
            p.exitValue();
            return false;
        }
        catch (IllegalThreadStateException e) {
            return true;
        }
    }


    private void setText(final String text) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                textArea_.setText(text);
            }
        });
    }

    private void appendText(final String text) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                textArea_.append(text);
            }
        });
    }
}

//
//public class OutputDisplayer implements Runnable {
//
//    protected final JTextArea textArea_;
//    protected Reader reader_ = null;
//    protected Thread thread_ = null;
//    protected Process proc_;
//
//    public OutputDisplayer(JTextArea textArea) {
//        textArea_ = textArea;
//
//    }
//
//    public void cancel() {
//        if (thread_ != null) {
//            thread_.interrupt();
//        }
//
//        if (proc_ != null) {
//            proc_.destroy();
//        }
//    }
//
//    public void commence(Process proc) {
//        cancel();
//        proc_ = proc;
//        InputStream in = proc_.getInputStream();
//        reader_ = new InputStreamReader(in);
//        thread_ = new Thread(this);
//        thread_.start();
//    }
//
//    public void run() {
//        StringBuilder buf = new StringBuilder();
//        setText("");
//        try {
//            final Thread currentThread = Thread.currentThread();
//            while(reader_ != null && !currentThread.isInterrupted()) {
//                int c = reader_.read();
//                if( c==-1 ) return;
//
//                buf.append((char) c);
//                setText( buf.toString() );
//            }
//        } catch ( IOException ioe ) {
//            buf.append("\n\nERROR:\n"+ioe.toString());
//            setText( buf.toString() );
//        } finally {
//            try {
//                reader_.close();
//            } catch ( IOException ioe ) {
//                ioe.printStackTrace();
//            }
//        }
//    }
//
//
//    private void setText(final String text) {
//        EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                textArea_.setText(text);
//            }
//        });
//    }
//}