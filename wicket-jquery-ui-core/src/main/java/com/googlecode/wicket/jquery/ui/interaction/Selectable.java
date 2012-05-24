/**
 * TODO license & javadoc
 */
package com.googlecode.wicket.jquery.ui.interaction;

import org.apache.wicket.model.IModel;

import com.googlecode.wicket.jquery.ui.JQueryBehavior;
import com.googlecode.wicket.jquery.ui.JQueryContainer;
import com.googlecode.wicket.jquery.ui.Options;

public class Selectable extends JQueryContainer
{
	private static final long serialVersionUID = 1L;

	public Selectable(String id)
	{
		super(id);
	}

	public Selectable(String id, IModel<?> model)
	{
		super(id, model);
	}

	@Override
	public JQueryBehavior newWidgetBehavior(String selector)
	{
		return new SelectableBehavior(selector, new Options());
	}

	public static class SelectableBehavior extends JQueryBehavior
	{
		private static final long serialVersionUID = 1L;
		private static final String METHOD = "selectable";
				
		public SelectableBehavior(String selector, Options options)
		{
			super(selector, METHOD, options);
		}
	}
}
