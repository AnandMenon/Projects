library(cluster)
library(fpc)
clusterOutput<-read.table("/home/subhranil/CourseWork/Spring 2014/DIC/Project2/Results/Cluster/run_21/part-r-00000",header = FALSE, sep = "\t");
head(clusterOutput);
clusterCentroids<-clusterOutput[,1];
newClusters<-kmeans(clusterCentroids,centers=3);
#plotcluster(clusterOutput[,3],newClusters$cluster);
clusplot(clusterOutput,newClusters$cluster,color=TRUE, shade=TRUE,labels=2, lines=0);
