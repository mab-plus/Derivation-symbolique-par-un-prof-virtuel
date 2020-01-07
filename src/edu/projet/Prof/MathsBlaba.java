package edu.projet.Prof;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MathsBlaba {
	private static String sep = "&";
	
	public static List< List<String> >  mathsBlaba () {
 
		Path mathsBlabla = FileSystems.getDefault().getPath("src/edu/projet/Prof/mathsBlabla");
		
		try (Stream<String> lines = Files.lines(mathsBlabla)) {
			
			List< List<String> > items = lines.map(line -> Arrays.asList(line.split(sep))).collect(Collectors.toList());
			return items;
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}