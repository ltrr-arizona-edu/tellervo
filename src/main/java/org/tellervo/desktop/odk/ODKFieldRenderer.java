package org.tellervo.desktop.odk;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import org.tellervo.desktop.odk.fields.AbstractODKField;
import org.tellervo.desktop.odk.fields.ODKDataType;
import org.tellervo.desktop.ui.Builder;

public class ODKFieldRenderer extends JLabel implements ListCellRenderer<AbstractODKField> {

	private static final long serialVersionUID = 1L;

	public ODKFieldRenderer()
	{
        setVerticalAlignment(CENTER);
        setOpaque(true);
	}
	
	@Override
	public Component getListCellRendererComponent(
			JList<? extends AbstractODKField> list, AbstractODKField value,
			int index, boolean isSelected, boolean cellHasFocus) {


        if (isSelected) {
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
        } else {
            setBackground(Color.WHITE);
            setForeground(list.getForeground());
        }
        
        if(value.isFieldRequired())
        {
        	if(value.isFieldHidden())
        	{
        		setText("<html>"+value.getFieldName()+" <font size=\"-2\"><i>[required and hidden]</i></font>");
        	}
        	else
        	{
        		setText("<html>"+value.getFieldName()+" <font size=\"-2\"><i>[required]</i></font>");
        	}
        }
        else
        {
        	if(value.isFieldHidden())
        	{
        		setText("<html>"+value.getFieldName()+" <font size=\"-2\"><i>[hidden]</i></font>");
        	}
        	else
        	{
        		setText("<html>"+value.getFieldName()+" <font size=\"-2\"><i></i></font>");
        	}
        }
        
        if(value.getFieldType().equals(ODKDataType.AUDIO))
        {
        	setIcon(Builder.getIcon("sound.png", 16));
        }
        else if(value.getFieldType().equals(ODKDataType.IMAGE))
        {
        	setIcon(Builder.getIcon("photo.png", 16));
        }
        else if(value.getFieldType().equals(ODKDataType.VIDEO))
        {
        	setIcon(Builder.getIcon("movie.png", 16));
        }
        else if(value.getFieldType().equals(ODKDataType.STRING))
        {
        	setIcon(Builder.getIcon("letters.png", 16));
        }
        else if(value.getFieldType().equals(ODKDataType.SELECT_MANY) || 
        		value.getFieldType().equals(ODKDataType.SELECT_ONE))
        {
        	setIcon(Builder.getIcon("list.png", 16));
        }
        else if(value.getFieldType().equals(ODKDataType.INTEGER) ||
        		value.getFieldType().equals(ODKDataType.DECIMAL))
        {
        	setIcon(Builder.getIcon("numbers.png", 16));
        }
        else if(value.getFieldType().equals(ODKDataType.LOCATION))
        {
        	setIcon(Builder.getIcon("pin.png", 16));
        }
        else if(value.getFieldType().equals(ODKDataType.DATE)||
        		value.getFieldType().equals(ODKDataType.DATE_TIME))
        {
        	setIcon(Builder.getIcon("calendar.png", 16));
        }
        else
        {
        	setIcon(null);
        }
        return this;
	}

}
