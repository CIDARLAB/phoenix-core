#Import libraries
library(flowCore)
library(flowBeads)
library(flowStats)
library(flowDensity)
library(data.table)
library(matrixStats)
library(ggplot2)
library(stringr)

#Function for processing means and standard deviations with filtering and compensating
process.samples <- function(expts, comp.mat, colorControls) {
	
	# compensatedData <- compensate(expts, comp.mat)
	compensatedData <- expts
				
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
	analyzedExpts <- data.frame(matrix(ncol = length(colnames(colorControls)), nrow=length(compensatedData)))
	colnames(analyzedExpts) <- gsub("-",".", colnames(colorControls))
	rownames(analyzedExpts) <- files
				
	for (p in 1:length(colnames(compensatedData))) {
					
		c1f <- curv1Filter(x=list(colnames(compensatedData)[p]), bwFac=4)
		fres <- filter(compensatedData[,p], c1f)
		s <- split(compensatedData[,p], fres, population=list(keep=c("peak 1")))
		analyzedExpts[,p] <- fsApply(s$`keep`,each_col,mean)
	}
		
	analyzedExpts[is.na(analyzedExpts)] <- 0
	analyzedExptsEval <- data.matrix(analyzedExpts)
			
	return(analyzedExptsEval)
}

#Import key file
key <- read.csv("key_4612.csv", header = TRUE)
key [is.na(key)] <- ""

#Find bead controls, apply bead normalization
beadControl <- key[which(TRUE == grepl("beads", key$PART, ignore.case=TRUE)),]
beadsControlKey <- key[which(TRUE == grepl("beads", key$CONTROL, ignore.case=TRUE)),]
beadsControl <- read.flowSet(path = "data", as.character(beadsControlKey$FILENAME), phenoData=list(Filename="$FIL"))

#Find color controls, calculate overlap, apply compensation
colorControlsKey <- key[which(!(key$CONTROL =="")),]
colorControlsKey <- colorControlsKey[-which(TRUE == grepl("beads", colorControlsKey$CONTROL, ignore.case=TRUE)),]
negativeControlKey <- key[which(TRUE == grepl("negative", key$CONTROL, ignore.case=TRUE)),]
colorControls <- read.flowSet(path = "data", as.character(colorControlsKey$FILENAME), phenoData=list(Filename="$FIL"))
negativeControl <- read.flowSet(path = "data", as.character(negativeControlKey$FILENAME), phenoData=list(Filename="$FIL"))

#Filter out for only relevant color columns based on controls
columnIndexes <- match(colorControlsKey$CONTROL, colnames(colorControls))
FSCSSCIndexes <- match(c("FSC-A", "SSC-A"), colnames(colorControls)) 
columnIndexes <- c(FSCSSCIndexes[!is.na(FSCSSCIndexes)], columnIndexes[!is.na(columnIndexes)])
colorControls <- colorControls[,columnIndexes]
negativeControl <- negativeControl[,columnIndexes]

#Determine the spillover matrix
# comp.mat <- spillover(x=colorControls,unstained=sampleNames(negativeControl),fsc="FSC-A",ssc="SSC-A",method="mean", stain_match = c("regexpr"))

#Group files by part name
#Determine unique parts and unique types of media
partsKey <- key[which((key$CONTROL =="")),]
uniqueParts <- unique(unlist(partsKey$PART))

#If part has the same time and media conditions, it is a duplicate data point
# mediaDuplicatesKey <- partsKey[duplicated(interaction(partsKey$MEDIA, partsKey$PART)),]
# mediaNonDuplicatesKey <- partsKey[!duplicated(interaction(partsKey$MEDIA, partsKey$PART)),]

mediaDuplicatesKey <- partsKey[duplicated(interaction(partsKey$MEDIA, partsKey$TIME, partsKey$PART)),]
mediaNonDuplicatesKey <- partsKey[!duplicated(interaction(partsKey$MEDIA, partsKey$TIME, partsKey$PART)),]

#Multiplex key
multiplexKey <- key[which(!(key$MULTIPLEX =="")),]
multiplexVals <- unique(multiplexKey$MULTIPLEX)

#Make multiplex dataframes
if (length(multiplexVals) > 1) {
	for (m in 1:length(multiplexVals)) {		
		Mmeans <- data.frame(matrix(ncol = length(colnames(colorControls)), nrow=0))
		colnames(Mmeans) <- gsub("-",".", colnames(colorControls))
		assign(paste("MMeans",multiplexVals[m],sep=""), Mmeans)
		Mstds <- data.frame(matrix(ncol = length(colnames(colorControls)), nrow=0))
		colnames(Mstds) <- gsub("-",".", colnames(colorControls))
		assign(paste("MStds",multiplexVals[m],sep=""), Mstds)
	}
}

#Loop through all unique parts
for (i in 1:length(uniqueParts)) {

	part <- as.character(uniqueParts[i])
	uniqueMediaThisPart <- mediaNonDuplicatesKey[which(TRUE == grepl(part, mediaNonDuplicatesKey$PART)),]
		
	mediaTypes <- sub("\\(?[0-9,.]+\\)?","", mediaNonDuplicatesKey$MEDIA)
	uniqueMediaTypes <- unique(sub("^\\s+|\\s+$","",mediaTypes))		

	#Loop through each media type
	for (j in 1:length(uniqueMediaTypes)) {

		media <- c()
		
		#Create data structures for means and standard deviations
		means <- data.frame(matrix(ncol = length(colnames(colorControls)), nrow=0))
		colnames(means) <- gsub("-",".", colnames(colorControls))
		standardDevs <- data.frame(matrix(ncol = length(colnames(colorControls)), nrow=0))
		colnames(standardDevs) <- gsub("-",".", colnames(colorControls))
		
		mediaType <- as.character(uniqueMediaTypes[j])
		mediaType <- gsub("\\(","\\\\(",mediaType)
		mediaType <- gsub("\\)","\\\\)",mediaType)
		
		#Only do this loop if a media type is entered
		if (!(mediaType=="") && length(uniqueMediaTypes) > 1) {
			
			blankMediaTypeThisPart <- uniqueMediaThisPart[which(mediaNonDuplicatesKey$MEDIA == ""),]
			uniqueMediaTypeThisPart <- uniqueMediaThisPart[which(TRUE == grepl(mediaType, mediaNonDuplicatesKey$MEDIA)),]
			uniqueMediaTypeThisPart <- rbind(blankMediaTypeThisPart,uniqueMediaTypeThisPart)
		
			#Loop through all unique medias and look for duplicates from the duplicates set
			for (k in 1:nrow(uniqueMediaTypeThisPart)) {
		
				file <- as.character(uniqueMediaTypeThisPart[k,]$FILENAME)
				media <- c(media, as.character(uniqueMediaTypeThisPart[k,]$MEDIA))

				#Search duplicate media matrix for duplicate files
				keys <- c("MEDIA", "PART", "TIME")
				unique <- data.table(uniqueMediaTypeThisPart[k,], key=keys)
				replicates <- data.table(mediaDuplicatesKey, key=keys)
				replicateFiles <- as.character(merge(unique, replicates)$FILENAME.y)
		
				#Define the flowset
				files <- c(file, replicateFiles)
				expts <- read.flowSet(path = "data", files, phenoData=list(Filename="$FIL"))
				expts <- expts[,columnIndexes]
								
				#Process experiments for this flowSet
				analyzedExptsEval <- process.samples(expts, comp.mat, colorControls)		
				means[k,] <- colMeans(analyzedExptsEval)
				standardDevs[k,] <- colSds(analyzedExptsEval)				
			}
			
			### PLOTTING OF PART MEDIA CONDITIONS ###
			#Get rid of FSC and SSC
			means <- means[,3:length(means)]
			means[is.na(means)] <- 0
			standardDevs <- standardDevs[,3:length(standardDevs)]
			standardDevs[is.na(standardDevs)] <- 0
			
			#Make plots
    		colnames(means) <- paste("MEAN", colnames(means), sep = "_")
    		colnames(standardDevs) <- paste("STD", colnames(standardDevs), sep = "_")
    		xaxis <- as.numeric(str_extract_all(sub("","0",media),"\\(?[0-9,.]+\\)?"))
    		xaxis <- cbind(xaxis)
    		
    		plotMat <- cbind(means, standardDevs)
    		plotMat <- cbind(xaxis, plotMat)
    		
    		#Plot each color
    		for (l in 1:length(colnames(means))) {
    			title <- colnames(means)[l]
    			yaxis <- cbind(means[,colnames(means)[l]])
    			yaxis <- as.numeric(yaxis)
    			error <- standardDevs[,colnames(standardDevs)[l]]
    			error <- as.numeric(error)
    			
    			ymin <- yaxis-error
    			ymax <- yaxis+error
    			limits <- aes(ymin=yaxis-error, ymax=yaxis+error)

				name <- as.character(paste(uniqueParts[i],"_",as.character(uniqueMediaTypes[j]),"_",colnames(means)[l],".png"))
				name <- str_replace_all(name, fixed(" "), "")
				name <- sub("/","",name)
				png(name)
    			p <- ggplot(plotMat, aes(x = xaxis, y = yaxis)) +
    			geom_errorbar(aes(ymin=yaxis-error, ymax=yaxis+error)) +
				geom_line() +
    			geom_point(size = 4, shape=21, fill="white") +
    			ylim(0,max(ymax)) +
    			#theme_bw() +
    			ggtitle(as.character(part)) +    			
    			xlab(as.character(uniqueMediaTypes[j])) +
    			ylab(as.character(paste(colnames(means)[l]," (RFU)")))
    			print(p)
    			dev.off()
    		}
    	
    	#If there is no curve, we still want to take averages of replicates
		} else {
			
			uniqueMediaTypeThisPart <- uniqueMediaThisPart[which(TRUE == grepl(mediaType, uniqueMediaThisPart$MEDIA)),]
			
			file <- as.character(uniqueMediaTypeThisPart[1,]$FILENAME)
			media <- c(media, as.character(uniqueMediaTypeThisPart[1,]$MEDIA))

			#Search duplicate media matrix for duplicate files
			keys <- c("MEDIA", "PART")
			unique <- data.table(uniqueMediaTypeThisPart[1,], key=keys)
			replicates <- data.table(mediaDuplicatesKey, key=keys)
			replicateFiles <- as.character(merge(unique, replicates)$FILENAME.y)
		
			#Analyze this flowset
			files <- c(file, replicateFiles)
			expts <- read.flowSet(path = "data", files, phenoData=list(Filename="$FIL"))
			expts <- expts[,columnIndexes]
			
			#Process experiments for this flowSet
			analyzedExptsEval <- process.samples(expts, comp.mat, colorControls)		
			means[1,] <- colMeans(analyzedExptsEval)
			standardDevs[1,] <- colSds(analyzedExptsEval)
			
			if ("MULTIPLEX" %in% colnames(key)) {
				if (!(uniqueMediaTypeThisPart$MULTIPLEX == "")) {
					x <- rbind(get(paste("MMeans",uniqueMediaTypeThisPart$MULTIPLEX,sep="")),means)
					assign(paste("MMeans",uniqueMediaTypeThisPart$MULTIPLEX,sep=""), x)
					y <- rbind(get(paste("MStds",uniqueMediaTypeThisPart$MULTIPLEX,sep="")),standardDevs)
					assign(paste("MStds",uniqueMediaTypeThisPart$MULTIPLEX,sep=""), y)
				}
			}
		}
	}
}

#Loop through multiplex values
if (length(multiplexVals) > 1) {
	multiplexDataFrame <- data.frame(matrix(ncol = length(colnames(colorControls)) + 2, nrow=0))
	colnames(multiplexDataFrame) <- c("TYPE", "MULTIPLEX", gsub("-",".", colnames(colorControls)))
	
	#Get standard deviation of averages
	for (n in 1:length(multiplexVals)) {
		finalMultiplexVals <- colSds(as.matrix(get(paste("MMeans",multiplexVals[n],sep=""))))
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
		png(name)
		PartType <- multiplexDataFrame$TYPE
		
    	p <- ggplot(multiplexDataFrame, aes(x = multiplexDataFrame$MULTIPLEX, y = yaxis, group = PartType, colour = PartType)) +
    	geom_line() +
    	geom_point(size = 4, shape=21, fill="white") +
    	theme_bw() +
    	ylim(0,max(yaxis)) +
    	ggtitle("Standard Deviation of Triplicate Population Averages") +    			
    	xlab("Number of Parts Multiplexed") +
    	ylab(paste(as.character(colnames(multiplexDataFrame)[o])," (RFU)"))
    	print(p)
    	dev.off()
    }
}