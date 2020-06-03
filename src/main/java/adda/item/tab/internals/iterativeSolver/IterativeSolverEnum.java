package adda.item.tab.internals.iterativeSolver;

import adda.base.annotation.DisplayString;

public enum IterativeSolverEnum {
    @DisplayString("Bi-CGStab(2)")
    bcgs2,

    @DisplayString("Bi-CG")
    bicg,

    @DisplayString("Bi-CGStab")
    bicgstab,

    @DisplayString("CGNR")
    cgnr,

    @DisplayString("CSYM")
    csym,

    @DisplayString("QMR")
    qmr,

    @DisplayString("QMR2")
    qmr2

}
