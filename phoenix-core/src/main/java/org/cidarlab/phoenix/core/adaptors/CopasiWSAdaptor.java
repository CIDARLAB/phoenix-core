/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.phoenix.core.adaptors;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.xml.stream.XMLStreamException;

import org.copasi.copasiws.schemas.GetSimulatorStatusDocument;
import org.copasi.copasiws.schemas.OutputResult;
import org.copasi.copasiws.schemas.StatusCode;
import org.copasi.copasiws.schemas.User;
import org.copasi.copasiws.services.parameterestimationws.types.CreateSimulationResourceDocument;
import org.copasi.copasiws.services.parameterestimationws.types.CreateSimulationResourceResponseDocument;
import org.copasi.copasiws.services.parameterestimationws.types.ExperimentalData;
import org.copasi.copasiws.services.parameterestimationws.types.FitItemsAndMethod;
import org.copasi.copasiws.services.parameterestimationws.types.GeneticAlgorithm;
import org.copasi.copasiws.services.parameterestimationws.types.GetResultDocument;
import org.copasi.copasiws.services.parameterestimationws.types.GetResultResponseDocument;
import org.copasi.copasiws.services.parameterestimationws.types.ItemToFit;
import org.copasi.copasiws.services.parameterestimationws.types.LevenbergMarquardt;
import org.copasi.copasiws.services.parameterestimationws.types.Method;
import org.copasi.copasiws.services.parameterestimationws.types.ParticleSwarm;
import org.copasi.copasiws.services.parameterestimationws.types.SendExperimentalDataDocument;
import org.copasi.copasiws.services.parameterestimationws.types.SendModelDocument;
import org.copasi.copasiws.services.parameterestimationws.types.SetFitItemsAndMethodDocument;
import org.copasi.copasiws.services.parameterestimationws.types.SimulatedAnnealing;
import org.copasi.copasiws.services.parameterestimationws.types.StartSimulatorDocument;
import org.copasi.copasiws.services.timecoursews.types.DeterministicParameters;
import org.copasi.copasiws.services.timecoursews.types.RunDeterministicSimulatorDocument;
import org.copasi.copasiws.services.timecoursews.types.RunDeterministicSimulatorResponseDocument;
import org.copasi.copasiwsclient.ParameterEstimationServiceStub;
import org.copasi.copasiwsclient.ServiceFaultMessage;
import org.copasi.copasiwsclient.TimeCourseServiceStub;
import org.sbml.jsbml.LocalParameter;
import org.sbml.jsbml.Reaction;
import org.sbml.jsbml.SBMLDocument;
import org.sbml.jsbml.SBMLReader;

/**
 * 
 * @author ckmadsen
 * @author prash
 */
public class CopasiWSAdaptor {

	public static void estimateParams(String modelFile, String dataFile, String methodName) throws ServiceFaultMessage,
			IOException, XMLStreamException {
		String serviceAddress = "http://www.comp-sys-bio.org/CopasiWS/services/ParameterEstimationService";
		String userId = "phoenix";
		String password = "cidarlab";
		ParameterEstimationServiceStub stub = new ParameterEstimationServiceStub(serviceAddress);

		CreateSimulationResourceDocument sim = CreateSimulationResourceDocument.Factory.newInstance();
		CreateSimulationResourceDocument.CreateSimulationResource simElement = CreateSimulationResourceDocument.CreateSimulationResource.Factory
				.newInstance();
		User user = User.Factory.newInstance();
		user.setUserId(userId);
		user.setPassword(password);
		simElement.setUser(user);
		sim.setCreateSimulationResource(simElement);
		CreateSimulationResourceResponseDocument response = stub.createSimulationResource(sim);
		int resourceId = response.getCreateSimulationResourceResponse().getResourceId();

		SendModelDocument mod = SendModelDocument.Factory.newInstance();
		SendModelDocument.SendModel modElement = SendModelDocument.SendModel.Factory.newInstance();
		StringBuffer fileData = new StringBuffer(1000);
		BufferedReader reader = new BufferedReader(new FileReader(modelFile));
		char[] buf = new char[1024];
		int numRead = 0;

		while ((numRead = reader.read(buf)) != -1) {
			fileData.append(buf, 0, numRead);
		}
		reader.close();
		String sbmlModel = fileData.toString();

		modElement.setUserId(userId);
		modElement.setResourceId(resourceId);
		modElement.setInputFormat(SendModelDocument.SendModel.InputFormat.SBML);
		modElement.setSbml(sbmlModel);
		mod.setSendModel(modElement);
		stub.sendModel(mod);

		Trace trace = new Trace(dataFile);
		ExperimentalData data = ExperimentalData.Factory.newInstance();
		data.setDataFileInStringFormat(trace.toCOPASIString());
		ExperimentalData[] dataArray = new ExperimentalData[1];
		dataArray[0] = data;
		SendExperimentalDataDocument exp = SendExperimentalDataDocument.Factory.newInstance();
		SendExperimentalDataDocument.SendExperimentalData expElement = SendExperimentalDataDocument.SendExperimentalData.Factory
				.newInstance();
		expElement.setUserId(userId);
		expElement.setResourceId(resourceId);
		expElement.setExperimentalDataArray(dataArray);
		exp.setSendExperimentalData(expElement);
		stub.sendExperimentalData(exp);

		FitItemsAndMethod params = FitItemsAndMethod.Factory.newInstance();
		params.setMethod(getMethod(methodName));
		params.setItemToFitArray(getItemsToFit(sbmlModel));

		SetFitItemsAndMethodDocument fit = SetFitItemsAndMethodDocument.Factory.newInstance();
		SetFitItemsAndMethodDocument.SetFitItemsAndMethod fitElement = SetFitItemsAndMethodDocument.SetFitItemsAndMethod.Factory
				.newInstance();
		fitElement.setUserId(userId);
		fitElement.setResourceId(resourceId);
		fitElement.setFitItemsAndMethod(params);
		fit.setSetFitItemsAndMethod(fitElement);
		stub.setFitItemsAndMethod(fit);

		StartSimulatorDocument start = StartSimulatorDocument.Factory.newInstance();
		StartSimulatorDocument.StartSimulator startElement = StartSimulatorDocument.StartSimulator.Factory
				.newInstance();
		startElement.setUserId(userId);
		startElement.setResourceId(resourceId);
		start.setStartSimulator(startElement);
		stub.startSimulator(start);

		GetSimulatorStatusDocument status = GetSimulatorStatusDocument.Factory.newInstance();
		GetSimulatorStatusDocument.GetSimulatorStatus statusElement = GetSimulatorStatusDocument.GetSimulatorStatus.Factory
				.newInstance();
		statusElement.setUserId(userId);
		statusElement.setResourceId(resourceId);
		status.setGetSimulatorStatus(statusElement);

		while (stub.getSimulatorStatus(status).getGetSimulatorStatusResponse().getStatus().getCode() != StatusCode.COMPLETED) {
			System.out.println(stub.getSimulatorStatus(status).getGetSimulatorStatusResponse().getStatus().getCode());
		}
		GetResultDocument result = GetResultDocument.Factory.newInstance();
		GetResultDocument.GetResult resultElement = GetResultDocument.GetResult.Factory.newInstance();
		resultElement.setUserId(userId);
		resultElement.setResourceId(resourceId);
		result.setGetResult(resultElement);
		GetResultResponseDocument getResultResponse = stub.getResult(result);
		OutputResult outputResult = getResultResponse.getGetResultResponse().getOutputResult();

		System.out.println(outputResult.getResult());
	}

	private static Method getMethod(String methodName) {
		Method method = Method.Factory.newInstance();

		if (methodName != null) {
			if (methodName.equals("LEVENBERG_MARQUARDT")) {
				method.setMethodName(Method.MethodName.LEVENBERG_MARQUARDT);
				long ITERATION_LIMIT = 200;
				float TOLERANCE = 1e-5f;
				LevenbergMarquardt levenbergMarquardt = LevenbergMarquardt.Factory.newInstance();
				levenbergMarquardt.setIterationLimit(BigInteger.valueOf(ITERATION_LIMIT));
				levenbergMarquardt.setTolerance(TOLERANCE);
				method.setLevenbergMarquardt(levenbergMarquardt);
			}
			else if (methodName.equals("PARTICLE_SWARM")) {
				method.setMethodName(Method.MethodName.PARTICLE_SWARM);
				long ITERATION_LIMIT = 2000;
				long RANDOM_NUMBER_GENERATOR = 1;
				long SEED = 0;
				float STANDARD_DEVIATION = 1e-6f;
				long SWARM_SIZE = 50;
				ParticleSwarm particleSwarm = ParticleSwarm.Factory.newInstance();
				particleSwarm.setIterationLimit(BigInteger.valueOf(ITERATION_LIMIT));
				particleSwarm.setRandomNumberGenerator(BigInteger.valueOf(RANDOM_NUMBER_GENERATOR));
				particleSwarm.setSeed(BigInteger.valueOf(SEED));
				particleSwarm.setStandardDeviation(STANDARD_DEVIATION);
				particleSwarm.setSwarmSize(BigInteger.valueOf(SWARM_SIZE));
				method.setParticleSwarm(particleSwarm);
			}
			else if (methodName.equals("SIMULATED_ANNEALING")) {
				float COOLING_FACTOR = 0.85f;
				long RANDOM_NUMBER_GENERATOR = 1;
				long SEED = 0;
				long START_TEMPERATURE = 1;
				float TOLERANCE = 1e-6f;
				SimulatedAnnealing simulatedAnnealing = SimulatedAnnealing.Factory.newInstance();
				simulatedAnnealing.setCoolingFactor(COOLING_FACTOR);
				simulatedAnnealing.setRandomNumberGenerator(BigInteger.valueOf(RANDOM_NUMBER_GENERATOR));
				simulatedAnnealing.setSeed(BigInteger.valueOf(SEED));
				simulatedAnnealing.setStartTemperature(BigInteger.valueOf(START_TEMPERATURE));
				simulatedAnnealing.setTolerance(TOLERANCE);
				method.setSimulatedAnnealing(simulatedAnnealing);
			}
			else {
				method.setMethodName(Method.MethodName.GENETIC_ALGORITHM);
				long NUMBER_OF_GENERATIONS = 200;
				long POPULATION_SIZE = 20;
				long RANDOM_NUMBER_GENERATOR = 1;
				long SEED = 0;
				GeneticAlgorithm geneticAlgorithm = GeneticAlgorithm.Factory.newInstance();
				geneticAlgorithm.setNumberOfGenerations(BigInteger.valueOf(NUMBER_OF_GENERATIONS));
				geneticAlgorithm.setPopulationSize(BigInteger.valueOf(POPULATION_SIZE));
				geneticAlgorithm.setRandomNumberGenerator(BigInteger.valueOf(RANDOM_NUMBER_GENERATOR));
				geneticAlgorithm.setSeed(BigInteger.valueOf(SEED));
				method.setGeneticAlgorithm(geneticAlgorithm);
			}
		}

		return method;
	}

	private static ItemToFit[] getItemsToFit(String sbml) throws XMLStreamException {
		List<ItemToFit> items = new ArrayList<ItemToFit>();
		double lowerBound = 1e-8;
		double upperBound = 1e8;
		float startValue = 1f;
		SBMLDocument doc = SBMLReader.read(new ByteArrayInputStream(sbml.getBytes()));
		for (Reaction r : doc.getModel().getListOfReactions()) {
			if (r.isSetKineticLaw()) {
				for (LocalParameter p : r.getKineticLaw().getListOfLocalParameters()) {
					ItemToFit itf = ItemToFit.Factory.newInstance();
					itf.setLowerBound(lowerBound);
					itf.setUpperBound(upperBound);
					itf.setStartValue(startValue);
					itf.setModelObjectType(ItemToFit.ModelObjectType.REACTION);
					itf.setAffectedExperimentArray(new String[] { "all" });

					org.copasi.copasiws.services.parameterestimationws.types.Reaction copasiReaction = org.copasi.copasiws.services.parameterestimationws.types.Reaction.Factory
							.newInstance();
					copasiReaction.setModelId(r.getId());
					copasiReaction.setParameterID(p.getId());
					copasiReaction
							.setQuantityType(org.copasi.copasiws.services.parameterestimationws.types.Reaction.QuantityType.PARAMETER_VALUE);
					itf.setReaction(copasiReaction);
					items.add(itf);
				}
			}
		}
		return items.toArray(new ItemToFit[0]);
	}

	public static void simulate(String modelFile, long stepNumber, float stepSize, float duration, float startTime)
			throws IOException, ServiceFaultMessage {
		String serviceAddress = "http://www.comp-sys-bio.org/CopasiWS/services/TimeCourseService";
		TimeCourseServiceStub stub = new TimeCourseServiceStub(serviceAddress);

		DeterministicParameters params = DeterministicParameters.Factory.newInstance();
		params.setStepNumber(BigInteger.valueOf(stepNumber));
		params.setStepSize(stepSize);
		params.setDuration(duration);
		params.setOutputStartTime(startTime);
		params.setInputFormat(DeterministicParameters.InputFormat.SBML);
		params.setOutputFormat(DeterministicParameters.OutputFormat.SBRML);

		StringBuffer fileData = new StringBuffer(1000);
		BufferedReader reader = new BufferedReader(new FileReader(modelFile));
		char[] buf = new char[1024];
		int numRead = 0;

		while ((numRead = reader.read(buf)) != -1) {
			fileData.append(buf, 0, numRead);
		}
		reader.close();
		params.setSbml(fileData.toString());

		RunDeterministicSimulatorDocument reqDoc = RunDeterministicSimulatorDocument.Factory.newInstance();
		RunDeterministicSimulatorDocument.RunDeterministicSimulator reqElement = RunDeterministicSimulatorDocument.RunDeterministicSimulator.Factory
				.newInstance();
		reqElement.setParameters(params);
		reqDoc.setRunDeterministicSimulator(reqElement);

		RunDeterministicSimulatorResponseDocument resDoc = stub.runDeterministicSimulator(reqDoc);
		RunDeterministicSimulatorResponseDocument.RunDeterministicSimulatorResponse resElement = resDoc
				.getRunDeterministicSimulatorResponse();
		OutputResult outputResult = resElement.getOutputResult();
		String results = outputResult.getResult();

		File file = new File("sbrml.xml");
		FileWriter writer = new FileWriter(file);
		BufferedWriter buffWriter = new BufferedWriter(writer);
		buffWriter.write(results);
		buffWriter.close();
		writer.close();
	}

	public static class Trace {

		private String[] variables;

		private double[][] data;

		private double[] timePoints;

		public Trace(List<String> variables, List<Double> timePoints, List<List<Double>> data) {
			convertListstoArrays(variables, timePoints, data);
		}

		public Trace(String[] variables, double[] timePoints, double[][] data) {
			this.variables = variables;
			this.timePoints = timePoints;
			this.data = data;
		}

		public Trace(Scanner trace) {
			String line = trace.nextLine();
			String[] vars = line.split(",");
			List<String> variables = new ArrayList<String>();
			for (int i = 1; i < vars.length; i++) {
				variables.add(vars[i].substring(1, vars[i].length() - 1));
			}
			List<Double> timePoints = new ArrayList<Double>();
			List<List<Double>> data = new ArrayList<List<Double>>();
			while (trace.hasNextLine()) {
				line = trace.nextLine();
				String[] dataLine = line.split(",");
				timePoints.add(Double.parseDouble(dataLine[0]));
				List<Double> vals = new ArrayList<Double>();
				for (int i = 1; i < dataLine.length; i++) {
					vals.add(Double.parseDouble(dataLine[i]));
				}
				data.add(vals);
			}
			trace.close();
			convertListstoArrays(variables, timePoints, data);
		}

		public Trace(String trace) {
			this(new Scanner(trace));
		}

		public Trace(File trace) throws FileNotFoundException {
			this(new Scanner(trace));
		}

		public Trace(InputStream trace) {
			this(new Scanner(trace));
		}

		private void convertListstoArrays(List<String> variables, List<Double> timePoints, List<List<Double>> data) {
			this.variables = variables.toArray(new String[0]);
			this.timePoints = new double[timePoints.size()];
			for (int i = 0; i < timePoints.size(); i++) {
				this.timePoints[i] = timePoints.get(i);
			}
			this.data = new double[data.size()][data.get(0).size()];
			for (int i = 0; i < data.size(); i++) {
				for (int j = 0; j < data.get(0).size(); j++) {
					this.data[i][j] = data.get(i).get(j);
				}
			}
		}

		public String[] getVariables() {
			return variables;
		}

		public double[] getTimePoints() {
			return timePoints;
		}

		public double getValue(String variable, double timePoint) {
			for (int i = 0; i < timePoints.length; i++) {
				if (timePoints[i] == timePoint) {
					for (int j = 0; j < variables.length; j++) {
						if (variables[j].equals(variable)) {
							return data[j][i];
						}
					}
				}
			}
			return Double.NaN;
		}

		public double[] getValues(String variable) {
			int var = -1;
			for (int i = 0; i < variables.length; i++) {
				if (variables[i].equals(variable)) {
					var = i;
					break;
				}
			}
			double[] values = new double[data[var].length];
			for (int i = 0; i < data[var].length; i++) {
				values[i] = data[var][i];
			}
			return values;
		}

		public String toCOPASIString() {
			String trace = "# Time" + "\t";
			for (String variable : variables) {
				trace += variable + "\t";
			}
			for (int i = 0; i < timePoints.length; i++) {
				trace += "\n" + timePoints[i] + "\t";
				for (double value : data[i]) {
					trace += value + "\t";
				}
			}
			return trace;
		}

		public String toString() {
			String trace = "\"time\"";
			for (String variable : variables) {
				trace += ",\"" + variable + "\"";
			}
			for (int i = 0; i < timePoints.length; i++) {
				trace += "\n" + timePoints[i];
				for (double value : data[i]) {
					trace += "," + value;
				}
			}
			return trace;
		}
	}

}
