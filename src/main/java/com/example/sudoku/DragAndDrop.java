
package com.example.sudoku;

import com.vaadin.data.Property;
import com.vaadin.event.dd.*;
import com.vaadin.event.dd.acceptcriteria.AcceptAll;
import com.vaadin.event.dd.acceptcriteria.AcceptCriterion;
import com.vaadin.ui.*;
import com.vaadin.ui.DragAndDropWrapper.WrapperTransferable;
import java.util.Stack;

/**
 *
 * @author stefano
 */

public class DragAndDrop implements DropHandler {


    
    private GridLayout grid;
    private Label label;
    private Stack<Undo> stack=new Stack();
    public DragAndDrop(Label label,Stack<Undo> stack) {
        this.label=label;
        this.stack=stack;
    }

    public Label getLabel() {
        return label;
    }
    
    @Override
    public void drop(final DragAndDropEvent dropEvent) {


       
       WrapperTransferable tr = (WrapperTransferable) dropEvent
                .getTransferable();
       Label transferedLabel=(Label)tr.getDraggedComponent();
       Undo undo=new Undo(label,Integer.parseInt(label.getValue()));
       Property property= transferedLabel.getPropertyDataSource();
       label.setPropertyDataSource(property);
       stack.push(undo);
        
        

    
    }

    @Override
    public AcceptCriterion getAcceptCriterion() {
        return AcceptAll.get();
    }

    
            }


    


                        
                        
        


    

