package org.tellervo.desktop.gui.widgets;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import net.miginfocom.swing.MigLayout;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.desktop.setupwizard.SetupWizard;
import org.tellervo.desktop.ui.Builder;

public class AbstractWizardDialog extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;

	protected JFrame parent;
	protected ArrayList<AbstractWizardPanel> pages;
	private ArrayList<Class<? extends AbstractWizardPanel>> ignoredPages = new ArrayList<Class<? extends AbstractWizardPanel>>();
	private final static Logger log = LoggerFactory
			.getLogger(SetupWizard.class);

	private Integer currPageIndex = 0;

	private JPanel mainPanel;
	private JButton btnClose;
	private JButton btnPrevious;
	private JButton btnNext;
	private JLabel lblTitle;
	private JLabel lblSideImage;
	private JPanel pagePanel;
	private JTextArea txtInstructions;
	private final String title;
	private final Icon sideImage;

	protected enum Direction {
		FORWARD, BACKWARD;
	}

	public AbstractWizardDialog(String title, Icon sideImage) {

		this.title = title;
		this.sideImage = sideImage;
	}
	
	public AbstractWizardDialog(JFrame parent,
			ArrayList<AbstractWizardPanel> pages, String title, Icon sideImage) {

		this.title = title;
		this.sideImage = sideImage;
		this.pages = pages;
		this.parent = parent;
		setupGui();
	}

	protected void setupGui() {
		setModal(true);
		setTitle(title);
		setIconImage(Builder.getApplicationIcon());

		mainPanel = new JPanel();
		mainPanel.setBackground(Color.WHITE);
		mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		getContentPane().add(mainPanel, BorderLayout.CENTER);
		mainPanel.setLayout(new MigLayout("", "[597.00,grow,fill]",
				"[][70px:n,fill][40.00px,grow,fill]"));

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
		mainPanel.add(txtInstructions, "cell 0 1,growx,wmin 10");

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
		buttonPanel
				.setLayout(new MigLayout(
						"",
						"[grow][90px:90px:90px,fill][66px,fill][17.00][90px:90px:90px]",
						"[25px]"));
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
		leftPanel.setBackground(new Color(59, 118, 163));
		getContentPane().add(leftPanel, BorderLayout.WEST);

		lblSideImage = new JLabel("");

		lblSideImage.setIcon(sideImage);
		leftPanel.add(lblSideImage);

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				cleanup();
			}
		});

		autoEnableNavButtons();
		this.setSize(new Dimension(771, 542));
		this.setLocationRelativeTo(parent);
		
		showPage(0, Direction.FORWARD);
	}

	/**
	 * Show the specified wizard page
	 * 
	 * @param index
	 */
	private void showPage(Integer index, Direction dir) {
		AbstractWizardPanel page;
		try {
			page = pages.get(index);
		} catch (Exception e) {
			log.error("Error setting wizard page to index: " + index);
			e.printStackTrace();
			return;
		}

		// Check to see if page should be ignored
		if (isIgnoredPageClass(page)) {
			if (dir.equals(Direction.FORWARD)) {
				index++;
			} else {
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

		page.initialViewTasks();

		pagePanel.repaint();

	}

	private Boolean isIgnoredPageClass(AbstractWizardPanel page) {
		for (Class<? extends AbstractWizardPanel> clazz : ignoredPages) {
			if (page.getClass().equals(clazz)) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Automatically enabled/disable navigation buttons based on the pages and
	 * location.
	 */
	private void autoEnableNavButtons() {
		if (currPageIndex.equals(pages.size() - 1)) {
			btnNext.setText("Finish");
		} else {
			btnNext.setText("Next >");
		}

		btnPrevious.setEnabled(!currPageIndex.equals(0));
	}

	private void cleanup() {
		for (AbstractWizardPanel page : pages) {
			page.finish();
		}

		dispose();
	}

	/**
	 * Show this next page in the wizard
	 */
	private void showNextPage() {
		if (currPageIndex.equals(pages.size() - 1)) {
			// Last page so dispose

			cleanup();
			return;
		}

		AbstractWizardPanel page = pages.get(currPageIndex);

		// Remove page from ignored list if requested
		if (page.getEnablePageClassArray().size() > 0) {
			for (Class<? extends AbstractWizardPanel> ignorePage : page
					.getEnablePageClassArray()) {
				ignoredPages.remove(ignorePage);
			}
		}

		// Add page to ignored list if requested
		if (page.getDisablePageClassArray().size() > 0) {
			for (Class<? extends AbstractWizardPanel> ignorePage : page
					.getDisablePageClassArray()) {
				if (!ignoredPages.contains(ignorePage)) {
					ignoredPages.add(ignorePage);
				}
			}
		}

		showPage(currPageIndex + 1, Direction.FORWARD);

	}

	/**
	 * Show the previous page in the wizard
	 */
	private void showPreviousPage() {
		showPage(currPageIndex - 1, Direction.BACKWARD);

	}

	@Override
	public void actionPerformed(ActionEvent evt) {
		if (evt.getActionCommand().equals("next")) {
			showNextPage();
		} else if (evt.getActionCommand().equals("previous")) {
			showPreviousPage();
		} else if (evt.getActionCommand().equals("close")) {
			cleanup();
		}

	}

}
