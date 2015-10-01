# Use a sliding window on the univariate density function
# Returns the top K sliding window peaks
top.sliding.window.peaks <- function(d, K, span=40) {
  y <- d$y
  x <- d$x
  ## sliding a window of size span and returns locations where the middle point in the window is maximum.  
  ## returns the indexes of the peaks
  ind <- c()
  for( i in 1:(length(y)-span)) {
    mid <- i+span%/%2
    if ( y[mid]==max(y[i:(i+span)]) & y[mid]!=y[i] & y[mid]!=y[i+span] ) ind <- c(ind, mid)
  }
  peaks <- cbind(x=x[ind],y=y[ind])
  top.peaks <- peaks[order(peaks[,'y'],decreasing=TRUE)[1:K],]
  top.peaks <- top.peaks[order(top.peaks[,'x']),]
  return(top.peaks)
}