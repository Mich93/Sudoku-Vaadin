package com.example.sudoku;

import com.vaadin.event.dd.DragAndDropEvent;
import com.vaadin.event.dd.DropHandler;
import com.vaadin.event.dd.acceptcriteria.AcceptAll;
import com.vaadin.event.dd.acceptcriteria.AcceptCriterion;
import com.vaadin.server.StreamResource;
import com.vaadin.server.StreamResource.StreamSource;
import com.vaadin.server.StreamVariable;
import com.vaadin.ui.Component;
import com.vaadin.ui.DragAndDropWrapper;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Html5File;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

/**
 *
 * @author stefano
 */
public class DragAndDropHandler extends DragAndDropWrapper implements DropHandler {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private static final long FILE_SIZE_LIMIT = 2 * 1024 * 1024; // 2MB

    private Board board;
    private GridLayout grid;

    public DragAndDropHandler(final Component root) {
        super(root);
        setDropHandler(this);
    }

    public DragAndDropHandler(Component root, Board board,
            GridLayout grid) {
        super(root);
        setDropHandler(this);
        this.board = board;
        this.grid = grid;
    }

    @Override
    public void drop(final DragAndDropEvent dropEvent) {

// expecting this to be an html5 drag
        final WrapperTransferable tr = (WrapperTransferable) dropEvent
                .getTransferable();
        final Html5File[] files = tr.getFiles();
        if (files != null) {
            for (final Html5File html5File : files) {
                final String fileName = html5File.getFileName();

                if (html5File.getFileSize() > FILE_SIZE_LIMIT) {
                    Notification
                            .show("File rejected. Max 2Mb files are accepted by Sampler",
                                    Notification.Type.WARNING_MESSAGE);
                } else {

                    final ByteArrayOutputStream bas = new ByteArrayOutputStream();
                    final StreamVariable streamVariable = new StreamVariable() {

                        @Override
                        public OutputStream getOutputStream() {
                            return bas;
                        }

                        @Override
                        public boolean listenProgress() {
                            return false;
                        }

                        @Override
                        public void onProgress(
                                final StreamingProgressEvent event) {
                        }

                        @Override
                        public void streamingStarted(
                                final StreamingStartEvent event) {
                        }

                        @Override
                        public void streamingFinished(
                                final StreamingEndEvent event) {

                            showFile(fileName, html5File.getType(), bas);

// UploadReceiver
                            try {
                                FileInputStream fis = new FileInputStream(
                                        "sudoinput.txt");
                                InputStreamReader isr = new InputStreamReader(
                                        fis);
                                BufferedReader br = new BufferedReader(isr);
                                String sCurrentLine;

                                int row = 0;
                                int col = 0;
                                while ((sCurrentLine = br.readLine()) != null) {

                                    String[] field = sCurrentLine.split(" ");

                                    col = 0;
                                    for (String s : field) {
// set to ReadOnly to false to allow for
// update
                                        board.setReadOnly(col, row, false);

                                        board.setValue(col, row, s,
                                                s.equals("0") ? false : true);

                                        col++;
                                    }
                                    row++;
                                }

// update the tiles for display
                                for (col = 0; col < 9; col++) {
                                    for (row = 0; row < 9; row++) {
                                        DragAndDropWrapper wrapper = (DragAndDropWrapper) grid
                                                .getComponent(col, row);
                                        DragAndDrop handler = (DragAndDrop) wrapper
                                                .getDropHandler();
                                        handler.getLabel()
                                                .setPropertyDataSource(
                                                        board.getCellElement(
                                                                col, row));
                                    }
                                }
                                System.out.println(board);
                                Notification.show("You imported the "+fileName+" Sudoku file",Type.WARNING_MESSAGE);

                            } catch (FileNotFoundException e) {
// TODO Auto-generated catch block
                                e.printStackTrace();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
// --------------------
                        }

                        @Override
                        public void streamingFailed(
                                final StreamingErrorEvent event) {

                        }

                        @Override
                        public boolean isInterrupted() {
                            return false;
                        }
                    };
                    html5File.setStreamVariable(streamVariable);

                }
            }

        } else {
            final String text = tr.getText();
            if (text != null) {
                showText(text);
            }
        }
    }

    private void showText(final String text) {
        showComponent(new Label(text), "Wrapped text content");
    }

    private void showFile(final String name, final String type,
            final ByteArrayOutputStream bas) {
// resource for serving the file contents
        final StreamSource streamSource = new StreamSource() {
            @Override
            public InputStream getStream() {
                if (bas != null) {
                    final byte[] byteArray = bas.toByteArray();
                    return new ByteArrayInputStream(byteArray);
                }
                return null;
            }
        };
        final StreamResource resource = new StreamResource(streamSource, name);

// show the file contents - images only for now
        final Embedded embedded = new Embedded(name, resource);
  //      showComponent(embedded, name);
    }

    private void showComponent(final Component c, final String name) {
        final VerticalLayout layout = new VerticalLayout();
        layout.setSizeUndefined();
        layout.setMargin(true);
        final Window w = new Window(name, layout);
        w.addStyleName("dropdisplaywindow");
        w.setSizeUndefined();
        w.setResizable(false);
        c.setSizeUndefined();
        layout.addComponent(c);
        UI.getCurrent().addWindow(w);

    }

    @Override
    public AcceptCriterion getAcceptCriterion() {
        return AcceptAll.get();
    }
}
