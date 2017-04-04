#Function for getting autoflouresnce values
getAutofluorescence <- function(negativeControlFlowSet, beadFlowFrame, stain) {
	
	colnames(negativeControlFlowSet) <- gsub("-",".", colnames(negativeControlFlowSet))
	colnames(negativeControlFlowSet) <- gsub(" ","_", colnames(negativeControlFlowSet))
	autofluorescence <- data.frame(matrix(ncol = length(colnames(negativeControlFlowSet)), nrow=1))
	colnames(autofluorescence) <- gsub("-",".", colnames(negativeControlFlowSet))
	colnames(autofluorescence) <- gsub(" ","_", colnames(negativeControlFlowSet))	
	
	#Apply cell size filter -- we only want cells clustered in the middle of FSC and SSC range
	cellSizeFilter <- norm2Filter(x = c("SSC.A", "FSC.A"), scale.factor = 2, filterId = "cellSize")
	cellSizeFilter.results <- filter (negativeControlFlowSet, cellSizeFilter)			
	negativeControlFlowSet <- Subset(negativeControlFlowSet, cellSizeFilter.results)
	
	#Apply a singlet filter if staining
	if (stain == TRUE) {		
		
		gs <- GatingSet(negativeControlFlowSet)		
		singletPolygon <- matrix(c(20000,5000,215000,230000,5000,20000,250000,235000),ncol=2,nrow=4)
		
		#FSC singlet gate
		colnames(singletPolygon) <- c("FSC.H","FSC.A")
		pg <- polygonGate(filterId="singlets_FSC", .gate= singletPolygon)
		add(gs, pg, parent = "root")
		recompute(gs)
		# autoplot(gs, "singlets_FSC")
		singletFileterFSC.results <- filter(negativeControlFlowSet,pg)
		negativeControlFlowSet <- Subset(negativeControlFlowSet, singletFileterFSC.results)
		
		#SSC singlet gate
		colnames(singletPolygon) <- c("SSC.H","SSC.A")
		pg <- polygonGate(filterId="singlets_SSC", .gate= singletPolygon)
		add(gs, pg, parent = "root")
		recompute(gs)
		# autoplot(gs, "singlets_SSC")
		singletFileterSSC.results <- filter(negativeControlFlowSet,pg)
		compensatedFlowSet <- Subset(negativeControlFlowSet, singletFileterSSC.results)
	}
	
	#Convert this to MEFL
	negativeControlFlowSet <- normalizeToBeads(negativeControlFlowSet, beadFlowFrame)
	
	# #Apply a curve1 filter
	# for (p in 1:length(colnames(negativeControlFlowSet))) {					
 		# c1f <- curv1Filter(x=list(colnames(negativeControlFlowSet)[p]), bwFac=2)
 		# fres <- filter(negativeControlFlowSet[,p], c1f)
 		# s <- split(negativeControlFlowSet[,p], fres, population=list(keep=c("peak 1")))
 		# autofluorescence[1,p] <- fsApply(s$`keep`,each_col,mean)
 		# # autofluorescence[,p] <- 0
 	# }
	
	autofluorescence[1,] <- fsApply(negativeControlFlowSet,each_col,mean)
	autofluorescence <- as.matrix(autofluorescence)
	
	return(autofluorescence)
}