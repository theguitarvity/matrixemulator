package matrixemulator;

import java.io.IOException;

import matrixemulator.Register;
import matrixemulator.MemoriaPrincipal;


import matrixemulator.Fetch;
import matrixemulator.Decode;
import matrixemulator.ControllerMips;
import matrixemulator.Mem;
import matrixemulator.WriteBack;

public class Main {

  private final PC programCounter = new PC();

  private final PipelineRegister fetch_decode      = new PipelineRegister();

  private final PipelineRegister deocde_execution  = new PipelineRegister();

  private final PipelineRegister execution_mem     = new PipelineRegister();

  private final PipelineRegister mem_writeback     = new PipelineRegister();

  private final RegisterFile registerFile   = new RegisterFile();

  private final Fetch fetch;
  private final Decode decode;
  private final ControllerMips exec;
  private final Mem mem;
  private final WriteBack writeback;

  public static void main(String[] args) {
    if (args.length < 1) {
      System.out.println("Aponte um arquivo binario");
    } else {
      try {
        Main mips = new Main(args[0]);
        mips.run();
      } catch (IOException e) {
        System.out.println("Erro ao abrir arquivo \"" + args[0] + "\"");
      }
    }
  }

  public Main(String filename) throws IOException {

    MemoriaPrincipal memory = new MemoriaPrincipal(filename);
    InstructionMemory inst = new InstructionMemory(filename);
    
    fetch  = new Fetch(fetch_decode, inst, programCounter);
    decode = new Decode(fetch_decode, deocde_execution, registerFile, programCounter);
    exec   = new ControllerMips(deocde_execution, execution_mem, mem_writeback);

    mem       = new Mem(execution_mem, mem_writeback, memory);
    writeback = new WriteBack(mem_writeback, registerFile);

  }

  public void run() {
    int cycle = 0;
    int instrCount = 0;
    
    fetch.run();
    decode.run();
    exec.run();
    mem.run();
    writeback.run();

    // incrementa ciclo
    cycle++;
      
    // clock pra todos os registradores
    tick();
    
    // output do resultado
    System.out.println("Número de instruções: \t" + instrCount);
    System.out.println("Ciclos executados: \t\t" + cycle);
    System.out.println(registerFile);

  }

  private void tick() {
    programCounter.tick();
    fetch_decode.tick();
    deocde_execution.tick();
    mem_writeback.tick();
  }

}