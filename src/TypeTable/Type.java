package TypeTable;

public abstract class Type {
		public int index;
		static int counter = 1;
	
		public Type() {
			index = counter++;
		}
        public abstract boolean SubType(Type type);
        
        
        
}