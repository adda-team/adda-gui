package adda.item.tab.internals.initialField;

import adda.base.annotation.DisplayString;

public enum InitialFieldEnum {
    @DisplayString("Auto")
    auto,

    @DisplayString("Incident field")
    inc,

//    @DisplayString("Read from file")
//    read,

    @DisplayString("Wentzel-Kramers-Brillouin")
    wkb,

    @DisplayString("Zero")
    zero
}
