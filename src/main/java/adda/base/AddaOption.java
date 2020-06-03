package adda.base;

import adda.utils.StringHelper;

public  class AddaOption implements IAddaOption {

    public static final String FORMAT = "-%s %s";
    public static final String FORMAT_SIMPLE = "-%s";
    private String name;
    private String value;
    private String displayString;

    public AddaOption(String name) {
        this(name, "", "");
    }

    public AddaOption(String name, String value) {
        this(name, value, "");
        displayString = getFormatted();
    }

    public AddaOption(String name, String value, String displayString) {
        this.name = name;
        this.value = value;
        this.displayString = displayString;
    }

    @Override
    public String getCommandLineName() {
        return this.name;
    }

    @Override
    public String getCommandLineValue() {
        return this.value;
    }

    @Override
    public String getFormatted() {
        if (!StringHelper.isEmpty(getCommandLineValue())) {
            return String.format(FORMAT, getCommandLineName(), getCommandLineValue());

        }
        return String.format(FORMAT_SIMPLE, getCommandLineName());

    }

    @Override
    public String getDisplayString() {
//        if (!StringHelper.isEmpty(getCommandLineValue())) {
//            return String.format("%s: %s", displayName, getCommandLineValue());
//        }
//        return String.format("%s", getCommandLineName());
//        return String.format("%s    [%s]", displayString, getFormatted());
        return displayString;

    }
}
