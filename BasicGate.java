/* 
  Generell logisk grind.
  Implementerar ActionListener för att kunna byta utvärde efter en viss fördröjning.
 */

import javax.swing.Timer;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public abstract class BasicGate extends Gate implements ActionListener {
  
  /* Skapar timer med delayvärdet från superklassen Gate, samt ställer in sig själv som lyssnare. */
  private Timer t = new Timer(Gate.getDelay(), this);
  private boolean newValue = false;
  
  public BasicGate(){
    super();
  }
  public BasicGate(String value) {
    super(value);
  }
  
  /* Kontrollerar så att ett tidigare byte av värde inte redan pågår.
     Förbereder byte av värde genom att spara ner det nya värdet och starta timern. 
   */
  public void inputChanged(){
    this.newValue = this.calculateValue();
    if (this.t.isRunning()) {
      return;
    }
    if (this.getOutputValue() != newValue) {
      t.setDelay(Gate.getDelay());
      t.start();
    }
  }
  
  /* Timern triggar byte av outputvärde. Timern stoppas för att den inte ska trigga flera event efter varandra. */
  public void actionPerformed(ActionEvent e) {
    this.t.stop();
    this.outputChanged(this.newValue);
  }
}
