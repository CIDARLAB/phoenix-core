# setwd("/Volumes/Seagate Backup Plus Drive/data/regulation/Degradation_Regulation_91315")

#Import key file
key <- read.csv("key_EXPRESSEE_degradation_regulation_91315.csv", header = TRUE)
key [is.na(key)] <- ""

#Specify minimum number of events to be considered
minEvents <- 1000

#Call master script
wd <- getwd()
source("~/phoenix/phoenix-core/cytometry_analysis/master_analytics_scripts/analyze.R", chdir=T)

# setwd(superWD)