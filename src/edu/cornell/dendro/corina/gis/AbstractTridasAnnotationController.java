package edu.cornell.dendro.corina.gis;

import gov.nasa.worldwind.WorldWindow;
import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.event.SelectEvent;
import gov.nasa.worldwind.event.SelectListener;
import gov.nasa.worldwind.examples.util.ButtonAnnotation;
import gov.nasa.worldwind.render.Annotation;
import gov.nasa.worldwind.util.Logging;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public abstract class AbstractTridasAnnotationController implements ActionListener, SelectListener
{
    private WorldWindow wwd;
    private boolean enabled;
    private TridasAnnotation annotation;
    protected ButtonAnnotation toolTipComponent;

    public AbstractTridasAnnotationController(WorldWindow worldWindow, TridasAnnotation annotation)
    {
        if (worldWindow == null)
        {
            String message = Logging.getMessage("nullValue.WorldWindow");
            Logging.logger().log(java.util.logging.Level.SEVERE, message);
            throw new IllegalArgumentException(message);
        }

        this.wwd = worldWindow;
        this.setAnnotation(annotation);
    }

    public WorldWindow getWorldWindow()
    {
        return this.wwd;
    }

    public boolean isEnabled()
    {
        return this.enabled;
    }

    public void setEnabled(boolean enabled)
    {
        if (!this.enabled && enabled)
        {
            this.doEnable();
        }
        else if (this.enabled && !enabled)
        {
            this.doDisable();
        }

        this.enabled = enabled;
    }

    protected void doEnable()
    {
        this.getWorldWindow().addSelectListener(this);
    }

    protected void doDisable()
    {
        this.getWorldWindow().removeSelectListener(this);
    }

    public TridasAnnotation getAnnotation()
    {
        return this.annotation;
    }

    public void setAnnotation(TridasAnnotation annotation)
    {
        if (this.annotation == annotation)
            return;

        if (this.annotation != null)
        {
            this.annotation.removeActionListener(this);
        }

        this.annotation = annotation;

        if (this.annotation != null)
        {
            this.annotation.addActionListener(this);
        }
    }

    //**************************************************************//
    //********************  Action Listener  ***********************//
    //**************************************************************//

    public void actionPerformed(ActionEvent e)
    {
        if (e == null)
            return;

        this.onActionPerformed(e);
    }

    protected void onActionPerformed(ActionEvent e)
    {
    }

    //**************************************************************//
    //********************  Select Listener  ***********************//
    //**************************************************************//

    public void selected(SelectEvent e)
    {
        if (e == null)
            return;

        this.onSelected(e);
    }

    protected void onSelected(SelectEvent e)
    {
        // Forward this event to any ButtonAnnotations under the main annotation.
        this.forwardToButtonAnnotations(this.getAnnotation(), e);

        // Change the cursor type if a ButtonAnnotation is beneath the cursor.
        this.updateCursor(e);

        // Show a tool tip if an ButtonAnnotation is beneath the cursor.
        this.updateToolTip(e);
    }

    protected void forwardToButtonAnnotations(Annotation annotation, SelectEvent e)
    {
        if (annotation instanceof ButtonAnnotation)
        {
            ((ButtonAnnotation) annotation).selected(e);
        }

        for (Annotation child : annotation.getChildren())
        {
            this.forwardToButtonAnnotations(child, e);
        }
    }

    protected void updateCursor(SelectEvent e)
    {
        Object topObject = e.getTopObject();
        if (topObject != null && topObject instanceof ButtonAnnotation)
        {
            this.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.HAND_CURSOR));
        }
        else if (topObject != null && topObject instanceof TridasAnnotation)
        {
            if (((TridasAnnotation) topObject).isBusy())
            {
                this.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.WAIT_CURSOR));
            }
            else
            {
                this.setCursor(java.awt.Cursor.getDefaultCursor());
            }
        }
        else
        {
            this.setCursor(java.awt.Cursor.getDefaultCursor());
        }
    }

    protected void setCursor(java.awt.Cursor cursor)
    {
        if (this.getWorldWindow() instanceof java.awt.Component)
        {
            java.awt.Component component = (java.awt.Component) this.getWorldWindow();
            if (!component.getCursor().equals(cursor))
            {
                component.setCursor(cursor);
            }
        }
    }
    
    @SuppressWarnings({"StringEquality"})
    protected void updateToolTip(SelectEvent e)
    {
        if (e.getEventAction() != SelectEvent.HOVER)
            return;

        Object topObject = e.getTopObject();
        if (topObject != null && topObject instanceof ButtonAnnotation)
        {
            this.showToolTip(e, (ButtonAnnotation) topObject);
        }
        else
        {
            this.showToolTip(e, null);
        }
    }

    protected void showToolTip(SelectEvent e, ButtonAnnotation annotation)
    {
        if (this.toolTipComponent == annotation)
            return;

        if (this.toolTipComponent != null)
        {
            this.toolTipComponent.setShowToolTip(false);
            this.toolTipComponent.setToolTipPoint(null);
            this.toolTipComponent = null;
        }

        if (annotation != null)
        {
            java.awt.Point point = this.getToolTipPoint(e);
            this.toolTipComponent = annotation;
            this.toolTipComponent.setShowToolTip(true);
            this.toolTipComponent.setToolTipPoint(point);
        }

        this.getWorldWindow().redraw();
    }

    protected java.awt.Point getToolTipPoint(SelectEvent e)
    {
        java.awt.Point pickPoint = e.getPickPoint();

        if (e.getSource() instanceof java.awt.Component)
        {
            pickPoint = this.glPointFromAwt((java.awt.Component) e.getSource(), pickPoint);
        }

        return new java.awt.Point(pickPoint.x, pickPoint.y - 40);
    }

    protected java.awt.Point glPointFromAwt(java.awt.Component c, java.awt.Point p)
    {
        return new java.awt.Point(p.x, c.getHeight() - p.y - 1);
    }
}

