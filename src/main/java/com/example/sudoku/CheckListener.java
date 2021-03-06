/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.sudoku;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author stefano
 */
public class CheckListener implements ClickListener{
    private Board board;
    private GridLayout grid;
    private static Cube c1, c2, c3, c4, c5, c6, c7, c8, c9;
    private boolean check=false;
	private static List<Cube> cubeList;
    public CheckListener(Board board, GridLayout grid) {
        this.board = board;
        this.grid = grid;
    }
    @Override
    public void buttonClick(Button.ClickEvent event) {
        cubeList = new ArrayList<Cube>();
		c1 = Cube.createCube(CubeTypes.TOPLEFT_11, board);
		c2 = Cube.createCube(CubeTypes.TOPMIDDLE_12, board);
		c3 = Cube.createCube(CubeTypes.TOPRIGHT_13, board);
		c4 = Cube.createCube(CubeTypes.MIDDLELEFT_21, board);
		c5 = Cube.createCube(CubeTypes.MIDDLEMIDDLE_22, board);
		c6 = Cube.createCube(CubeTypes.MIDDLERIGHT_23, board);
		c7 = Cube.createCube(CubeTypes.BOTTOMLEFT_31, board);
		c8 = Cube.createCube(CubeTypes.BOTTOMMIDDLE_32, board);
		c9 = Cube.createCube(CubeTypes.BOTTOMRIGHT_33, board);
		cubeList.add(c1);
		cubeList.add(c2);
		cubeList.add(c3);
		cubeList.add(c4);
		cubeList.add(c5);
		cubeList.add(c6);
		cubeList.add(c7);
		cubeList.add(c8);
		cubeList.add(c9);
		
		check=solveSudoko();
                if(check==true){
                    Notification.show("The Sudoku is correct!",Type.WARNING_MESSAGE);
                    return;
                }else{
                    Notification.show("The Sudoku is not corret. Try again.",Type.ERROR_MESSAGE);
                    return;
                }
    }
    private boolean solveSudoko() {

		Square cell = EmptySquarePresent();
		
		if (!cell.isEmptySquare()) {
			
                        return true;
		}

		/*
		 * Reason why its 1 to 9 is because you want to try numbers from
		 * 1 to 9 in each cell
		 */
		int row = cell.getRowIndex();
		int col = cell.getColumnIndex();
		Cube cc = findCube(row, col);
		
		for (int num = 1; num <= 9; num++) {
			
			// check if no conflicts then
			if (!AreThereConflicts(num, row, col)) {

				cc.getLeftOutValues().remove(num);
				cc.getCubeValues().add(num);
				
				if (solveSudoko()) {
					return true;
				}
				
				// need to unassign				
				int value = board.getIntegerValue(col, row);
				//perform house keeping on our lists
				cc.getLeftOutValues().add(value);
				cc.getCubeValues().remove(value);
				board.setValue(col, row, 0, false ); // unassign
				
			}
		}
		
		return false;
	}

	/*
	 * Are there any conflicts ?
	 */
	private boolean AreThereConflicts(Integer number, Integer row, Integer col) {

		Cube cc = findCube(row, col);
		
		// is the number already in the cube?
		if(cc.getCubeValues().contains(number)) {
			return true;
		}
	
		// is the number already found in the same row or column of the board?
		for (int k = 0; k < 9; k++) {
			if ((number == board.getIntValue(k, row) || board.getIntValue(col, k) == number)) {
				return true;
			}
		}

		return false;
	}

	/*
	 * Find cube
	 */
	private Cube findCube(int i, int j) {
		for (Cube ll : cubeList) {
			if (i >= ll.getRowfromIndex() && i < ll.getRowtoIndex() && j >= ll.getColfromIndex() && j < ll.getColtoIndex()) {
				return ll;
			}
		}
		return null;
	}

	/*
	 * Returns an empty square
	 */
	private Square EmptySquarePresent() {

		Square sq = new Square();
		for (int row = 0; row < 9; row++) {
			for (int col = 0; col < 9; col++) {
				if (board.getIntValue( col, row) == 0) {
					sq.setRowIndex(row);
					sq.setColumnIndex(col);
					sq.setEmptySquare(true);
					return sq;
				}
			}
		}
		return sq;
	}
}
