
import java.awt.event.*;
import java.util.Vector;
import javax.swing.*;

public class GUI extends JFrame{
    Market market;
    private JPanel showPanel;
    private JPanel infoPanel;
    private JButton startButton;
    private JButton compareButton;
    private JLabel inputNum;
    private JTextField checkoutNum;
    private JTextArea checkoutArea=new JTextArea("Please input the number of checkouts(1-8)",25,60);
    int num=0;

    public GUI() 
    {
    	super("Supermarket Simulation");
    	showPanel = new JPanel();
        infoPanel = new JPanel();
        inputNum=new JLabel("Number of checkouts: ");
        infoPanel.add(inputNum);
        
        checkoutNum=new JTextField(10);
        checkoutNum.setText("");
        infoPanel.add(checkoutNum);
        
        startButton=new JButton();
        startButton.setText("Start");
        startButton.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if(!checkoutNum.getText().equals(""))
				{
					checkoutArea.setText("");
					num=Integer.parseInt(checkoutNum.getText());
					if(num>=1&&num<=8)
						createFrame(num);
					else
					{
						checkoutArea.append("--Error, please input again!--"+"\n");
						checkoutArea.paintImmediately(checkoutArea.getBounds());
					}
				}
			}

        });
        infoPanel.add(startButton);
        
        compareButton=new JButton();
        compareButton.setText("Compare");
        compareButton.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				checkoutArea.setText("");
				compare();
			}
        });
        infoPanel.add(compareButton);  
        
        checkoutArea.setLineWrap(true);    
        showPanel.add(infoPanel);
        showPanel.add(new JScrollPane(checkoutArea));
        
        this.setContentPane(showPanel);
		this.setBounds(300,200,800,500);
		this.setResizable(false);
		this.addWindowListener(new WindowAdapter() {
		    public void windowClosing(WindowEvent e) {System.exit(0);}
		});
		this.setVisible(true);
    }


    public static void main(String[] args) {
    	new GUI();
    }
    

    void createFrame(int num)
    {
    	int randnum=num;
		int sumOfcus=(int)(Math.random()*1000)%100+1;
		
		checkoutArea.append("******Supermarket Simulation******"+"\n"+"\n");
		checkoutArea.paintImmediately(checkoutArea.getBounds());
		checkoutArea.append("There are "+sumOfcus+" customer coming into this supermarket!"+"\n");
		checkoutArea.paintImmediately(checkoutArea.getBounds());
		
		int lost=0;
		
		if(sumOfcus>randnum*6)
		{
			lost=sumOfcus-randnum*6;
			sumOfcus=randnum*6;
		}
		
		Vector<Customer> allCustomers = new Vector<Customer>();
		Customer customer;
		
		
		market=new Market(randnum,sumOfcus,allCustomers,false,checkoutArea);
		
		market.numofLost=lost;
		
		for(int i=0;i<sumOfcus;i++)
		{
			customer=new Customer(checkoutArea);
			allCustomers.add(customer);
		}
		market.init(false,0);
		
		checkoutArea.append("There are "+market.numOfch+" checkouts and "+market.sumOfcustomers+" customers in the supermarket to check out."+"\n"+"\n");
		checkoutArea.paintImmediately(checkoutArea.getBounds());

		
		for(int i=0;i<market.numOfch;i++){
			market.newCh[i].display(i+1);
		}
		
		for(int i=0;i<market.numOfch;i++){//checkout thread start
			market.newCh[i].start();
		} 
		
		new Add(checkoutArea, market).start();
		
		for(int i=0;i<market.numOfch;i++){
				synchronized(market.newCh[i].queue){
					for (int y = 0; y < market.newCh[i].queue.size(); y++) {
						//customer thread start
					if(market.newCh[i].queue.elementAt(y).stateofTh==false)
					{
						market.newCh[i].queue.elementAt(y).belong=market;
						market.newCh[i].queue.elementAt(y).start();
						market.newCh[i].queue.elementAt(y).stateofTh=true;
					}
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		} 
		
		for(int i=0;i<market.numOfch;i++){
			try {
				market.newCh[i].join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		checkoutArea.append("Number of lost customers: "+market.numofLost+"\n");
		checkoutArea.paintImmediately(checkoutArea.getBounds());
    }
    
    void compare()
	{
		int sumOfcus=(int)(Math.random()*1000)%30+1;
		
		Vector<Customer> allCustomers = new Vector<Customer>();
		Customer customer;
		
		checkoutArea.append("******Supermarket Compare******"+"\n");
		checkoutArea.append("There are "+sumOfcus+" customers in the supermarket now."+"\n");
		checkoutArea.paintImmediately(checkoutArea.getBounds());
		
		for(int i=0;i<sumOfcus;i++)
		{
			customer=new Customer(checkoutArea);
			allCustomers.add(customer);
			allCustomers.add(customer);
		}
		
		Market market1=new Market(5,sumOfcus,allCustomers,false,checkoutArea);
		Market market2=new Market(6,sumOfcus,allCustomers,false,checkoutArea);
		Market market3=new Market(5,sumOfcus,allCustomers,false,checkoutArea);
		Market market4=new Market(5,sumOfcus,allCustomers,false,checkoutArea);
		
		market1.init(false,0); //(fast,num)
		market2.init(false,0);
		market3.init(true,5);
		market4.init(false,0);

		
		checkoutArea.append("\n"+"--Use 5 checkouts--"+"\n");
		checkoutArea.paintImmediately(checkoutArea.getBounds());
		
		long before1 = System.currentTimeMillis();
		
		for(int i=0;i<market1.numOfch;i++)
		{
			market1.newCh[i].start();
		}
		for(int i=0;i<market1.numOfch;i++)
		{
			try {
				market1.newCh[i].join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		long after1 = System.currentTimeMillis();
		
		checkoutArea.append("\n"+"--Use 6 checkouts--"+"\n");
		checkoutArea.paintImmediately(checkoutArea.getBounds());
		
		long before2 = System.currentTimeMillis();
		
		for(int i=0;i<market2.numOfch;i++)
		{
			market2.newCh[i].start();
		}
		
		for(int i=0;i<market2.numOfch;i++)
		{
			try {
				market2.newCh[i].join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		long after2 = System.currentTimeMillis();
		
		checkoutArea.append("use six checkouts instead of five, save "+((after1-before1)-(after2-before2))+" ms"+"\n");
		
		if(((after1-before1)-(after2-before2))>0)
			checkoutArea.append("It is worth!"+"\n");
		else
			checkoutArea.append("It isn't worth!"+"\n");
		checkoutArea.paintImmediately(checkoutArea.getBounds());
		
		checkoutArea.append("\n"+"--5 checkouts, 1 fast checkout--"+"\n");
		checkoutArea.paintImmediately(checkoutArea.getBounds());
		
		long before3 = System.currentTimeMillis();
		
		for(int i=0;i<market3.numOfch;i++)
		{
			market3.newCh[i].start();
		}
		for(int i=0;i<market3.numOfch;i++)
		{
			try {
				market3.newCh[i].join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		long after3 = System.currentTimeMillis();
		
		checkoutArea.append("use fast checkout, save "+((after1-before1)-(after3-before3))+" ms"+"\n");
		
		if(((after1-before1)-(after3-before3))>0)
			checkoutArea.append("It is useful!"+"\n");
		else
			checkoutArea.append("It isn't useful!"+"\n");
		checkoutArea.paintImmediately(checkoutArea.getBounds());
		
		checkoutArea.append("\n"+"Use new scanners!"+"\n");
		checkoutArea.paintImmediately(checkoutArea.getBounds());
		
		long before4 = System.currentTimeMillis();
		
		for(int i=0;i<market4.numOfch;i++)
		{
			market4.newCh[i].start();
		}
		for(int i=0;i<market4.numOfch;i++)
		{
			try {
				market4.newCh[i].join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		long after4 = System.currentTimeMillis();
		
		checkoutArea.append("use new scanners, save "+((after1-before1)-(after4-before4))+" ms"+"\n");
		
		if(3*(after1-before1)>=5*(after4-before4))
			checkoutArea.append("It is worth buying!"+"\n");
		else
			checkoutArea.append("It isn't worth buying!"+"\n");
		checkoutArea.paintImmediately(checkoutArea.getBounds());
	}
}


 


