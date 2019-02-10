import java.util.*;
import java.lang.*;

class CompanyHierarchy{
    public static class SearchTree{
        class SearchSequence{
            String name;
            Node next;
        }

        class BSTNode{
            String name;
            String immediateBoss;
            int level;
            BSTNode leftChild,rightChild,parent;
        }
        BSTNode root=new BSTNode();
        
        void fillEmployee(BSTNode t,String s,String ib,int v){
            t.name=s;
            t.level=v;
            t.immediateBoss=ib;
            t.leftChild=new BSTNode();
            t.rightChild=new BSTNode();
            t.leftChild.parent=t;
            t.rightChild.parent=t;
        }

        Boolean isLeaf(BSTNode t){
            return (t.name==null);
        }

        void createST(String s,String ib,int v){//this method creates a binary search tree based on names
            BSTNode t=root;
            while (!isLeaf(t)){
                if (s.compareTo(t.name)<0)
                    t=t.leftChild;
                else if (s.compareTo(t.name)>0)
                    t=t.rightChild;
                else
                    break;
            }
            if (isLeaf(t))
                fillEmployee(t,s,ib,v);
        }

        BSTNode SearchName(String s){
            BSTNode t=root;
            while (s.compareTo(t.name)==0){
                if (t==leaf)
                    break;
                else if (s.compareTo(t.name)<0)
                    t=t.leftChild;
                else 
                    t=t.rightChild;
            }//now t has the pointer to node that contains the name
            if (isLeaf(t)){
                System.out.println("No such employee exists");
                return null;
            }
            return t;
        }

        SearchSequence FindWay(String s,SearchSequence head){//this method returns a linked list of names of how to reach a particular employee from boss
            BSTNode t=SearchName(s);
            SearchSequence p=new SearchSequence();
            p.name=t.name;
            p.next=head;
            head=p;
            if (t.level==1)
                return p;
            else
                return FindWay(t.ib,head);
        }

        void DeleteNode(BSTNode t){//this method deletes the name of an employee from the Binary Search Tree for names
            if (t==leaf){//exception 1
                System.out.println("No such Employee exists");
                return;
            }

            if (isLeaf(t.leftChild) && isLeaf(t.rightChild)){//when both children are leaf
                t=new BSTNode();
                return;
            }
            else if (!isLeaf(t.leftChild) && !isLeaf(t.rightChild)){//when it is an internal node
                BSTNode q=t.rightChild;
                while (!isLeaf(q.leftChild))
                    q=q.leftChild;
                t.name=q.name;
                DeleteNode(q);
            }
            else {//when one child is leaf and other is not leaf
                BSTNode internalChild;
                if (isLeaf(t.leftChild))
                    internalChild=t.rightChild;
                else
                    internalChild=t.rightChild;
                
                if (t.parent.leftChild==t){
                    t.parent.leftChild=internalChild;
                    return;
                }
                else{
                    t.parent.rightChild=internalChild;
                    return;
                }
            }
        }

    }

    public static class HierarchyTree{
        class ListNode{
            EmployeeNode child;
            ListNode next;
        }
        
        class EmployeeNode{
            String name;
            int level;
            Node boss;
            ListNode head;//head is the pointer to the first node of the list of children
        
            public void addChild(EmployeeNode x){
                ListNode l=new ListNode;
                l.child=x;
                l.child.level=level+1;
                l.child.boss=x;
                l.next=head;
                head=l;
            }
        
            public void removeChild throws EmptyListException(EmployeeNode x){
                if (head==null)
                    throw new EmptyListException();
                ListNode p=head,q=p;
                while (p.child!=x && p!=null){
                    q=p;
                    p=p.next;
                }
                if (p==null){
                    System.out.println("No such Child found");
                    return;
                }
                if (q==p)
                    head=head.next;
                else 
                    q.next=p.next;
            }

        }
    }
}