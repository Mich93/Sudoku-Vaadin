package com.example.sudoku;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinService;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.DragAndDropWrapper;
import com.vaadin.ui.DragAndDropWrapper.DragStartMode;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.Upload;
import com.vaadin.ui.VerticalLayout;
import java.util.Stack;
import javax.swing.ImageIcon;

/**
 * The Application's "main" class
 */
@SuppressWarnings("serial")
@Theme("demo")
public class Sudoku extends UI {
	
	private Panel panel;
	private GridLayout grid;
	private Board board;
	private ImageIcon image;
	private VerticalLayout vLayout = new VerticalLayout();
        private VerticalLayout vLayout1 = new VerticalLayout();
	private HorizontalLayout hLayout = new HorizontalLayout();
	private HorizontalLayout hLayout1 = new HorizontalLayout();
	private HorizontalLayout hLayout2 = new HorizontalLayout();
	private UploadReceiver uploadReceiver;
	private Upload upload,drag;
	private Button solveButton;
        private Button easy,medium,hard,undo,check,reset;
        private Stack<Undo> stack=new Stack();
        
	@WebServlet(value = "/*", asyncSupported = true)
	@VaadinServletConfiguration(productionMode = false, ui = Sudoku.class)
	   public static class Servlet extends VaadinServlet {
    }

    @Override
    protected void init(VaadinRequest request) {

        // Find the application directory
        String basepath = VaadinService.getCurrent().getBaseDirectory().getAbsolutePath();

        System.out.println(basepath);

        grid = new GridLayout(9, 9);
        grid.setMargin(false);
        grid.setSpacing(false);
        grid.setWidth("350px");
        grid.setHeight("350px");
        grid.addLayoutClickListener(new GridClickListener());
        
        GridLayout grid2 = new GridLayout(3, 3);

        grid2.setMargin(false);
        grid2.setSpacing(false);
        grid2.setWidth("114px");
        grid2.setHeight("114px");

        grid2.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

        
        panel = new Panel();
        panel.setContent(grid);
        panel.setWidth("355px");
        panel.setHeight("355px");
        board = new Board();
        
        Panel panel2 = new Panel();
        panel2.setContent(grid2);
        panel2.setWidth("119px");
        panel2.setHeight("119px");
        Panel textPanel=new Panel();
        textPanel.setWidth("170px");
        textPanel.setHeight("120px");
        
        Label infoLabel = new Label("Drag the input file here");
        VerticalLayout dropPane = new VerticalLayout(infoLabel);
        dropPane.setWidth(400.0f, Unit.PIXELS);
        dropPane.setHeight(200.0f, Unit.PIXELS);
        dropPane.addStyleName("drop-area");
        
        DragAndDropHandler drop = new DragAndDropHandler(dropPane,board, grid);
        drop.setSizeUndefined();

        // connect the tile to the display
        for (int col = 0; col < 9; col++) {
            for (int row = 0; row < 9; row++) {
                Label label = new Label();
                label.setPropertyDataSource(board.getCellElement(col, row));
                 label.addStyleName("separator");
                if (col == 2 || col == 5) {
                    label.addStyleName("margin-col");

                }
                if (row == 2 || row == 5) {

                    label.addStyleName("margin-row");

                }
               
                label.addValueChangeListener(new CEValueChangeListener());
                DragAndDropWrapper wrapper = new DragAndDropWrapper(label);
                wrapper.setDropHandler(new DragAndDrop(label,stack));
                wrapper.setData(label);
                wrapper.setSizeUndefined();
                grid.addComponent(wrapper, col, row);
                grid.setComponentAlignment(wrapper, Alignment.MIDDLE_CENTER);

                 }
        }
        for (int row = 1; row <= 3; row++) {
            for (int col = 1; col <= 3; col++) {

                Label label = new Label();
                if (row == 1) {
                    label.setPropertyDataSource(new CellElement(col, true, col, row));
                } else if (row == 2) {
                    label.setPropertyDataSource(new CellElement(col + 3, true, col, row));
                } else if (row == 3) {
                    label.setPropertyDataSource(new CellElement(col + 6, true, col, row));
                }
                DragAndDropWrapper wrapper = new DragAndDropWrapper(label);
                wrapper.setSizeUndefined();
                label.addStyleName("separator");
                label.setWidth(null);
                label.setImmediate(true);

                wrapper.setDragStartMode(DragStartMode.WRAPPER);
                grid2.addComponent(wrapper);

            }
        }
        uploadReceiver = new UploadReceiver(grid, board);
        upload = new Upload(" ", uploadReceiver);
        upload.setButtonCaption("Load Soduko File");
        upload.setImmediate(true);
        easy = new Button("EASY");
        easy.addClickListener(new ButtonListener("easy", grid, board));
        medium = new Button("MEDIUM");
        medium.addClickListener(new ButtonListener("medium", grid, board));
        hard = new Button("HARD");
        hard.addClickListener(new ButtonListener("hard", grid, board));
        undo=new Button("UNDO");
        undo.addClickListener(new UndoListener(stack));
        check=new Button("CHECK");
        check.addClickListener(new CheckListener(board,grid));
        reset=new Button("RESET");
        reset.addClickListener(new ResetListener(grid,board));
        solveButton = new Button("Solve");
        solveButton.addClickListener(new Solver(grid, board));
        
        vLayout.addComponent(upload);
        vLayout.setComponentAlignment(upload, Alignment.TOP_CENTER);
        vLayout.addComponent(drop);
        vLayout.setComponentAlignment(drop, Alignment.TOP_CENTER);       
        hLayout1.addComponent(easy);
        hLayout1.addComponent(medium);
        hLayout1.addComponent(hard);
        hLayout1.setMargin(true);
        hLayout1.setSpacing(true);
        vLayout.addComponent(hLayout1);
        vLayout.addComponent(solveButton);
        vLayout.setComponentAlignment(hLayout1, Alignment.BOTTOM_CENTER);
        vLayout.setComponentAlignment(solveButton, Alignment.BOTTOM_CENTER);
        vLayout.setComponentAlignment(hLayout1, Alignment.BOTTOM_CENTER);

        hLayout2.addComponent(panel2);
        hLayout2.addComponent(panel);
        vLayout1.addComponent(undo);
        vLayout1.setComponentAlignment(undo, Alignment.BOTTOM_CENTER);
        vLayout1.addComponent(check);
        vLayout1.addComponent(reset);
        vLayout1.setMargin(true);
        vLayout1.setSpacing(true);
        hLayout2.addComponent(vLayout1);
        hLayout2.setMargin(true);
        hLayout2.setSpacing(true);
        vLayout.setMargin(true);
        vLayout.setSpacing(true);
        vLayout.addComponent(hLayout2);
        vLayout.setComponentAlignment(hLayout2, Alignment.MIDDLE_CENTER);
        setContent(vLayout);

        upload.addFinishedListener(uploadReceiver);
        
    }
}
