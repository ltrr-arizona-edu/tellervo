package edu.cornell.dendro.corina.prefs;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JToggleButton;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;

import net.miginfocom.swing.MigLayout;

import com.l2fprod.common.swing.JButtonBar;
import com.l2fprod.common.swing.plaf.blue.BlueishButtonBarUI;

import edu.cornell.dendro.corina.prefs.panels.HardwarePrefsPanel;
import edu.cornell.dendro.corina.prefs.panels.NetworkPrefsPanel;
import edu.cornell.dendro.corina.prefs.panels.StatsPrefsPanel;
import edu.cornell.dendro.corina.ui.Builder;
import edu.cornell.dendro.corina.ui.I18n;

public class ModernPreferences extends JDialog {
	
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JButtonBar buttonBar;
	private JPanel toolbarPanel;
	private JLayeredPane mainPanel;
	private JLabel lblTitle;
	private JLabel lblSubtitle;
	private ButtonGroup pageButtons = new ButtonGroup();
	private JToggleButton hardwareButton;
	private HardwarePrefsPanel hardwarePrefsPanel;
	private JToggleButton networkButton;
	private NetworkPrefsPanel networkPrefsPanel;
	private JToggleButton statsButton;
	private StatsPrefsPanel statsPrefsPanel;
	private JToggleButton appearanceButton;
	private JToggleButton mappingButton;
	private JButton btnResetAllPreferences;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			ModernPreferences dialog = new ModernPreferences();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public ModernPreferences() {
		this.setIconImage(Builder.getApplicationIcon());
		this.setTitle("Preferences");
		
		setBounds(100, 100, 663, 550);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		{
			toolbarPanel = new JPanel();
			toolbarPanel.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
			toolbarPanel.setBackground(Color.WHITE);
			contentPanel.add(toolbarPanel, BorderLayout.WEST);
		}
		{
			JPanel panel = new JPanel();
			contentPanel.add(panel, BorderLayout.CENTER);
			panel.setLayout(new BorderLayout(10, 10));
			{
				mainPanel = new JLayeredPane();
				mainPanel.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
				panel.add(mainPanel);
			}

		}
		{
			JPanel titlePanel = new JPanel();
			titlePanel.setBackground(Color.WHITE);
			contentPanel.add(titlePanel, BorderLayout.NORTH);
			titlePanel.setLayout(new MigLayout("", "[65px,grow][]", "[15px][]"));
			{
				lblTitle = new JLabel("Corina Preferences");
				lblTitle.setFont(new Font("Dialog", Font.BOLD, 14));
				titlePanel.add(lblTitle, "cell 0 0,alignx left,aligny top");
			}
			{
				JPanel panel = new JPanel();
				panel.setBackground(Color.WHITE);
				titlePanel.add(panel, "cell 1 0 1 2,grow");
				{
					JLabel lblIcon = new JLabel();
					lblIcon.setIcon(Builder.getIcon("advancedsettings.png", 48));
					panel.add(lblIcon);
				}
			}
			{
				lblSubtitle = new JLabel("Select the preferences category to edit");
				lblSubtitle.setFont(new Font("Dialog", Font.PLAIN, 10));
				titlePanel.add(lblSubtitle, "cell 0 1,aligny top");
			}
		}
		{
			JPanel buttonPane = new JPanel();
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			buttonPane.setLayout(new MigLayout("", "[][54px,grow][81px]", "[3:3:3][25px]"));
			{
				JSeparator separator = new JSeparator();
				buttonPane.add(separator, "cell 0 0 3 1");
			}
			{
				btnResetAllPreferences = new JButton("Reset all to default");
				buttonPane.add(btnResetAllPreferences, "cell 0 1");
			}
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener(){

					@Override
					public void actionPerformed(ActionEvent e) {
						setVisible(false);						
					}
					
				});
				buttonPane.add(okButton, "cell 2 1,growx,aligny top");
				getRootPane().setDefaultButton(okButton);
			}
		}
		
		setupPages();
	}
	
	private void hideAllPages()
	{
		hardwarePrefsPanel.setVisible(false);
		networkPrefsPanel.setVisible(false);
		statsPrefsPanel.setVisible(false);
	}
	
	private void setupPages()
	{
		buttonBar = new JButtonBar();
		buttonBar.setOrientation(JButtonBar.VERTICAL);
		
		buttonBar.setUI(new BlueishButtonBarUI());
		buttonBar.setBorder(BorderFactory.createEmptyBorder(2, 4, 2, 4));
		
		
		// Hardware Page
		hardwareButton = new JToggleButton();
		hardwareButton.setFont(new Font("Dialog", Font.PLAIN, 10));
		hardwareButton.setText("Hardware");
		hardwareButton.setIcon(Builder.getIcon("hardware.png", 48));
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		hardwarePrefsPanel = new HardwarePrefsPanel();
		mainPanel.add(hardwarePrefsPanel);
		hardwareButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				hideAllPages();
				hardwarePrefsPanel.setVisible(true);
				lblTitle.setText(I18n.getText("preferences.hardware")+ " Preferences");
				lblSubtitle.setText("Set measuring platform and barcode scanner preferences");
				
			}
			
		});
		pageButtons.add(hardwareButton);
		buttonBar.add(hardwareButton);
		
		// Network Page
		networkButton = new JToggleButton();
		networkButton.setFont(new Font("Dialog", Font.PLAIN, 10));
		networkButton.setText(I18n.getText("preferences.network"));
		networkButton.setIcon(Builder.getIcon("networksettings.png", 48));
		networkPrefsPanel = new NetworkPrefsPanel();
		mainPanel.add(networkPrefsPanel);
		networkButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				hideAllPages();
				networkPrefsPanel.setVisible(true);
				lblTitle.setText(I18n.getText("preferences.network")+ " Preferences");
				lblSubtitle.setText("Webservice network connection preferences");
			}
			
		});
		pageButtons.add(networkButton);
		buttonBar.add(networkButton);
		
		// Stats Page
		statsButton = new JToggleButton();
		statsButton.setFont(new Font("Dialog", Font.PLAIN, 10));
		statsButton.setText(I18n.getText("preferences.statistics"));
		statsButton.setIcon(Builder.getIcon("chart.png", 48));
		statsPrefsPanel = new StatsPrefsPanel();
		mainPanel.add(statsPrefsPanel);
		statsButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				hideAllPages();
				statsPrefsPanel.setVisible(true);
				
			}
			
		});
		pageButtons.add(statsButton);
		buttonBar.add(statsButton);
		
		// Appearance Page
		appearanceButton = new JToggleButton();
		appearanceButton.setFont(new Font("Dialog", Font.PLAIN, 10));
		appearanceButton.setText(I18n.getText("preferences.appearance"));
		appearanceButton.setIcon(Builder.getIcon("appearance.png", 48));
		HardwarePrefsPanel hardwarePrefsPanel4 = new HardwarePrefsPanel();
		//mainPanel.add(hardwarePrefsPanel, 1);
		appearanceButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
		});
		pageButtons.add(appearanceButton);
		buttonBar.add(appearanceButton);
		
		// Mapping Page
		mappingButton = new JToggleButton();
		mappingButton.setFont(new Font("Dialog", Font.PLAIN, 10));
		mappingButton.setText(I18n.getText("preferences.mapping"));
		mappingButton.setIcon(Builder.getIcon("map.png", 48));
		HardwarePrefsPanel hardwarePrefsPanel5 = new HardwarePrefsPanel();
		//mainPanel.add(hardwarePrefsPanel, 1);
		mappingButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
		});
		pageButtons.add(mappingButton);
		buttonBar.add(mappingButton);
		
		
		
		// Add button bar to toolbar panel
		toolbarPanel.add(buttonBar);
		
		
		hardwareButton.doClick();
	}
	

	


	
}
	