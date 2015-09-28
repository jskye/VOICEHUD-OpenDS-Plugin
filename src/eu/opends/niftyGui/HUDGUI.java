package eu.opends.niftyGui;

import java.util.Calendar;
import java.util.GregorianCalendar;

import com.jme3.input.InputManager;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.renderer.ViewPort;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.ListBox;
import de.lessvoid.nifty.controls.ListBox.SelectionMode;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.tools.Color;
import eu.opends.basics.SimulationBasics;
import eu.opends.niftyGui.listBox.TextListBoxModel;
import eu.opends.niftyGui.HUDGUIController;

public class HUDGUI {
	
	private InputManager inputManager;
	private ViewPort guiViewPort;
	private NiftyJmeDisplay niftyDisplay;
	private ListBox<TextListBoxModel> listBox;
	private Nifty nifty;
	
	/**
	 * Creates a new message box by assigning the lines of text to send all messages to 
	 * and the number of characters a line may have before the next line will be used.
	 * 
	 * @param sim
	 * 			Simulation Basics
	 */
	@SuppressWarnings("unchecked")
	public HUDGUI(SimulationBasics sim) 
	{
		inputManager = sim.getInputManager();
		guiViewPort = sim.getGuiViewPort();
		
		niftyDisplay = new NiftyJmeDisplay(sim.getAssetManager(), inputManager, 
				sim.getAudioRenderer(), guiViewPort);

		// Create a new NiftyGUI object
		nifty = niftyDisplay.getNifty();

		String xmlPath = "Interface/HUDGUI.xml";

		// Read XML and initialize custom ScreenController
		nifty.fromXml(xmlPath, "start",	new HUDGUIController(sim, this));
		
		// init list box
//		Screen screen = nifty.getCurrentScreen();
//		listBox = (ListBox<TextListBoxModel>) screen.findNiftyControl("messageBox", ListBox.class);
//		listBox.changeSelectionMode(SelectionMode.Disabled, false);		
//		listBox.setFocusable(false);
	}
	
}
