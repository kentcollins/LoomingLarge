package weaveit2me.looms;

/**
 * Handle weaving process steps.  Implementing classes are expected to configure
 * the weaving hardware and translate these broad steps into specific electronic
 * sequences for operating the loom.
 * 
 * @author kentcollins
 *
 */
public interface LoomController {

	void setShafts(int bitPattern);

	void openShed();

	void closeShed();

	void weave();

	void beat();

	void wind();

}
