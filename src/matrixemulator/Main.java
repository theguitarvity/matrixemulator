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
      List<Integer> integerArray = new ArrayList<>();

      for(int i = 0; i+3 < bytes.length; i = i+3) {
      	byte[] temp = new byte[4];
      	temp[0] = bytes[i];
      	temp[1] = bytes[i+1];
      	temp[2] = bytes[i+2];
      	temp[3] = bytes[i+3];
      	int result = ByteBuffer.wrap(temp).getInt();
      	integerArray.add(result);
      }

      decode(integerArray);

    } catch (IOException e) {
      System.out.println(e);
    }

  }

  public static void decode(List<Integer> instructions) {
    for(int i = 0; i < instructions.size(); i++) {
      System.out.println(instructions.get(i));
    }
  }

}