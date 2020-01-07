package edu.projet.Prof;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SujetObjet {
	
	private static String sep = "&";
	
	public static String sujetObjet (String terme) {
 
		Path sujetObjet = FileSystems.getDefault().getPath("src/edu/projet/Prof/sujetObjet");
		
		try (Stream<String> lines = Files.lines(sujetObjet)) {
			
			List< List<String> > items = lines.map(line -> Arrays.asList(line.split(sep))).collect(Collectors.toList());
			
			for(int i = 0; i < items.size(); i++) {
			      
				if(terme.equalsIgnoreCase(items.get(i).get(0))) {
			    	  return items.get(i).get(1);  
			    }
				if(terme.equalsIgnoreCase(items.get(i).get(1))) {
			    	  return items.get(i).get(0);  
			    }
			}
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}
}


