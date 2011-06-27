/*******************************************************************************
 * Copyright (C) 2011 Peter Brewer.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contributors:
 *     Peter Brewer
 ******************************************************************************/
package edu.cornell.dendro.corina.gis;

import gov.nasa.worldwind.WorldWindow;
import gov.nasa.worldwind.exception.NoItemException;
import gov.nasa.worldwind.geom.Angle;
import gov.nasa.worldwind.geom.LatLon;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.geom.Sector;
import gov.nasa.worldwind.globes.Globe;
import gov.nasa.worldwind.poi.BasicPointOfInterest;
import gov.nasa.worldwind.poi.Gazetteer;
import gov.nasa.worldwind.poi.PointOfInterest;
import gov.nasa.worldwind.poi.YahooGazetteer;
import gov.nasa.worldwind.view.orbit.OrbitView;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.JTextComponent;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.xml.sax.SAXException;


public class CorinaGazetteerPanel extends JPanel {

	private static final long serialVersionUID = 1462658061492070327L;
	private final WorldWindow wwd;
    private Gazetteer gazeteer;
    private JPanel resultsPanel;
    private JComboBox resultsBox;

    public CorinaGazetteerPanel(final WorldWindow wwd, String gazetteerClassName)
        throws IllegalAccessException, InstantiationException, ClassNotFoundException
    {
        super(new BorderLayout());

        if (gazetteerClassName != null)
            this.gazeteer = this.constructGazetteer(gazetteerClassName);
        else
            this.gazeteer = new YahooGazetteer();

        this.wwd = wwd;

        // The label
        URL imageURL = this.getClass().getResource("/images/32x32-icon-earth.png");
        ImageIcon icon = new ImageIcon(imageURL);
        JLabel label = new JLabel(icon);
        label.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 15));

        // The text field
        final JTextField field = new JTextField("Enter place name or coordinate to search for...");
        field.addActionListener(new ActionListener()
        {
            public void actionPerformed(final ActionEvent actionEvent)
            {
                EventQueue.invokeLater(new Runnable()
                {
                    public void run()
                    {
                        try
                        {
                            handleEntryAction(actionEvent);
                        }
                        catch (NoItemException e)
                        {
                            JOptionPane.showMessageDialog(CorinaGazetteerPanel.this,
                                "Location not available \"" + (field.getText() != null ? field.getText() : "") + "\"\n"
                                    + "(" + e.getMessage() + ")",
                                "Location Not Available", JOptionPane.ERROR_MESSAGE);
                        }
                        catch (IllegalArgumentException e)
                        {
                            JOptionPane.showMessageDialog(CorinaGazetteerPanel.this,
                                "Error parsing input \"" + (field.getText() != null ? field.getText() : "") + "\"\n"
                                    + e.getMessage(),
                                "Lookup Failure", JOptionPane.ERROR_MESSAGE);                  
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                            JOptionPane.showMessageDialog(CorinaGazetteerPanel.this,
                                "Error looking up \"" + (field.getText() != null ? field.getText() : "") + "\"\n"
                                    + e.getMessage(),
                                "Lookup Failure", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                });
            }
        });

        // Enclose entry field in an inner panel in order to control spacing/padding
        JPanel fieldPanel = new JPanel(new BorderLayout());
        fieldPanel.add(field, BorderLayout.CENTER);
        fieldPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 15));

        // Put everything together
        this.add(label, BorderLayout.WEST);
        this.add(fieldPanel, BorderLayout.CENTER);

        resultsPanel = new JPanel(new GridLayout(1,2));
        resultsPanel.add(new JLabel("Results: "));
        resultsBox = new JComboBox();
        resultsBox.addActionListener(new ActionListener()
        {
            public void actionPerformed(final ActionEvent actionEvent)
            {
                EventQueue.invokeLater(new Runnable()
                {
                    public void run()
                    {
                        JComboBox cb = (JComboBox)actionEvent.getSource();
                        PointOfInterest selectedPoi = (PointOfInterest)cb.getSelectedItem();
                        CorinaGazetteerPanel.moveToLocation(wwd, selectedPoi);
                    }
                });
            }
        });
        resultsPanel.add(resultsBox);
        resultsPanel.setVisible(false);
        this.add(resultsPanel, BorderLayout.EAST);
    }

    @SuppressWarnings("unchecked")
	private Gazetteer constructGazetteer(String className)
        throws ClassNotFoundException, IllegalAccessException, InstantiationException
    {
        if (className == null || className.length() == 0)
        {
            throw new IllegalArgumentException("Gazetteer class name is null");
        }

        Class c = Class.forName(className.trim());
        Object o = c.newInstance();

        if (!(o instanceof Gazetteer))
            throw new IllegalArgumentException("Gazetteer class name is null");

        return (Gazetteer) o;
    }

    private void handleEntryAction(ActionEvent actionEvent) throws IOException, ParserConfigurationException,
        XPathExpressionException, SAXException, NoItemException, IllegalArgumentException
    {
        String lookupString = null;

        //hide any previous results
        resultsPanel.setVisible(false);
        if (actionEvent.getSource() instanceof JTextComponent)
            lookupString = ((JTextComponent) actionEvent.getSource()).getText();

        if (lookupString == null || lookupString.length() < 1)
            return;

        java.util.List<PointOfInterest> poi = parseSearchValues(lookupString);

        if (poi != null)
        {
            if (poi.size() == 1)
            {
            	CorinaGazetteerPanel.moveToLocation(wwd, poi.get(0));
            }
            else
            {
                resultsBox.removeAllItems();
                for ( PointOfInterest p:poi)
                {
                    resultsBox.addItem(p);
                }
                resultsPanel.setVisible(true);
            }
        }
    }

    /*
    Sample imputs
    Coordinate formats:
    39.53, -119.816  (Reno, NV)
    21 10 14 N, 86 51 0 W (Cancun)
    -31� 59' 43", 115� 45' 32" (Perth)
     */
    private java.util.List<PointOfInterest> parseSearchValues(String searchStr)
    {
        String sepRegex = "[,]"; //other seperators??
        searchStr = searchStr.trim();
        String[] searchValues = searchStr.split(sepRegex);
        if (searchValues.length == 1) 
        {
            return queryService(searchValues[0].trim());
        }
        else if (searchValues.length == 2) //possible coordinates
        {
            //any numbers at all?
            String regex = "[0-9]";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(searchValues[1]); //Street Address may have numbers in first field so use 2nd
            if (matcher.find())
            {
                java.util.List<PointOfInterest> list = new ArrayList<PointOfInterest>();
                list.add(parseCoordinates(searchValues));
                return list;
            }
            else
            {
                return queryService(searchValues[0].trim() + "+" + searchValues[1].trim());
            }
        }
        else
        {
            //build search string and send to service
            StringBuilder sb = new StringBuilder();
            for (int i=0; i<searchValues.length; i++)
            {
                sb.append(searchValues[i].trim());
                if (i < searchValues.length-1)
                    sb.append("+");

            }

            return queryService(sb.toString());
        }
    }

    private java.util.List<PointOfInterest> queryService(String queryString)
    {
        java.util.List<PointOfInterest> results = this.gazeteer.findPlaces(queryString);
        if (results == null || results.size() == 0)
            return null;
        else
            return results;
    }

    //throws IllegalArgumentException
    private PointOfInterest parseCoordinates(String coords[])
    {
        if (isDecimalDegrees(coords))
        {
            Double d1 = Double.parseDouble(coords[0].trim());
            Double d2 = Double.parseDouble(coords[1].trim());

            return new BasicPointOfInterest(LatLon.fromDegrees(d1, d2));
        }
        else //may be in DMS
        {
            Angle aLat = Angle.fromDMS(coords[0].trim());
            Angle aLon = Angle.fromDMS(coords[1].trim());

            return new BasicPointOfInterest(LatLon.fromDegrees(aLat.getDegrees(), aLon.getDegrees()));
        }
    }

    private boolean isDecimalDegrees(String[] coords)
    {
        try{
            Double.parseDouble(coords[0].trim());
            Double.parseDouble(coords[1].trim());
        } catch(NumberFormatException nfe)
        {
            return false;
        }

        return true;
    }

    public static void moveToLocation(WorldWindow wwd, Position location)
    {
    	wwd.getView().goTo(location, 25e3);
    }
    
    public static void moveToLocation(WorldWindow wwd, PointOfInterest location)
    {
            // Use a PanToIterator to iterate view to target position
        wwd.getView().goTo(new Position(location.getLatlon(), 0), 25e3);
    }

    public static void moveToLocation(WorldWindow wwd, Sector sector, Double altitude)
    {
        OrbitView view = (OrbitView) wwd.getView();

        Globe globe = wwd.getModel().getGlobe();

        if (altitude == null || altitude == 0)
        {
        	double t = sector.getDeltaLonRadians() > sector.getDeltaLonRadians()
        		? sector.getDeltaLonRadians() : sector.getDeltaLonRadians();
        	double w = 0.5 * t * 6378137.0;
        	altitude = w / wwd.getView().getFieldOfView().tanHalfAngle();
        }

        if (globe != null && view != null)
        {
            wwd.getView().goTo(new Position(sector.getCentroid(), 0), altitude);
        }
    }
}
