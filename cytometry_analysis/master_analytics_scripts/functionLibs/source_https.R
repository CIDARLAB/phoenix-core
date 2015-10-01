#Function to get source https with RCurl library
source_https <- function(u, unlink.tmp.certs = FALSE) {
  
  # load RCurl package
  require(RCurl)
 
  # read script lines from website using a security certificate
  if(!file.exists("cacert.pem")) download.file(url="http://curl.haxx.se/ca/cacert.pem", destfile = "cacert.pem")
  script <- getURL(u, followlocation = TRUE, cainfo = "cacert.pem")
  if(unlink.tmp.certs) unlink("cacert.pem")
 
  # parase lines and evealuate in the global environement
  eval(parse(text = script), envir= .GlobalEnv)
}