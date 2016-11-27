import java.util.Vector;

import javax.swing.JTextArea;

public class Market extends Thread 
{
	Vector<Customer> allCustomers = new Vector<Customer>();//all the customers in this market
	Checkout[] newCh;//all the checkouts in this market
	Vector<Vector<Customer>> queue=new Vector<Vector<Customer>>();//all the queues in all checkouts
	
	int sumOfcustomers;//the total number of customers in this market
	int numofLost;//the number of lost customers
	int numOfch=0;//the number of checkouts
	boolean newflag=false;//if this market uses new scanners
	JTextArea checkoutArea;

	public Market(int numOfch, int sumOfcustomers, Vector<Customer> allCustomers,boolean newflag,JTextArea checkoutArea) {
		this.numOfch = numOfch;
		newCh = new Checkout[this.numOfch];
		this.sumOfcustomers = sumOfcustomers;
		this.allCustomers = allCustomers;
		this.newflag=newflag;
		this.checkoutArea=checkoutArea;
	}

	void init(boolean fastflag, int fastItems) {//initialize all the customers in all the checkouts

		Customer customer;
		Vector<Customer> temp;
		
		for(int i=0;i<numOfch;i++){
			temp=new Vector<Customer>();
			queue.add(temp);
		}
		
		int x=0;
		if(fastflag==true)
		{
			int y = 0;
			for(int i=0;i<sumOfcustomers;i++)
			{
				if(allCustomers.elementAt(i).numOfitems<=fastItems)
				{
					customer=allCustomers.elementAt(i);
					customer.x=0;
					customer.y=y;
					y++;
					queue.elementAt(0).add(customer);
					allCustomers.elementAt(i).fast=true;
				}
				if(queue.elementAt(0).size()==6)
					break;
			}
			x=1;
		}
		
		int c=0,y=0;
		while(c<sumOfcustomers)
		{
			for(int i=x;i<numOfch&&c<sumOfcustomers;i++)
			{
				while(allCustomers.elementAt(c).fast==true)
				{
					c++;
				}
				customer = allCustomers.elementAt(c);
				customer.x = i;
				customer.y = y;
				queue.elementAt(i).add(customer);
				c++;
			}
			y++;
		}
		
		for(int i=0;i<numOfch;i++){
			newCh[i] = new Checkout(queue.elementAt(i).size(), queue.elementAt(i), i, this,checkoutArea);
		}
	}

}
