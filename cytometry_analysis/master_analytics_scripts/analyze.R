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

#Function for getting autoflouresnce values
getAutofluorescence <- function(negativeControlFlowSet) {
	
	autofluorescence <- data.frame(matrix(ncol = length(colnames(colorControlsFlowSet)), nrow=1))
	colnames(autofluorescence) <- gsub("-",".", colnames(colorControlsFlowSet))	
	
	#Apply cell size filter -- we only want cells clustered in the middle of FSC and SSC range
	cellSizeFilter <- norm2Filter(x = c("SSC-A", "FSC-A"), scale.factor = 2, filterId = "cellSize")
	cellSizeFilter.results <- filter (negativeControlFlowSet, cellSizeFilter)			
	negativeControlFlowSet <- Subset(negativeControlFlowSet, cellSizeFilter.results)
	
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
# key <- read.csv("key_EXPRESSEE_DEGRADATION_81215.csv", header = TRUE)
# key [is.na(key)] <- ""

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

#Specify minimum number of events to be considered
# minEvents <- 1000

#Determine the spillover matrix
comp.mat <- NULL
if (length(columnIndexes) > 3) {
	comp.mat <- spillover(x=colorControlsFlowSet,unstained=sampleNames(negativeControlFlowSet),fsc="FSC-A",ssc="SSC-A",method="mean", stain_match = c("regexpr"))
}

#Get autofluorescence
autofluorescence <- getAutofluorescence(negativeControlFlowSet)

#Group files by part name
#Determine unique parts
partsRows <- key[which((key$CONTROL =="")),]
uniquePartNames <- unique(unlist(partsRows$PART))

#Separate unique rows and duplicate rows by PART, MEDIA and TIME
allPartTimeMediaDuplicateRows <- partsRows[duplicated(interaction(partsRows$MEDIA, partsRows$TIME, partsRows$PART)),]
allPartTimeMediaUniqueRows <- partsRows[!duplicated(interaction(partsRows$MEDIA, partsRows$TIME, partsRows$PART)),]

#Multiplex key
multiplexRows <- key[which(!(key$MULTIPLEX =="")),]
multiplexVals <- unique(multiplexRows$MULTIPLEX)

#Make multiplex dataframes if there is any multiplexing to analyze
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
	
	#Initialize media-SM data frame
	mediaSMMap <- data.frame(matrix(ncol = 3, nrow=0))
	colnames(mediaSMMap) <- c("BASE", "SM", "CONCENTRATION")
	
	#New way to find unique media types with regular media type and a plus sign to indicate inducers
	uniqueMediaTypes <- c()
	mediaTypes <- uniqueTimeMediaRows$MEDIA	

	#Build matrix of base medias, inducers and concentrations for analysis
	for (x in 1:length(mediaTypes)) {
		mediaTypeAndSM <- trimws(strsplit(as.character(mediaTypes[x]), "\\+")[[1]])
		uniqueMediaTypes <- c(uniqueMediaTypes, as.character(mediaTypeAndSM[1]))

		#This contains some fancy regex from the internet, but separates the concentration values from the type of inducer if one exists
		if (length(mediaTypeAndSM) > 1) {
			SMConc <- trimws(gsub(".*\\((.*)\\).*", "\\1", mediaTypeAndSM[2]))
			SM <- trimws(gsub("\\(?[0-9,.]+\\)?","", SMConc))
			concentration <- unique(unlist(regmatches(SMConc, gregexpr("[0-9]+", SMConc))))
			mediaSMMap[x,] <- c(mediaTypeAndSM[1], SM, concentration)
		} else {
			mediaSMMap[x,] <- c(mediaTypeAndSM[1], "", "")
		}		
	}

	uniqueMediaTypes <- as.character(unique(mediaSMMap$BASE))

	#Loop through each unique media type, ignoring numeric values in any entry
	for (j in 1:length(uniqueMediaTypes)) {

		#Get list of small molecule additives
		mediaType <- as.character(uniqueMediaTypes[j])
		MediaTypeSMs <- mediaSMMap[which(TRUE == grepl(mediaType, mediaSMMap$BASE)),]
		uniqueMediaSMs <- as.character(unique(MediaTypeSMs$SM))			 
				
		#Get unique media rows with this media type				
		uniqueMediaRows <- uniqueTimeMediaRows[which(TRUE == grepl(mediaType, uniqueTimeMediaRows$MEDIA)),]
		
		#Loop through all small molecule additions for this mediaType
		for (y in 1:length(uniqueMediaSMs)) {
			
			SM <- as.character(uniqueMediaSMs[y])
										
			media <- c()
		
			#Initialize data structures for meansMedia and standardDevsMedia
			meansMedia <- data.frame(matrix(ncol = length(colnames(colorControlsFlowSet)), nrow=0))
			colnames(meansMedia) <- gsub("-",".", colnames(colorControlsFlowSet))
			standardDevsMedia <- data.frame(matrix(ncol = length(colnames(colorControlsFlowSet)), nrow=0))
			colnames(standardDevsMedia) <- gsub("-",".", colnames(colorControlsFlowSet))
											
			#Process data for files with no small molecule additions
			if (nchar(SM) == 0) {
				
				#Change the SM variable to an exact match on the media
				noSM <- paste("^", mediaType, "$", sep = '')
				uniqueMediaSMRowsAllTimes <- uniqueMediaRows[which(TRUE == grepl(noSM, uniqueMediaRows$MEDIA)),]			
				
				allSMConcs <- noSM
			
			} else {
				
				#Get the list of concentrations of this media with this small molecule additive			
				uniqueMediaSMRowsAllTimes <- uniqueMediaRows[which(TRUE == grepl(SM, uniqueMediaRows$MEDIA)),]			
				allSMConcs <- MediaTypeSMs[which(TRUE == grepl(SM, MediaTypeSMs$SM)),]$CONCENTRATION
				
				#If there is a media with no small molecule included in this set
				if ("" %in% uniqueMediaSMs) {
					noSM <- paste("^", mediaType, "$", sep = '')
					noSMRows <- uniqueMediaRows[which(TRUE == grepl(noSM, uniqueMediaRows$MEDIA)),]
					uniqueMediaSMRowsAllTimes <- rbind(noSMRows, uniqueMediaSMRowsAllTimes)	
					allSMConcs <- c(noSM, allSMConcs)
				}
			}
	
			#Loop through all unique media concentrations for this part
			for (k in 1:length(allSMConcs)) {				
				
				time <- c()		
				conc <- allSMConcs[k]
				
				#Name this media with some extra formatting 
				if (allSMConcs[k] == noSM && length(allSMConcs) == 1) {
					mediaName <- as.character(mediaType)
				} else if (allSMConcs[k] == noSM && length(allSMConcs) > 1) {
					mediaName <- as.character(paste(mediaType, "0", SM,  sep = '_'))
					media <- c(media, "0")
				} else {
					mediaName <- as.character(paste(mediaType, conc, SM,  sep = '_'))
					media <- c(media, conc)
				}						
								
				#Initialize data structures for meansMediaTime and standardDevsMediaTime
				meansMediaTime <- data.frame(matrix(ncol = length(colnames(colorControlsFlowSet)), nrow=0))
				colnames(meansMediaTime) <- gsub("-",".", colnames(colorControlsFlowSet))
				standardDevsMediaTime <- data.frame(matrix(ncol = length(colnames(colorControlsFlowSet)), nrow=0))
				colnames(standardDevsMediaTime) <- gsub("-",".", colnames(colorControlsFlowSet))
				
				#Get all unique times per one media condition
				uniqueMediaRowsAllTimes <- uniqueMediaSMRowsAllTimes[which(TRUE == grepl(conc, uniqueMediaSMRowsAllTimes$MEDIA)),]		
				uniqueMediaTimes <- as.character(unique(uniqueMediaRowsAllTimes$TIME))
				
				#Hold replicates across all times per media concentration for media graphs
				filesMediaConcentration <- c()
				files <- c()
				
				#Loop through all times for this media and small molecule concentration for time series analysis
				for (t in 1:length(uniqueMediaTimes)) {
					
					uniqueMediaTime <- uniqueMediaTimes[t]
					uniqueMediaTime <- paste("^", uniqueMediaTime, "$", sep = '')
					uniqueMediaTimeRow <- uniqueMediaRowsAllTimes[which(TRUE == grepl(uniqueMediaTime, uniqueMediaRowsAllTimes$TIME)),]
					
					time <- c(time, as.character(uniqueMediaTimes[t]))
					Afile <- as.character(uniqueMediaTimeRow$FILENAME)
					
					#Search duplicate media matrix for duplicate files
					partMediaKey <- c("MEDIA", "PART", "TIME")
					uniqueRowsThisExactMedia <- data.table(uniqueMediaTimeRow, key=partMediaKey)
					duplicateRowsThisExactMedia <- data.table(allPartTimeMediaDuplicateRows, key=partMediaKey)
					replicateFiles <- as.character(merge(uniqueRowsThisExactMedia, duplicateRowsThisExactMedia)$FILENAME.y)
					
					#Summarize files for times in this media condition in addition to files for all times in this media condition
					files <- c(Afile, replicateFiles)
					filesMediaConcentration <- c(filesMediaConcentration, files)				
					
					#Only process this data if there is more than one time for this media and small molecule
					if (length(uniqueMediaTimes) > 1) {
						
						#Do not do a time series for the blank media in combination with supplemented media
						if (allSMConcs[k] != noSM | length(allSMConcs) == 1) {
					
							#Do not evaluate files with less than 1000 events
							finalFiles <- c()
							for (nFile in 1:length(files)) {
								frame <- read.flowSet(path = "data", files[nFile])
								if (length(frame[[1]]) > minEvents) {
									finalFiles <- c(finalFiles, files[nFile])
								}
							}
										
							#Analyze this flowset if there are any files left after the filter for minimal events
							if (!is.null(finalFiles)) {
								experimentFlowSet <- read.flowSet(path = "data", finalFiles, phenoData=list(Filename="$FIL"))
								experimentFlowSet <- experimentFlowSet[,columnIndexes]				
												
								#Process experiments for this flowSet
								analyzedExptsMediaTimeEval <- process.samples(experimentFlowSet, comp.mat, colorControlsFlowSet, autofluorescence, finalFiles)
								meansMediaTime[t,] <- colMeans(analyzedExptsMediaTimeEval)
								standardDevsMediaTime[t,] <- colSds(analyzedExptsMediaTimeEval)
							}
						}
					}	
				}
				
				#Add time series data to output file
				for (w in 1:length(uniqueMediaTimes)) {
					outputDataRow <- as.data.frame(rbind(c(part, conc, uniqueMediaTimes[w], as.character(meansMediaTime[w,3:length(meansMediaTime)]))))
					colnames(outputDataRow) <- colnames(output)
					output <- rbind(output, outputDataRow)
				}
				
				#Make a fluorescence v. time plot if more than one time entered to this media at this concentration
				if (nrow(meansMediaTime) > 1) {
					
					### PLOTTING OF PART MEDIA CONDITIONS ###
					#Get rid of FSC and SSC
					
					#Edge case of only one color aside from FSC and SSC
					storedColName <- colnames(meansMediaTime)[3]
					
					meansMediaTime <- cbind(meansMediaTime[,3:length(meansMediaTime)])
					meansMediaTime[is.na(meansMediaTime)] <- 0				
					standardDevsMediaTime <- cbind(standardDevsMediaTime[,3:length(standardDevsMediaTime)])
					standardDevsMediaTime[is.na(standardDevsMediaTime)] <- 0
					
					#Column name correction
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
		    			
		    			#Define y values
		    			yaxis <- cbind(meansMediaTime[,colnames(meansMediaTime)[u]])
		    			yaxis <- as.numeric(yaxis)
		    			
		    			#Get standard error values for y values
		    			error <- standardDevsMediaTime[,colnames(standardDevsMediaTime)[u]]
		    			error <- as.numeric(error)	    			
		    			ymin <- yaxis-error
		    			ymax <- yaxis+error
		    			limits <- aes(ymin=yaxis-error, ymax=yaxis+error)
		
						#File naming and placement in a subdirectory
						name <- as.character(paste(part,"_",mediaName,"_",colnames(meansMediaTime)[u],".png"), sep='')
						name <- str_replace_all(name, fixed(" "), "")
						name <- sub("/","",name)
						File <- as.character(paste("./time_series/", part, "/", name))
	   					File <- str_replace_all(File, fixed(" "), "")
						# if (file.exists(File)) stop(File, " already exists")
						dir.create("time_series")
						dir.create(dirname(File), showWarnings = FALSE)
						png(File, width=960, height=960, res=120)
		    			
		    			#Plotting
		    			pt <- ggplot(data = plotMat, aes(x = xaxis, y = yaxis)) +
		    			geom_errorbar(aes(ymin=yaxis-error, ymax=yaxis+error)) +
						geom_line() +
						scale_y_log10(limits = c(1e-1,1e6)) +
		    			geom_point(size = 4, shape=21, fill="white") +
		    			# ylim(0,max(ymax)) +
		    			# #theme_bw() +
		    			ggtitle(as.character(paste(part,mediaName,colnames(meansMediaTime)[u], sep = "_"))) +    	
		    			xlab(as.character("TIME")) +
						ylab(as.character(paste(colnames(meansMediaTime)[u]," (RFU)")))
		    			print(pt)
		    			dev.off()
		    		}
				}			
		
				#Remove files with less than minEvents
				finalFilesMediaConcentration <- c()
				for (nFile in 1:length(filesMediaConcentration)) {
					frame <- read.flowSet(path = "data", filesMediaConcentration[nFile])
					if (length(frame[[1]]) > minEvents) {
						finalFilesMediaConcentration <- c(finalFilesMediaConcentration, filesMediaConcentration[nFile])
					}
				}
		
				#Analyze this flowset if there are any files left after the filter for minimal events
				if (!is.null(finalFilesMediaConcentration)) {
					experimentFlowSetMedia <- read.flowSet(path = "data", finalFilesMediaConcentration, phenoData=list(Filename="$FIL"))
					experimentFlowSetMedia <- experimentFlowSetMedia[,columnIndexes]				
									
					#Process experiments for this flowSet
					analyzedExptsMediaEval <- process.samples(experimentFlowSetMedia, comp.mat, colorControlsFlowSet, autofluorescence, finalFilesMediaConcentration)
					meansMedia[k,] <- colMeans(analyzedExptsMediaEval)
					standardDevsMedia[k,] <- colSds(analyzedExptsMediaEval)
					
					#Edge case where there is only one type of media for this part - throw it into a separate data frame for a bar graph
					# if (length(uniqueMediaTypeConcentrations) == 1) {				
						# oneMediaParts <- c(oneMediaParts, paste(part, uniqueMediaTypeConcentrations[k], sep = "_"))				
						# meansOneMedia <- rbind(meansOneMedia, meansMedia[k,])
						# standardDevsOneMedia <- rbind(standardDevsOneMedia, standardDevsMedia[k,])				
					# }				
				}
			}
			
			#Make a fluorescence v. media plot if more than one time entered to this media at this concentration
			if (nrow(meansMedia) > 1) {
			
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
	    			
	    			#Define y values
	    			yaxis <- cbind(meansMedia[,colnames(meansMedia)[l]])
	    			yaxis <- as.numeric(yaxis)
	    			
	    			#Get standard error values for y values
	    			error <- standardDevsMedia[,colnames(standardDevsMedia)[l]]
	    			error <- as.numeric(error)    			
	    			ymin <- yaxis-error
	    			ymax <- yaxis+error
	    			limits <- aes(ymin=yaxis-error, ymax=yaxis+error)
	
					#File naming and placement in a subdirectory
					name <- as.character(paste(part,"_",mediaType,"_", SM, "_",colnames(meansMedia)[l],".png"))
					name <- str_replace_all(name, fixed(" "), "")
					name <- sub("/","",name)
					File <- as.character(paste("./media_induction/", part, "/", name))
		   			File <- str_replace_all(File, fixed(" "), "")
					# if (file.exists(File)) stop(File, " already exists")
					dir.create("media_induction")
					dir.create(dirname(File), showWarnings = FALSE)
					png(File, width=960, height=960, res=120)
					
					#Plotting
	    			p <- ggplot(plotMat, aes(x = xaxis, y = yaxis)) +
	    			geom_errorbar(aes(ymin=yaxis-error, ymax=yaxis+error)) +
					geom_line() +
					scale_y_log10(limits = c(1e-1,1e6)) +
	    			geom_point(size = 4, shape=21, fill="white") +
	    			# ylim(0,max(ymax)) +
	    			#theme_bw() +
	    			ggtitle(as.character(part)) +    			
	    			xlab(as.character(paste(mediaType, SM, sep ='+'))) +
	    			ylab(as.character(paste(colnames(meansMedia)[l]," (RFU)")))
	    			print(p)
	    			dev.off()
	    		}    	
		    	
		    	#If there is no media curve, we still want to take averages of duplicateTimeMediaRows
			} else {
				
				oneMediaParts <- c(oneMediaParts, paste(part, mediaType, sep = "_"))				
				meansOneMedia <- rbind(meansOneMedia, meansMedia[k,])
				standardDevsOneMedia <- rbind(standardDevsOneMedia, standardDevsMedia[k,])
				
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
}

#If a part has only one media, make bar plots for that part in its media
if (length(oneMediaParts) > 1) {
	
	### PLOTTING OF PART MEDIA CONDITIONS ###
	#Get rid of FSC and SSC
	#Edge case of only one color aside from FSC and SSC
	storedColName <- colnames(meansOneMedia)[3]
	
	meansOneMedia <- cbind(meansOneMedia[,3:length(meansOneMedia)])
	meansOneMedia[is.na(meansOneMedia)] <- 0				
	standardDevsOneMedia <- cbind(standardDevsOneMedia[,3:length(standardDevsOneMedia)])
	standardDevsOneMedia[is.na(standardDevsOneMedia)] <- 0
	
	#Column name correction
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
   		
   		#Define y values
   		yaxis <- cbind(meansOneMedia[,colnames(meansOneMedia)[v]])
   		yaxis <- as.numeric(yaxis)
   		
   		#Get standard error values for y values
   		error <- standardDevsOneMedia[,colnames(standardDevsOneMedia)[v]]
   		error <- as.numeric(error)    			
   		ymin <- yaxis-error
   		ymax <- yaxis+error
   		limits <- aes(ymin=yaxis-error, ymax=yaxis+error)		

		#File naming and placement in a subdirectory
		name <- as.character(paste("Mean_Population_Averages_One_Media_",as.character(title),".png"))
		name <- str_replace_all(name, fixed(" "), "")
		name <- sub("/","",name)		   		
   		File <- as.character(paste("./one_media/", name))
   		File <- str_replace_all(File, fixed(" "), "")
		# if (file.exists(File)) stop(File, " already exists")
		dir.create(dirname(File), showWarnings = FALSE)   		
   		png(File, width=960, height=960, res=120)
   		
   		#Plotting
   		po <- ggplot(plotMat, aes(x = xaxis, y = yaxis)) +
   		geom_bar(colour="black", fill="#DD8888", width=.8, stat="identity") + 
    	guides(fill=FALSE) +
    	scale_y_log10(limits = c(1e-1,1e6)) +
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
    	
    	#Define y values
    	yaxis <- cbind(multiplexDataFrame[,colnames(multiplexDataFrame)[o]])
		yaxis <- as.numeric(yaxis)

		#File naming and placement in a subdirectory
		name <- as.character(paste(colnames(multiplexDataFrame)[o],".png"))
		name <- str_replace_all(name, fixed(" "), "")
		File <- as.character(paste("./functional_richness/", uniquePartNames[i], "/", name))
   		File <- str_replace_all(File, fixed(" "), "")
		# if (file.exists(File)) stop(File, " already exists")
		dir.create("functional_richness")
		dir.create(dirname(File), showWarnings = FALSE)		
		png(File, width=960, height=960, res=120)

		PartType <- multiplexDataFrame$TYPE
		
    	p <- ggplot(multiplexDataFrame, aes(x = multiplexDataFrame$MULTIPLEX, y = yaxis, group = PartType, colour = PartType)) +
    	geom_line() +
    	geom_point(size = 4, shape=21, fill="white") +
    	theme_bw() +
    	scale_y_log10(limits = c(1e-1,1e6)) +
    	ggtitle("Functional Richness of Triplicate Population Averages") +    			
    	xlab("Number of Parts Multiplexed") +
    	ylab(paste(as.character(colnames(multiplexDataFrame)[o])," (RFU)"))
    	print(p)
    	dev.off()
    }
}

#Write output into an exported csv file for Phoenix
# filename <- "results_5615.csv"
# write.csv(file=filename, x=output, row.names=FALSE)