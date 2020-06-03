package adda.item.tab.internals.formulation;

import adda.base.annotation.DisplayString;

public enum InteractionEnum {

    @DisplayString("Interaction of point dipoles")
    poi,

    @DisplayString("filtered Green’s tensor (FCD)")
    fcd,

    @DisplayString("static FCD")
    fcd_st,

    @DisplayString("Integrating Green's tensor")
    igt,

    @DisplayString("Integrating Green's tensor (SO)")
    igt_so,

    @DisplayString("Non-local")
    nloc,

    @DisplayString("Non-local AV")
    nloc_av,

    @DisplayString("Second Order (SO)")
    so
}
