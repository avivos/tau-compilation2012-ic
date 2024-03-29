package IC.AST;

/**
 * Abstract base class for variable reference AST nodes.
 * 
 * @author Tovi Almozlino
 */
public abstract class Location extends Expression {

	/**
	 * Constructs a new variable reference node. Used by subclasses.
	 * 
	 * @param line
	 *            Line number of reference.
	 */
	public TypeTable.Type TTtype; //used only for arrays !
	
	protected Location(int line) {
		super(line);
	}

}
