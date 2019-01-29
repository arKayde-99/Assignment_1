import java.util.*;

public class GeneralizedTowerOfHanoi{
    class MyStack{
        private class E{
            //define the class E
        }
    
        private class Node{
            E data=new E();
            Node next;
        }
        private Node head=null;;//reference to head of linked list which points to null in beginning
    
        public void push(E item){//push means adding a node to head of linked list and storing latest element in it
            Node a=new Node();
            a.data=item;
            a.next=head;
            head=a;
        }
    
        public E pop() throws EmptyStackException{//pop means removing a node from the head of the linked list since it has latest element
            if (head==null)
                throw new EmptyStackException();

            Node b=head;
            head=head.next;
            return b.data;
        }
    
        public E peek() throws EmptyStackException{
            if (head==null)
                throw new EmptyStackException();
            
            return head.data;
        }
    
        public Boolean empty(){
            if (head==null)
                return true;
            else
                return false;
        }
    }

    public static void toh_with_recursion(int num_disks, int start_pos, int end_pos){//used in gtoh_with_recursion
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

    public static void gtoh_without_recursion(int num_disks, int start_pos, int r, int b){
        
    }
    
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