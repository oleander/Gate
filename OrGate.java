public class OrGate extends BasicGate {
  
  public OrGate(){
    super();
  }
  
  public boolean calculateValue() throws GateException {
    if (this.getInputGates().size() < 2) {
      throw new GateException("Error in: " + this.getName() + " - invalid number of input gates.");
    }
    int trues = 0;
    for (Gate g : this.getInputGates()) {
      if (g.getOutputValue()) {
        trues++;
      }
    }
    return trues >= 1 ? true : false;
  }
}
