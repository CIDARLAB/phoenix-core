#Import key file
key <- read.csv("key_EXPRESSOR_pLac_81115.csv", header = TRUE)
key [is.na(key)] <- ""

#Specify minimum number of events to be considered
minEvents <- 1000

#Call master script
wd <- getwd()
source("~/cidar/phoenix-core/cytometry_analysis/master_analytics_scripts/analyze.R",chdir=T)
