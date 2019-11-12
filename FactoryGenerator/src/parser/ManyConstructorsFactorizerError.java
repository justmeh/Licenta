package parser;

public class ManyConstructorsFactorizerError extends Throwable {

	@Override
	public String getMessage() {
		// TODO Auto-generated method stub
		return "The class you are trying to factorize cotains too many constructors. The number of constructors should be 1 "
				+ "either just the default one or another one";
	}

}
