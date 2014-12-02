/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.sudoku;

import com.vaadin.server.VaadinService;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.DragAndDropWrapper;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author stefano
 */
public class ResetListener implements ClickListener{
    private GridLayout grid;
    private Board board;
    private Stack<Undo> stack=new Stack();
    public ResetListener(GridLayout grid, Board board) {
        this.grid = grid;
        this.board = board;
    }
    @Override
    public void buttonClick(Button.ClickEvent event) {
        for (int col = 0; col < 9; col++) {
            for (int row = 0; row < 9; row++) {
                board.setReadOnly(col, row, false);
                board.setValue(col, row, new Integer(0), true);
                Label label = (Label) ((DragAndDropWrapper) grid.getComponent(col, row)).getData();
                label.setPropertyDataSource(board.getCellElement(col, row));
                

            }
        }
        

    }
}
