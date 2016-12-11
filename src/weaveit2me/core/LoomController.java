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

	/**
	 * Pick the shafts that will next be latched
	 * @param selectedShafts
	 */
	void pickShafts(int[] selectedShafts);

	/**
	 * Open the shed
	 */
	void openShed();

	/**
	 * Close the shed
	 */
	void closeShed();

	/**
	 * Make one pass with the warp
	 */
	void weave();

	/**
	 * Pull and release the beater
	 */
	void beat();

	/**
	 * Wind to advance cloth
	 */
	void wind();
	
	/**
	 * Retrieve status information
	 * @return
	 */
	String getStatus();

}
