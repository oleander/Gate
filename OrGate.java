public class OrGate extends BasicGate {
  
  public OrGate(){
    super();
  }
  
  public boolean calculateValue() throws GateException {
    System.out.println(this.inputGates.size());
    if (this.inputGates.size() < 2) {
      throw new GateException("Error in: " + this.name + " - invalid number of input gates.");
    }
    
    for (Gate g : this.inputGates) {
      if (g.getOutputValue()) {
        return true;
      }
    }
    return false;
  }
}