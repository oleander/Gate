public class NorGate extends BasicGate {
  public NorGate(){
    super();
  }
  
  public boolean calculateValue() throws GateException {
    if (this.inputGates.size() != 2) {
      throw new GateException("Error in: " + this.name + " - invalid number of input gates.");
    }
    return !(this.inputGates.get(0).getOutputValue() || this.inputGates.get(1).getOutputValue());
  }
}