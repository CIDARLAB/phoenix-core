#Function for getting autoflouresnce values
getAutofluorescence <- function(negativeControlFlowSet, beadFlowFrame) {
	
	colnames(negativeControlFlowSet) <- gsub("-",".", colnames(negativeControlFlowSet))
	autofluorescence <- data.frame(matrix(ncol = length(colnames(negativeControlFlowSet)), nrow=1))
	colnames(autofluorescence) <- gsub("-",".", colnames(negativeControlFlowSet))	
	
	#Apply cell size filter -- we only want cells clustered in the middle of FSC and SSC range
	cellSizeFilter <- norm2Filter(x = c("SSC.A", "FSC.A"), scale.factor = 2, filterId = "cellSize")
	cellSizeFilter.results <- filter (negativeControlFlowSet, cellSizeFilter)			
	negativeControlFlowSet <- Subset(negativeControlFlowSet, cellSizeFilter.results)
	
	#Convert this to MEFL
	negativeControlFlowSet <- normalizeToBeads(negativeControlFlowSet, beadFlowFrame)
	
	#Apply a curve1 filter
	for (p in 1:length(colnames(negativeControlFlowSet))) {					
 		c1f <- curv1Filter(x=list(colnames(negativeControlFlowSet)[p]), bwFac=4)
 		fres <- filter(negativeControlFlowSet[,p], c1f)
 		s <- split(negativeControlFlowSet[,p], fres, population=list(keep=c("peak 1")))
 		autofluorescence[,p] <- fsApply(s$`keep`,each_col,mean)
 	}
	
	autofluorescence[1,] <- fsApply(negativeControlFlowSet,each_col,mean)
	autofluorescence <- as.matrix(autofluorescence)
	
	return(autofluorescence)
}