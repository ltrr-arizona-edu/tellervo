// Copyright (c) 2005 Aaron Hamid.  All rights reserved.
// See license in COPYING.txt distributed with this file and available online at http://www.gnu.org/licenses/gpl.txt
// Created on Mar 28, 2005

package vectormap;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.ListModel;

import corina.site.Site;

/**
 * Panel containing Site information
 * @author Aaron Hamid (aaron at users dot sf dot net)
 */
public class SitePanel extends JComponent {
  private Site site;
  private ListModel model;
  public SitePanel(Site site, ListModel model) {
    this.site = site;
    this.model = model;
    System.out.println("isDoubleBuffered: " + isDoubleBuffered());
    setLayout(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();
    
    JLabel altitudeLabel = new JLabel("Altitude:");
    
    add(new JLabel("hi"));
  }
}
