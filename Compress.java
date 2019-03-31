import java.util.*;
import java.io.*;
import java.nio.*;
import java.lang.*;

public class Compress{

    public static class HashDictionary extends Compress{
        class Obj{
            int checker;
            String word;
            Obj(){
                checker=0;
            }
        }
        public int collisionCount=0;
        public int currSize=0;

        public long myPow(int d,int N){
            if (N==0)
                return 1;

            if (N%2==1)
                return d*myPow(d*d,N/2);
            else
                return myPow(d*d,N/2);
        }

        Obj[] hashArr=new Obj[65537];
        HashDictionary(){
            for (int i=0;i<65537;i++){
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
        public int getHashCode(String query){
            long value=0,step;
            for (int i=0;i<query.length();i++){
                step=(query.charAt(i))*myPow(31,query.length()-i-1);
                step=step%primeN;
                value=value+step;
                value=value%primeN;
            }
            return (int)value;
        }

        public int compressionFunction(long key){//key is a very large integer value that has to be compressed
            int result=(int)key;
            return result%primeN;
        }

        public Boolean SearchQuery(String query){//this function searches for a query in the hash table
            int key=getHashCode(query);
            //System.out.println("query="+query+" key="+key);
            if (hashArr[key].checker==0)
                return false;
            else {
                if (query.equals(hashArr[key].word))
                    return true;
                
                int quad=1; int index=key;
                while (!query.equals(hashArr[(index+(quad*quad))%primeN].word)){
                    //System.out.println("index= "+index+"  query= "+query);//for debugging.....
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

        public int getAddress(String query){
            int key=getHashCode(query);
            //System.out.println("HashValue= "+hashValue+" Key= "+key);
        
            if (query.equals(hashArr[key].word))
                return key;
                
            int quad=1; int index=key;
            while (!query.equals(hashArr[((index+(quad*quad))%primeN)].word)){
                //System.out.println("index= "+index+"  query= "+query);//for debugging.....
                index=(index+quad*quad)%primeN;
                quad++;
            }
            return ((index+quad*quad)%primeN);
        }

        public void AddWord(String query){//this function adds a new word to the dictionary and does collision handling by quadratic probing

            int key=getHashCode(query);
            currSize++;
            if ((hashArr[key].checker)==0){
                hashArr[key].checker=1;
                hashArr[key].word=query;
                return;
            }
            else if (query.equals(hashArr[key].word)){
                //System.out.println("The word already exists in the dictionary");
                return;
            }
            int quad=1; int index=key;
            while (!(hashArr[(index+quad*quad)%primeN].checker==0)){
                collisionCount++;//just for personal reference
                index=(index+quad*quad)%primeN;
                if (index==key){
                    //System.out.println("Word cannot be inserted due to the weirdest thing ever");
                    return;
                }
            } index=index+quad*quad;
            hashArr[index].checker=1;
            hashArr[index].word=query;
            return;
        }

        public void DeleteWord(String query){//this function deletes a word from the dictionary
            if (!SearchQuery(query)){
                //System.out.println("No such word exists in the dictionary");
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

    public static String getBits(int num){
        int[] arr=new int[16];
        for (int i=0;i<16;i++){
            arr[i]=num%2;
            num=num/2;
        } 
        String result="";
        for (int i=0;i<16;i++)
            result=result+arr[15-i];
        return result;
    }
    public static void main(String[] args){
        try {
            FileInputStream fstream=new FileInputStream("file1.txt");
            Scanner s=new Scanner(fstream);
            HashDictionary Oxford=new HashDictionary();

            while (s.hasNextLine()){
                String line=s.nextLine();
                String accumulate="",b;
                for (int count=0;count<line.length();count++){
                    b=Character.toString(line.charAt(count));
                    if (Oxford.SearchQuery(accumulate+b)){
                        accumulate=accumulate+b;
                    }
                    else {
                        String bitAddress=getBits(Oxford.getAddress(accumulate));
                        System.out.print(bitAddress+" ");
                        Oxford.AddWord(accumulate+b);
                        accumulate=b;
                    }
                }
                String bitAddress=getBits(Oxford.getAddress(accumulate));
                System.out.print(bitAddress+" ");
            }
            //System.out.println("Current Dictionary size= "+Oxford.currSize);//for debugging.....
        }
        catch (FileNotFoundException e){
            System.out.println("File was not found");
        }
        return;
    }
}