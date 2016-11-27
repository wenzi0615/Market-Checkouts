import java.text.DecimalFormat;
import java.util.Vector;
import javax.swing.JTextArea;

public class Checkout extends Thread
{
	boolean stateofCh=false;//if this checkout thread started
	int numOfcustomers;//number of customers in this checkout
	int no;//No. of this checkout in the market
	Vector<Customer> queue;//the queue of customers of this checkout
	DecimalFormat df = new DecimalFormat("######0.0");
	Market market;//the market the customer belongs to
	JTextArea checkoutArea;

	public Checkout(int numOfcustomers,Vector<Customer> queue,int no, Market market,JTextArea checkoutArea)
	{
		this.numOfcustomers=numOfcustomers;
		this.queue=queue;
		this.no=no;
		this.market=market;
		this.checkoutArea=checkoutArea;
	}

	public void run() {//thread of this checkout, for customers check out
		stateofCh=true;
		check();
	}
	
	void display(int num)//print the information of customers of this checkout
	{
		if(market.newCh[num-1].queue.size()==0)
			return;
		
		checkoutArea.append("customers of checkout "+(num)+" : "+numOfcustomers+"\n");
		checkoutArea.paintImmediately(checkoutArea.getBounds());
		
		double itemOfcheckout=0;
		double timeOfcheckout=0;
		double timeOfcustomer=0;

		for (int i = 0; i < numOfcustomers; i++) 
		{
			checkoutArea.append("customer"+(i+1)+"	"+"numOfitems:"+queue.elementAt(i).numOfitems()+"	");
			checkoutArea.append("Each:");
			
			for (int j = 0; j < queue.elementAt(i).numOfitems(); j++) {
					double temp=Double.parseDouble(String.valueOf(queue.elementAt(i).eachtime(j)));
					checkoutArea.append(queue.elementAt(i).eachtime(j)+"	");
					timeOfcustomer=timeOfcustomer+temp;
			}
			
			timeOfcheckout=timeOfcustomer;
			itemOfcheckout=itemOfcheckout+queue.elementAt(i).numOfitems();
			
			checkoutArea.append("\n"+"total wait time for this customer:"+df.format(timeOfcustomer)+"s"+"\n");
			checkoutArea.paintImmediately(checkoutArea.getBounds());
		}
		checkoutArea.append("total utilization for this checkout:"+df.format(timeOfcheckout)+"s"+"\n");
		checkoutArea.append("average customer wait time:"+df.format(timeOfcheckout/numOfcustomers)+"s"+"\n");
		checkoutArea.append("average products per trolley:"+df.format(itemOfcheckout/numOfcustomers)+"s"+"\n"+"\n");
		checkoutArea.paintImmediately(checkoutArea.getBounds());
	}
	
	void check()
	{
		while(queue.size()!=0)
		{
		
			double timeOffirst=0;
			for(int j=0;j<queue.elementAt(0).numOfitems();j++)
				timeOffirst+=Double.parseDouble(String.valueOf(queue.elementAt(0).eachtime(j)));
			if(market.newflag=true)
				timeOffirst=timeOffirst/5;
			
			try {
				sleep((long) (timeOffirst*100));//1000
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			checkoutArea.append("Checkout "+(no+1)+"： One customer checkout is completed!"+"\n");
			checkoutArea.paintImmediately(checkoutArea.getBounds());
			
			if(queue.size()!=0)
				queue.remove(0);
			if(queue.size()==0){
				try {
					sleep(3000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(queue.size()==0)
					stateofCh=false;
			}
		}
		checkoutArea.append("Checkout "+(no+1)+" finish!"+"\n");
		checkoutArea.paintImmediately(checkoutArea.getBounds());
	}
}
