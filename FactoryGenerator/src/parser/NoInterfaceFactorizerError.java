package parser;

public class NoInterfaceFactorizerError extends Throwable{

	@Override
	public String getMessage() {
		// TODO Auto-generated method stub
		return "The class implements no interface, can't factorize";
	}

}
