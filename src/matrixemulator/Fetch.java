package matrixemulator; 

import matrixemulator.MemoriaPrincipal;
import matrixemulator.PC;
import matrixemulator.Run;
import matrixemulator.RegisterName;
import matrixemulator.PipelineRegister;

public class Fetch extends Run {
	private static final int HALT_INSTRUCTION = 0xFC000000; 
	
	private final PipelineRegister if_id;
	private final InstructionMemory memory;
	private final PC pc;
	
	public Fetch( PipelineRegister if_id, InstructionMemory memory, PC pc) {
		this.if_id = if_id;
		this.memory = memory;
		this.pc = pc;
	}
	
	@Override
	public void run() {
		System.out.println("PC VALUE - - - - -");
		System.out.println(pc.getValue());

		long instruction = memory.instrucoes().get((int) pc.getValue());

		System.out.println("INSTRUCTION - - - - -");
		System.out.println(instruction);

		System.out.println(Long.toHexString(instruction));


		if_id.setValue(RegisterName.INSTRUCTION, instruction);
		if_id.setValue(RegisterName.OP_CODE, 0);
		if_id.setValue(RegisterName.PC, pc.getValue());
		
		if (instruction != HALT_INSTRUCTION) {
			pc.increment();
		}
	}

}