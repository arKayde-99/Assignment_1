import java.util.*;
import java.io.*;
import java.lang.*;

public class Decompress{
    public static class HashDictionary extends Decompress{
        class Obj{
            int checker;
            String word;
            Obj(){
                checker=0;
            }
        }
        public int collisionCount=0;
        public int currSize=0;

        public int myPow(int d,int N){
            if (N==0)
                return 1;

            if (N%2==1)
                return d*myPow(d*d,N/2);
            else
                return myPow(d*d,N/2);
        }

        Obj[] hashArr=new Obj[myPow(2,16)+1];
        HashDictionary(){
            for (int i=0;i<myPow(2,16)+1;i++){
                hashArr[i]=new Obj();
            }
            for (int i=0;i<256;i++){//initialising the dictionary with all possible characters
                String temp=Character.toString((char)i);
                AddWord(temp);
            }
            hashArr[65536].checker=1;
            hashArr[65536].word="kshitij";
        }

        public final int primeN=65537;

        public int compressionFunction(int key){//key is a very large integer value that has to be compressed
            return key%primeN;
        }

        public Boolean SearchQuery(String query){//this function searches for a query in the hash table
            int hashValue=query.hashCode();
            int key=compressionFunction(hashValue);
            if (hashArr[key].checker==0)
                return false;
            else {
                if (query.equals(hashArr[key].word))
                    return true;
                
                int quad=1; int index=key;
                while (!query.equals(hashArr[(index+(quad*quad))%primeN].word)){
                    index=(index+quad*quad)%primeN;
                    if (hashArr[index].checker==0)
                        return false;
                    if (index==key)
                        return false;
                    quad++;
                }
                return true;
            }
        }

        public String getName(int address){
            return hashArr[address].word;
        }

        public int getAddress(String query){
            int hashValue=query.hashCode();
            int key=compressionFunction(hashValue);
            //System.out.println("HashValue= "+hashValue+" Key= "+key);
        
            if (query.equals(hashArr[key].word))
                return key;
                
            int quad=1; int index=key;
            while (!query.equals(hashArr[(index+(quad*quad))%primeN].word)){
                index=(index+quad*quad)%primeN;
                quad++;
            }
            return ((index+quad*quad)%primeN);
        }

        public void AddWord(String query){//this function adds a new word to the dictionary and does collision handling by quadratic probing

            int hashValue=query.hashCode();
            int key=compressionFunction(hashValue);
            currSize++;
            if ((hashArr[key].checker)==0){
                hashArr[key].checker=1;
                hashArr[key].word=query;
                return;
            }
            else if (query.equals(hashArr[key].word)){
                System.out.println("The word already exists in the dictionary");
                return;
            }
            int quad=1; int index=key;
            while (!(hashArr[(index+quad*quad)%primeN].checker==0)){
                collisionCount++;//just for personal reference
                index=(index+quad*quad)%primeN;
                if (index==key){
                    System.out.println("Word cannot be inserted due to the weirdest thing ever");
                    return;
                }
            } index=index+quad*quad;
            hashArr[index].checker=1;
            hashArr[index].word=query;
            return;
        }

        public void DeleteWord(String query){//this function deletes a word from the dictionary
            if (!SearchQuery(query)){
                System.out.println("No such word exists in the dictionary");
                return;
            }
            currSize--;
            int index=getAddress(query);
            hashArr[index].checker=0;
            hashArr[index].word=null;
            //now shift the quadratically probed elements back
            for (int quad=1;hashArr[(index+(quad*quad))%primeN].checker==1;quad++){
                index=(index+(quad*quad))%primeN;
                String s=hashArr[index].word;
                DeleteWord(s);
                AddWord(s);
                if (getAddress(s)==index)
                    break;
            }
            return;
        }
    }

    public static void main(String[] args){
        try {
            FileInputStream fstream=new FileInputStream("dInput.txt");
            Scanner s=new Scanner(fstream);
            HashDictionary Oxford=new HashDictionary();

            int recent,old; String accumulate,b;
            old=s.nextInt(); accumulate=Oxford.getName(old); b=Character.toString(accumulate.charAt(0));

            System.out.print(accumulate);
            while (s.hasNext()){
                recent=s.nextInt();

                if ((Oxford.hashArr[recent].checker)==0){
                    accumulate=Oxford.getName(old);
                    accumulate=accumulate+b;
                }
                else {
                    accumulate=Oxford.getName(recent);
                }

                System.out.print(accumulate);
                b=Character.toString(accumulate.charAt(0));
                Oxford.AddWord(Oxford.getName(old)+b);
                old=recent;
            }
        }
        catch (FileNotFoundException e){
            System.out.println("File was not found");
        }
        return;
    }
}