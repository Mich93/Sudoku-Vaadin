package com.example.sudoku;

import com.vaadin.data.Property;
import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.ui.Label;


public class GridClickListener implements LayoutClickListener {

	@Override
	public void layoutClick(LayoutClickEvent event) {
		if(event.getClickedComponent() != null)
		{		
 			Property property = ((Label)event.getClickedComponent()).getPropertyDataSource();
                        
                        if(!property.isReadOnly())
			{ 
                                int inter=Integer.parseInt(property.getValue().toString());
                                if(inter<9)
                                    inter++;
                                else
                                    inter=0;
				property.setValue( new Integer(inter));
				((Label)event.getClickedComponent()).setPropertyDataSource(property);
			}
		
		}
	}
}
