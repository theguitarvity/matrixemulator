package matrixemulator;

import java.util.HashMap;

public class RegisterFile {

	protected HashMap<RegisterName, Register> registers = new HashMap<RegisterName, Register>();
	
	public long getValue(RegisterName registerName) {
		return getRegister(registerName).getValue();
	}

	public void setValue(RegisterName registerName, long value) {
		Register register = getRegister(registerName);
		register.setValue(value);
	}
	
	public void tick() {
		for (Register register : registers.values()) {
			register.tick();
		} 
	}
	
	protected boolean validRegister(RegisterName registerName) {
		return registerName.isPrimitive();
	}
	
	private Register getRegister(RegisterName registerName) {
		if (!validRegister(registerName)) {
			throw new IllegalArgumentException(
				"Invalid register: " + registerName.name()
			);
		}
		Register register = registers.get(registerName);
		
		if (register == null) {
			register = new Register();
			registers.put(registerName, register);
		}
		
		return register;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < 32; i++) {
			builder.append("$r");
			builder.append(i);
			builder.append('\t');
			builder.append(Long.toHexString(getRegister(RegisterName.valueOf(i)).getValue()));
			builder.append('\n');
		}
		
		return builder.toString();
	}
	
}