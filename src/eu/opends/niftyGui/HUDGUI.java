package eu.opends.niftyGui;

import java.util.Calendar;
import java.util.GregorianCalendar;

import com.jme3.asset.AssetManager;
import com.jme3.audio.AudioRenderer;
import com.jme3.input.FlyByCamera;
import com.jme3.input.InputManager;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial.CullHint;
import com.jme3.texture.Image;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.Button;
import de.lessvoid.nifty.controls.ListBox;
import de.lessvoid.nifty.controls.ListBox.SelectionMode;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.tools.Color;
import eu.opends.basics.SimulationBasics;
import eu.opends.niftyGui.listBox.TextListBoxModel;
import eu.opends.niftyGui.HUDGUIController;
import eu.opends.niftyGui.KeyMappingGUI.GuiLayer;

public class HUDGUI {
	
	private InputManager inputManager;
	private ViewPort guiViewPort;
	private NiftyJmeDisplay niftyDisplay;
	private ListBox<TextListBoxModel> listBox;
	private Nifty nifty;
	private SimulationBasics sim;
	private boolean keyMappingHidden = true;
	private boolean initiallyPaused = false;
	private AssetManager assetManager;
	private AudioRenderer audioRenderer;
	private FlyByCamera flyCam;
	
	private boolean hudVisible = false;

	
	/**
	 * This enum contains all tabs of the key mapping and graphic 
	 * settings GUI. Each tab is connected to a certain layer and 
	 * button.
	 */
	public enum GuiLayer 
	{
		KEYMAPPING_PAGE1("keyMappingLayer1", "keyMappingButton1"), 
		KEYMAPPING_PAGE2("keyMappingLayer2", "keyMappingButton2"), 
		GRAPHICSETTINGS("graphicSettingsLayer", "graphicSettingsButton");
		
		private String layerName;
		private String button;
		
		GuiLayer(String layerName, String button)
		{
			this.layerName = layerName;
			this.button = button;
		}
		
		public String getLayerName()
		{
			return layerName;
		}
		
		public String getButton()
		{
			return button;
		}
	}

	
	/**
	 * Creates a new instance of the HUD GUI.
	 * 
	 * @param sim
	 * 			SimulationBasics class.
	 */
	public HUDGUI(SimulationBasics sim)
	{
		this.sim = sim;
		this.assetManager = sim.getAssetManager();
		this.inputManager = sim.getInputManager();
		this.audioRenderer = sim.getAudioRenderer();
		this.guiViewPort = sim.getGuiViewPort();
		this.flyCam = sim.getFlyByCamera();
		this.initHUDGUI();
	}
	
	/**
	 * Initializes key mapping and graphic settings GUI.
	 */
	private void initHUDGUI()
	{		
		NiftyJmeDisplay niftyDisplay = new NiftyJmeDisplay(assetManager, inputManager, audioRenderer, guiViewPort);
    	
    	// Create a new NiftyGUI object
    	nifty = niftyDisplay.getNifty();
    		
    	String xmlPath = "Interface/HUDGUI.xml";
    	
    	// Read XML and initialize custom ScreenController
    	nifty.fromXml(xmlPath, "start", new HUDGUIController(sim, this));
    		
    	// attach the Nifty display to the GUI view port as a processor
    	guiViewPort.addProcessor(niftyDisplay);
    	
    	// disable fly camera, since mouse pointer is needed for user input
    	flyCam.setEnabled(false);
	}
	
	
//	private void testTransparentImage(){
//		Image transPNG=Image.createImage("Textures/hud/grid.png");  //load the tranparent image or opaque image
//		int rgbData[];
//		transPNG.getRGB(rgbData, 0,transPNG.getWidth(), 0, 0,transPNG.getWidth(), transPNG.getHeight());
//		Image tranparentImage==Image.createRGBImage(rgbData, width, height, true); //process alpha (true) for opaque false
//		transPNG=null;
//	}

	
	
	/**
	 * Sets the visibility of the hud display to true.
	 */
	public void showHUD() 
	{
		if(!hudVisible)
		{				
			// attach the Nifty display to the gui view port as a processor
			guiViewPort.addProcessor(niftyDisplay);
			
			// if scroll bar available --> set mouse pointer visible
//			inputManager.setCursorVisible(listBox.itemCount() > nrOflines);
			
			hudVisible = true;
		}
	}
	
	/**
	 * Sets the visibility of the HUD to false.
	 */
	public void hideHUD() 
	{
		if(hudVisible)
		{
			// detach the Nifty display from the gui view port as a processor
			guiViewPort.removeProcessor(niftyDisplay);
//			inputManager.setCursorVisible(false);
			
			hudVisible = false;
		}
	}
	
	
	/**
	 * Toggles the visibility of the message box.
	 */
	public void toggleDialog() 
	{
		if (hudVisible)
			hideHUD();
		else 
			showHUD();
	}
	
	
	/**
	 * Returns Nifty element of the key mapping and graphic settings GUI.
	 * 
	 * @return
	 * 			Nifty Element.
	 */
	public Nifty getNifty()
	{
		return nifty;
	}
	
	

	/**
	 * Changes over to the given layer of the (already opened)
	 * key mapping and graphic settings GUI.
	 * 
	 * @param selectedLayer
	 * 			Layer name to show.
	 */
	public void openLayer(GuiLayer selectedLayer)
	{
		Screen screen = nifty.getCurrentScreen();
		
		// show given layer, hide all others (except "menuLayer" which contains menu buttons)
		for(Element layer : screen.getLayerElements())
			if(layer.getId().equals(selectedLayer.getLayerName()) || layer.getId().equals("menuLayer"))
				layer.show();
			else
				layer.hide();
		
		// set focus to button related to the selected layer
		Button button = (Button) screen.findNiftyControl(selectedLayer.getButton(), Button.class);
		button.setFocus();
	}
	
	
	/**
	 * Close key mapping and graphic settings GUI.
	 */
	private void closeHUDGUI() 
	{
		nifty.exit();
        inputManager.setCursorVisible(false);
        flyCam.setEnabled(true);
	}
	
	
}
