/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.sudoku;

import com.vaadin.data.Property;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import java.util.Stack;

/**
 *
 * @author stefano
 */
public class UndoListener implements ClickListener {

    private Stack<Undo> stack;

    public UndoListener(Stack<Undo> stack) {
        this.stack = stack;
    }

    @Override
    public void buttonClick(ClickEvent event) {
        if (!stack.empty()){
          Undo undo = stack.pop();
          Label label = new Label();
          label=undo.getLabel();
          System.out.println("The label is "+label.getValue());
          Property property= label.getPropertyDataSource();
          property.setValue(undo.getOldValue());
          System.out.println("The old value in Undo is "+property.getValue());
          System.out.println("The old value in UndoListener is "+undo.getOldValue());
        
          label.setPropertyDataSource(property);
        }
        else
            Notification.show("Nothing to undo",Type.WARNING_MESSAGE);
    }

}
    

