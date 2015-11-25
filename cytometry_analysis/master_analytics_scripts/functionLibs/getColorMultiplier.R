#Function for getting color multiplier values
getColorMultiplier <- function(colorControlsFlowSet, gatedBeadControlFlowFrame, autofluorescence, dataFiles) {
	
	colnames(colorControlsFlowSet) <- gsub("-",".", colnames(colorControlsFlowSet))
	colorMultiplier <- data.frame(matrix(ncol = length(colnames(colorControlsFlowSet)), nrow=length(colorControlsFlowSet)))
	colnames(colorMultiplier) <- gsub("-",".", colnames(negativeControlFlowSet))
	rownames(colorMultiplier) <- gsub("-",".", dataFiles)	
	
	#Apply cell size filter -- we only want cells clustered in the middle of FSC and SSC range
	cellSizeFilter <- norm2Filter(x = c("SSC.A", "FSC.A"), scale.factor = 2, filterId = "cellSize")
	cellSizeFilter.results <- filter (colorControlsFlowSet, cellSizeFilter)				
	colorControlsFlowSet <- Subset(colorControlsFlowSet, cellSizeFilter.results)
	
	#Convert this to MEFL
	colorControlsFlowSet <- normalizeToBeads(colorControlsFlowSet, gatedBeadControlFlowFrame)
	
	#Apply a curve1 filter
	for (p in 1:length(colnames(colorControlsFlowSet))) {					
 		# c1f <- curv1Filter(x=list(colnames(colorControlsFlowSet)[p]), bwFac=4)
 		# fres <- filter(colorControlsFlowSet[,p], c1f)
 		# s <- split(colorControlsFlowSet[,p], fres, population=list(keep=c("peak 1")))
 		# colorMultiplier[,p] <- fsApply(s$`keep`,each_col,median)
 		colorMultiplier[,p] <- fsApply(colorControlsFlowSet[,p],each_col,mean)
 	}
	
	#Subtract autofluorescence
	colorMultiplier <- sweep(colorMultiplier,2,autofluorescence)
	
	colorMultiplierVector <- data.frame(matrix(ncol = length(colnames(colorMultiplier))-2, nrow=1))
	colnames(colorMultiplierVector) <- colnames(colorMultiplier[3:length(colnames(colorMultiplier))])
	
	#Reduce matrix to single multiplier vector
	for (q in 1:length(colnames(colorMultiplier))) {
		for (r in 1:length(rownames(colorMultiplier))) {
			if (TRUE == grepl(colnames(colorMultiplier)[q], rownames(colorMultiplier)[r], ignore.case=TRUE)) {
				colorMultiplierVector[1,which(TRUE == grepl(colnames(colorMultiplier)[q], colnames(colorMultiplierVector), ignore.case=TRUE))] <- colorMultiplier[r,q]
			}	
		}		
	}

	#Create multiple
	FITC <- as.numeric(colorMultiplierVector[,which(TRUE == grepl("FITC", colnames(colorMultiplierVector), ignore.case=TRUE))])
	for (s in 1:length(colorMultiplierVector)) {
		colorMultiplierVector[s] <- FITC/colorMultiplierVector[s]
	}

	return(colorMultiplierVector)
}