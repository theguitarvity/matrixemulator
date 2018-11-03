package matrixemulator;

import matrixemulator.RegisterFile;
import matrixemulator.RegisterName;
import matrixemulator.PipelineRegister;
import matrixemulator.Run;

public class WriteBack extends Run {

	private final PipelineRegister mem_wb;
	private final RegisterFile registerFile;
	
	public WriteBack(
		PipelineRegister mem_wb,
		RegisterFile registerFile
	) {
		this.mem_wb = mem_wb;
		this.registerFile = registerFile;
	}
	
	public boolean done() {
		return mem_wb.getValue(RegisterName.HALT) == 1;
	}
	
	public boolean isNop() {
		return mem_wb.getValue(RegisterName.REG_DST) == 0 &&
			mem_wb.getValue(RegisterName.ALU_SRC) == 0 &&
			mem_wb.getValue(RegisterName.MEM_TO_REG) == 0 &&
			mem_wb.getValue(RegisterName.REG_WRITE) == 0 &&
			mem_wb.getValue(RegisterName.MEM_READ) == 0 &&
			mem_wb.getValue(RegisterName.MEM_WRITE) == 0 &&
			mem_wb.getValue(RegisterName.BRANCH) == 0 &&
			mem_wb.getValue(RegisterName.BRANCH_NE) == 0 &&
			mem_wb.getValue(RegisterName.JUMP) == 0 &&
			mem_wb.getValue(RegisterName.JUMP_SRC) == 0 &&
			mem_wb.getValue(RegisterName.ALU_OP) == 0;
	}
	
	@Override
	public void run() {
		System.out.println("RUNNING WRITE BACK - - - - -");
		System.out.println(mem_wb.getValue(RegisterName.REG_WRITE) == 1);
		
		if (mem_wb.getValue(RegisterName.REG_WRITE) == 1) {
			long dest = mem_wb.getValue(RegisterName.REG_DST) == 1 ?
				mem_wb.getValue(RegisterName.R_D) :
				mem_wb.getValue(RegisterName.R_T);
			
			long value = mem_wb.getValue(RegisterName.MEM_TO_REG) == 1 ?
				mem_wb.getValue(RegisterName.MEM_RESULT) :
				mem_wb.getValue(RegisterName.ALU_RESULT);

				System.out.println("DEST writeback - - - - -");
				System.out.println(dest);
				System.out.println("VALUE writeback - - - - -");
				System.out.println(value);
				
			if (dest != 0) {
				registerFile.setValue(RegisterName.valueOf(dest), value);
				registerFile.tick();
			}
		}
	}

}