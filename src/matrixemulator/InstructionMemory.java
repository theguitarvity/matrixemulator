package matrixemulator;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

public class InstructionMemory{

	private ArrayList<Long> mem = new ArrayList<Long>();
	public InstructionMemory(String file) throws IOException{
		FileInputStream in = new FileInputStream(file);
		byte[] b = new byte[4];
		while(in.read(b)!=-1) {
			long word = getUnsignedValue(b[0]);
			word += getUnsignedValue(b[1]) << 8;
			word += getUnsignedValue(b[2]) << 16;
			word += getUnsignedValue(b[3]) << 24;
			mem.add(word);
		}
		
	}
	public ArrayList<Long> instrucoes(){
		return mem;
	}
	private long getUnsignedValue(byte b) {
		// TODO Auto-generated method stub
		if(b>=0)
			return b;
		return 256+b;
	}

}
