public class NotGate extends BasicGate  {
  public NotGate(){
    
  }
  
  public boolean calculateValue() throws GateException {
    if (this.getInputGates().size() != 1) {
      throw new GateException("Error in: " + this.getName() + " - invalid number of input gates.");
    }
    return !this.getInputGates().get(0).getOutputValue();
  }
  
  // Får man göra så här? Eller får man spec-smisk?
  public void getInputGate(Gate gate) throws GateException {
    this.getInputGates().set(0, gate);
  }
}