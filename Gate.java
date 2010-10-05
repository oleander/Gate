import java.util.*;

public abstract class Gate {

  protected String name;
  protected boolean outputValue;
  protected ArrayList<Gate> outputGates;
  protected ArrayList<Gate> inputGates;
  protected static int delay;
  
  public Gate(){
    this.inputGates = new ArrayList<Gate>();
    this.outputGates = new ArrayList<Gate>();
  }
  
  public void init(){
    this.inputChanged();
  }
  
  public String getName(){
    return this.name;
  }
  
  public void setName(String name){
    this.name = name;
  }
  
  public boolean getOutputValue(){
    return this.outputValue;
  }
  
  public void setOutputValue(boolean value){
    this.outputValue = value;
  }
  
  public void setInputGate(Gate gate){
    this.inputGates.add(gate);
    gate.setOutputGate(this);
  }
  
  public List<Gate> getInputGates(){
    return (List<Gate>) this.inputGates;
  }
  
  public abstract boolean calculateValue();
  
  public static void setDelay(int d){
    delay = d;
  }
  
  public static int getDelay(){
    return delay;
  }
  
  protected void outputChanged(boolean value) {
    this.outputValue = value;
    for (Gate g : this.outputGates) {
      g.inputChanged();
    }
  }
  
  protected void setOutputGate(Gate g) {
    this.outputGates.add(g);
  }
  
  
  public abstract void inputChanged();
  
}