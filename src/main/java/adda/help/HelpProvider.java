package adda.help;

import adda.Context;
import adda.application.controls.VerticalLayout;
import adda.base.models.IModel;
import adda.item.tab.base.beam.BeamModel;
import adda.item.tab.base.dplGrid.DplGridModel;
import adda.item.tab.base.lambda.LambdaModel;
import adda.item.tab.base.propagation.PropagationModel;
import adda.item.tab.base.refractiveIndex.RefractiveIndexModel;
import adda.item.tab.base.size.SizeModel;
import adda.item.tab.internals.accuracy.AccuracyModel;
import adda.item.tab.internals.formulation.FormulationModel;
import adda.item.tab.internals.initialField.InitialFieldModel;
import adda.item.tab.internals.iterativeSolver.IterativeSolverModel;
import adda.item.tab.internals.jagged.JaggedModel;
import adda.item.tab.internals.maxIterations.MaxIterationsModel;
import adda.item.tab.internals.optimization.OptimizationModel;
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
import adda.item.tab.shape.surface.SurfaceModel;
import adda.utils.StringHelper;

import javax.help.CSH;
import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HelpProvider {

    static Map<Class, List<String>> idMapper = new HashMap<>();

    static {
        idMapper.put(BeamModel.class, Arrays.asList("beam_type"));
        idMapper.put(DplGridModel.class, Arrays.asList("construction_of_a_dipole_set"));
        idMapper.put(LambdaModel.class, Arrays.asList("the_computational_grid"));
        idMapper.put(PropagationModel.class, Arrays.asList("single_orientation"));
        idMapper.put(RefractiveIndexModel.class, Arrays.asList("extensions_of_the_dda"));
        idMapper.put(SizeModel.class, Arrays.asList("the_computational_grid"));
        idMapper.put(AccuracyModel.class, Arrays.asList("general_applicability"));
        idMapper.put(FormulationModel.class, Arrays.asList("dda_formulation"));
        idMapper.put(InitialFieldModel.class, Arrays.asList("iterative_solver"));
        idMapper.put(IterativeSolverModel.class, Arrays.asList("iterative_solver"));
        idMapper.put(JaggedModel.class, Arrays.asList("construction_of_a_dipole_set"));
        idMapper.put(MaxIterationsModel.class, Arrays.asList("iterative_solver"));
        idMapper.put(OptimizationModel.class, Arrays.asList("system_requirements"));
        idMapper.put(SymmetryModel.class, Arrays.asList("construction_of_a_dipole_set"));
        idMapper.put(VolCorrectionModel.class, Arrays.asList("construction_of_a_dipole_set"));
        idMapper.put(AsymParamsSaveModel.class, Arrays.asList("integral_scattering_quantities"));
        idMapper.put(BeamSaveModel.class, Arrays.asList("intfield,_dippol,_and_incbeam"));
        idMapper.put(GeometrySaveModel.class, Arrays.asList("geometry_files"));
        idMapper.put(GranulSaveModel.class, Arrays.asList("granules"));
        idMapper.put(InternalFieldSaveModel.class, Arrays.asList("intfield,_dippol,_and_incbeam"));
        idMapper.put(PlaneSaveModel.class, Arrays.asList("intfield,_dippol,_and_incbeam"));
        idMapper.put(PolarizationSaveModel.class, Arrays.asList("intfield,_dippol,_and_incbeam"));
        idMapper.put(QabsSaveModel.class, Arrays.asList("crosssec"));
        idMapper.put(QextSaveModel.class, Arrays.asList("crosssec"));
        idMapper.put(QscaSaveModel.class, Arrays.asList("crosssec"));
        idMapper.put(RadiationForceSaveModel.class, Arrays.asList("radforce"));
        idMapper.put(ScatteringMatrixSaveModel.class, Arrays.asList("ampl", "mueller"));
        idMapper.put(ThetaSaveModel.class, Arrays.asList("definition_of_scattering_plane_and_angles"));
        idMapper.put(DipoleShapeModel.class, Arrays.asList("introduction"));
        idMapper.put(GranulesModel.class, Arrays.asList("granule_generator"));
        idMapper.put(OrientationModel.class, Arrays.asList("definition_of_scattering_plane_and_angles"));
        idMapper.put(ShapeSelectorModel.class, Arrays.asList("predefined_shapes"));
        idMapper.put(SurfaceModel.class, Arrays.asList("surface_mode"));

    }

    final static List<String> introduction = Arrays.asList("introduction");

    public static JPanel getHelpPanel(IModel model) {
        List<String> ids = getHelpID(model);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setMaximumSize(new Dimension(200, 9999));
        panel.setBackground(Color.white);
        String html = "";
        final JLabel label = new JLabel();
        panel.add(label);
        for (String id : ids) {
            JButton button = new JButton("<HTML><FONT color=\"#000099\"><U>more info</U></FONT></HTML>");
            CSH.setHelpIDString(button, id);
            button.addActionListener(Context.getInstance().getHelpActionListener());
            button.setBorderPainted(false);
            button.setFocusPainted(false);
            button.setOpaque(false);
            button.setContentAreaFilled(false);
            button.setFocusable(false);
            button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            panel.add(button);
            html += getShortDescByID(id);
        }
        label.setText("<HTML>" + html + "</HTML>");
        return panel;
    }

    public static List<String> getHelpID(IModel model) {
        List<String> ids = introduction;
        if (idMapper.containsKey(model.getClass())) {
            ids = idMapper.get(model.getClass());
        }
        return ids;
    }

    public static String getShortDescByID(String id) {
        switch (id) {
            case "introduction":
                return StringHelper.toDisplayString("The discrete dipole approximation (DDA) is<br>" +
                        "a general method to calculate scattering and absorption of electromagnetic<br>" +
                        "waves by particles of arbitrary geometry. In this method the volume of the<br>" +
                        "scatterer is divided into small cubical subvolumes (“dipoles”). Dipole<br>" +
                        "interactions are approximated ba");

            case "using_the_manual":
                return StringHelper.toDisplayString("This manual is intended to cover the<br>" +
                        "computational and physical aspects of ADDA, i.e. choosing proper<br>" +
                        "values for input parameters, performing the simulations, and analyzing the<br>" +
                        "results. In particular, the succeeding sections contain instructions for:");

            case "running_adda":
                return StringHelper.toDisplayString("");

            case "sequential_mode":
                return StringHelper.toDisplayString("The simplest<br>" +
                        "way to run ADDA is to type");

            case "parallel_mode":
                return StringHelper.toDisplayString("On different systems MPI is used<br>" +
                        "differently, you should consult someone familiar with MPI usage on your system.<br>" +
                        "However, running on a multi-core PC is simple, just type");

            case "opencl_(gpu)_mode":
                return StringHelper.toDisplayString("This mode is very similar to the sequential<br>" +
                        "mode, except ADDA executable is named adda_ocl and part of the<br>" +
                        "calculations is carried on the GPU [9]. Therefore<br>" +
                        "a GPU, supporting double precision calculations, is required as well as recent<br>" +
                        "drivers for it. Currently, ADDA can use only a single GPU, a");

            case "applicability_of_the_dda":
                return StringHelper.toDisplayString("");

            case "general_applicability":
                return StringHelper.toDisplayString("The principal advantage of the DDA is that<br>" +
                        "it is completely flexible regarding the geometry of the scatterer, being<br>" +
                        "limited only by the need to use a dipole size d small compared to both any structural length in the scatterer and<br>" +
                        "the wavelength &#955;. A large number<br>" +
                        "of studies devoted to the ac");

            case "extensions_of_the_dda":
                return StringHelper.toDisplayString("In its original form the DDA is derived for<br>" +
                        "finite particles (or a set of several finite particles) in vacuum. However, it<br>" +
                        "is also applicable to finite particles embedded in a homogeneous non-absorbing dielectric<br>" +
                        "medium (refractive index m0).<br>" +
                        "To account for the medium one should replace the part");

            case "system_requirements":
                return StringHelper.toDisplayString("Computational requirements of DDA primarily<br>" +
                        "depend on the size of the computational grid, which in turn depends on the size<br>" +
                        "parameter x and refractive index m of the scatterer (§6.2). This<br>" +
                        "section addresses requirements of standard (FFT) mode of ADDA, while sparse mode is discussed separately (§1");

            case "defining_a_scatterer":
                return StringHelper.toDisplayString("");

            case "reference_frames":
                return StringHelper.toDisplayString("Three different reference frames are used<br>" +
                        "by ADDA: laboratory, particle, and incident wave reference frames. The<br>" +
                        "laboratory reference frame is the default one, and all input parameters and<br>" +
                        "other reference frames are specified relative to it. ADDA simulates light scattering in the particle referen");

            case "the_computational_grid":
                return StringHelper.toDisplayString("ADDA embeds a scatterer in a rectangular computational box, which is<br>" +
                        "divided into identical cubes (as required for the FFT acceleration, §12.2).<br>" +
                        "Each cube is called a “dipole”; its size should be much smaller than a<br>" +
                        "wavelength. The flexibility of the DDA method lies in its ability to naturally<br>" +
                        "s");

            case "construction_of_a_dipole_set":
                return StringHelper.toDisplayString("After defining the computational grid (§6.2) each<br>" +
                        "dipole of the grid should be assigned a refractive index (a void dipole is<br>" +
                        "equivalent to a dipole with refractive index equal to 1). This can be done<br>" +
                        "automatically for a number of predefined shapes or in a very flexible way by<br>" +
                        "specifying scattere");

            case "predefined_shapes":
                return StringHelper.toDisplayString("Predefined<br>" +
                        "shapes are initialized by the command line option");

            case "granule_generator":
                return StringHelper.toDisplayString("Granule<br>" +
                        "generator is enabled by the command line option");

            case "partition_over_processors_in_parallel_mode":
                return StringHelper.toDisplayString("To understand the parallel performance of ADDA it is important to realize how a scattering problem is distributed<br>" +
                        "among different processors. Both the computational grid and the scatterer are<br>" +
                        "partitioned in slices parallel to the xy&#8209;plane<br>" +
                        "(in another words, partition is performed over the z");

            case "particle_symmetries":
                return StringHelper.toDisplayString("Symmetries of a light-scattering problem<br>" +
                        "are used in ADDA to reduce simulation time. All the symmetries are defined for the<br>" +
                        "default incident beam (§9). If<br>" +
                        "the particle is symmetric with respect to reflection over the xz&#8209;plane (yz&#8209;plane), only half of the scattering yz&#8209;plane (xz&");

            case "surface_mode":
                return StringHelper.toDisplayString("Apart from the default<br>" +
                        "free-space mode, ADDA is applicable to particles (finite systems of scatterers) located<br>" +
                        "above the plane surface parallel to the xy&#8209;plane<br>" +
                        "[37]. This<br>" +
                        "is enabled by command line option:");

            case "orientation_of_the_scatterer":
                return StringHelper.toDisplayString("");

            case "single_orientation":
                return StringHelper.toDisplayString("Any particle orientation with respect to<br>" +
                        "the laboratory reference frame can be specified by three Euler angles (&#945;,&#946;,&#947;). ADDA uses a notation based on [38],<br>" +
                        "which is also called “zyz&#8209;notation”<br>" +
                        "or “y&#8209;convention”. In short,<br>" +
                        "coordinate axes attached to the particle are fir");

            case "orientation_averaging":
                return StringHelper.toDisplayString("Orientation averaging is performed over<br>" +
                        "three Euler angles (&#945;,&#946;,&#947;). Rotating over &#945;<br>" +
                        "is equivalent to rotating the scattering plane without changing the orientation<br>" +
                        "of the scatterer relative to the incident radiation. Therefore, averaging over<br>" +
                        "this orientation angle is done wi");

            case "incident_beam":
                return StringHelper.toDisplayString("This section describes how to specify the<br>" +
                        "incident electric field. This field, calculated for each dipole, can be saved<br>" +
                        "to file IncBeam (§C.9). To<br>" +
                        "enable this functionality, specify command line option");

            case "propagation_direction":
                return StringHelper.toDisplayString("The<br>" +
                        "direction of propagation of the incident radiation is specified by the command<br>" +
                        "line option");

            case "beam_type":
                return StringHelper.toDisplayString("Additionally to the default plane wave ADDA supports several types of finite size incident beams, specified by the<br>" +
                        "command line option");

            case "incident_polarization":
                return StringHelper.toDisplayString("Usually, ADDA performs simulation for<br>" +
                        "two incident polarizations e0 (§11.1),<br>" +
                        "which are then used to compute amplitude and Mueller scattering matrices (§11.2, §11.3). The<br>" +
                        "latter can then be used to calculate the result for any incident polarization.<br>" +
                        "Symmetry can sometimes be used to effectively a");

            case "dda_formulation":
                return StringHelper.toDisplayString("Since its introduction by<br>" +
                        "Purcell and Pennypacker [3] the DDA<br>" +
                        "has been constantly developed; therefore a number of different DDA formulations<br>" +
                        "exist [2]. Here<br>" +
                        "we only provide a short summary, focusing on those that are implemented in ADDA. All formulations are equivalent to the solution of the li");

            case "polarizability_prescription":
                return StringHelper.toDisplayString("A<br>" +
                        "number of expressions for the polarizability are known [2]. ADDA implements the following: the Clausius–Mossotti (CM), the radiative<br>" +
                        "reaction correction (RR, [45]),<br>" +
                        "formulation by Lakhtakia (LAK, [46]),<br>" +
                        "digitized Green’s function (DGF, [46]),<br>" +
                        "approximate integration of Green’s tensor over the");

            case "interaction_term":
                return StringHelper.toDisplayString("A few<br>" +
                        "formulations for the direct interaction term are known [2]. Currently,<br>" +
                        "ADDA can use the simplest one (interaction of point dipoles), the FCD<br>" +
                        "(in other words, filtered Green’s tensor [19]),<br>" +
                        "quasistatic version of the FCD, the Integrated Green’s Tensor (IGT, [14]),<br>" +
                        "approximation of IGT (IGT");

            case "reflection_term":
                return StringHelper.toDisplayString("In contrast to G&#773; the reflection term R&#773;<br>" +
                        "does not have full translational symmetry, but only the one parallel to the<br>" +
                        "surface. In particular, R&#773; is a<br>" +
                        "function of distance between evaluation point and the image of source:");

            case "how_to_calculate_scattering_quantities":
                return StringHelper.toDisplayString("The simplest way to calculate scattering<br>" +
                        "quantities is to consider a set of point dipoles with known polarizations, as<br>" +
                        "summarized by Draine [45]. The<br>" +
                        "scattering amplitude F(n) is defined through the asymptotic expansion<br>" +
                        "of the electric field:");

            case "what_scattering_quantities_are_calculated":
                return StringHelper.toDisplayString("");

            case "definition_of_scattering_plane_and_angles":
                return StringHelper.toDisplayString("Currently two definitions for scattering<br>" +
                        "plane and angles are employed in ADDA. In the free-space mode<br>" +
                        "all scattering angles (polar &#952;<br>" +
                        "and azimuthal &#966;, the latter is<br>" +
                        "counted from the x&#8209;axis) are<br>" +
                        "specified in the incident-wave reference frame (§6.1). For<br>" +
                        "default propagation direc");

            case "mueller_matrix_and_its_derivatives":
                return StringHelper.toDisplayString("ADDA calculates the complete Mueller scattering matrix [53]<br>" +
                        "for a set of scattering angles. The Mueller matrix is 4?4 real matrix, which relates<br>" +
                        "the Stokes vector of scattered and incoming waves (the latter is different from<br>" +
                        "incident field in the surface mode, §9.2):");

            case "amplitude_matrix":
                return StringHelper.toDisplayString("An alternative<br>" +
                        "approach to characterize the scattering process is by 2?2 dimensionless complex<br>" +
                        "matrix, the amplitude scattering matrix, which relates<br>" +
                        "scattered and incoming electric fields (the latter is different from incident<br>" +
                        "field in the surface mode, §9.2):");

            case "integral_scattering_quantities":
                return StringHelper.toDisplayString("All scattering quantities described in this<br>" +
                        "section are saved to several files (§C.7),<br>" +
                        "corresponding to each of two incident polarizations (CrossSec&#8209;X and CrossSec&#8209;Y) and to<br>" +
                        "orientation-averaged results (CrossSec). For the latter<br>" +
                        "the result is calculated for unpolarized incident ligh");

            case "decay_rate_enhancement":
                return StringHelper.toDisplayString("These quantities are calculated if and only<br>" +
                        "if the point-dipole incident field is used (§9.2). A<br>" +
                        "dipole with polarization p0<br>" +
                        "at position r0 has a<br>" +
                        "certain decay rate in free-space, which is enhanced in the presence of<br>" +
                        "nanoparticle. The emitted energy is, in general, partly absorbed by the<br>" +
                        "parti");

            case "radiation_forces":
                return StringHelper.toDisplayString("Radiation force for the whole scatterer and<br>" +
                        "for each dipole can also be calculated by ADDA. If the command line<br>" +
                        "option");

            case "internal_fields_and_dipole_polarizations":
                return StringHelper.toDisplayString("ADDA can save internal electric fields Ei and/or<br>" +
                        "dipole polarizations Pi at each dipole (§10) to<br>" +
                        "files IntField and DipPol respectively (§C.9),<br>" +
                        "using command line options");

            case "near-field":
                return StringHelper.toDisplayString("Near-field is the electric field near the<br>" +
                        "particle, i.e. neither in the particle itself nor far enough to be considered a<br>" +
                        "scattered field. Currently, ADDA cannot calculate the near-field in a completely convenient manner. However,<br>" +
                        "Fabio Della Sala and Stefania D'Agostino [27] have<br>" +
                        "contributed a ");

            case "computational_issues":
                return StringHelper.toDisplayString("");

            case "iterative_solver":
                return StringHelper.toDisplayString("The main computation of a DDA simulation,<br>" +
                        "usually taking the major part the execution time, is finding a solution of a large<br>" +
                        "system of linear equations. ADDA uses an alternative form of Eq. (20):");

            case "fast_fourier_transform":
                return StringHelper.toDisplayString("The iterative method only needs the interaction<br>" +
                        "matrix for calculating matrix–vector products. This can be done in &#119978;(NlnN) operations (where N is<br>" +
                        "the total number of dipoles) using the FFT [77].<br>" +
                        "In ADDA 3D (parallel) FFT is explicitly decomposed into a set of 1D FFTs, reducing<br>" +
                        "calculatio");

            case "sparse_mode":
                return StringHelper.toDisplayString("For cases when N &gt;&gt; Nreal,<br>" +
                        "e.g. for very sparse (porous) aggregates, FFT may not provide desired<br>" +
                        "acceleration. Specially for such cases there is a compilation mode of ADDA, named “sparse mode”. It is a direct implementation of matrix–vector<br>" +
                        "product in the DDA without FFT. Its main advantage");

            case "parallel_performance":
                return StringHelper.toDisplayString("ADDA is capable of running on a multiprocessor system, parallelizing a<br>" +
                                "single DDA simulation. It uses MPI for communication routines. This feature can<br>" +
                                "be used both to accelerate the computations and to circumvent the<br>" +
                                "single-computer limit of the available memory, thus enabling simulations with a");

            case "checkpoints":
                return StringHelper.toDisplayString("To facilitate very long simulations ADDA incorporates a checkpoint system, which can be used to break a<br>" +
                        "single simulation into smaller parts. All the intermediate vectors of the<br>" +
                        "iterative solver (§12.1) are<br>" +
                        "saved, which allows restarting the iterative solver exactly at the position,<br>" +
                        "where the ch");

            case "romberg_integration":
                return StringHelper.toDisplayString("Integration is performed in several parts<br>" +
                        "of ADDA: orientation averaging (§8.2),<br>" +
                        "integration of the Mueller matrix over the azimuthal angle (§11.2), and<br>" +
                        "integration of the scattered field over the whole solid angle (§11.4). The<br>" +
                        "same routine is used for all these purposes, which is based on the o");

            case "sommerfeld_integrals":
                return StringHelper.toDisplayString("The four Sommerfeld integrals used by ADDA to calculate reflection term (§10.3) are<br>" +
                        "expressed through two essential integrals V22<br>" +
                        "and U22 [52,83]:");

            case "timing":
                return StringHelper.toDisplayString("");

            case "basic_timing":
                return StringHelper.toDisplayString("The basic timing of ADDA execution on a single processor is performed using standard ANSI C<br>" +
                        "functions clock and time, which are completely portable. The<br>" +
                        "drawbacks are low precision (1 s) of wall-time and low precision (0.01–0.1 s<br>" +
                        "on most systems) and possible overflows (after 1 hour on some syste");

            case "precise_timing":
                return StringHelper.toDisplayString("This feature of ADDA is used to perform the thorough timing of the most computationally<br>" +
                        "intensive parts: initialization of interaction matrix and FFT (especially FFTW3, §12.2) and matrix–vector<br>" +
                        "product. It gives the detailed information both on FFT and algebraic parts,<br>" +
                        "which can be used for deep ");

            case "miscellanea":
                return StringHelper.toDisplayString("Additional<br>" +
                        "files contributed by users will be located in the directory misc/. These files should be<br>" +
                        "considered to be supported (to some extent) by their respective authors. They<br>" +
                        "should be accompanied by enough information to explain their use. A brief<br>" +
                        "description of the available tools is given ");

            case "finale":
                return StringHelper.toDisplayString("");

            case "references":
                return StringHelper.toDisplayString("");

            case "command_line_options":
                return StringHelper.toDisplayString("");

            case "input_files":
                return StringHelper.toDisplayString("");

            case "expcount":
                return StringHelper.toDisplayString("");

            case "avg_params.dat":
                return StringHelper.toDisplayString("");

            case "alldir_params.dat":
                return StringHelper.toDisplayString("");

            case "scat_params.dat":
                return StringHelper.toDisplayString("");


            case "contour_file":
                return StringHelper.toDisplayString("");

            case "field_files":
                return StringHelper.toDisplayString("");

            case "output_files":
                return StringHelper.toDisplayString("");

            case "stderr,_logerr":
                return StringHelper.toDisplayString("");

            case "stdout":
                return StringHelper.toDisplayString("");

            case "output_directory":
                return StringHelper.toDisplayString("");

            case "log":
                return StringHelper.toDisplayString("");

            case "mueller":
                return StringHelper.toDisplayString("");

            case "ampl":
                return StringHelper.toDisplayString("");

            case "crosssec":
                return StringHelper.toDisplayString("");

            case "radforce":
                return StringHelper.toDisplayString("");

            case "intfield,_dippol,_and_incbeam":
                return StringHelper.toDisplayString("");

            case "log_orient_avg_and_log_int":
                return StringHelper.toDisplayString("");

            case "geometry_files":
                return StringHelper.toDisplayString("");

            case "granules":
                return StringHelper.toDisplayString("");

            case "auxiliary_files":
                return StringHelper.toDisplayString("");

            case "tables":
                return StringHelper.toDisplayString("");

            case "checkpoint_files":
                return StringHelper.toDisplayString("");


        }
        return "";
    }

}
