package net.sf.fmj.ui.wizard;

import java.beans.*;
import java.util.*;

import javax.swing.*;

/**
 * Adapted Robert Eckstein's sample at
 * http://java.sun.com/developer/technicalArticles/GUI/swing/wizard/
 *
 * The model for the Wizard component, which tracks the text, icons, and enabled
 * state of each of the buttons, as well as the current panel that is displayed.
 * Note that the model, in its current form, is not intended to be subclassed.
 */

public class WizardModel
{
    /**
     * Identification string for the current panel.
     */
    public static final String CURRENT_PANEL_DESCRIPTOR_PROPERTY = "currentPanelDescriptorProperty";

    /**
     * Property identification String for the Back button's text
     */
    public static final String BACK_BUTTON_TEXT_PROPERTY = "backButtonTextProperty";
    /**
     * Property identification String for the Back button's icon
     */
    public static final String BACK_BUTTON_ICON_PROPERTY = "backButtonIconProperty";
    /**
     * Property identification String for the Back button's enabled state
     */
    public static final String BACK_BUTTON_ENABLED_PROPERTY = "backButtonEnabledProperty";

    /**
     * Property identification String for the Next button's text
     */
    public static final String NEXT_FINISH_BUTTON_TEXT_PROPERTY = "nextButtonTextProperty";
    /**
     * Property identification String for the Next button's icon
     */
    public static final String NEXT_FINISH_BUTTON_ICON_PROPERTY = "nextButtonIconProperty";
    /**
     * Property identification String for the Next button's enabled state
     */
    public static final String NEXT_FINISH_BUTTON_ENABLED_PROPERTY = "nextButtonEnabledProperty";

    /**
     * Property identification String for the Cancel button's text
     */
    public static final String CANCEL_BUTTON_TEXT_PROPERTY = "cancelButtonTextProperty";
    /**
     * Property identification String for the Cancel button's icon
     */
    public static final String CANCEL_BUTTON_ICON_PROPERTY = "cancelButtonIconProperty";
    /**
     * Property identification String for the Cancel button's enabled state
     */
    public static final String CANCEL_BUTTON_ENABLED_PROPERTY = "cancelButtonEnabledProperty";

    private WizardPanelDescriptor currentPanel;

    private HashMap panelHashmap;

    private HashMap buttonTextHashmap;
    private HashMap buttonIconHashmap;
    private HashMap buttonEnabledHashmap;

    private PropertyChangeSupport propertyChangeSupport;

    /**
     * Default constructor.
     */
    public WizardModel()
    {
        panelHashmap = new HashMap();

        buttonTextHashmap = new HashMap();
        buttonIconHashmap = new HashMap();
        buttonEnabledHashmap = new HashMap();

        propertyChangeSupport = new PropertyChangeSupport(this);

    }

    public void addPropertyChangeListener(PropertyChangeListener p)
    {
        propertyChangeSupport.addPropertyChangeListener(p);
    }

    protected void firePropertyChange(String propertyName, Object oldValue,
            Object newValue)
    {
        propertyChangeSupport.firePropertyChange(propertyName, oldValue,
                newValue);
    }

    Boolean getBackButtonEnabled()
    {
        return (Boolean) buttonEnabledHashmap.get(BACK_BUTTON_ENABLED_PROPERTY);
    }

    Icon getBackButtonIcon()
    {
        return (Icon) buttonIconHashmap.get(BACK_BUTTON_ICON_PROPERTY);
    }

    Object getBackButtonText()
    {
        return buttonTextHashmap.get(BACK_BUTTON_TEXT_PROPERTY);
    }

    Boolean getCancelButtonEnabled()
    {
        return (Boolean) buttonEnabledHashmap
                .get(CANCEL_BUTTON_ENABLED_PROPERTY);
    }

    Icon getCancelButtonIcon()
    {
        return (Icon) buttonIconHashmap.get(CANCEL_BUTTON_ICON_PROPERTY);
    }

    Object getCancelButtonText()
    {
        return buttonTextHashmap.get(CANCEL_BUTTON_TEXT_PROPERTY);
    }

    /**
     * Returns the currently displayed WizardPanelDescriptor.
     *
     * @return The currently displayed WizardPanelDescriptor
     */
    WizardPanelDescriptor getCurrentPanelDescriptor()
    {
        return currentPanel;
    }

    Boolean getNextFinishButtonEnabled()
    {
        return (Boolean) buttonEnabledHashmap
                .get(NEXT_FINISH_BUTTON_ENABLED_PROPERTY);
    }

    Icon getNextFinishButtonIcon()
    {
        return (Icon) buttonIconHashmap.get(NEXT_FINISH_BUTTON_ICON_PROPERTY);
    }

    Object getNextFinishButtonText()
    {
        return buttonTextHashmap.get(NEXT_FINISH_BUTTON_TEXT_PROPERTY);
    }

    WizardPanelDescriptor getPanelDescriptor(Object id)
    {
        // First, get the hashtable reference to the panel that should
        // be displayed.

        WizardPanelDescriptor nextPanel = (WizardPanelDescriptor) panelHashmap
                .get(id);

        // If we couldn't find the panel that should be displayed, return
        // false.

        if (nextPanel == null)
            throw new WizardPanelNotFoundException();

        return nextPanel;

    }

    /**
     * Registers the WizardPanelDescriptor in the model using the
     * Object-identifier specified.
     *
     * @param id
     *            Object-based identifier
     * @param descriptor
     *            WizardPanelDescriptor that describes the panel
     */
    void registerPanel(Object id, WizardPanelDescriptor descriptor)
    {
        // Place a reference to it in a hashtable so we can access it later
        // when it is about to be displayed.

        panelHashmap.put(id, descriptor);

    }

    public void removePropertyChangeListener(PropertyChangeListener p)
    {
        propertyChangeSupport.removePropertyChangeListener(p);
    }

    void setBackButtonEnabled(Boolean newValue)
    {
        Boolean oldValue = getBackButtonEnabled();
        if (newValue != oldValue)
        {
            buttonEnabledHashmap.put(BACK_BUTTON_ENABLED_PROPERTY, newValue);
            firePropertyChange(BACK_BUTTON_ENABLED_PROPERTY, oldValue, newValue);
        }
    }

    void setBackButtonIcon(Icon newIcon)
    {
        Object oldIcon = getBackButtonIcon();
        if (!newIcon.equals(oldIcon))
        {
            buttonIconHashmap.put(BACK_BUTTON_ICON_PROPERTY, newIcon);
            firePropertyChange(BACK_BUTTON_ICON_PROPERTY, oldIcon, newIcon);
        }
    }

    void setBackButtonText(Object newText)
    {
        Object oldText = getBackButtonText();
        if (!newText.equals(oldText))
        {
            buttonTextHashmap.put(BACK_BUTTON_TEXT_PROPERTY, newText);
            firePropertyChange(BACK_BUTTON_TEXT_PROPERTY, oldText, newText);
        }
    }

    void setCancelButtonEnabled(Boolean newValue)
    {
        Boolean oldValue = getCancelButtonEnabled();
        if (newValue != oldValue)
        {
            buttonEnabledHashmap.put(CANCEL_BUTTON_ENABLED_PROPERTY, newValue);
            firePropertyChange(CANCEL_BUTTON_ENABLED_PROPERTY, oldValue,
                    newValue);
        }
    }

    void setCancelButtonIcon(Icon newIcon)
    {
        Icon oldIcon = getCancelButtonIcon();
        if (!newIcon.equals(oldIcon))
        {
            buttonIconHashmap.put(CANCEL_BUTTON_ICON_PROPERTY, newIcon);
            firePropertyChange(CANCEL_BUTTON_ICON_PROPERTY, oldIcon, newIcon);
        }
    }

    void setCancelButtonText(Object newText)
    {
        Object oldText = getCancelButtonText();
        if (!newText.equals(oldText))
        {
            buttonTextHashmap.put(CANCEL_BUTTON_TEXT_PROPERTY, newText);
            firePropertyChange(CANCEL_BUTTON_TEXT_PROPERTY, oldText, newText);
        }
    }

    /**
     * Sets the current panel to that identified by the Object passed in.
     *
     * @param id
     *            Object-based panel identifier
     * @return boolean indicating success or failure
     */
    boolean setCurrentPanel(Object id)
    {
        // First, get the hashtable reference to the panel that should
        // be displayed.

        WizardPanelDescriptor nextPanel = (WizardPanelDescriptor) panelHashmap
                .get(id);

        // If we couldn't find the panel that should be displayed, return
        // false.

        if (nextPanel == null)
            throw new WizardPanelNotFoundException();

        WizardPanelDescriptor oldPanel = currentPanel;
        currentPanel = nextPanel;

        if (oldPanel != currentPanel)
            firePropertyChange(CURRENT_PANEL_DESCRIPTOR_PROPERTY, oldPanel,
                    currentPanel);

        return true;

    }

    void setNextFinishButtonEnabled(Boolean newValue)
    {
        Boolean oldValue = getNextFinishButtonEnabled();
        if (newValue != oldValue)
        {
            buttonEnabledHashmap.put(NEXT_FINISH_BUTTON_ENABLED_PROPERTY,
                    newValue);
            firePropertyChange(NEXT_FINISH_BUTTON_ENABLED_PROPERTY, oldValue,
                    newValue);
        }
    }

    public void setNextFinishButtonIcon(Icon newIcon)
    {
        Object oldIcon = getNextFinishButtonIcon();
        if (!newIcon.equals(oldIcon))
        {
            buttonIconHashmap.put(NEXT_FINISH_BUTTON_ICON_PROPERTY, newIcon);
            firePropertyChange(NEXT_FINISH_BUTTON_ICON_PROPERTY, oldIcon,
                    newIcon);
        }
    }

    void setNextFinishButtonText(Object newText)
    {
        Object oldText = getNextFinishButtonText();
        if (!newText.equals(oldText))
        {
            buttonTextHashmap.put(NEXT_FINISH_BUTTON_TEXT_PROPERTY, newText);
            firePropertyChange(NEXT_FINISH_BUTTON_TEXT_PROPERTY, oldText,
                    newText);
        }
    }

}
