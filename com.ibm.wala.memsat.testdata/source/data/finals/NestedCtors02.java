package data.finals;

public class NestedCtors02 {
	public static class Base {
		public final int baseField;
		
		protected Base() {
			baseField = 1;
		}
	}
	
	public static final class Derived extends Base {
		public final int derivedField;
		
		public Derived() {
			derivedField = 1;
			d = this;
		}
	}
	
	public static Derived d = null;
	
	public static final void p1() {
		Base ignored = new Derived();
	}
	
	public static final void p2() {
		Derived td = d;
		assert td != null;
		if (td != null) {
			assert td.baseField == 0;
			assert td.derivedField == 0;
		}
	}
}
