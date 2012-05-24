package com.googlecode.wicket.jquery.ui.samples.pages.selectable;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.Model;

import com.googlecode.wicket.jquery.ui.interaction.Draggable;
import com.googlecode.wicket.jquery.ui.interaction.Droppable;
import com.googlecode.wicket.jquery.ui.interaction.Selectable;
import com.googlecode.wicket.jquery.ui.panel.JQueryFeedbackPanel;

public class DefaultSelectablePage extends AbstractSelectablePage
{
	private static final long serialVersionUID = 1L;
	final FeedbackPanel feedbackPanel;
	
	public DefaultSelectablePage()
	{
		// FeedbackPanel //
		this.feedbackPanel = new JQueryFeedbackPanel("feedback");
		this.add(this.feedbackPanel.setOutputMarkupId(true));

		Selectable selectable = new Selectable("selectable");
		this.add(selectable);
		
		selectable.add(new Draggable<String>("draggable1", new Model<String>("draggable 1")));
		selectable.add(new Draggable<String>("draggable2", new Model<String>("draggable 2")));

		this.add(this.newDroppable("droppable"));
	}

	/**
	 * Gets a new Droppable.
	 * By default 'over' and 'exit' ('out') events are disabled to minimize client/server round-trips.
	 */
	private Droppable<String> newDroppable(String id)
	{
		return new Droppable<String>(id, new Model<String>("droppable area")) {
	
			private static final long serialVersionUID = 1L;

			@Override
			protected void onDrop(AjaxRequestTarget target, Draggable<?> draggable)
			{
				if (draggable != null)
				{
					info(String.format("%s dropped in %s", draggable.getDefaultModelObjectAsString(), this.getDefaultModelObjectAsString()));
				}

				target.add(feedbackPanel);
			}

			@Override
			protected void onOver(AjaxRequestTarget target, Draggable<?> draggable)
			{
				// should override #isOverEventEnabled(), returning true, for this event to be triggered.
			}

			@Override
			protected void onExit(AjaxRequestTarget target, Draggable<?> draggable)
			{
				// should override #isExitEventEnabled(), returning true, for this event to be triggered.
			}
		};
	}
}
