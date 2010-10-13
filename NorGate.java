

public class NorGate extends BasicGate {
  public NorGate(){
    super();
  }
  
  public boolean calculateValue() throws GateException {
    if (getInputGates().size() < 2) {
      throw new GateException("Error in: " + getName() + " - invalid number of input gates.");
    }
    for (Gate g : getInputGates()) {
      if (g.getOutputValue()) {
        return false;
      }
    }
    return true;
  }
}
