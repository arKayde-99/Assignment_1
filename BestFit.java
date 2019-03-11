import java.util.*;
import java.lang.*;
import java.io.*;

public class BestFit(){

    public class MyList{
        class Box{
            int object_id;
            int object_size;
            Box next;
            Box prev;
        }
        int length=0;
        Box HEADER=new Box();
        Box TRAILER=new Box();

        public Box Add_Box(int identity, int size){
            Box p=new Box();
            p.object_id=identity; p.object_size=size;

            if (HEADER.next==TRAILER){
                p.prev=HEADER; p.next=TRAILER;
                HEADER.next=p; TRAILER.prev=p;
            }
            else {
                Box q=HEADER.next;
                p.prev=HEADER; HEADER.next=p;
                p.next=q; q.prev=p;
            }
            length=length+1;
            return p;
        }

        public void Delete_Box(Box location){
            Box a=location.prev;
            Box b=location.next;
            a.next=b;
            b.prev=a;
            length=length-1;
        }
    }

    public class BinTree{
        public class Node{// a node will correspond to one bin
            Integer ID;
            Integer capacity;
            MyList objectlist;
            int height;
            Node leftChild;
            Node rightChild;
            Node parent;

            Node(){
                height=0;
            }

            Boolean isEmpty(){
                return (capacity==null);
            }
        }

        public void fillNode(Node t,int x,int y){
            t.capacity=y;
            t.ID=x;
            t.leftChild=new Node();
            t.rightChild=new Node();
            t.leftChild.parent=t;
            t.rightChild.parent=t;

            while (t!=null){
                t.height=1+Math.max(t.leftChild.height,t.rightChild.height);
                t=t.parent;
            }
        }

        Node root=new Node();

        public Node SearchNode(int key){
            Node t=root;

            while (t.capacity!=key && !t.isEmpty()){
                if (t.capacity<key)
                    t=t.rightChild;
                else if (t.capacity>key)
                    t=t.leftChild;
            }
            return t;
        }

        void case1(Node x,Node y,Node z){
            Node t3=y.rightChild; y.parent=z.parent;
            z.parent=y; y.rightChild=z;
            z.leftChild=t3; t3.parent=z;

            if (y.parent==null)
                root=y;
            else if (y.parent.leftChild==z)
                y.parent.leftChild=y;
            else    
                y.parent.rightChild=y;
            return;
        }

        void case2(Node x,Node y,Node z){
            Node t2=y.leftChild;  y.parent=z.parent;
            z.parent=y; y.leftChild=z;
            z.rightChild=t2; t2.parent=z;
           
            if (y.parent==null)
                root=y;
            else if (y.parent.leftChild==z)
                y.parent.leftChild=y;
            else    
                y.parent.rightChild=y;
            return;
        }

        void case3(Node x,Node y,Node z){
            Node t2=x.leftChild; Node t3=x.rightChild;
            y.rightChild=t2; t2.parent=y;
            z.leftChild=t3; t3.parent=z;
            x.leftChild=y; x.rightChild=z;
            x.parent=z.parent;
            z.parent=x; y.parent=x;
            if (x.parent==null)
                root=x;
            else if (x.parent.leftChild==z)
                x.parent.leftChild=x;
            else
                x.parent.rightChild=x;
            return;
        }

        void case4(Node x,Node y,Node z){
            Node t2=x.leftChild; Node t3=x.rightChild;
            y.leftChild=t3; t3.parent=y;
            z.rightChild=t2; t2.parent=z;
            x.leftChild=z; x=rightChild=y;
            x.parent=z.parent;
            z.parent=x; y.parent=x;
            if (x.parent==null)
                root=x;
            else if (x.parent.leftChild==z)
                x.parent.leftChild=x;
            else
                x.parent.rightChild=x;
            return;         
        }

        void reStructure(Node z){//function to restructure the AVL Tree by rotations
            while (z!=null){
                heightDiff=z.leftChild.height=z.rightChild.height;
                if (heightDiff<-2 || heightDiff>2){
                    Node x,y;
                    if (z.rightChild.height>z.leftChild.height)
                        y=z.rightChild;
                    else   
                        y=z.leftChild;
                    
                    if (y.rightChild.height>y.leftChild.height)
                        x=y.rightChild;
                    else if (y.rightChild.height<y.leftChild.height)  
                        x=y.leftChild;
                    else {
                        if (y==z.rightChild)
                            x=y.rightChild;
                        else
                            x=y.leftChild;
                    }

                    if (y==z.leftChild && x==y.leftChild)//CASE 1 or left-left
                        case1(x,y,z);
            
                    else if (y==z.rightChild && x==y.rightChild)//CASE 2 or right-right
                        case2(x,y,z);

                    else if (y==z.leftChild && x==y.rightChild)//CASE 3 or left-right
                        case3(x,y,z);

                    else //CASE 4 or right-left
                        case4(x,y,z);
            
                    x.height=1+Math.max(x.leftChild.height,x.rightChild.height);
                    y.height=1+Math.max(y.leftChild.height,y.rightChild.height);
                    z.height=1+Math.max(z.leftChild.height,z.rightChild.height);
        
                    Node t=y;
                    while (t!=null){
                        t.height=1+Math.max(t.leftChild.height,t.rightChild.height);
                        t=t.parent;
                    }
                }
                z=z.parent;
            }
        }

        public void Add_Node(int bin_id,int capacity){
            Node z=SearchNode(capacity);
            if (z.isEmpty()){
                fillNode(z,bin_id,capacity);
            }
            else {
                //get the inOrder successor of this node and add another similar node before it
            }
            reStructure(z);
        }//add node function ends
        
        public void Delete_Node(Node t){//function to delete a node in this tree
            if (t.leftChild.isEmpty() && t.rightChild,isEmpty()){//when both children are leaf
                if (t.parent.leftChild==t)
                    t.parent.leftChild=new Node();
                else
                    t.parent.rightChild=new Node();
                t.parent=null;
                reStructure(t);
            }
            else if (!t.leftChild.isEmpty() && !t.rightChild.isEmpty()){//when it is an internal node
                Node q=t.rightChild;
                while (!q.leftChild.isEmpty())
                    q=q.leftChild;
                t.ID=q.ID;
                t.capacity=q.capacity;
                t.location=q.location;
                t.objectlist=q.objectlist;
                Delete_Node(q);
            }
            else {//when one child is leaf and other is not leaf
                Node internalChild;
                if (t.leftChild.isEmpty())
                    internalChild=t.rightChild;
                else
                    internalChild=t.rightChild;
                
                if (t.parent.leftChild==t)
                    t.parent.leftChild=internalChild;
                else
                    t.parent.rightChild=internalChild;
                restructure(t);
                return;
            }            
        }//delete function ends

        public Node getMax(){
            Node t=root;
            while (!t.isEmpty())
                t=t.rightChild;
            if (t!=root)
                return t.parent;
            else 
                return root;
        }
    }

    public class IDTree{
        public class Piece{// a piece will correspond to one piece
            Integer ID;
            Node theBin;
            int height;
            Node leftChild;
            Node rightChild;
            Node parent;

            Piece(){
                height=0;
            }

            Boolean isEmpty(){
                return (ID==null);
            }
        }

        public void fillPiece(Piece t,int x,Node y){
            t.ID=x;
            t.theBin=y;
            t.leftChild=new Piece();
            t.rightChild=new Piece();
            t.leftChild.parent=t;
            t.rightChild.parent=t;

            while (t!=null){
                t.height=1+Math.max(t.leftChild.height,t.rightChild.height);
                t=t.parent;
            }
        }

        Piece Iroot=new Piece();

        public Piece SearchPiece(int key){
            Piece t=Iroot;

            while (t.ID!=key && !t.isEmpty()){
                if (t.ID<key)
                    t=t.rightChild;
                else if (t.ID>key)
                    t=t.leftChild;
            }
            return t;
        }

        void case1(Piece x,Piece y,Piece z){
            Piece t3=y.rightChild; y.parent=z.parent;
            z.parent=y; y.rightChild=z;
            z.leftChild=t3; t3.parent=z;

            if (y.parent==null)
                Iroot=y;
            else if (y.parent.leftChild==z)
                y.parent.leftChild=y;
            else    
                y.parent.rightChild=y;
            return;
        }

        void case2(Piece x,Piece y,Piece z){
            Piece t2=y.leftChild;  y.parent=z.parent;
            z.parent=y; y.leftChild=z;
            z.rightChild=t2; t2.parent=z;
           
            if (y.parent==null)
                Iroot=y;
            else if (y.parent.leftChild==z)
                y.parent.leftChild=y;
            else    
                y.parent.rightChild=y;
            return;
        }

        void case3(Piece x,Piece y,Piece z){
            Piece t2=x.leftChild; Piece t3=x.rightChild;
            y.rightChild=t2; t2.parent=y;
            z.leftChild=t3; t3.parent=z;
            x.leftChild=y; x.rightChild=z;
            x.parent=z.parent;
            z.parent=x; y.parent=x;
            if (x.parent==null)
                Iroot=x;
            else if (x.parent.leftChild==z)
                x.parent.leftChild=x;
            else
                x.parent.rightChild=x;
            return;
        }

        void case4(Piece x,Piece y,Piece z){
            Piece t2=x.leftChild; Piece t3=x.rightChild;
            y.leftChild=t3; t3.parent=y;
            z.rightChild=t2; t2.parent=z;
            x.leftChild=z; x=rightChild=y;
            x.parent=z.parent;
            z.parent=x; y.parent=x;
            if (x.parent==null)
                Iroot=x;
            else if (x.parent.leftChild==z)
                x.parent.leftChild=x;
            else
                x.parent.rightChild=x;
            return;         
        }

        void reStructure(Piece z){//function to restructure the AVL Tree by rotations
            while (z!=null){
                heightDiff=z.leftChild.height=z.rightChild.height;
                if (heightDiff<-2 || heightDiff>2){
                    Piece x,y;
                    if (z.rightChild.height>z.leftChild.height)
                        y=z.rightChild;
                    else   
                        y=z.leftChild;
                    
                    if (y.rightChild.height>y.leftChild.height)
                        x=y.rightChild;
                    else if (y.rightChild.height<y.leftChild.height)  
                        x=y.leftChild;
                    else {
                        if (y==z.rightChild)
                            x=y.rightChild;
                        else
                            x=y.leftChild;
                    }

                    if (y==z.leftChild && x==y.leftChild)//CASE 1 or left-left
                        case1(x,y,z);
            
                    else if (y==z.rightChild && x==y.rightChild)//CASE 2 or right-right
                        case2(x,y,z);

                    else if (y==z.leftChild && x==y.rightChild)//CASE 3 or left-right
                        case3(x,y,z);

                    else //CASE 4 or right-left
                        case4(x,y,z);
            
                    x.height=1+Math.max(x.leftChild.height,x.rightChild.height);
                    y.height=1+Math.max(y.leftChild.height,y.rightChild.height);
                    z.height=1+Math.max(z.leftChild.height,z.rightChild.height);
        
                    Piece t=y;
                    while (t!=null){
                        t.height=1+Math.max(t.leftChild.height,t.rightChild.height);
                        t=t.parent;
                    }
                }
                z=z.parent;
            }
        }

        public void Add_Piece(int bin_id,Node bin_loc){
            Piece z=SearchPiece(bin_id);
            if (z.isEmpty()){
                fillPiece(z,bin_id,bin_loc);
            }
            else {
                //get the inOrder successor of this node and add another similar node before it
            }
            reStructure(z);
        }//add node function ends
        
        public void Delete_Piece(Piece t){//function to delete a node in this tree
            if (t.leftChild.isEmpty() && t.rightChild,isEmpty()){//when both children are leaf
                if (t.parent.leftChild==t)
                    t.parent.leftChild=new Piece();
                else
                    t.parent.rightChild=new Piece();
                t.parent=null;
                reStructure(t);
            }
            else if (!t.leftChild.isEmpty() && !t.rightChild.isEmpty()){//when it is an internal node
                Piece q=t.rightChild;
                while (!q.leftChild.isEmpty())
                    q=q.leftChild;
                t.ID=q.ID;
                t.theBin=q.theBin;
                Delete_Piece(q);
            }
            else {//when one child is leaf and other is not leaf
                Piece internalChild;
                if (t.leftChild.isEmpty())
                    internalChild=t.rightChild;
                else
                    internalChild=t.rightChild;
                
                if (t.parent.leftChild==t)
                    t.parent.leftChild=internalChild;
                else
                    t.parent.rightChild=internalChild;
                restructure(t);
                return;
            }            
        }//delete function ends
    }

    public class ObjectTree{// this is an AVL Tree of objects
        public class Link{
            Integer obj_id;
            Node storing_bin;
            Box location;
            Link leftChild;
            Link rightChild;
            Link parent;
            int height;

            Link(){
                height=0;
            }

            Boolean isEmpty(){
                return (obj_id==null)
            }
        }

        Link ORoot=new Link();

        public void fillLink(Link t,int ID,Node bin,Box loc){
            t.obj_id=ID;
            t.storing_bin=bin;
            t.location=loc;
            t.leftChild=new Node();
            t.rightChild=new Node();
            t.leftChild.parent=t;
            t.rightChild.parent=t;

            while (t!=null){
                t.height=1+Math.max(t.leftChild.height,t.rightChild.height);
                t=t.parent;
            }
        }
        
        public Link SearchLink(int key){
            Link t=ORoot;

            while (t.obj_id!=key && !t.isEmpty()){
                if (t.obj_id<key)
                    t=t.rightChild;
                else if (t.obj_id>key)
                    t=t.leftChild;
            }
            return t;
        }

        void case1(Link x,Link y,Link z){
            Link t3=y.rightChild; y.parent=z.parent;
            z.parent=y; y.rightChild=z;
            z.leftChild=t3; t3.parent=z;

            if (y.parent==null)
                ORoot=y;
            else if (y.parent.leftChild==z)
                y.parent.leftChild=y;
            else    
                y.parent.rightChild=y;
            return;
        }

        void case2(Link x,Link y,Link z){
            Link t2=y.leftChild;  y.parent=z.parent;
            z.parent=y; y.leftChild=z;
            z.rightChild=t2; t2.parent=z;
           
            if (y.parent==null)
                ORoot=y;
            else if (y.parent.leftChild==z)
                y.parent.leftChild=y;
            else    
                y.parent.rightChild=y;
            return;
        }

        void case3(Link x,Link y,Link z){
            Link t2=x.leftChild; Link t3=x.rightChild;
            y.rightChild=t2; t2.parent=y;
            z.leftChild=t3; t3.parent=z;
            x.leftChild=y; x.rightChild=z;
            x.parent=z.parent;
            z.parent=x; y.parent=x;
            if (x.parent==null)
                ORoot=x;
            else if (x.parent.leftChild==z)
                x.parent.leftChild=x;
            else
                x.parent.rightChild=x;
            return;
        }

        void case4(Link x,Link y,Link z){
            Link t2=x.leftChild; Link t3=x.rightChild;
            y.leftChild=t3; t3.parent=y;
            z.rightChild=t2; t2.parent=z;
            x.leftChild=z; x=rightChild=y;
            x.parent=z.parent;
            z.parent=x; y.parent=x;
            if (x.parent==null)
                root=x;
            else if (x.parent.leftChild==z)
                x.parent.leftChild=x;
            else
                x.parent.rightChild=x;
            return;         
        }

        void reStructure(Link z){
            while (z!=null){
                heightDiff=z.leftChild.height=z.rightChild.height;
                if (heightDiff<-2 || heightDiff>2){
                    Link x,y;
                    if (z.rightChild.height>z.leftChild.height)
                        y=z.rightChild;
                    else   
                        y=z.leftChild;
                    
                    if (y.rightChild.height>y.leftChild.height)
                        x=y.rightChild;
                    else if (y.rightChild.height<y.leftChild.height)  
                        x=y.leftChild;
                    else {
                        if (y==z.rightChild)
                            x=y.rightChild;
                        else
                            x=y.leftChild;
                    }

                    if (y==z.leftChild && x==y.leftChild)//CASE 1 or left-left
                        case1(x,y,z);
            
                    else if (y==z.rightChild && x==y.rightChild)//CASE 2 or right-right
                        case2(x,y,z);

                    else if (y==z.leftChild && x==y.rightChild)//CASE 3 or left-right
                        case3(x,y,z);

                    else //CASE 4 or right-left
                        case4(x,y,z);
            
                    x.height=1+Math.max(x.leftChild.height,x.rightChild.height);
                    y.height=1+Math.max(y.leftChild.height,y.rightChild.height);
                    z.height=1+Math.max(z.leftChild.height,z.rightChild.height);
        
                    Link t=y;
                    while (t!=null){
                        t.height=1+Math.max(t.leftChild.height,t.rightChild.height);
                        t=t.parent;
                    }
                }
                z=z.parent;                    
            }
        }

        public void Add_Link(int ID, Node bin, Box loc){
            Link z=SearchLink(ID);
            if (z.isEmpty()){
                fillLink(z,ID,bin,loc);
            }
            else {
                //get the inOrder successor of this node and add another similar node before it
            }
            reStructure(z);
        }

        public void Delete_Link(Link t){//function to delete a link in this tree
            if (t.isEmpty()){
                System.out.println("No such object exists in the database");
                return;
            }

            if (t.leftChild.isEmpty() && t.rightChild,isEmpty()){//when both children are leaf
                if (t.parent.leftChild==t)
                    t.parent.leftChild=new Link();
                else
                    t.parent.rightChild=new Link();
                t.parent=null;
                reStructure(t);
            }
            else if (!t.leftChild.isEmpty() && !t.rightChild.isEmpty()){//when it is an internal node
                Link q=t.rightChild;
                while (!q.leftChild.isEmpty())
                    q=q.leftChild;
                t.obj_id=q.obj_id;
                t.storing_bin=q.storing_bin;
                t.location=q.location;
                Delete_Link(q);
            }
            else {//when one child is leaf and other is not leaf
                Link internalChild;
                if (t.leftChild.isEmpty())
                    internalChild=t.rightChild;
                else
                    internalChild=t.rightChild;
                
                if (t.parent.leftChild==t)
                    t.parent.leftChild=internalChild;
                
                else
                    t.parent.rightChild=internalChild;
                reStructure(t);
            }
        }//delete function ends
    }

    BinTree PineTree=new BinTree();
    ObjectTree AppleTree=new ObjectTree();
    IDTree CoconutTree=new IDTree();

    public void add_bin(int bin_id,int capacity){
        PineTree.Add_Node(bin_id,capacity);
        BinTree.Node bin=PineTree.SearchNode(capacity); 
        CoconutTree.Add_Piece(bin_id,bin);
    }

    public Integer add_object(int obj_id,int size){
        BinTree.Node max_cap=PineTree.getMax();
        if (size>max_cap.capacity){
            System.out.println("Cannot add object as it is too big");
            return null;
        }
        max_cap.capacity=max_cap.capacity-size;//decreasing the capacity of bin
        BinTree.Node.Box location=max_cap.objectlist.Add_Box(obj_id,size);

        AppleTree.Add_Link(obj_id,max_cap,location);//adding the object in Object Tree

        PineTree.Add_Node(max_cap.ID,max_cap.capacity);//making a new node in AVL Tree with updated capacity
        BinTree.Node p=PineTree.SearchNode(max_cap.capacity);
        p.objectlist=max_cap.objectlist;

        IDTree.Piece r=CoconutTree.SearchPiece(max_cap.ID);//updating the bin pointer in ID Tree
        r.bin_loc=p;

        PineTree.Delete_Node(max_cap);//Deleting the old node in Bin tree
        return p.ID;
    }
    
    public Integer delete_object(int obj_id){
        ObjectTree.Link obj_search=AppleTree.SearchLink(obj_id);
        BinTree.Node.Box point=obj_search.location;
        BinTree.Node bin=obj_search.storing_bin;
        int result=bin.ID;
        bin.capacity=bin.capacity+point.object_size;//update the capacity of the bin

        AppleTree.Delete_Link(obj_search);//delete the object from the object tree

        bin.objectlist.Delete_Box(point);//delete the object from the object list of the bin

        PineTree.Add_Node(bin.ID,bin.capacity);//shift the bin in the bin tree
        Bin.Node p=PineTree.SearchNode(bin.capacity);
        p.objectlist=bin.objectlist;

        IDTree.Piece r=CoconutTree.SearchPiece(bin.ID);//updating the bin pointer in ID Tree
        r.bin_loc=p;

        PineTree.Delete_Node(bin);//deleting the old node in Bin Tree
        return result;
    }

    public void List_contents(int bin_id){
        IDTree.Piece p=CoconutTree.SearchPiece(bin_id);
        BinTree.Node q=PineTree.SearchNode(p.bin_loc);
        System.out.println("Bin ID: "+q.ID+" ");
        BinTree.Node.MyList.Box r=q.objectlist.HEADER;
        r=r.next;
        while (r!=q.objectlist.TRAILER)
            System.out.println(r.object_id+" "+r.object_size);
    }

    public static void main(String[] args){
        try{
            FileInputStream fstream=new FileInputStream("file.txt");
            Scanner s=new Scanner(fstream);
            while (s.hasNextLine()){
                int check=s.nextInt();

                if (check==1){
                    int parameter1=s.nextInt(); int parameter2=s.nextInt();
                    add_bin(parameter1,parameter2);
                }

                else if (check==2){
                    int parameter1=s.nextInt(); int parameter2=s.nextInt();
                    int result=add_object(parameter1,parameter2);
                    System.out.println(result);
                }

                else if (check==3){
                    int parameter1=s.nextInt();
                    int result=delete_object(parameter1);
                    System.out.println(result);
                }

                else if (check==4){
                    int parameter1=s.nextInt();
                    List_contents(parameter1);
                }

                else {
                    System.out.prinln("Invalid option no.")
                }
            }
        }

        catch (FileNotFoundException e){
            System.out.println("File not found");
        }
        return;
    }
}