package adda.help;

import adda.Context;
import adda.application.controls.VerticalLayout;
import adda.base.models.IModel;
import adda.item.tab.base.beam.BeamModel;
import adda.item.tab.base.dplGrid.DplGridModel;
import adda.item.tab.base.lambda.LambdaModel;
import adda.item.tab.base.propagation.PropagationModel;
import adda.item.tab.base.refractiveIndex.RefractiveIndexModel;
import adda.item.tab.base.refractiveIndexAggregator.RefractiveIndexAggregatorModel;
import adda.item.tab.base.size.SizeModel;
import adda.item.tab.internals.accuracy.AccuracyModel;
import adda.item.tab.internals.formulation.FormulationModel;
import adda.item.tab.internals.initialField.InitialFieldModel;
import adda.item.tab.internals.iterativeSolver.IterativeSolverModel;
import adda.item.tab.internals.jagged.JaggedModel;
import adda.item.tab.internals.maxIterations.MaxIterationsModel;
import adda.item.tab.internals.optimization.OptimizationModel;
import adda.item.tab.internals.reducedFFT.ReducedFftModel;
import adda.item.tab.internals.symmetry.SymmetryModel;
import adda.item.tab.internals.volCorrection.VolCorrectionModel;
import adda.item.tab.output.asymParams.AsymParamsSaveModel;
import adda.item.tab.output.beam.BeamSaveModel;
import adda.item.tab.output.geometry.GeometrySaveModel;
import adda.item.tab.output.granul.GranulSaveModel;
import adda.item.tab.output.internalField.InternalFieldSaveModel;
import adda.item.tab.output.plane.PlaneSaveModel;
import adda.item.tab.output.polarization.PolarizationSaveModel;
import adda.item.tab.output.qabs.QabsSaveModel;
import adda.item.tab.output.qext.QextSaveModel;
import adda.item.tab.output.qsca.QscaSaveModel;
import adda.item.tab.output.radiationForce.RadiationForceSaveModel;
import adda.item.tab.output.scatteringMatrix.ScatteringMatrixSaveModel;
import adda.item.tab.output.theta.ThetaSaveModel;
import adda.item.tab.shape.dipoleShape.DipoleShapeModel;
import adda.item.tab.shape.granules.GranulesModel;
import adda.item.tab.shape.orientation.OrientationModel;
import adda.item.tab.shape.selector.ShapeSelectorModel;
import adda.item.tab.shape.selector.params.bicoated.BicoatedModel;
import adda.item.tab.shape.selector.params.biellipsoid.BiellipsoidModel;
import adda.item.tab.shape.selector.params.bisphere.BisphereModel;
import adda.item.tab.shape.selector.params.capsule.CapsuleModel;
import adda.item.tab.shape.selector.params.chebyshev.ChebyshevModel;
import adda.item.tab.shape.selector.params.coated.CoatedModel;
import adda.item.tab.shape.selector.params.cuboid.CuboidModel;
import adda.item.tab.shape.selector.params.cylinder.CylinderModel;
import adda.item.tab.shape.selector.params.egg.EggModel;
import adda.item.tab.shape.selector.params.ellipsoid.EllipsoidModel;
import adda.item.tab.shape.selector.params.line.LineModel;
import adda.item.tab.shape.selector.params.plate.PlateBox;
import adda.item.tab.shape.selector.params.plate.PlateModel;
import adda.item.tab.shape.selector.params.prism.PrismModel;
import adda.item.tab.shape.selector.params.rbc.RbcBox;
import adda.item.tab.shape.selector.params.rbc.RbcModel;
import adda.item.tab.shape.selector.params.sphere.SphereModel;
import adda.item.tab.shape.selector.params.spherecuboid.SphereCuboidModel;
import adda.item.tab.shape.selector.params.superellipsoid.SuperellipsoidModel;
import adda.item.tab.shape.surface.SurfaceModel;
import adda.utils.StringHelper;

import javax.help.CSH;
import javax.sound.sampled.Line;
import javax.swing.*;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.text.BreakIterator;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HelpProvider {

    static Map<Class, List<String>> idMapper = new HashMap<>();

    static {
        idMapper.put(BeamModel.class, Arrays.asList("beam_type", StringHelper.toDisplayString("more about beams")));
        idMapper.put(DplGridModel.class, Arrays.asList("construction_of_a_dipole_set", StringHelper.toDisplayString("more about dpl and grid")));
        idMapper.put(LambdaModel.class, Arrays.asList("the_computational_grid", StringHelper.toDisplayString("more about lambda")));
        idMapper.put(PropagationModel.class, Arrays.asList("single_orientation", StringHelper.toDisplayString("more about light propagation")));
        idMapper.put(RefractiveIndexAggregatorModel.class, Arrays.asList("extensions_of_the_dda", StringHelper.toDisplayString("more about refractive index")));
        idMapper.put(SizeModel.class, Arrays.asList("the_computational_grid", StringHelper.toDisplayString("more about size")));
        idMapper.put(AccuracyModel.class, Arrays.asList("general_applicability", StringHelper.toDisplayString("more about accuracy")));
        idMapper.put(FormulationModel.class, Arrays.asList("dda_formulation", StringHelper.toDisplayString(" all DDA formulations"), "polarizability_prescription", StringHelper.toDisplayString(" about polarizability"), "interaction_term", StringHelper.toDisplayString(" about interaction"), "how_to_calculate_scattering_quantities", StringHelper.toDisplayString(" about calculating scattering quantities")));
        idMapper.put(InitialFieldModel.class, Arrays.asList("iterative_solver", StringHelper.toDisplayString("about initial field")));
        idMapper.put(IterativeSolverModel.class, Arrays.asList("iterative_solver", StringHelper.toDisplayString("more about iterative solver")));
        idMapper.put(JaggedModel.class, Arrays.asList("construction_of_a_dipole_set", StringHelper.toDisplayString("more about 'jagged' option")));
        idMapper.put(MaxIterationsModel.class, Arrays.asList("iterative_solver", StringHelper.toDisplayString("about maximum iterations")));
        idMapper.put(OptimizationModel.class, Arrays.asList("system_requirements", StringHelper.toDisplayString("more about optimization")));
        idMapper.put(SymmetryModel.class, Arrays.asList("construction_of_a_dipole_set", StringHelper.toDisplayString("more about symmetry")));
        idMapper.put(VolCorrectionModel.class, Arrays.asList("construction_of_a_dipole_set", StringHelper.toDisplayString("about volume correction")));
        idMapper.put(AsymParamsSaveModel.class, Arrays.asList("integral_scattering_quantities", StringHelper.toDisplayString("about asym option")));
        idMapper.put(BeamSaveModel.class, Arrays.asList("intfield,_dippol,_and_incbeam", StringHelper.toDisplayString("see all beam types")));
        idMapper.put(GeometrySaveModel.class, Arrays.asList("geometry_files", StringHelper.toDisplayString("about geometry file")));
        idMapper.put(GranulSaveModel.class, Arrays.asList("granules", StringHelper.toDisplayString("more about granules")));
        idMapper.put(InternalFieldSaveModel.class, Arrays.asList("intfield,_dippol,_and_incbeam", StringHelper.toDisplayString("more about saving internal field")));
        idMapper.put(PlaneSaveModel.class, Arrays.asList("intfield,_dippol,_and_incbeam", StringHelper.toDisplayString("more about plane")));
        idMapper.put(PolarizationSaveModel.class, Arrays.asList("intfield,_dippol,_and_incbeam", StringHelper.toDisplayString("")));
        idMapper.put(QabsSaveModel.class, Arrays.asList("crosssec", StringHelper.toDisplayString("more about Qabs")));
        idMapper.put(QextSaveModel.class, Arrays.asList("crosssec", StringHelper.toDisplayString("more about Qext")));
        idMapper.put(QscaSaveModel.class, Arrays.asList("crosssec", StringHelper.toDisplayString("more about Qsca")));
        idMapper.put(RadiationForceSaveModel.class, Arrays.asList("radforce", StringHelper.toDisplayString("about radiation force")));
        idMapper.put(ScatteringMatrixSaveModel.class, Arrays.asList("ampl", StringHelper.toDisplayString("about 'Ampl' matrix"), "mueller", StringHelper.toDisplayString("about 'Mueller' matrix")));
        idMapper.put(ThetaSaveModel.class, Arrays.asList("definition_of_scattering_plane_and_angles", StringHelper.toDisplayString("more about theta")));
        idMapper.put(DipoleShapeModel.class, Arrays.asList("introduction", StringHelper.toDisplayString("about dipole shape")));
        idMapper.put(GranulesModel.class, Arrays.asList("granule_generator", StringHelper.toDisplayString("more about granules")));
        idMapper.put(OrientationModel.class, Arrays.asList("definition_of_scattering_plane_and_angles", StringHelper.toDisplayString("about particle orientation")));
        idMapper.put(ShapeSelectorModel.class, Arrays.asList("predefined_shapes", StringHelper.toDisplayString("see all predefined shapes")));
        idMapper.put(SurfaceModel.class, Arrays.asList("surface_mode", StringHelper.toDisplayString("about surface mode")));
        idMapper.put(BicoatedModel.class, Arrays.asList("predefined_shapes", StringHelper.toDisplayString("about bicoated")));
        idMapper.put(BiellipsoidModel.class, Arrays.asList("predefined_shapes", StringHelper.toDisplayString("about biellipsoid")));
        idMapper.put(BisphereModel.class, Arrays.asList("predefined_shapes", StringHelper.toDisplayString("about bisphere")));
        idMapper.put(CuboidModel.class, Arrays.asList("predefined_shapes", StringHelper.toDisplayString("about box")));
        idMapper.put(CapsuleModel.class, Arrays.asList("predefined_shapes", StringHelper.toDisplayString("about capsule")));
        idMapper.put(ChebyshevModel.class, Arrays.asList("predefined_shapes", StringHelper.toDisplayString("about chebyshev")));
        idMapper.put(CoatedModel.class, Arrays.asList("predefined_shapes", StringHelper.toDisplayString("about coated")));
        idMapper.put(CylinderModel.class, Arrays.asList("predefined_shapes", StringHelper.toDisplayString("about cylinder")));
        idMapper.put(EggModel.class, Arrays.asList("predefined_shapes", StringHelper.toDisplayString("about egg")));
        idMapper.put(EllipsoidModel.class, Arrays.asList("predefined_shapes", StringHelper.toDisplayString("about ellipsoid")));
        idMapper.put(PlateModel.class, Arrays.asList("predefined_shapes", StringHelper.toDisplayString("about plate")));
        idMapper.put(PrismModel.class, Arrays.asList("predefined_shapes", StringHelper.toDisplayString("about prism")));
        idMapper.put(RbcModel.class, Arrays.asList("predefined_shapes", StringHelper.toDisplayString("about rbc")));
        idMapper.put(SphereCuboidModel.class, Arrays.asList("predefined_shapes", StringHelper.toDisplayString("about spherebox")));
        idMapper.put(ReducedFftModel.class, Arrays.asList("fast_fourier_transform", StringHelper.toDisplayString("about FFT")));

    }

    final static List<String> introduction = Arrays.asList("introduction", StringHelper.toDisplayString("more info"));

    public static JPanel getHelpPanel(IModel model) {
        List<String> list = getHelpID(model);

        JPanel panel = new JPanel();

        int width = 300;

        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setMaximumSize(new Dimension(width, 9999));
        panel.setMinimumSize(new Dimension(width, 10));
        panel.setBackground(Color.white);
        StringBuilder html = new StringBuilder();
        final JLabel label = new JLabel();
        label.setHorizontalAlignment(JLabel.LEFT);
        panel.add(label);

        int buttonHeight = 0;
        for (int i = 0; i < list.size(); i += 2) {
            String id = list.get(i);
            String displayString = list.get(i + 1);
            JButton button = new JButton("<HTML><FONT color=\"#000099\"><U>" + displayString + "</U></FONT></HTML>");
            CSH.setHelpIDString(button, id);
            button.addActionListener(Context.getInstance().getHelpActionListener());
            button.setBorderPainted(false);
            button.setFocusPainted(false);
            button.setOpaque(false);
            button.setContentAreaFilled(false);
            button.setFocusable(false);
            button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            button.setHorizontalAlignment(JButton.LEFT);
            panel.add(button);
            html.append(getShortDescByID(id));
            buttonHeight += 25;
        }


        final String shortDescByClass = getShortDescByClass(model.getClass());
        label.setText("<HTML>" + (StringHelper.isEmpty(shortDescByClass) ? html.toString() : shortDescByClass) + "</HTML>");

        int height = getTextHeight(label, width);

        label.setPreferredSize(new Dimension(width, height));
        panel.setPreferredSize(new Dimension(width, height + buttonHeight));

        return panel;
    }

    private static int getTextHeight(JLabel label, int breakWidth) {

        final AttributedString attributedString = new AttributedString(label.getText());
        attributedString.addAttribute(TextAttribute.FONT, label.getFont());
        AttributedCharacterIterator paragraph = attributedString.getIterator();
        int paragraphStart = paragraph.getBeginIndex();
        int paragraphEnd = paragraph.getEndIndex();
        FontRenderContext frc = label.getFontMetrics(label.getFont()).getFontRenderContext();
        ;
//                    FontRenderContext frc = new FontRenderContext(FONT.getTransform(), true, true);
        LineBreakMeasurer lineMeasurer = new LineBreakMeasurer(paragraph, BreakIterator.getWordInstance(), frc);

        float drawPosY = 2;
        // Set position to the index of the first character in the paragraph.
        lineMeasurer.setPosition(paragraphStart);
        // Get lines until the entire paragraph has been displayed.
        while (lineMeasurer.getPosition() < paragraphEnd) {
            // Retrieve next layout. A cleverer program would also cache
            // these layouts until the component is re-sized.
            TextLayout layout = lineMeasurer.nextLayout(breakWidth);
            // Compute pen x position. If the paragraph is right-to-left we
            // will align the TextLayouts to the right edge of the panel.
            // Note: this won't occur for the English text in this sample.
            // Note: drawPosX is always where the LEFT of the text is placed.
            float drawPosX = layout.isLeftToRight()
                    ? 2 : breakWidth - layout.getAdvance();
            // Move y-coordinate by the ascent of the layout.
            drawPosY += layout.getAscent();
            // Draw the TextLayout at (drawPosX, drawPosY).
            // Move y-coordinate in preparation for next layout.
            drawPosY += layout.getDescent() + layout.getLeading();
        }

        return (int) drawPosY + 15;
    }

    public static List<String> getHelpID(IModel model) {
        List<String> ids = introduction;
        if (idMapper.containsKey(model.getClass())) {
            ids = idMapper.get(model.getClass());
        }
        return ids;
    }

    public static String getShortDescByClass(Class clazz) {
        if (ReducedFftModel.class.equals(clazz)) {
            return StringHelper.toDisplayString(
                    "Using symmetry of the interaction matrix to reduce the storage space for the Fourier transformed matrix"
            );
        }
        if (RefractiveIndexAggregatorModel.class.equals(clazz)) {
            return StringHelper.toDisplayString(
                    "Sets refractive " +
                            "indices, float. Each pair of arguments specifies real and imaginary part of the refractive index of one of " +
                            "the domains. If '-anisotr' is specified, three refractive indices correspond to one domain (diagonal elements " +
                            "of refractive index tensor in particle reference frame). Maximum number of different refractive indices is " +
                            "defined at compilation time by the parameter MAX_NMAT in file const.h (by default, 15). None of the " +
                            "refractive indices can be equal to 1+0i.\n" +
                            "Specifies that refractive index is anisotropic (its tensor is limited to be diagonal in particle " +
                            "reference frame). '-m' then accepts 6 arguments per each domain. Can not be used with CLDR polarizability and " +
                            "all SO formulations."
            );
        }
        if (BeamModel.class.equals(clazz)) {
            return StringHelper.toDisplayString(
                    "Sets the incident beam.\n" +
                            "<b>Gaussian beam</b>: 5th order approximation of the Gaussian beam (by Barton), this is recommended option for simulation of the Gaussian beam. The beam width is " +
                            "obligatory and x, y, z coordinates of the center of the beam (in laboratory reference frame) are optional.\n" +
                            "<b>Dipole as source</b>: field of a unit point dipole placed at x, y, z coordinates (in laboratory reference " +
                            "frame). Orientation of the dipole is determined by -prop command line option.\n" +
                            "<b>Plane</b>: infinite plane wave"
            );
        }
        if (RadiationForceSaveModel.class.equals(clazz)) {
            return StringHelper.toDisplayString(
                    "Calculate the total radiation force, expressed as cross section."
            );
        }

        if (QabsSaveModel.class.equals(clazz)) {
            return StringHelper.toDisplayString(
                    "Calculate absorption cross section"
            );
        }

        if (DipoleShapeModel.class.equals(clazz)) {
            return StringHelper.toDisplayString(
                    "Use rectangular-cuboid dipoles. Three arguments are the relative dipole sizes along "+
                    "the corresponding axes. Absolute scale is not relevant, i.e. '1 2 2' is equivalent to '0.5 1 1'."
            );
        }

        if (QextSaveModel.class.equals(clazz)) {
            return StringHelper.toDisplayString(
                    "Calculate extinction cross section"
            );
        }

        if (QscaSaveModel.class.equals(clazz)) {
            return StringHelper.toDisplayString(
                    "Calculate scattering cross section (by integrating the scattered field)"
            );
        }

        if (FormulationModel.class.equals(clazz)) {
            return StringHelper.toDisplayString(
                    "ADDA implements the following of expressions for the <b>polarizability</b>: the Clausius-Mossotti (CM), the radiative" +
                            "reaction correction," +
                            "formulation by Lakhtakia," +
                            "digitized Green's function," +
                            "integration of Green's and other." +
                            "formulations for the direct <b>interaction</b> term are known. Currently," +
                            "ADDA can use the simplest one (interaction of point dipoles), the FCD " +
                            "(in other words, filtered Green's tensor)," +
                            "quasistatic version of the FCD, the Integrated Green's Tensor," +
                            "approximation of IGT"
            );
        }

        if (VolCorrectionModel.class.equals(clazz)) {
            return StringHelper.toDisplayString(
                    "Do not use 'dpl (volume) correction'. If this option is given, ADDA will try to match size of "+
                    "the dipole grid along x-axis to that of the particle, either given by '-size' or calculated analytically from "+
                    "'-eq_rad'. Otherwise (by default) ADDA will try to match the volumes, using either '-eq_rad' or the value "+
                    "calculated analytically from '-size'."
            );
        }

        if (IterativeSolverModel.class.equals(clazz)) {
            return StringHelper.toDisplayString(
                    "The main computation of a DDA simulation," +
                    "usually taking the major part the execution time, is finding a solution of a large" +
                    "system of linear equations. ADDA uses the following: Bi-CGStab(2), Bi-CG, Bi-CGStab, CGNR, CSYM, QMR, QMR2."
            );
        }

        if (ThetaSaveModel.class.equals(clazz)) {
            return StringHelper.toDisplayString(
                    "Sets the number of intervals, into which the range of scattering angles [0,180] (degrees) is "+
                            "equally divided, integer. This is used for scattering angles in yz-plane. If particle is not symmetric and "+
                            "orientation averaging is not used, the range is extended to 360 degrees (with the same length of elementary "+
                            "interval, i.e. number of intervals is doubled)."
            );
        }

        if (OptimizationModel.class.equals(clazz)) {
            return StringHelper.toDisplayString(
                    "Sets whether ADDA should optimize itself for maximum speed or for minimum memory usage."
            );
        }

        if (AccuracyModel.class.equals(clazz)) {
            return StringHelper.toDisplayString(
                    "Specifies the stopping criterion for the iterative solver by setting the relative norm of the " +
                            "residual 'epsilon' to reach. <arg> is an exponent of base 10 (float), i.e. epsilon=10^(-<arg>).\n" +
                            "Default: 5 (epsilon=1E-5)"
            );
        }

        if (ScatteringMatrixSaveModel.class.equals(clazz)) {
            return StringHelper.toDisplayString(
                    "Specifies which scattering matrices (from Mueller and amplitude) should "+
                    "be saved to file. Amplitude matrix is never integrated"
            );
        }

        if (AsymParamsSaveModel.class.equals(clazz)) {
            return StringHelper.toDisplayString(
                    "Calculate the asymmetry vector. Implies '-Csca' and '-vec'"
            );
        }

        if (GeometrySaveModel.class.equals(clazz)) {
            return StringHelper.toDisplayString(
                    "Save dipole configuration to a file."+
                            "Specifies format for saving geometry files. First two are ADDA "+
                    "default formats for single- and multi-domain particles respectively. 'text' is automatically changed to "+
                    "'text_ext' for multi-domain particles. Two DDSCAT formats correspond to its shape options 'FRMFIL' (version "+
                    "6) and 'FROM_FILE' (version 7) and output of 'calltarget' utility."
            );
        }

        if (BeamSaveModel.class.equals(clazz)) {
            return StringHelper.toDisplayString(
                    "Save incident beam to a file"
            );
        }

        if (PolarizationSaveModel.class.equals(clazz)) {
            return StringHelper.toDisplayString(
                    "Save dipole polarizations to a file"
            );
        }

        if (GranulSaveModel.class.equals(clazz)) {
            return StringHelper.toDisplayString(
                    "Save granule coordinates (placed by '-granul' option) to a file"
            );
        }

        if (InternalFieldSaveModel.class.equals(clazz)) {
            return StringHelper.toDisplayString(
                    "Save internal fields to a file"
            );
        }

        if (PlaneSaveModel.class.equals(clazz)) {
            return StringHelper.toDisplayString(
                    "Explicitly enables calculation of the scattering in the yz-plane (in incident-wave reference frame). "+
                    "It can also be implicitly enabled by other options."
            );
        }
//
//        if (.class.equals(clazz)) {
//            return StringHelper.toDisplayString(
//
//            );
//        }
//
//        if (.class.equals(clazz)) {
//            return StringHelper.toDisplayString(
//
//            );
//        }



        if (GranulesModel.class.equals(clazz)) {
            return StringHelper.toDisplayString(
                    "Specifies that one particle domain should be randomly filled with " +
                            "spherical granules with specified diameter <diam> and volume fraction <vol_frac>. Domain number to fill is " +
                            "given by the last optional argument. Algorithm may fail for volume fractions > 30-50%.\n"
            );
        }
        if (DplGridModel.class.equals(clazz)) {
            return StringHelper.toDisplayString(
                    "<b>Dipoles per lambda</b>: sets parameter 'dipoles per lambda' (along the x-axis). " +
                            "<b>Grid along X axis</b>: sets dimensions of the computation grid (any positive integers). If '-jagged' option is used the grid dimension is effectively multiplied by the specified number.\n"
            );
        }
        if (InitialFieldModel.class.equals(clazz)) {
            return StringHelper.toDisplayString(
                    "Sets prescription to calculate initial (starting) field for the iterative solver."
            );
        }
        if (JaggedModel.class.equals(clazz)) {
            return StringHelper.toDisplayString(
                    "Sets a size of a big dipole in units of small dipoles, integer. It is used to improve the " +
                            "discretization of the particle without changing the shape.\n"
            );
        }
        if (LambdaModel.class.equals(clazz)) {
            return StringHelper.toDisplayString(
                    "Sets incident wavelength in um, float."
            );
        }
        if (MaxIterationsModel.class.equals(clazz)) {
            return StringHelper.toDisplayString(
                    "Sets the maximum number of iterations of the iterative solver, integer."
            );
        }
        if (OrientationModel.class.equals(clazz)) {
            return StringHelper.toDisplayString(
                    "<b>Rotation</b>: either sets an orientation of the particle by three " +
                            "Euler angles 'alpha','beta','gamma' (in degrees) or specifies that orientation averaging should be " +
                            "performed. <b>Average</b> sets a file with parameters for orientation averaging. Here zyz-notation (or " +
                            "y-convention) is used for Euler angles.\n"
            );
        }
        if (SurfaceModel.class.equals(clazz)) {
            return StringHelper.toDisplayString(
                    "Specifies that scatterer is located above the plane surface, parallel to the " +
                            "xy-plane. <h> specifies the height of particle center above the surface (along the z-axis, in um). Particle " +
                            "must be entirely above the substrate. Following argument(s) specify the refractive index of the substrate " +
                            "(below the surface), assuming that the vacuum is above the surface. It is done either by two values (real and " +
                            "imaginary parts of the complex value) or as effectively infinite 'inf' which corresponds to perfectly" +
                            "reflective surface. The latter implies certain simplifications during calculations."
            );
        }
        if (SymmetryModel.class.equals(clazz)) {
            return StringHelper.toDisplayString(
                    "Automatically determine particle symmetries ('auto'), do not take them into account ('no'), or enforce them ('enf').\n"
            );
        }
        if (SizeModel.class.equals(clazz)) {
            return StringHelper.toDisplayString(
                    "<b>Size along X axis</b>: sets the size of the computational grid along the x-axis in um, float. If default wavelength " +
                            "is used, this option specifies the 'size parameter' of the computational grid. Can not be used together with " +
                            "'-eq_rad'. Size is defined by some shapes themselves, then this option can be used to override the internal " +
                            "specification and scale the shape.\n" +
                            "<b>Equivalent radius</b>: sets volume-equivalent radius of the particle in um, float. If default wavelength is used, " +
                            "this option specifies the volume-equivalent size parameter. Can not be used together with '-size'. Size is " +
                            "defined by some shapes themselves, then this option can be used to override the internal specification and " +
                            "scale the shape.\n"

            );
        }
        if (PropagationModel.class.equals(clazz)) {
            return StringHelper.toDisplayString(
                    "Sets propagation direction of incident radiation, float. Normalization (to the unity " +
                            "vector) is performed automatically. For point-dipole incident beam this determines its direction.\n"
            );
        }
        if (BicoatedModel.class.equals(clazz)) {
            return StringHelper.toDisplayString(
                    "Two identical concentric coated spheres with outer diameter d (first domain), " +
                            "inner diameter d_in, and center-to-center distance R_cc (along the z-axis). It describes both separate and " +
                            "sintered coated spheres. In the latter case sintering is considered symmetrically for cores and shells."
            );
        }
        if (BiellipsoidModel.class.equals(clazz)) {
            return StringHelper.toDisplayString(
                    "Two general ellipsoids in default orientations with " +
                            "centers on the z-axis, touching each other. Their semi-axes are x1,y1,z1 (lower one, first domain) and " +
                            "x2,y2,z2 (upper one, second domain)."
            );
        }
        if (BisphereModel.class.equals(clazz)) {
            return StringHelper.toDisplayString(
                    "Two identical spheres with diameter d and center-to-center distance R_cc (along the " +
                            "z-axis). It describe both separate and sintered spheres."
            );
        }
        if (CuboidModel.class.equals(clazz)) {
            return StringHelper.toDisplayString(
                    "Homogeneous cube (if no arguments are given) or a rectangular parallelepiped with edges x,y,z."
            );
        }
        if (CapsuleModel.class.equals(clazz)) {
            return StringHelper.toDisplayString(
                    "Homogeneous capsule (cylinder with half-spherical end caps) with cylinder height h and " +
                            "diameter d (its axis of symmetry coincides with the z-axis)."
            );
        }
        if (ChebyshevModel.class.equals(clazz)) {
            return StringHelper.toDisplayString(
                    "Axisymmetric Chebyshev particle of amplitude eps and order n, r=r_0[1+eps*cos(n*theta)]. " +
                            "eps is a real number, such that |eps|<=1, while n is a natural number"
            );
        }
        if (CoatedModel.class.equals(clazz)) {
            return StringHelper.toDisplayString(
                    "Sphere with a spherical inclusion; outer sphere has a diameter d (first " +
                            "domain). The included sphere has a diameter d_in (optional position of the center: x,y,z)."
            );
        }
        if (CylinderModel.class.equals(clazz)) {
            return StringHelper.toDisplayString(
                    "Homogeneous cylinder with height (length) h and diameter d (its axis of symmetry coincides " +
                            "with the z-axis)."
            );
        }
        if (EggModel.class.equals(clazz)) {
            return StringHelper.toDisplayString(
                    "Axisymmetric egg shape given by a^2=r^2+nu*r*z-(1-eps)z^2, where 'a' is scaling factor. " +
                            "Parameters must satisfy 0<eps<=1, 0<=nu<eps."
            );
        }
        if (EllipsoidModel.class.equals(clazz)) {
            return StringHelper.toDisplayString(
                    "Homogeneous general ellipsoid with semi-axes x,y,z"
            );
        }
        if (PlateModel.class.equals(clazz)) {
            return StringHelper.toDisplayString(
                    "Homogeneous plate (cylinder with rounded side) with cylinder height h and full diameter d (i.e. " +
                            "diameter of the constituent cylinder is d-h). Its axis of symmetry coincides with the z-axis."
            );
        }
        if (PrismModel.class.equals(clazz)) {
            return StringHelper.toDisplayString(
                    "Homogeneous right prism with height (length along the z-axis) h based on a regular polygon " +
                            "with n sides of size 'a'. The polygon is oriented so that positive x-axis is a middle perpendicular for one " +
                            "of its sides. Dx is size of the polygon along the x-axis, equal to 2Ri and Rc+Ri for even and odd n " +
                            "respectively. Rc=a/[2sin(pi/n)] and Ri=Rc*cos(pi/n) are radii of circumscribed and inscribed circles " +
                            "respectively."
            );
        }
        if (RbcModel.class.equals(clazz)) {
            return StringHelper.toDisplayString(
                    "Red Blood Cell, an axisymmetric (over z-axis) biconcave homogeneous particle, which is " +
                            "characterized by diameter d, maximum and minimum width h, b, and diameter at the position of the maximum " +
                            "width c. The surface is described by ro^4 + 2S*ro^2*z^2 + z^4 + P*ro^2 + Q*z^2+R=0, ro^2=x^2+y^2, P,Q,R,S are " +
                            "determined by the described parameters."
            );
        }
        if (SphereCuboidModel.class.equals(clazz)) {
            return StringHelper.toDisplayString(
                    "Sphere (diameter d_sph) in a cube (size Dx, first domain)"
            );
        }
        if (SphereModel.class.equals(clazz)) {
            return StringHelper.toDisplayString(
                    "Sphere (diameter is Dx)"
            );
        }

        if (LineModel.class.equals(clazz)) {
            return StringHelper.toDisplayString(
                    "Line along X axis (size Dx)"
            );
        }

        if (SuperellipsoidModel.class.equals(clazz)) {
            return StringHelper.toDisplayString(
                    "Homogeneous superellipsoid with semi-axes a, b, c along the x, y, and z "+
                    "directions, respectively. Nonnegative e and n control the shape of cross sections parallel and perpendicular "+
                    "to the xy-plane, respectively, according to [(x/a)^(2/e) + (y/b)^(2/e)]^(e/n) + (z/c)^(2/n) <= 1. Large "+
                    "values of e and/or n lead to spiky shapes with potential discretization problems."
            );
        }


//            idMapper.put(BeamModel.class, Arrays.asList("beam_type", StringHelper.toDisplayString("more about beams")));
//        idMapper.put(DplGridModel.class, Arrays.asList("construction_of_a_dipole_set", StringHelper.toDisplayString("more about dpl and grid")));
//        idMapper.put(LambdaModel.class, Arrays.asList("the_computational_grid", StringHelper.toDisplayString("more about lambda")));
//        idMapper.put(PropagationModel.class, Arrays.asList("single_orientation", StringHelper.toDisplayString("more about light propagation")));
//        idMapper.put(RefractiveIndexModel.class, Arrays.asList("extensions_of_the_dda", StringHelper.toDisplayString("more about refractive index")));
//        idMapper.put(SizeModel.class, Arrays.asList("the_computational_grid", StringHelper.toDisplayString("more about size")));
//        idMapper.put(AccuracyModel.class, Arrays.asList("general_applicability", StringHelper.toDisplayString("more about accuracy")));
//        idMapper.put(FormulationModel.class, Arrays.asList("dda_formulation", StringHelper.toDisplayString(" all DDA formulations")));
//        idMapper.put(InitialFieldModel.class, Arrays.asList("iterative_solver", StringHelper.toDisplayString("about initial field")));
//        idMapper.put(IterativeSolverModel.class, Arrays.asList("iterative_solver", StringHelper.toDisplayString("more about iterative solver")));
//        idMapper.put(JaggedModel.class, Arrays.asList("construction_of_a_dipole_set", StringHelper.toDisplayString("more about 'jagged' option")));
//        idMapper.put(MaxIterationsModel.class, Arrays.asList("iterative_solver", StringHelper.toDisplayString("about maximum iterations")));
//        idMapper.put(OptimizationModel.class, Arrays.asList("system_requirements", StringHelper.toDisplayString("more about optimization")));
//        idMapper.put(SymmetryModel.class, Arrays.asList("construction_of_a_dipole_set", StringHelper.toDisplayString("more about symmetry")));
//        idMapper.put(VolCorrectionModel.class, Arrays.asList("construction_of_a_dipole_set", StringHelper.toDisplayString("about volume correction")));
//        idMapper.put(AsymParamsSaveModel.class, Arrays.asList("integral_scattering_quantities", StringHelper.toDisplayString("about asym option")));
//        idMapper.put(BeamSaveModel.class, Arrays.asList("intfield,_dippol,_and_incbeam", StringHelper.toDisplayString("see all beam types")));
//        idMapper.put(GeometrySaveModel.class, Arrays.asList("geometry_files", StringHelper.toDisplayString("about geometry file")));
//        idMapper.put(GranulSaveModel.class, Arrays.asList("granules", StringHelper.toDisplayString("more about granules")));
//        idMapper.put(InternalFieldSaveModel.class, Arrays.asList("intfield,_dippol,_and_incbeam", StringHelper.toDisplayString("more about saving internal field")));
//        idMapper.put(PlaneSaveModel.class, Arrays.asList("intfield,_dippol,_and_incbeam", StringHelper.toDisplayString("more about saving incident field")));
//        idMapper.put(PolarizationSaveModel.class, Arrays.asList("intfield,_dippol,_and_incbeam", StringHelper.toDisplayString("")));
//        idMapper.put(QabsSaveModel.class, Arrays.asList("crosssec", StringHelper.toDisplayString("more about Qabs")));
//        idMapper.put(QextSaveModel.class, Arrays.asList("crosssec", StringHelper.toDisplayString("more about Qext")));
//        idMapper.put(QscaSaveModel.class, Arrays.asList("crosssec", StringHelper.toDisplayString("more about Qsca")));
//        idMapper.put(RadiationForceSaveModel.class, Arrays.asList("radforce", StringHelper.toDisplayString("about radiation force")));
//        idMapper.put(ScatteringMatrixSaveModel.class, Arrays.asList("ampl", StringHelper.toDisplayString("about 'Ampl' matrix"), "mueller", StringHelper.toDisplayString("about 'Mueller' matrix")));
//        idMapper.put(ThetaSaveModel.class, Arrays.asList("definition_of_scattering_plane_and_angles", StringHelper.toDisplayString("more about theta")));
//        idMapper.put(DipoleShapeModel.class, Arrays.asList("introduction", StringHelper.toDisplayString("about dipole shape")));
//        idMapper.put(GranulesModel.class, Arrays.asList("granule_generator", StringHelper.toDisplayString("more about granules")));
//        idMapper.put(OrientationModel.class, Arrays.asList("definition_of_scattering_plane_and_angles", StringHelper.toDisplayString("about particle orientation")));
//        idMapper.put(ShapeSelectorModel.class, Arrays.asList("predefined_shapes", StringHelper.toDisplayString("see all predefined shapes")));
//        idMapper.put(SurfaceModel.class, Arrays.asList("surface_mode", StringHelper.toDisplayString("about surface mode")));

        return "";
    }

    public static String getShortDescByID(String id) {
        switch (id) {
            case "introduction":
                return StringHelper.toDisplayString("The discrete dipole approximation (DDA) is" +
                        "a general method to calculate scattering and absorption of electromagnetic" +
                        "waves by particles of arbitrary geometry. In this method the volume of the" +
                        "scatterer is divided into small cubical subvolumes ('dipoles'). Dipole" +
                        "interactions are approximated ba...");

            case "using_the_manual":
                return StringHelper.toDisplayString("This manual is intended to cover the" +
                        "computational and physical aspects of ADDA, i.e. choosing proper" +
                        "values for input parameters, performing the simulations, and analyzing the" +
                        "results. In particular, the succeeding sections contain instructions for:...");

            case "sequential_mode":
                return StringHelper.toDisplayString("The simplest way to run ADDA is to type...");

            case "parallel_mode":
                return StringHelper.toDisplayString("On different systems MPI is used" +
                        "differently, you should consult someone familiar with MPI usage on your system." +
                        "However, running on a multi-core PC is simple, just type...");

            case "opencl_(gpu)_mode":
                return StringHelper.toDisplayString("This mode is very similar to the sequential" +
                        "mode, except ADDA executable is named adda_ocl and part of the" +
                        "calculations is carried on the GPU [9]. Therefore" +
                        "a GPU, supporting double precision calculations, is required as well as recent" +
                        "drivers for it. Currently, ADDA can use only a single GPU, a...");

            case "general_applicability":
                return StringHelper.toDisplayString("The principal advantage of the DDA is that" +
                        "it is completely flexible regarding the geometry of the scatterer, being" +
                        "limited only by the need to use a dipole size d small compared to both any structural length in the scatterer and" +
                        "the wavelength &#955;. A large number of studies devoted to the ac...");

            case "extensions_of_the_dda":
                return StringHelper.toDisplayString("In its original form the DDA is derived for" +
                        "finite particles (or a set of several finite particles) in vacuum. However, it" +
                        "is also applicable to finite particles embedded in a homogeneous non-absorbing dielectric" +
                        "medium (refractive index m0). To account for the medium one should replace the part...");

            case "system_requirements":
                return StringHelper.toDisplayString("Computational requirements of DDA primarily" +
                        "depend on the size of the computational grid, which in turn depends on the size" +
                        "parameter x and refractive index m of the scatterer (§6.2). This" +
                        "section addresses requirements of standard (FFT) mode of ADDA, while sparse mode is discussed separately (§1...");

            case "reference_frames":
                return StringHelper.toDisplayString("Three different reference frames are used" +
                        "by ADDA: laboratory, particle, and incident wave reference frames. The" +
                        "laboratory reference frame is the default one, and all input parameters and" +
                        "other reference frames are specified relative to it. ADDA simulates light scattering in the particle referen...");

            case "the_computational_grid":
                return StringHelper.toDisplayString("ADDA embeds a scatterer in a rectangular computational box, which is" +
                        "divided into identical cubes (as required for the FFT acceleration, §12.2)." +
                        "Each cube is called a 'dipole'; its size should be much smaller than a" +
                        "wavelength. The flexibility of the DDA method lies in its ability to naturally" +
                        "s...");

            case "construction_of_a_dipole_set":
                return StringHelper.toDisplayString("After defining the computational grid (§6.2) each" +
                        "dipole of the grid should be assigned a refractive index (a void dipole is" +
                        "equivalent to a dipole with refractive index equal to 1). This can be done" +
                        "automatically for a number of predefined shapes or in a very flexible way by" +
                        "specifying scattere...");

            case "predefined_shapes":
                return StringHelper.toDisplayString("Predefined " +
                        "shapes are initialized by the command line option...");

            case "granule_generator":
                return StringHelper.toDisplayString("Granule" +
                        "generator is enabled by the command line option...");

            case "partition_over_processors_in_parallel_mode":
                return StringHelper.toDisplayString("To understand the parallel performance of ADDA it is" +
                        "important to realize how a scattering problem is distributed" +
                        "among different processors. Both the computational grid and the scatterer are" +
                        "partitioned in slices parallel to the xy&#8209;plane" +
                        "(in another words, partition is performed over the z...");

            case "particle_symmetries":
                return StringHelper.toDisplayString("Symmetries of a light-scattering problem" +
                        "are used in ADDA to reduce simulation time. All the symmetries are defined for the" +
                        "default incident beam (§9). If" +
                        "the particle is symmetric with respect to reflection over the xz&#8209;plane (yz&#8209;plane)," +
                        "only half of the scattering yz&#8209;plane (xz&...");

            case "surface_mode":
                return StringHelper.toDisplayString("Apart from the default" +
                        "free-space mode, ADDA is applicable to particles (finite systems of scatterers) located" +
                        "above the plane surface parallel to the xy&#8209;plane" +
                        "[37]. This" +
                        "is enabled by command line option:...");

            case "orientation_of_the_scatterer":
                return StringHelper.toDisplayString("...");

            case "single_orientation":
                return StringHelper.toDisplayString("Any particle orientation with respect to" +
                        "the laboratory reference frame can be specified by three Euler angles (&#945;,&#946;,&#947;)." +
                        "ADDA uses a notation based on [38]," +
                        "which is also called 'zyz&#8209;notation'" +
                        "or 'y&#8209;convention'. In short," +
                        "coordinate axes attached to the particle are fir...");

            case "orientation_averaging":
                return StringHelper.toDisplayString("Orientation averaging is performed over" +
                        "three Euler angles (&#945;,&#946;,&#947;). Rotating over &#945;" +
                        "is equivalent to rotating the scattering plane without changing the orientation" +
                        "of the scatterer relative to the incident radiation. Therefore, averaging over" +
                        "this orientation angle is done wi...");

            case "incident_beam":
                return StringHelper.toDisplayString("This section describes how to specify the" +
                        "incident electric field. This field, calculated for each dipole, can be saved" +
                        "to file IncBeam (§C.9). To" +
                        "enable this functionality, specify command line option...");

            case "propagation_direction":
                return StringHelper.toDisplayString("The" +
                        "direction of propagation of the incident radiation is specified by the command" +
                        "line option...");

            case "beam_type":
                return StringHelper.toDisplayString("Additionally to the default plane wave ADDA supports " +
                        "several types of finite size incident beams, specified by the" +
                        "command line option...");

            case "incident_polarization":
                return StringHelper.toDisplayString("Usually, ADDA performs simulation for" +
                        "two incident polarizations e0 (§11.1) which are then used to compute amplitude" +
                        "and Mueller scattering matrices (§11.2, §11.3)." +
                        "The latter can then be used to calculate the result for any incident polarization." +
                        "Symmetry can sometimes be used to effectively a...");

            case "dda_formulation":
                return StringHelper.toDisplayString("Since its introduction by" +
                        "Purcell and Pennypacker [3] the DDA has been constantly developed; therefore a number of different DDA formulations" +
                        "exist [2]. Here we only provide a short summary, focusing on those that are implemented in ADDA." +
                        "All formulations are equivalent to the solution of the li...");

            case "polarizability_prescription":
                return StringHelper.toDisplayString("A number of expressions for the polarizability are known." +
                        "ADDA implements the following: the Clausius–Mossotti (CM), the radiative" +
                        "reaction correction (RR, [45])," +
                        "formulation by Lakhtakia (LAK, [46])," +
                        "digitized Green’s function (DGF, [46])," +
                        "approximate integration of Green’s tensor over the...");

            case "interaction_term":
                return StringHelper.toDisplayString("A few" +
                        "formulations for the direct interaction term are known [2]. Currently," +
                        "ADDA can use the simplest one (interaction of point dipoles), the FCD" +
                        "(in other words, filtered Green’s tensor [19])," +
                        "quasistatic version of the FCD, the Integrated Green’s Tensor (IGT, [14])," +
                        "approximation of IGT (IGT...");

            case "reflection_term":
                return StringHelper.toDisplayString("In contrast to G&#773; the reflection term R&#773;" +
                        "does not have full translational symmetry, but only the one parallel to the" +
                        "surface. In particular, R&#773; is a" +
                        "function of distance between evaluation point and the image of source:...");

            case "how_to_calculate_scattering_quantities":
                return StringHelper.toDisplayString("The simplest way to calculate scattering" +
                        "quantities is to consider a set of point dipoles with known polarizations, as" +
                        "summarized by Draine [45]. The" +
                        "scattering amplitude F(n) is defined through the asymptotic expansion" +
                        "of the electric field:...");

            case "what_scattering_quantities_are_calculated":
                return StringHelper.toDisplayString("...");

            case "definition_of_scattering_plane_and_angles":
                return StringHelper.toDisplayString("Currently two definitions for scattering" +
                        "plane and angles are employed in ADDA." +
                        "In the free-space mode" +
                        "all scattering angles (polar &#952;" +
                        "and azimuthal &#966;, the latter is" +
                        "counted from the x&#8209;axis) are" +
                        "specified in the incident-wave reference frame...");

            case "mueller_matrix_and_its_derivatives":
                return StringHelper.toDisplayString("ADDA calculates the complete Mueller scattering matrix [53]" +
                        "for a set of scattering angles. The Mueller matrix is 4?4 real matrix, which relates" +
                        "the Stokes vector of scattered and incoming waves (the latter is different from" +
                        "incident field in the surface mode, §9.2):...");

            case "amplitude_matrix":
                return StringHelper.toDisplayString("An alternative" +
                        "approach to characterize the scattering process is by 2?2 dimensionless complex" +
                        "matrix, the amplitude scattering matrix, which relates" +
                        "scattered and incoming electric fields (the latter is different from incident" +
                        "field in the surface mode, §9.2):...");

            case "integral_scattering_quantities":
                return StringHelper.toDisplayString("All scattering quantities described in this" +
                        "section are saved to several files (§C.7)," +
                        "corresponding to each of two incident polarizations and to" +
                        "orientation-averaged results (CrossSec). For the latter" +
                        "the result is calculated for unpolarized incident ligh...");

            case "decay_rate_enhancement":
                return StringHelper.toDisplayString("These quantities are calculated if and only" +
                        "if the point-dipole incident field is used (§9.2). A" +
                        "dipole with polarization p0" +
                        "at position r0 has a" +
                        "certain decay rate in free-space, which is enhanced in the presence of" +
                        "nanoparticle. The emitted energy is, in general, partly absorbed by the" +
                        "parti...");

            case "radiation_forces":
                return StringHelper.toDisplayString("Radiation force for the whole scatterer and" +
                        "for each dipole can also be calculated by ADDA. If the command line" +
                        "option...");

            case "internal_fields_and_dipole_polarizations":
                return StringHelper.toDisplayString("ADDA can save internal electric fields Ei and/or" +
                        "dipole polarizations Pi at each dipole (§10) to" +
                        "files IntField and DipPol respectively (§C.9)," +
                        "using command line options...");

            case "near-field":
                return StringHelper.toDisplayString("Near-field is the electric field near the" +
                        "particle, i.e. neither in the particle itself nor far enough to be considered a" +
                        "scattered field. Currently, ADDA cannot calculate the near-field in a completely convenient manner." +
                        "However, Fabio Della Sala and Stefania D'Agostino [27] have contributed a ...");

            case "iterative_solver":
                return StringHelper.toDisplayString("The main computation of a DDA simulation," +
                        "usually taking the major part the execution time, is finding a solution of a large" +
                        "system of linear equations. ADDA uses an alternative form of Eq. (20):...");

            case "fast_fourier_transform":
                return StringHelper.toDisplayString("The iterative method only needs the interaction" +
                        "matrix for calculating matrix–vector products. This can be done in &#119978;(NlnN) operations" +
                        "(where N is the total number of dipoles) using the FFT [77]." +
                        "In ADDA 3D (parallel) FFT is explicitly decomposed into a set of 1D FFTs, reducing calculatio...");

            case "sparse_mode":
                return StringHelper.toDisplayString("For cases when N &gt;&gt; Nreal," +
                        "e.g. for very sparse (porous) aggregates, FFT may not provide desired" +
                        "acceleration. Specially for such cases there is a compilation mode of ADDA," +
                        "named 'sparse mode'. It is a direct implementation of matrix–vector" +
                        "product in the DDA without FFT. Its main advantage...");

            case "parallel_performance":
                return StringHelper.toDisplayString("ADDA is capable of running on a multiprocessor system, parallelizing a" +
                        "single DDA simulation. It uses MPI for communication routines. This feature can" +
                        "be used both to accelerate the computations and to circumvent the" +
                        "single-computer limit of the available memory, thus enabling simulations with a...");

            case "checkpoints":
                return StringHelper.toDisplayString("To facilitate very long simulations ADDA incorporates a checkpoint system," +
                        "which can be used to break a" +
                        "single simulation into smaller parts. All the intermediate vectors of the" +
                        "iterative solver (§12.1) are" +
                        "saved, which allows restarting the iterative solver exactly at the position," +
                        "where the ch...");

            case "romberg_integration":
                return StringHelper.toDisplayString("Integration is performed in several parts" +
                        "of ADDA: orientation averaging (§8.2)," +
                        "integration of the Mueller matrix over the azimuthal angle (§11.2), and" +
                        "integration of the scattered field over the whole solid angle (§11.4). The" +
                        "same routine is used for all these purposes, which is based on the o...");

            case "sommerfeld_integrals":
                return StringHelper.toDisplayString("The four Sommerfeld integrals used by ADDA to calculate reflection term are" +
                        "expressed through two essential integrals V22 and U22 [52,83]:...");

            case "basic_timing":
                return StringHelper.toDisplayString("The basic timing of ADDA execution on a single processor is performed using standard ANSI C" +
                        "functions clock and time, which are completely portable. The" +
                        "drawbacks are low precision (1 s) of wall-time and low precision (0.01–0.1 s" +
                        "on most systems) and possible overflows (after 1 hour on some syste...");

            case "precise_timing":
                return StringHelper.toDisplayString("This feature of ADDA is used to perform the thorough timing of the most computationally" +
                        "intensive parts: initialization of interaction matrix and FFT (especially FFTW3, §12.2) and matrix–vector" +
                        "product. It gives the detailed information both on FFT and algebraic parts," +
                        "which can be used for deep ...");

            case "miscellanea":
                return StringHelper.toDisplayString("Additional" +
                        "files contributed by users will be located in the directory misc/. These files should be" +
                        "considered to be supported (to some extent) by their respective authors. They" +
                        "should be accompanied by enough information to explain their use. A brief" +
                        "description of the available tools is given ...");

        }
        return "";
    }

}
