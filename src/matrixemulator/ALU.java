package matrixemulator;



public final  class ALU implements AluOperations{
	private int op;
	private int alusrc1;
	private int alusrc2;
	private int alurtr;
	private int[] returns;
	
	
	public int[] ALUActivate(int op, int alu1, int alu2, int alurt) {
		returns = new int[3];
		switch(op) {
			case 0:
				returns = buildOutput(and(), 0, 0);
				break;
			case 1:
				returns = buildOutput(or(), 0, 0);
				break;
			case 2:
				returns = buildOutput(add(), 0, 0);
				break;
			case 4:
				returns = buildOutput(slt(), 0, 0);
			
		}
		return returns;
		
	}
	
	

	private int[] buildOutput(int res, int i, int j) {
		// TODO Auto-generated method stub
		returns[0] = res;
		returns[1] = i;
		returns[2] = j;
		return returns;
	}



	@Override
	public int and() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int or() {
		// TODO Auto-generated method stub
		return alusrc1|alusrc2;
	}

	@Override
	public int add() {
		// TODO Auto-generated method stub
		return alusrc1+ alusrc2;
	}

	@Override
	public int subtract() {
		// TODO Auto-generated method stub
		return alusrc1-alusrc2;
	}

	@Override
	public int slt() {
		// TODO Auto-generated method stub
		if(alusrc1<alusrc2)
			return 1;
		else
			return 0;
	}

	@Override
	public int beq() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int bne() {
		// TODO Auto-generated method stub
		return 0;
	}
	

}
