package corina.browser;

import corina.util.Platform;
import corina.ui.Builder;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.BorderFactory;
import java.awt.Color;

import java.awt.dnd.*;
import java.awt.datatransfer.*;

import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;

/*
 left to do:
 -- click on it, it opens the user's trash with the system browser (finder/explorer)
 -- what's right-click do?  menu?  (open trash, undo last move-to-trash, empty trash?)
 */

public class Trash extends JLabel implements DropTargetListener, MouseListener {
    public Trash() {
        setIcon(Builder.getIcon("Trash.png"));
        setText("Trash");
        setBorder(BorderFactory.createCompoundBorder(
                                                      BorderFactory.createLineBorder(Color.gray, 1),
                                                      BorderFactory.createEmptyBorder(3, 3, 3, 3)));

        // drop target
        DropTarget target = new DropTarget(this /*component*/, this /*droplistener*/);

        // click to open
        addMouseListener(this);
    }

//    "it's one thing to solve design problems; it's much, much harder to give a personality, to give life to a product.  it's much harder, but it's infinitely more satisfying." -- jonathon ive

    // click to open
    public void mouseClicked(MouseEvent e) {
        // double-click?
        if (e.getClickCount() == 2)
            Platform.open(Platform.getTrash());
    }
    public void mouseEntered(MouseEvent e) { }
    public void mouseExited(MouseEvent e) { }
    public void mousePressed(MouseEvent e) { }
    public void mouseReleased(MouseEvent e) { }

    // handle drops
    public void dragEnter(DropTargetDragEvent e) {
        e.acceptDrag(DnDConstants.ACTION_COPY);
    }
    public void dragOver(DropTargetDragEvent e) {
        setBorder(BorderFactory.createCompoundBorder(
                                                     BorderFactory.createLineBorder(Color.gray, 3),
                                                     BorderFactory.createEmptyBorder(1, 1, 1, 1)));
    }
    public void dragExit(DropTargetEvent e) {
        setBorder(BorderFactory.createCompoundBorder(
                                                     BorderFactory.createLineBorder(Color.gray, 1),
                                                     BorderFactory.createEmptyBorder(3, 3, 3, 3)));
    }
    public void dropActionChanged(DropTargetDragEvent e) { }
    public void drop(DropTargetDropEvent e) {
        try {
            Transferable transferable = e.getTransferable();

            // we accept only filelists
            if (transferable.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
                e.acceptDrop(DnDConstants.ACTION_MOVE);
                Object o = transferable.getTransferData(DataFlavor.javaFileListFlavor);
                List l = (List) o; // a List of Files

                // trash each one in turn
                for (int i=0; i<l.size(); i++)
                    trash((File) l.get(i));

                // done
                e.getDropTargetContext().dropComplete(true);
            } else {
                e.rejectDrop();
            }
        } catch (IOException ioe) {
            e.rejectDrop(); // handle error?
        } catch (UnsupportedFlavorException ufe) {
            e.rejectDrop(); // handle error?
        }
    }
    
    // (private: move file to trash)
    public static void trash(File f) { // throws IOE?
        File target = new File(Platform.getTrash(), f.getName());
        if (!target.exists()) {
            f.renameTo(target);
        } else {
            // "x" "x copy" "x copy 2" "x copy 3" ...
            target = new File(Platform.getTrash(), f.getName() + " copy");
            if (!target.exists()) {
                f.renameTo(target);
            } else { // this isn't really a special case...  refactor
                int x=2;
                for (;;) {
                    target = new File(Platform.getTrash(), f.getName() + " copy " + x);
                    if (!target.exists()) {
                        f.renameTo(target);
                        break;
                    }
                    x++;
                }
            }
        }
        // just renameTo?
        // (what if it's on a different fs?)
        // try renameTo(), if that fails, do a blockwise copy + delete
        // (test to make sure renameTo fails iff it's on a different filesystem)
    }
    // (error: what if it's open?  i should wait and try again, at least once -- no, i should see when, if ever, this happens, first)
    // error: what if user deletes AYA.SUM in a/, user deletes AYA.SUM in b/, trash better rename the second one (to something like "AYA.SUM - 2"), then how can restoring it unmangle the name?
    // mac os doesn't have a concept of "restore", so i guess i shouldn't worry about unmangling (ugh!)

    public static void main(String args[]) {
        javax.swing.JFrame f = new javax.swing.JFrame("trash");
        f.getContentPane().add(new Trash());
        f.pack();
        f.show();
    }
}
