library(rpart)
#library(rpart.plot)
#library(DAAG)
#library(caTools)

arbre_classification = function(repertoire, indY,splitRatio=0.7, minbucket=10){
  fichier = read.csv(repertoire, header=TRUE)
  if (!is.factor(fichier[,indY])){
    fichier[,19] =  as.factor(fichier[,19])
  }
  nameY = names(fichier)[19]
  
  long = nrow(fichier)
  longTrain = round(long*splitRatio)
  indTrain = sample(1:long, longTrain)
  train.fichier = fichier[indTrain,]
  test.fichier = fichier[-indTrain,]
  
  set.seed(runif(1,0,1000))
  
  fichier.rpart = rpart(eval(parse(text=(nameY)))~.,data=train.fichier, method="class", minbucket  = minbucket)
  predict.fichier = predict(fichier.rpart, newdata = test.fichier, type="class")
  cmat_Cart = table(eval(parse(text=(paste0("test.fichier$",nameY)))), predict.fichier)
  success=0
  for (i in 1:nrow(cmat_Cart)){
    success = success +  cmat_Cart[i,i]
  }
  accuracy = success/sum(cmat_Cart)

  return(accuracy)
}

