package com.example.sudoku;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Label;

public class CEValueChangeListener implements ValueChangeListener {

	@Override
	public void valueChange(ValueChangeEvent event) {
//		System.out.println( "In CEValueChangeListener: " + event.getProperty() );
		((Label)event.getProperty()).markAsDirty();
		((Label)event.getProperty()).setImmediate(true);
	}
}
