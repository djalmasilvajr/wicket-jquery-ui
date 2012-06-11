package com.googlecode.wicket.jquery.ui.wizard;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.event.IEvent;
import org.apache.wicket.extensions.wizard.IWizard;
import org.apache.wicket.extensions.wizard.IWizardModel;
import org.apache.wicket.extensions.wizard.IWizardModelListener;
import org.apache.wicket.extensions.wizard.IWizardStep;
import org.apache.wicket.extensions.wizard.Wizard;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.model.IModel;

import com.googlecode.wicket.jquery.ui.dialog.AbstractFormDialog;
import com.googlecode.wicket.jquery.ui.dialog.DialogButton;
import com.googlecode.wicket.jquery.ui.dialog.DialogEvent;
import com.googlecode.wicket.jquery.ui.panel.JQueryFeedbackPanel;

public abstract class AbstractWizardDialog<T extends Serializable> extends AbstractFormDialog<T> implements IWizardModelListener, IWizard
{
	private static final long serialVersionUID = 1L;

	// Buttons //
	private static final DialogButton BTN_PREV = new DialogButton("<");
	private static final DialogButton BTN_NEXT = new DialogButton(">");
	private static final DialogButton BTN_LAST = new DialogButton(">>");
	private static final DialogButton BTN_FINISH = new DialogButton("Finish");

	private IWizardModel wizardModel;
	private Form<T> form;

	/**
	 * @param id
	 * @param title
	 */
	public AbstractWizardDialog(String id, String title)
	{
		super(id, title);
	}

	/**
	 * @param id
	 * @param title
	 */
	public AbstractWizardDialog(String id, String title, IWizardModel wizardModel)
	{
		super(id, title);

		this.init(wizardModel);
	}

	/**
	 * @param id
	 * @param title
	 * @param modal
	 */
	public AbstractWizardDialog(String id, String title, boolean modal)
	{
		super(id, title, modal);

	}

	/**
	 * @param id
	 * @param title
	 * @param model
	 * @param modal
	 */
	public AbstractWizardDialog(String id, String title, IModel<T> model, boolean modal)
	{
		super(id, title, model, modal);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param id
	 * @param title
	 * @param model
	 */
	public AbstractWizardDialog(String id, String title, IModel<T> model)
	{
		super(id, title, model);
		// TODO Auto-generated constructor stub
	}

	protected void init(final IWizardModel wizardModel)
	{
		if (wizardModel == null)
		{
			throw new IllegalArgumentException("argument wizardModel must be not null");
		}

		this.wizardModel = wizardModel;
		this.wizardModel.addListener(this);

		this.form = new Form<T>(Wizard.FORM_ID);
		this.add(this.form);

		// header (title + summary )//
		this.form.add(new EmptyPanel(Wizard.HEADER_ID));

		// Feedback //
		this.form.add(new JQueryFeedbackPanel(Wizard.FEEDBACK_ID, this));

		// dummy view to be replaced
		this.form.add(new EmptyPanel(Wizard.VIEW_ID));
//		this.form.add(newOverviewBar(Wizard.OVERVIEW_ID));
	}

	// Events //
	/**
	 * TODO javadoc
	 * @param target
	 */
	protected void onConfigure(AjaxRequestTarget target)
	{
		// configure buttons //
		BTN_PREV.setEnabled(target, this.wizardModel.isPreviousAvailable());
		BTN_NEXT.setEnabled(target, this.wizardModel.isNextAvailable());
		BTN_LAST.setEnabled(target, this.wizardModel.isLastAvailable());

		// update form //
		target.add(this.form);
	}

	@Override
	public void onEvent(IEvent<?> event)
	{
		if (event.getPayload() instanceof DialogEvent)
		{
			final DialogEvent payload = (DialogEvent) event.getPayload();
			AjaxRequestTarget target = payload.getTarget();

			if (payload.isClicked(this.getSubmitButton()))
			{
				this.onSubmit(target);
				this.close(target, this.getSubmitButton());
				//super.onEvent(event); //TODO: temp only, to remove
			}
			else if (payload.isClicked(this.getCancelButton()))
			{
				this.onCancel(target);
				this.close(target, this.getCancelButton());
				//super.onEvent(event); //TODO: temp only, to remove
			}
			else
			{
				// will call onActiveStepChanged
				if (payload.isClicked(BTN_PREV))
				{
					this.getWizardModel().previous(); 
				}
				else if (payload.isClicked(BTN_NEXT))
				{
					this.getWizardModel().next();
				}
				else if (payload.isClicked(BTN_LAST))
				{
					this.getWizardModel().last();
				}

				// re-configure buttons //
				AbstractWizardDialog.this.onConfigure(target);
			}
		}
	}
	

	@Override
	protected void onOpen(AjaxRequestTarget target)
	{
		super.onOpen(target);

		this.wizardModel.reset(); // reset model to prepare for action
		this.onConfigure(target);
	}

	@Override
	protected final void onSubmit(AjaxRequestTarget target)
	{
		this.onFinish();
		this.onFinish(target);
	}

	@Override
	public void onFinish()
	{
		// from IWizardModelListener, not intended to be used.
	}

	protected abstract void onFinish(AjaxRequestTarget target);

	@Override
	public void onCancel()
	{
		// from IWizardModelListener, not intended to be used.
	}

	/**
	 * <b>Warning: </b> by overriding this method, you need to call <code>super.onCancel(target)</code>, if you plan to also handle default {@link #onCancel()} event.
	 * @param target
	 */
	protected void onCancel(AjaxRequestTarget target)
	{
		this.onCancel();
	}
	
	@Override
	protected void onError(AjaxRequestTarget target)
	{
		// not mandatory to override
	}


	// IWizard //
	@Override
	public IWizardModel getWizardModel()
	{
		return this.wizardModel;
	}


	// IWizardModelListener //
	@Override
	public void onActiveStepChanged(IWizardStep step)
	{
		this.form.replace(step.getHeader(Wizard.HEADER_ID, this, this));
		this.form.replace(step.getView(Wizard.VIEW_ID, this, this));
	}


	// AbstractFormDialog //
	@Override
	protected List<DialogButton> getButtons()
	{
		return Arrays.asList(BTN_PREV, BTN_NEXT, BTN_LAST, BTN_FINISH, BTN_CANCEL);
	}

	@Override
	protected DialogButton getSubmitButton()
	{
		return BTN_FINISH;
	}

	protected DialogButton getCancelButton()
	{
		return BTN_CANCEL;
	}

	@Override
	public Form<?> getForm()
	{
		return this.form;
	}

	@Override
	protected Form<?> getForm(DialogButton button)
	{
		//BTN_PREV, BTN_NEXT and BTN_LAST are form buttons
		if (button.equals(BTN_PREV) || button.equals(BTN_NEXT) || button.equals(BTN_LAST))
		{
			return this.getForm();
		}

		return super.getForm(button);
	}
}
