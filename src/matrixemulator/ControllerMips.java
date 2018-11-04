package matrixemulator;

import matrixemulator.RegisterName;
import matrixemulator.PipelineRegister;
import matrixemulator.Run;

public class ControllerMips extends Run {
	private final PipelineRegister execute_mem;
	private final PipelineRegister decode_execute;
	private final PipelineRegister mem_writeback;
	
	public ControllerMips(PipelineRegister decode_execute,PipelineRegister execute_mem, PipelineRegister mem_writeback) {
		this.execute_mem = execute_mem;
		this.decode_execute = decode_execute;
		this.mem_writeback = mem_writeback;
	}

	@Override
	public void run() {
		long aluArg1, aluArg2, writeData;
		
		long dest = execute_mem.getValue(RegisterName.REG_DST) == 1 ?
						execute_mem.getValue(RegisterName.R_D) :
						execute_mem.getValue(RegisterName.R_T);
		long dest2 = mem_writeback.getValue(RegisterName.REG_DST) == 1 ?
						mem_writeback.getValue(RegisterName.R_D) :
						mem_writeback.getValue(RegisterName.R_T);

		System.out.println("DEST 1 E 2 - - - - -");
		System.out.println(dest);
		System.out.println(dest2);
		
		
		if (execute_mem.getValue(RegisterName.REG_WRITE) == 1 && dest != 0 && dest == decode_execute.getValue(RegisterName.R_S)) {
			aluArg1 = execute_mem.getValue(RegisterName.ALU_RESULT);
		} else if (mem_writeback.getValue(RegisterName.REG_WRITE) == 1 && dest2 != 0 && dest2 == decode_execute.getValue(RegisterName.R_S)) {
			if (mem_writeback.getValue(RegisterName.MEM_TO_REG) == 1) {
				aluArg1 = mem_writeback.getValue(RegisterName.MEM_RESULT);
			} else {
				aluArg1 = mem_writeback.getValue(RegisterName.ALU_RESULT);
			}
		} else {
			aluArg1 = decode_execute.getValue(RegisterName.READ_DATA_1);
		}
		
	
		if (execute_mem.getValue(RegisterName.REG_WRITE) == 1 && dest != 0 &&  dest == decode_execute.getValue(RegisterName.R_T)) {
			writeData = execute_mem.getValue(RegisterName.ALU_RESULT);
		} else if (mem_writeback.getValue(RegisterName.REG_WRITE) == 1 && dest2 != 0 && dest2 == decode_execute.getValue(RegisterName.R_T)) {
			if (mem_writeback.getValue(RegisterName.MEM_TO_REG) == 1) {
				writeData = mem_writeback.getValue(RegisterName.MEM_RESULT);
			} else {
				writeData = mem_writeback.getValue(RegisterName.ALU_RESULT);
			}
		} else {
			writeData = decode_execute.getValue(RegisterName.READ_DATA_2);
		}
			
		if (decode_execute.getValue(RegisterName.ALU_SRC) == 0) {
			aluArg2 = writeData;
		} else {
			aluArg2 = decode_execute.getValue(RegisterName.IMMEDIATE);
		}
		
		decode_execute.forwardValues(execute_mem);
		
		long result = 0;

		System.out.println("ALU OP - - - - -");

		System.out.println((int)decode_execute.getValue(RegisterName.ALU_OP));
		
		switch((int)decode_execute.getValue(RegisterName.ALU_OP)) {
			case (0):
				result = aluArg1 + aluArg2;
				break;
			case (1):
				result = aluArg1 - aluArg2;
				break;
			case (2):
				result = aluArg1 & aluArg2;
				break;
			case (3):
				result = aluArg1 | aluArg2;
				break;
			case (4):
				result = ~(aluArg1 | aluArg2) & 0xFFFFFFFFl;
				break;
			case (5):
				result = aluArg1 < aluArg2 ? 1 : 0;
				break;
		}


		System.out.println("EX MEM RESULT ");
		System.out.println(result);
		System.out.println(writeData);
		
		execute_mem.setValue(RegisterName.ALU_RESULT, result);
		execute_mem.setValue(RegisterName.WRITE_DATA, writeData);
	}
}