public class NotGate extends BasicGate  {
  public NotGate(){
    super();
  }
  public NotGate(String s) {
    super(s);
  }
  
  public boolean calculateValue() throws GateException {
    if (this.getInputGates().size() != 1) {
      throw new GateException("Error in: " + this.getName() + " - invalid number of input gates.");
    }
    return !this.getInputGates().get(0).getOutputValue();
  }
  
  public void setInputGate(Gate gate) throws GateException {
    if (this.getInputGates().size() >= 1) {
      throw new GateException("Error: Tried to add more than one input gate to gate: " + this.getName());
    }
    this.getInputGates().add(gate);
    gate.getOutputGates().add(this);
  }
}
