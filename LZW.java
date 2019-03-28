import java.util.*;
import java.io.*;
import java.lang.*;

public class LZW{
    public static class Dictionary extends LZW{
        public int leastAv=0;

        public int myPow(int d, int N){
            if (N==0)
                return 1;

            if (N%2==1)
                return d*myPow(d*d,N/2);
            else
                return myPow(d*d,N/2);
        }
        int size=myPow(2,16);
        String[] Table=new String[size];

        Dictionary(){
            for (int count=0;count<myPow(2,8);count++){//initialising the table with all possible characters
                int temp=count;
                Table[count]=Character.toString((char)temp);
                leastAv++;
            }
        }

        public Boolean search(String foo){//checks if a string is present in the dictionary
            for (int count=0;count<leastAv;count++){
                if (foo.equals(Table[count]))
                    return true;
            }
            return false;
        }

        public int getAddress(String foo){//returns the address of the string in dictionary
            int count;
            for (count=0;count<leastAv;count++){
                if (foo.equals(Table[count]))
                    break;
            }
            return count;
        }

        public void insertLeastAv(String foo){//inserting in dictionary at the least available index
            Table[leastAv]=foo;
            leastAv++;
            return;
        }
    }

    public static void main(String[] args){
        try {
            FileInputStream fstream=new FileInputStream("input.txt");
            Scanner s=new Scanner(fstream);
            Dictionary Oxford=new Dictionary();

            while (s.hasNextLine()){
                String line=s.nextLine();
                String accumulate="",b;
                for (int count=0;count<line.length();count++){
                    b=Character.toString(line.charAt(count));
                    if (Oxford.search(accumulate+b)){
                        accumulate=accumulate+b;
                    }
                    else {
                        System.out.print(Oxford.getAddress(accumulate)+" ");
                        Oxford.insertLeastAv(accumulate+b);
                        accumulate=b;
                    }
                }
                System.out.println(Oxford.getAddress(accumulate));
            }
            return;
        }
        catch (FileNotFoundException e){
            System.out.println("File was not found");
        }
        return;
    }
}