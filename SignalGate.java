public class SignalGate extends Gate {
  
  public SignalGate(){
    
  }
  
  public void setValue(boolean value){
    this.outputChanged(value);
  }
  
  public boolean calculateValue(){
    return this.getOutputValue();
  }
  
  public void setInputGate(Gate gate) throws GateException {
    throw new GateException("Error: SignalGate has no input.");
  }
  
  public void inputChanged() throws GateException {
    throw new GateException("Error: SignalGate has no input.");
  }
  
}