package parser;

import java.util.ArrayList;
import java.util.HashMap;

import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.EnumConstantDeclaration;
import com.github.javaparser.ast.body.EnumDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import com.github.javaparser.ast.expr.SimpleName;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ReturnStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.stmt.SwitchEntry;
import com.github.javaparser.ast.stmt.SwitchStmt;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.TypeParameter;

public class Factorizer {
	
	private FileManager _parser;
	
	private HashMap<String, ClassOrInterface> _factoryClasses;
	
	public Factorizer(FileManager parser)
	{
		_parser = parser;
		
		_factoryClasses = new HashMap<String, ClassOrInterface>();
	}
	
	public ClassOrInterface factorize(String className) throws Throwable
	{
		ClassOrInterface  currentCos = _parser.parse(className);
		
		if(currentCos.isInterface() == false)
		{
			ArrayList<ClassOrInterfaceType> interfaces = currentCos.getImplements();
			
			if(interfaces.size() == 1)
			{
				ClassOrInterfaceType root = interfaces.get(0);
				
				ArrayList<ConstructorDeclaration> constructors = currentCos.getConstructors();
				
				if(constructors.size() == 0)
                {
                    constructors.add(new ConstructorDeclaration(currentCos.getName().toString()));
                }
				
				if(constructors.size() == 1)
				{
					NodeList<Parameter> constructorParameters = constructors.get(0).getParameters();
					
					String name  = root.getName().getIdentifier();
					
					name = name.substring(1);
					
					ClassOrInterface factoryClass = null;
				
					if(_factoryClasses.containsKey(name))
					{
						factoryClass = _factoryClasses.get(name);
					}
					else
					{
						if(_parser.canParse(name + "Factory") == false)
						{
							factoryClass = new ClassOrInterface(name + "Factory");
							
							factoryClass.AddEnum(NodeList.nodeList(new Modifier(Modifier.Keyword.PUBLIC)), "Enum" + name);
							
							Parameter enumParameter = new Parameter(new TypeParameter("Enum" + name), new SimpleName("type"));
							
							SwitchStmt stmt = new SwitchStmt();
							
							stmt.setSelector(new NameExpr("type"));

							BlockStmt block = new BlockStmt();
							
							block.setStatements(NodeList.nodeList((Statement)stmt));
							
							NodeList<Parameter> parameters = new NodeList<Parameter>();
						
							parameters.add(enumParameter);
							
							parameters.addAll(constructorParameters);
							
							factoryClass.AddMethod(NodeList.nodeList(new Modifier(Modifier.Keyword.PUBLIC), new Modifier(Modifier.Keyword.STATIC)),
									   "create" +name, root, parameters , block);
							
							_factoryClasses.put(name, factoryClass);
						}
						else
						{
							factoryClass = _parser.parse(name + "Factory");
							
							_factoryClasses.put(name, factoryClass);
						}
					}
					
					EnumDeclaration enums = factoryClass.getEnums().get(0);
					
					for(EnumConstantDeclaration cstDecl: enums.getEntries())
					{
						if(cstDecl.getName().getIdentifier().equals((currentCos.getName().toUpperCase())))
							return currentCos;
					}
					
					enums.addEntry(new EnumConstantDeclaration(currentCos.getName().toUpperCase()));
					
					MethodDeclaration createMethod = factoryClass.getMethods().get(0);
					
					SwitchStmt switchStatement = createMethod.getBody().get().getStatements().get(0).asSwitchStmt();
					
					SwitchEntry switchEntry = new SwitchEntry();
					
					switchEntry.setLabels(NodeList.nodeList((Expression)new NameExpr("Enum" + name  + "." + currentCos.getName().toUpperCase())));
					
					ReturnStmt returnStmt = new ReturnStmt();
					
					NodeList<Expression> constructorArguments = new NodeList<Expression>();
					
					NodeList<Parameter> createMethodParameters = createMethod.getParameters();
					
					if(constructorParameters.size() != createMethodParameters.size() -1)
						throw new ConstructorParametersLengthFactorizerError();
					
					for(int i=0;i<createMethodParameters.size() - 1;++i)
					{
						if(createMethodParameters.get(i+1).getType().toString().equals(constructorParameters.get(i).getType().toString()) == false)
						{
							throw new ConstructorParametersFactorizerError();
						}
					}
					
					for(int i = 1 ;i < createMethodParameters.size();++i)
					{
						constructorArguments.add(new NameExpr(createMethodParameters.get(i).getName().getIdentifier()));
					}
					
					returnStmt.setExpression(new ObjectCreationExpr(null, currentCos.getType(), constructorArguments));
					
					switchEntry.setStatements(NodeList.nodeList((Statement)returnStmt));
				
					NodeList<SwitchEntry> switchEntries = switchStatement.getEntries();
					
					switchEntries.add(switchEntry);
					
					switchStatement.setEntries(switchEntries);
						
					return factoryClass;
				}
				else
				{
					throw new ManyConstructorsFactorizerError();
				}
				
			}
			else if(interfaces.size() == 0)
			{
				throw new NoInterfaceFactorizerError();
			}
			
		}
		else
		{
			throw new NotInterfaceFactorizerError();
		}
		
		return null;
	}
}
