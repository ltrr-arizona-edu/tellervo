// Copyright (c) 2005 Aaron Hamid.  All rights reserved.
// See license in COPYING.txt distributed with this file and available online at http://www.gnu.org/licenses/gpl.txt
// Created on Mar 28, 2005

package vectormap;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import javax.swing.AbstractListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JSplitPane;
import javax.swing.JViewport;
import javax.swing.SpinnerNumberModel;
import javax.swing.WindowConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import corina.core.App;
import corina.map.SiteInfoDialog;
import corina.site.Site;
import corina.site.SiteDB;

/**
 * Atlas
 * @author Aaron Hamid (aaron at users dot sf dot net)
 */
public class Atlas {
  private static final PrintStream realOut = System.out;
  private static final PrintStream realErr = System.err;

  public static void main(String[] args) throws Exception {
    App.init(null);

    VectorMap map = VectorMap.load(args[0]);

    final JFrame frame = new JFrame("Atlas");
    frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    frame.addWindowListener(new WindowAdapter() {
      public void windowClosed(WindowEvent we) {
        realErr.println("SDSDFSDDFFD");
        realErr.println(we);
        try {
          SiteDB.getSiteDB().saveDB();
        } catch (IOException ioe) {
          JOptionPane.showMessageDialog(frame, "Error saving Site DB", "Error", JOptionPane.ERROR_MESSAGE);
        }
        System.exit(0);
      }
    });
    JLabel siteDbLabel = new JLabel("Site DB: " + App.prefs.getPref("corina.dir.data") + File.separator + "Site DB");
    final VectorMapPanel vectorMapPanel = new VectorMapPanel(map);
    final JScrollPane mapScrollPane = new JScrollPane(vectorMapPanel);
    mapScrollPane.getViewport().setBackground(Color.white);
    //sp.getViewport().setScrollMode(JViewport.BACKINGSTORE_SCROLL_MODE);
    switch (mapScrollPane.getViewport().getScrollMode()) {
      case JViewport.BACKINGSTORE_SCROLL_MODE:
        System.out.println("BACKINGSTORE_SCROLL_MODE");
        break;
      case JViewport.BLIT_SCROLL_MODE:
        System.out.println("BLIT_SCROLL_MODE");
        break;
      case JViewport.SIMPLE_SCROLL_MODE:
        System.out.println("SIMPLE_SCROLL_MODE");
        break;
    }
    mapScrollPane.getViewport().setScrollMode(JViewport.BLIT_SCROLL_MODE);

    JSplitPane mainSplitPane = new JSplitPane();
    mainSplitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
    final JList list = new JList(new AbstractListModel() {
      public Object getElementAt(int i) {
        return SiteDB.getSiteDB().sites.get(i);
      }
      public int getSize() {
        return SiteDB.getSiteDB().sites.size();
      }
    });
    final JButton edit = new JButton("Edit");
    edit.setEnabled(false);
    list.addListSelectionListener(new ListSelectionListener() {
      public void valueChanged(ListSelectionEvent e) {
        if (e.getValueIsAdjusting()) return;
        Site s = (Site) list.getSelectedValue();
        edit.setEnabled(s != null);
      }
    });

    Container c = new Container();
    c.setLayout(new BorderLayout());
    c.add(new JScrollPane(list), BorderLayout.CENTER);
    edit.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent ae) {
        Site s = (Site) list.getSelectedValue();
        new SiteInfoDialog(s, frame);
      }
    });
    c.add(edit, BorderLayout.SOUTH);
    mainSplitPane.setLeftComponent(c);

    JSpinner spinner = new JSpinner();
    spinner.setModel(new SpinnerNumberModel(new Float(1.0), new Float(1.0), new Float(30.0), new Float(0.5))); 
    spinner.setValue(new Float(1.0f));
    c = new Container();
    c.setLayout(new BorderLayout());
    c.add(mapScrollPane, BorderLayout.CENTER);
    c.add(spinner, BorderLayout.SOUTH);
    mainSplitPane.setRightComponent(c);

    spinner.addChangeListener(new ChangeListener() {
      public void stateChanged(ChangeEvent ce) {
        vectorMapPanel.setZoom(((Number) ((JSpinner) ce.getSource()).getValue()).floatValue());
        /*EventQueue.invokeLater(new Runnable() {
          public void run() {
            JScrollBar sb = sp.getHorizontalScrollBar();
            sb.setBlockIncrement((int) (sb.getVisibleAmount() * (3.0/5.0)));
            sb.setUnitIncrement(sb.getVisibleAmount() / 10);
            sb = sp.getVerticalScrollBar();
            sb.setBlockIncrement((int) (sb.getVisibleAmount() * (3.0/5.0)));
            sb.setUnitIncrement(sb.getVisibleAmount() / 10);
          }
        });*/
      }
    });
    ComponentListener listener = new ComponentAdapter() {
      public void componentResized(ComponentEvent e) {
        JScrollBar sb = mapScrollPane.getHorizontalScrollBar();
        sb.setBlockIncrement((int) (sb.getVisibleAmount() * (3.0/5.0)));
        sb.setUnitIncrement(sb.getVisibleAmount() / 10);
        sb = mapScrollPane.getVerticalScrollBar();
        sb.setBlockIncrement((int) (sb.getVisibleAmount() * (3.0/5.0)));
        sb.setUnitIncrement(sb.getVisibleAmount() / 10);
      }
    }; 
    vectorMapPanel.addComponentListener(listener);
    mapScrollPane.addComponentListener(listener);

    frame.getContentPane().add(siteDbLabel, BorderLayout.NORTH);
    frame.getContentPane().add(mainSplitPane, BorderLayout.CENTER);

    frame.pack();
    /*JScrollBar sb = sp.getHorizontalScrollBar();
    sb.setBlockIncrement((int) (sb.getVisibleAmount() * (3.0/5.0)));
    sb.setUnitIncrement(sb.getVisibleAmount() / 10);
    sb = sp.getVerticalScrollBar();
    sb.setBlockIncrement((int) (sb.getVisibleAmount() * (3.0/5.0)));
    sb.setUnitIncrement(sb.getVisibleAmount() / 10);*/
    frame.show();
  }
}