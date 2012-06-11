/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.googlecode.wicket.jquery.ui.dialog;

import java.io.Serializable;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.event.IEvent;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.IFormSubmitter;
import org.apache.wicket.model.IModel;

/**
 * Base class for form-based dialogs
 * @author Sebastien Briquet - sebastien@7thweb.net
 *
 * @param <T> the model object type
 */
public abstract class AbstractFormDialog<T extends Serializable> extends AbstractDialog<T> // implements IFormSubmitter
{
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor
	 * @param id the markup id
	 * @param title the dialog's title
	 */
	public AbstractFormDialog(String id, String title)
	{
		super(id, title);
	}

	/**
	 * Constructor
	 * @param id the markup id
	 * @param title the dialog's title
	 * @param model the dialog's model
	 */
	public AbstractFormDialog(String id, String title, IModel<T> model)
	{
		super(id, title, model, true);
	}
	
	/**
	 * Constructor
	 * @param id the markup id
	 * @param title the dialog's title
	 * @param modal indicates whether the dialog is modal
	 */
	public AbstractFormDialog(String id, String title, boolean modal)
	{
		super(id, title, modal);
	}

	/**
	 * Constructor
	 * @param id the markup id
	 * @param title the dialog's title
	 * @param model the dialog's model
	 * @param modal indicates whether the dialog is modal
	 */
	public AbstractFormDialog(String id, String title, IModel<T> model, boolean modal)
	{
		super(id, title, model, modal);
	}

	
	/* Properties */
	/**
	 * Identifies the button that submit the form.<br/>
	 * It should be in the list of buttons returned by {@link #getButtons()}
	 * @return the submit button
	 */
	protected abstract DialogButton getSubmitButton();

	
	/**
	 * Returns whether form should be processed the default way. When false (default is true), all
	 * validation and form updating is bypassed and the onSubmit method of that button is called
	 * directly, and the onSubmit method of the parent form is not called. A common use for this is
	 * to create a cancel button.
	 * 
	 * @return defaultFormProcessing
	 */
	public boolean getDefaultFormProcessing()
	{
		return true; //default
	}

	/**
	 * Gets the form to be validated by this dialog.<br/>
	 * Warning, the onSubmit and the onError are being delegated to this dialog. However, it does not prevent the use of Form#onSubmit nor Form#onError
	 * @return the form
	 */
	public abstract Form<?> getForm();
	
	/**
	 * Returns the form associated to the button.<br/>
	 * It means that it will return the form if the button is the submit button and null otherwise.
	 * The callback script of {@link FormButtonAjaxBehavior} will differ depending on this.
	 * 
	 * @param button the dialog's button
	 * @return the {@link Form} or <code>null</code>
	 */
	protected Form<?> getForm(DialogButton button)
	{
		if (button.equals(this.getSubmitButton()))
		{
			return this.getForm();
		}

		return null;
	}

	/* Factories */
	/**
	 * Gets the FormButtonAjaxBehavior associated to the specified button.<br/>
	 */
	@Override
	protected ButtonAjaxBehavior newButtonAjaxBehavior(DialogButton button)
	{
		return new FormButtonAjaxBehavior(this, button, this.getForm(button));
	}
	
	
	/* Events */
	/**
	 * DO NOT OVERRIDE UNLESS A VERY GOOD REASON
	 */
	@Override
	public void onEvent(IEvent<?> event)
	{
		Form<?> form = this.getForm(); //null form not handled ; should not go until here if it's the case.

		if (event.getPayload() instanceof DialogEvent)
		{
			final DialogEvent payload = (DialogEvent) event.getPayload();
		
			if (payload.isClicked(this.getSubmitButton()))
			{
				// same technique as AjaxButton class //
				form.process(new IFormSubmitter()
				{
					public Form<?> getForm()
					{
						return AbstractFormDialog.this.getForm();
					}

					public boolean getDefaultFormProcessing()
					{
						return AbstractFormDialog.this.getDefaultFormProcessing();
					}

					public void onSubmit()
					{
						AbstractFormDialog.this.onSubmit(payload.getTarget());
					}

					public void onError()
					{
						AbstractFormDialog.this.onError(payload.getTarget());
					}
				});
	
				if (!form.hasError())
				{
					super.onEvent(event); //close the dialog
				}
			}
			else
			{
				super.onEvent(event); //close the dialog
			}
		}
	}
	
	/**
	 * Triggered after {@link Form#onError()} (when of form processing has error)
	 * @param target
	 */
	protected abstract void onError(AjaxRequestTarget target);

	/**
	 * Triggered after {@link Form#onSubmit()} (the form has been submitted and it does not have error)
	 * @param target
	 */
	protected abstract void onSubmit(AjaxRequestTarget target);
	
	@Override
	protected void onClose(AjaxRequestTarget target, DialogButton button)
	{
		//not mandatory to override
	}

	
	/* Ajax behavior */
	class FormButtonAjaxBehavior extends AbstractDialog<T>.ButtonAjaxBehavior
	{
		private static final long serialVersionUID = 1L;

		private final Form<?> form;

		public FormButtonAjaxBehavior(AbstractFormDialog<T> dialog, DialogButton button, Form<?> form)
		{
			super(dialog, button);

			this.form = form;
		}

		/**
		 * The formId may intentionally be null
		 */
		@Override
		public CharSequence getCallbackScript()
		{
			if (this.form != null)
			{
				final CharSequence script = String.format("wicketSubmitFormById('%s', '%s', null", this.form.getMarkupId(), this.getCallbackUrl()); 

				return this.generateCallbackScript(script) + ";return false";
			}

			return super.getCallbackScript();
		}
	}
}
