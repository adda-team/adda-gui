package adda.application.validation.validator;

import adda.application.validation.Error;
import adda.application.validation.ErrorProvider;
import adda.base.models.IModel;
import adda.utils.StringHelper;

import javax.swing.*;

public class ModelValidator extends ErrorProvider {

    protected IModel model;
    protected String propertyName;

    public ModelValidator(JFrame parent, JComponent c, IModel model, String propertyName) {
        super(parent,c);
        this.model = model;
        this.propertyName = propertyName;
    }

    @Override
    protected Error ErrorDefinition(JComponent c) {
        String str = "";
        if (c instanceof JTextField) {
            str = ((JTextField) c).getText();
        }
        if (str.isEmpty()) {
            return new Error(Error.ERROR, "This field cannot be empty");
        }

        if (model != null
                && !model.validate()
                && model.getValidationErrors().containsKey(propertyName)
                && !StringHelper.isEmpty(model.getValidationErrors().get(propertyName))
        ) {
            return new Error(Error.ERROR, model.getValidationErrors().get(propertyName));
        }

        return new Error(Error.NO_ERROR, "");
    }
}


