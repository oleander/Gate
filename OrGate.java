public class OrGate extends BasicGate {
  
  public OrGate(){
    super();
  }
  
  public boolean calculateValue() throws GateException {
    if (this.inputGates.size() < 2) {
      throw new GateException("Error in: " + this.name + " - invalid number of input gates.");
    }
    int trues = 0;
    for (Gate g : this.inputGates) {
      if (g.getOutputValue()) {
        trues++;
      }
    }
    return trues >= 1 ? true : false;
  }
}