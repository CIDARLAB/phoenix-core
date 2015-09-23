#Function for performing bead analysis for Phoenix
normalizeToBeads <- function(experimentFlowSet, gatedBeadControlFlowFrame) {	
	
	# #Gate out main cluster of cells on side and forward scatter
	# X <- beadsControlFlowFrame@exprs[,c('FSC-A','SSC-A')]
	# res <- flowClust(X,K=4)		
	# smoothPlot(X, classification=res@label, chulls=FALSE)
	
	# #Cluster with the most events is the main one
	# i <- which.max(table(res@label))==res@label 
	# X <- beadsControlFlowFrame[i,]@exprs[,grep('-A$',colnames(beadsControlFlowFrame))]	
	# X.trans <- apply(X,2,logicleTransform(w=.1))
	# X.trans <- X.trans[,-grep('SSC-A|FSC-A',colnames(X.trans))]
	
	# #Use PCA on all 18 colours
	# pca <- princomp(X.trans)
	# pca.X.trans <- pca$scores[,1:2]
	# dens <- density(pca$scores[,1])
	
	# #Find the top 8 peaks
	# p <- top.sliding.window.peaks(dens,K=8)[,1]
	# abline(v=p)
	
	# #Cluster using kmeans
	# res <- kmeans(pca.X.trans[,1],p)
	# abline(v=res$centers)
	
	# #These are now the MFIs
	# MFIs <-do.call('rbind',by(X,res$cluster,colMeans))
	
	for (i in 1:length(experimentFlowSet)) {
		toMEF(gatedBeadControlFlowFrame, experimentFlowSet[[i]])
	}
	
	return(experimentFlowSet)
}