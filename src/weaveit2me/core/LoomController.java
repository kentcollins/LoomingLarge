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
	 * Select the shafts that will lifted next
	 * @param selectedShafts
	 */
	void pickShafts(Integer[] selectedShafts);

	/**
	 * Open the shed, lifting the selected shafts
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
	
	/**
	 * Handle custom commands
	 * @param s
	 */
	void custom(String s);
	
	/**
	 * Prepare loom for operation
	 */
	void startup();
	
	/**
	 * Prepare loom for shutting down
	 */
	void shutdown();

}
