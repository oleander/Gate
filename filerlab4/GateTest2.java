public class GateTest2 {

  public static void main(String[] arg) {
    AndGate fa = new AndGate(), fb = new AndGate();
    fa.setName("fa"); fb.setName("fb"); 
    NotGate na = new NotGate(), nb = new NotGate();
    na.setName("a'"); nb.setName("b'");
    NorGate  fe  = new NorGate();
    fe.setName("fe");
    SignalGate a = new SignalGate(), b = new SignalGate();
    a.setName("a"); b.setName("b");
    fa.setInputGate(a); fa.setInputGate(nb);
    fb.setInputGate(na); fb.setInputGate(b);
    na.setInputGate(a);  nb.setInputGate(b);
    fe.setInputGate(fa); fe.setInputGate(fb);
    na.init(); nb.init();
    fa.init(); fb.init(); fe.init();
    System.out.println("a  b  fa  fb  fe"); 
    Gate.setDelay(1);   
    for (int i=0; i<2; i++)
      for (int j=0; j<2; j++) {
        try {
          a.setValue(i==1);
          Thread.sleep(5);
          b.setValue(j==1);      
          Thread.sleep(5);
        }
        catch (InterruptedException e) {}
        System.out.println(value(a) + "  " + value(b) + "  " + 
                           value(fa) + "   " + value(fb) + "   " + value(fe));
      }   
  }

  public static String value(Gate g) {
    return ((g.getOutputValue()) ? "1" : "0");
  }

}
