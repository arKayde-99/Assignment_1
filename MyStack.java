import java.util.*;

public class MyStack{
    private class E{
        //define the class E
    }

    private class Node{
        //defining the methods and fields in class E
        E data=new E();
        Node next;
    }
    private Node head=null;;//reference to head of linked list which points to null in beginning

    public void push(E item){//push means adding a node to head of linked list and storing latest element in it
        private Node a=new Node();
        a.data=item;
        a.next=head;
        head=a;
    }

    public E pop() throws EmptyStackException{//pop means removing a node from the head of the linked list since it has latest element
        if (head==null){
            throw new EmptyStackException();
            return;
        }
        private Node b=head;
        head=head.next;
        return b.data;
    }

    public E peek() throws EmptyStackException{
        if (head==null){
            throw new EmptyStackException();
            return;
        }
        return head.data;
    }

    public Boolean empty(){
        if (head==null)
            return true;
        else
            return false;
    }

    /*public static void main(String[] args){
        //.....suppose main function contents in which we define a stack
        MyStack stack1=new MyStack();

        try {
            stack1.pop();
        } catch (EmptyStackException e) {
            System.out.println("The Stack is empty");
        }

        try {
            stack1.peek();
        } catch (EmptyStackException e) {
            System.out.println("The Stack is empty")
        }
    }*/
}