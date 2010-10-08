public class NotGate extends BasicGate  {
  public NotGate(){
    
  }
  
  public boolean calculateValue() throws GateException {
    if (this.getInputGates().size() != 1) {
      throw new GateException("Error in: " + this.getName() + " - invalid number of input gates.");
    }
    return !this.getInputGates().get(0).getOutputValue();
  }
  
  public void setInputGate(Gate gate) throws GateException {
    if (this.inputGates.size() >= 1) {
      throw new GateException("Error: Tried to add more than one input gate to gate: " + this.getName());
    }
    this.inputGates.add(gate);
    gate.setOutputGate(this);
  }
}