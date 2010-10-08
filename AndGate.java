

public class AndGate extends BasicGate {
  public AndGate(){
    super();
  }
  
  public boolean calculateValue() throws GateException {
    if (this.inputGates.size() < 2) {
      throw new GateException("Error in: " + this.name + " - invalid number of input gates.");
    }
    for (Gate g : this.inputGates) {
      if (!g.getOutputValue()) {
        return false;
      }
    }
    return true;
  }
}