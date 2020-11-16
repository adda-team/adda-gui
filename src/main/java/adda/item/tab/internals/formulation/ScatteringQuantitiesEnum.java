package adda.item.tab.internals.formulation;

import adda.base.annotation.DisplayString;

public enum ScatteringQuantitiesEnum {

    @DisplayString("Draine")
    dr,

    @DisplayString("Integrated Green's tensor (SO)")
    igt_so,

    @DisplayString("Second Order (SO)")
    so,

    @DisplayString("Finite dipole correction")
    fin
}
