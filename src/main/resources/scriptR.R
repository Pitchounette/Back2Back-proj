library(rpart)
library(randomForest)


arbre_classification = function(repertoireTrain, repertoireTest, indY, minbucket, transform){
  train.fichier = read.csv(repertoireTrain, header=TRUE)
  test.fichier = read.csv(repertoireTest, header=TRUE)
  if (!is.factor(train.fichier[,indY]) & transform){
    train.fichier[,indY] =  as.factor(train.fichier[,indY])
    test.fichier[,indY] =  as.factor(test.fichier[,indY])
  }
  nameY = names(train.fichier)[indY]
  
  set.seed(runif(1,0,1000))
  
  fichier.rpart = rpart(eval(parse(text=(nameY)))~.,data=train.fichier, method="class", minbucket  = minbucket, na.action = na.roughfix)
  predict.fichier = predict(fichier.rpart, newdata = test.fichier, type="class")
  cmat_Cart = table(eval(parse(text=(paste0("test.fichier$",nameY)))), predict.fichier)
  success=0
  for (i in 1:nrow(cmat_Cart)){
    success = success +  cmat_Cart[i,i]
  }
  accuracy = success/sum(cmat_Cart)

  return(accuracy)
}

foret_aleatoire = function(repertoireTrain,repertoireTest, indY, ntree, mtry, transform){
  train.fichier = read.csv(repertoireTrain, header=TRUE)
  test.fichier = read.csv(repertoireTest, header=TRUE)
  if (!is.factor(train.fichier[,indY]) & transform){
    train.fichier[,indY] =  as.factor(train.fichier[,indY])
    test.fichier[,indY] =  as.factor(test.fichier[,indY])
  }
  nameY = names(train.fichier)[indY]

  set.seed(runif(1,0,1000))

  fichier.rpart = randomForest(eval(parse(text=(nameY))) ~ ., data = train.fichier, ntree = ntree,
                               mtry = mtry, na.action = na.roughfix)

  predict.fichier = predict(fichier.rpart, newdata = test.fichier)

  cmat_Cart = table(eval(parse(text=(paste0("test.fichier$",nameY)))), predict.fichier)
  success=0
  for (i in 1:nrow(cmat_Cart)){
    success = success +  cmat_Cart[i,i]
  }
  accuracy = success/sum(cmat_Cart)

  return(accuracy)
}
