package matrixemulator;

import java.io.IOException;

import matrixemulator.Register;
import matrixemulator.Fetch;
import matrixemulator.Memory;

public class Main {

  private final PC programCounter = new PC();

  private final PipelineRegister if_id    = new PipelineRegister();

  private final RegisterFile registerFile = new RegisterFile();

  private final Fetch fetch;

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
    
    fetch = new Fetch(if_id, memory, programCounter);


    // decode = new Decode(if_id, id_ex, registerFile, programCounter);
    // execute = new Execute(id_ex, ex_mem, mem_wb);
    // memory = new Memory(ex_mem, mem_wb, memoryStore);
    // writeback = new Writeback(mem_wb, registerFile);

  }

  public void run() {
    int instructionCount = 0;
    int cycleCount = 0;
    
    fetch.run();
      
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
  }

}