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

	public void setShafts(int bitPattern);

	public void openShed();

	public void closeShed();

	public void weave();

	public void beat();

	public void wind();

}
