/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.sudoku;

/**
 *
 * @author stefano
 */
import com.vaadin.ui.Label;
import java.io.Serializable;

public class Undo implements Serializable{

    private Label label;
    private Integer oldVal;

    public Label getLabel() {
        return label;
    }

    public void setLabel(Label lb) {
        this.label = lb;
    }

    public Integer getOldValue() {
        return oldVal;
    }

    public void setOldValue(Integer oldVal) {
        this.oldVal = oldVal;
    }

    public Undo(Label lb, Integer oldVal) {
        this.label = lb;
        this.oldVal = oldVal;
    }
}
