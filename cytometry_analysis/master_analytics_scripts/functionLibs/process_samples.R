#Function for processing meansMedia and standard deviations with filtering and compensating
process.samples <- function(experimentFlowSet, comp.mat, colorControlsFlowSet, autofluorescence, dataFiles) {		
	
	if (!is.null(comp.mat)) {
		compensatedFlowSet <- compensate(experimentFlowSet, comp.mat)
	} else {
		compensatedFlowSet <- experimentFlowSet
	}
				
	#Apply cell size filter -- we only want cells clustered in the middle of FSC and SSC range
	cellSizeFilter <- norm2Filter(x = c("SSC-A", "FSC-A"), scale.factor = 2, filterId = "cellSize")
	cellSizeFilter.results <- filter (compensatedFlowSet, cellSizeFilter)			
	compensatedFlowSet <- Subset(compensatedFlowSet, cellSizeFilter.results)					
				
	#Remove negative and fringe values
	for (q in 1:length(compensatedFlowSet)) {
				
		#Logicle Transform
		# lgcl <- estimateLogicle(compensatedFlowSet[[q]], colnames(compensatedFlowSet))
		# compensatedFlowSet[[q]] <- transform(compensatedFlowSet[[q]], lgcl)	
		
		compensatedFlowSet[[q]] <- nmRemove(compensatedFlowSet[[q]], colnames(compensatedFlowSet), neg=TRUE)
	}							
				
	#Processing for row and column names
	colnames(compensatedFlowSet) <- gsub("-",".", colnames(compensatedFlowSet))
	analyzedExpts <- data.frame(matrix(ncol = length(colnames(colorControlsFlowSet)), nrow=length(compensatedFlowSet)))
	colnames(analyzedExpts) <- gsub("-",".", colnames(colorControlsFlowSet))
	rownames(analyzedExpts) <- dataFiles
				
	#Apply a curve1 filter
	for (p in 1:length(colnames(compensatedFlowSet))) {
					
		c1f <- curv1Filter(x=list(colnames(compensatedFlowSet)[p]), bwFac=4)
		fres <- filter(compensatedFlowSet[,p], c1f)
		s <- split(compensatedFlowSet[,p], fres, population=list(keep=c("peak 1")))
		analyzedExpts[,p] <- fsApply(s$`keep`,each_col,mean)
	}
		
	#Subtract autofluorescence
	# analyzedExpts <- sweep(analyzedExpts,2,autofluorescence)
		
	analyzedExpts[is.na(analyzedExpts)] <- 0
	analyzedExptsMatrix <- data.matrix(analyzedExpts)
			
	return(analyzedExptsMatrix)
}