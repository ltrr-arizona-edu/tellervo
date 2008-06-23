/*
 * ImportWizard.java
 *
 * Created on June 10, 2008, 11:37 AM
 */

package edu.cornell.dendro.corina.gui.newui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import edu.cornell.dendro.corina.site.GenericIntermediateObject;
import edu.cornell.dendro.corina.site.Radius;
import edu.cornell.dendro.corina.site.Site;
import edu.cornell.dendro.corina.site.Specimen;
import edu.cornell.dendro.corina.site.Subsite;
import edu.cornell.dendro.corina.site.Tree;
import edu.cornell.dendro.corina.util.Center;

/**
 *
 * @author  peterbrewer
 */
public class ImportWizard extends javax.swing.JDialog implements WizardPanelParent {
	private CardLayout cardLayout;
	private JPanel insidePanel;
	private Dimension origDimension;
	private JScrollPane insidePanelScroller;
	private BaseContentPanel<?>[] cards;
	private int cardIdx;
    
    /** Creates new form ImportWizard */
    public ImportWizard(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        
        cardLayout = new CardLayout();
        insidePanel = new JPanel(cardLayout);

        /***************************/
        cards = new BaseContentPanel<?>[5];
        
        cards[0] = new BaseContentPanel<Site>(this, Site.class);
        cards[1] = new BaseContentPanel<Subsite>(this, Subsite.class);
        cards[2] = new BaseContentPanel<Tree>(this, Tree.class);
        cards[3] = new BaseContentPanel<Specimen>(this, Specimen.class);
        cards[4] = new BaseContentPanel<Radius>(this, Radius.class);
//        cards[4] = new BaseContentPanel<Subsite>(this, Radius.class);
        
        // populate the site list
        cards[0].repopulate();
        
        cardIdx = 0;
        addPanel(cards[0]);
        addPanel(cards[1]);
        addPanel(cards[2]);
        addPanel(cards[3]);
        addPanel(cards[4]);
        
        // Set instructions and step number on GUI
        setInstructionsForStep(1, (cards.length-1));
        /********************************/

        // add a scroll pane around the inside container, in case it gets too big to display vertically
        /*
        insidePanelScroller = new JScrollPane(insidePanel, 
        		JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, 
        		JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        
        insidePanelScroller.setPreferredSize(new Dimension(1234, 200));
        */
                     
//        panelContent.setLayout(new BorderLayout());
 //       panelContent.add(insidePanel, BorderLayout.CENTER);

        panelContent.setLayout(new FlowLayout());
        panelContent.add(insidePanel);
        
		// make sure we can go 'next' on our initial state
    	btnNext.setEnabled(cards[cardIdx].isPanelValid());
        
        btnNext.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {		

				// did the card say it was ok to continue?
				if(cards[cardIdx].verifyAndSelectNextPanel()) {
					GenericIntermediateObject newParent = cards[cardIdx].getPanelObject(); // the selected object of this box...
					
					// NEXT!
					cardIdx++;
					setInstructionsForStep((cardIdx+1), (cards.length-1));
					
					// did we finish?
					if(cardIdx == cards.length) {
						System.out.println("WOOT!!!");
						dispose();
						return;
					}
					
					// pass the parent on to the child (which should populate itself)
					cards[cardIdx].setParentObject(newParent);
					
					// determine our prefix up to this point
					StringBuffer prefix = new StringBuffer();
					for(int i = 0; i < cardIdx; i++) {
						String cardPrefix = cards[i].getPrefix();
						
						// special handing for subsite...
						if(cards[i].getContentClass().equals(Subsite.class)) {
							if(cardPrefix.equalsIgnoreCase("main"))
								cardPrefix = "";
							else
								cardPrefix = "/" + cardPrefix;
						}
												
						// special handling for site...
						if(cards[i].getContentClass().equals(Site.class)) {
							prefix.append("C-");
							prefix.append(cardPrefix);
						} else {
							// normal
							prefix.append(cardPrefix);
							prefix.append("-");
						}
					}
					
					// set the parent prefix of the next card
					cards[cardIdx].setParentPrefix(prefix.toString());
					
					// advance us forward
					cardLayout.next(insidePanel);
					
					// make sure we can go back!
					btnBack.setEnabled(true);

					// make sure we can go 'next'
			    	btnNext.setEnabled(cards[cardIdx].isPanelValid());
					
					// change the button text if we're at the end
					if(cardIdx == cards.length - 1)
						btnNext.setText("Finish");
				}
				
			}
        });

        btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				setInstructionsForStep(cardIdx, (cards.length-1));
				
				if(cardIdx > 0) {
					// change the button text if we're at the end
					if(cardIdx == cards.length - 1)
						btnNext.setText("Finish");
					cardIdx--;
					
					// go back!
					cardLayout.previous(insidePanel);

					// can go 'next'? (this should always be true?)
			    	btnNext.setEnabled(cards[cardIdx].isPanelValid());
					
					// disable the back button on the first card
					if(cardIdx == 0)
						btnBack.setEnabled(false);
				}
			}
        });
        
        // noooo! after all we've worked for...
        btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
        });        
        
        pack();
        Center.center(this);
    }

    private void setInstructionsForStep(int currentStep, int totalSteps){
    	
    	// Set 'Step X of X' label in button bar
    	lblProgress.setText("Step " + currentStep + " of " + totalSteps);
    	
    	// Set instructions panel 
    	switch(currentStep){
    	case 1: panelInstructions.setText("Welcome to the Corina data import wizard which will " +
    			"help you create or import data into the Corina database.\n\n"+
    			"To begin, please select an existing site from which your sample is from or if " +
    			"the site is not already in the database, create a new one.\n\n"); 
    			break;
    	case 2: panelInstructions.setText("Next, select the sub site of your site where your sample is from. " +
    			"If the site is not divided into sub sites then continue with the default 'Main' " +
    			"sub site.  Alternatively, you can create a new sub site using the form below.");
    			break;
    	case 3: panelInstructions.setText("Now select the tree from which your sample is taken.  Again, if " +
    			"the tree does not exist in the database you can create it here.");
    			break;    			
    	case 4: panelInstructions.setText("Next, select or create a specimen record for your sample.  " +
    			"A specimen is either a core, section or piece of charcoal.  Multiple cores from the same " +
    			"tree for instance would each have a separate record associated with the same tree.");
    			break;    			
    	case 5: panelInstructions.setText("Finally, select or create a radius for your sample.  A radius " +
    			"is a particular line across the tree rings of your sample.  Whilst a core would typically " +
    			"only have a single radius, a section may have several radiating out in different directions " +
    			"from the pith.\n\n " +
    			"Once you have done this click finish to complete the wizard.");
    			break;       			
    	default: panelInstructions.setText("Default instructions");
    			break;
    	
    	}
    	
    
    };
    
    
    private int cardCount = 0;
    private void addPanel(JPanel panel) {
    	JScrollPane scrolly = new JScrollPane(panel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    	scrolly.setPreferredSize(panelContent.getPreferredSize());
        insidePanel.add(scrolly, "card" + cardCount);
        cardCount++;
    }
    
    // methods below are for our wizardpanelparent interface
    public Dimension getContainerPreferredSize() {
    	//return new Dimension(123, 123);
    	return panelContent.getPreferredSize();
    }
    
    public void notifyPanelStateChanged(BaseContentPanel<?> panel) {
    	// only care about our current panel (this shouldn't happen, anyway)
    	if(cards[cardIdx] != panel)
    		return;
    	
    	btnNext.setEnabled(panel.isPanelValid());
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelEyeCandy = new javax.swing.JPanel();
        panelButtons = new javax.swing.JPanel();
        btnCancel = new javax.swing.JButton();
        btnNext = new javax.swing.JButton();
        seperatorButtons = new javax.swing.JSeparator();
        lblProgress = new javax.swing.JLabel();
        btnBack = new javax.swing.JButton();
        splitPaneWizard = new javax.swing.JSplitPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        panelInstructions = new javax.swing.JTextPane();
        panelContent = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Import Wizard");

        panelEyeCandy.setBackground(new java.awt.Color(255, 255, 255));

        org.jdesktop.layout.GroupLayout panelEyeCandyLayout = new org.jdesktop.layout.GroupLayout(panelEyeCandy);
        panelEyeCandy.setLayout(panelEyeCandyLayout);
        panelEyeCandyLayout.setHorizontalGroup(
            panelEyeCandyLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 145, Short.MAX_VALUE)
        );
        panelEyeCandyLayout.setVerticalGroup(
            panelEyeCandyLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 401, Short.MAX_VALUE)
        );

        btnCancel.setText("Cancel");

        btnNext.setText("Next");
        btnNext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNextActionPerformed(evt);
            }
        });

        seperatorButtons.setBackground(new java.awt.Color(153, 153, 153));
        seperatorButtons.setOpaque(true);

        lblProgress.setText("Step 1 of 4");

        btnBack.setText("Back");
        btnBack.setEnabled(false);

        org.jdesktop.layout.GroupLayout panelButtonsLayout = new org.jdesktop.layout.GroupLayout(panelButtons);
        panelButtons.setLayout(panelButtonsLayout);
        panelButtonsLayout.setHorizontalGroup(
            panelButtonsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, panelButtonsLayout.createSequentialGroup()
                .addContainerGap()
                .add(lblProgress, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 320, Short.MAX_VALUE)
                .add(90, 90, 90)
                .add(btnBack)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(btnNext)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(btnCancel)
                .add(16, 16, 16))
            .add(org.jdesktop.layout.GroupLayout.TRAILING, seperatorButtons, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 683, Short.MAX_VALUE)
        );
        panelButtonsLayout.setVerticalGroup(
            panelButtonsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(panelButtonsLayout.createSequentialGroup()
                .add(seperatorButtons, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(panelButtonsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(btnCancel)
                    .add(lblProgress)
                    .add(btnNext)
                    .add(btnBack))
                .addContainerGap(27, Short.MAX_VALUE))
        );

        splitPaneWizard.setBorder(null);
        splitPaneWizard.setDividerLocation(130);
        splitPaneWizard.setDividerSize(11);
        splitPaneWizard.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

        panelInstructions.setBackground(new java.awt.Color(255, 255, 225));
        panelInstructions.setBorder(null);
        panelInstructions.setEditable(false);
        //panelInstructions.setText("Welcome to the Corina data import wizard!\n\nThis pain will contain instructions as well as suggested entries for forms based on the information we can gather from the file being imported.\n\nPlease start by selecting the file that you would like to import into Corina.");
        
        jScrollPane1.setViewportView(panelInstructions);

        splitPaneWizard.setLeftComponent(jScrollPane1);

        org.jdesktop.layout.GroupLayout panelContentLayout = new org.jdesktop.layout.GroupLayout(panelContent);
        panelContent.setLayout(panelContentLayout);
        panelContentLayout.setHorizontalGroup(
            panelContentLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 538, Short.MAX_VALUE)
        );
        panelContentLayout.setVerticalGroup(
            panelContentLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 262, Short.MAX_VALUE)
        );

        splitPaneWizard.setRightComponent(panelContent);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(panelEyeCandy, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(splitPaneWizard, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 538, Short.MAX_VALUE))
            .add(org.jdesktop.layout.GroupLayout.TRAILING, panelButtons, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(splitPaneWizard, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 401, Short.MAX_VALUE)
                    .add(panelEyeCandy, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(panelButtons, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnNextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNextActionPerformed
        // TODO add your handling code here:
}//GEN-LAST:event_btnNextActionPerformed
    
    /**
     * @param args the command line arguments
     */
    public static void mzzzain(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                ImportWizard dialog = new ImportWizard(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBack;
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnNext;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblProgress;
    private javax.swing.JPanel panelButtons;
    private javax.swing.JPanel panelContent;
    private javax.swing.JPanel panelEyeCandy;
    private javax.swing.JTextPane panelInstructions;
    private javax.swing.JSeparator seperatorButtons;
    private javax.swing.JSplitPane splitPaneWizard;
    // End of variables declaration//GEN-END:variables
    
}
