package com.googlecode.wicket.jquery.ui.dialog;

import org.apache.wicket.IClusterable;
import org.apache.wicket.ajax.AjaxRequestTarget;

//TODO javadac & license

public class DialogButton implements IClusterable
{
	private static final long serialVersionUID = 1L;
	private static short sequence = 0;

	private final int id;
	private final String text;
	private boolean enabled;

	public DialogButton(String text)
	{
		this(text, true);
	}

	public DialogButton(String text, boolean enabled)
	{
		this.id = this.nextSequence();
		this.text = text;
		this.enabled = enabled;
	}

	// Properties //
	public String getText()
	{
		return this.text;
	}

	public boolean isEnabled()
	{
		return this.enabled;
	}

	public void setEnabled(boolean enabled)
	{
		this.enabled = enabled;
	}

	public void setEnabled(AjaxRequestTarget target, boolean enabled)
	{
		if (enabled)
		{
			this.enable(target);
		}
		else
		{
			this.disable(target);
		}
	}


	/**
	 * Get the markupId of the specified button.<br/>
	 * This can be use to enable/disable the button
	 * @param button the button name
	 * @return the markupId
	 */
	protected String getMarkupId()
	{
		return String.format("btn%02x", this.id).toLowerCase();
	}

	// Methods //
	/**
	 * //0x00 to 0xFF
	 * @return
	 */
	private synchronized int nextSequence()
	{
		return (DialogButton.sequence++ % Short.MAX_VALUE);
	}

	/**
	 * TODO javadoc
	 * @param target
	 * @param button
	 */
	private void enable(AjaxRequestTarget target)
	{
		target.appendJavaScript(String.format("$('#%s').button('enable');", this.getMarkupId()));
	}

	/**
	 * TODO javadoc
	 * @param target
	 * @param button
	 */
	private void disable(AjaxRequestTarget target)
	{
		target.appendJavaScript(String.format("$('#%s').button('disable');", this.getMarkupId()));
	}


	@Override
	public int hashCode()
	{
		return this.id;
	}

	@Override
	public boolean equals(Object object)
	{
		if (object instanceof DialogButton)
		{
			return (((DialogButton) object).id == this.id);
		}

		return super.equals(object);
	}

	@Override
	public String toString()
	{
		return this.text;
	}
}
