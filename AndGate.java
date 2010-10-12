public class AndGate extends BasicGate {
  public AndGate(){
    super();
  }
  
  public boolean calculateValue() throws GateException {
    if (this.getInputGates().size() < 2) {
      throw new GateException("Error in: " + this.getName() + " - invalid number of input gates.");
    }
    for (Gate g : this.getInputGates()) {
      if (!g.getOutputValue()) {
        return false;
      }
    }
    return true;
  }
}
