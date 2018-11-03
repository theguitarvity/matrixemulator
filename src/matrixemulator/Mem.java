package matrixemulator;

import matrixemulator.Memory;
import matrixemulator.RegisterName;
import matrixemulator.PipelineRegister;
import matrixemulator.Run;

public class Mem extends Run {

	private final PipelineRegister ex_mem;
	private final PipelineRegister mem_wb;
	private final Memory memory;
	
	public Mem(
		PipelineRegister ex_mem,
		PipelineRegister mem_wb,
		Memory memory
	) {
		this.ex_mem = ex_mem;
		this.mem_wb = mem_wb;
		this.memory = memory;
	}
	
	@Override
	public void run() {
		if (ex_mem.getValue(RegisterName.MEM_READ) == 1) {
			mem_wb.setValue(
				RegisterName.MEM_RESULT, 
				memory.getValue(ex_mem.getValue(RegisterName.ALU_RESULT))
			);
		}
		
		if (ex_mem.getValue(RegisterName.MEM_WRITE) == 1) {
			memory.storeValue(
				ex_mem.getValue(RegisterName.ALU_RESULT), 
				ex_mem.getValue(RegisterName.WRITE_DATA)
			);
		}
		
		ex_mem.forwardValues(mem_wb);
	}

}