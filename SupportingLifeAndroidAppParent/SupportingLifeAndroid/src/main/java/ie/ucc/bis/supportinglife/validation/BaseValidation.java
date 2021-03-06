package ie.ucc.bis.supportinglife.validation;

import android.content.Context;

public abstract class BaseValidation implements Validation {

    protected Context context;

    protected BaseValidation(Context context) {
        this.context = context;
    }

	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}

    @Override
    public ValidationResult validate(RadioGroupFieldValidations field) {
		return null;
    }

	@Override
	public ValidationResult validate(TextFieldValidations fieldValidation) {
		return null;
	}
	
	@Override
	public ValidationResult validate(ButtonGroupTableValidations buttonGroupTableValidations) {
		return null;
	}		
}
