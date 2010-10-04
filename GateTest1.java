public class GateTest1 {

  public static void main(String[] arg) {
    AndGate a1 = new AndGate(), a2 = new AndGate();
    a1.setName("a1"); a2.setName("a2"); 
    NotGate n1 = new NotGate(), n2 = new NotGate();
    n1.setName("n1"); n2.setName("n2");
    OrGate  z  = new OrGate();
    z.setName("z");
    SignalGate x = new SignalGate(), y = new SignalGate();
    x.setName("x"); y.setName("y");
    n1.setInputGate(y); n2.setInputGate(x);
    a1.setInputGate(x); a1.setInputGate(n1);
    a2.setInputGate(y); a2.setInputGate(n2);
    z.setInputGate(a1); z.setInputGate(a2); 
    n1.init(); n2.init();
    a1.init(); a2.init();
    z.init();
    System.out.println("x  y  z");
    Gate.setDelay(1); 
    for (int i=0; i<2; i++)
      for (int j=0; j<2; j++) {
        try {
          x.setValue(i==1);
          Thread.sleep(5);
          y.setValue(j==1);      
          Thread.sleep(5);
        }
        catch (InterruptedException e) {}
        System.out.println(value(x) + "  " + value(y) + "  " + value(z));
      }   
  }

  public static String value(Gate g) {
    return ((g.getOutputValue()) ? "1" : "0");
  }
}
