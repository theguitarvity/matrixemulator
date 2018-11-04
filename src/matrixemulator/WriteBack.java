package matrixemulator;

import matrixemulator.RegisterFile;
import matrixemulator.RegisterName;
import matrixemulator.PipelineRegister;
import matrixemulator.Run;

public class WriteBack extends Run {

	private final PipelineRegister mem_writeback;
	private final RegisterFile registerFile;
	
	public WriteBack(PipelineRegister mem_writeback, RegisterFile registerFile) {
		this.mem_writeback = mem_writeback;
		this.registerFile = registerFile;
	}
	
	@Override
	public void run() {
		System.out.println("RUNNING WRITE BACK - - - - -");
		System.out.println(mem_writeback.getValue(RegisterName.REG_WRITE) == 1);
		
		if (mem_writeback.getValue(RegisterName.REG_WRITE) == 1) {
			long dest = mem_writeback.getValue(RegisterName.REG_DST) == 1 ?
				mem_writeback.getValue(RegisterName.R_D) :
				mem_writeback.getValue(RegisterName.R_T);
			
			long value = mem_writeback.getValue(RegisterName.MEM_TO_REG) == 1 ?
				mem_writeback.getValue(RegisterName.MEM_RESULT) :
				mem_writeback.getValue(RegisterName.ALU_RESULT);

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