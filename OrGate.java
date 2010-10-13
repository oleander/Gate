public class OrGate extends BasicGate {
  
  public OrGate(){
    super();
  }
  
  public boolean calculateValue() throws GateException {
    if (getInputGates().size() < 2) {
      throw new GateException("Error in: " + getName() + " - invalid number of input gates.", getOriginFile(), getLine());
    }
    int trues = 0;
    for (Gate g : getInputGates()) {
      if (g.getOutputValue()) {
        trues++;
      }
    }
    return trues >= 1 ? true : false;
  }
}
