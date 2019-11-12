package parser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;


public class FileManager {
	
	private String _workingDirectory;
	
	private HashMap<String, ClassOrInterface> _classesOrInterfaces;
	
	public FileManager(String workingDirectory)
	{
		_workingDirectory = workingDirectory;
		
		_classesOrInterfaces = new HashMap<String, ClassOrInterface>();
	}
	
	
	public Boolean canParse(String className)
	{
		File file = new File(_workingDirectory + "\\" + className + ".java");
		return file.exists() && !file.isDirectory();
	}
	
	public ClassOrInterface parse(String className)
	{
		
		if(_classesOrInterfaces.containsKey(className))
		{
			return _classesOrInterfaces.get(className);
		}
		
		ArrayList<ClassOrInterface> classInfo = new ArrayList<ClassOrInterface>();
		
		try {
			new VoidVisitorAdapter<ArrayList<ClassOrInterface> >()
			{
				
				 @Override
			     public void visit(ClassOrInterfaceDeclaration classOrInterface, ArrayList<ClassOrInterface> arg) {
			         super.visit(classOrInterface, arg);
			       
			         arg.add(new ClassOrInterface(classOrInterface));
			     }
				 
			}.visit(StaticJavaParser.parse(new File(_workingDirectory + "\\" + className + ".java")), classInfo);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ClassOrInterface desiredClass = null;
		
		for(ClassOrInterface classOrInterface: classInfo)
		{
			if(_classesOrInterfaces.containsKey(classOrInterface.getName()) == false)
			{
				_classesOrInterfaces.put(classOrInterface.getName(), classOrInterface);
			}
		
			if(classOrInterface.getName().equals(className))
			{
				desiredClass = classOrInterface;
			}
		}
		
		return desiredClass;	
	}
	
	
	public void write(ClassOrInterface classOrInterface)
	{
		try {
			FileWriter writer = new FileWriter(_workingDirectory + "\\" + classOrInterface.getName() + ".java");
			
			writer.write(classOrInterface.toString());
			
			writer.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
}
