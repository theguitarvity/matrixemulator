package matrixemulator;

public class Register {

	private long value = 0;
	
	protected long newValue = -1;

	public Register() {
		this(0);
	}
	
	public Register(long value) {
		this.value = value;
	}
	
	public long getValue() {
		return value;
	}
	
	public void setValue(long newValue) {
		this.newValue = newValue;
	}

	public void tick() {
		if (newValue != -1) {
			value = newValue;
			newValue = -1;
		}
	}
}