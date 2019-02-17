import java.util.*;
import java.lang.*;

class CompanyHierarchy{
    static class SearchSequence{
        String name;
        Node next;
    }

    public class LevelList{
        class StoreNode{
            String name;
            StoreNode next;
        }

        class LevelNode{//a level node stores all employees at a given level
            int level;
            StoreNode head;
            LevelNode next;
        }

        LevelNode CEONode=new LevelNode();
        CEONode.level=1;

        void AddWorker(String s,int x){// adds a worker in the level list
            LevelNode t=CEONode;
            while (t.level<x && t.next!=null)
                t=t.next;
            while (t.next==null && t.level<x){
                LevelNode p=new LevelNode();
                p.level=t.level+1;
                t.next=p;
                t=p;
            }//now  t contains the pointer to level node with given level
            if (t.head==null){
                StoreNode q=new StoreNode();
                q.name=s;
                t.head=q;
                return;
            }

            StoreNode q;
            q=t.head;
            while (q.next!=null)
                q=q.next;
            StoreNode r=new StoreNode();
            r.name=s;
            q.next=r;
            return;
        }

        void RemoveWorker(String s,int x){
            LevelNode t=CEONode;
            while (t.level<x)
                t=t.next;
            StoreNode p=t.head;
            if (s.compareTo(p.name)==0)
                t.head=t.head.next;
            else {
                StoreNode q=p;
                p=p.next;
                while (s.compareTo(p.name)!=0){
                    q=p;
                    p=p.next;
                }//now p contains the pointer to the node storing the employee that has to be deleted
                q.next=p.next;
            }
            return;
        }
    }

    public class SearchTree{
        public class BSTNode{
            String name;
            String ib;//contains name of immediate boss
            int level;
            BSTNode leftChild,rightChild,parent;
        }
        BSTNode root=new BSTNode();
        
        void fillEmployee(BSTNode t,String s,String b,int v){
            t.name=s;
            t.level=v;
            t.ib=b;
            t.leftChild=new BSTNode();
            t.rightChild=new BSTNode();
            t.leftChild.parent=t;
            t.rightChild.parent=t;
        }

        Boolean isLeaf(BSTNode t){
            return (t.name==null);
        }

        void createST(String s,String b,int v){//this method creates a binary search tree based on names
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
                fillEmployee(t,s,b,v);
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

        SearchSequence FindWay(String s,SearchSequence head){//this method returns a linked list of names of how to reach a particular employee from root
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

        //debugging function 1
        public static int lengthOfSearchSeq(SearchSequence head){
            SearchSequence p=head;
            while (p!=null){
                count++;
                p=p.next;
            }
            return count;
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
        class ListNode{//defining a node of the list that stores the children of an employee node
            EmployeeNode child;
            ListNode next;
        }
        
        public class EmployeeNode{//defining a node of the tree that stores employees
            String name;
            int level;
            EmployeeNode boss;
            ListNode head;//head is the pointer to the first node of the list of children
            int numChildren;
        
            void addChild(EmployeeNode x){//adding a child to the list of children
                ListNode l=new ListNode;
                l.child=x;
                l.child.level=level+1;
                l.next=head;
                head=l;
                numChildren=countChildren();
                return;
            }
        
            void removeChild throws EmptyListException(EmployeeNode x){//function to remove a child from the list of children
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
                numChildren=countChildren();
                return;
            }

            int countChildren(){
                ListNode p=head;
                for (int count=0;p!=null;count++)
                    p=p.next;
                return count;
            }
            numChildren=countChildren();
        }
        EmployeeNode CEO=new EmployeeNode();
        CEO.level=1;
        CEO.boss=CEO;
        
        SearchTree btree=new SearchTree();
        //making a binary search tree of names and a level array out of the Hierarchy tree as we make the hierarchy tree
        LevelList llist=new LevelList();
        //making a 2D list of employees with a row containing all employees of same level

        public void AddElementDS(SearchTree tree,LevelList arr,EmployeeNode p){
            tree.createST(p.name,p.boss.name,p.level);
            arr.AddWorker(p.name,p.level);
        }

        void DeleteElementDS(SearchTree LevelList arr,EmployeeNode p){
            tree.DeleteNode(tree.SearchName(p.name));
            arr.RemoveWorker(p.name,p.level);            
        }

        EmployeeNode getToName(String name){
            SearchSequence a=null;
            a=btree.FindWay(name,a);//a is now the head to linked list containing the path to boss from root
            EmployeeNode p=CEO;
            while (a.next!=null){
                a=a.next;
                ListNode l=p.head;
                while ((a.name).compareTo(l.child.name)!=0 && l!=null)
                    l=l.next;
                if (l==null){
                    System.out.println("Some employee in the chain of getting to S is giving BT");
                    return;
                }
                p=l.child;
            }//now we have the pointer p which points to the employee in the company found in k.logn time where k is the level of the employee
            return p;
        }

        public void AddEmployee(String employee,String boss){//this function adds an employee under the given boss in the hierarchy tree and also updates the binary tree
            p=getToName(boss);
            EmployeeNode e=new EmployeeNode();
            e.name=employee;
            e.boss=p;
            p.addChild(e);
            AddElementDS(btree,llist,e);
        }

        public void DeleteEmployee(String firedEmployee,String newHead){//Deletes an employee and transfers it's children to another employee at same level
            EmployeeNode a,b,t;
            a=getToName(firedEmployee);
            b=getToName(newHead);

            if (a.level!=b.level){
                System.out.println("ERROR: The level of the two given employees is not equal");
                return;
            }
            DeleteElementDS(btree,llist,a);//deleting this employee from search tree and level array
            t=a.boss;
            t.removeChild(a);//breaking connections b/w fired employee and it's boss
            a.boss=null;

            ListNode l=a.head;
            while (l!=null){
                l.child.boss=b;//making b the boss of fired employee's children
                l=l.next;
            }

            l=b.head;
            if (l==null){
                b.head=a.head;
            }                  //merging the list of children of a and b
            else {
                while (l.next!=null)
                    l=l.next;
                l.next=a.head;
            }
        }//delete employee function ends

        public void LowestCommonBoss(String workerA,String workerB){//finds out the lowest common boss of two employees
            EmployeeNode a=getToName(workerA);
            EmployeeNode b=getToName(workerB);

            while (a.level<b.level)
                b=b.boss;
            while (a.level>b.level)//can be made so much better
                a=a.boss;

            while (a!=b){
                a=a.boss; b=b.boss;
            }
            System.out.println(a.name);
        }

        public void PrintEmployees(){
            LevelNode level=llist.CEONode;
            while (level!=null){
                StoreNode worker=level.head;
                System.out.print("Employees at level "+level.level+": ");
                while (worker!=null){
                    System.out.print(worker.name+" ");
                    worker=worker.next;
                }
                System.out.print("\n");
                level.next;
            }
        }//print employees function ends
    }

    public static void main(String[] args){//main function for testing
        int num_employees,count;
        Scanner s=new Scanner(System.in);
        System.out.println("Enter the number of employees");
        num_employees=s.nextInt();
        HierarchyTree htree=new HierarchyTree();
        System.out.println("Enter the initial employee names along with boss names");

        for (count=0;count<num_employees;count++){
            String a=s.nextString(); String b=s.nextString();
            if (count=0){
                htree.CEO.name=a;
                htree.AddElementDS(htree.btree,htree.llist,CEO);
                htree.AddEmployee(b,a);
                count++;
            }
            else
                htree.AddEmployee(a,b);
        }
    }
}