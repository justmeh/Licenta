package parser;

public class NotInterfaceFactorizerError extends Throwable{
	
	@Override
	public String getMessage() {
		// TODO Auto-generated method stub
		return "This is not a class, but an interface";
	}
}
