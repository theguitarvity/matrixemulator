package matrixemulator;

public interface MemoryInterface<Address, Value> {
	public void put(Address a, Value v);
	public void pop(Address a);
	public void next(); 
}
