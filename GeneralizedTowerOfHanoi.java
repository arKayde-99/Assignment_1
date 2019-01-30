import java.util.*;

public class GeneralizedTowerOfHanoi{
   
    /*############################################################################################################################*/ 
    /*................................DOMAIN OF RECURSIVE GENERALIZED TOWERS OF HANOI STARTS......................................*/
    public static void toh_with_recursion(int num_disks, int start_pos, int end_pos){
        if (num_disks==0)
            return;
        int aux_pos=(6-start_pos-end_pos);

        toh_with_recursion(num_disks-1,start_pos,aux_pos);
        System.out.println(start_pos+" "+end_pos);
        toh_with_recursion(num_disks-1,aux_pos,end_pos);
        return;
    }

    public static void gtoh_with_recursion(int num_disks, int start_pos, int r, int b){
        if (num_disks==0)//base case1
            return;

        if (num_disks==1){
            System.out.println(start_pos+" "+b);//base case2
            return;
        }
        if (num_disks==2){
            System.out.println(start_pos+" "+b);//base case3
            System.out.println(start_pos+" "+r);
            return;
        }


        if (start_pos==r){
            toh_with_recursion(num_disks-1,start_pos,6-r-b);
            gtoh_with_recursion(num_disks-1,6-r-b,r,b);//abnormal case1
            return;
        }
        else if (start_pos==b){
            toh_with_recursion(num_disks,start_pos,6-r-b);//abnormal case2
            gtoh_with_recursion(num_disks,6-r-b,r,b);
            return;
        }

        if (num_disks%2==0){
            toh_with_recursion(num_disks-1,start_pos,b);
            System.out.println(start_pos+" "+r);
            toh_with_recursion(num_disks-3,b,start_pos);
            System.out.println(b+" "+r);
            gtoh_with_recursion(num_disks-3,start_pos,r,b);
        }
        else{
            toh_with_recursion(num_disks-1,start_pos,r);
            System.out.println(start_pos+" "+b);
            toh_with_recursion(num_disks-3,r,start_pos);
            System.out.println(r+" "+b);
            gtoh_with_recursion(num_disks-3,start_pos,r,b);
        }
        
        return;        
    }
    /*................................DOMAIN OF RECURSIVE GENERALIZED TOWERS OF HANOI ENDS......................................*/
    /*##########################################################################################################################*/
    
    public static class MyStack{//this stack is used for storing parameters of problems to be solved using toh_without recursion
        public class Node{
            int num_disks,start_pos,r,b;
            Node next;
            Node(int x,int y,int z,int w){//constructor for the node
                num_disks=x; start_pos=y; r=z; b=w;
            }
        }
        Node head=null;
    
        public void push(int x,int y,int z,int w){
            Node a=new Node(x,y,z,w);
            a.next=head;
            head=a;
        }
    
        public Node pop() throws EmptyStackException{
            if (head==null)
                throw new EmptyStackException();

            Node b=head;
            head=head.next;
            return b;
        }
    
        public Node peek() throws EmptyStackException{
            if (head==null)
                throw new EmptyStackException();
            
            return head;
        }
    
        public Boolean empty(){
            if (head==null)
                return true;
            else
                return false;
        }
    }

    static class DiscStack{//this stack will represent the disc on a tower,push would mean putting a disc on this and pop would mean taking the top disc off
        private class Node{
            int diskSize;
            Node next;
            Node(int x){
                diskSize=x;
            }
        }
        Node head=null;;
    
        public void push(int x){
            Node a=new Node(x);
            a.next=head;
            head=a;
        }
    
        public Node pop() throws EmptyStackException{
            if (head==null)
                throw new EmptyStackException();

            Node b=head;
            head=head.next;
            return b;
        }
    
        public Node peek() throws EmptyStackException{
            if (head==null)
                throw new EmptyStackException();
            
            return head;
        }
    
        public Boolean empty(){
            if (head==null)
                return true;
            else
                return false;
        }
    }

    /*$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$*/
    /*................................DOMAIN OF NON-RECURSIVE GENERALIZED TOWERS OF HANOI STARTS................................*/
    public static int pow(int d, int N){
        if (N==0)
            return 1;//base case

        if (N%2==1)//writing N as binary number and then doing multiplication using squaring
            return d*pow(d*d,N/2);
        else
            return pow(d*d,N/2);
    }

    public static void moveBetween(int tow1, int tow2, DiscStack[] TowerStack){
        int diskSize1,diskSize2;
        if (TowerStack[tow1-1].empty())
            diskSize1=1000000;
        else
            diskSize1=TowerStack[tow1-1].peek().diskSize;
       
        if (TowerStack[tow2-1].empty())
            diskSize2=1000000;
        else
            diskSize2=TowerStack[tow2-1].peek().diskSize;

        if (diskSize1>diskSize2 || TowerStack[tow1-1].empty()){//movement will occur from tower 2 to tower 1
            System.out.println(tow2+" "+tow1);
            int temp=TowerStack[tow2-1].pop().diskSize;//taking out disk from tow2 and pushing it on tow1
            TowerStack[tow1-1].push(temp);
        }

        else {//movement of disk will happen from tower 1 to tower 2
            System.out.println(tow1+" "+tow2);
            int temp=TowerStack[tow1-1].pop().diskSize;//taking out disk from tow1 and pushing it on tow2
            TowerStack[tow2-1].push(temp);
        }
        return;
    }

    public static void toh_without_recursion(int num_disks, int start_pos, int end_pos){
        if (num_disks<=0)
         return;
        
        int num_moves,aux_pos=(6-start_pos-end_pos);//number of moves to move N disks is 1+2+4+.....+2^(N-1)
        DiscStack[] TowerStack=new DiscStack[3];

        TowerStack[0]=new DiscStack();//created three stacks that represent each tower and values of disk are stacked in these stacks
        TowerStack[1]=new DiscStack();
        TowerStack[2]=new DiscStack();

        for (int i=num_disks;i>=1;i--)//in the stack of start tower N disks are stacked with biggest one at bottom
            TowerStack[start_pos-1].push(i);

        if (num_disks%2==0){//if there are even number of disks then first move is from start to auxillary
            int temp=aux_pos;
            aux_pos=end_pos;
            end_pos=temp;
        }

        for (num_moves=1;num_moves<=(pow(2,num_disks)-1);num_moves++){
            if ((num_moves%3)==1)
                moveBetween(start_pos,end_pos,TowerStack);
            else if ((num_moves%3)==2)
                moveBetween(start_pos,aux_pos,TowerStack);
            else
                moveBetween(aux_pos,end_pos,TowerStack);
        } 
        return;
    }
    
    public static void gtoh_without_recursion(int num_disks, int start_pos, int r, int b){
        MyStack ProblemStack=new MyStack();
        int count;

        if (start_pos==r){//abnormal case 1
            toh_without_recursion(num_disks-1,start_pos,6-r-b);
            num_disks=num_disks-1; 
            start_pos=6-r-b;            
        }
        else if (start_pos==b){//abnormal case 2
            toh_without_recursion(num_disks,start_pos,6-r-b);
            start_pos=6-r-b;            
        }
        
        count=num_disks%3;
        if (count<3)
            count=count+3;
        while (count<=num_disks && num_disks>2){
            ProblemStack.push(count,start_pos,r,b);
            count=count+3;
        }

        while (!ProblemStack.empty()){
            MyStack.Node i=ProblemStack.pop();
            if (i.num_disks%2==0){
                toh_without_recursion(i.num_disks-1,i.start_pos,i.b);
                System.out.println(i.start_pos+" "+i.r);
                toh_without_recursion(i.num_disks-3,i.b,i.start_pos);
                System.out.println(i.b+" "+i.r);
            }
            else {
                toh_without_recursion(i.num_disks-1,i.start_pos,i.r);//modify code here
                System.out.println(i.start_pos+" "+i.b);
                if (i.num_disks==3){
                    System.out.println(i.r+" "+i.b);
                    return;
                }
                toh_without_recursion(i.num_disks-3,i.r,i.start_pos);
                System.out.println(i.r+" "+i.b);
            }
        }
        if ((num_disks%3)==1)
            System.out.println(start_pos+" "+b);
        else if ((num_disks%3)==2){
            System.out.println(start_pos+" "+b);
            System.out.println(start_pos+" "+r);
        } 
        return;           
    }
    
    /*................................DOMAIN OF NON-RECURSIVE GENERALIZED TOWERS OF HANOI ENDS..................................*/
    /*$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$*/

    public static void main(String[] args){
        int num_disks,start_pos,r,b;
        Scanner s=new Scanner(System.in);
        System.out.println("Enter the number of disks, start position, final position of red disks, final position of black disks");
        num_disks=s.nextInt(); start_pos=s.nextInt(); r=s.nextInt(); b=s.nextInt();

        System.out.println("Solving Generalized Towers of Hanoi using recursion...");
        gtoh_with_recursion(num_disks,start_pos,r,b);
        System.out.print("\n\n");
        System.out.println("Solving Generalized Towers of Hanoi without recursion...");
        gtoh_without_recursion(num_disks,start_pos,r,b);
        return;
    }
}