package parser;

public class ManyInterfacesFactorizerError extends Throwable {

	@Override
	public String getMessage() {
		// TODO Auto-generated method stub
		return "The class implements too many interfaces, can't decide based on which one to factorize";
	}

}
