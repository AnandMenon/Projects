library('tm');
library('wordcloud');
lords<-Corpus(DirSource("/home/subhranil/CourseWork/Spring 2014/DIC/Project2/graph_final_data/"));
inspect(lords);
wordcloud(lords, scale=c(5,0.005), max.words=20, random.order=FALSE, rot.per=0.0, use.r.layout=FALSE);



