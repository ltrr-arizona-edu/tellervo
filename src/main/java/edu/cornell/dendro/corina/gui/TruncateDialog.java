package edu.cornell.dendro.corina.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

@SuppressWarnings("serial")
public class TruncateDialog extends JPanel {

    private JLabel lowerLabel = new JLabel();
    private JLabel lowerValueLabel = new JLabel();
    private JLabel upperLabel = new JLabel();
    private JLabel upperValueLabel = new JLabel();
    private RangeSlider rangeSlider = new RangeSlider();

    public TruncateDialog() {
        setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
        setLayout(new GridBagLayout());
        
        lowerLabel.setText("Beginning in year:");
        upperLabel.setText("Ending in year:");
        lowerValueLabel.setHorizontalAlignment(JLabel.LEFT);
        upperValueLabel.setHorizontalAlignment(JLabel.LEFT);
        
        rangeSlider.setPreferredSize(new Dimension(440, rangeSlider.getPreferredSize().height));
        rangeSlider.setMinimum(1560);
        rangeSlider.setMaximum(1985);
        
        // Add listener to update display.
        rangeSlider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                RangeSlider slider = (RangeSlider) e.getSource();
                lowerValueLabel.setText(String.valueOf(slider.getValue()));
                upperValueLabel.setText(String.valueOf(slider.getUpperValue()));
            }
        });

        add(lowerLabel     , new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
            GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 0, 3, 3), 0, 0));
        add(lowerValueLabel, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
            GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 0, 3, 0), 0, 0));
        add(upperLabel     , new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
            GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 0, 3, 3), 0, 0));
        add(upperValueLabel, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
            GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 0, 6, 0), 0, 0));
        add(rangeSlider    , new GridBagConstraints(0, 2, 2, 1, 0.0, 0.0,
            GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    }
    
    public void display() {
        // Initialize values.
        rangeSlider.setValue(1560);
        rangeSlider.setUpperValue(1985);
        
        // Initialize value display.
        lowerValueLabel.setText(String.valueOf(rangeSlider.getValue()));
        upperValueLabel.setText(String.valueOf(rangeSlider.getUpperValue()));
        
        // Create window frame.
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setTitle("Range Slider Demo");
        
        // Set window content and validate.
        frame.getContentPane().setLayout(new BorderLayout());
        frame.getContentPane().add(this, BorderLayout.CENTER);
        frame.pack();
        
        // Set window location and display.
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
    
}
