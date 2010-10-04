class SignalGate extends Gate {
  
  public SignalGate(){
    
  }
  
  public void setValue(boolean value){
    this.value = value;
  }
  
  public boolean calculateValue(){
    return true;
  }
  
  public void setInputGate(Gate gate) throws GateException {
    throw new GateException("Error SignalGate has no input");
  }
  
  public void inputChanged() throws GateException {
    throw new GateException("Error SignalGate has no input");
  }
  
}