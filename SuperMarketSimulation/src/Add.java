
import javax.swing.JTextArea;

public class Add extends Thread {
	JTextArea checkoutArea;
	Market market;

	public Add(JTextArea checkoutArea, Market market) {
		this.checkoutArea = checkoutArea;
		this.market = market;
	}
	
	public void run() 
	{
		if((int)(Math.random()*100)%2==0)
		{
			add();
		}
		try {
			sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void add() {

		int min = 0;
		for (int i = 0; i < market.numOfch; i++) {
			if ((market.newCh[min].queue.size() > market.newCh[i].queue.size()) && market.newCh[i].stateofCh == true)
				min = i;
		}
		if ((market.newCh[min].queue.size() < 4) && market.newCh[min].stateofCh == true) //// lost=3
		{
			checkoutArea.append("--A New Customer Add to Checkout " + (min + 1) + " --" + "\n");
			checkoutArea.paintImmediately(checkoutArea.getBounds());
			
			Customer newCus = new Customer(checkoutArea);
			market.newCh[min].queue.add(newCus);
			newCus.belong=market;
			newCus.start();
		} 
		else {
			market.numofLost++;
			checkoutArea.append("--Lost A Customer--"+ "\n");
			checkoutArea.paintImmediately(checkoutArea.getBounds());
		}

	}
}
