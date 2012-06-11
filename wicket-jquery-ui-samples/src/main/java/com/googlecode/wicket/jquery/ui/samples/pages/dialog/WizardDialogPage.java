package com.googlecode.wicket.jquery.ui.samples.pages.dialog;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.FeedbackPanel;

import com.googlecode.wicket.jquery.ui.form.button.AjaxButton;
import com.googlecode.wicket.jquery.ui.panel.JQueryFeedbackPanel;
import com.googlecode.wicket.jquery.ui.samples.component.UserWizard;

public class WizardDialogPage extends AbstractDialogPage
{
	private static final long serialVersionUID = 1L;
	
	public WizardDialogPage()
	{
		// FeedbackPanel //
		final FeedbackPanel feedbackPanel = new JQueryFeedbackPanel("feedback");
		this.add(feedbackPanel.setOutputMarkupId(true));

		// Wizard //
		final UserWizard wizard = new UserWizard("wizard", "Create a user") {

			private static final long serialVersionUID = 1L;

			@Override
			protected void onCancel(AjaxRequestTarget target)
			{
				info("Canceled...");
				target.add(feedbackPanel);
			}
			
			@Override
			protected void onFinish(AjaxRequestTarget target)
			{
				info("Finished!");
				target.add(feedbackPanel);
			}
		};

		this.add(wizard);

		// Form //
		Form<Void> form = new Form<Void>("form");
		this.add(form);

		// Button //
		form.add(new AjaxButton("open") {

			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form)
			{
				wizard.open(target);
			}
			
		});
	}
}
