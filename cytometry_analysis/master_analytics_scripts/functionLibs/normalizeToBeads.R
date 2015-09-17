#Function for performing bead analysis for Phoenix
normalizeToBeads <- function() {
	source_https('https://raw.githubusercontent.com/pontikos/FCS/master/fcs.R')
	
	flowCore::read.FCS('beads_41215.fcs')->b
	filename <- as.character("beads_41215")
	
	X <- b@exprs[,c('FSC-A','SSC-A')]
	
	# 4 might not always be appropriate
	res <- flowClust(X,K=4)
	
	# gate out main cluster of cells on side and forward scatter
	smoothPlot(X, classification=res@label, chulls=FALSE)
	
	#cluster with the most events is the main one
	i <- which.max(table(res@label))==res@label 
	X <- b[i,]@exprs[,grep('-A$',colnames(b))]
	
	X.trans <- apply(X,2,logicleTransform(w=.1))
	X.trans <- X.trans[,-grep('SSC-A|FSC-A',colnames(X.trans))]
	
	# use PCA on all 18 colours
	pca <- princomp(X.trans)
	pca.X.trans <- pca$scores[,1:2]
	dens <- density(pca$scores[,1])
	
	# find the top 8 peaks
	p <- top.sliding.window.peaks(dens,K=8)[,1]
	#plot(dens)
	abline(v=p)
	
	# cluster using kmeans
	res <- kmeans(pca.X.trans[,1],p)
	#plot(dens)
	abline(v=res$centers)
	
	# these are now the MFIs
	MFIs <-do.call('rbind',by(X,res$cluster,colMeans))
}