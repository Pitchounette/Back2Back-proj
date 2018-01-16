library(rpart)
#library(rpart.plot)
#library(DAAG)
#library(caTools)


arbre_classification = function(repertoire, y,splitRatio=0.7, minbucket=20){
  fichier = read.csv(repertoire, header=TRUE)
  long = nrow(fichier)
  longTrain = round(long*splitRatio)
  indTrain = sample(1:long, longTrain)
  train.fichier = fichier[indTrain,]
  test.fichier = fichier[-indTrain,]
  
  set.seed(runif(1,0,1000))
  
  fichier.rpart = rpart(eval(parse(text=(y)))~.,data=train.fichier, method="class", minbucket  = minbucket)
  predict.fichier = predict(fichier.rpart, newdata = test.fichier, type="class")
  cmat_Cart = table(eval(parse(text=(paste0("test.fichier$",y)))), predict.fichier)
  success=0
  for (i in 1:nrow(cmat_Cart)){
    success = success +  cmat_Cart[i,i]
  }
  accuracy = success/sum(cmat_Cart)

  return(accuracy)
}
