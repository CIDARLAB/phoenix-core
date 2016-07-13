package parameterestimation;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import javax.xml.stream.XMLStreamException;

import learn.genenet.Experiments;
import learn.genenet.SpeciesCollection;
import learn.parameterestimator.ParameterEstimator;

public class ParameterEstimation {

	public static void estimateParameters(String sbmlFile, List<String> parameters, List<String> experimentFiles) throws IOException, XMLStreamException {
		String[] split = sbmlFile.split(File.separator);
		String root = sbmlFile.substring(0, sbmlFile.length() - split[split.length - 1].length());
		Experiments experiments = new Experiments();
		SpeciesCollection speciesCollection = new SpeciesCollection();
		for (String experiment : experimentFiles) {
			parseCSV(experiment, speciesCollection, experiments);
		}
		ParameterEstimator.estimate(sbmlFile, root, parameters, experiments, speciesCollection);
	}
	
	private static void parseCSV(String filename, SpeciesCollection speciesCollection, Experiments experiments)
	{
		int experiment = experiments.getNumOfExperiments();
		Scanner scan = null;
		boolean isFirst = true;
		try
		{
			scan = new Scanner(new File(filename));
			int row = 0;
			while (scan.hasNextLine())
			{
				String line = scan.nextLine();

				String[] values = line.split(",");

				if (isFirst)
				{
					for (int i = 0; i < values.length; i++)
					{
						speciesCollection.addSpecies(values[i], i);
					}
					isFirst = false;
				}
				else
				{
					for (int i = 0; i < values.length; i++)
					{
						experiments.addExperiment(experiment, row, i, Double.parseDouble(values[i]));
					}
					row++;
				}
			}
		}
		catch (FileNotFoundException e)
		{
			System.out.println("Could not find the file!");
		}
		finally
		{
			if (scan != null)
			{
				scan.close();
			}

		}
	}

}
