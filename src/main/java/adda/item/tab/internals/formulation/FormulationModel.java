package adda.item.tab.internals.formulation;

import adda.base.AddaOption;
import adda.base.IAddaOption;
import adda.item.tab.TabEnumModel;
import adda.utils.StringHelper;

import java.util.Arrays;
import java.util.List;

public class FormulationModel  extends TabEnumModel<FormulationEnum> {


    public static final String POLARIZATION_FIELD_NAME = "polarization";
    public static final String INTERACTION_FIELD_NAME = "interaction";
    public static final String SCATTERING_QUANTITIES_FIELD_NAME = "scatteringQuantities";
    public static final String SHELL_COUNT_FIELD_NAME = "shellCount";
    public static final String POL = "pol";
    public static final String INT = "int";
    public static final String SCAT = "scat";
    protected PolarizationEnum polarization = PolarizationEnum.ldr;
    protected InteractionEnum interaction = InteractionEnum.poi;
    protected ScatteringQuantitiesEnum scatteringQuantities = ScatteringQuantitiesEnum.dr;
    protected int shellCount = 0;

    public FormulationModel() {
        this.setLabel("DDA formulation");//todo localization
        setEnumValue(FormulationEnum.Default);
        setDefaultEnumValue(FormulationEnum.Default);
    }

    @Override
    public void setEnumValue(FormulationEnum enumValue) {
        super.setEnumValue(enumValue);
        switch (enumValue) {
            case Default:
                setDefaultFormulation();
                break;
            case FCD:
                setFcdFormulation();
                break;
            case IGT_SO:
                setIgtsoFormulation();
                break;
        }
    }



    @Override
    public void applyDefaultState() {
        setEnumValue(FormulationEnum.Default);
        setDefaultFormulation();
    }

    public PolarizationEnum getPolarization() {
        return polarization;
    }

    public void setPolarization(PolarizationEnum polarization) {
        if(!this.polarization.equals(polarization)) {
            this.polarization = polarization;
            notifyObservers(POLARIZATION_FIELD_NAME, polarization);
        }
    }

    public InteractionEnum getInteraction() {
        return interaction;
    }

    public void setInteraction(InteractionEnum interaction) {
        if(!this.interaction.equals(interaction)) {
            this.interaction = interaction;
            notifyObservers(INTERACTION_FIELD_NAME, interaction);
        }
    }

    public ScatteringQuantitiesEnum getScatteringQuantities() {
        return scatteringQuantities;
    }

    public void setScatteringQuantities(ScatteringQuantitiesEnum scatteringQuantities) {
        if(!this.scatteringQuantities.equals(scatteringQuantities)) {
            this.scatteringQuantities = scatteringQuantities;
            notifyObservers(SCATTERING_QUANTITIES_FIELD_NAME, scatteringQuantities);
        }
    }

    public int getShellCount() {
        return shellCount;
    }

    public void setShellCount(int shellCount) {
        if(this.shellCount != shellCount) {
            this.shellCount = shellCount;
            notifyObservers(SHELL_COUNT_FIELD_NAME, shellCount);
        }
    }


    @Override
    protected List<IAddaOption> getAddaOptionsInner() {
        String additional =  interaction.equals(InteractionEnum.igt) ? String.format(" %d", shellCount) : "";
        String additionalDesc = StringHelper.isEmpty(additional) ? "" : String.format("[%s ]", additional);
        return Arrays.asList(
                new AddaOption(POL, polarization.toString(), StringHelper.toDisplayString(POLARIZATION_FIELD_NAME) + ": " + StringHelper.toDisplayString(polarization)),
                new AddaOption(INT, interaction.toString() + additional, StringHelper.toDisplayString(INTERACTION_FIELD_NAME) + ": " + StringHelper.toDisplayString(interaction) + additionalDesc),
                new AddaOption(SCAT, scatteringQuantities.toString(), StringHelper.toDisplayString("scattering quantities") + ": " + StringHelper.toDisplayString(scatteringQuantities))
        );
    }


    private void setDefaultFormulation() {
        setInteraction(InteractionEnum.poi);
        setPolarization(PolarizationEnum.ldr);
        setScatteringQuantities(ScatteringQuantitiesEnum.dr);
    }

    private void setFcdFormulation() {
        setInteraction(InteractionEnum.fcd);
        setPolarization(PolarizationEnum.fcd);
        setScatteringQuantities(ScatteringQuantitiesEnum.dr);
    }

    private void setIgtsoFormulation() {
        setInteraction(InteractionEnum.igt_so);
        setPolarization(PolarizationEnum.igt_so);
        setScatteringQuantities(ScatteringQuantitiesEnum.igt_so);
    }

}

