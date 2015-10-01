#Function for calculating functional diversity
functionalDiversity <- function(multiplexMeansDataSet) {
	
	fdRaoQ <- data.frame(matrix(ncol = length(colnames(multiplexMeansDataSet)), nrow=0))
	colnames(fdRaoQ) <- gsub("-",".", colnames(multiplexMeansDataSet))
	
	if (nrow(multiplexMeansDataSet) > 1)  {
		for (s in 1:length(colnames(multiplexMeansDataSet))) {
		
			channelFDdf <- as.data.frame(multiplexMeansDataSet[,s])
			colnames(channelFDdf) <- colnames(multiplexMeansDataSet)[s]
			fd <- dbFD(channelFDdf)
			
			fdRaoQ[1,s] <- fd$FRic
		}
	} else {
		for (s in 1:length(colnames(multiplexMeansDataSet))) {
			fdRaoQ[1,s] <- 0
		}
	}	
	return(fdRaoQ)
}