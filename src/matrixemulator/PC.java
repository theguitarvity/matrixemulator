package matrixemulator;

import matrixemulator.Register;

public class PC extends Register {
	private static final int FIRST_INSTRUCTION = 0x0000;
	
	public PC() {
		super(FIRST_INSTRUCTION);
	}
	
	public void increment() {
		newValue = getValue() + 4;
	}
}
