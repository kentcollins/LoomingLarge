package weaveit2me.core;

/**
 * Handle weaving process steps.  Implementing classes are expected to configure
 * the weaving hardware and translate these broad steps into specific electronic
 * sequences for operating the loom.
 * 
 * @author kentcollins
 *
 */
public interface LoomController {

	void pickShafts(int[] selectedShafts);

	void openShed();

	void closeShed();

	void weave();

	void beat();

	void wind();
	
	String getStatus();

}
