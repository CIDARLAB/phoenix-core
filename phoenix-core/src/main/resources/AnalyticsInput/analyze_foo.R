#Import libraries
library(flowCore)
library(flowBeads)
library(flowStats)
library(flowDensity)
library(data.table)
library(matrixStats)
library(ggplot2)
library(stringr)
library(FD)

#Function for processing meansMedia and standard deviations with filtering and compensating
process.samples <- function(experimentFlowSet, comp.mat, colorControlsFlowSet, dataFiles) {
	
	if (!is.null(comp.mat)) {
		compensatedData <- compensate(experimentFlowSet, comp.mat)
	} else {
		compensatedData <- experimentFlowSet
	}
				
	#Apply cell size filter -- we only want cells clustered in the middle of FSC and SSC range
	cellSizeFilter <- norm2Filter(x = c("SSC-A", "FSC-A"), scale.factor = 2, filterId = "cellSize")
	cellSizeFilter.results <- filter (compensatedData, cellSizeFilter)			
	compensatedData <- Subset(compensatedData, cellSizeFilter.results)					
				
	#Remove negative and fringe values
	for (q in 1:length(compensatedData)) {
		nmRemove(compensatedData[[q]], colnames(compensatedData), neg=TRUE)
	}
				
	#Processing for row and column names
	colnames(compensatedData) <- gsub("-",".", colnames(compensatedData))
	analyzedExpts <- data.frame(matrix(ncol = length(colnames(colorControlsFlowSet)), nrow=length(compensatedData)))
	colnames(analyzedExpts) <- gsub("-",".", colnames(colorControlsFlowSet))
	rownames(analyzedExpts) <- dataFiles
				
	for (p in 1:length(colnames(compensatedData))) {
					
		c1f <- curv1Filter(x=list(colnames(compensatedData)[p]), bwFac=4)
		fres <- filter(compensatedData[,p], c1f)
		s <- split(compensatedData[,p], fres, population=list(keep=c("peak 1")))
		analyzedExpts[,p] <- fsApply(s$`keep`,each_col,mean)
	}
		
	analyzedExpts[is.na(analyzedExpts)] <- 0
	analyzedExptsMediaEval <- data.matrix(analyzedExpts)
			
	return(analyzedExptsMediaEval)
}

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

#Import key file
key <- read.csv("foo.csv", header = TRUE)
key [is.na(key)] <- ""

#Find bead controls, apply bead normalization
beadsControlRow <- key[which(TRUE == grepl("beads", key$CONTROL, ignore.case=TRUE)),]
beadsControlFlowSet <- read.flowSet(path = "data", as.character(beadsControlRow$FILENAME), phenoData=list(Filename="$FIL"))

#Find color controls, calculate overlap, apply compensation
allControlRows <- key[which(!(key$CONTROL =="")),]
colorControlRows <- allControlRows[-which(TRUE == grepl("beads", allControlRows$CONTROL, ignore.case=TRUE)),]
negativeControlRows <- key[which(TRUE == grepl("negative", key$CONTROL, ignore.case=TRUE)),]
colorControlsFlowSet <- read.flowSet(path = "data", as.character(colorControlRows$FILENAME), phenoData=list(Filename="$FIL"))
negativeControlFlowSet <- read.flowSet(path = "data", as.character(negativeControlRows$FILENAME), phenoData=list(Filename="$FIL"))

#Filter out for only relevant color columns based on controls
columnIndexes <- match(colorControlRows$CONTROL, colnames(colorControlsFlowSet))
FSCSSCIndexes <- match(c("FSC-A", "SSC-A"), colnames(colorControlsFlowSet)) 
columnIndexes <- c(FSCSSCIndexes[!is.na(FSCSSCIndexes)], columnIndexes[!is.na(columnIndexes)])
colorControlsFlowSet <- colorControlsFlowSet[,columnIndexes]
negativeControlFlowSet <- negativeControlFlowSet[,columnIndexes]

#Determine the spillover matrix
comp.mat <- NULL
if (length(columnIndexes) > 3) {
	comp.mat <- spillover(x=colorControlsFlowSet,unstained=sampleNames(negativeControlFlowSet),fsc="FSC-A",ssc="SSC-A",method="mean", stain_match = c("regexpr"))
}

#Group files by part name
#Determine unique parts
partsRows <- key[which((key$CONTROL =="")),]
uniquePartNames <- unique(unlist(partsRows$PART))

allPartTimeMediaDuplicateRows <- partsRows[duplicated(interaction(partsRows$MEDIA, partsRows$TIME, partsRows$PART)),]
allPartTimeMediaUniqueRows <- partsRows[!duplicated(interaction(partsRows$MEDIA, partsRows$TIME, partsRows$PART)),]

#Multiplex key
multiplexRows <- key[which(!(key$MULTIPLEX =="")),]
multiplexVals <- unique(multiplexRows$MULTIPLEX)

#Make multiplex dataframes
if (length(multiplexVals) > 1) {
	for (m in 1:length(multiplexVals)) {		
		MmeansMedia <- data.frame(matrix(ncol = length(colnames(colorControlsFlowSet)), nrow=0))
		colnames(MmeansMedia) <- gsub("-",".", colnames(colorControlsFlowSet))
		assign(paste("MMeans",multiplexVals[m],sep=""), MmeansMedia)
		Mstds <- data.frame(matrix(ncol = length(colnames(colorControlsFlowSet)), nrow=0))
		colnames(Mstds) <- gsub("-",".", colnames(colorControlsFlowSet))
		assign(paste("MStds",multiplexVals[m],sep=""), Mstds)
	}
}

#Initialize data structures for meansMedia and standardDevsMedia
meansOneMedia <- data.frame(matrix(ncol = length(colnames(colorControlsFlowSet)), nrow=0))
colnames(meansOneMedia) <- gsub("-",".", colnames(colorControlsFlowSet))
standardDevsOneMedia <- data.frame(matrix(ncol = length(colnames(colorControlsFlowSet)), nrow=0))
colnames(standardDevsOneMedia) <- gsub("-",".", colnames(colorControlsFlowSet))
oneMediaParts <- c()

#Initialize output file
output <- data.frame(matrix(ncol = length(colnames(colorControlsFlowSet)) + 1, nrow=0))
colnames(output) <- c("PART", "MEDIA", "TIME", gsub("-",".", colnames(colorControlsFlowSet))[3:length(colnames(colorControlsFlowSet))])

#Loop through all unique parts to make plots
for (i in 1:length(uniquePartNames)) {

	part <- as.character(uniquePartNames[i])
	uniqueTimeMediaRows <- allPartTimeMediaUniqueRows[which(TRUE == grepl(part, allPartTimeMediaUniqueRows$PART)),]
		
	#Find unique media types, ignoring numeric values in any entry
	mediaTypes <- sub("\\(?[0-9,.]+\\)?","", allPartTimeMediaUniqueRows$MEDIA)
	uniqueMediaTypes <- unique(sub("^\\s+|\\s+$","",mediaTypes))		

	#Loop through each unique media type, ignoring numeric values in any entry
	for (j in 1:length(uniqueMediaTypes)) {

		media <- c()
		
		#Initialize data structures for meansMedia and standardDevsMedia
		meansMedia <- data.frame(matrix(ncol = length(colnames(colorControlsFlowSet)), nrow=0))
		colnames(meansMedia) <- gsub("-",".", colnames(colorControlsFlowSet))
		standardDevsMedia <- data.frame(matrix(ncol = length(colnames(colorControlsFlowSet)), nrow=0))
		colnames(standardDevsMedia) <- gsub("-",".", colnames(colorControlsFlowSet))
		
		#Get this media type with string formatting substitutions
		mediaType <- as.character(uniqueMediaTypes[j])
		mediaType <- gsub("\\(","\\\\(",mediaType)
		mediaType <- gsub("\\)","\\\\)",mediaType)				
		
		#Get unique media rows with this media type				
		# blankMediaTimeRow <- uniqueTimeMediaRows[which(allPartTimeMediaUniqueRows$MEDIA == ""),]
		uniqueMediaRows <- uniqueTimeMediaRows[which(TRUE == grepl(mediaType, uniqueTimeMediaRows$MEDIA)),]
		# uniqueMediaRows <- rbind(blankMediaTimeRow,uniqueMediaRows)			
		
		uniqueMediaTypeConcentrations <- as.character(unique(uniqueMediaRows$MEDIA))
	
		#Loop through all unique media concentrations for this part
		for (k in 1:length(uniqueMediaTypeConcentrations)) {				
			
			time <- c()
			media <- c(media, as.character(uniqueMediaTypeConcentrations[k]))
			
			#Initialize data structures for meansMediaTime and standardDevsMediaTime
			meansMediaTime <- data.frame(matrix(ncol = length(colnames(colorControlsFlowSet)), nrow=0))
			colnames(meansMediaTime) <- gsub("-",".", colnames(colorControlsFlowSet))
			standardDevsMediaTime <- data.frame(matrix(ncol = length(colnames(colorControlsFlowSet)), nrow=0))
			colnames(standardDevsMediaTime) <- gsub("-",".", colnames(colorControlsFlowSet))
			
			#Get this media concentration with string formatting substitutions
			uniqueMediaTypeConcentration <- uniqueMediaTypeConcentrations[k]
			uniqueMediaTypeConcentration <- gsub("\\(","\\\\(",uniqueMediaTypeConcentration)
			uniqueMediaTypeConcentration <- gsub("\\)","\\\\)",uniqueMediaTypeConcentration)
			uniqueMediaTypeConcentration <- paste("^", uniqueMediaTypeConcentration, "$", sep = '')
			
			#Get all unique times per one media condition
			uniqueMediaRowsAllTimes <- uniqueMediaRows[which(TRUE == grepl(uniqueMediaTypeConcentration, uniqueMediaRows$MEDIA)),]				
			uniqueMediaTimes <- as.character(unique(uniqueMediaRowsAllTimes$TIME))
			
			#Hold replicates across all times per media concentration for media graphs
			filesMediaConcentration <- c()
			
			#Loop through all times for this unique media and part combination table
			for (t in 1:length(uniqueMediaTimes)) {
				
				uniqueMediaTime <- uniqueMediaTimes[t]
				uniqueMediaTime <- paste("^", uniqueMediaTime, "$", sep = '')
				uniqueMediaTimeRow <- uniqueMediaRowsAllTimes[which(TRUE == grepl(uniqueMediaTime, uniqueMediaRowsAllTimes$TIME)),]
				
				time <- c(time, as.character(uniqueMediaTimes[t]))
				file <- as.character(uniqueMediaTimeRow$FILENAME)
				
				#Search duplicate media matrix for duplicate files
				partMediaKey <- c("MEDIA", "PART", "TIME")
				uniqueRowsThisExactMedia <- data.table(uniqueMediaTimeRow, key=partMediaKey)
				duplicateRowsThisExactMedia <- data.table(allPartTimeMediaDuplicateRows, key=partMediaKey)
				replicateFiles <- as.character(merge(uniqueRowsThisExactMedia, duplicateRowsThisExactMedia)$FILENAME.y)
				
				#Summarize files for times in this media condition in addition to files for all times in this media condition
				files <- c(file, replicateFiles)
				filesMediaConcentration <- c(filesMediaConcentration, files)				
				
				#Only process this data if there is more than one media time
				if (length(uniqueMediaTimes) > 1) {
				
					#Analyze this flowset
					experimentFlowSet <- read.flowSet(path = "data", files, phenoData=list(Filename="$FIL"))
					experimentFlowSet <- experimentFlowSet[,columnIndexes]				
									
					#Process experiments for this flowSet
					analyzedExptsMediaTimeEval <- process.samples(experimentFlowSet, comp.mat, colorControlsFlowSet, files)
					meansMediaTime[t,] <- colMeans(analyzedExptsMediaTimeEval)
					standardDevsMediaTime[t,] <- colSds(analyzedExptsMediaTimeEval)
				}	
			}
			
			#Add data to output file
			for (w in 1:length(uniqueMediaTimes)) {
				outputDataRow <- as.data.frame(rbind(c(part, uniqueMediaTypeConcentrations[k], uniqueMediaTimes[w], as.character(meansMediaTime[w,3:length(meansMediaTime)]))))
				colnames(outputDataRow) <- colnames(output)
				output <- rbind(output, outputDataRow)
			}
			
			#Make a time v. media plot if more than one media time
			if (length(uniqueMediaTimes) > 1) {
				
				### PLOTTING OF PART MEDIA CONDITIONS ###
				#Get rid of FSC and SSC
				
				#Edge case of only one color aside from FSC and SSC
				storedColName <- colnames(meansMediaTime)[3]
				
				meansMediaTime <- cbind(meansMediaTime[,3:length(meansMediaTime)])
				meansMediaTime[is.na(meansMediaTime)] <- 0				
				standardDevsMediaTime <- cbind(standardDevsMediaTime[,3:length(standardDevsMediaTime)])
				standardDevsMediaTime[is.na(standardDevsMediaTime)] <- 0
				
				if (length(colnames(meansMediaTime)) == 0) {
					colnames(meansMediaTime) <- storedColName
					colnames(standardDevsMediaTime) <- storedColName
				}			
				
				#Make plots				
	    		colnames(meansMediaTime) <- paste("MEAN", colnames(meansMediaTime), sep = "_")
	    		colnames(standardDevsMediaTime) <- paste("STD", colnames(standardDevsMediaTime), sep = "_")
	    		xaxis <- as.numeric(str_extract_all(sub("","0",time),"\\(?[0-9,.]+\\)?"))
	    		xaxis <- cbind(xaxis)
	    		
	    		plotMat <- cbind(meansMediaTime, standardDevsMediaTime)
	    		plotMat <- cbind(xaxis, plotMat)
	    		plotMat <- as.data.frame(plotMat)
	    		
	    		#Plot each color
	    		for (u in 1:length(colnames(meansMediaTime))) {
	    			title <- colnames(meansMediaTime)[u]
	    			yaxis <- cbind(meansMediaTime[,colnames(meansMediaTime)[u]])
	    			yaxis <- as.numeric(yaxis)
	    			error <- standardDevsMediaTime[,colnames(standardDevsMediaTime)[u]]
	    			error <- as.numeric(error)
	    			
	    			ymin <- yaxis-error
	    			ymax <- yaxis+error
	    			limits <- aes(ymin=yaxis-error, ymax=yaxis+error)
	
					name <- as.character(paste(uniquePartNames[i],"_",as.character(uniqueMediaTypeConcentrations[k]),"_",colnames(meansMediaTime)[u],".png"))
					name <- str_replace_all(name, fixed(" "), "")
					name <- sub("/","",name)
					png(name, width=960, height=960, res=120)
	    			pt <- ggplot(data = plotMat, aes(x = xaxis, y = yaxis)) +
	    			geom_errorbar(aes(ymin=yaxis-error, ymax=yaxis+error)) +
					geom_line() +
	    			geom_point(size = 4, shape=21, fill="white") +
	    			ylim(0,max(ymax)) +
	    			# #theme_bw() +
	    			ggtitle(as.character(paste(uniquePartNames[i],as.character(uniqueMediaTypeConcentrations[k]),colnames(meansMediaTime)[u], sep = "_"))) +    	
	    			xlab(as.character("TIME")) +
					ylab(as.character(paste(colnames(meansMediaTime)[u]," (RFU)")))
	    			print(pt)
	    			dev.off()
	    		}
			}			
	
			#Analyze this flowset for all times of this media condition
			experimentFlowSetMedia <- read.flowSet(path = "data", filesMediaConcentration, phenoData=list(Filename="$FIL"))
			experimentFlowSetMedia <- experimentFlowSetMedia[,columnIndexes]				
							
			#Process experiments for this flowSet
			analyzedExptsMediaEval <- process.samples(experimentFlowSetMedia, comp.mat, colorControlsFlowSet, filesMediaConcentration)		
			meansMedia[k,] <- colMeans(analyzedExptsMediaEval)
			standardDevsMedia[k,] <- colSds(analyzedExptsMediaEval)
			
			#Edge case where there is only one type of media for this part - throw it into a separate data frame for a bar graph
			if (length(uniqueMediaTypeConcentrations) == 1) {				
				oneMediaParts <- c(oneMediaParts, paste(part, uniqueMediaTypeConcentrations[k], sep = "_"))				
				meansOneMedia <- rbind(meansOneMedia, meansMedia[k,])
				standardDevsOneMedia <- rbind(standardDevsOneMedia, standardDevsMedia[k,])				
			}				
		}
		
		#Only do this loop if a media type is entered
		if (length(uniqueMediaTypeConcentrations) > 1) {
		
			### PLOTTING OF PART MEDIA CONDITIONS ###
			#Get rid of FSC and SSC
			meansMedia <- meansMedia[,3:length(meansMedia)]
			meansMedia[is.na(meansMedia)] <- 0
			standardDevsMedia <- standardDevsMedia[,3:length(standardDevsMedia)]
			standardDevsMedia[is.na(standardDevsMedia)] <- 0
			
			#Make plots
    		colnames(meansMedia) <- paste("MEAN", colnames(meansMedia), sep = "_")
    		colnames(standardDevsMedia) <- paste("STD", colnames(standardDevsMedia), sep = "_")
    		xaxis <- as.numeric(str_extract_all(sub("","0",media),"\\(?[0-9,.]+\\)?"))
    		xaxis <- cbind(xaxis)
    		
    		plotMat <- cbind(meansMedia, standardDevsMedia)
    		plotMat <- cbind(xaxis, plotMat)
    		
    		#Plot each color
    		for (l in 1:length(colnames(meansMedia))) {
    			title <- colnames(meansMedia)[l]
    			yaxis <- cbind(meansMedia[,colnames(meansMedia)[l]])
    			yaxis <- as.numeric(yaxis)
    			error <- standardDevsMedia[,colnames(standardDevsMedia)[l]]
    			error <- as.numeric(error)
    			
    			ymin <- yaxis-error
    			ymax <- yaxis+error
    			limits <- aes(ymin=yaxis-error, ymax=yaxis+error)

				name <- as.character(paste(uniquePartNames[i],"_",as.character(uniqueMediaTypes[j]),"_",colnames(meansMedia)[l],".png"))
				name <- str_replace_all(name, fixed(" "), "")
				name <- sub("/","",name)
				png(name, width=960, height=960, res=120)
    			p <- ggplot(plotMat, aes(x = xaxis, y = yaxis)) +
    			geom_errorbar(aes(ymin=yaxis-error, ymax=yaxis+error)) +
				geom_line() +
    			geom_point(size = 4, shape=21, fill="white") +
    			ylim(0,max(ymax)) +
    			#theme_bw() +
    			ggtitle(as.character(part)) +    			
    			xlab(as.character(uniqueMediaTypes[j])) +
    			ylab(as.character(paste(colnames(meansMedia)[l]," (RFU)")))
    			print(p)
    			dev.off()
    		}    	
    	
    	#If there is no media curve, we still want to take averages of duplicateTimeMediaRows
		} else {
			
			if ("MULTIPLEX" %in% colnames(key)) {
				if (!(uniqueMediaRows$MULTIPLEX == "")) {
					x <- rbind(get(paste("MMeans",uniqueMediaRows$MULTIPLEX,sep="")),meansMedia)
					assign(paste("MMeans",uniqueMediaRows$MULTIPLEX,sep=""), x)
					y <- rbind(get(paste("MStds",uniqueMediaRows$MULTIPLEX,sep="")),standardDevsMedia)
					assign(paste("MStds",uniqueMediaRows$MULTIPLEX,sep=""), y)
				}
			}
		}
	}
}

if (length(oneMediaParts) > 1) {
	
	### PLOTTING OF PART MEDIA CONDITIONS ###
	#Get rid of FSC and SSC
	#Edge case of only one color aside from FSC and SSC
	storedColName <- colnames(meansOneMedia)[3]
	
	meansOneMedia <- cbind(meansOneMedia[,3:length(meansOneMedia)])
	meansOneMedia[is.na(meansOneMedia)] <- 0				
	standardDevsOneMedia <- cbind(standardDevsOneMedia[,3:length(standardDevsOneMedia)])
	standardDevsOneMedia[is.na(standardDevsOneMedia)] <- 0
	
	if (length(colnames(meansOneMedia)) == 0) {
		colnames(meansOneMedia) <- storedColName
		colnames(standardDevsOneMedia) <- storedColName
	}			
	
	#Make plots
	colnames(meansOneMedia) <- paste("MEAN", colnames(meansOneMedia), sep = "_")
   	colnames(standardDevsOneMedia) <- paste("STD", colnames(standardDevsOneMedia), sep = "_")
    xaxis <- oneMediaParts
    xaxis <- cbind(xaxis)
    		
    plotMat <- cbind(meansOneMedia, standardDevsOneMedia)
    plotMat <- cbind(xaxis, plotMat)
	plotMat <- as.data.frame(plotMat)
 		
    #Plot each color
    for (v in 1:length(colnames(meansOneMedia))) {
		title <- colnames(meansOneMedia)[v]
   		yaxis <- cbind(meansOneMedia[,colnames(meansOneMedia)[v]])
   		yaxis <- as.numeric(yaxis)
   		error <- standardDevsOneMedia[,colnames(standardDevsOneMedia)[v]]
   		error <- as.numeric(error)
    			
   		ymin <- yaxis-error
   		ymax <- yaxis+error
   		limits <- aes(ymin=yaxis-error, ymax=yaxis+error)

		name <- as.character(paste("Mean_Population_Averages_Parts_One_Medium",".png"))
		name <- str_replace_all(name, fixed(" "), "")
		name <- sub("/","",name)
		png(name, width=960, height=960, res=120)
   		
   		po <- ggplot(plotMat, aes(x = xaxis, y = yaxis)) +
   		geom_bar(colour="black", fill="#DD8888", width=.8, stat="identity") + 
    	guides(fill=FALSE) +
   		geom_errorbar(aes(ymin=yaxis-error, ymax=yaxis+error)) +
  		ggtitle(as.character("Mean Population Averages")) +    			
   		xlab(as.character("PARTS")) +
   		ylab(as.character(paste(colnames(meansOneMedia)[v]," (RFU)"))) +
   		theme(axis.title.x = element_blank()) +
   		theme(axis.text.x  = element_text(angle=45, vjust=0.5, size=8))
   		print(po)
   		dev.off()
  	}
}

#Loop through multiplex values
if (length(multiplexVals) > 1) {
	multiplexDataFrame <- data.frame(matrix(ncol = length(colnames(colorControlsFlowSet)) + 2, nrow=0))
	colnames(multiplexDataFrame) <- c("TYPE", "MULTIPLEX", gsub("-",".", colnames(colorControlsFlowSet)))
	
	#Get standard deviation of averages
	### NEW -> Get the Biodiversity of the averages ###
	for (n in 1:length(multiplexVals)) {
		multiplexMeansDataSet <- as.matrix(get(paste("MMeans",multiplexVals[n],sep="")))
		#finalMultiplexVals <- colSds(multiplexMeansDataSet)
		finalMultiplexVals <- functionalDiversity(multiplexMeansDataSet) 
		finalMultiplexVals[is.na(finalMultiplexVals)] <- 0
		multiplexDataFrame[n,] <- c(sub("\\(?[0-9,.]+\\)?","", multiplexVals[n]),as.numeric(str_extract_all(sub("","0",multiplexVals),"\\(?[0-9,.]+\\)?")[n]), finalMultiplexVals)
	}
	
	multiplexDataFrame <- multiplexDataFrame[,c(1:2,5:length(multiplexDataFrame))]
	
	#Plot solutions
    for (o in 3:length(colnames(multiplexDataFrame))) {
    	title <- colnames(multiplexDataFrame)[o]
    	yaxis <- cbind(multiplexDataFrame[,colnames(multiplexDataFrame)[o]])
		yaxis <- as.numeric(yaxis)

		name <- as.character(paste(colnames(multiplexDataFrame)[o],".png"))
		name <- str_replace_all(name, fixed(" "), "")
		png(name, width=960, height=960, res=120)
		PartType <- multiplexDataFrame$TYPE
		
    	p <- ggplot(multiplexDataFrame, aes(x = multiplexDataFrame$MULTIPLEX, y = yaxis, group = PartType, colour = PartType)) +
    	geom_line() +
    	geom_point(size = 4, shape=21, fill="white") +
    	theme_bw() +
    	ylim(0,max(yaxis)) +
    	ggtitle("Functional Richness of Triplicate Population Averages") +    			
    	xlab("Number of Parts Multiplexed") +
    	ylab(paste(as.character(colnames(multiplexDataFrame)[o])," (RFU)"))
    	print(p)
    	dev.off()
    }
}

#Write output into an exported csv file for Phoenix
filename <- "foo.csv", header = TRUE)
write.csv(file=filename, x=output, row.names=FALSE)
