package parser;

public class ConstructorParametersFactorizerError extends Throwable {
	@Override
	public String getMessage() {
		// TODO Auto-generated method stub
		return "The class constructor and factory class method don't have the same parameters type";
	}

}
