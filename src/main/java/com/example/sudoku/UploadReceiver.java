package com.example.sudoku;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;

import com.vaadin.ui.DragAndDropWrapper;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Upload.FinishedEvent;
import com.vaadin.ui.Upload.FinishedListener;
import com.vaadin.ui.Upload.Receiver;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UploadReceiver implements Receiver, FinishedListener {

	private static final long serialVersionUID = 1L;
	private FileOutputStream fos = null;
	private FileInputStream fis = null;
	private InputStreamReader isr = null;

	private GridLayout grid;
	private Board board;
	
	public UploadReceiver( GridLayout grid, Board board )
	{
		this.grid = grid;
		this.board = board;
	}

	// The receiveUpload() method is called when the user clicks the submit
	// button.
	@Override
	public OutputStream receiveUpload(String filename, String mimeType) {

		try {
			fos = new FileOutputStream("sudoinput.txt");
		} 
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		return new OutputStream() {

			@Override
			public void write(int b) throws IOException {
				fos.write(b);
			}

		};
	}

	@Override
	public void uploadFinished(FinishedEvent event) {


		try {
			fis = new FileInputStream("sudoinput.txt");
			isr = new InputStreamReader(fis);
			BufferedReader br = new BufferedReader(isr);
			String sCurrentLine;

			int row = 0; int col = 0;
			while ((sCurrentLine = br.readLine()) != null) {

				String[] field = sCurrentLine.split(" ");

				col = 0;
				for (String s : field ) 
				{
                                    
					// set to ReadOnly to false to allow for update
					board.setReadOnly( col, row, false );
                                        board.setValue(col, row, s, s.equals("0") ? false : true);
					col++;
				}
				row++;
			}			
			
			// update the tiles for display
			for( col = 0; col < 9; col++ ){
				for( row = 0; row < 9; row++ ){
                                 Label label=(Label)((DragAndDropWrapper)grid.getComponent(col, row)).getData();
                                 label.setPropertyDataSource(board.getCellElement(col, row));

			
                                }
                        }
                        Notification.show("File imported succesfully",Notification.Type.WARNING_MESSAGE);
		
		} catch (FileNotFoundException ex) {
                Logger.getLogger(UploadReceiver.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(UploadReceiver.class.getName()).log(Level.SEVERE, null, ex);
            }

	}
}
