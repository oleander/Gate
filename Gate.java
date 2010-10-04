import java.util.ArrayList;

public class Gate {
  
  private String name;
  private boolean outputValue;
  private ArrayList<Gate> outputGates;
  private ArrayList<Gate> inputGates;
  private static int delay;
  
  public Gate(){
    this.init();
  }
  
  public void init(){
    
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
  }
  
  public ArrayList<Gate> getInputGates(){
    return this.inputGates;
  }
  
  public boolean calculateValue(){
    return true;
  }
  
  public void setDelay(int delay){
    this.delay = delay;
  }
  
  public int getDelay(){
    return this.delay;
  }
  
  public void setOutputChanged(Gate gate){
    
  }
  
  public Gate getOutputChanged(){
    return new Gate();
  }
}