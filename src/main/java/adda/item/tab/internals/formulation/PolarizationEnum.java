package adda.item.tab.internals.formulation;

import adda.base.annotation.DisplayString;

public enum PolarizationEnum {

    @DisplayString("Corrected LDR")
    cldr,

    @DisplayString("Clausius–Mossotti")
    cm,

    @DisplayString("Digitized Green’s function")
    dgf,

    @DisplayString("Filtered Coupled Dipoles")
    fcd,

    @DisplayString("Integrating Green's tensor (SO)")
    igt_so,

    @DisplayString("Lakhtakia")
    lak,

    @DisplayString("LDR")
    ldr,

    @DisplayString("Non-local")
    nloc,

    @DisplayString("Non-local AV")
    nloc_av,

    @DisplayString("Radiative Reaction Correction")
    rrc,

    @DisplayString("Second Order (SO)")
    so
}
