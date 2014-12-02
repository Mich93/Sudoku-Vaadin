/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.sudoku;

import com.vaadin.server.VaadinService;
import com.vaadin.ui.Button;
import com.vaadin.ui.DragAndDropWrapper;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author stefano
 */
public class ButtonListener implements Button.ClickListener {

    private String level;
    private GridLayout grid;
    private Board board;
    private FileInputStream fis = null;
    private InputStreamReader isr = null;

    public ButtonListener(String level, GridLayout grid, Board board) {
        this.level = level;
        this.grid = grid;
        this.board = board;
    }

    @Override
    public void buttonClick(Button.ClickEvent event) {
        try {
            String basepath = VaadinService.getCurrent()
                    .getBaseDirectory().getAbsolutePath();
            fis = new FileInputStream(basepath + "/files/" + level);
            isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            String sCurrentLine;

            int row = 0;
            int col = 0;
            while ((sCurrentLine = br.readLine()) != null) {

                String[] field = sCurrentLine.split(" ");

                col = 0;
                for (String s : field) {
                    // set to ReadOnly to false to allow for update
                    board.setReadOnly(col, row, false);
                    board.setValue(col, row, s, s.equals("0") ? false : true);
                    col++;
                }
                row++;
            }

            // update the tiles for display
            for (col = 0; col < 9; col++) {
                for (row = 0; row < 9; row++) {
                   Label label = (Label) ((DragAndDropWrapper) grid.getComponent(col, row)).getData();
                   label.setPropertyDataSource(board.getCellElement(col, row));

                }
            }
            Notification.show(level + " mode set succesfully", Notification.Type.WARNING_MESSAGE);

        } catch (FileNotFoundException ex) {
            Logger.getLogger(UploadReceiver.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(UploadReceiver.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
