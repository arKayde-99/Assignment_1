import java.util.*;
import java.lang.*;
import java.io.*;

public class SuffixTree{
    public static class SuffixTrie extends SuffixTree{

        public class MyList{
            class ListNode{
                Integer value;
                ListNode next;
            }

            int N=0;

            ListNode head=new ListNode(); ListNode tail=new ListNode();

            public void Add(int x){//adding to list in ascending order
                N++;
                if (head.value==null){
                    head.value=x;
                    tail=head;
                    return;
                }
                ListNode l=new ListNode();
                if (x==tail.value)
                    return;
                l.value=x; tail.next=l; tail=l;
                return;
            }

            public int size(){
                return N;
            }
        }

        public class Node{
            MyList locList=new MyList();//storing only where a pattern begins            
            String word;
            Node parent;
            Boolean isWordEnd;
            Node[] ChildArray=new Node[26];//contains all the children of this node, whichever are present are initialised

            Node(){
                isWordEnd=false;
            }
        }

        public int getIndex(char c){
            return (int)c-97;
        }

        public String conCat(String s,int x,int y){//function to concatenate string from index i to j
            String temp="";
            for (int i=x;i<=y;i++)
                temp=temp+Character.toString(s.charAt(i));
            return temp;
        }

        Node root=new Node();

        public Boolean isWordPresent(String mystery){//checks if a word is present in the trie
            Node t=root;
            for (int count=0;count<mystery.length();){
                char c=mystery.charAt(count);
                if (t.ChildArray[getIndex(c)]==null){
                    //System.out.println(mystery+" is NOT present");
                    return false;
                }

                Node test=t.ChildArray[getIndex(c)];
                String s=test.word;

                int i;
                for (i=0;mystery.charAt(count)==s.charAt(i);count++,i++){
                    if (count==(mystery.length()-1)){
                        return (test.isWordEnd && (i==(s.length()-1)));
                    }
                    
                    if (i==(s.length()-1))
                        i++;
                        break;
                }
                if (i!=s.length()){
                    return false;
                }

                count++;
                t=test;
            }
            
            return false;
        }

        public void makeListSame(Node p,Node q){//to copy the elements of q's list to p's list
            for (MyList.ListNode r=q.locList.head;r!=null;r=r.next){
                int e=r.value;
                p.locList.Add(e);;
            }
            return;
        }

        public void AddWord(String treasure,int p){
            if (isWordPresent(treasure)){//checking if the word is present
                return;
            }

            Node t=root;
            for (int count=0;count<treasure.length();){
                char c=treasure.charAt(count);
                if (t.ChildArray[getIndex(c)]==null){//CASE 1: Even the first letter of the new word does not exist in Child Array
                    String part=conCat(treasure, count, treasure.length()-1); 
                    if (t!=root)
                        t.locList.Add(p);
                    Node child=new Node();
                    child.parent=t; child.word=part; child.isWordEnd=true; child.locList.Add(p);//SOMEHOW ADD THE INDEX TO LIST........
                    t.ChildArray[getIndex(c)]=child;
                    return;
                }

                Node test=t.ChildArray[getIndex(c)];
                String s=test.word;
                int i;

                int q=count;
                for (i=0;treasure.charAt(count)==s.charAt(i);count++,i++){
                    if (count==(treasure.length()-1)){//CASE 2: Word is a prefix of an already existing word
                        if (i==(s.length()-1)){
                            test.isWordEnd=true; test.locList.Add(p); 
                        }
                        else {
                            String unCommon=conCat(s,i+1,s.length()-1);
                            Node newPart=new Node();
                            makeListSame(newPart,test);
                            test.word=conCat(treasure,q,count); test.isWordEnd=true; test.locList.Add(p);

                            newPart.word=unCommon; newPart.ChildArray=test.ChildArray; newPart.isWordEnd=true;
                            for (int j=0;j<26;j++){
                                if (test.ChildArray[j]!=null)
                                    test.ChildArray[j].parent=newPart;
                            }
                            newPart.parent=test;
                            test.ChildArray=new Node[26];
                            char d=unCommon.charAt(0);
                            test.ChildArray[getIndex(d)]=newPart;
                        }
                        return;
                    }
                
                    if (i==(s.length()-1)){//CASE 3: The Word's prefix is already present =>> node does not have to split
                        count++; i++;
                        test.locList.Add(p);
                        t=test;
                        break;
                    }
                }

                if (i!=s.length()){//CASE 4: splitting of the node has to be done(i=s.length() has to be done)
                    String commonPart=conCat(s,0,i-1);
                    String unCommon=conCat(s,i,s.length()-1);
                    test.word=commonPart; 

                    Node newPart=new Node(); 
                    makeListSame(newPart,test); test.locList.Add(p);
                    newPart.word=unCommon; newPart.ChildArray=test.ChildArray; newPart.isWordEnd=true; test.isWordEnd=false;
                    for (int j=0;j<26;j++){
                        if (test.ChildArray[j]!=null)
                            test.ChildArray[j].parent=newPart;
                    }
                    newPart.parent=test;
                    test.ChildArray=new Node[26];
                    char d=unCommon.charAt(0);
                    test.ChildArray[getIndex(d)]=newPart;
                    t=test;
                }                       
            }
        }

        public MyList.ListNode findPattern(String mystery){//returns a list containing all starting positions of the pattern
            Node t=root;
            for (int count=0;count<mystery.length();){
                char c=mystery.charAt(count);
                if (t.ChildArray[getIndex(c)]==null){
                    return null;
                }

                Node test=t.ChildArray[getIndex(c)];
                String s=test.word; 

                int i; int past=count;
                for (i=0;mystery.charAt(count)==s.charAt(i);count++,i++){
                    if (count==(mystery.length()-1)){
                        MyList.ListNode q=test.locList.head;
                        return q;
                    }
                    
                    if (i==(s.length()-1)){
                        i++;
                        break;
                    }
                }
                if (i!=s.length())
                    return null;
                count++;
                t=test;
            }
            return null;
        }

        public void printList(String mystery){//prints the contents of the list
            MyList.ListNode q=findPattern(mystery);
            while (q!=null){
                int ini=q.value; int fin=ini+mystery.length()-1;
                System.out.print(ini+" "+fin+"    ");
                q=q.next;
            }
            System.out.print("\n");
        }

        public void SimpleCases(String mystery,int place){
            if (place==0){
                String search=conCat(mystery,1,mystery.length()-1);
                printList(search);
            }
            else {
                String search=conCat(mystery,0,mystery.length()-2);
                printList(search);
            }
            return;
        }

        public void WildcardStar(String mystery,String text){
            String forward,backward; int count;
            for (count=0;count<mystery.length() && mystery.charAt(count)!='*';count++);

            if (count==0 || count==mystery.length()-1){
                SimpleCases(mystery,count);
                return;
            }
            forward=conCat(mystery,0,count-1);
            backward=conCat(mystery,count+1,mystery.length()-1);

            MyList.ListNode i=findPattern(forward);
            MyList.ListNode j=findPattern(backward);

            while (i!=null){
                MyList.ListNode t=j;
                while (t!=null){
                    int a,b,c,d;
                    a=i.value; b=a+forward.length()-1;
                    c=t.value; d=c+backward.length()-1;

                    if (c-b>1){
                        String result=conCat(text,a,d);
                        System.out.println(result);
                    }
                    t=t.next;    
                }
                i=i.next;
            }
            return;
        }

        public void WildcardQuestion(String mystery,int[] arr){

        }

        public void Joker(String mystery){

        }
        //Suffix Trie Class ends
    }

    public static void main(String[] args){
        try{
            FileInputStream fstream=new FileInputStream(args[0]);
            Scanner s=new Scanner(fstream);
            SuffixTrie OakTrie=new SuffixTrie();

            String line=s.nextLine(); int fileIndex=0;
            String[] strarr=line.split(" ");
            for (int count=0;count<strarr.length;count++,fileIndex++){//Adding the words and all their suffix to the Suffix Trie
                String word=strarr[count];
                for (int i=0;i<word.length();i++,fileIndex++){
                    String temp="";
                    for (int j=i;j<word.length();j++)
                        temp=temp+Character.toString(word.charAt(j));
                    OakTrie.AddWord(temp,fileIndex);
                }
            }

            int y,N=s.nextInt();//number of search operations to be implemented

            for (int count=0;count<N;count++){
                String search=s.next();

                Boolean hasStar=false,hasQuestionMark=false;
                for (y=0;y<search.length();y++)//checking for presence of star in search string
                    if (search.charAt(y)=='*'){
                        hasStar=true; break; 
                    }
                
                int [] sArray=new int[search.length()]; 
                for (y=0;y<search.length();y++){
                    if (search.charAt(y)=='?'){
                        hasQuestionMark=true;
                        sArray[y]=1;
                    }
                }//checking if the search string has Question marks
                if (hasQuestionMark & !hasStar)
                    OakTrie.WildcardQuestion(search,sArray);

                else if (hasStar & !hasQuestionMark)
                    OakTrie.WildcardStar(search,line);

                else if (hasStar && hasQuestionMark)
                    OakTrie.Joker(search);
                
                else
                    OakTrie.printList(search);
            }
        }
        catch (FileNotFoundException e){
            System.out.println("File was not found");
        }
        return;      
    }
}