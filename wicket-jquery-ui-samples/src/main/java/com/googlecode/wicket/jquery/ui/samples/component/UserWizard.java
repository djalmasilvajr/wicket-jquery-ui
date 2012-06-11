package com.googlecode.wicket.jquery.ui.samples.component;

import org.apache.wicket.extensions.wizard.WizardModel;
import org.apache.wicket.extensions.wizard.WizardStep;

import com.googlecode.wicket.jquery.ui.wizard.AbstractWizardDialog;

public abstract class UserWizard extends AbstractWizardDialog<String>
{
	private static final long serialVersionUID = 1L;

	public UserWizard(String id, String title)
	{
		super(id, title);
		
		WizardModel wizardModel = new WizardModel();

		wizardModel.add(new Step1());
		wizardModel.add(new Step2());

		this.init(wizardModel);
	}

	class Step1 extends WizardStep
	{
		private static final long serialVersionUID = 1L;
		
		@Override
		public String getTitle()
		{
			return "Step1";
		}
	}

	class Step2 extends WizardStep
	{
		private static final long serialVersionUID = 1L;
		
		@Override
		public String getTitle()
		{
			return "Step2";
		}
	}
}
