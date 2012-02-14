package edu.cornell.dendro.corina.setupwizard;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;

import edu.cornell.dendro.corina.core.App;
import edu.cornell.dendro.corina.gui.Bug;
import edu.cornell.dendro.corina.gui.LoginSplash;
import edu.cornell.dendro.corina.gui.ProgressMeter;
import edu.cornell.dendro.corina.gui.XCorina;
import edu.cornell.dendro.corina.platform.Platform;
import edu.cornell.dendro.corina.ui.Builder;
import net.miginfocom.swing.MigLayout;
import java.awt.Font;
import java.awt.Color;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SetupWizard extends JDialog implements ActionListener{
	
	private static final long serialVersionUID = 1L;

	private JFrame parent;
	private ArrayList<AbstractWizardPanel> pages;
	private ArrayList<Class <? extends AbstractWizardPanel>> ignoredPages = new ArrayList<Class <? extends AbstractWizardPanel >>();
	private final static Logger log = LoggerFactory.getLogger(SetupWizard.class);

	private Integer currPageIndex = 0;
	
	
	private JPanel mainPanel;
	private JButton btnClose;
	private JButton btnPrevious;
	private JButton btnNext;
	private JLabel lblTitle;
	private JLabel lblSideImage;
	private JPanel pagePanel;
	private JTextArea txtInstructions;
	
	private enum Direction
	{
		FORWARD,
		BACKWARD;
	}
	
	
	
	
	public SetupWizard(JFrame parent, ArrayList<AbstractWizardPanel> pages) {
		
		this.pages = pages;
		this.parent = parent;
		
		setupGui();
		showPage(0, Direction.FORWARD);
		
	}
	
	public static void launchWizard()
	{
		App.init();

		ArrayList<AbstractWizardPanel> pages = new ArrayList<AbstractWizardPanel>();
		pages.add(new WizardWelcome());
		pages.add(new WizardProxy());
		pages.add(new WizardServer());
		pages.add(new WizardHardwareAsk());
		pages.add(new WizardHardwareDo());
		pages.add(new WizardFinish());
		
		JDialog dialog = new SetupWizard(null, pages);
		
		dialog.setVisible(true);
		
	}
	
	public static void launchFirstRunWizard()
	{
		App.init();

		ArrayList<AbstractWizardPanel> pages = new ArrayList<AbstractWizardPanel>();
		pages.add(new WizardFirstRunWelcome());
		pages.add(new WizardProxy());
		pages.add(new WizardServer());
		pages.add(new WizardHardwareAsk());
		pages.add(new WizardHardwareDo());
		pages.add(new WizardFinish());
		
		JDialog dialog = new SetupWizard(null, pages);
		
		dialog.setVisible(true);
	}
	
	private void setupGui()
	{
		setModal(true);
		setTitle("Setup wizard");
		setIconImage(Builder.getApplicationIcon());
		
		mainPanel = new JPanel();
		mainPanel.setBackground(Color.WHITE);
		mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		getContentPane().add(mainPanel, BorderLayout.CENTER);
		mainPanel.setLayout(new MigLayout("", "[620px,fill]", "[][70px:n,fill][40.00px,grow,fill]"));
		
		lblTitle = new JLabel("New label");
		mainPanel.add(lblTitle, "cell 0 0,growx,aligny top");
		lblTitle.setFont(new Font("Dialog", Font.BOLD, 17));
		
		txtInstructions = new JTextArea();
		txtInstructions.setBackground(null);
		txtInstructions.setFont(new Font("Dialog", Font.PLAIN, 12));
		txtInstructions.setEditable(false);
		txtInstructions.setWrapStyleWord(true);
		txtInstructions.setLineWrap(true);
		txtInstructions.setBorder(new EmptyBorder(5, 5, 5, 5));
		mainPanel.add(txtInstructions, "cell 0 1");
		
		pagePanel = new JPanel();
		pagePanel.setBorder(new EmptyBorder(5, 5, 0, 5));
		pagePanel.setBackground(Color.WHITE);
		mainPanel.add(pagePanel, "cell 0 2,growx,aligny top");
		pagePanel.setLayout(new BorderLayout(5, 5));
		
		JPanel buttonPanel = new JPanel();
		getContentPane().add(buttonPanel, BorderLayout.SOUTH);
		
		btnPrevious = new JButton("< Back");
		btnPrevious.setActionCommand("previous");
		btnPrevious.addActionListener(this);
		buttonPanel.setLayout(new MigLayout("", "[grow][90px:90px:90px,fill][66px,fill][17.00][90px:90px:90px]", "[25px]"));
		buttonPanel.add(btnPrevious, "cell 1 0,alignx left,aligny top");
		
		btnNext = new JButton("Next >");
		btnNext.setActionCommand("next");
		btnNext.addActionListener(this);
		buttonPanel.add(btnNext, "cell 2 0,alignx left,aligny top");
		
		btnClose = new JButton("Cancel");
		btnClose.setActionCommand("close");
		btnClose.addActionListener(this);
		buttonPanel.add(btnClose, "cell 4 0,growx,aligny top");
		
		JPanel leftPanel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) leftPanel.getLayout();
		flowLayout.setVgap(0);
		flowLayout.setHgap(0);
		leftPanel.setBorder(new EmptyBorder(0, 0, 0, 0));
		leftPanel.setBackground(Color.WHITE);
		getContentPane().add(leftPanel, BorderLayout.WEST);
		
		lblSideImage = new JLabel("");
	
		lblSideImage.setIcon(Builder.getImageAsIcon("sidebar.png"));
		leftPanel.add(lblSideImage);
		
		
		autoEnableNavButtons();
		this.setSize(new Dimension(772, 479));
		this.setLocationRelativeTo(parent);
	}
	
	/**
	 * Show the specified wizard page 
	 * 
	 * @param index
	 */
	private void showPage(Integer index, Direction dir)
	{
		AbstractWizardPanel page;
		try{
			page = pages.get(index);
		} catch (Exception e)
		{
			log.error("Error setting wizard page to index: "+index);
			e.printStackTrace();
			return;
		}
		
		// Check to see if page should be ignored
		if(isIgnoredPageClass(page))
		{
			if(dir.equals(Direction.FORWARD))
			{
				index++;
			}
			else
			{
				index--;
			}
			
			showPage(index, dir);
			return;
		}

		pagePanel.removeAll();
		pagePanel.add(page);
		lblTitle.setText(page.getTitle());
		txtInstructions.setText(page.getInstructions());
		currPageIndex = index;
		autoEnableNavButtons();
		
		pagePanel.repaint();

	}
	
	private Boolean isIgnoredPageClass(AbstractWizardPanel page)
	{
		for(Class<? extends AbstractWizardPanel> clazz : ignoredPages )
		{
			if(page.getClass().equals(clazz))
			{
				return true;
			}
		}
		
		return false;
	}
	

	
	/**
	 * Automatically enabled/disable navigation buttons
	 * based on the pages and location.
	 */
	private void autoEnableNavButtons()
	{		
		if(currPageIndex.equals(pages.size()-1))
		{
			btnNext.setText("Finish");
		}
		else
		{
			btnNext.setText("Next >");
		}
		
		btnPrevious.setEnabled(!currPageIndex.equals(0));
	}
	
	/**
	 * Show this next page in the wizard
	 */
	private void showNextPage()
	{
		if(currPageIndex.equals(pages.size()-1))
		{
			// Last page so dispose
			dispose();
			return;
		}
		
		AbstractWizardPanel page = pages.get(currPageIndex);
				
		// Remove page from ignored list if requested
		if(page.getEnablePageClass()!=null)
		{
			if(ignoredPages.contains(page.getEnablePageClass()))
			{
					ignoredPages.remove(page.getEnablePageClass());
			}
		}
		
		// Add page to ignored list if requested
		if(page.getDisablePageClass()!=null)
		{
			if(!ignoredPages.contains(page.getDisablePageClass()))
			{
				ignoredPages.add(page.getDisablePageClass());
			}
		}


		showPage(currPageIndex+1, Direction.FORWARD);

	}
	
	/**
	 * Show the previous page in the wizard
	 */
	private void showPreviousPage()
	{
		showPage(currPageIndex-1, Direction.BACKWARD);
		
	}

	@Override
	public void actionPerformed(ActionEvent evt) {
		if(evt.getActionCommand().equals("next"))
		{
			showNextPage();
		}
		else if (evt.getActionCommand().equals("previous"))
		{
			showPreviousPage();
		}
		else if (evt.getActionCommand().equals("close"))
		{
			dispose();
		}
		
	}

}
