package matrixemulator;

import java.util.HashMap;

import matrixemulator.PC;
import matrixemulator.RegisterFile;
import matrixemulator.RegisterName;
import matrixemulator.PipelineRegister;
import matrixemulator.Run;

public class Decode extends Run {
	// values for decoding the instruction
	private static final int OPCODE_MASK = 0xFC000000;
	private static final int OPCODE_SHIFT = 26;
	private static final int RS_MASK = 0x3E00000;
	private static final int RS_SHIFT = 21;
	private static final int RT_MASK = 0x1F0000;
	private static final int RT_SHIFT = 16;
	private static final int RD_MASK = 0xF800;
	private static final int RD_SHIFT = 11;
	private static final int SHAMT_MASK = 0x7C0;
	private static final int SHAMT_SHIFT = 6;
	private static final int FUNCT_MASK = 0x3F;
	private static final int FUNCT_SHIFT = 0;
	private static final int ADDRESS_MASK = 0x3FFFFFF;
	private static final int ADDRESS_SHIFT = 0;
	private static final int IMMEDIATE_MASK = 0xFFFF;
	private static final int IMMEDIATE_SHIFT = 0;
	
	// values for opcodes and funct codes
	private static final int ARITH_OP_CODE = 0;
	private static final int ADD_FUNCT = 0x20;
	private static final int SUB_FUNCT = 0x22;
	private static final int ORI = 0xD;
	private static final int SLTI = 0xA;
	private static final int AND_FUNCT = 0x24;
	private static final int OR_FUNCT = 0x25;
	private static final int ADDI = 0x8;
	private static final int ANDI = 0xC;
	private static final int BEQ = 0x4;
	private static final int BNE = 0x5;
	private static final int NOR_FUNCT = 0x27;
	private static final int SLT_FUNCT = 0x2a;
	private static final int J = 0x2;
	private static final int JR_FUNCT = 0x8;
	private static final int LW = 0x23;
	private static final int SW = 0x2B;
	private static final int HLT = 0x3F;
	
	public static final int NOP = 0xFF;
	
	private final PipelineRegister fetch_decode;
	private final PipelineRegister decode_execute;
	private final RegisterFile registerFile;
	private final PC pc;
	
	public Decode(PipelineRegister fetch_decode, PipelineRegister decode_execute, RegisterFile registerFile,PC pc) {
		this.fetch_decode = fetch_decode;
		this.decode_execute = decode_execute;
		this.registerFile = registerFile;
		this.pc = pc;
	}
	
	@Override
	public void run() {
		long instruction = fetch_decode.getValue(RegisterName.INSTRUCTION);
		long opCode = (instruction & OPCODE_MASK) >> OPCODE_SHIFT;
		long rs = (instruction & RS_MASK) >> RS_SHIFT;
		long rt = (instruction & RT_MASK) >> RT_SHIFT;
		long rd = (instruction & RD_MASK) >> RD_SHIFT;
		long shamt = (instruction & SHAMT_MASK) >> SHAMT_SHIFT;
		long funct = (instruction & FUNCT_MASK) >> FUNCT_SHIFT;
		long immediate = (instruction & IMMEDIATE_MASK) >> IMMEDIATE_SHIFT;
		long address = (instruction & ADDRESS_MASK) >> ADDRESS_SHIFT;
				
		decode_execute.setValue(RegisterName.R_S, rs);
		decode_execute.setValue(RegisterName.R_T, rt);
		decode_execute.setValue(RegisterName.R_D, rd);
		decode_execute.setValue(RegisterName.SHAMT, shamt);
		decode_execute.setValue(RegisterName.IMMEDIATE, immediate);
		decode_execute.setValue(RegisterName.ADDRESS, address);
		decode_execute.setValue(RegisterName.OP_CODE, opCode);
		
		HashMap<RegisterName, Long> controlLines = 
			setControlLines(opCode, funct);
		
		long readData1 = registerFile.getValue(RegisterName.valueOf(rs));
		long readData2 = registerFile.getValue(RegisterName.valueOf(rt));
		
		decode_execute.setValue(RegisterName.READ_DATA_1, readData1);
		decode_execute.setValue(RegisterName.READ_DATA_2, readData2);
		
		if (decode_execute.getValue(RegisterName.MEM_READ) == 1 && ((decode_execute.getValue(RegisterName.R_T) == rs) || (decode_execute.getValue(RegisterName.R_T) == rt))) {
		} else if (controlLines.get(RegisterName.BRANCH) == 1 && ((controlLines.get(RegisterName.BRANCH_NE) == 1 && readData1 != readData2) || (controlLines.get(RegisterName.BRANCH_NE) == 0 && readData1 == readData2))) {
			takeBranch(immediate);
		} else if (controlLines.get(RegisterName.JUMP) == 1) {
			long jumpAddress = address;
			
			if (controlLines.get(RegisterName.JUMP_SRC) == 1) {
				jumpAddress = readData1;
				
				if (jumpAddress % 4 != 0) {
					throw new IllegalArgumentException("Valor do jump não é divisivel por 4");
				}
				
				jumpAddress /= 4;
			}
			
			takeJump(jumpAddress);
		}
	}
	
	private HashMap<RegisterName, Long> setControlLines(long opCode, long funct) {
		HashMap<RegisterName, Long> values = 
			new HashMap<RegisterName, Long>();
		
		values.put(RegisterName.REG_DST, 0l);
		decode_execute.setValue(RegisterName.REG_DST, 0l);
		values.put(RegisterName.ALU_SRC, 0l);
		decode_execute.setValue(RegisterName.ALU_SRC, 0l);
		values.put(RegisterName.MEM_TO_REG, 0l);
		decode_execute.setValue(RegisterName.MEM_TO_REG, 0l);
		values.put(RegisterName.REG_WRITE, 0l);
		decode_execute.setValue(RegisterName.REG_WRITE, 0l);
		values.put(RegisterName.MEM_READ, 0l);
		decode_execute.setValue(RegisterName.MEM_READ, 0l);
		values.put(RegisterName.MEM_WRITE, 0l);
		decode_execute.setValue(RegisterName.MEM_WRITE, 0l);
		values.put(RegisterName.BRANCH, 0l);
		decode_execute.setValue(RegisterName.BRANCH, 0l);
		values.put(RegisterName.BRANCH_NE, 0l);
		decode_execute.setValue(RegisterName.BRANCH_NE, 0l);
		values.put(RegisterName.JUMP, 0l);
		decode_execute.setValue(RegisterName.JUMP, 0l);	
		values.put(RegisterName.JUMP_SRC, 0l);
		decode_execute.setValue(RegisterName.JUMP_SRC, 0l);
		values.put(RegisterName.ALU_OP, 0l);
		decode_execute.setValue(RegisterName.ALU_OP, 0l);		
		values.put(RegisterName.HALT, 0l);
		decode_execute.setValue(RegisterName.HALT, 0l);
		
		if (fetch_decode.getValue(RegisterName.OP_CODE) == NOP) {
			return values;
		}
		
		switch ((int)opCode) {
			case ARITH_OP_CODE:
				values.put(RegisterName.REG_DST, 1l);
				decode_execute.setValue(RegisterName.REG_DST, 1l);

				values.put(RegisterName.REG_WRITE, 1l);
				decode_execute.setValue(RegisterName.REG_WRITE, 1l);

				switch ((int)funct) {
					case SUB_FUNCT:
						values.put(RegisterName.ALU_OP, 1l);
						decode_execute.setValue(RegisterName.ALU_OP, 1l);
						break;
					case AND_FUNCT:
						values.put(RegisterName.ALU_OP, 2l);
						decode_execute.setValue(RegisterName.ALU_OP, 2l);
						break;
					case OR_FUNCT:
						values.put(RegisterName.ALU_OP, 3l);
						decode_execute.setValue(RegisterName.ALU_OP, 3l);
						break;
					case NOR_FUNCT:
						values.put(RegisterName.ALU_OP, 4l);
						decode_execute.setValue(RegisterName.ALU_OP, 4l);
						break;
					case SLT_FUNCT:
						values.put(RegisterName.ALU_OP, 5l);
						decode_execute.setValue(RegisterName.ALU_OP, 5l);
						break;		
					case JR_FUNCT:
						values.put(RegisterName.JUMP, 1l);
						decode_execute.setValue(RegisterName.JUMP, 1l);
						values.put(RegisterName.JUMP_SRC, 1l);
						decode_execute.setValue(RegisterName.JUMP_SRC, 1l);
						break;
				}
				break;
				
			case ADDI:
				values.put(RegisterName.ALU_SRC, 1l);
				decode_execute.setValue(RegisterName.ALU_SRC, 1l);
				values.put(RegisterName.REG_WRITE, 1l);
				decode_execute.setValue(RegisterName.REG_WRITE, 1l);
				break;
			case ANDI:
				values.put(RegisterName.ALU_SRC, 1l);
				decode_execute.setValue(RegisterName.ALU_SRC, 1l);
				values.put(RegisterName.REG_WRITE, 1l);
				decode_execute.setValue(RegisterName.REG_WRITE, 1l);
				values.put(RegisterName.ALU_OP, 2l);
				decode_execute.setValue(RegisterName.ALU_OP, 2l);
				break;
			case ORI:
				values.put(RegisterName.ALU_SRC, 1l);
				decode_execute.setValue(RegisterName.ALU_SRC, 1l);
				values.put(RegisterName.REG_WRITE, 1l);
				decode_execute.setValue(RegisterName.REG_WRITE, 1l);
				values.put(RegisterName.ALU_OP, 3l);
				decode_execute.setValue(RegisterName.ALU_OP, 3l);
				break;
			case SLTI:
				values.put(RegisterName.ALU_SRC, 1l);
				decode_execute.setValue(RegisterName.ALU_SRC, 1l);
				values.put(RegisterName.REG_WRITE, 1l);
				decode_execute.setValue(RegisterName.REG_WRITE, 1l);
				values.put(RegisterName.ALU_OP, 5l);
				decode_execute.setValue(RegisterName.ALU_OP, 5l);
				break;
			case BEQ:
				values.put(RegisterName.BRANCH, 1l);
				decode_execute.setValue(RegisterName.BRANCH, 1l);
				values.put(RegisterName.ALU_OP, 1l);
				decode_execute.setValue(RegisterName.ALU_OP, 1l);
				break;
			case BNE:
				values.put(RegisterName.BRANCH, 1l);
				decode_execute.setValue(RegisterName.BRANCH, 1l);
				values.put(RegisterName.BRANCH_NE, 1l);
				decode_execute.setValue(RegisterName.BRANCH_NE, 1l);
				values.put(RegisterName.ALU_OP, 1l);
				decode_execute.setValue(RegisterName.ALU_OP, 1l);
				break;
				
			case J:
				values.put(RegisterName.JUMP, 1l);
				decode_execute.setValue(RegisterName.JUMP, 1l);
				break;
			case LW:
				values.put(RegisterName.ALU_SRC, 1l);
				decode_execute.setValue(RegisterName.ALU_SRC, 1l);
				values.put(RegisterName.MEM_TO_REG, 1l);
				decode_execute.setValue(RegisterName.MEM_TO_REG, 1l);
				values.put(RegisterName.REG_WRITE, 1l);
				decode_execute.setValue(RegisterName.REG_WRITE, 1l);
				values.put(RegisterName.MEM_READ, 1l);
				decode_execute.setValue(RegisterName.MEM_READ, 1l);
				break;	
			case SW:
				values.put(RegisterName.ALU_SRC, 1l);
				decode_execute.setValue(RegisterName.ALU_SRC, 1l);
				values.put(RegisterName.MEM_WRITE, 1l);
				decode_execute.setValue(RegisterName.MEM_WRITE, 1l);
				break;
			case HLT:
				values.put(RegisterName.HALT, 1l);
				decode_execute.setValue(RegisterName.HALT, 1l);
				break;
		}
		
		return values;
	}
	private void takeJump(long address) {
		zeroOutRegister(fetch_decode);
		
		pc.setValue(address * 4);
	}

	private void takeBranch(long addressOffset) {
		zeroOutRegister(fetch_decode);
		
		pc.setValue(fetch_decode.getValue(RegisterName.PC) + 4 + 4 * addressOffset);
	}
	
	private void zeroOutRegister(PipelineRegister rp) {
		rp.setValue(RegisterName.REG_DST, 0);
		rp.setValue(RegisterName.MEM_TO_REG, 0);
		rp.setValue(RegisterName.ALU_SRC, 0);
		rp.setValue(RegisterName.REG_WRITE, 0);
		rp.setValue(RegisterName.JUMP, 0);
		rp.setValue(RegisterName.MEM_READ, 0);
		rp.setValue(RegisterName.BRANCH_NE, 0);
		rp.setValue(RegisterName.MEM_WRITE, 0);
		rp.setValue(RegisterName.BRANCH, 0);
		rp.setValue(RegisterName.ALU_OP, 0);
		rp.setValue(RegisterName.JUMP_SRC, 0);
		rp.setValue(RegisterName.OP_CODE, NOP);
	}
}