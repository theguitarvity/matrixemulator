package matrixemulator;

import java.util.ArrayList;
import java.io.FileInputStream;
import java.io.IOException;

public class MemoriaPrincipal {
	private ArrayList<Long> memory = new ArrayList<Long>();
	            
	public MemoriaPrincipal(String filename) throws IOException {
		FileInputStream input = new FileInputStream(filename);
		
		byte[] b = new byte[4];
		
		while (input.read(b) != -1) {
			long word = getUnsignedValue(b[0]);
			word += getUnsignedValue(b[1]) << 8;
			word += getUnsignedValue(b[2]) << 16;
			word += getUnsignedValue(b[3]) << 24;
			
			memory.add(word);
		}
	}

	public long getValue(long location) {

		long memoryAddress = location >> 2;
		
		if (memoryAddress >= memory.size()) {
			System.out.println("Endereço - - - - - - -");
			System.out.println(memoryAddress);
			System.out.println("Tamanho - - - - - - -");
			System.out.println(memory.size());
			throw new IllegalArgumentException("Endereço fora do limite");
		}
	
		return memory.get((int)memoryAddress);
	}
	
	public void storeValue(long location, long value) {
		long memoryAddress = location >> 2;
		
		if (memoryAddress >= memory.size()) {
			System.out.println("LOCATION - - - - - - -");
			System.out.println(location);
			throw new IllegalArgumentException("Memory address out of bounds");
		}
		memory.set((int)memoryAddress, value);
	}
	
	public long getUnsignedValue(byte b) {
		if (b >= 0) { return b; }
		
		return 256 + b;
	}
}
