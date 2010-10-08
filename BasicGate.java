import javax.swing.Timer;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public abstract class BasicGate extends Gate implements ActionListener {
  
  private Timer t = new Timer(this.delay, this);
  private boolean newValue = false;
  
  public BasicGate(){
    
  }
  
  public void inputChanged(){
    if (this.t.isRunning()) {
      return;
    }
    this.newValue = this.calculateValue();
    if (this.getOutputValue() != newValue) {
      t.start();
    }
  }
  
  public void actionPerformed(ActionEvent e) {
    this.t.stop();
    this.outputChanged(this.newValue);
  }
}