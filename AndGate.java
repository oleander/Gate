public class AndGate extends BasicGate {
  public AndGate(){
    super();
  }
  
  public boolean calculateValue() throws GateException {
    for (Gate g : this.inputGates) {
      if (!g.getOutputValue()) {
        return false;
      }
    }
    return true;
  }
}