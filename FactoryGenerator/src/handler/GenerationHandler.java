package handler;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;

import parser.ClassOrInterface;
import parser.Factorizer;
import parser.FileManager;


public class GenerationHandler extends AbstractHandler {

	String filePath;
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		
		IWorkbench workbench = PlatformUI.getWorkbench();
		IWorkbenchWindow window =  workbench == null ? null : workbench.getActiveWorkbenchWindow();
		IWorkbenchPage activePage = window == null ? null : window.getActivePage();

		IEditorPart editor =  activePage == null ? null : activePage.getActiveEditor();
		IEditorInput input =  editor == null ? null : editor.getEditorInput();
		IPath path = input instanceof FileEditorInput ? ((FileEditorInput)input).getPath() : null;
		if (path != null)
		{
		    filePath = path.toString();
		}
		
		filePath = filePath.substring(0, filePath.lastIndexOf('.'));
		
		filePath = filePath.replaceAll("/", "\\\\");

		String className = filePath.substring(filePath.lastIndexOf('\\') + 1);
		
		String projectPath = filePath.substring(0,filePath.lastIndexOf('\\'));
		
		FileManager parser = new FileManager(projectPath);
		
		Factorizer factorizer = new Factorizer(parser);
		
		try {
			ClassOrInterface classOrInterface = factorizer.factorize(className);
			
			parser.write(classOrInterface);
			
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		 return null;
	}
}
