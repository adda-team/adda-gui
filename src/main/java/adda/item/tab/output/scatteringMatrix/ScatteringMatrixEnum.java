package adda.item.tab.output.scatteringMatrix;

import adda.base.annotation.DisplayString;

public enum ScatteringMatrixEnum {
    @DisplayString("Mueller matrix")//todo localization
    muel,

    @DisplayString("Amplitude matrix")//todo localization
    ampl,

    @DisplayString("Mueller and amplitude matrix")//todo localization
    both,

    @DisplayString("Without matrix")//todo localization
    none
}
