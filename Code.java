import java.awt.event.*;
import java.util.Vector;
import javax.swing.*;
import java.text.DecimalFormat;

public class GUI extends JFrame
{
    
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
        int sumOfcus=(int)(Math.random()*1000)%40+1;
        
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

class Add extends Thread
{
    JTextArea checkoutArea;
    Market market;
    
    public Add(JTextArea checkoutArea, Market market) {
        this.checkoutArea = checkoutArea;
        this.market = market;
    }
    
    public void run()//a thread for adding new customer
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
    
    public void add()//add a new customer randomly
    {
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


class Market extends Thread
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

class Checkout extends Thread
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


class Customer extends Thread{
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
    
    public synchronized void run()//thread of this customer, for changing checkout
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



