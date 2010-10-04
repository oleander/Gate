public abstract class BasicGate extends Gate {
  public BasicGate(){
    
  }
  
  public void inputChanged(){
    this.outputChanged(this.calculateValue());
  }
}