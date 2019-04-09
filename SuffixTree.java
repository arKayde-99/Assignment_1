import java.util.*;
import java.lang.*;
import java.io.*;

public class SuffixTree{

    public static class SuffixTrie extends SuffixTree{
        
        public class Node{
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
                if (t.ChildArray[getIndex(c)]==null)
                    return false;

                Node test=t.ChildArray[getIndex(c)];
                String s=test.word;

                int i;
                for (i=0;mystery.charAt(count)==s.charAt(i);count++,i++){
                    if (count==(mystery.length()-1))
                        return (test.isWordEnd && (i==(s.length()-1)));
                    
                    if (i==(s.length()-1))
                        break;
                }
                if (i!=(s.length()-1))
                    return false;

                count++;
                t=test;
            }
            return false;
        }

        public void AddWord(String treasure){
            if (isWordPresent(treasure)){
                //System.out.println("This String already exists in the trie");
                return;
            }

            Node t=root;
            for (int count=0;count<treasure.length();){
                char c=treasure.charAt(count);
                if (t.ChildArray[getIndex(c)]==null){//CASE 1: Even the first letter of the new word does not exist in Child Array
                    String part=conCat(treasure, count, treasure.length()-1);
                    Node child=new Node();
                    child.parent=t; child.word=part; child.isWordEnd=true;
                    t.ChildArray[getIndex(c)]=child;
                    return;
                }

                Node test=t.ChildArray[getIndex(c)];
                String s=test.word;
                int i;

                for (i=0;treasure.charAt(count)==s.charAt(i);count++,i++){
                    if (count==(treasure.length()-1)){//CASE 2: Word is a prefix of an already existing word
                        if (i==(s.length()-1))
                            test.isWordEnd=true;
                        else {
                            String unCommon=conCat(s,count+1,s.length()-1);
                            Node newPart=new Node();
                            test.word=treasure; test.isWordEnd=true;

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
                        count++;
                        t=test;
                        break;
                    }
                }

                if (i!=s.length()){//splitting of the node has to be done
                    String commonPart=conCat(s,0,i-1);
                    String unCommon=conCat(s,i,s.length()-1);
                    test.word=commonPart;

                    Node newPart=new Node();
                    newPart.word=unCommon; newPart.ChildArray=test.ChildArray; newPart.isWordEnd=true;
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

        //Suffix Trie Class ends
    }

    public static void main(String[] args){
        System.out.println("Enter the number of words you wish to enter");
        Scanner s=new Scanner(System.in);
        int N=s.nextInt();
        SuffixTrie OakTrie=new SuffixTrie();

        for (int count=0;count<N;count++){//Adding the words and all their suffix to the Suffix Trie
            String word=s.next();
            for (int i=0;i<word.length();i++){
                String temp="";
                for (int j=i;j<word.length();j++)
                    temp=temp+Character.toString(word.charAt(j));
                OakTrie.AddWord(temp);
            }
        }

        System.out.println("Enter the number of commands you wish to execute");
        N=s.nextInt();

        for (int count=0;count<N;count++){
            String search=s.next();
            if (OakTrie.isWordPresent(search))
                System.out.println("This word is present in the trie");
            else
                System.out.println("This word is NOT present in the trie");
        }
        return;      
    }
}