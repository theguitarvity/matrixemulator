package matrixemulator;

import java.util.Map.Entry;

import matrixemulator.Register;
import matrixemulator.RegisterFile;
import matrixemulator.RegisterName;
import matrixemulator.Decode;

public class PipelineRegister extends RegisterFile {
	public PipelineRegister() {
		super();
		Register register = new Register(Decode.NOP);
		registers.put(RegisterName.OP_CODE, register);	
	}
	
	protected boolean validRegister(RegisterName registerName) {
		return true;
	}
	
	public void forwardValues(PipelineRegister target) {
		for (Entry<RegisterName, Register> entry : registers.entrySet()) {
			target.setValue(entry.getKey(), entry.getValue().getValue());
		}
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		for (Entry<RegisterName, Register> entry : registers.entrySet()) {
			builder.append(entry.getKey());
			builder.append('\t');
			builder.append(entry.getValue().getValue());
			builder.append('\n');
		}
		
		return builder.toString();
	}
}