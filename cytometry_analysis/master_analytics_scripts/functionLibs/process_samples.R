#Function for processing meansMedia and standard deviations with filtering and compensating
process.samples <- function(experimentFlowSet, colorControlsFlowSet, beadFlowFrame, comp.mat, autofluorescence, dataFiles, colorMultiplierVector, stain, dataPath, plotGates) {		
	
	#Perform copmensation
	if (!is.null(comp.mat)) {
		compensatedFlowSet <- compensate(experimentFlowSet, comp.mat)
	} else {
		compensatedFlowSet <- experimentFlowSet
	}
				
	#Apply cell size filter -- we only want cells clustered in the middle of FSC and SSC range
	#If plotGates option passed, plot before and after this filter
	if (plotGates) {		
		flowSetName <- paste(sampleNames(compensatedFlowSet), collapse = "_")		
		flowSetName <- gsub(".fcs", "", flowSetName)
		dir.create(paste("plotted_gates", "/", flowSetName, sep=""))
		
		filename <- paste(flowSetName, "_FSC_SSC_beforeKmeans.png", sep = '')
		File <- as.character(paste("./plotted_gates/", flowSetName, "/", filename, sep = ''))
		dir.create(dirname(File), showWarnings = FALSE)   		
	   	png(File, width=960, height=960, res=120)
		p1 <- autoplot(compensatedFlowSet, "FSC-A", "SSC-A") +
		xlim(0, 50000) +
		ylim(0, 50000)
		print(p1)
	   	dev.off()
		
		cellSizeFilter <- norm2Filter(x = c("SSC-A", "FSC-A"), scale.factor = 2, filterId = "cellSize")
		cellSizeFilter.results <- filter (compensatedFlowSet, cellSizeFilter)			
		compensatedFlowSet <- Subset(compensatedFlowSet, cellSizeFilter.results)
		
		filename <- paste(flowSetName, "_FSC_SSC_afterKmeans.png", sep = '')
		File <- as.character(paste("./plotted_gates/", flowSetName, "/", filename, sep = ''))
		dir.create(dirname(File), showWarnings = FALSE)   		
	   	png(File, width=960, height=960, res=120)
		p2 <- autoplot(compensatedFlowSet, "FSC-A", "SSC-A") +
		xlim(0, 50000) +
		ylim(0, 50000)
		print(p2)
	   	dev.off()
		
	} else {
		cellSizeFilter <- norm2Filter(x = c("SSC-A", "FSC-A"), scale.factor = 2, filterId = "cellSize")
		cellSizeFilter.results <- filter (compensatedFlowSet, cellSizeFilter)			
		compensatedFlowSet <- Subset(compensatedFlowSet, cellSizeFilter.results)
	}													
				
	#Extra gate specific to this experiment due to laser problems
	gs <- GatingSet(compensatedFlowSet)		
	polygonCellsOnly <- matrix(c(10^2,10^2,10^4,10^4,10^2.5,10^4.5,10^2.5,10^4.5),ncol=2,nrow=4)
		
	#FSC singlet gate
	colnames(polygonCellsOnly) <- c("SSC-A","FSC-A")
	cog <- polygonGate(filterId="cellsOnly", .gate= polygonCellsOnly)
	add(gs, cog, parent = "root")
		
	# if (plotGates) {
		# filename <- paste(flowSetName, "_cells_only.png", sep = '')
		# #File <- as.character(paste("./plotted_gates/", filename, sep = ''))
		# File <- as.character(paste("./plotted_gates/", flowSetName, "/", filename, sep = ''))
		# dir.create(dirname(File), showWarnings = FALSE)   		
	 	# png(File, width=960, height=960, res=120)
	   		
		# recompute(gs)
		# p <- autoplot(gs, "singlets_FSC") +
		# xlim(0, 250000) +
		# ylim(0, 250000)
		# print(p)
	   	# dev.off()
	# }
		
	cellsOnly.results <- filter(compensatedFlowSet,cog)
	compensatedFlowSet <- Subset(compensatedFlowSet, cellsOnly.results)		
				
	#Apply a singlet filter if staining
	# if (stain == TRUE) {		
		
		# gs <- GatingSet(compensatedFlowSet)		
		# polygonFSC <- matrix(c(20000,5000,215000,230000,5000,20000,250000,235000),ncol=2,nrow=4)
		
		# #FSC singlet gate
		# colnames(polygonFSC) <- c("FSC-H","FSC-A")
		# pg <- polygonGate(filterId="singlets_FSC", .gate= polygonFSC)
		# add(gs, pg, parent = "root")
		
		# if (plotGates) {
			# filename <- paste(flowSetName, "_singlets_FSC.png", sep = '')
			# #File <- as.character(paste("./plotted_gates/", filename, sep = ''))
			# File <- as.character(paste("./plotted_gates/", flowSetName, "/", filename, sep = ''))
			# dir.create(dirname(File), showWarnings = FALSE)   		
	   		# png(File, width=960, height=960, res=120)
	   		
			# recompute(gs)
			# p <- autoplot(gs, "singlets_FSC") +
			# xlim(0, 250000) +
			# ylim(0, 250000)
			# print(p)
	   		# dev.off()
		# }
		
		# singletFileterFSC.results <- filter(compensatedFlowSet,pg)
		# compensatedFlowSet <- Subset(compensatedFlowSet, singletFileterFSC.results)
		
		# #SSC singlet gate
		# polygonSSC <- matrix(c(1000,500,4500,5000,500,1000,5000,4500),ncol=2,nrow=4)
		# colnames(polygonSSC) <- c("SSC-H","SSC-A")
		# pg <- polygonGate(filterId="singlets_SSC", .gate= polygonSSC)
		# add(gs, pg, parent = "root")
		
		# if (plotGates) {
			# filename <- paste(flowSetName, "_singlets_SSC.png", sep = '')
			# # File <- as.character(paste("./plotted_gates/", filename, sep = ''))
			# File <- as.character(paste("./plotted_gates/", flowSetName, "/", filename, sep = ''))
			# dir.create(dirname(File), showWarnings = FALSE)   		
	   		# png(File, width=960, height=960, res=120)
			
			# recompute(gs)
			# p <- autoplot(gs, "singlets_SSC") 
			# # xlim(0, 5000) +
			# # ylim(0, 5000)
			# print(p)
	   		# dev.off()
		# }
		
		# singletFileterSSC.results <- filter(compensatedFlowSet,pg)
		# compensatedFlowSet <- Subset(compensatedFlowSet, singletFileterSSC.results)
	# }			
				
	#Processing for row and column names
	colnames(compensatedFlowSet) <- gsub("-",".", colnames(compensatedFlowSet))
	colnames(compensatedFlowSet) <- gsub(" ","_", colnames(compensatedFlowSet))
	analyzedExpts <- data.frame(matrix(ncol = length(colnames(experimentFlowSet)), nrow=length(compensatedFlowSet)))
	colnames(analyzedExpts) <- gsub("-",".", colnames(experimentFlowSet))
	colnames(analyzedExpts) <- gsub(" ","_", colnames(experimentFlowSet))
	rownames(analyzedExpts) <- dataFiles
				
	#Normalize the flow set to 8-peak bead controls
	compensatedFlowSet <- normalizeToBeads(compensatedFlowSet, beadFlowFrame)
	
	#Remove negative and fringe values
	# for (q in 1:length(compensatedFlowSet)) {
		# compensatedFlowSet[[q]] <- nmRemove(compensatedFlowSet[[q]], colnames(compensatedFlowSet), neg=TRUE)				
		
		# #Logicle Transform
		# # lgcl <- estimateLogicle(compensatedFlowSet[[q]], colnames(compensatedFlowSet))
		# # compensatedFlowSet[[q]] <- transform(compensatedFlowSet[[q]], lgcl)					
	# }	
				
	#Apply a curve1 filter
	# for (p in 1:length(colnames(compensatedFlowSet))) {
	
		# out <- 0
		# tryCatch(
	        # {
	            # # message("Attempting the curve1 filter")
	            # c1f <- curv1Filter(x=list(colnames(compensatedFlowSet)[p]), bwFac=2)
				# fres <- filter(compensatedFlowSet[,p], c1f)
				# s <- split(compensatedFlowSet[,p], fres, population=list(keep=c("peak 1")))
				# out <- fsApply(s$`keep`,each_col,mean)
	        # },
	        # error=function(cond) {
	            # # message("Here's the original error message:")
	            # # message(cond)
	            # out <- fsApply(compensatedFlowSet[,p],each_col,mean)
	        # },
	        # warning=function(cond) {
	            # # message("Here's the original warning message:")
	            # # message(cond)
	            # out <- fsApply(compensatedFlowSet[,p],each_col,mean)
	        # },
	        # finally={
	        # }
    	# )

		# # out <- fsApply(compensatedFlowSet[,p],each_col,mean)
		# analyzedExpts[,p] <- out
	# }
	
	for (q in 1:length(compensatedFlowSet)) {
		analyzedExpts[q,] <- fsApply(compensatedFlowSet[q],each_col,mean)
	}
		
	#Subtract autofluorescence
	analyzedExpts <- sweep(analyzedExpts,2,autofluorescence)
		
	#Color multiplication applied
	for (r in 1:length(colnames(analyzedExpts))) {
		m <- as.numeric(colorMultiplierVector[,which(TRUE == grepl(colnames(analyzedExpts)[r], colnames(colorMultiplierVector), ignore.case=TRUE))])
		analyzedExpts[,r] <- m * analyzedExpts[,r]
	}	
		
	analyzedExpts[is.na(analyzedExpts)] <- 0
	analyzedExptsMatrix <- data.matrix(analyzedExpts)
			
	# print(analyzedExpts)		
			
	return(analyzedExptsMatrix)
}
