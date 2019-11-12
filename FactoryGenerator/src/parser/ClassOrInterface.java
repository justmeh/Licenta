package parser;

import java.util.ArrayList;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.EnumDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.expr.SimpleName;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.ReferenceType;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.type.TypeParameter;


public class ClassOrInterface{
	

	private ClassOrInterfaceDeclaration _classOrInterface;
	
	
	public ClassOrInterface(String name)
	{
		CompilationUnit compilationUnit = new CompilationUnit();
		
		_classOrInterface = compilationUnit.addClass(name);
	}
	
	public ClassOrInterface(ClassOrInterfaceDeclaration classOrInterface)
	{
		_classOrInterface = classOrInterface;
	}
	
	public String getName()
	{
		return _classOrInterface.getName().getIdentifier();
	}
	
	public ClassOrInterfaceType getType()
	{
		return StaticJavaParser.parseClassOrInterfaceType(_classOrInterface.asTypeDeclaration().getNameAsString()).asClassOrInterfaceType();
	}
	
	public ArrayList<FieldDeclaration> getMembers()
	{
		ArrayList<FieldDeclaration> fieldDeclarations = new ArrayList<FieldDeclaration>();
		
		for(BodyDeclaration<?> bodyDeclaration: _classOrInterface.getMembers())
		{
			if(bodyDeclaration instanceof FieldDeclaration)
			{
				fieldDeclarations.add((FieldDeclaration)bodyDeclaration);
			}
		}
		
		return fieldDeclarations;
	}
	
	public ArrayList<ConstructorDeclaration> getConstructors()
	{
		ArrayList<ConstructorDeclaration> constructorDeclarations = new ArrayList <ConstructorDeclaration>();
		
		for(BodyDeclaration<?> bodyDeclaration: _classOrInterface.getMembers())
		{
			if(bodyDeclaration instanceof ConstructorDeclaration)
			{
				constructorDeclarations.add((ConstructorDeclaration)bodyDeclaration);
			}
		}
		
		return constructorDeclarations;
	}

	
	public ArrayList<MethodDeclaration> getMethods()
	{
		ArrayList<MethodDeclaration> methodDeclarations = new ArrayList <MethodDeclaration>();
		
		for(BodyDeclaration<?> bodyDeclaration: _classOrInterface.getMembers())
		{
			if(bodyDeclaration instanceof MethodDeclaration)
			{
				methodDeclarations.add((MethodDeclaration)bodyDeclaration);
			}
		}
		
		return methodDeclarations;
	}
	
	public void AddMethod(final NodeList<Modifier> modifiers, final String name, final Type type, final NodeList<Parameter> parameters, BlockStmt body)
	{
		_classOrInterface.addMember( new MethodDeclaration(modifiers,new NodeList<AnnotationExpr>() , new NodeList<TypeParameter>(),type,new SimpleName(name), parameters, new NodeList<ReferenceType>(),body));
	}
	
	public void AddEnum(final NodeList<Modifier> modifiers, final String name)
	{
		_classOrInterface.addMember(new EnumDeclaration(modifiers, name));
	}
	
	public ArrayList<EnumDeclaration> getEnums()
	{
		ArrayList<EnumDeclaration> enumDeclarations = new ArrayList <EnumDeclaration>();
		
		for(BodyDeclaration<?> bodyDeclaration: _classOrInterface.getMembers())
		{
			if(bodyDeclaration instanceof EnumDeclaration)
			{
				enumDeclarations.add((EnumDeclaration)bodyDeclaration);
			}
		}
		
		return enumDeclarations;
	}
	
	public ArrayList<ClassOrInterfaceType> getImplements()
	{
		ArrayList<ClassOrInterfaceType> implementsList = new ArrayList<ClassOrInterfaceType>();
		for(ClassOrInterfaceType implement: _classOrInterface.getImplementedTypes())
		{
			implementsList.add(implement);
		}
		
		return implementsList;
	}
	
	@Override
	public String toString()
	{
		return _classOrInterface.toString();
	}
	
	public Boolean isInterface()
	{
		return _classOrInterface.isInterface();
	}
	
}
