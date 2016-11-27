import java.text.DecimalFormat;
import java.util.Vector;

import javax.swing.JTextArea;

public class Customer extends Thread
{
	boolean fast=false;//if this customer is in fast checkout
	Vector<Double> items;//items of this customer
	int numOfitems;//the number of products for each trolley
	double numOftime;//the time of scanning each product
	int x,y;//the location of this customer in the market
	DecimalFormat df = new DecimalFormat("######0.0");
	int numofCh;//number of checkouts
	Checkout[] Ch;//supermarket checkouts
	Market belong;//the market the customer belongs to
	boolean stateofTh=false;//if this customer thread started
	JTextArea checkoutArea;
	
	public Customer(JTextArea checkoutArea)//initialize items, numOfitems and numOftime of this customer
	{
		items = new Vector<Double>();
		numOfitems = (int) (Math.random() * 40000) % 5 + 1;//200

		for (int j = 0; j < numOfitems; j++) {

			String text = df.format((Math.random() * 1000 % 5.5) + 0.5);
			numOftime = Double.parseDouble(text);
			items.addElement(numOftime);
		}
		this.checkoutArea=checkoutArea;
	}

	public void run()//thread of this customer, for changing checkout
	{
		Ch = belong.newCh;
		numofCh=belong.numOfch;
		
		if(numofCh>1)
		{

			int min=0;
			for(int i=0;i<numofCh;i++)
			{
				if(Ch[min].queue.size()>Ch[i].queue.size())
					min=i;
			}
			
			synchronized(Ch[this.x].queue)
			{
				if(((Ch[x].queue.size()-Ch[min].queue.size())>=2)&&(Ch[min].stateofCh==true)&&Ch[x].queue.contains(this))
				{
					checkoutArea.append("--Checkout "+(x+1)+" Customer "+(y+1)+" Change Queue To Checkout "+(min+1)+" --"+"\n");
					checkoutArea.paintImmediately(checkoutArea.getBounds());
					Ch[x].queue.remove(this);
					Ch[min].queue.add(this);
				}
			}
		}
		
		try {
			sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public int numOfitems(){
		return this.numOfitems;
	}
	
	public Double eachtime(int i)
	{
		return this.items.elementAt(i);
	}
}
