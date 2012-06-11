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
package com.googlecode.wicket.jquery.ui.form.button;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import com.googlecode.wicket.jquery.ui.JQueryIcon;
import com.googlecode.wicket.jquery.ui.dialog.DialogButton;
import com.googlecode.wicket.jquery.ui.dialog.DialogButtons;
import com.googlecode.wicket.jquery.ui.dialog.DialogIcon;
import com.googlecode.wicket.jquery.ui.dialog.MessageFormDialog;
import com.googlecode.wicket.jquery.ui.panel.ModelPanel;

/**
 * Provides a {@link AjaxButton} which pop-ups an OK-Cancel confirmation dialog when clicked. In case of confirmation, the form is sent via an ajax post.<br/>
 * <br/>
 * <b>Note: </b> this component is not an {@link AjaxButton} itself but a Panel, it should not be attached to a &lt;button /&gt;; it can be attached on a &lt;div /&gt; or a &lt;span /&gt; for instance.<br/>
 * <br/>
 * <b>Warning: </b> it is not possible to get a form component value - that is going to be changed - to be displayed in the dialog box message. The reason is that in order to get a form component (updated) model object, the form component should be validated. The dialog does not proceed to a (whole) form validation while being opened, because the form validation will occur when the user will confirm (by clicking on OK button). This the intended behavior.
 * 
 * @author Sebastien Briquet - sebastien@7thweb.net
 *
 */
public abstract class ConfirmAjaxButton extends ModelPanel<String>
{
	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructor
	 * @param id markup id
	 * @param label the button text
	 * @param title the dialog title
	 * @param message the dialog message
	 */
	public ConfirmAjaxButton(String id, String label, String title, String message)
	{
		this(id, label, title, new Model<String>(message));
	}

	/**
	 * Constructor
	 * @param id markup id
	 * @param label the button text
	 * @param title the dialog title
	 * @param message the dialog message
	 */
	public ConfirmAjaxButton(String id, String label, String title, IModel<String> message)
	{
		super(id, message);

		final MessageFormDialog dialog = new MessageFormDialog("dialog", title, this.getModel(), DialogButtons.OK_CANCEL, DialogIcon.WARN) {

			private static final long serialVersionUID = 1L;

			@Override
			protected DialogButton getSubmitButton()
			{
				return BTN_OK;
			}

			@Override
			public Form<?> getForm()
			{
				return Form.findForm(ConfirmAjaxButton.this);
			}

			@Override
			protected void onError(AjaxRequestTarget target)
			{
				super.close(target, null); //by default, the dialog does not close on error.
				ConfirmAjaxButton.this.onError(target, this.getForm());
			}

			@Override
			protected void onSubmit(AjaxRequestTarget target)
			{
				ConfirmAjaxButton.this.onSubmit(target, this.getForm());
			}
		};

		this.add(dialog);

		final AjaxButton button = new AjaxButton("button") {

			private static final long serialVersionUID = 1L;
			
			@Override
			protected JQueryIcon getIcon()
			{
				return JQueryIcon.Alert;
			}

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form)
			{
				dialog.open(target);
			}
		};
		
		this.add(button.setDefaultFormProcessing(false)); //does not validate the form before the dialog is being displayed

		button.add(new Label("label", new Model<String>(label)).setRenderBodyOnly(true));
	}
	
	
	// Events //
	/**
	 * Triggered when the form has been submitted, but the validation failed
	 * @param target the {@link AjaxRequestTarget}
	 * @param form the {@link Form}
	 */
	protected abstract void onError(AjaxRequestTarget target, Form<?> form);

	/**
	 * Triggered when the form has been submitted, and the validation succeed
	 * @param target the {@link AjaxRequestTarget}
	 * @param form the {@link Form}
	 */
	protected abstract void onSubmit(AjaxRequestTarget target, Form<?> form);
}
