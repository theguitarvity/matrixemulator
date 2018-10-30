package matrixemulator;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.ByteBuffer;

public class Main {

  private final static char[] hexArray = "0123456789ABCDEF".toCharArray();

  public static void main(String[] args) {

    Path data = Paths.get(args[0]);

    try {
      byte[] bytes = Files.readAllBytes(data);

      for(int i = 0; i <bytes.length; i ++) {
    	  String teste = String.format("%8s", Integer.toBinaryString(bytes[i] & 0xFF)).replace(' ', '0');
    	  int op = (Integer.parseInt(new StringBuilder(teste).reverse().toString())&0xFF);
    	  System.out.println(op);
    	  
      }

      

    } catch (IOException e) {
      System.out.println(e);
    }

  }

  public static void decode(List<Integer> instructions) {
    for(int i = 0; i < instructions.size(); i++) {
      System.out.println(instructions.get(i));
    }
  }

  public static int[] convertToIntArray(byte[] input)
	{
	    int[] ret = new int[input.length];
	    for (int i = 0; i < input.length; i++)
	    {
	        ret[i] = input[i];
	    }
	    return ret;
	}

}