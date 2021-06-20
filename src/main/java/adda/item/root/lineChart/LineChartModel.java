package adda.item.root.lineChart;

import adda.base.models.ModelBase;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class LineChartModel extends ModelBase {

    public static final String DISPLAY_NAME_FIELD_NAME = "displayName";
    public static final String DESCRIPTION_FIELD_NAME = "description";
    public static final String DATA_FIELD_NAME = "data";
    public static final String HEADERS_FIELD_NAME = "headers";
    public static final String IS_LOG_FIELD_NAME = "isLog";
    public static final String OX_INDEX_FIELD_NAME = "oxIndex";
    public static final String OY_INDEX_FIELD_NAME = "oyIndex";

    public String displayName;
    public String description;


    public double[][] data;
    public String[] headers;
    public boolean isLog = true;

    public int oxIndex = -1;
    public int oyIndex = -1;

    public void loadFromFileAsync(String path) {
        Thread t = new Thread(() -> {
            List<String> lines = null;
            try {
                lines = Files.readAllLines(Paths.get(path));
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (lines == null || lines.size() < 1) {
                return;
            }

            String firstLine = lines.stream().findFirst().orElse("");
            boolean useFirst = false;
            String[] headers;
            if (firstLine.startsWith("theta")) {
                headers = firstLine.split(" ");
            } else {
                headers = IntStream
                        .range(0, firstLine.split(" ").length)
                        .mapToObj(String::valueOf)
                        .collect(Collectors.toList())
                        .toArray(new String[]{});
                useFirst = true;
            }

            if (!useFirst) {
                lines = lines.stream().skip(1).collect(Collectors.toList());
            }

            double[][] data = new double[headers.length][lines.size()];

            for (int j = 0; j < lines.size(); j++) {
                String[] tmp = lines.get(j).split(" ");
                for (int i = 0; i < tmp.length; i++) {
                    data[i][j] = Double.parseDouble(tmp[i]);
                }
            }

            javax.swing.SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    setData(data);
                    setHeaders(headers);
                    setOxIndex(0);
                    setOyIndex(1);
                }
            });
        });
        t.start();
    }

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

    public double[][] getData() {
        return data;
    }

    public void setData(double[][] data) {
        if (this.data != data) {
            this.data = data;
            notifyObservers(DATA_FIELD_NAME, data);
        }
    }

    public String[] getHeaders() {
        return headers;
    }

    public void setHeaders(String[] headers) {
        if (this.headers != headers) {
            this.headers = headers;
            notifyObservers(HEADERS_FIELD_NAME, headers);
        }
    }

    public boolean isLog() {
        return isLog;
    }

    public void setLog(boolean log) {
        if (this.isLog != log) {
            this.isLog = log;
            notifyObservers(IS_LOG_FIELD_NAME, isLog);
        }
    }

    public int getOxIndex() {
        return oxIndex;
    }

    public void setOxIndex(int oxIndex) {
        if (this.oxIndex != oxIndex) {
            this.oxIndex = oxIndex;
            notifyObservers(OX_INDEX_FIELD_NAME, oxIndex);
        }
    }

    public int getOyIndex() {
        return oyIndex;
    }

    public void setOyIndex(int oyIndex) {
        if (this.oyIndex != oyIndex) {
            this.oyIndex = oyIndex;
            notifyObservers(OY_INDEX_FIELD_NAME, oyIndex);
        }
    }
}