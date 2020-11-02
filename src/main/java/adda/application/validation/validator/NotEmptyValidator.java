package adda.application.validation.validator;

import javax.swing.*;
import adda.application.validation.Error;
import adda.application.validation.ErrorProvider;

public class NotEmptyValidator extends ErrorProvider {
    public NotEmptyValidator(JFrame parent, JComponent c) {
        super(parent,c);
    }

    @Override
    protected Error ErrorDefinition(JComponent c) {
        String str = "";
        if (c instanceof JTextField) {
            str = ((JTextField) c).getText();
        } else if (c instanceof JPasswordField) {
            str = new String(((JPasswordField) c).getPassword());
        }
        if (str.isEmpty()) {
            return new Error(Error.ERROR, "This field cannot be empty");
        } else {
            return new Error(Error.NO_ERROR, "");
        }
    }
}
