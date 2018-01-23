library(rpart)
library(randomForest)
#library(rpart.plot)
#library(DAAG)
#library(caTools)


arbre_classification = function(repertoireTrain, repertoireTrain, indY,splitRatio=0.7, minbucket=10, transform){
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

foret_aléatoire = function(repertoireTrain,repertoireTest, indY, ntree, mtry, transform){
  train.fichier = read.csv(repertoireTrain, header=TRUE)
  test.fichier = read.csv(repertoireTest, header=TRUE)
  if (!is.factor(train.fichier[,indY]) & transform){
    train.fichier[,indY] =  as.factor(train.fichier[,indY])
    test.fichier[,indY] =  as.factor(test.fichier[,indY])
  }
  nameY = names(fichier)[indY]

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

# lycee <- read.csv2("https://www.data.gouv.fr/s/resources/indicateurs-de-resultat-des-lycees-denseignement-general-et-technologique/20160401-163749/MEN-DEPP-indicateurs-de-resultats-des-LEGT-2015.csv", 
#                    sep = ";", header = TRUE, fileEncoding = "ISO-8859-15", na.strings = c(" ", 
#                                                                                           "."))
# 
# nometab <- paste(lycee$Etablissement, lycee$Code.Etablissement)
# nometab <- gsub("LYCEE ", "", nometab)
# row.names(lycee) <- nometab
# library(dplyr)
# lycee2 <- select(lycee, Secteur.Public.PU.Privé.PR, Académie, Sructure.pédagogique.en.7.groupes, 
#                  Taux.Brut.de.Réussite.Total.séries, Taux.Réussite.Attendu.toutes.séries, 
#                  Effectif.de.seconde, Effectif.de.première, Effectif.de.terminale)
# for (i in 4:ncol(lycee2)) {
#   lycee2[, i] <- as.numeric(as.character(lycee2[, i]))
# }
# set.seed(123)
# na.action = na.roughfix # Remplace les données manquantes par les valeurs médianes. A conserver pour la suite!!!
# set.seed(123)
# 
# 
# # Si Y quant, regression. Si Y  qual classification
# long = nrow(lycee2)
# longTrain = round(long*splitRatio)
# indTrain = sample(1:long, longTrain)
# train.lycee2 = lycee2[indTrain,]
# test.lycee2 = lycee2[-indTrain,]
# 
# fit <- randomForest(Secteur.Public.PU.Privé.PR ~ ., data = train.lycee2, ntree = 2000,
#                     mtry = 4, na.action = na.roughfix)
# predict.fit = predict(fit, newdata = test.lycee2)
# fit$confusion
# table(test.lycee2$Secteur.Public.PU.Privé.PR, predict.fit)
