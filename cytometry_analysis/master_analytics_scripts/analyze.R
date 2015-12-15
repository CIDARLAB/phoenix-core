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
library(flowCore)
library(flowClust)
library(Biobase)
library(xtable)

#Import function libraries
source("functionLibs/source_https.R")
source("functionLibs/functionalDiversity.R")
source("functionLibs/getAutoFluorescence.R")
source("functionLibs/getColorMultiplier.R")
source("functionLibs/normalizeToBeads.R")
source("functionLibs/top_sliding_window_peaks.R")
source("functionLibs/process_samples.R")
source_https('https://raw.githubusercontent.com/pontikos/FCS/master/fcs.R')
setwd(wd)

#Overall analysis method call
# analyzeKey <- function (key, minEvents) {
	
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
	
	#Define MEFL csv file based on which channels are controlled
	MEFL <- cbind(c(NA,692,2192,6028,17493,35674,126907,290983))
	MEFLDataFrame <- data.frame(matrix(ncol = length(colnames(colorControlsFlowSet)), nrow=8))
	colnames(MEFLDataFrame) <- gsub("-A","", colnames(colorControlsFlowSet))
	rownames(MEFLDataFrame) <- c(1:8)
	# MEFLDataFrame <- MEFLDataFrame[,3:length(colnames(MEFLDataFrame))]
	for (channel in 1:length(colnames(MEFLDataFrame))) {
		MEFLDataFrame[,channel] <- MEFL  
	}
	write.table(file="MEFLmatrix.csv", x=MEFLDataFrame, row.names=FALSE, sep=";")
	
	#Find bead controls, apply bead normalization
	beadsControlRow <- key[which(TRUE == grepl("beads", key$CONTROL, ignore.case=TRUE)),]
	beadsControlFlowFrame <- BeadFlowFrame(fcs.filename = paste('data/', as.character(beadsControlRow$FILENAME), sep=''), bead.filename = "MEFLmatrix.csv")
	gatedBeadControlFlowFrame <- gateBeads(beadsControlFlowFrame)
	
	#Determine the spillover matrix
	comp.mat <- NULL
	if (length(columnIndexes) > 3) {
		comp.mat <- spillover(x=colorControlsFlowSet,unstained=sampleNames(negativeControlFlowSet),fsc="FSC-A",ssc="SSC-A",method="mean", stain_match = c("regexpr"))
	}
	
	#Get autofluorescence
	autofluorescence <- getAutofluorescence(negativeControlFlowSet, gatedBeadControlFlowFrame)
	
	#Get color multiplier vector relative to FITC-A channel for gfp control
	colorMultiplierVector <- getColorMultiplier(colorControlsFlowSet, gatedBeadControlFlowFrame, autofluorescence, as.character(colorControlRows$FILENAME))
	
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
	
	#Initialize data structures for meansOneMedia and standardDevsOneMedia
	meansOneMedia <- data.frame(matrix(ncol = length(colnames(colorControlsFlowSet)), nrow=0))
	colnames(meansOneMedia) <- gsub("-",".", colnames(colorControlsFlowSet))
	standardDevsOneMedia <- data.frame(matrix(ncol = length(colnames(colorControlsFlowSet)), nrow=0))
	colnames(standardDevsOneMedia) <- gsub("-",".", colnames(colorControlsFlowSet))
	oneMediaParts <- c()
	
	#Initialize data structures for meansMediaReg and standardDevsMedia
	meansReg <- data.frame(matrix(ncol = length(colnames(colorControlsFlowSet)), nrow=0))
	colnames(meansReg) <- gsub("-",".", colnames(colorControlsFlowSet))
	standardDevsReg <- data.frame(matrix(ncol = length(colnames(colorControlsFlowSet)), nrow=0))
	colnames(standardDevsReg) <- gsub("-",".", colnames(colorControlsFlowSet))
	regParts <- c()
	regMedia <- c()
	
	#Loop through all unique parts to make plots
	for (i in 1:length(uniquePartNames)) {
		
		#Create a directory for this part
		part <- as.character(uniquePartNames[i])
		dir.create(part, showWarnings = FALSE)
		
		uniqueTimeMediaRows <- allPartTimeMediaUniqueRows[which(TRUE == grepl(paste("^", part, "$", sep = ''), allPartTimeMediaUniqueRows$PART)),]
		
		#Initialize media-SM data frame
		mediaSMMap <- data.frame(matrix(ncol = 3, nrow=0))
		colnames(mediaSMMap) <- c("BASE", "SM", "CONCENTRATION")
		
		#New way to find unique media types with regular media type and a plus sign to indicate inducers
		uniqueMediaTypes <- c()
		mediaTypes <- unique(uniqueTimeMediaRows$MEDIA)	
	
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
			#Create a directory for this mediaType
			mediaType <- as.character(uniqueMediaTypes[j])
			mediaTypePath <- paste(part, "/", mediaType, sep = '')		
			dir.create(mediaTypePath, showWarnings = FALSE)
			
			MediaTypeSMs <- mediaSMMap[which(TRUE == grepl(paste("^", mediaType, "$", sep = ''), mediaSMMap$BASE)),]
			uniqueMediaSMs <- as.character(unique(MediaTypeSMs$SM))			 
					
			#Get unique media rows with this media type				
			uniqueMediaRows <- uniqueTimeMediaRows[which(TRUE == grepl(mediaType, uniqueTimeMediaRows$MEDIA)),]
			
			#Loop through all small molecule additions for this mediaType
			for (y in 1:length(uniqueMediaSMs)) {
							
				SM <- as.character(uniqueMediaSMs[y])						
				noSM <- c()										
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
					
					currentMediaTypePath <- mediaTypePath
										
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
					
					#Create a directory for this small molecule
					SMdirName <- gsub("/", "_", SM)
					SMdirName <- gsub(" ", "_", SMdirName)
					currentMediaTypePath <- paste(mediaTypePath, "/", SMdirName, sep = '')
					dir.create(currentMediaTypePath, showWarnings = FALSE)			
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
					if (!is.na(as.numeric(conc)))  {
						uniqueMediaRowsAllTimes <- uniqueMediaSMRowsAllTimes[which(TRUE == grepl(paste(conc, SM, sep = ' '), uniqueMediaSMRowsAllTimes$MEDIA)),]
					} else {
						uniqueMediaRowsAllTimes <- uniqueMediaSMRowsAllTimes[which(TRUE == grepl(conc, uniqueMediaSMRowsAllTimes$MEDIA)),]
					}
							
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
						if (uniqueMediaTimes[t] == uniqueMediaTimes[which.max(uniqueMediaTimes)]) {						
							filesMediaConcentration <- c(filesMediaConcentration, files)				
						}
						
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
									analyzedExptsMediaTimeEval <- process.samples(experimentFlowSet, colorControlsFlowSet, gatedBeadControlFlowFrame, comp.mat, autofluorescence, finalFiles, colorMultiplierVector)
									meansMediaTime[t,] <- colMeans(analyzedExptsMediaTimeEval)
									standardDevsMediaTime[t,] <- colSds(analyzedExptsMediaTimeEval)
								}	
							}
						}	
					}
					
					#Make a fluorescence v. time plot if more than one time entered to this media at this concentration
					if (nrow(meansMediaTime) > 1) {					
										
						#Create a directory if there are multiple 
						if (grepl(mediaType, conc) == TRUE) {
							# currentMediaTypeConcPath <- paste(currentMediaTypePath, "/", "plain", sep='')
							currentMediaTypeConcPath <- currentMediaTypePath
						} else {
							currentMediaTypeConcPath <- paste(currentMediaTypePath, "/", conc, sep='')
						}
						dir.create(currentMediaTypeConcPath, showWarnings = FALSE)
						
						### PLOTTING OF PART MEDIA TIMES ###
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
							name <- as.character(paste(part, "_", colnames(meansMediaTime)[u],".png", sep=''))
							name <- str_replace_all(name, fixed(" "), "")
							name <- sub("/","",name)
							File <- as.character(paste(currentMediaTypeConcPath, "/plots/", name, sep=''))
		   					File <- str_replace_all(File, fixed(" "), "")
		   					dir.create(as.character(paste(currentMediaTypeConcPath, "/plots", sep='')))
							dir.create(dirname(File), showWarnings = FALSE)
			    			
			    			#Plotting
			    			png(File, width=960, height=960, res=120)
			    			pt <- ggplot(data = plotMat, aes(x = xaxis, y = yaxis)) +
			    			geom_errorbar(aes(ymin=yaxis-error, ymax=yaxis+error)) +
							geom_line() +
							scale_y_log10(limits = c(1e-1,1e6)) +
			    			geom_point(size = 4, shape=21, fill="white") +
			    			# ylim(0,max(ymax)) +
			    			# #theme_bw() +
			    			ggtitle(as.character(paste(part,mediaName,colnames(meansMediaTime)[u], sep = "_"))) +    	
			    			xlab(as.character("TIME")) +
							ylab(as.character(paste(colnames(meansMediaTime)[u]," (MEFL)"))) +
					    	theme(axis.text.y  = element_text(colour="black")) +
	    					theme(axis.text.x  = element_text(colour="black")) 
			    			print(pt)
			    			dev.off()		    			
			    		}		    		
			    		
		    			#Output file
						colnames(plotMat)[1] <- "Time"
						filename <- as.character(paste(currentMediaTypeConcPath, "/", "timeSeriesPlotPoints.csv", sep=''))
						write.csv(file=filename, x=plotMat, row.names=FALSE)
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
						analyzedExptsMediaEval <- process.samples(experimentFlowSetMedia, colorControlsFlowSet, gatedBeadControlFlowFrame, comp.mat, autofluorescence, finalFilesMediaConcentration, colorMultiplierVector)
						meansMedia[k,] <- colMeans(analyzedExptsMediaEval)
						standardDevsMedia[k,] <- colSds(analyzedExptsMediaEval)							
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
						name <- as.character(paste(part, "_",colnames(meansMedia)[l],".png"), sep='')
						name <- str_replace_all(name, fixed(" "), "")
						name <- sub("/","",name)
						File <- as.character(paste(currentMediaTypePath, "/plots/", name, sep=''))
	   					File <- str_replace_all(File, fixed(" "), "")
	   					dir.create(as.character(paste(currentMediaTypePath, "/plots", sep='')))
						dir.create(dirname(File), showWarnings = FALSE)
																
						#Plotting
						png(File, width=960, height=960, res=120)
		    			p <- ggplot(plotMat, aes(x = xaxis, y = yaxis)) +
		    			geom_errorbar(aes(ymin=yaxis-error, ymax=yaxis+error)) +
						geom_line() +
						scale_y_log10(limits = c(1e-1,1e6)) +
		    			geom_point(size = 4, shape=21, fill="white") +
		    			# ylim(0,max(ymax)) +
		    			#theme_bw() +
		    			ggtitle(as.character(part)) +    			
		    			xlab(as.character(paste(mediaType, SM, sep ='+'))) +
		    			ylab(as.character(paste(colnames(meansMedia)[l]," (MEFL)"))) +
	    				theme(axis.text.y  = element_text(colour="black")) +
	    				theme(axis.text.x  = element_text(colour="black")) 
		    			print(p)
		    			dev.off()	    								
		    		}    	
		    		
		    		#Output file
					colnames(plotMat)[1] <- SMdirName
					filename <- as.character(paste(currentMediaTypePath, "/", "mediaTitrationPlotPoints.csv", sep=''))
					write.csv(file=filename, x=plotMat, row.names=FALSE)
		    		
		    		#New regulation plotting bassis - for only a single strain, not across strains
		    		if (!is.null(uniqueMediaRows$REGULATION)) {
		    			
		    			#This check needs to be extended to look for all rows that have regulation and only use those, but will not do that for the time being
						if (!(uniqueMediaRows$REGULATION[1] == "")) {
							
							#Determine regulation control channels for plotting
							regControlPartAndChannels <- trimws(strsplit(as.character(uniqueMediaRows$REGULATION[1]), "\\(")[[1]])
							regControlPartAndChannels <- gsub("\\)","", regControlPartAndChannels)							
							regControlChannels <- trimws(strsplit(as.character(trimws(gsub(".*\\((.*)\\).*", "\\1", regControlPartAndChannels[1]))), "\\|")[[1]])

							#Check how many channels are in this split - right now this is only going ot handle two, but could be extended to do more
							if (length(regControlChannels) == 2) {
								
								ychannel <- gsub("-",".", regControlChannels[2])
								yaxis <- meansMedia[,which(TRUE == grepl(ychannel, colnames(meansMedia)))]
								yaxis <- as.numeric(yaxis)
								xchannel <- gsub("-",".", regControlChannels[1])
								xaxis <- meansMedia[,which(TRUE == grepl(xchannel, colnames(meansMedia)))]
								xaxis <- as.numeric(xaxis)
								ylabel <- as.character(paste(ychannel, " (MEFL)", sep=""))
								xlabel <- as.character(paste(xchannel, " (MEFL)", sep=""))
								plotMatColNames <- c(xchannel, ychannel)
								
								plotMat <- cbind(xaxis, as.numeric(yaxis))
					    		plotMat <- as.data.frame(plotMat)
					    		
					    		title <- paste("Regulation of ", xlabel, " ON ", ylabel, sep='')
				
								#File naming and placement in a subdirectory
								name <- as.character(paste(ychannel, "_v_", xchannel,".png", sep=''))
								name <- str_replace_all(name, fixed(" "), "")
								name <- sub("/","",name)
								File <- as.character(paste(currentMediaTypePath, "/regulation/plots/", name, sep=''))
			   					File <- str_replace_all(File, fixed(" "), "")
			   					dir.create(as.character(paste(currentMediaTypePath, "/regulation", sep='')))
								dir.create(dirname(File), showWarnings = FALSE)	    			
										    			
				    			#Plotting																		
								png(File, width=960, height=960, res=120)
				    			pt <- ggplot(data = plotMat, aes(x = xaxis, y = as.numeric(yaxis))) +
								#geom_line() +
								# scale_y_log10(limits = c(1e-1,1e6)) +								
								# scale_x_log10(limits = c(1e-1,1e6)) +
								theme_bw() +
				    			# geom_point(size = 4, shape=21, fill="white") +
				    			geom_point(size = 4, shape=21, fill="red") +
				    			ggtitle(as.character(title)) +    	
				    			xlab(xlabel) +
								ylab(ylabel) +
			    				theme(axis.text.y  = element_text(colour="black")) +
						    	theme(axis.text.x  = element_text(colour="black")) 
				    			print(pt)
				    			dev.off()
				    			
				    			#Output file
								colnames(plotMat) <- plotMatColNames
								filename <- as.character(paste(currentMediaTypePath, "/regulation/", "regulationPlotPoints.csv", sep=''))
								write.csv(file=filename, x=plotMat, row.names=FALSE)
							}
						}
					}
			    	
			    #If there is no media curve, we still want to take averages of duplicateTimeMediaRows
				} else {
					
					#Remove extra directories if there are no time series, inducer titration or regulation
					if(nrow(meansMediaTime) < 2) {
						if (is.null(uniqueMediaRows$REGULATION) || (uniqueMediaRows$REGULATION[1] == "")) {
							unlink(part, recursive = TRUE)
						}
					}				
					
					oneMediaParts <- c(oneMediaParts, paste(part, mediaType, sep = "_"))				
					meansOneMedia <- rbind(meansOneMedia, meansMedia[1,])
					standardDevsOneMedia <- rbind(standardDevsOneMedia, standardDevsMedia[1,])
					
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
	   		File <- as.character(paste("./one_media_MEFL/", name, sep = ''))
	   		File <- str_replace_all(File, fixed(" "), "")
			# if (file.exists(File)) stop(File, " already exists")
			dir.create(dirname(File), showWarnings = FALSE)   		
	   		png(File, width=960, height=960, res=120)
	   		
	   		#Plotting
	   		po <- ggplot(plotMat, aes(x = xaxis, y = yaxis)) +
	   		# geom_bar(colour="black", fill="#DD8888", width=.8, stat="identity") + 
	   		geom_bar(colour="black", fill="#56B4E9", width=.8, stat="identity") + 
	    	guides(fill=FALSE) +
	    	scale_y_log10(limits = c(1e-1,1e6)) +
	   		geom_errorbar(aes(ymin=yaxis-error, ymax=yaxis+error)) +
	  		ggtitle(as.character("Mean Population Averages")) +    			
	   		xlab(as.character("PARTS")) +
	   		ylab(as.character(paste(colnames(meansOneMedia)[v]," (MEFL)"))) +
	   		theme(axis.title.x = element_blank()) +
	   		theme(axis.text.x  = element_text(angle=45, vjust=0.5, size=8, colour="black")) +
	   		theme(axis.text.y  = element_text(colour="black"))
	   		print(po)
	   		dev.off()
	  	}
	  	  	 
	    #Output file
		colnames(plotMat)[1] <- "Part"
		filename <- as.character(paste("./one_media_MEFL/", "oneMediaPlotPoints.csv", sep=''))
		write.csv(file=filename, x=plotMat, row.names=FALSE)
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
			File <- as.character(paste("./functional_richness/", name))
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
	    	# scale_y_log10(limits = c(1e-1,1e6)) +
	    	ggtitle("Functional Richness of Triplicate Population Averages") +    			
	    	xlab("Number of Parts Multiplexed") +
	    	ylab(paste(as.character(colnames(multiplexDataFrame)[o])," (MEFL)")) +
	    	theme(axis.text.y  = element_text(colour="black")) +
	    	theme(axis.text.x  = element_text(colour="black")) 
	    	print(p)
	    	dev.off()
	    }
	    
	    #Output file
		colnames(plotMat)[1] <- "Multiplex"
		filename <- as.character(paste("./functional_richness/", "fnRichnessPlotPoints.csv", sep=''))
		write.csv(file=filename, x=multiplexDataFrame, row.names=FALSE)   
	}
# }