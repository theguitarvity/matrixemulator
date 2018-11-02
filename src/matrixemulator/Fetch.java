package matrixemulator; 

import matrixemulator.Memory;
import matrixemulator.PC;
import matrixemulator.Run;
import matrixemulator.RegisterName;
import matrixemulator.PipelineRegister;

public class Fetch extends Run {
	private static final int HALT_INSTRUCTION = 0xFC000000; 
	
	private final PipelineRegister if_id;
	private final Memory memory;
	private final PC pc;
	
	public Fetch(
		PipelineRegister if_id, 
		Memory memory, 
		PC pc
	) {
		this.if_id = if_id;
		this.memory = memory;
		this.pc = pc;
	}
	
	@Override
	public void run() {
		long instruction = memory.getValue(pc.getValue());
		//System.out.println(Integer.toHexString(instruction));
		if_id.setValue(RegisterName.INSTRUCTION, instruction);
		if_id.setValue(RegisterName.OP_CODE, 0);
		if_id.setValue(RegisterName.PC, pc.getValue());
		
		if (instruction != HALT_INSTRUCTION) {
			pc.increment();
		}
	}

}