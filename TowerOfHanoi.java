import java.util.*;

public class TowerOfHanoi{

    static class MyStack{
        private class Node{
            int diskSize;
            Node next;
            Node (int x){//constructor for node
                diskSize=x;
            }
        }
        Node head=null;;//reference to head of linked list which points to null in beginning
    
        public void push(int x){//push means adding a node to head of linked list and storing latest element in it
            Node a=new Node(x);
            a.next=head;
            head=a;
        }
    
        public Node pop() throws EmptyStackException{//pop means removing a node from the head of the linked list since it has latest element
            if (head==null){
                throw new EmptyStackException();
    
            }
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

    public static void toh_with_recursion(int num_disks, int start_pos, int end_pos){
        if (num_disks==0)
            return;
        int aux_pos=(6-start_pos-end_pos);//auxillary pole which can be used to transfer from start_pos to end_pos

        toh_with_recursion(num_disks-1,start_pos,aux_pos);//shift n-1 disks from start to auxillary pole
        System.out.println(start_pos+" "+end_pos);//shift biggest disk from start to end pole
        toh_with_recursion(num_disks-1,aux_pos,end_pos);//shift n-1 disks from auxillary to end pole
        return;
    }

    public static int pow(int d, int N){
        if (N==0)
            return 1;//base case

        if (N%2==1)//writing N as binary number and then doing multiplication using squaring
            return d*pow(d*d,N/2);
        else
            return pow(d*d,N/2);
    }

    public static void moveBetween(int tow1, int tow2, MyStack[] TowerStack){
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
        int num_moves,aux_pos=(6-start_pos-end_pos);//number of moves to move N disks is 1+2+4+.....+2^(N-1)
        MyStack[] TowerStack=new MyStack[3];

        TowerStack[0]=new MyStack();//created three stacks that represent each tower and values of disk are stacked in these stacks
        TowerStack[1]=new MyStack();
        TowerStack[2]=new MyStack();

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

    public static void main(String[] args){//main function for testing functions
        int num_disks,start_pos,end_pos;
        Scanner s=new Scanner(System.in);
        System.out.println("Enter the number of disks, start position, end position");
        num_disks=s.nextInt(); start_pos=s.nextInt(); end_pos=s.nextInt();

        System.out.println("Solving Towers of Hanoi using recursion...");
        toh_with_recursion(num_disks,1,3);
        System.out.print("\n\n");
        System.out.println("Solving Towers of Hanoi without recursion...");
        toh_without_recursion(num_disks,1,3);
        return;
    }
}