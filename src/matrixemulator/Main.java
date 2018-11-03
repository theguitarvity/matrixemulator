package matrixemulator;

import java.io.IOException;

import matrixemulator.Register;
import matrixemulator.Memory;


import matrixemulator.Fetch;
import matrixemulator.Decode;
import matrixemulator.Exec;
import matrixemulator.Mem;
import matrixemulator.WriteBack;

public class Main {

  private final PC programCounter = new PC();

  private final PipelineRegister if_id      = new PipelineRegister();

  private final PipelineRegister id_ex      = new PipelineRegister();

  private final PipelineRegister ex_mem     = new PipelineRegister();

  private final PipelineRegister mem_wb     = new PipelineRegister();

  private final RegisterFile registerFile   = new RegisterFile();

  private final Fetch fetch;
  private final Decode decode;
  private final Exec exec;
  private final Mem mem;
  private final WriteBack writeback;

  public static void main(String[] args) {
    if (args.length < 1) {
      System.out.println("You must supply an input filename");
    } else {
      try {
        // create the simulator
        Main mips = new Main(args[0]);
        
        // run the simulator
        mips.run();
      } catch (IOException e) {
        System.out.println("Error opening file named \"" + args[0] + "\"");
      }
    }
  }

  public Main(String filename) throws IOException {

    Memory memory = new Memory(filename);
    
    fetch  = new Fetch(if_id, memory, programCounter);
    decode = new Decode(if_id, id_ex, registerFile, programCounter);
    exec   = new Exec(id_ex, ex_mem, mem_wb);

    mem       = new Mem(ex_mem, mem_wb, memory);
    writeback = new WriteBack(mem_wb, registerFile);

  }

  public void run() {
    int instructionCount = 0;
    int cycleCount = 0;
    
    fetch.run();
    decode.run();
    exec.run();
    mem.run();
    writeback.run();

    // increment the cycle counter
    cycleCount++;
      
    // tick over all our register values
    tick();
    
    // compute the cycles per instruction
    float cpi = (float)cycleCount / instructionCount;
    
    // output the results
    System.out.println("Instruction count: \t" + instructionCount);
    System.out.println("Cycle count: \t\t" + cycleCount);
    System.out.println("CPI: \t\t\t" + cpi);
    System.out.println(registerFile);

  }

  private void tick() {
    programCounter.tick();
    if_id.tick();
    id_ex.tick();
    mem_wb.tick();
  }

}